package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Caixote extends ElementoLabirinto{
    
    private boolean destruido;
    private PowerUp conteudo; 

    public Caixote(boolean destruido, PowerUp conteudo, int x, int y) {
        super(x, y, '#');
        this.destruido = false;
        this.conteudo = conteudo;
    }
    
    public void quebrar(){
        destruido = true;
    }

    public boolean isDestruido() {
        return destruido;
    }
    
    public PowerUp revelarConteudo(){
        return conteudo; 
    }
    
    public void setConteudo(PowerUp conteudo) {
        this.conteudo = conteudo;
    }
    
    @Override
    public void receberExplosao() {
        quebrar();
    }
}
