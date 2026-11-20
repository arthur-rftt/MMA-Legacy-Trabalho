package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import mmalegacy.modelo.Regiao;
import java.util.Random;

public final class Chute implements Golpe {
    @Override public ResultadoGolpe aplicar(Lutador atacante, Lutador defensor, Random sorteio) {
        return CalculoGolpe.resolver("Chute no corpo", atacante, defensor, sorteio,
                Regiao.CORPO, atacante.getChutes(), 9, 15, 10);
    }
}
