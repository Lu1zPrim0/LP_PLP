---
layout: linguagem
permalink: /linguagens/funcional1
linguagem: Funcional 1
short: Func1
order: 3
---

- Estende a Linguagem de Expressões 2 com funções parametrizadas e recursivas
- O corpo de uma função é uma expressão e a aplicação da função a um argumento retorna um valor
- O contexto inclui dois componentes:
    - mapeamento de identificadores em valores
    - mapeamento de identificadores (nomes de função) em definições de função
- Um programa é uma expressão

**Extensão (projeto): enumerações e união de tipos.** Os elementos marcados com (+) foram acrescentados pela extensão. Os links de `Programa`, `Aplicacao` e `IfThenElse` apontam para as subclasses que implementam as novas regras de tipos.

## Fontes

{% include code-link.html path="Funcional1/src/lf1/plp/functional1/ProgramaEstendido.java" content='Programa' %} ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/Expressao.java" %}

Expressao ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/Valor.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpUnaria.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpBinaria.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/expression/ExpDeclaracao.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/Id.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/expression/AplicacaoEstendida.java" content='Aplicacao' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/expression/IfThenElseUniao.java" content='IfThenElse' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/expression/ExpChecagemTipo.java" %} (+)

Valor ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ValorConcreto.java" %}

ValorConcreto ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ValorInteiro.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ValorBooleano.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ValorString.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/expression/ValorEnum.java" %} (+)

ExpUnaria ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpMenos.java" content='"-" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpNot.java" content='"not" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpLength.java" content='"length" Expressao' %}

ExpBinaria ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpSoma.java" content='Expressao "+" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpSub.java" content='Expressao "-" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpAnd.java" content='Expressao "and" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpOr.java" content='Expressao "or" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpEquals.java" content='Expressao "==" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/expressions2/expression/ExpConcat.java" content='Expressao "++" Expressao' %}

ExpDeclaracao ::= "let" {% include code-link.html path="Funcional1/src/lf1/plp/functional1/declaration/DeclaracaoFuncional.java" %} "in" Expressao

DeclaracaoFuncional ::= {% include code-link.html path="Funcional1/src/lf1/plp/functional1/declaration/DecVariavel.java" %}  
{% include code-indent.html %} | {% include code-link.html path="Funcional1/src/lf1/plp/functional1/declaration/DecFuncao.java" %}  
{% include code-indent.html %} | {% include code-link.html path="Funcional1/src/lf1/plp/functional1/declaration/DecComposta.java" %}
{% include code-indent.html %} | {% include code-link.html path="Funcional1/src/lf1/plp/functional1/declaration/DecEnum.java" %} (+)

DecVariavel ::= "var" Id "=" Expressao

DecFuncao ::= "fun" ListId "=" Expressao

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= Id  \|  Id ListId

Aplicacao ::=  Id"(" ListExp ")"

ListExp ::= Expressao \| Expressao, ListExp

IfThenElse ::= "if" Expressao "then" Expressao "else" Expressao

DecEnum ::= "enum" Id "=" ListConstEnum (+)

ListConstEnum ::= Id \| Id "\|" ListConstEnum (+)

ExpChecagemTipo ::= Expressao "is" Tipo (+)

Tipo ::= {% include code-link.html path="Funcional1/src/lf1/plp/expressions1/util/TipoPrimitivo.java" content='"Inteiro" \| "Booleano" \| "String"' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/TipoEnum.java" content='Id' %} (+)


## Classes Auxiliares

{% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/DefFuncao.java" %}

{% include code-link.html path="Funcional1/src/lf1/plp/expressions1/util/Tipo.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/TipoFuncao.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/TipoPolimorfico.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/TipoEnum.java" %} (+)\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/util/TipoUniao.java" %} (+)

{% include code-link.html path="Funcional1/src/lf1/plp/expressions2/memory/AmbienteCompilacao.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/expressions2/memory/ContextoCompilacao.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/AmbienteCompilacaoFuncional.java" %} (+)\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/ContextoCompilacaoFuncional.java" %} (+)

{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/AmbienteFuncional.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/AmbienteExecucaoFuncional.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/ContextoFuncional.java" %}\
{% include code-link.html path="Funcional1/src/lf1/plp/functional1/memory/ContextoExecucaoFuncional.java" %}

## Exemplos de Programas

{% include code-link.html path="Funcional1/input" %}\
{% include code-link.html path="Funcional1/exemplos/7_4_ramo_inalcancavel" %}\
{% include code-link.html path="Funcional1/exemplos/8_1_enum_casos" %}\
{% include code-link.html path="Funcional1/exemplos/8_2_enum_variavel" %}\
{% include code-link.html path="Funcional1/exemplos/8_3_funcao_inteiro_ou_string" %}\
{% include code-link.html path="Funcional1/exemplos/8_4_valor_uniao" %}\
{% include code-link.html path="Funcional1/exemplos/8_5_is" %}\
{% include code-link.html path="Funcional1/exemplos/8_6_enum_na_uniao" %}\
{% include code-link.html path="Funcional1/exemplos/8_7_enum_else_if" %}\
{% include code-link.html path="Funcional1/exemplos/8_8_uniao_tres_tipos" %}\
{% include code-link.html path="Funcional1/exemplos/8_9_funcao_retorna_uniao" %}\
{% include code-link.html path="Funcional1/exemplos/8_10_estreitamento_sem_eliminar" %}\
{% include code-link.html path="Funcional1/exemplos/8_11_erro_enums_diferentes" %}\
{% include code-link.html path="Funcional1/exemplos/8_11_erro_not_is" %}\
{% include code-link.html path="Funcional1/exemplos/8_11_erro_soma_uniao" %}

## Parser

{% include code-link.html path="Funcional1/src/lf1/plp/functional1/parser/Functional1.jj" %}