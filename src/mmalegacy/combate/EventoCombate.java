package mmalegacy.combate;

/** Mensagem transmitida do combate aos observadores. */
public record EventoCombate(int round, String descricao) {}
