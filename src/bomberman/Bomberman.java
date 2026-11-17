package bomberman;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Mariana e Luiz
 */
public class Bomberman {
    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            Labirinto labirinto = new Labirinto(Labirinto.lerMapa(), new Random().nextLong(), false, 3);
            while (true) {
                labirinto.exibirMapa();
                System.out.println("Mover: W/A/S/D | Q: sair");
                if (!entrada.hasNext()) {
                    return;
                }
                char direcao = Character.toUpperCase(entrada.next().charAt(0));
                if (direcao == 'Q') {
                    return;
                }
                try {
                    labirinto.getPersonagem().mover(direcao, labirinto);
                } catch (MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Erro ao carregar o mapa: " + e.getMessage());
        }
    }
}
