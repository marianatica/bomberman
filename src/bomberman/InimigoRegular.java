package bomberman;

/**
 * @author Mariana e Luiz
 */
public class InimigoRegular extends Inimigo {
    private static final long serialVersionUID = 1L;
    public InimigoRegular(int intervalo, int x, int y) {
        super(intervalo, x, y, 'C');
    }

    @Override
    public void mover(Personagem personagem, Labirinto labirinto) {
        moverAleatoriamente(labirinto);
    }
}
