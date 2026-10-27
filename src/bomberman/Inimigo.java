package bomberman;

/**
 * @author Mariana e Luiz
 */
public abstract class Inimigo extends ElementoLabirinto{
    protected int velocidade;


    public Inimigo(int velocidade, int x, int y) {
        super(x , y, 'I');
        this.velocidade = velocidade;
    }
    
    public abstract void mover(Personagem p, Labirinto l);
      
}
