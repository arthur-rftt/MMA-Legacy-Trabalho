package mmalegacy.combate;

/** OBSERVER — contrato de quem deseja receber eventos. */
public interface ObservadorCombate {
    void aoOcorrer(EventoCombate evento);
}
