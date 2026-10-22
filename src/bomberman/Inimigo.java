package bomberman;

/**
 * @author Mariana e Luiz
 */
public abstract class Inimigo {
    protected int velocidade; 

    public Inimigo(int velocidade) {
        this.velocidade = velocidade;
    }
    
    public abstract void mover();
      
}
