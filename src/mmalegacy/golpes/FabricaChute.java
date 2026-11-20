package mmalegacy.golpes;

public final class FabricaChute extends FabricaGolpe {
    @Override protected Golpe criarGolpe() { return new Chute(); }
}
