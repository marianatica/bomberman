package bomberman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_DICAS = 3;

    private final String nome;
    private final boolean dificil;
    private final long semente;
    private final String[] mapa;
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
            return labirinto.temPowerUpPerto(3)
                    ? "Há um power-up escondido a até três células."
                    : "Nenhum power-up escondido a até três células.";
        }
        if (acao == 'G') {
            debug = !debug;
            return debug ? "Modo DEBUG ativado." : "Modo DEBUG desativado.";
        }

        int vidasAnteriores = labirinto.getPersonagem().getVidas();
        labirinto.jogar(acao);
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

    public void salvar(File destino) throws IOException {
        File arquivo = destino.getAbsoluteFile();
        File temporario = File.createTempFile("bomberman-", ".tmp", arquivo.getParentFile());
        try {
            try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(temporario))) {
                saida.writeObject(this);
            }

            // Guarda a versão anterior até terminar a troca do arquivo no Windows.
            File anterior = copiaAnterior(arquivo);
            if (arquivo.exists()) {
                if ((anterior.exists() && !anterior.delete()) || !arquivo.renameTo(anterior)) {
                    throw new IOException("Não foi possível preservar a partida anterior.");
                }
            }
            if (!temporario.renameTo(arquivo)) {
                throw new IOException("Não foi possível substituir o arquivo da partida.");
            }
            anterior.delete();
        } finally {
            temporario.delete();
        }
    }

    public static Partida carregar(File origem) throws IOException {
        File arquivo = origem.exists() ? origem : copiaAnterior(origem);
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object objeto = entrada.readObject();
            if (!(objeto instanceof Partida)) {
                throw new IOException("O arquivo não contém uma partida do Bomberman.");
            }
            return (Partida) objeto;
        } catch (ClassNotFoundException e) {
            throw new IOException("O arquivo contém uma classe desconhecida.", e);
        }
    }

    private static File copiaAnterior(File arquivo) {
        return new File(arquivo.getPath() + ".bak");
    }

    static Partida carregarFormatoAnterior(File origem) throws IOException {
        try (BufferedReader entrada = new BufferedReader(new FileReader(origem, StandardCharsets.UTF_8))) {
            if (!"BOMBERMAN-1".equals(entrada.readLine())) {
                throw new IOException("Formato de partida inválido.");
            }
            String nome = entrada.readLine();
            String dificuldade = entrada.readLine();
            String semente = entrada.readLine();
            String acoes = entrada.readLine();
            ArrayList<String> linhas = new ArrayList<>();
            String linha;
            while ((linha = entrada.readLine()) != null) {
                linhas.add(linha);
            }
            if (nome == null || acoes == null || linhas.isEmpty()
                    || (!"F".equals(dificuldade) && !"D".equals(dificuldade))) {
                throw new IOException("Dados da partida antiga incompletos.");
            }

            // Só os arquivos da versão anterior precisam reconstruir a partida pelo histórico.
            Partida partida = new Partida(nome, "D".equals(dificuldade), Long.parseLong(semente),
                    linhas.toArray(new String[0]));
            for (int i = 0; i < acoes.length(); i++) {
                partida.agir(acoes.charAt(i));
            }
            return partida;
        } catch (IllegalArgumentException | MovimentoInvalidoException | LimiteDeBombasAtingidoException e) {
            throw new IOException("Dados da partida antiga inválidos: " + e.getMessage(), e);
        }
    }
}
