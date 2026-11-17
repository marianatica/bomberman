package bomberman;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariana e Luiz
 */
public abstract class Inimigo extends ElementoLabirinto implements IDestrutivel {
    private final int intervalo;
    private boolean vivo = true;

    protected Inimigo(int intervalo, int x, int y, char simbolo) {
        super(x, y, simbolo);
        this.intervalo = intervalo;
    }

    public boolean podeAgir(int turno) {
        return vivo && turno % intervalo == 0;
    }

    public abstract void mover(Personagem personagem, Labirinto labirinto);

    protected void moverAleatoriamente(Labirinto labirinto) {
        List<int[]> livres = new ArrayList<>();
        for (int[] direcao : Labirinto.DIRECOES) {
            if (labirinto.inimigoPodeEntrar(x + direcao[0], y + direcao[1])) {
                livres.add(direcao);
            }
        }
        if (!livres.isEmpty()) {
            int[] direcao = livres.get(labirinto.sortear(livres.size()));
            x += direcao[0];
            y += direcao[1];
        }
    }

    @Override
    public void receberExplosao() {
        vivo = false;
    }

    public boolean isVivo() {
        return vivo;
    }
}
