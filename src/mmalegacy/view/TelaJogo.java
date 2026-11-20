package mmalegacy.view;

import mmalegacy.combate.*;
import mmalegacy.golpes.*;
import mmalegacy.modelo.*;
import mmalegacy.treino.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

/** Interface Swing: observa o combate, sem que o domínio conheça a interface. */
public final class TelaJogo extends JFrame implements ObservadorCombate {
    private static final Color FUNDO = new Color(16, 20, 29);
    private static final Color PAINEL = new Color(28, 35, 48);
    private static final Color TEXTO = new Color(234, 239, 246);
    private static final Color MUTED = new Color(166, 179, 196);
    private static final Color VERMELHO = new Color(223, 60, 84);
    private static final Color VERDE = new Color(47, 184, 145);
    private static final Font BASE = new Font("SansSerif", Font.PLAIN, 13);

    private final Random sorteio = new Random();
    private final CampTreinamento camp = new CampTreinamento();
    private Lutador jogador;
    private Combate combate;
    private HistoricoCombate historico;
    private int numeroLuta;
    private int treinosRestantes;
    private boolean resultadoRegistrado;

    private final JTextField campoNome = new JTextField("O Predador", 13);
    private final JComboBox<String> campoEstilo = new JComboBox<>(new String[]{"Boxe", "Muay Thai", "Wrestling"});
    private final JComboBox<FocoTreino> campoTreino = new JComboBox<>(FocoTreino.values());
    private final JButton botaoCriar = new JButton("Criar lutador");
    private final JButton botaoLuta = new JButton("Iniciar luta");
    private final JButton botaoSoco = new JButton("SOCO");
    private final JButton botaoChute = new JButton("CHUTE");
    private final JButton botaoQueda = new JButton("QUEDA");
    private final JButton botaoTreino = new JButton("Treinar (+2)");
    private final JTextArea narracao = new JTextArea();
    private final JLabel status = new JLabel("Crie seu lutador para começar.");
    private final JLabel round = new JLabel("ROUND —");
    private final JLabel meusDados = new JLabel("Nenhum atleta criado");
    private final JLabel dadosRival = new JLabel("Aguardando adversário");
    private final JProgressBar minhaCabeca = barra(VERMELHO);
    private final JProgressBar meuCorpo = barra(new Color(246, 163, 79));
    private final JProgressBar minhaEnergia = barra(VERDE);
    private final JProgressBar rivalCabeca = barra(VERMELHO);
    private final JProgressBar rivalCorpo = barra(new Color(246, 163, 79));
    private final JProgressBar rivalEnergia = barra(VERDE);
    private final JLabel meusAtributos = new JLabel(" ");
    private final JLabel disponibilidadeTreino = new JLabel(" ");

    public TelaJogo() {
        super("MMA LEGACY | Protótipo Java — Padrões de Projeto");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980, 800);
        setMinimumSize(new Dimension(850, 680));
        setLocationRelativeTo(null);
        montarInterface();
        configurarAcoes();
        atualizar();
    }

    private void montarInterface() {
        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(FUNDO);
        raiz.setBorder(new EmptyBorder(18, 20, 18, 20));
        setContentPane(raiz);

        JPanel topo = new JPanel(new BorderLayout(0, 11));
        topo.setOpaque(false);
        JLabel titulo = texto("MMA LEGACY", 30, Font.BOLD, TEXTO);
        JLabel subtitulo = texto("PROTÓTIPO EM JAVA  •  BUILDER  •  FACTORY METHOD  •  OBSERVER", 11, Font.BOLD, MUTED);
        JPanel marca = vertical(); marca.add(titulo); marca.add(subtitulo);
        topo.add(marca, BorderLayout.NORTH);

        JPanel criacao = painel();
        criacao.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));
        criacao.add(texto("Nome:", 12, Font.BOLD, TEXTO));
        estilizarCampo(campoNome);
        criacao.add(campoNome);
        criacao.add(texto("Especialidade:", 12, Font.BOLD, TEXTO));
        estilizarCampo(campoEstilo);
        criacao.add(campoEstilo);
        estilizarBotao(botaoCriar, VERMELHO);
        criacao.add(botaoCriar);
        topo.add(criacao, BorderLayout.CENTER);
        raiz.add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setOpaque(false);

        JPanel versus = new JPanel(new GridLayout(1, 2, 12, 0));
        versus.setOpaque(false);
        versus.add(cartaoAtleta("SEU LUTADOR", meusDados, meusAtributos, minhaCabeca, meuCorpo, minhaEnergia));
        versus.add(cartaoAtleta("ADVERSÁRIO", dadosRival, null, rivalCabeca, rivalCorpo, rivalEnergia));
        centro.add(versus, BorderLayout.NORTH);

        JPanel logPanel = painel();
        logPanel.setLayout(new BorderLayout(0, 9));
        JPanel cabecalho = new JPanel(new BorderLayout()); cabecalho.setOpaque(false);
        cabecalho.add(texto("NARRAÇÃO DO COMBATE", 13, Font.BOLD, TEXTO), BorderLayout.WEST);
        round.setForeground(VERMELHO); round.setFont(new Font("SansSerif", Font.BOLD, 13));
        cabecalho.add(round, BorderLayout.EAST);
        logPanel.add(cabecalho, BorderLayout.NORTH);
        narracao.setEditable(false);
        narracao.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        narracao.setForeground(TEXTO);
        narracao.setBackground(new Color(20, 26, 36));
        narracao.setBorder(new EmptyBorder(12, 12, 12, 12));
        narracao.setLineWrap(true); narracao.setWrapStyleWord(true);
        JScrollPane rolagem = new JScrollPane(narracao);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        logPanel.add(rolagem, BorderLayout.CENTER);
        centro.add(logPanel, BorderLayout.CENTER);
        raiz.add(centro, BorderLayout.CENTER);

        JPanel rodape = painel();
        rodape.setLayout(new BorderLayout(0, 10));
        JPanel comandos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        comandos.setOpaque(false);
        estilizarBotao(botaoLuta, VERMELHO);
        estilizarBotao(botaoSoco, new Color(75, 116, 175));
        estilizarBotao(botaoChute, new Color(75, 116, 175));
        estilizarBotao(botaoQueda, new Color(75, 116, 175));
        comandos.add(botaoLuta); comandos.add(botaoSoco);
        comandos.add(botaoChute); comandos.add(botaoQueda);
        rodape.add(comandos, BorderLayout.NORTH);

        JPanel inferior = new JPanel(new BorderLayout(5, 0)); inferior.setOpaque(false);
        JPanel treino = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); treino.setOpaque(false);
        treino.add(texto("CAMP:", 12, Font.BOLD, TEXTO));
        estilizarCampo(campoTreino); treino.add(campoTreino);
        estilizarBotao(botaoTreino, VERDE); treino.add(botaoTreino);
        disponibilidadeTreino.setForeground(MUTED); treino.add(disponibilidadeTreino);
        inferior.add(treino, BorderLayout.WEST);
        rodape.add(inferior, BorderLayout.CENTER);
        status.setFont(BASE); status.setForeground(MUTED);
        rodape.add(status, BorderLayout.SOUTH);
        raiz.add(rodape, BorderLayout.SOUTH);
    }

    private JPanel cartaoAtleta(String rotulo, JLabel nome, JLabel atributos,
                               JProgressBar cabeca, JProgressBar corpo, JProgressBar energia) {
        JPanel p = painel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(texto(rotulo, 11, Font.BOLD, VERMELHO));
        nome.setFont(new Font("SansSerif", Font.BOLD, 17)); nome.setForeground(TEXTO);
        nome.setBorder(new EmptyBorder(7, 0, 3, 0)); p.add(nome);
        if (atributos != null) { atributos.setFont(BASE); atributos.setForeground(MUTED); p.add(atributos); }
        adicionarMedidor(p, "Cabeça", cabeca);
        adicionarMedidor(p, "Corpo", corpo);
        adicionarMedidor(p, "Energia", energia);
        return p;
    }

    private void adicionarMedidor(JPanel p, String nome, JProgressBar barra) {
        JLabel rotulo = texto(nome, 11, Font.PLAIN, MUTED);
        rotulo.setBorder(new EmptyBorder(5, 0, 2, 0));
        p.add(rotulo);
        barra.setAlignmentX(Component.LEFT_ALIGNMENT);
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        p.add(barra);
    }

    private void configurarAcoes() {
        botaoCriar.addActionListener(e -> criarLutador());
        botaoLuta.addActionListener(e -> novaLuta());
        botaoSoco.addActionListener(e -> atacar(new FabricaSoco()));
        botaoChute.addActionListener(e -> atacar(new FabricaChute()));
        botaoQueda.addActionListener(e -> atacar(new FabricaQueda()));
        botaoTreino.addActionListener(e -> treinar());
    }

    private void criarLutador() {
        try {
            int especialidade = campoEstilo.getSelectedIndex();
            jogador = new LutadorBuilder()
                    .nome(campoNome.getText())
                    .pais("Brasil")
                    .categoria("Peso-leve")
                    .socos(especialidade == 0 ? 78 : 59)
                    .chutes(especialidade == 1 ? 78 : 59)
                    .grappling(especialidade == 2 ? 78 : 59)
                    .defesa(62)
                    .cardio(85)
                    .build(); // BUILDER utilizado de verdade pela interface.
            combate = null; numeroLuta = 0; treinosRestantes = 2;
            narracao.setText("Lutador criado! Escolha um camp ou comece a luta.\n");
            status.setText("Lutador pronto. Você pode fazer até dois treinos antes da próxima luta.");
            atualizar();
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Erro de cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void novaLuta() {
        if (jogador == null || (combate != null && !combate.terminou())) return;
        String[] nomes = {"Rafael Falcão", "Diego Monteiro", "Bruno Tempestade", "Caio Torres"};
        String nome = nomes[numeroLuta % nomes.length];
        int ajuste = Math.min(12, numeroLuta * 2);
        Lutador rival = new LutadorBuilder()
                .nome(nome).pais("Brasil").categoria("Peso-leve")
                .socos(60 + ajuste).chutes(62 + ajuste)
                .grappling(59 + ajuste).defesa(61 + ajuste).cardio(85)
                .build();
        numeroLuta++;
        combate = new Combate(jogador, rival, sorteio);
        historico = new HistoricoCombate();
        combate.adicionarObservador(this);           // OBSERVER 1: tela
        combate.adicionarObservador(historico);       // OBSERVER 2: histórico
        resultadoRegistrado = false;
        treinosRestantes = 0;
        narracao.setText("");
        combate.iniciar();
        atualizar();
    }

    private void atacar(FabricaGolpe fabrica) {
        if (combate == null || combate.terminou()) return;
        combate.proximaTroca(fabrica); // FACTORY METHOD cria Soco, Chute ou Queda.
        atualizar();
    }

    private void treinar() {
        if (jogador == null || treinosRestantes <= 0 || (combate != null && !combate.terminou())) return;
        FocoTreino foco = (FocoTreino) campoTreino.getSelectedItem();
        String mensagem = camp.treinar(jogador, foco);
        treinosRestantes--;
        narracao.append("[TREINO] " + mensagem + "\n");
        status.setText(mensagem);
        atualizar();
    }

    @Override public void aoOcorrer(EventoCombate evento) {
        // O Combate conhece apenas ObservadorCombate, não conhece JFrame/JTextArea.
        narracao.append("[R" + evento.round() + "] " + evento.descricao() + "\n");
        narracao.setCaretPosition(narracao.getDocument().getLength());
        if (combate != null && combate.terminou() && !resultadoRegistrado) {
            resultadoRegistrado = true;
            treinosRestantes = 2;
            status.setText(combate.getResultado() + "  Agora você pode treinar para a próxima luta.");
        }
        atualizar();
    }

    private void atualizar() {
        boolean emLuta = combate != null && !combate.terminou();
        botaoCriar.setEnabled(!emLuta);
        campoNome.setEnabled(!emLuta); campoEstilo.setEnabled(!emLuta);
        botaoLuta.setEnabled(jogador != null && !emLuta);
        botaoLuta.setText(numeroLuta == 0 ? "Iniciar luta" : "Próxima luta");
        for (JButton b : new JButton[]{botaoSoco, botaoChute, botaoQueda}) b.setEnabled(emLuta);
        boolean podeTreinar = jogador != null && !emLuta && treinosRestantes > 0;
        botaoTreino.setEnabled(podeTreinar); campoTreino.setEnabled(podeTreinar);
        disponibilidadeTreino.setText("Sessões disponíveis: " + treinosRestantes);
        if (jogador != null) {
            meusDados.setText(jogador.getNome() + "  •  " + jogador.cartel());
            meusAtributos.setText("Boxe " + jogador.getSocos() + "  |  Chute " + jogador.getChutes()
                    + "  |  Grappling " + jogador.getGrappling() + "  |  Cardio " + jogador.getCardio());
            minhaCabeca.setValue(jogador.getCabeca());
            meuCorpo.setValue(jogador.getCorpo());
            minhaEnergia.setValue(jogador.getEnergia());
        }
        if (combate != null) {
            Lutador rival = combate.getAdversario();
            dadosRival.setText(rival.getNome());
            rivalCabeca.setValue(rival.getCabeca());
            rivalCorpo.setValue(rival.getCorpo());
            rivalEnergia.setValue(rival.getEnergia());
            round.setText(combate.terminou() ? "ENCERRADO" : "ROUND " + combate.getRound() + "/3");
        } else {
            dadosRival.setText("Aguardando adversário");
            rivalCabeca.setValue(100); rivalCorpo.setValue(100); rivalEnergia.setValue(100);
            round.setText("ROUND —");
        }
    }

    private static JPanel painel() {
        JPanel p = new JPanel(); p.setBackground(PAINEL);
        p.setBorder(new EmptyBorder(12, 15, 12, 15));
        return p;
    }
    private static JPanel vertical() {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); return p;
    }
    private static JLabel texto(String s, int tamanho, int tipo, Color cor) {
        JLabel label = new JLabel(s);
        label.setFont(new Font("SansSerif", tipo, tamanho)); label.setForeground(cor);
        return label;
    }
    private static JProgressBar barra(Color cor) {
        JProgressBar b = new JProgressBar(0, 100);
        b.setValue(100); b.setForeground(cor);
        b.setBackground(new Color(55, 63, 76)); b.setBorderPainted(false);
        b.setStringPainted(true);
        return b;
    }
    private static void estilizarCampo(JComponent componente) {
        componente.setFont(BASE);
        componente.setForeground(TEXTO);
        componente.setBackground(new Color(45, 54, 70));
        componente.setPreferredSize(new Dimension(componente instanceof JComboBox ? 145 : 150, 30));
        if (componente instanceof JComboBox<?> lista) {
            lista.setRenderer(new DefaultListCellRenderer() {
                @Override public Component getListCellRendererComponent(JList<?> origem,
                        Object valor, int indice, boolean selecionado, boolean foco) {
                    JLabel item = (JLabel) super.getListCellRendererComponent(origem,
                            valor, indice, selecionado, foco);
                    item.setForeground(TEXTO);
                    item.setBackground(selecionado ? new Color(61, 79, 108) : new Color(45, 54, 70));
                    return item;
                }
            });
        }
    }
    private static void estilizarBotao(JButton botao, Color cor) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setBackground(cor); botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setOpaque(true); botao.setBorder(new EmptyBorder(10, 14, 10, 14));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
