package bomberman;

/**
 * @author Mariana e Luiz
 */
public class AumentoAlcance extends PowerUp {
    public AumentoAlcance(int x, int y) {
        super(x, y, 'A');
    }

    @Override
    public void aplicar(Personagem personagem) {
        personagem.aumentarAlcance();
    }
}
