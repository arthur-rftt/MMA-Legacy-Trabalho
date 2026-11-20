# Justificativa da escolha entre os 10 padrões

| Padrão | Decisão | Contexto deste protótipo |
|---|---|---|
| Singleton | Não usar | Um estado global único de jogo dificultaria testes e múltiplas partidas. Não há recurso compartilhado que exija uma única instância. |
| **Factory Method** | **Usar** | Soco, Chute e Queda implementam `Golpe`, e diferentes fábricas criam esses produtos. |
| Adapter | Não usar | Não há API externa com interface incompatível a adaptar. |
| Bridge | Não usar | Não precisamos separar duas dimensões de variação em hierarquias independentes. |
| Decorator | Possível no futuro | Poderia acumular efeitos temporários, como bônus de golpes, mas é complexidade desnecessária para a demonstração atual. |
| **Builder** | **Usar** | Configuração legível de vários atributos de `Lutador`, com validação no `build()`. |
| Proxy | Não usar | Não há rede, controle remoto, carregamento pesado ou serviço com acesso protegido. |
| Iterator | Não usar | As coleções do Java já suportam iteração e não precisamos de uma travessia especializada. |
| Memento | Possível no futuro | Poderia desfazer a seleção de golpes ou armazenar checkpoints; não há requisito de desfazer no protótipo. |
| **Observer** | **Usar** | Motor envia mensagens para tela e histórico sem depender diretamente de Swing. |

**Por que mudar a seleção do projeto UNIVEL?** No projeto de cadastro, Singleton para gerenciamento de conexões era uma opção. Já neste jogo, usar Singleton não traz um benefício tão claro e pode estimular estado global. Aqui, Factory Method resolve um problema concreto e é melhor para demonstrar herança e polimorfismo.
