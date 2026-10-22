package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Personagem extends ElementoLabirinto{
    private int vidas;
    private int bombasSimultaneas;
    private int alcanceExplosao; 

    public Personagem(int vidas, int bombasSimultaneas, int alcanceExplosao, int x, int y, char simbolo) {
        super(x, y, simbolo);
        this.vidas = vidas;
        this.bombasSimultaneas = bombasSimultaneas;
        this.alcanceExplosao = alcanceExplosao;
    }
    
    public void mover(char direcao){
        
    }
    
    public void gerarBomba(){
        
    }
    
    public void receberDano(){
        
    }
}
