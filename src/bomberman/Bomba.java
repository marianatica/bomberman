package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Bomba extends ElementoLabirinto {
    
    private int alcance;
    private int tempoRestante;
    private boolean ativa;

    public Bomba(int alcance, int tempoRestante, boolean ativa, int x, int y, char simbolo) {
        super(x, y, simbolo);
        this.alcance = alcance;
        this.tempoRestante = tempoRestante;
        this.ativa = ativa;
    }
    
    public void iniciarContagem(){
        
    }
    
    public void atualizarTemporizador(){
        
    }
    
    public void explodir(){
        
    }
    
    public void desarmar(){
        
    }
}
