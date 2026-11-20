package mmalegacy.treino;

import mmalegacy.modelo.Lutador;

/** Evolução fora das lutas: +2 pontos no foco escolhido, limitada a 100. */
public final class CampTreinamento {
    public String treinar(Lutador lutador, FocoTreino foco) {
        if (lutador == null || foco == null) throw new IllegalArgumentException("Treino inválido.");
        switch (foco) {
            case BOXE -> lutador.melhorarSocos(2);
            case CHUTES -> lutador.melhorarChutes(2);
            case GRAPPLING -> lutador.melhorarGrappling(2);
            case CARDIO -> lutador.melhorarCardio(2);
        }
        lutador.recuperarEnergia();
        return "Camp concluído: " + foco + " (+2 até o limite de 100).";
    }
}
