package bomberman;

/**
 * @author Mariana e Luiz
 */
public class InimigoRastreador extends Inimigo {
    public InimigoRastreador(int intervalo, int x, int y) {
        super(intervalo, x, y, 'R');
    }

    @Override
    public void mover(Personagem personagem, Labirinto labirinto) {
        if (x != personagem.getX() && y != personagem.getY()) {
            moverAleatoriamente(labirinto);
            return;
        }

        int dx = Integer.compare(personagem.getX(), x);
        int dy = Integer.compare(personagem.getY(), y);
        int coluna = y + dy;
        int linha = x + dx;
        while (linha != personagem.getX() || coluna != personagem.getY()) {
            if (!labirinto.inimigoPodeEntrar(linha, coluna)) {
                moverAleatoriamente(labirinto);
                return;
            }
            linha += dx;
            coluna += dy;
        }
        if (labirinto.inimigoPodeEntrar(linha, coluna)) {
            x += dx;
            y += dy;
        } else {
            moverAleatoriamente(labirinto);
        }
    }
}
