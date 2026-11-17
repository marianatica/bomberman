package bomberman;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Partida {
    private static final int MAX_DICAS = 3;
    private static final String VERSAO = "BOMBERMAN-1";

    private final String nome;
    private final boolean dificil;
    private final long semente;
    private final String[] mapa;
    private final StringBuilder historico = new StringBuilder();
    private Labirinto labirinto;
    private int dicasRestantes;
    private boolean debug;

    public Partida(String nome, boolean dificil, long semente, String[] mapa) {
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do jogador.");
        }
        this.nome = nome;
        this.dificil = dificil;
        this.semente = semente;
        this.mapa = mapa;
        reiniciar();
    }

    public void reiniciar() {
        labirinto = new Labirinto(mapa, semente, dificil, 3);
        historico.setLength(0);
        dicasRestantes = MAX_DICAS;
        debug = false;
    }

    public String agir(char acao) {
        if (terminou()) {
            throw new IllegalArgumentException("A partida já terminou.");
        }
        if (acao == 'I') {
            if (dicasRestantes == 0) {
                throw new IllegalArgumentException("Você já usou as três dicas.");
            }
            dicasRestantes--;
            historico.append(acao);
            return labirinto.temPowerUpPerto(3)
                    ? "Há um power-up escondido a até três células."
                    : "Nenhum power-up escondido a até três células.";
        }
        if (acao == 'G') {
            debug = !debug;
            historico.append(acao);
            return debug ? "Modo DEBUG ativado." : "Modo DEBUG desativado.";
        }

        int vidasAnteriores = labirinto.getPersonagem().getVidas();
        labirinto.jogar(acao);
        historico.append(acao);
        int vidas = labirinto.getPersonagem().getVidas();
        if (vidas > 0 && vidas < vidasAnteriores) {
            labirinto = new Labirinto(mapa, semente, dificil, vidas);
            return "Você perdeu uma vida. A fase foi reiniciada.";
        }
        return "";
    }

    public boolean terminou() {
        return labirinto.venceu() || labirinto.getPersonagem().getVidas() == 0;
    }

    public boolean venceu() {
        return labirinto.venceu();
    }

    public void exibir() {
        labirinto.exibirMapa(debug);
        Personagem jogador = labirinto.getPersonagem();
        System.out.printf("%s | %s | Vidas: %d | Bombas simultâneas: %d | Raio: %d | Dicas: %d%n",
                nome, dificil ? "Difícil" : "Fácil", jogador.getVidas(),
                jogador.getBombasSimultaneas(), jogador.getAlcanceExplosao(), dicasRestantes);
    }

    public void salvar(Path destino) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add(VERSAO);
        linhas.add(nome);
        linhas.add(dificil ? "D" : "F");
        linhas.add(Long.toString(semente));
        linhas.add(historico.toString());
        linhas.addAll(Arrays.asList(mapa));

        Path arquivo = destino.toAbsolutePath();
        Path temporario = Files.createTempFile(arquivo.getParent(), "bomberman-", ".tmp");
        try {
            Files.write(temporario, linhas, StandardCharsets.UTF_8);
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    public static Partida carregar(Path origem) throws IOException {
        List<String> linhas = Files.readAllLines(origem, StandardCharsets.UTF_8);
        if (linhas.size() < 6 || !VERSAO.equals(linhas.get(0))) {
            throw new IOException("Formato de partida inválido.");
        }
        String dificuldade = linhas.get(2);
        if (!dificuldade.equals("F") && !dificuldade.equals("D")) {
            throw new IOException("Dificuldade da partida inválida.");
        }
        try {
            long semente = Long.parseLong(linhas.get(3));
            String[] mapa = linhas.subList(5, linhas.size()).toArray(new String[0]);
            Partida partida = new Partida(linhas.get(1), dificuldade.equals("D"), semente, mapa);
            // A semente e as ações reconstroem o tabuleiro, incluindo posições e fusíveis.
            for (char acao : linhas.get(4).toCharArray()) {
                partida.agir(acao);
            }
            return partida;
        } catch (IllegalArgumentException | MovimentoInvalidoException | LimiteDeBombasAtingidoException e) {
            throw new IOException("Dados da partida inválidos: " + e.getMessage(), e);
        }
    }
}
