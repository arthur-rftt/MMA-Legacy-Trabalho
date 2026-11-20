package mmalegacy.golpes;

import mmalegacy.modelo.Regiao;

public record ResultadoGolpe(String nome, boolean acertou, Regiao regiao, int dano) {}
