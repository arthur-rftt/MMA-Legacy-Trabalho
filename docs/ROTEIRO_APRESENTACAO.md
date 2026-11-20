# Roteiro da apresentação — MMA Legacy Java (6–8 minutos)

## 1. Contexto (40 segundos)

"Meu projeto é um protótipo em Java do jogo MMA Legacy. O jogador cria um lutador, escolhe golpes, acompanha a luta e evolui no treinamento. Usei programação orientada a objetos para separar responsabilidades e apliquei três padrões: Builder, Factory Method e Observer."

Mostre a tela e crie um personagem. Explique a diferença: **Java + Swing**, sem conexão com MySQL; o jogo original mais completo permanece separado.

## 2. Orientação a objetos (45 segundos)

"Lutador é uma classe que representa uma entidade do jogo. Cada objeto possui nome, atributos, condição física e cartel. Os campos são `private`, então alterações devem passar por métodos como `receberDano`, `gastarEnergia` e `registrarVitoria`: isso é encapsulamento. A interface `Golpe` define um contrato; `Soco`, `Chute` e `Queda` implementam o contrato com regras diferentes: isso demonstra polimorfismo."

**Arquivo para abrir:** `modelo/Lutador.java`, depois `golpes/Golpe.java`.

## 3. Padrão Builder (1 minuto)

**Nome e propósito:** Builder, criacional. Montar objetos passo a passo.

**Problema:** `Lutador` exige vários atributos, e um construtor enorme com muitos números é difícil de ler e de validar.

**Solução:** `LutadorBuilder` possui métodos `nome()`, `socos()`, `chutes()`, `grappling()`, `defesa()`, `cardio()` e `build()`. Cada método retorna `this`; `build()` valida e cria o objeto pronto.

**Código-chave:**

```java
jogador = new LutadorBuilder()
    .nome(campoNome.getText())
    .socos(78)
    .cardio(85)
    .build();
```

O exemplo do código real adiciona outros campos. Mostre o diagrama `diagramas/01_builder.png`.

**Pergunta provável:** "Por que não usar só um construtor?" Resposta: é possível; Builder torna a criação mais legível e concentra a validação para o caso de muitos atributos.

## 4. Padrão Factory Method (1 minuto e meio)

**Nome e propósito:** Factory Method, criacional. Definir uma operação de criação e permitir que subclasses decidam qual produto concreto instanciar.

**Problema:** sem o padrão, a lógica central de combate precisaria escolher `new Soco()`, `new Chute()`, `new Queda()`, criando dependência direta entre regras da luta e classes de golpes.

**Solução:** `FabricaGolpe` declara `protected abstract Golpe criarGolpe()`. `FabricaSoco`, `FabricaChute` e `FabricaQueda` implementam esse método. O método comum `executar()` recebe o produto como `Golpe` e usa seu método `aplicar()`.

**Código-chave:**

```java
public abstract class FabricaGolpe {
    protected abstract Golpe criarGolpe();

    public final ResultadoGolpe executar(Lutador a, Lutador d, Random r) {
        Golpe golpe = criarGolpe();
        return golpe.aplicar(a, d, r);
    }
}
```

No jogo, clique nos três botões. Mostre `diagramas/02_factory_method.png`.

**Pergunta provável:** "Por que é Factory Method e não Simple Factory?" Resposta: porque subclasses de `FabricaGolpe` **sobrescrevem** o método de criação, em vez de uma única classe usar um `switch` para instanciar produtos.

## 5. Padrão Observer (1 minuto e meio)

**Nome e propósito:** Observer, comportamental. Avisar automaticamente os inscritos quando o estado ou evento de um objeto muda.

**Problema:** o motor da luta não deve conhecer detalhes de Swing nem gravar narração diretamente na área de texto.

**Solução:** `Combate` mantém `List<ObservadorCombate>`; `TelaJogo` e `HistoricoCombate` implementam a mesma interface. Quando um golpe acontece, `Combate.avisar()` envia `EventoCombate` a todos.

**Código-chave:**

```java
for (ObservadorCombate observador : List.copyOf(observadores)) {
    observador.aoOcorrer(evento);
}
```

Mostre `diagramas/03_observer.png`. Clique em Soco e aponte que a narração muda; `Combate` não chama `JTextArea`.

**Pergunta provável:** "Quais são os sujeitos e observadores?" Resposta: `Combate` é Subject; `TelaJogo` e `HistoricoCombate` são ConcreteObservers; `ObservadorCombate` é a interface Observer.

## 6. Treinamento e teste (45 segundos)

"No MMA Legacy, os atributos não aumentam automaticamente por vencer uma luta. Criei `CampTreinamento` para evoluí-los entre lutas. Os testes em `Testes.java` verificam a criação com Builder, três fábricas, notificações a observadores e a separação de treino e combate."

Execute `testar.bat` para mostrar `OK: 3 padrões + regras de treino e combate verificados.`

## 7. Encerramento (20 segundos)

"Os padrões resolveram problemas concretos: Builder organiza criação de lutadores; Factory Method desacopla criação de golpes; Observer desacopla regras de combate da interface e do histórico. Por ser um protótipo acadêmico, mantive só as mecânicas necessárias para demonstrar essas responsabilidades."

---

## Ordem dos arquivos para abrir na apresentação

1. `modelo/LutadorBuilder.java` e `modelo/Lutador.java`.
2. `golpes/FabricaGolpe.java`, `golpes/FabricaSoco.java`, `golpes/Soco.java`.
3. `combate/ObservadorCombate.java`, `combate/Combate.java` e `view/TelaJogo.java` (método `aoOcorrer`).
4. `Testes.java`.

> **Aviso acadêmico:** o trabalho original cita integração obrigatória ao projeto da Prof. Célia. Apresente este jogo como entrega substituta somente com aprovação do professor.
