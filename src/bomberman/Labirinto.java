package bomberman;
import java.util.Random; 
/**
 * @author Mariana e Luiz
 */
public class Labirinto {
    private ElementoLabirinto[][] matriz;
    private int largura= 20; 
    private int altura = 20; 
    private Personagem personagem;

    public Labirinto() {
        matriz = new ElementoLabirinto[altura][largura];
    }
    
    public void gerarMapa(){
        Random random = new Random();
        personagem = new Personagem(3,1,1,1,1);
        matriz[1][1] = personagem;
    }
    
    public boolean posicaoLivre(int x, int y) {
        if (x < 0 || x >= altura || y < 0 || y >= largura) {
            return false;
        }
        return matriz[x][y] == null;
    }
    
    public void exibirMapa() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {

                if (personagem.getX() == i && personagem.getY() == j) {
                    System.out.print("P ");
                } else if (matriz[i][j] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(matriz[i][j].getSimbolo() + " ");
                }
            }
            System.out.println();
        }
    }
    
    public ElementoLabirinto obterElemento(int x, int y){
        return matriz[x][y];
    }
    
    public Personagem getPersonagem(){
        return personagem; 
    }
}
