package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import java.util.Random;

/** Produto do Factory Method: implementações concretas têm comportamentos próprios. */
public interface Golpe {
    ResultadoGolpe aplicar(Lutador atacante, Lutador defensor, Random sorteio);
}
