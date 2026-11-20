package mmalegacy.combate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Observador concreto: guarda todos os eventos recebidos. */
public final class HistoricoCombate implements ObservadorCombate {
    private final List<EventoCombate> eventos = new ArrayList<>();

    @Override public void aoOcorrer(EventoCombate evento) { eventos.add(evento); }
    public List<EventoCombate> getEventos() { return Collections.unmodifiableList(eventos); }
}
