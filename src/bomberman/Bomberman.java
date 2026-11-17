package bomberman;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Mariana e Luiz
 */
public class Bomberman {
    private static final Path PARTIDA_SALVA = Path.of("partida-bomberman.txt");

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            System.out.println("Bem-vindo ao Bomberman!");
            System.out.println("Mapa: P jogador | X parede | # caixote | C/R/S inimigos | B bomba | + fogo");
            System.out.println("Itens e saída: A alcance | M mais bombas | E saída");
            while (true) {
                System.out.println("1: Jogar | 2: Carregar jogo salvo | Q: Sair");
                String opcao = lerOpcao(entrada);
                if (opcao == null || opcao.equals("Q")) {
                    return;
                }
                try {
                    Partida partida;
                    switch (opcao) {
                        case "1":
                            partida = criarPartida(entrada);
                            break;
                        case "2":
                            partida = Partida.carregar(PARTIDA_SALVA);
                            break;
                        default:
                            System.out.println("Opção inválida.");
                            continue;
                    }
                    if (partida != null && !jogar(entrada, partida)) {
                        return;
                    }
                } catch (NoSuchFileException e) {
                    System.out.println("Nenhuma partida salva encontrada.");
                } catch (IOException | IllegalArgumentException e) {
                    System.out.println("Não foi possível iniciar a partida: " + e.getMessage());
                }
            }
        }
    }

    private static Partida criarPartida(Scanner entrada) throws IOException {
        System.out.println("Nome do jogador:");
        if (!entrada.hasNextLine()) {
            return null;
        }
        String nome = entrada.nextLine().trim();
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Informe um nome.");
        }

        while (true) {
            System.out.println("1: Fácil | 2: Difícil | N: Voltar ao menu");
            String dificuldade = lerOpcao(entrada);
            if (dificuldade == null || dificuldade.equals("N")) {
                return null;
            }
            if (dificuldade.equals("1") || dificuldade.equals("2")) {
                boolean dificil = dificuldade.equals("2");
                return new Partida(nome, dificil, new Random().nextLong(), Labirinto.lerMapa());
            }
            System.out.println("Dificuldade inválida.");
        }
    }

    private static boolean jogar(Scanner entrada, Partida partida) {
        while (true) {
            partida.exibir();
            if (partida.terminou()) {
                System.out.println(partida.venceu() ? "Você venceu!" : "Você perdeu!");
                System.out.println("R: Reiniciar Jogo | N: Novo Jogo | Q: Sair");
                String opcao = lerOpcao(entrada);
                if (opcao == null || opcao.equals("Q")) {
                    return false;
                }
                if (opcao.equals("N")) {
                    return true;
                }
                if (opcao.equals("R")) {
                    partida.reiniciar();
                } else {
                    System.out.println("Opção inválida.");
                }
                continue;
            }

            System.out.println("W/A/S/D: mover | B: bomba | T: esperar | I: dica | G: DEBUG | Q: sair");
            String opcao = lerOpcao(entrada);
            if (opcao == null) {
                return false;
            }
            if (opcao.equals("Q")) {
                String escolha = pausar(entrada, partida);
                if (escolha == null || escolha.equals("Q") || escolha.equals("S")) {
                    return false;
                }
                if (escolha.equals("N")) {
                    return true;
                }
                if (escolha.equals("R")) {
                    partida.reiniciar();
                }
                continue;
            }

            try {
                if (opcao.length() != 1) {
                    throw new IllegalArgumentException("Comando inválido.");
                }
                String mensagem = partida.agir(opcao.charAt(0));
                if (!mensagem.isEmpty()) {
                    System.out.println(mensagem);
                }
            } catch (MovimentoInvalidoException | LimiteDeBombasAtingidoException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String pausar(Scanner entrada, Partida partida) {
        while (true) {
            System.out.println("S: Salvar e sair | Q: Encerrar | R: Reiniciar | N: Novo Jogo | V: Voltar");
            String opcao = lerOpcao(entrada);
            if (opcao == null || opcao.equals("Q") || opcao.equals("R") || opcao.equals("N") || opcao.equals("V")) {
                return opcao;
            }
            if (opcao.equals("S")) {
                try {
                    partida.salvar(PARTIDA_SALVA);
                    System.out.println("Partida salva.");
                    return opcao;
                } catch (IOException e) {
                    System.out.println("Não foi possível salvar: " + e.getMessage());
                }
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    private static String lerOpcao(Scanner entrada) {
        return entrada.hasNextLine() ? entrada.nextLine().trim().toUpperCase(Locale.ROOT) : null;
    }
}
