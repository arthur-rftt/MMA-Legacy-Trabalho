package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import mmalegacy.modelo.Regiao;
import java.util.Random;

/** Algoritmo simplificado (não pretende reproduzir o jogo original por completo). */
final class CalculoGolpe {
    private CalculoGolpe() {}

    static ResultadoGolpe resolver(String nome, Lutador atacante, Lutador defensor,
                                   Random sorteio, Regiao regiao, int tecnica, int custo,
                                   int danoBase, int dificuldade) {
        // Energia baixa reduz precisão; defesa alta dificulta acertos.
        int fadiga = atacante.getEnergia() < 20 ? 18 : 0;
        int chance = Math.max(15, Math.min(92,
                58 + (tecnica - defensor.getDefesa()) / 2 - dificuldade - fadiga));
        atacante.gastarEnergia(custo);
        boolean acertou = sorteio.nextInt(100) < chance;
        int dano = acertou ? Math.max(3, danoBase + tecnica / 9 - defensor.getDefesa() / 12) : 0;
        if (acertou) defensor.receberDano(regiao, dano);
        return new ResultadoGolpe(nome, acertou, regiao, dano);
    }
}
