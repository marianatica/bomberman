package bomberman;

/**
 * @author Mariana e Luiz
 */
public class AumentoBombas extends PowerUp {
    public AumentoBombas(int x, int y) {
        super(x, y, 'M');
    }

    @Override
    public void aplicar(Personagem personagem) {
        personagem.aumentarLimiteDeBombas();
    }
}
