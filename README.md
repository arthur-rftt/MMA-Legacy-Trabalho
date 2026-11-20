# MMA Legacy — Protótipo de Padrões de Projeto (Java)

**Objetivo acadêmico:** demonstrar orientação a objetos e **Builder, Factory Method e Observer** em um protótipo jogável do MMA Legacy. A versão completa do MMA Legacy continua sendo um projeto HTML/JavaScript independente; este repositório é uma **adaptação reduzida em Java** para fins didáticos, não uma conversão integral.

> **Atenção à exigência da disciplina:** o enunciado diz que os padrões devem ser aplicados ao projeto conjunto da disciplina da Prof. Célia. Antes de substituir o sistema UNIVEL por este jogo na entrega, confirme com a professora ou com o professor responsável que o novo tema é aceito. Você pode usar este protótipo como estudo e demonstração, mesmo que precise entregar a integração ao projeto original.

## Rodar no Windows

1. Instale o **JDK 17 ou superior** (JDK, não apenas JRE).
2. Extraia o ZIP.
3. Execute `executar.bat` com duplo clique (ou abra a pasta no VS Code e rode `mmalegacy.Main`).
4. Para verificar os testes automatizados, execute `testar.bat`.

## Como jogar

1. Informe o nome do lutador, escolha entre **Boxe**, **Muay Thai** e **Wrestling** e clique em **Criar lutador**.
2. Você pode realizar **até duas sessões de camp** (Boxe, Chutes, Grappling ou Cardio). Cada sessão aumenta o atributo correspondente em 2, limitado a 100.
3. Clique em **Iniciar luta**. A luta possui até **3 rounds, com 4 trocas por round**.
4. Escolha **SOCO** (atinge cabeça), **CHUTE** (atinge corpo) ou **QUEDA** (desgaste do corpo e energia). O rival reage automaticamente.
5. A narração exibe os eventos, e as barras mostram dano por região e energia. Quando cabeça ou corpo chega a zero, a luta termina antecipadamente (KO/TKO). Se completar os rounds, ocorre decisão simplificada.
6. Ao finalizar, o cartel é atualizado. Você evolui no camp entre as lutas.

O modelo de dano e a pontuação são **simplificações próprias do protótipo**, não uma reprodução idêntica das regras mais amplas do MMA Legacy.

## Os três padrões implementados

| Padrão | Tipo | Problema | Solução | Classes principais |
|---|---|---|---|---|
| **Builder** | Criacional | A criação de Lutador requer vários campos | Métodos encadeados e `build()` validam a construção | `LutadorBuilder`, `Lutador` |
| **Factory Method** | Criacional | O combate precisaria conhecer `new Soco()`, `new Chute()` e `new Queda()` | Um método de criação polimórfico escolhe a implementação de `Golpe` | `FabricaGolpe`, `FabricaSoco`, `FabricaChute`, `FabricaQueda`, `Golpe` |
| **Observer** | Comportamental | A engine de combate não deve manipular diretamente elementos visuais ou o histórico | O `Combate` publica eventos para observadores registrados | `Combate`, `ObservadorCombate`, `TelaJogo`, `HistoricoCombate` |

## Organização de pastas

```text
src/mmalegacy/
  Main.java                    Entrada do programa
  Testes.java                  Testes sem frameworks
  modelo/
    Lutador.java               Entidade com estado protegido
    LutadorBuilder.java        BUILDER
    Regiao.java                CABECA, CORPO
  golpes/
    Golpe.java                 Interface do produto
    Soco.java, Chute.java, Queda.java
    FabricaGolpe.java          Criador abstrato do FACTORY METHOD
    FabricaSoco.java, FabricaChute.java, FabricaQueda.java
    ResultadoGolpe.java, CalculoGolpe.java
  combate/
    Combate.java               Lógica da luta + emissor OBSERVER
    ObservadorCombate.java     Interface Observer
    HistoricoCombate.java      Observer concreto
    EventoCombate.java         Dados do evento
  treino/
    CampTreinamento.java       Progressão exclusivamente no treino
    FocoTreino.java
  view/
    TelaJogo.java              Swing + Observer concreto

docs/
   diagramas/                     UML em PNG e .drawio editável

```

## Demonstração para avaliação (2 minutos)

- Criar um lutador -> mostrar a chamada `new LutadorBuilder()...build()`.
- Iniciar luta e clicar em Soco, Chute, Queda -> mostrar `FabricaGolpe` e os métodos `criarGolpe()`.
- Ver narração e barras se atualizando -> mostrar `Combate.avisar()` e `TelaJogo.aoOcorrer()`.
- Terminar a luta, treinar e ver atributo crescer -> mostrar `CampTreinamento`.

## Limitações intencionais

- Não há draft de 12 rodadas, 19 atributos, habilidades especiais, ranking Top 15, cinturões, contratos, ou salvamento da carreira nessa versão pequena.
- A luta usa regras simplificadas e não realiza renderização em 3D.
- O progresso permanece só durante a execução; não grava banco nem arquivo.
- `java -ea` é necessário para ativar os `assert` dos testes.

**Referências conceituais:** https://refactoring.guru/pt-br/design-patterns/catalog e https://www.devmedia.com.br/guia/padroes-de-projeto-em-java/34456.
