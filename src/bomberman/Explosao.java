package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Explosao {
    public void propagar(Bomba bomba, Labirinto labirinto) {
        int x = bomba.getX();
        int y = bomba.getY();
        labirinto.atingirCelula(x, y);
        for (int[] direcao : Labirinto.DIRECOES) {
            for (int passo = 1; passo <= bomba.getAlcance(); passo++) {
                if (!labirinto.atingirCelula(x + passo * direcao[0], y + passo * direcao[1])) {
                    break;
                }
            }
        }
    }
}
