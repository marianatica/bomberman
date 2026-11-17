package bomberman;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Mariana e Luiz
 */
public class Labirinto {
    private final ElementoLabirinto[][] matriz;
    private final List<Inimigo> inimigos = new ArrayList<>();
    private final Personagem personagem;

    public Labirinto(String[] mapa, long semente, boolean dificil, int vidas) {
        int tamanho = mapa.length;
        matriz = new ElementoLabirinto[tamanho][tamanho];
        List<int[]> livres = new ArrayList<>();
        Personagem jogador = null;
        for (int x = 0; x < tamanho; x++) {
            if (mapa[x].length() != tamanho) {
                throw new IllegalArgumentException("O mapa deve ser quadrado.");
            }
            for (int y = 0; y < tamanho; y++) {
                switch (mapa[x].charAt(y)) {
                    case 'X':
                        matriz[x][y] = new Parede(x, y);
                        break;
                    case 'P':
                        if (jogador != null) {
                            throw new IllegalArgumentException("O mapa deve ter um único jogador.");
                        }
                        jogador = new Personagem(vidas, 1, 1, x, y);
                        break;
                    case '.':
                        livres.add(new int[]{x, y});
                        break;
                    default:
                        throw new IllegalArgumentException("Símbolo desconhecido no mapa.");
                }
            }
        }
        if (jogador == null) {
            throw new IllegalArgumentException("Posição inicial do jogador ausente.");
        }
        personagem = jogador;
        livres.removeIf(celula -> distanciaDoJogador(celula[0], celula[1]) <= 2);
        int quantidadeInimigos = dificil ? 4 : 2;
        int quantidadeCaixotes = 18;
        if (livres.size() < quantidadeCaixotes + quantidadeInimigos) {
            throw new IllegalArgumentException("O mapa tem poucas células livres.");
        }
        Collections.shuffle(livres, new Random(semente));
        for (int i = 0; i < quantidadeCaixotes; i++) {
            int[] celula = livres.get(i);
            matriz[celula[0]][celula[1]] = new Caixote(false, null, celula[0], celula[1]);
        }
        int[] comum = livres.get(quantidadeCaixotes);
        int[] rastreador = livres.get(quantidadeCaixotes + 1);
        inimigos.add(new InimigoRegular(2, comum[0], comum[1]));
        inimigos.add(new InimigoRastreador(1, rastreador[0], rastreador[1]));
        if (dificil) {
            int[] sabotador = livres.get(quantidadeCaixotes + 2);
            int[] outro = livres.get(quantidadeCaixotes + 3);
            inimigos.add(new InimigoSabotador(1, sabotador[0], sabotador[1]));
            inimigos.add(new InimigoRegular(2, outro[0], outro[1]));
        }
    }

    public static String[] lerMapa() throws IOException {
        InputStream arquivo = Labirinto.class.getResourceAsStream("mapa.txt");
        if (arquivo == null) {
            throw new IOException("Arquivo mapa.txt não encontrado.");
        }
        List<String> linhas = new ArrayList<>();
        try (Scanner leitor = new Scanner(arquivo, StandardCharsets.UTF_8.name())) {
            while (leitor.hasNextLine()) {
                linhas.add(leitor.nextLine());
            }
            if (leitor.ioException() != null) {
                throw leitor.ioException();
            }
        }
        return linhas.toArray(new String[0]);
    }

    public boolean dentroDoMapa(int x, int y) {
        return x >= 0 && x < matriz.length && y >= 0 && y < matriz.length;
    }

    public boolean posicaoLivre(int x, int y) {
        return dentroDoMapa(x, y) && matriz[x][y] == null && inimigoEm(x, y) == null;
    }

    public Inimigo inimigoEm(int x, int y) {
        for (Inimigo inimigo : inimigos) {
            if (inimigo.getX() == x && inimigo.getY() == y) {
                return inimigo;
            }
        }
        return null;
    }

    private int distanciaDoJogador(int x, int y) {
        return Math.abs(personagem.getX() - x) + Math.abs(personagem.getY() - y);
    }

    public void exibirMapa() {
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz.length; y++) {
                Inimigo inimigo = inimigoEm(x, y);
                if (personagem.getX() == x && personagem.getY() == y) {
                    System.out.print("P ");
                } else if (inimigo != null) {
                    System.out.print(inimigo.getSimbolo() + " ");
                } else {
                    ElementoLabirinto elemento = matriz[x][y];
                    System.out.print(elemento == null ? ". " : elemento.getSimbolo() + " ");
                }
            }
            System.out.println();
        }
    }

    public ElementoLabirinto obterElemento(int x, int y) {
        return matriz[x][y];
    }

    public Personagem getPersonagem() {
        return personagem;
    }
}
