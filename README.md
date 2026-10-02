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
7. [Regras de tipos e semântica](#7-regras-de-tipos-e-semântica)
8. [Exemplos](#8-exemplos)
9. [Plano de implementação](#9-plano-de-implementação)
10. [Referências](#10-referências)

---

## 1. Visão geral

Este projeto estende a **Linguagem Funcional 1 (LF1)**, apresentada na disciplina, com duas construções relacionadas:

- **Enumerações (`enum`)**: tipos definidos pelo programador a partir de um conjunto finito de constantes nomeadas.
- **União de tipos (`T1 | T2`)**: tipos cujos valores podem pertencer a qualquer um dos tipos que os compõem.

As duas extensões são tratadas em conjunto porque uma enumeração é, essencialmente, o **caso mais simples de união de tipos**: uma união cujas variantes não carregam nenhum dado além do próprio nome. Partir do `enum` permite introduzir, de forma gradual, os mecanismos de declaração, checagem e casamento de padrões que depois são generalizados para uniões arbitrárias.

Ambas as extensões são **conservativas**: todo programa válido em LF1 continua válido e com o mesmo significado na linguagem estendida.

## 2. Motivação

Na LF1, toda expressão tem exatamente um tipo, inferido a partir do uso (`Inteiro`, `Booleano` ou `String`). Isso impede situações bastante comuns na prática, como:

- representar um conjunto fechado de alternativas (dias da semana, estados de um pedido, cores) sem recorrer a inteiros ou strings "mágicos";
- escrever uma função que aceite, por exemplo, tanto um `Inteiro` quanto uma `String`, tratando cada caso de forma apropriada.

A união de tipos resolve o segundo problema; a enumeração resolve o primeiro. Em ambos os casos, o objetivo é preservar a **segurança de tipos** (*type safety*): nenhuma operação deve ser aplicada a um valor cujo tipo ela não suporta. Para isso, a linguagem passa a oferecer mecanismos de:

- **introspecção** — descobrir, em tempo de execução, qual tipo concreto um valor de tipo união carrega (`e is T`);
- **desestruturação** — extrair esse valor com o tipo adequado para usá-lo com segurança (`match ... with ... end`).

Como a LF1 não possui anotações de tipo, a extensão também introduz **anotações opcionais** em parâmetros, retornos de funções e declarações de variáveis. Sem elas, não haveria como o programador declarar que um parâmetro tem tipo `Inteiro | String`.

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

Uma enumeração é declarada dentro de um `let`, como as demais declarações da LF1, listando suas constantes separadas por `|`:

```
let enum Cor = Vermelho | Verde | Azul in ...
```

- O nome da enumeração (`Cor`) passa a ser um **tipo** válido no escopo da declaração.
- As constantes são referenciadas de forma **qualificada** (`Cor.Vermelho`). Isso evita conflito com identificadores comuns e deixa explícito a qual enumeração a constante pertence.
- Enumerações são **nominais**: duas enumerações distintas nunca são compatíveis entre si, ainda que tenham as mesmas constantes.
- Valores de enumeração podem ser comparados com `==` e desestruturados com `match`.

**Relação com a união de tipos.** A enumeração `Cor` pode ser vista como a união de três tipos unitários, cada um com um único valor: `{Vermelho} | {Verde} | {Azul}`. A diferença em relação a uma união geral é que as variantes de um `enum` não carregam nenhum dado associado — a própria etiqueta (*tag*) já é o valor. Por isso o `enum` é o ponto de partida natural para a implementação de uniões.

### 5.2 União de tipos

Um tipo união `T1 | T2 | ... | Tn` é composto por dois ou mais tipos simples (primitivos ou enumerações). Um valor desse tipo carrega, internamente, uma etiqueta indicando qual dos tipos ele realmente possui.

- **Normalização**: a ordem dos membros é irrelevante e repetições são descartadas. Assim, `Inteiro | String` e `String | Inteiro` denotam o mesmo tipo, e `Inteiro | Inteiro` equivale a `Inteiro`.
- **Injeção implícita**: um valor de tipo `Ti` pode ser usado onde se espera `T1 | ... | Tn`, desde que `Ti` seja um dos membros. É o que acontece, por exemplo, ao passar `10` para um parâmetro anotado com `Inteiro | String`.
- **Injeção explícita (`inj<T>(e)`)**: encapsula um valor em uma união quando não há anotação que permita inferir o tipo desejado.
- **Checagem de tipo (`e is T`)**: expressão booleana que verifica se o valor de uma união carrega o tipo `T`.
- **Casamento de padrões (`match e with ... end`)**: seleciona um ramo de acordo com o tipo concreto (ou a constante de enumeração) do valor, ligando-o a um identificador já com o tipo específico.

Operações da LF1 não podem ser aplicadas diretamente a um valor de tipo união. Por exemplo, se `x : Inteiro | String`, a expressão `x + 1` é rejeitada pelo verificador de tipos; é preciso antes desestruturar `x` com `match`.

### 5.3 Anotações de tipo

Para que uniões e enumerações possam aparecer em assinaturas, a extensão permite anotar, opcionalmente:

- parâmetros de funções: `fun f (x : Inteiro | String) = ...`
- o tipo de retorno de funções: `fun f (x : Inteiro) : Booleano = ...`
- declarações de variáveis: `var c : Cor = Cor.Azul`

Elementos não anotados continuam tendo seu tipo inferido exatamente como na LF1 original. Tipos união **nunca são inferidos**: eles só surgem a partir de anotações ou de `inj`. Dessa forma, um `if` cujos ramos têm tipos diferentes continua sendo um erro de tipo, como na LF1.

### 5.4 Novos tokens e palavras reservadas

| Elemento | Uso |
|---|---|
| `enum` | declaração de enumeração |
| `is` | checagem de tipo |
| `inj` | injeção explícita em união |
| `match`, `with`, `case`, `end` | casamento de padrões |
| `Inteiro`, `Booleano`, `String` | nomes dos tipos primitivos em anotações |
| `->` | separa o padrão do corpo de um caso |
| `_` | padrão curinga em `match` |

Os símbolos `|`, `:`, `.`, `<` e `>` já existem como tokens no parser da LF1 e passam a ser usados também pelas novas regras.

## 6. BNF estendida

A gramática abaixo é a BNF da LF1 acrescida das regras deste projeto. Para facilitar a leitura:

- `(+)` marca produções ou alternativas **novas**;
- `(*)` marca produções da LF1 que foram **alteradas**;
- `[ ... ]` indica um elemento opcional.

As produções sem marcação permanecem idênticas às da LF1.

```bnf
Programa ::= Expressao

Expressao ::= Valor
            | ExpUnaria
            | ExpBinaria
            | ExpDeclaracao
            | Id
            | Aplicacao
            | IfThenElse
            | ConstanteEnum                                        (+)
            | ExpChecagemTipo                                      (+)
            | ExpInjecao                                           (+)
            | ExpMatch                                             (+)

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
                      | DecEnum                                    (+)

DecVariavel ::= "var" Id [ ":" Tipo ] "=" Expressao                (*)

DecFuncao ::= "fun" Id [ ListParametro ] [ ":" Tipo ] "=" Expressao   (*)

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListParametro ::= Parametro | Parametro ListParametro              (+)

Parametro ::= Id | "(" Id ":" Tipo ")"                             (+)

Aplicacao ::= Id "(" ListExp ")"

ListExp ::= Expressao | Expressao "," ListExp

IfThenElse ::= "if" Expressao "then" Expressao "else" Expressao

(* ---------- Enumerações ---------- *)

DecEnum ::= "enum" Id "=" ListConstEnum                            (+)

ListConstEnum ::= Id | Id "|" ListConstEnum                        (+)

ConstanteEnum ::= Id "." Id                                        (+)

(* ---------- Tipos ---------- *)

Tipo ::= TipoSimples | TipoUniao                                   (+)

TipoUniao ::= TipoSimples "|" Tipo                                 (+)

TipoSimples ::= TipoPrimitivo | Id                                 (+)

TipoPrimitivo ::= "Inteiro" | "Booleano" | "String"                (+)

(* ---------- Introspecção e desestruturação ---------- *)

ExpChecagemTipo ::= Expressao "is" TipoSimples                     (+)

ExpInjecao ::= "inj" "<" TipoUniao ">" "(" Expressao ")"           (+)

ExpMatch ::= "match" Expressao "with" ListCaso "end"               (+)

ListCaso ::= Caso | Caso ListCaso                                  (+)

Caso ::= "case" Padrao "->" Expressao                              (+)

Padrao ::= TipoSimples Id                                          (+)
         | ConstanteEnum
         | "_"
```

**Observações sobre a gramática:**

- `DecFuncao` foi reescrita separando o nome da função (`Id`) da lista de parâmetros. Quando nenhuma anotação é usada, ela reconhece exatamente as mesmas sentenças que `"fun" ListId "=" Expressao` da LF1, o que garante a compatibilidade. O mesmo vale para `DecVariavel`.
- Em `TipoSimples`, o `Id` deve ser o nome de uma enumeração visível no escopo; essa restrição é verificada na checagem de tipos, não na sintaxe.
- Em `Padrao`, a forma `Cor c` (tipo seguido de identificador) e a forma `Cor.Vermelho` (constante) são distinguidas pelo token `.`, o que exige apenas um *lookahead* de dois tokens no JavaCC.
- O terminador `end` em `ExpMatch` evita a ambiguidade que surgiria com `match` aninhados, já que o corpo de um caso é uma expressão arbitrária.
- `is` tem precedência menor que `==`, de modo que `x is Inteiro` funciona como uma expressão booleana completa e pode ser usada diretamente como condição de um `if`.

## 7. Regras de tipos e semântica

Nas regras abaixo, `Γ ⊢ e : T` significa "no ambiente de tipos `Γ`, a expressão `e` tem tipo `T`".

### 7.1 Enumerações

- **Declaração.** `enum E = C1 | ... | Cn` é bem tipada se as constantes `Ci` forem distintas entre si e `E` não redeclarar outro tipo no mesmo escopo. A declaração adiciona `E` ao ambiente como um novo tipo.
- **Constante.** Se `E` está declarada e `C` é uma de suas constantes, então `Γ ⊢ E.C : E`.
- **Igualdade.** Se `Γ ⊢ e1 : E` e `Γ ⊢ e2 : E`, então `Γ ⊢ e1 == e2 : Booleano`. Comparar constantes de enumerações diferentes é erro de tipo.
- **Semântica.** Cada constante é avaliada para um valor de enumeração, identificado pelo par (enumeração, constante). Dois valores são iguais se ambos os componentes coincidirem.

### 7.2 União de tipos

- **Boa formação.** `T1 | ... | Tn` é válido se cada `Ti` for um tipo primitivo ou uma enumeração declarada.
- **Injeção implícita.** Se `Γ ⊢ e : Ti` e `Ti` é membro de `U`, então `e` pode ser usada onde se espera `U`. Em tempo de execução, o valor é encapsulado como (`Ti`, `v`).
- **Injeção explícita.** Se `Γ ⊢ e : Ti` e `Ti` é membro de `U`, então `Γ ⊢ inj<U>(e) : U`.
- **Checagem.** Se `Γ ⊢ e : U` e `T` é membro de `U`, então `Γ ⊢ e is T : Booleano`. Em tempo de execução, o resultado é `true` exatamente quando a etiqueta do valor é `T`. Testar um tipo que não pertence à união é rejeitado, pois o resultado seria sempre `false`.

### 7.3 Casamento de padrões

Para `match e with case P1 -> e1 ... case Pn -> en end`:

- Se `Γ ⊢ e : U` (união), cada padrão `Ti xi` deve ter `Ti` membro de `U`, e o ramo `ei` é verificado no ambiente `Γ` estendido com `xi : Ti`.
- Se `Γ ⊢ e : E` (enumeração), cada padrão deve ser uma constante `E.C` válida.
- Todos os ramos devem ter o mesmo tipo `R`, que é o tipo da expressão `match`.
- O `match` deve ser **exaustivo**: todos os membros da união (ou todas as constantes da enumeração) precisam ser cobertos, seja explicitamente, seja pelo curinga `_`. Um `match` não exaustivo é erro de tipo.
- **Semântica.** O valor de `e` é avaliado e comparado com os padrões na ordem em que aparecem; o primeiro que casar tem seu ramo avaliado, com o identificador do padrão (se houver) ligado ao valor desencapsulado.

### 7.4 Anotações

- Em `var x : T = e`, o tipo de `e` deve ser `T` ou injetável em `T`.
- Em `fun f (x : T) : R = e`, o parâmetro `x` tem tipo `T` no corpo, e o tipo do corpo deve ser `R` ou injetável em `R`.
- Em uma aplicação `f(a)`, cada argumento deve ter o tipo do parâmetro correspondente ou ser injetável nele.

## 8. Exemplos

### 8.1 Enumeração com `match`

```
let enum Cor = Vermelho | Verde | Azul
in let fun codigo (c : Cor) : Inteiro =
         match c with
           case Cor.Vermelho -> 1
           case Cor.Verde    -> 2
           case Cor.Azul     -> 3
         end
   in codigo(Cor.Verde)
```

Resultado: `2`.

### 8.2 Igualdade entre constantes

```
let enum Estado = Aberto | Fechado
in let var s : Estado = Estado.Aberto
   in if (s == Estado.Fechado) then "fechado" else "aberto"
```

Resultado: `"aberto"`.

### 8.3 Função com parâmetro de tipo união

```
let fun tamanho (x : Inteiro | String) : Inteiro =
      match x with
        case Inteiro i -> i
        case String s  -> length s
      end
in tamanho("plp") + tamanho(10)
```

Resultado: `13`. Os argumentos `"plp"` e `10` são injetados implicitamente na união, e ambos os ramos do `match` produzem `Inteiro`, como exige a regra de tipos.

### 8.4 Checagem de tipo com `is`

```
let fun ehTexto (x : Inteiro | Booleano | String) : Booleano = x is String
in ehTexto(true)
```

Resultado: `false`.

### 8.5 Injeção explícita e curinga

```
let var v = inj<Inteiro | Booleano | String>(42)
in match v with
     case Inteiro n -> n + 1
     case _         -> 0
   end
```

Resultado: `43`. Como `v` não possui anotação, o `inj` é necessário para que seu tipo seja a união, e não apenas `Inteiro`.

### 8.6 Enumeração como membro de uma união

```
let enum Cor = Vermelho | Verde | Azul
in let fun descreve (x : Cor | Inteiro) : String =
         match x with
           case Cor c     -> if (c == Cor.Azul) then "azul" else "outra cor"
           case Inteiro n -> "numero"
         end
   in descreve(Cor.Azul)
```

Resultado: `"azul"`.

### 8.7 Programas rejeitados pelo verificador de tipos

```
let fun f (x : Inteiro | String) = x + 1
in f(1)
```
Erro: `+` não se aplica a `Inteiro | String`; é necessário desestruturar `x` antes.

```
let fun f (x : Inteiro | String) : Inteiro =
      match x with
        case Inteiro i -> i
      end
in f(1)
```
Erro: o `match` não é exaustivo (falta o caso `String`).

## 9. Plano de implementação

A implementação seguirá a organização de pacotes já existente no módulo `Funcional1`:

| Componente | Local | Descrição |
|---|---|---|
| `TipoEnum` | `lf1.plp.functional1.util` | Tipo nominal de uma enumeração, com seu nome e conjunto de constantes. Implementa `Tipo`. |
| `TipoUniao` | `lf1.plp.functional1.util` | Conjunto normalizado de tipos simples. Implementa `Tipo`, com `eIgual` e `intersecao` adaptados à relação de pertinência. |
| `ValorEnum` | `lf1.plp.functional1.expression` | Valor de uma constante de enumeração. |
| `ValorUniao` | `lf1.plp.functional1.expression` | Valor etiquetado (tipo concreto, valor) produzido pela injeção. |
| `ConstanteEnum` | `lf1.plp.functional1.expression` | Expressão `E.C`. |
| `ExpChecagemTipo` | `lf1.plp.functional1.expression` | Expressão `e is T`. |
| `ExpInjecao` | `lf1.plp.functional1.expression` | Expressão `inj<U>(e)`. |
| `ExpMatch` e `Caso` | `lf1.plp.functional1.expression` | Casamento de padrões, incluindo a verificação de exaustividade. |
| `DecEnum` | `lf1.plp.functional1.declaration` | Declaração `enum E = C1 \| ... \| Cn`. |
| Alterações | `DecVariavel`, `DecFuncao`, `DefFuncao` | Suporte a anotações opcionais de tipo. |
| Alterações | `AmbienteFuncional` e implementações | Registro dos tipos de enumeração declarados em cada escopo. |
| Alterações | `ExpEquals` | Igualdade entre valores de enumeração. |
| Alterações | `Functional1.jj` | Novos tokens e produções descritos na seção 6. |

A ordem prevista é: primeiro as enumerações (tipo, valor, declaração, igualdade e `match` sobre constantes), depois as anotações de tipo e, por fim, a união de tipos, reaproveitando a infraestrutura de `match` construída para os enums.

## 10. Referências

- SAMPAIO, A. *Linguagem Funcional 1*. Material da disciplina de Paradigmas de Linguagens de Programação, CIn-UFPE. Disponível em: <https://augustosampaio.github.io/PLP/linguagens/funcional1>.
- PIERCE, B. C. *Types and Programming Languages*. MIT Press, 2002. (Capítulo 11, seções sobre somas e variantes.)
