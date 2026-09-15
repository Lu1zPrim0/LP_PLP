# LP_PLP
Projeto da disciplina de Paradigma da Linguagem de Programação (2026.2) - CIn, UFPE. Ministrada pelo professor Augusto Sampaio.

# União de Tipos na Linguagem Funcional I

**Universidade Federal de Pernambuco - Centro de Informática**  
**IN1007 - Paradigmas de Linguagens de Programação**  

**Equipe:**
* Luiz Primo

---

## 1. Escopo e Motivação

Este projeto estende a linguagem **Funcional 1** introduzindo suporte a **Tipos União (*Union Types*)**. O projeto consiste também em se atentar a mecanismos de introspecção e desestruturação de valores pertencentes a esses tipos. Isto é, a linguagem permite que você investigue de qual tipo é o dado armazenado (introspecção/checagem) e  desencapsular o valor com segurança, pela conveniência do tipo que se pede, para usá-lo sem quebrar o programa (desestruturação).

Um Tipo União $T_1 \cup T_2$ (representado por `T1 | T2`) permite que um mesmo identificador ou expressão produza e armazene valores de mais de um tipo distinto em momentos , garantindo contudo a segurança de tipos (*type safety*). O type safety é a natureza de operações válidas sobre um dado tipo de informação, impedindo que tipos incompatíveis interajam de forma errada e causem comportamentos indefinidos ou erros de memória em tempo de execução.

O objetivo é que os valores e tipos na Funcional I permitam ser utilizados em anotações de tipos de parâmetros e retornos de funções.

---

## 2. Elementos Adicionados

### 2.1 Novas Estruturas e Tipos
* **TipoUniao (`Tipo1 | Tipo2`):** Representação do tipo composto que aceita instâncias de `Tipo1` ou `Tipo2`.
* **ValorUniao (`union(tag, valor)`):** Encapsulamento interno (ou taggeamento de variante) para diferenciar a origem do valor contido na união.

### 2.2 Operadores e Expressões Especiais
* **Checagem de Tipo (`e is Tipo`):** Expressão booleana que verifica se o valor em `e` pertence a um determinado tipo da união.
* **Coerção / Injeção (`inj<Tipo>(e)`):** Encapsula explicitamente um valor simples em uma união.
* **Match / Case (`match e with ...`):** Estrutura de controle para desestruturar valores de tipo união com base em suas variantes/tipos.

---
## 3. Exemplos de Código

### 3.1 Declaração e Casamento de Padrões (Match)

```functional
let
  // Função que aceita um tipo União: Inteiro | String
  var processa = fn x . 
    match x with
      case Inteiro i -> i + 1
      case String s -> s ++ "!"
    end
in
  processa(10) // Retorna 11
end;
```

---
#### 4. BFN
#### 4.1 Acrescimo a Funcional 1

```<tipo> ::= <tipo-base>
         | <tipo-uniao>

<tipo-uniao> ::= <tipo-base> "|" <tipo>
               | <tipo-base> "|" <tipo-uniao>

<tipo-base> ::= "Inteiro"
              | "String"
              | "Booleano"
              | "Real"
              | "Unit"

<expressao> ::= ...
              | <checagem-tipo>
              | <injecao>
              | <match>

<checagem-tipo> ::= <expressao> "is" <tipo>

<injecao> ::= "inj" "<" <tipo> ">" "(" <expressao> ")"

<match> ::= "match" <expressao> "with" <casos> "end"

<casos> ::= <caso>
          | <caso> <casos>

<caso> ::= "case" <tipo> <identificador> "->" <expressao>```
