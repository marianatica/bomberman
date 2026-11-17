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
    static final int[][] DIRECOES = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private final ElementoLabirinto[][] matriz;
    private final boolean[][] fogo;
    private final List<Bomba> bombas = new ArrayList<>();
    private final Explosao explosao = new Explosao();
    private final Random random;
    private int turno;
    private boolean jogadorAtingido;
    private final List<Inimigo> inimigos = new ArrayList<>();
    private final Personagem personagem;

    public Labirinto(String[] mapa, long semente, boolean dificil, int vidas) {
        int tamanho = mapa.length;
        matriz = new ElementoLabirinto[tamanho][tamanho];
        fogo = new boolean[tamanho][tamanho];
        random = new Random(semente);
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
        Collections.shuffle(livres, random);
        for (int i = 0; i < quantidadeCaixotes; i++) {
            int[] celula = livres.get(i);
            int x = celula[0];
            int y = celula[1];
            ElementoLabirinto conteudo = switch (i) {
                case 0 -> new PortaSaida(x, y);
                case 1, 2 -> new AumentoAlcance(x, y);
                case 3, 4 -> new AumentoBombas(x, y);
                default -> null;
            };
            matriz[x][y] = new Caixote(conteudo, x, y);
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
        if (!dentroDoMapa(x, y) || matriz[x][y] instanceof Parede || matriz[x][y] instanceof Caixote) {
            return false;
        }
        Bomba bomba = bombaEm(x, y);
        return bomba == null || bomba.isArmadilha();
    }

    boolean inimigoPodeEntrar(int x, int y) {
        return posicaoLivre(x, y) && bombaEm(x, y) == null && inimigoEm(x, y) == null;
    }

    int sortear(int limite) {
        return random.nextInt(limite);
    }

    Bomba bombaEm(int x, int y) {
        for (Bomba bomba : bombas) {
            if (bomba.getX() == x && bomba.getY() == y) {
                return bomba;
            }
        }
        return null;
    }

    public void jogar(char acao) {
        Bomba nova = null;
        switch (acao) {
            case 'W', 'A', 'S', 'D' -> personagem.mover(acao, this);
            case 'B' -> nova = plantarBomba();
            case 'T' -> { } // Esperar.
            default -> throw new IllegalArgumentException("Comando inválido.");
        }

        for (boolean[] linha : fogo) {
            java.util.Arrays.fill(linha, false);
        }
        turno++;
        jogadorAtingido = false;
        int x = personagem.getX();
        int y = personagem.getY();
        Bomba armadilha = bombaEm(x, y);
        if (armadilha != null && armadilha.isArmadilha()) {
            detonar(armadilha);
        }
        if (inimigoEm(x, y) != null) {
            causarDanoAoJogador();
        }
        if (matriz[x][y] instanceof PowerUp item) {
            item.aplicar(personagem);
            matriz[x][y] = null;
        }

        for (Inimigo inimigo : inimigos) {
            if (inimigo.podeAgir(turno)) {
                inimigo.mover(personagem, this);
                if (inimigo.getX() == x && inimigo.getY() == y) {
                    causarDanoAoJogador();
                }
            }
        }
        for (Bomba bomba : new ArrayList<>(bombas)) {
            if (bomba != nova && bombas.contains(bomba) && bomba.atualizarTemporizador()) {
                detonar(bomba);
            }
        }
        inimigos.removeIf(inimigo -> !inimigo.isVivo());
    }

    private Bomba plantarBomba() {
        int ativas = 0;
        for (Bomba bomba : bombas) {
            if (!bomba.isArmadilha()) {
                ativas++;
            }
        }
        if (ativas >= personagem.getBombasSimultaneas()) {
            throw new LimiteDeBombasAtingidoException();
        }
        if (bombaEm(personagem.getX(), personagem.getY()) != null) {
            throw new IllegalArgumentException("Já há uma bomba nesta célula.");
        }
        Bomba bomba = personagem.gerarBomba();
        bombas.add(bomba);
        return bomba;
    }

    private void detonar(Bomba bomba) {
        bombas.remove(bomba);
        explosao.propagar(bomba, this);
    }

    boolean atingirCelula(int x, int y) {
        if (!dentroDoMapa(x, y) || matriz[x][y] instanceof Parede) {
            return false;
        }
        fogo[x][y] = true;

        if (matriz[x][y] instanceof Caixote caixote) {
            caixote.receberExplosao();
            if (caixote.isDestruido()) {
                matriz[x][y] = caixote.revelarConteudo();
            }
            return false;
        }
        Inimigo inimigo = inimigoEm(x, y);
        if (inimigo != null) {
            inimigo.receberExplosao();
        }
        if (personagem.getX() == x && personagem.getY() == y) {
            causarDanoAoJogador();
        }
        Bomba outra = bombaEm(x, y);
        if (outra != null) {
            detonar(outra);
            return !outra.isArmadilha();
        }
        return true;
    }

    private void causarDanoAoJogador() {
        if (!jogadorAtingido) {
            personagem.receberExplosao();
            jogadorAtingido = true;
        }
    }

    public boolean venceu() {
        return !jogadorAtingido && inimigos.isEmpty()
                && matriz[personagem.getX()][personagem.getY()] instanceof PortaSaida;
    }

    public Inimigo inimigoEm(int x, int y) {
        for (Inimigo inimigo : inimigos) {
            if (inimigo.isVivo() && inimigo.getX() == x && inimigo.getY() == y) {
                return inimigo;
            }
        }
        return null;
    }

    private int distanciaDoJogador(int x, int y) {
        return Math.abs(personagem.getX() - x) + Math.abs(personagem.getY() - y);
    }

    public boolean temPowerUpPerto(int distancia) {
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz.length; y++) {
                if (distanciaDoJogador(x, y) <= distancia
                        && matriz[x][y] instanceof Caixote caixote
                        && caixote.revelarConteudo() instanceof PowerUp) {
                    return true;
                }
            }
        }
        return false;
    }

    public void exibirMapa(boolean debug) {
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz.length; y++) {
                Inimigo inimigo = inimigoEm(x, y);
                Bomba bomba = bombaEm(x, y);
                if (fogo[x][y]) {
                    System.out.print("+ ");
                } else if (personagem.getX() == x && personagem.getY() == y) {
                    System.out.print("P ");
                } else if (bomba != null) {
                    System.out.print(bomba.getSimbolo() + " ");
                } else if (inimigo != null) {
                    System.out.print(inimigo.getSimbolo() + " ");
                } else {
                    ElementoLabirinto elemento = matriz[x][y];
                    if (debug && elemento instanceof Caixote caixote && caixote.revelarConteudo() != null) {
                        elemento = caixote.revelarConteudo();
                    }
                    System.out.print(elemento == null ? ". " : elemento.getSimbolo() + " ");
                }
            }
            System.out.println();
        }
    }

    public Personagem getPersonagem() {
        return personagem;
    }
}
