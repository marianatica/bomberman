package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Personagem extends ElementoLabirinto implements IDestrutivel {
    private static final long serialVersionUID = 1L;
    private int vidas;
    private int bombasSimultaneas;
    private int alcanceExplosao; 

    public Personagem(int vidas, int bombasSimultaneas, int alcanceExplosao, int x, int y) {
        super(x, y, 'P');
        this.vidas = vidas;
        this.bombasSimultaneas = bombasSimultaneas;
        this.alcanceExplosao = alcanceExplosao;
    }

    public int getVidas() {
        return vidas;
    }

    public int getBombasSimultaneas() {
        return bombasSimultaneas;
    }

    public int getAlcanceExplosao() {
        return alcanceExplosao;
    }
    
    public void mover(char direcao, Labirinto labirinto) throws MovimentoInvalidoException {
        int novoX = x;
        int novoY = y;

        switch (Character.toUpperCase(direcao)) {
            case 'W':
                novoX--;
                break;
            case 'S':
                novoX++;
                break;
            case 'A':
                novoY--;
                break;
            case 'D':
                novoY++;
                break;
            default:
                throw new MovimentoInvalidoException("Direcao invalida!");
        }

        if (!labirinto.posicaoLivre(novoX, novoY)) {
            throw new MovimentoInvalidoException("Movimento invalido!");
        } else {
            this.x = novoX;
            this.y = novoY;
        }
    }
    
    public Bomba gerarBomba(){
        return new Bomba(alcanceExplosao, x, y);
    }
    
    public void aumentarAlcance() {
        alcanceExplosao++;
    }

    public void aumentarLimiteDeBombas() {
        bombasSimultaneas++;
    }

    public void receberDano(){
        if(vidas > 0){
            vidas--; 
        }
    }
    
    @Override
    public void receberExplosao() {
        receberDano();
    }
}
