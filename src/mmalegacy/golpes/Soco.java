package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import mmalegacy.modelo.Regiao;
import java.util.Random;

public final class Soco implements Golpe {
    @Override public ResultadoGolpe aplicar(Lutador atacante, Lutador defensor, Random sorteio) {
        return CalculoGolpe.resolver("Soco", atacante, defensor, sorteio,
                Regiao.CABECA, atacante.getSocos(), 5, 8, 0);
    }
}
