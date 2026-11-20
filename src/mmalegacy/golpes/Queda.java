package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import mmalegacy.modelo.Regiao;
import java.util.Random;

public final class Queda implements Golpe {
    @Override public ResultadoGolpe aplicar(Lutador atacante, Lutador defensor, Random sorteio) {
        // Representação simplificada do desgaste corporal ao completar a queda.
        return CalculoGolpe.resolver("Queda", atacante, defensor, sorteio,
                Regiao.CORPO, atacante.getGrappling(), 12, 18, 16);
    }
}
