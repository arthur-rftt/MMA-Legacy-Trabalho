package mmalegacy.combate;

import mmalegacy.golpes.*;
import mmalegacy.modelo.Lutador;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** OBSERVER — Subject. Simula uma luta curta de até 3 rounds de 4 trocas. */
public final class Combate {
    public static final int TOTAL_ROUNDS = 3;
    public static final int TROCAS_POR_ROUND = 4;
    private final Lutador jogador;
    private final Lutador adversario;
    private final Random sorteio;
    private final List<ObservadorCombate> observadores = new ArrayList<>();
    private final FabricaGolpe[] golpesNpc = {
            new FabricaSoco(), new FabricaChute(), new FabricaQueda()
    };
    private int trocas;
    private boolean encerrado;
    private String resultado = "";

    public Combate(Lutador jogador, Lutador adversario, Random sorteio) {
        if (jogador == null || adversario == null || sorteio == null)
            throw new IllegalArgumentException("Lutadores e sorteio são obrigatórios.");
        this.jogador = jogador;
        this.adversario = adversario;
        this.sorteio = sorteio;
        jogador.restaurarParaLuta();
        adversario.restaurarParaLuta();
    }

    public void adicionarObservador(ObservadorCombate observador) {
        if (observador != null && !observadores.contains(observador)) observadores.add(observador);
    }
    public void removerObservador(ObservadorCombate observador) { observadores.remove(observador); }
    private void avisar(String mensagem) {
        EventoCombate evento = new EventoCombate(getRound(), mensagem);
        // Cópia defensiva permite alterar inscrições durante a notificação.
        for (ObservadorCombate observador : List.copyOf(observadores)) observador.aoOcorrer(evento);
    }
    public void iniciar() { avisar("Soa o gongo! " + jogador.getNome() + " x " + adversario.getNome()); }

    public void proximaTroca(FabricaGolpe ataqueJogador) {
        if (encerrado) throw new IllegalStateException("A luta já terminou.");
        if (ataqueJogador == null) throw new IllegalArgumentException("Escolha um golpe.");

        int roundAnterior = getRound();
        aplicar(jogador, adversario, ataqueJogador);
        if (avaliarNocaute()) return;

        aplicar(adversario, jogador, golpesNpc[sorteio.nextInt(golpesNpc.length)]);
        if (avaliarNocaute()) return;

        trocas++;
        if (trocas >= TOTAL_ROUNDS * TROCAS_POR_ROUND) {
            decidirPorPontos();
        } else if (getRound() > roundAnterior) {
            avisar("--- Início do round " + getRound() + " ---");
        }
    }

    private void aplicar(Lutador atacante, Lutador defensor, FabricaGolpe fabrica) {
        ResultadoGolpe resultadoGolpe = fabrica.executar(atacante, defensor, sorteio);
        if (resultadoGolpe.acertou()) {
            avisar(atacante.getNome() + " acertou " + resultadoGolpe.nome() +
                    " em " + defensor.getNome() + " (" + resultadoGolpe.dano() + " dano).");
        } else {
            avisar(atacante.getNome() + " tentou " + resultadoGolpe.nome() + ", mas errou.");
        }
    }

    private boolean avaliarNocaute() {
        if (adversario.estaNocauteado()) {
            encerrar(jogador, adversario, adversario.getCabeca() == 0 ? "KO" : "TKO no corpo");
            return true;
        }
        if (jogador.estaNocauteado()) {
            encerrar(adversario, jogador, jogador.getCabeca() == 0 ? "KO" : "TKO no corpo");
            return true;
        }
        return false;
    }

    private void decidirPorPontos() {
        int pontosJogador = jogador.pontosParaDecisao();
        int pontosAdversario = adversario.pontosParaDecisao();
        if (pontosJogador == pontosAdversario) {
            // Não há empate neste protótipo; desempate por energia restante.
            if (jogador.getEnergia() >= adversario.getEnergia())
                encerrar(jogador, adversario, "decisão apertada");
            else encerrar(adversario, jogador, "decisão apertada");
        } else if (pontosJogador > pontosAdversario) {
            encerrar(jogador, adversario, "decisão dos juízes");
        } else encerrar(adversario, jogador, "decisão dos juízes");
    }

    private void encerrar(Lutador vencedor, Lutador perdedor, String metodo) {
        encerrado = true;
        vencedor.registrarVitoria();
        perdedor.registrarDerrota();
        resultado = vencedor.getNome() + " venceu por " + metodo + "!";
        avisar("RESULTADO: " + resultado + " Cartel: " + jogador.cartel());
        // Intencional: a luta NÃO concede pontos de atributos.
        // Atributos evoluem somente por CampTreinamento.
    }

    public Lutador getJogador() { return jogador; }
    public Lutador getAdversario() { return adversario; }
    public int getRound() { return Math.min(TOTAL_ROUNDS, 1 + trocas / TROCAS_POR_ROUND); }
    public int getTrocas() { return trocas; }
    public boolean terminou() { return encerrado; }
    public String getResultado() { return resultado; }
}
