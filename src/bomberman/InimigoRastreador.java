package bomberman;

/**
 * @author Mariana e Luiz
 */

public class InimigoRastreador extends Inimigo implements IDestrutivel{

    public InimigoRastreador(int velocidade, int x, int y) {
        super(velocidade, x, y);
    }

    @Override
    public void mover(Personagem p, Labirinto l) {

    }

    @Override
    public void receberExplosao() {

    }
}
