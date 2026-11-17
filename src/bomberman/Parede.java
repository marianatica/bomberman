package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Parede extends ElementoLabirinto {
    private static final long serialVersionUID = 1L;
    
    public Parede(int x, int y) {
        super(x, y, 'X');
    }
    
    public boolean bloquearPassagem(){
        return true;
    }
    
    public boolean bloquearExplosao(){
        return true; 
    }
}
