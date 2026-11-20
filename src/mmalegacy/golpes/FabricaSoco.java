package mmalegacy.golpes;

public final class FabricaSoco extends FabricaGolpe {
    @Override protected Golpe criarGolpe() { return new Soco(); }
}
