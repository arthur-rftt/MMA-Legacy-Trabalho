package mmalegacy.modelo;

/** BUILDER: monta um Lutador passo a passo, com validações centralizadas. */
public final class LutadorBuilder {
    private String nome = "Novato";
    private String pais = "Brasil";
    private String categoria = "Peso-leve";
    private int socos = 60, chutes = 60, grappling = 60, defesa = 60, cardio = 80;

    public LutadorBuilder nome(String valor) { nome = valor; return this; }
    public LutadorBuilder pais(String valor) { pais = valor; return this; }
    public LutadorBuilder categoria(String valor) { categoria = valor; return this; }
    public LutadorBuilder socos(int valor) { socos = valor; return this; }
    public LutadorBuilder chutes(int valor) { chutes = valor; return this; }
    public LutadorBuilder grappling(int valor) { grappling = valor; return this; }
    public LutadorBuilder defesa(int valor) { defesa = valor; return this; }
    public LutadorBuilder cardio(int valor) { cardio = valor; return this; }

    public Lutador build() {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório.");
        if (pais == null || pais.isBlank() || categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("País e categoria são obrigatórios.");
        validar("socos", socos); validar("chutes", chutes);
        validar("grappling", grappling); validar("defesa", defesa); validar("cardio", cardio);
        return new Lutador(nome.trim(), pais.trim(), categoria.trim(),
                socos, chutes, grappling, defesa, cardio);
    }

    private static void validar(String campo, int valor) {
        if (valor < 1 || valor > 100)
            throw new IllegalArgumentException(campo + " deve estar entre 1 e 100.");
    }
}
