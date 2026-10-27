package bomberman;
import java.util.Random;
/**
 * @author Mariana e Luiz
 */
public class InimigoRegular extends Inimigo implements IDestrutivel{

    private Random random = new Random();
    public InimigoRegular(int velocidade, int x, int y) {
        super(x,y,velocidade);
    }
    
    public void mover(Personagem p,Labirinto l) throws MovimentoInvalidoException{
        int novoX = x;
        int novoY = y;
        switch (random.nextInt(5)) {
            case (1)://move para baixo
                if (l.posicaoLivre(x + 1, y, l)) {
                    novoX = x + 1;
                    break;
                }
            case (2)://move pra cima
                if (l.posicaoLivre(x - 1, y, l)) {
                    novoX = x - 1;
                    break;
                }
            case (3)://move pra direita
                if (l.posicaoLivre(x, y + 1, l)) {
                    novoY = y + 1;
                    break;
                }
            case (4)://move para esquerda
                if (l.posicaoLivre(x, y - 1, l)) {
                    novoY = y - 1;
                    break;
                }
            default:
                throw new MovimentoInvalidoException("Direcao invalida!");
        }
        if ((novoX < 0 || novoX >= 20) ||
                (novoY < 0 || novoY >= 20)) {
            throw new MovimentoInvalidoException("Movimento invalido!");
        } else {
            this.x = novoX;
            this.y = novoY;
        }

    }

    @Override
    public void receberExplosao() {
    }
}
