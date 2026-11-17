package bomberman;

/**
 * @author Mariana e Luiz
 */
public abstract class PowerUp extends ElementoLabirinto {
    protected PowerUp(int x, int y, char simbolo) {
        super(x, y, simbolo);
    }

    public abstract void aplicar(Personagem personagem);
}
