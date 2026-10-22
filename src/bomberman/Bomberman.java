package bomberman;
import java.util.Scanner;
/**
 * @author Mariana e Luiz
 */
public class Bomberman {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Personagem personagem = new Personagem(3, 1, 1, 1, 1);
        Labirinto labirinto = new Labirinto();
        labirinto.gerarMapa();
        
        char direcao;

        do {
            labirinto.exibirMapa();

            System.out.println("Mover: W/A/S/D | Q: sair");
            direcao = sc.next().charAt(0);

            try {
                if (Character.toUpperCase(direcao) != 'Q') {
                    labirinto.getPersonagem().mover(direcao);
                }
            } catch (MovimentoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (Character.toUpperCase(direcao) != 'Q');

        sc.close();
    }
}