package mmalegacy.modelo;

/** Entidade do domínio: encapsula atributos, condição e cartel de um atleta. */
public final class Lutador {
    private final String nome;
    private final String pais;
    private final String categoria;
    private int socos, chutes, grappling, defesa, cardio;
    private int cabeca = 100, corpo = 100, energia;
    private int vitorias, derrotas;

    // Construtor restrito ao pacote: a aplicação cria lutadores pelo Builder.
    Lutador(String nome, String pais, String categoria, int socos, int chutes,
            int grappling, int defesa, int cardio) {
        this.nome = nome;
        this.pais = pais;
        this.categoria = categoria;
        this.socos = socos;
        this.chutes = chutes;
        this.grappling = grappling;
        this.defesa = defesa;
        this.cardio = cardio;
        this.energia = cardio;
    }

    public String getNome() { return nome; }
    public String getPais() { return pais; }
    public String getCategoria() { return categoria; }
    public int getSocos() { return socos; }
    public int getChutes() { return chutes; }
    public int getGrappling() { return grappling; }
    public int getDefesa() { return defesa; }
    public int getCardio() { return cardio; }
    public int getCabeca() { return cabeca; }
    public int getCorpo() { return corpo; }
    public int getEnergia() { return energia; }
    public int getVitorias() { return vitorias; }
    public int getDerrotas() { return derrotas; }

    public void gastarEnergia(int quantidade) {
        energia = Math.max(0, energia - Math.max(0, quantidade));
    }
    public void receberDano(Regiao regiao, int dano) {
        int seguro = Math.max(0, dano);
        if (regiao == Regiao.CABECA) cabeca = Math.max(0, cabeca - seguro);
        else corpo = Math.max(0, corpo - seguro);
    }
    public void restaurarParaLuta() {
        cabeca = 100;
        corpo = 100;
        energia = cardio;
    }
    public void registrarVitoria() { vitorias++; }
    public void registrarDerrota() { derrotas++; }
    public void melhorarSocos(int pontos) { socos = Math.min(100, socos + Math.max(0, pontos)); }
    public void melhorarChutes(int pontos) { chutes = Math.min(100, chutes + Math.max(0, pontos)); }
    public void melhorarGrappling(int pontos) { grappling = Math.min(100, grappling + Math.max(0, pontos)); }
    public void melhorarCardio(int pontos) { cardio = Math.min(100, cardio + Math.max(0, pontos)); }
    public void recuperarEnergia() { energia = cardio; }
    public boolean estaNocauteado() { return cabeca == 0 || corpo == 0; }
    public int pontosParaDecisao() { return cabeca + corpo; }
    public String cartel() { return vitorias + "V - " + derrotas + "D"; }
}
