package bomberman;

/**
 * @author Mariana e Luiz
 */
public class InimigoSabotador extends Inimigo {
    private static final long serialVersionUID = 1L;
    public InimigoSabotador(int intervalo, int x, int y) {
        super(intervalo, x, y, 'S');
    }

    @Override
    public void mover(Personagem personagem, Labirinto labirinto) {
        for (int[] direcao : Labirinto.DIRECOES) {
            Bomba bomba = labirinto.bombaEm(x + direcao[0], y + direcao[1]);
            if (bomba != null && bomba.armarArmadilha()) {
                return;
            }
        }
        moverAleatoriamente(labirinto);
    }
}
