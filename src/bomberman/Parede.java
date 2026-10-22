package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Parede extends ElementoLabirinto {
    
    public Parede(int x, int y) {
        super(x, y, '█');
    }
    
    public boolean bloquearPassagem(){
        return true;
    }
    
    public boolean bloquearExplosao(){
        return true; 
    }
}
