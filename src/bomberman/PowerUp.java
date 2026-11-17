package bomberman;

/**
 * @author Mariana e Luiz
 */
public abstract class PowerUp extends ElementoLabirinto {
    private static final long serialVersionUID = 1L;
    protected PowerUp(int x, int y, char simbolo) {
        super(x, y, simbolo);
    }

    public abstract void aplicar(Personagem personagem);
}
