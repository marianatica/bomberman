package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Bomba extends ElementoLabirinto {
    private static final long serialVersionUID = 1L;
    private final int alcance;
    private int tempoRestante = 5;
    private boolean armadilha;

    public Bomba(int alcance, int x, int y) {
        super(x, y, 'B');
        this.alcance = alcance;
    }

    public boolean atualizarTemporizador() {
        return !armadilha && --tempoRestante == 0;
    }

    public boolean armarArmadilha() {
        if (armadilha || tempoRestante < 3) {
            return false;
        }
        armadilha = true;
        simbolo = '#';
        return true;
    }

    public int getAlcance() {
        return alcance;
    }

    public boolean isArmadilha() {
        return armadilha;
    }
}
