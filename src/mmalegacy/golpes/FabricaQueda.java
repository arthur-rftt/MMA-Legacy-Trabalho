package mmalegacy.golpes;

public final class FabricaQueda extends FabricaGolpe {
    @Override protected Golpe criarGolpe() { return new Queda(); }
}
