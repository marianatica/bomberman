package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Caixote extends ElementoLabirinto{
    
    private boolean destruido;
    private PowerUp conteudo; 

    public Caixote(boolean destruido, PowerUp conteudo, int x, int y, char simbolo) {
        super(x, y, simbolo);
        this.destruido = destruido;
        this.conteudo = conteudo;
    }
    
    public void quebrar(){
        
    }
    
    public PowerUp revelarConteudo(){
        
    }
}
