package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Caixote extends ElementoLabirinto implements IDestrutivel {
    private static final long serialVersionUID = 1L;
    private final ElementoLabirinto conteudo;
    private boolean destruido;

    public Caixote(ElementoLabirinto conteudo, int x, int y) {
        super(x, y, '#');
        this.conteudo = conteudo;
    }

    public boolean isDestruido() {
        return destruido;
    }

    public ElementoLabirinto revelarConteudo() {
        return conteudo;
    }

    @Override
    public void receberExplosao() {
        destruido = true;
    }
}
