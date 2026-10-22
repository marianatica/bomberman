package bomberman;

/**
 * @author Mariana e Luiz
 */
public class Explosao {
    private int alcance; 

    public Explosao(int alcance) {
        this.alcance = alcance;
    }
    
    public void propagar(){
        System.out.println("Explosao! Alcance: " + alcance);
    }
    
    public void atingirElemento(IDestrutivel e){
        if (e != null){
            e.receberExplosao();
        }
    }
}
