# Enumerações e União de Tipos na Linguagem Funcional 1

**Universidade Federal de Pernambuco — Centro de Informática (CIn)**
**IN1007 — Paradigmas de Linguagens de Programação (2026.2)**
**Professor:** Augusto Sampaio

**Equipe:**

- Luiz Primo — lvps@cin.ufpe.br
- Kennedy Melo — kecm@cin.ufpe.br

---

## Sumário

1. [Visão geral](#1-visão-geral)
2. [Motivação](#2-motivação)
3. [Estrutura do repositório e execução](#3-estrutura-do-repositório-e-execução)
4. [A linguagem base: Funcional 1 (LF1)](#4-a-linguagem-base-funcional-1-lf1)
5. [Extensões propostas](#5-extensões-propostas)
6. [BNF estendida](#6-bnf-estendida)
7. [Verificação de tipos e execução](#7-verificação-de-tipos-e-execução)
8. [Exemplos](#8-exemplos)
9. [Implementação](#9-implementação)
10. [Referências](#10-referências)

---

## 1. Visão geral

Este projeto estende a **Linguagem Funcional 1 (LF1)**, apresentada na disciplina, com duas construções relacionadas:

- **Enumerações (`enum`)**: tipos definidos pelo programador a partir de um conjunto finito de constantes nomeadas.
- **União de tipos**: expressões cujo valor pode ser de mais de um tipo, por exemplo, um `Inteiro` ou uma `String`.

A extensão foi pensada para **preservar a sintaxe da LF1**. Nenhuma regra da BNF original é alterada: a extensão apenas acrescenta uma nova forma de declaração (`enum`) e uma nova expressão (`e is T`). Assim como na LF1, não há anotações de tipo. Os tipos, inclusive os tipos união, continuam sendo inferidos a partir do uso.

As duas construções são tratadas em conjunto porque uma enumeração é o **caso mais simples de união**: uma união de constantes que não carregam nenhum dado além do próprio nome.

Toda expressão válida em LF1 continua válida e com o mesmo significado na linguagem estendida.

## 2. Motivação

Na LF1, toda expressão tem exatamente um tipo (`Inteiro`, `Booleano` ou `String`). Por isso, algumas situações bastante comuns não podem ser expressas:

- representar um conjunto fechado de alternativas, como cores, dias da semana ou estados de um pedido, sem recorrer a números ou strings "mágicos";
- escrever uma expressão que, dependendo de uma condição, produza valores de tipos diferentes. Um exemplo é `if ok then 42 else "erro"`, que a LF1 rejeita.

A enumeração resolve o primeiro problema e a união de tipos resolve o segundo. Em ambos os casos, o objetivo é manter a **segurança de tipos**: nenhuma operação pode ser aplicada a um valor cujo tipo ela não suporta. Para isso, a linguagem passa a oferecer:

- **introspecção**: a expressão `e is T` verifica, em tempo de execução, se o valor de `e` é do tipo `T`;
- **desestruturação**: dentro de um `if x is T then ... else ...`, a variável `x` é tratada como sendo do tipo `T` no ramo `then` e como sendo de um dos tipos restantes no ramo `else`.

A desestruturação reaproveita o `if` da LF1, em vez de introduzir uma construção nova como `match`. Isso mantém a gramática enxuta.

## 3. Estrutura do repositório e execução

O repositório contém o código-base das linguagens da disciplina, organizado como um projeto Maven multimódulo. Cada linguagem é um módulo independente, com seu próprio parser em JavaCC:

| Módulo | Descrição |
|---|---|
| `Expressoes1`, `Expressoes2` | Linguagens de Expressões 1 e 2 |
| `Funcional1` | **Linguagem Funcional 1 — base deste projeto** |
| `Funcional2`, `Funcional3` | Linguagens Funcionais 2 e 3 |
| `Imperativa1`, `Imperativa2` | Linguagens Imperativas 1 e 2 |
| `Objetos1`, `Objetos2` | Linguagens Orientadas a Objetos 1 e 2 |
| `AppUI`, `WebUI`, `WebAPI`, `WebDebug` | Interfaces gráficas e web para os interpretadores |

O trabalho deste projeto concentra-se no módulo `Funcional1`, cujos principais pontos são:

- `src/lf1/plp/functional1/parser/Functional1.jj` — gramática JavaCC da LF1;
- `src/lf1/plp/functional1/declaration/` — declarações (`DecVariavel`, `DecFuncao`, `DecComposta`);
- `src/lf1/plp/functional1/expression/` — expressões próprias da LF1 (`Aplicacao`, `IfThenElse`, `ExpDeclaracao`);
- `src/lf1/plp/functional1/util/` — tipos de funções e o tipo polimórfico usado na inferência;
- `src/lf1/plp/expressions1/` e `src/lf1/plp/expressions2/` — expressões, valores, tipos primitivos e ambientes herdados das Linguagens de Expressões.

### Pré-requisitos

- Java 8 ou superior
- Maven 3

### Executando o interpretador da LF1

```bash
cd Funcional1
mvn clean generate-sources compile exec:java
```

Por padrão, o interpretador lê o programa do arquivo `Funcional1/input`, verifica os tipos, executa e imprime o resultado.

## 4. A linguagem base: Funcional 1 (LF1)

A LF1 estende a Linguagem de Expressões 2 com **funções parametrizadas e recursivas**. O corpo de uma função é uma expressão, e a aplicação de uma função produz um valor. Os tipos são inferidos: não há anotações de tipo na sintaxe.

A BNF a seguir é a definida no material da disciplina:

```bnf
Programa ::= Expressao

Expressao ::= Valor
            | ExpUnaria
            | ExpBinaria
            | ExpDeclaracao
            | Id
            | Aplicacao
            | IfThenElse

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro
                | ValorBooleano
                | ValorString

ExpUnaria ::= "-" Expressao
            | "not" Expressao
            | "length" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao

ExpDeclaracao ::= "let" DeclaracaoFuncional "in" Expressao

DeclaracaoFuncional ::= DecVariavel
                      | DecFuncao
                      | DecComposta

DecVariavel ::= "var" Id "=" Expressao

DecFuncao ::= "fun" ListId "=" Expressao

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= Id | Id ListId

Aplicacao ::= Id "(" ListExp ")"

ListExp ::= Expressao | Expressao "," ListExp

IfThenElse ::= "if" Expressao "then" Expressao "else" Expressao
```

Um exemplo de programa em LF1 (o fatorial, presente em `Funcional1/input`):

```
let fun fat n =
      let fun mult x y = if (x == 0) then (0) else (y + (mult((x - 1), y)))
      in if (n == 0) then (1) else (mult(n, (fat(n - 1))))
in fat(5)
```

## 5. Extensões propostas

### 5.1 Enumerações

Uma enumeração é declarada dentro de um `let`, assim como variáveis e funções, listando suas constantes separadas por `|`:

```
let enum Cor = Vermelho | Verde | Azul in ...
```

- `Cor` passa a ser o nome de um **novo tipo**, válido no corpo do `let`.
- Cada constante (`Vermelho`, `Verde`, `Azul`) é introduzida no escopo como um **identificador comum**, cujo valor é a própria constante e cujo tipo é `Cor`. Por isso não é preciso nenhuma sintaxe nova para usá-las: `Verde` é simplesmente um `Id`.
- As constantes seguem as mesmas regras de escopo das variáveis da LF1. Declarar duas vezes o mesmo nome no mesmo `let` é erro; um `let` interno pode esconder um nome externo.
- Enumerações são **nominais**: duas enumerações distintas nunca são compatíveis entre si.
- Constantes de uma mesma enumeração podem ser comparadas com `==`. A análise de casos sobre uma enumeração é feita com o próprio `if` da LF1.

**Relação com a união de tipos.** A enumeração `Cor` pode ser vista como a união de três tipos com um único valor cada: `{Vermelho} | {Verde} | {Azul}`. A diferença em relação a uma união geral é que as constantes de um `enum` não carregam dado algum: o nome já é o valor. Por isso o `enum` é o ponto de partida natural da implementação.

### 5.2 União de tipos

Na LF1, os dois ramos de um `if` precisam ter o mesmo tipo. Na linguagem estendida, essa exigência deixa de existir. Quando os ramos têm tipos diferentes, o tipo do `if` é a **união** desses tipos:

```
if ok then 42 else "erro"        -- tipo: Inteiro | String
```

Os tipos união são, portanto, **inferidos**, e não escritos pelo programador. Eles podem ser formados a partir de qualquer tipo da linguagem: os primitivos e as enumerações declaradas.

Como não há anotações de tipo, "declarar" um valor de tipo união significa ligar a um nome uma expressão cujo tipo inferido é uma união. Isso pode ser feito com as duas formas de declaração da LF1:

- **Com `var`**: a variável recebe diretamente um `if` com ramos de tipos diferentes.

  ```
  let var v = if (1 == 1) then 42 else "erro"       -- v : Inteiro | String
  in ...
  ```

- **Com `fun`**: o corpo da função é um `if` com ramos de tipos diferentes, e o tipo de retorno da função é a união (exemplo completo em 8.9).

  ```
  let fun converte n = if (n == 0) then "zero" else n    -- retorno : Inteiro | String
  in ...
  ```

As demais formas de obter um valor de tipo união são combinações dessas duas:

- **Variável ligada a uma aplicação**: `var r = converte(5)` faz `r` ter tipo `Inteiro | String`, o tipo de retorno de `converte`.
- **Parâmetro de função**: quando uma função é aplicada a um argumento de tipo união, o parâmetro assume esse tipo dentro do corpo, como no exemplo 8.6.
- **Uniões com mais de dois tipos**: são obtidas encadeando `else if`, como no exemplo 8.8.

**O tipo de uma variável não pode ser ampliado depois da declaração.** Como a LF1 não tem atribuição, uma variável recebe seu valor uma única vez, no `let var`, e seu tipo fica fixo a partir desse ponto. Não é possível acrescentar um tipo à união de uma variável já declarada. O que se pode fazer é declarar, em um `let` interno, uma nova variável com o mesmo nome, que esconde a externa:

```
let var v = if (1 == 1) then 42 else "erro"         -- v : Inteiro | String
in let var v = if (1 == 2) then v else true         -- novo v : Inteiro | String | Booleano
   in ...
```

O `v` do lado direito da declaração interna é o `v` externo, pois a expressão é avaliada antes de o novo nome entrar no escopo. O `v` externo não é modificado: o novo `v` é outra variável, válida apenas no corpo do `let` interno.

- **Ordem e repetição não importam.** `Inteiro | String` e `String | Inteiro` são o mesmo tipo, e `Inteiro | Inteiro` é apenas `Inteiro`. Uniões dentro de uniões são achatadas: um `if` cujos ramos têm tipos `Inteiro | String` e `Booleano` tem tipo `Inteiro | String | Booleano`.
- **Operações da LF1 não se aplicam diretamente a uniões.** Se `v` tem tipo `Inteiro | String`, a expressão `v + 1` é rejeitada, pois `v` pode ser uma string. Antes é preciso descobrir qual é o tipo de `v`.
- **Introspecção com `is`.** A expressão `e is T` resulta em `true` se o valor de `e` for do tipo `T`, e em `false` caso contrário. `T` pode ser `Inteiro`, `Booleano`, `String` ou o nome de uma enumeração.
- **Desestruturação com `if`.** Em `if x is T then e1 else e2`, onde `x` é um identificador, a variável `x` é tratada como sendo do tipo `T` dentro de `e1` e como sendo de um dos demais tipos da união dentro de `e2`. É dessa forma que um valor de tipo união passa a ser usado com segurança:

```
if v is Inteiro then v + 1 else length v
```

Os valores da LF1 já carregam, em tempo de execução, a informação do seu tipo: um valor é um inteiro, um booleano, uma string ou, agora, uma constante de enumeração. Por isso, uma união **não precisa de uma representação especial**. O valor de uma expressão de tipo `Inteiro | String` é simplesmente um inteiro ou uma string, e o `is` apenas consulta essa informação.

### 5.3 Novas palavras reservadas

| Palavra | Uso |
|---|---|
| `enum` | declaração de enumeração |
| `is` | verificação de tipo |
| `Inteiro`, `Booleano`, `String` | nomes dos tipos primitivos, usados após `is` |

O símbolo `|` já existe como token no parser da LF1 e passa a ser usado também para separar as constantes de uma enumeração.

## 6. BNF estendida

A gramática abaixo é a BNF da LF1 acrescida das regras deste projeto. Produções e alternativas **novas** estão marcadas com `(+)`. Todas as demais permanecem **idênticas** às da LF1: nenhuma regra existente foi alterada ou removida.

```bnf
Programa ::= Expressao

Expressao ::= Valor
            | ExpUnaria
            | ExpBinaria
            | ExpDeclaracao
            | Id
            | Aplicacao
            | IfThenElse
            | ExpChecagemTipo                                (+)

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro
                | ValorBooleano
                | ValorString

ExpUnaria ::= "-" Expressao
            | "not" Expressao
            | "length" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao

ExpDeclaracao ::= "let" DeclaracaoFuncional "in" Expressao

DeclaracaoFuncional ::= DecVariavel
                      | DecFuncao
                      | DecComposta
                      | DecEnum                              (+)

DecVariavel ::= "var" Id "=" Expressao

DecFuncao ::= "fun" ListId "=" Expressao

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= Id | Id ListId

Aplicacao ::= Id "(" ListExp ")"

ListExp ::= Expressao | Expressao "," ListExp

IfThenElse ::= "if" Expressao "then" Expressao "else" Expressao

DecEnum ::= "enum" Id "=" ListConstEnum                      (+)

ListConstEnum ::= Id | Id "|" ListConstEnum                  (+)

ExpChecagemTipo ::= Expressao "is" Tipo                      (+)

Tipo ::= "Inteiro" | "Booleano" | "String" | Id              (+)
```

No total, a extensão acrescenta **duas alternativas** a produções existentes e **quatro produções novas**.

**Observações sobre a gramática:**

- A união de tipos **não tem sintaxe própria**. Ela surge da regra de tipos do `IfThenElse`, que deixa de exigir ramos de mesmo tipo (seção 7). A produção `IfThenElse` continua a mesma.
- As constantes de enumeração são reconhecidas pela alternativa `Id`, que já existe em `Expressao`. A distinção entre uma variável e uma constante é feita no ambiente, e não na sintaxe.
- Em `Tipo`, o `Id` deve ser o nome de uma enumeração visível no escopo. Essa restrição é verificada na checagem de tipos.
- O operador `is` tem a mesma precedência que `==`. Para combiná-lo com `and`, `or` ou `not`, usam-se parênteses, como já ocorre com `==` na LF1.
- Como `Inteiro`, `Booleano` e `String` passam a ser palavras reservadas, um programa LF1 que usasse algum desses nomes como identificador precisaria renomeá-lo. Esse é o único ponto em que a extensão não é totalmente transparente.

## 7. Verificação de tipos e execução

Esta seção descreve, em linguagem direta, o que o verificador de tipos aceita e como cada construção é avaliada. As regras da LF1 continuam valendo, com exceção da regra do `if`, que é generalizada.

### 7.1 Enumerações

- **Declaração.** Uma declaração `enum E = C1 | ... | Cn` é válida quando as constantes são distintas entre si e nenhum nome entra em conflito com outro declarado no mesmo `let`. Ela introduz o tipo `E` e as constantes `C1`, ..., `Cn`, todas do tipo `E`.
- **Uso de uma constante.** Uma constante é usada como qualquer identificador. Seu tipo é a enumeração que a declarou e seu valor é a própria constante.
- **Igualdade.** `a == b` é válida quando `a` e `b` são da mesma enumeração e resulta em `true` exatamente quando as duas constantes são a mesma. Comparar constantes de enumerações diferentes, ou uma constante com um inteiro, é um erro de tipo, como já ocorre na LF1 ao comparar valores de tipos diferentes.

### 7.2 União de tipos

- **Formação.** Em `if c then e1 else e2`, a condição `c` continua tendo que ser booleana. Se `e1` e `e2` têm o mesmo tipo, esse é o tipo do `if`, exatamente como na LF1. Se têm tipos diferentes, o tipo do `if` é a união dos dois.
- **Uso.** As operações da LF1 (`+`, `-`, `and`, `or`, `not`, `length`, `++`, `==`) exigem operandos com tipos específicos. Um operando de tipo união é rejeitado, mesmo que um dos seus membros seja o tipo esperado.
- **Funções.** Parâmetros e retornos de funções continuam tendo seus tipos inferidos, como na LF1. Uma função pode receber um argumento de tipo união e pode também retornar um valor de tipo união.
- **Execução.** O `if` é avaliado como na LF1. O valor produzido é o valor do ramo escolhido, sem nenhum encapsulamento.

### 7.3 Verificação de tipo com `is`

- `e is T` é sempre uma expressão do tipo `Booleano`. `T` deve ser um tipo primitivo ou uma enumeração declarada.
- Em tempo de execução, `e` é avaliada e o resultado é `true` se o valor obtido for do tipo `T`, e `false` caso contrário.

### 7.4 Desestruturação no `if`

Quando a condição de um `if` tem a forma `x is T`, com `x` um identificador, o verificador de tipos usa essa informação nos ramos:

- no ramo `then`, `x` é tratada como sendo do tipo `T`;
- no ramo `else`, `x` é tratada como sendo de um dos tipos possíveis de `x`, exceto `T`. Se restar apenas um tipo, `x` passa a ter exatamente esse tipo.

Se `x` não pode ser do tipo `T`, o ramo `then` nunca será executado. Da mesma forma, se `x` só pode ser do tipo `T`, o ramo `else` nunca será executado. Um ramo que nunca é executado não é verificado e não contribui para o tipo do `if`.

**Parâmetros de função.** Como na LF1, o corpo de uma função é verificado uma única vez, com os tipos dos parâmetros ainda por inferir. Quando o corpo testa `x is T` e `x` é um parâmetro sem tipo conhecido, o tipo de `x` passa a ser a união de `T` com o tipo que o ramo `else` exige de `x`. Se o ramo `else` não exigir nenhum tipo específico, `x` aceita, além de `T`, qualquer outro tipo. Uma chamada é aceita quando o tipo do argumento está contido nessa união (exemplo 8.3).

Uma consequência direta dessa regra é que um ramo que nunca é executado pode conter qualquer expressão, inclusive uma que seria rejeitada em outro lugar. No trecho abaixo, `v` já chegou ao último `if` com tipo `Booleano`, então o teste `v is Booleano` é sempre verdadeiro, e o `else` não é verificado:

```
let var v = if (1 == 1) then 10 else if (1 == 2) then "abc" else true
in if v is Inteiro then v + 1
   else if v is String then length v
   else if v is Booleano then 0 else v + "abc"     -- aceito: o último else nunca executa
```

Isso não compromete a segurança de tipos, pois a expressão mal tipada nunca é avaliada.

O estreitamento vale apenas para a forma `x is T` usada diretamente como condição. Condições compostas, como `not (x is T)` ou `(x is T) and b`, são expressões booleanas válidas, mas não alteram o tipo de `x` nos ramos.

## 8. Exemplos

### 8.1 Enumeração e análise de casos

```
let enum Cor = Vermelho | Verde | Azul
in let fun codigo c =
         if (c == Vermelho) then 1
         else if (c == Verde) then 2
         else 3
   in codigo(Verde)
```

Resultado: `2`. O parâmetro `c` tem seu tipo inferido como `Cor` a partir das comparações com as constantes.

### 8.2 Constante como valor de uma variável

```
let enum Estado = Aberto | Fechado
in let var s = Aberto
   in if (s == Fechado) then "fechado" else "aberto"
```

Resultado: `"aberto"`.

### 8.3 Função que trata um inteiro ou uma string

```
let fun tamanho x = if x is Inteiro then x else length x
in tamanho("plp") + tamanho(10)
```

Resultado: `13`. No ramo `then`, `x` é tratado como `Inteiro`. No ramo `else`, `length x` exige que `x` seja uma `String`. Por isso o parâmetro é inferido como `Inteiro | String`, e as duas chamadas são aceitas. Os dois ramos produzem `Inteiro`, que é o tipo de retorno da função. Uma chamada como `tamanho(true)` é rejeitada, pois `Booleano` não está na união.

### 8.4 Valor de tipo união

```
let var v = if (1 == 1) then 42 else "erro"
in if v is Inteiro then v + 1 else 0
```

Resultado: `43`. A variável `v` tem tipo `Inteiro | String`. No ramo `then`, ela é tratada como `Inteiro`, o que permite a soma.

### 8.5 Verificação de tipo com `is`

```
let fun ehTexto x = x is String
in ehTexto(true)
```

Resultado: `false`.

### 8.6 Enumeração como membro de uma união

```
let enum Cor = Vermelho | Verde | Azul
in let fun descreve x =
         if x is Cor then (if (x == Azul) then "azul" else "outra cor")
         else "numero"
   in descreve(if (1 == 1) then Azul else 0)
```

Resultado: `"azul"`. O argumento tem tipo `Cor | Inteiro`. No primeiro ramo, `x` é tratado como `Cor` e pode ser comparado com `Azul`.

### 8.7 Enumeração com `else if`

```
let enum Semaforo = Verde | Amarelo | Vermelho
in let fun acao s =
         if (s == Verde) then "siga"
         else if (s == Amarelo) then "atencao"
         else "pare"
   in acao(Amarelo)
```

Resultado: `"atencao"`. Cada `else if` compara `s` com uma constante diferente. O último `else` cobre a constante que restou, `Vermelho`.

### 8.8 União de três tipos com `else if`

```
let var v = if (1 == 1) then 10 else if (1 == 2) then "abc" else true
in if v is Inteiro then v + 1
   else if v is String then length v
   else if v then 1 else 0
```

Resultado: `11`. A variável `v` tem tipo `Inteiro | String | Booleano`. Cada `else` retira da união o tipo que acabou de ser testado:

| Ponto do programa | Tipo de `v` |
|---|---|
| Primeiro `then` | `Inteiro` |
| Primeiro `else` | `String \| Booleano` |
| Segundo `then` | `String` |
| Segundo `else` | `Booleano` |

No último ramo resta apenas `Booleano`, então `v` pode ser usado diretamente como condição. Os três ramos produzem `Inteiro`, que é o tipo da expressão inteira.

### 8.9 Função que retorna um valor de tipo união

```
let fun converte n = if (n == 0) then "zero" else n
in let var a = converte(0), var b = converte(7)
   in (if a is String then length a else a) + (if b is String then length b else b)
```

Resultado: `11`. O tipo de retorno de `converte` é `Inteiro | String`, e por isso `a` e `b` também têm esse tipo. Em tempo de execução, `a` vale `"zero"` e `b` vale `7`. Cada `if` trata os dois casos e produz um `Inteiro`: `length a` é `4` e `b` é `7`. Por isso a soma é aceita.

### 8.10 Estreitamento que não elimina a união

```
let var x = (let var v = if (1 == 1) then 10 else if (1 == 2) then "abc" else true
             in if v is Inteiro then v + 1
                else if v is String then v
                else if v then v else v)
in x
```

Resultado: `11`. Como no exemplo 8.8, cada `else` retira um tipo da união, e `v` pode ser usado com segurança em cada ramo:

| Ramo | Tipo de `v` | Expressão | Tipo do ramo |
|---|---|---|---|
| Primeiro `then` | `Inteiro` | `v + 1` | `Inteiro` |
| Segundo `then` | `String` | `v` | `String` |
| `if v then v else v` | `Booleano` | `v` | `Booleano` |

A diferença em relação ao 8.8 é que aqui os ramos produzem tipos diferentes. Por isso o `if` forma de novo uma união, e `x` tem tipo `Inteiro | String | Booleano`. O estreitamento permite usar `v` dentro de cada ramo, mas não elimina a união do valor resultante. Para usar `x` em uma operação como `x + 1`, é preciso testá-lo novamente com `is`.

Um detalhe: `if v then v else v` equivale a escrever só `v`. Os dois ramos são iguais, então não muda nem o tipo nem o valor.

### 8.11 Programas rejeitados pelo verificador de tipos

```
let var v = if (1 == 1) then 1 else "a"
in v + 1
```
Erro: `v` tem tipo `Inteiro | String`, e `+` exige dois inteiros. É preciso testar `v` com `is` antes de somar.

```
let enum Cor = Vermelho | Verde, enum Fruta = Banana | Maca
in Vermelho == Banana
```
Erro: `Cor` e `Fruta` são enumerações diferentes e seus valores não podem ser comparados.

```
let var v = if (1 == 1) then 1 else "a"
in if (not (v is Inteiro)) then length v else 0
```
Erro: a condição não tem a forma `x is T`, então `v` continua com tipo `Inteiro | String` dentro dos ramos e `length v` é rejeitado.

## 9. Implementação

A extensão foi implementada no módulo `Funcional1` **sem alterar nenhuma classe Java da LF1**. Todo comportamento novo está em classes novas, e as regras que mudam (a do `if` e a da aplicação de função) estão em subclasses que sobrescrevem apenas a verificação de tipos e herdam a avaliação. O único arquivo existente alterado é a gramática, para reconhecer a sintaxe nova.

| Componente | Local | Descrição |
|---|---|---|
| `TipoEnum` | `lf1.plp.functional1.util` | Tipo de uma enumeração, identificado pelo nome. Implementa `Tipo`. |
| `TipoUniao` | `lf1.plp.functional1.util` | Conjunto de tipos sem repetição e sem ordem, com as operações de união, pertinência e remoção usadas no estreitamento. Implementa `Tipo`. |
| `ValorEnum` | `lf1.plp.functional1.expression` | Valor de uma constante de enumeração. Estende `ValorConcreto`, o que faz `==` funcionar sem alterar `ExpEquals`. |
| `DecEnum` | `lf1.plp.functional1.declaration` | Declaração `enum E = C1 \| ... \| Cn`, que registra o tipo e as constantes no ambiente. |
| `ExpChecagemTipo` | `lf1.plp.functional1.expression` | Expressão `e is T`. |
| `IfThenElseUniao` | `lf1.plp.functional1.expression` | Subclasse de `IfThenElse`: formação de união quando os ramos têm tipos diferentes e estreitamento do tipo de `x` quando a condição é `x is T`. |
| `AplicacaoEstendida` | `lf1.plp.functional1.expression` | Subclasse de `Aplicacao`: aceita um argumento cujo tipo está contido no tipo união do parâmetro. |
| `AmbienteCompilacaoFuncional`, `ContextoCompilacaoFuncional` | `lf1.plp.functional1.memory` | Ambiente de compilação que também registra as enumerações visíveis em cada escopo. |
| `ProgramaEstendido` | `lf1.plp.functional1` | Subclasse de `Programa` que verifica os tipos usando o ambiente acima. |
| Alteração | `Functional1.jj` | Novos tokens (`enum`, `is`, `Inteiro`, `Booleano`, `String`), as produções da seção 6 e a criação de `ProgramaEstendido`, `IfThenElseUniao` e `AplicacaoEstendida` no lugar das classes originais. |

Todo programa aceito pela LF1 continua aceito, com o mesmo resultado. A única mudança de comportamento é a pretendida: um `if` com ramos de tipos diferentes, antes rejeitado, passa a ter tipo união.

**Limitação conhecida.** Enumerações são comparadas pelo nome. Duas enumerações declaradas com o mesmo nome em escopos aninhados são, portanto, tratadas como o mesmo tipo.
-------------------------------------------------------------------
### Funcional 2 e Funcional 3

Os módulos `Funcional2` e `Funcional3` não dependem do `Funcional1`: cada um tem a sua própria cópia de toda a hierarquia de classes, com prefixo de pacote próprio (`lf2.plp.*` e `lf3.plp.*`). Por isso a extensão foi portada para cada um deles, com as mesmas classes e a mesma estratégia, sem alterar nenhuma classe existente:

- `TipoEnum`, `TipoUniao`, `ValorEnum`, `ExpChecagemTipo`, `IfThenElseUniao`, `AmbienteCompilacaoFuncional` e `ContextoCompilacaoFuncional` são idênticos aos da LF1, a menos do pacote.
- `DecEnum` implementa a interface de declarações dessas linguagens, que tem `incluir(..., boolean)` e `reduzir`. Na redução, cada constante é substituída pelo seu valor, como as demais variáveis.
- `AplicacaoEstendida` estende a `Aplicacao` da LF2, em que a função aplicada pode ser qualquer expressão (um identificador, uma função anônima `fn x . e` ou uma aplicação). Aplicar uma expressão cujo tipo não é função, como uma união, é um erro de tipo.
- `ProgramaEstendido` estende o `Programa` de cada linguagem.

Como funções (e, na LF3, listas) são valores nessas linguagens, elas também podem ser membros de uma união:

```
let var f = if (1 == 1) then fn x . x + 1 else 0
in if f is Inteiro then f else f(2)          -- resultado: 3
```

O `is` só testa tipos primitivos e enumerações: uma função ou uma lista nunca é `Inteiro`, `Booleano`, `String` ou um enum. Os exemplos de cada linguagem estão em `Funcional2/exemplos/` e `Funcional3/exemplos/`.
-------------------------------------------------------------------
### Executando os exemplos

Os exemplos da seção 8 estão em `Funcional1/exemplos/`, um por arquivo. O `exec:java` do Maven sempre lê o arquivo `input`, então, para rodar outro arquivo, compile e chame o interpretador diretamente:

```bash
cd Funcional1
mvn clean generate-sources compile
java -cp target/classes lf1.plp.functional1.parser.Func1Parser exemplos/8_3_funcao_inteiro_ou_string
```

## 10. Referências

- SAMPAIO, A. *Linguagem Funcional 1*. Material da disciplina de Paradigmas de Linguagens de Programação, CIn-UFPE. Disponível em: <https://augustosampaio.github.io/PLP/linguagens/funcional1>.
- PIERCE, B. C. *Types and Programming Languages*. MIT Press, 2002. (Capítulo 11, seções sobre somas e variantes.)
- TOBIN-HOCHSTADT, S.; FELLEISEN, M. *Logical Types for Untyped Languages*. ICFP, 2010. (Base do estreitamento de tipos por testes em condicionais, também chamado de *occurrence typing*.)
