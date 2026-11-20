package mmalegacy;

import mmalegacy.modelo.*;
import mmalegacy.golpes.*;
import mmalegacy.combate.*;
import mmalegacy.treino.*;
import java.util.Random;

/** Testes sem JUnit: execute java -ea -cp out mmalegacy.Testes */
public final class Testes {
    public static void main(String[] args) {
        testarBuilder();
        testarFactoryMethod();
        testarObserverECombate();
        System.out.println("OK: 3 padrões + regras de treino e combate verificados.");
    }

    private static Lutador lutador(String nome) {
        return new LutadorBuilder().nome(nome).pais("Brasil").categoria("Peso-leve")
                .socos(70).chutes(70).grappling(70).defesa(60).cardio(90).build();
    }

    private static void testarBuilder() {
        Lutador l = new LutadorBuilder().nome("Atleta").socos(75).cardio(95).build();
        assert l.getNome().equals("Atleta");
        assert l.getSocos() == 75 && l.getCardio() == 95;
        boolean falhou = false;
        try { new LutadorBuilder().nome("  ").build(); }
        catch (IllegalArgumentException esperado) { falhou = true; }
        assert falhou : "Nome vazio deveria ser rejeitado";
    }

    private static void testarFactoryMethod() {
        Lutador a = lutador("A"), b = lutador("B");
        FabricaGolpe soco = new FabricaSoco();
        FabricaGolpe chute = new FabricaChute();
        FabricaGolpe queda = new FabricaQueda();
        assert soco.executar(a, b, new Random(1)).nome().equals("Soco");
        assert chute.executar(a, b, new Random(1)).nome().equals("Chute no corpo");
        assert queda.executar(a, b, new Random(1)).nome().equals("Queda");
    }

    private static void testarObserverECombate() {
        Lutador jogador = lutador("Jogador"), rival = lutador("Rival");
        Combate luta = new Combate(jogador, rival, new Random(17));
        HistoricoCombate historico = new HistoricoCombate();
        final int[] notificacoes = {0};
        ObservadorCombate contador = e -> notificacoes[0]++;
        luta.adicionarObservador(historico);
        luta.adicionarObservador(contador);
        luta.iniciar();
        assert notificacoes[0] == 1 && historico.getEventos().size() == 1;
        luta.removerObservador(contador);
        int socosAntes = jogador.getSocos();
        int cardioAntes = jogador.getCardio();
        while (!luta.terminou()) luta.proximaTroca(new FabricaSoco());
        assert !luta.getResultado().isBlank();
        assert jogador.getVitorias() + jogador.getDerrotas() == 1;
        assert jogador.getSocos() == socosAntes && jogador.getCardio() == cardioAntes
                : "Luta não pode conceder atributo automaticamente";
        assert historico.getEventos().size() > 2;
        assert notificacoes[0] == 1 : "Observer removido não pode continuar recebendo eventos";
        new CampTreinamento().treinar(jogador, FocoTreino.BOXE);
        assert jogador.getSocos() == socosAntes + 2;
    }
}
