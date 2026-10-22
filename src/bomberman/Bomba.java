package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Bomba extends ElementoLabirinto {
    
    private int alcance;
    private int tempoRestante;
    private boolean ativa;

    public Bomba(int alcance, int tempoRestante, boolean ativa, int x, int y) {
        super(x, y, 'B');
        this.alcance = alcance;
        this.tempoRestante = 5;
        this.ativa = false;
    }
    
    public void iniciarContagem(){
        ativa = true;
    }
    
    public void atualizarTemporizador(){
        if( ativa && tempoRestante > 0){
            tempoRestante--;
        }
    }
    
    public void explodir(){
        
    }
    
    public void desarmar(){
        ativa = false; 
    }

    public int getAlcance() {
        return alcance;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }

    public boolean isAtiva() {
        return ativa;
    }
    
    
}
