---
layout: linguagem
permalink: /linguagens/funcional3
linguagem: Funcional 3
short: Func3
order: 5
---

* Estende a Linguagem Funcional 2 com listas e compreensão de listas
* Uma Lista é um valor
* Portanto pode ser passado como parâmetro para uma função
* Um programa é uma expressão

**Extensão (projeto): enumerações e união de tipos.** Os elementos marcados com (+) foram acrescentados pela extensão. Os links de `Programa`, `Aplicacao` e `IfThenElse` apontam para as subclasses que implementam as novas regras de tipos.

## Fontes

{% include code-link.html path="Funcional3/src/lf3/plp/functional3/ProgramaEstendido.java" content='Programa' %} ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

Expressao ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Valor.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpUnaria.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpBinaria.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional2/expression/ExpDeclaracao.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional2/expression/AplicacaoEstendida.java" content='Aplicacao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional1/expression/IfThenElseUniao.java" content='IfThenElse' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional1/expression/ExpChecagemTipo.java" %} (+)

Valor ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ValorConcreto.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional2/expression/ValorAbstrato.java" %}

ValorAbstrato ::= {% include code-link.html path="Funcional3/src/lf3/plp/functional2/expression/ValorFuncao.java" %}

ValorConcreto ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ValorInteiro.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ValorBooleano.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ValorString.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ValorLista.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional1/expression/ValorEnum.java" %} (+)

ValorFuncao ::= "fn" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %} {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %} "." {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

ExpUnaria ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpMenos.java" content='"-" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpNot.java" content='"not" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpLength.java" content='"length" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpHead.java" content='head(Expressao)' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpTail.java" content='tail(Expressao)' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpCompreensaoLista.java" %}

ExpCompreensaoLista ::= Expressao {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/Gerador.java" %}\
{% include code-indent.html %} | Expressao Gerador Filtro

Gerador ::= "for" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %} "in" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}\
{% include code-indent.html %} | "for" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %} "in" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %} ["," Gerador]

Filtro ::= "if" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

ExpBinaria ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpSoma.java" content='Expressao "+" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpSub.java" content='Expressao "-" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpMult.java" content='Expressao "*" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpMaiorQue.java" content='Expressao ">" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpMenorQue.java" content='Expressao "<" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpAnd.java" content='Expressao "and" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpOr.java" content='Expressao "or" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpEquals.java" content='Expressao "==" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/ExpConcat.java" content='Expressao "++" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpSequencia.java" content='Expressao ".." Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpCons.java" content='Expressao ":" Expressao' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/expression/ExpConcatLista.java" content='Expressao "^^" Expressao' %}


ExpDeclaracao ::= "let" {% include code-link.html path="Funcional3/src/lf3/plp/functional1/declaration/DeclaracaoFuncional.java" %} "in" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

DeclaracaoFuncional ::= {% include code-link.html path="Funcional3/src/lf3/plp/functional1/declaration/DecVariavel.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional2/declaration/DecFuncao.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional3/declaration/DecComposta.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional1/declaration/DecEnum.java" %} (+)

DecVariavel ::= "var" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %} "=" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

DecFuncao ::= "fun" ListId "=" {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Expressao.java" %}

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/expressions2/expression/Id.java" %}, ListId

Aplicacao ::= Expressao "(" ListExp ")"

ListExp ::= Expressao
{% include code-indent.html %} | Expressao, ListExp

DecEnum ::= "enum" Id "=" ListConstEnum (+)

ListConstEnum ::= Id \| Id "\|" ListConstEnum (+)

ExpChecagemTipo ::= Expressao "is" Tipo (+)

Tipo ::= {% include code-link.html path="Funcional3/src/lf3/plp/expressions1/util/TipoPrimitivo.java" content='"Inteiro" \| "Booleano" \| "String"' %}\
{% include code-indent.html %} | {% include code-link.html path="Funcional3/src/lf3/plp/functional1/util/TipoEnum.java" content='Id' %} (+)


## Classes auxiliares
{% include code-link.html path="Funcional3/src/lf3/plp/expressions2/memory/Ambiente.java" %}\
{% include code-link.html path="Funcional3/src/lf3/plp/expressions2/memory/AmbienteCompilacao.java" %}\
{% include code-link.html path="Funcional3/src/lf3/plp/expressions2/memory/AmbienteExecucao.java" %}\
{% include code-link.html path="Funcional3/src/lf3/plp/functional3/util/TipoLista.java" %}\
{% include code-link.html path="Funcional3/src/lf3/plp/functional3/util/ListaVaziaException.java" %}\
{% include code-link.html path="Funcional3/src/lf3/plp/functional1/util/TipoEnum.java" %} (+)\
{% include code-link.html path="Funcional3/src/lf3/plp/functional1/util/TipoUniao.java" %} (+)\
{% include code-link.html path="Funcional3/src/lf3/plp/functional1/memory/AmbienteCompilacaoFuncional.java" %} (+)\
{% include code-link.html path="Funcional3/src/lf3/plp/functional1/memory/ContextoCompilacaoFuncional.java" %} (+)


## Exemplos de Programas

{% include code-link.html path="Funcional3/input" %}\
{% include code-link.html path="Funcional3/exemplos/7_4_ramo_inalcancavel" %}\
{% include code-link.html path="Funcional3/exemplos/8_1_enum_casos" %}\
{% include code-link.html path="Funcional3/exemplos/8_2_enum_variavel" %}\
{% include code-link.html path="Funcional3/exemplos/8_3_funcao_inteiro_ou_string" %}\
{% include code-link.html path="Funcional3/exemplos/8_4_valor_uniao" %}\
{% include code-link.html path="Funcional3/exemplos/8_5_is" %}\
{% include code-link.html path="Funcional3/exemplos/8_6_enum_na_uniao" %}\
{% include code-link.html path="Funcional3/exemplos/8_7_enum_else_if" %}\
{% include code-link.html path="Funcional3/exemplos/8_8_uniao_tres_tipos" %}\
{% include code-link.html path="Funcional3/exemplos/8_9_funcao_retorna_uniao" %}\
{% include code-link.html path="Funcional3/exemplos/8_10_estreitamento_sem_eliminar" %}\
{% include code-link.html path="Funcional3/exemplos/8_11_erro_enums_diferentes" %}\
{% include code-link.html path="Funcional3/exemplos/8_11_erro_not_is" %}\
{% include code-link.html path="Funcional3/exemplos/8_11_erro_soma_uniao" %}\
{% include code-link.html path="Funcional3/exemplos/9_1_uniao_com_funcao" %}\
{% include code-link.html path="Funcional3/exemplos/9_2_lambda_com_is" %}\
{% include code-link.html path="Funcional3/exemplos/9_3_enum_alta_ordem" %}\
{% include code-link.html path="Funcional3/exemplos/9_4_erro_aplica_uniao" %}\
{% include code-link.html path="Funcional3/exemplos/9_5_funcao_nao_e_inteiro" %}\
{% include code-link.html path="Funcional3/exemplos/10_1_uniao_com_lista" %}\
{% include code-link.html path="Funcional3/exemplos/10_2_lista_nao_e_inteiro" %}\
{% include code-link.html path="Funcional3/exemplos/10_3_enum_em_lista" %}

## Parser

{% include code-link.html path="Funcional3/src/lf3/plp/functional3/parser/Funcional3.jj" %}
