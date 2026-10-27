package bomberman;

/**
 * @author Mariana e Luiz
 */
public class InimigoSabotador extends Inimigo implements IDestrutivel {

    public InimigoSabotador(int velocidade, int x, int y) {
        super(x,y,velocidade);
    }
    
    public void sabotarBomba(Bomba b){
    }
    @Override
    public void mover(Personagem p,Labirinto l) {

    }
    @Override
    public void receberExplosao() {

    }
}
