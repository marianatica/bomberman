package bomberman;

/**
 * @author Mariana e Luiz
 */
public abstract class ElementoLabirinto {
    protected int x; 
    protected int y;
    protected char simbolo; 

    public ElementoLabirinto(int x, int y, char simbolo) {
        this.x = x;
        this.y = y;
        this.simbolo = simbolo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSimbolo() {
        return simbolo;
    }
    
}
