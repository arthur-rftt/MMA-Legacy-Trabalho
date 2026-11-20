# Guia de estudo — entenda o código antes da arguição

## Conceitos de POO usados

- **Classe**: molde que define dados e comportamentos, como `Lutador`.
- **Objeto**: instância criada a partir de uma classe, como `jogador` ou `rival`.
- **Encapsulamento**: atributos `private`; o código externo deve pedir para `Lutador` receber dano, em vez de alterar diretamente a barra de vida.
- **Abstração**: `Golpe` descreve *o que* qualquer golpe sabe fazer (`aplicar`).
- **Herança**: `FabricaSoco extends FabricaGolpe`, herdando `executar()` e implementando `criarGolpe()`.
- **Polimorfismo**: o método `executar()` trata `Golpe` como contrato, sem precisar conhecer `Soco`, `Chute` ou `Queda` em tempo de compilação.
- **Composição/associação**: `Combate` contém referências a dois `Lutador` e uma lista de observadores.

## O que cada arquivo faz

| Arquivo | Responsabilidade |
|---|---|
| `Main.java` | Abre a interface Swing. |
| `Lutador.java` | Guarda e protege atributos, dano e cartel. |
| `LutadorBuilder.java` | Define como criar um lutador válido. |
| `Golpe.java` | Interface que todo golpe implementa. |
| `Soco.java`, `Chute.java`, `Queda.java` | Regras específicas dos três ataques. |
| `CalculoGolpe.java` | Cálculo compartilhado de acerto, gasto de energia e dano. |
| `FabricaGolpe.java` | Template com método de criação abstrato do Factory Method. |
| `FabricaSoco/Chute/Queda.java` | Decidem qual golpe concreto criar. |
| `Combate.java` | Controla sequência dos golpes, rounds, resultado e notificações. |
| `ObservadorCombate.java` | Contrato para receber notificações de uma luta. |
| `HistoricoCombate.java` | Armazena eventos de combate. |
| `TelaJogo.java` | Interação Swing e observador que atualiza a tela. |
| `CampTreinamento.java` | Modifica atributos fora da luta. |
| `Testes.java` | Valida cenários fundamentais sem bibliotecas externas. |

## Perguntas que podem cair

**1. Por que não usar tudo em uma única classe?**

Porque mistura interface, regras de negócio e criação de objetos. A separação facilita alterar e testar uma parte sem reescrever as outras.

**2. Para que serve `private`?**

Controla o acesso aos dados e evita que uma parte externa mude, por exemplo, a vida de um lutador para um valor impossível.

**3. O que significa `return this` no Builder?**

Retorna a própria instância do Builder para permitir encadeamento, por exemplo `.nome(...).socos(...).build()`.

**4. Qual é a diferença entre Builder e Factory Method?**

Builder **configura o estado** de um objeto com muitos atributos; Factory Method **escolhe o tipo concreto** de objeto criado por uma hierarquia de criadores.

**5. Onde há herança?**

`FabricaSoco extends FabricaGolpe`, assim como as demais fábricas. O tipo pai é abstrato.

**6. Onde há interface?**

`Golpe` e `ObservadorCombate`. Classes diferentes podem implementar o mesmo contrato.

**7. Por que o Factory Method é melhor que `if`?**

O motor opera sobre `FabricaGolpe` e `Golpe`. Para acrescentar outro golpe, podemos criar uma classe `Cotovelada` e sua fábrica sem modificar a implementação central de `FabricaGolpe`.

**8. Se eu quiser adicionar outro observador, o que muda?**

Basta implementar `ObservadorCombate` e registrar em `Combate.adicionarObservador(...)`. O motor não precisa conhecer essa nova classe.

**9. O que `List.copyOf(observadores)` faz?**

Produz uma cópia imutável da lista naquele momento. Evita problemas se alguém alterar as inscrições enquanto as notificações são enviadas.

**10. O que significa `@Override`?**

Indica que o método implementa ou sobrescreve um método declarado no tipo pai ou em uma interface. O compilador verifica se a assinatura está correta.

**11. O que significa `final` em classes e métodos?**

Impede subclasses (`final class`) ou sobrescrita (`final` no método). Em `FabricaGolpe.executar()`, a sequência comum é preservada, enquanto `criarGolpe()` é personalizado.

**12. O jogo respeita completamente o MMA Legacy original?**

Não. É um **protótipo reduzido** do núcleo do jogo, com poucos atributos e golpes, para demonstrar POO e padrões em Java. Recursos como draft, ranking e cinturões ficam fora do escopo didático.

**13. O que significa `Random` e por que passar por parâmetro?**

Representa a fonte de aleatoriedade dos golpes. Passá-la na construção torna possíveis testes reproduzíveis com uma semente fixa, como `new Random(17)`.

**14. Quem decide o fim da luta?**

A classe `Combate`: KO/TKO se vida de uma região chegar a zero; caso contrário, decisão simplificada após 12 trocas.

**15. Onde o Observer é visível na interface?**

Clique em um golpe: `Combate.avisar()` envia o evento e `TelaJogo.aoOcorrer()` escreve no campo de narração, sem o `Combate` importar Swing.

## Checklist de estudo

- [ ] Eu sei explicar o que é uma classe e um objeto.
- [ ] Eu sei apontar a diferença entre `extends` e `implements`.
- [ ] Eu consigo mostrar o Builder construindo um objeto.
- [ ] Eu consigo encontrar o Factory Method `criarGolpe()`.
- [ ] Eu consigo apontar o Subject e dois Observers.
- [ ] Eu sei como uma luta termina e como o cartel muda.
- [ ] Eu sei explicar por que pontos de atributo são ganhos no camp, e não após a luta.
- [ ] Eu consigo executar o jogo e os testes sem ajuda.
