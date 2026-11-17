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
            String[] mapa = Labirinto.lerMapa();
            long semente = new Random().nextLong();
            Labirinto labirinto = new Labirinto(mapa, semente, false, 3);
            while (true) {
                labirinto.exibirMapa();
                Personagem jogador = labirinto.getPersonagem();
                System.out.printf("Vidas: %d | Bombas simultâneas: %d | Raio: %d%n",
                        jogador.getVidas(), jogador.getBombasSimultaneas(), jogador.getAlcanceExplosao());
                System.out.println("W/A/S/D: mover | B: bomba | T: esperar | Q: sair");
                if (!entrada.hasNext()) {
                    return;
                }
                char acao = Character.toUpperCase(entrada.next().charAt(0));
                if (acao == 'Q') {
                    return;
                }
                int vidas = jogador.getVidas();
                try {
                    labirinto.jogar(acao);
                } catch (MovimentoInvalidoException | LimiteDeBombasAtingidoException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                if (labirinto.venceu() || jogador.getVidas() == 0) {
                    labirinto.exibirMapa();
                    System.out.println(labirinto.venceu() ? "Você venceu!" : "Fim de jogo.");
                    return;
                }
                if (jogador.getVidas() < vidas) {
                    labirinto.exibirMapa();
                    System.out.println("Você perdeu uma vida. A fase será reiniciada.");
                    labirinto = new Labirinto(mapa, semente, false, jogador.getVidas());
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Erro ao carregar o mapa: " + e.getMessage());
        }
    }
}
