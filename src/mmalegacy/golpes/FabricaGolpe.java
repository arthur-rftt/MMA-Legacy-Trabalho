package mmalegacy.golpes;

import mmalegacy.modelo.Lutador;
import java.util.Random;

/** FACTORY METHOD — Creator. A execução usa o produto criado por subclasses. */
public abstract class FabricaGolpe {
    protected abstract Golpe criarGolpe(); // Factory Method: subclasses decidem o produto.

    public final ResultadoGolpe executar(Lutador atacante, Lutador defensor, Random sorteio) {
        Golpe golpe = criarGolpe();
        return golpe.aplicar(atacante, defensor, sorteio);
    }
}
