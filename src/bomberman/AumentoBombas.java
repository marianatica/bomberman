package bomberman;

/**
 * @author Mariana e Luiz
 */
public class AumentoBombas extends PowerUp {
    private static final long serialVersionUID = 1L;
    public AumentoBombas(int x, int y) {
        super(x, y, 'M');
    }

    @Override
    public void aplicar(Personagem personagem) {
        personagem.aumentarLimiteDeBombas();
    }
}
