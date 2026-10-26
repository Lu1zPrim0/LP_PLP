---
layout: linguagem
permalink: /linguagens/funcional2
linguagem: Funcional 2
short: Func2
order: 4
---

- Estende a Linguagem Funcional 1 com funções de alta ordem
- Uma função passa a ser um valor
- O contexto inclui um único componente:
    - mapeamento de identificadores em valores
- Portanto, o resultado da avaliação de uma expressão pode ser uma função, uma função pode ser argumento de outra função, ...
- Um programa é uma expressão

**Extensão (projeto): enumerações e união de tipos.** Os elementos marcados com (+) foram acrescentados pela extensão. Os links de `Programa`, `Aplicacao` e `IfThenElse` apontam para as subclasses que implementam as novas regras de tipos.


## Fontes

{% include code-link.html path="Funcional2/src/lf2/plp/functional2/ProgramaEstendido.java" content='Programa' %} ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/Expressao.java" %}

Expressao ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/Valor.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpUnaria.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpBinaria.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional2/expression/ExpDeclaracao.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/Id.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional2/expression/AplicacaoEstendida.java" content='Aplicacao' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/expression/IfThenElseUniao.java" content='IfThenElse' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/expression/ExpChecagemTipo.java" %} (+)

Valor ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ValorConcreto.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional2/expression/ValorAbstrato.java" %}

ValorAbstrato ::= {% include code-link.html path="Funcional2/src/lf2/plp/functional2/expression/ValorFuncao.java" %}

ValorConcreto ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ValorInteiro.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ValorBooleano.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ValorString.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/expression/ValorEnum.java" %} (+)

ValorFuncao ::= "fn" ListId "." Expressao

ExpUnaria ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpMenos.java" content='"-" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpNot.java" content='"not" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpLength.java" content='"length" Expressao' %}

ExpBinaria ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpSoma.java" content='Expressao "+" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpSub.java" content='Expressao "-" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpAnd.java" content='Expressao "and" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpOr.java" content='Expressao "or" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpEquals.java" content='Expressao "==" Expressao' %}  
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/expressions2/expression/ExpConcat.java" content='Expressao "++" Expressao' %}

ExpDeclaracao ::= "let" {% include code-link.html path="Funcional2/src/lf2/plp/functional1/declaration/DeclaracaoFuncional.java" %} "in" Expressao

DeclaracaoFuncional ::= {% include code-link.html path="Funcional2/src/lf2/plp/functional1/declaration/DecVariavel.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional2/declaration/DecFuncao.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/declaration/DecComposta.java" %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/declaration/DecEnum.java" %} (+)

DecVariavel ::= "var" Id "=" Expressao

DecFuncao ::= "fun" ListId "=" Expressao

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= Id \| Id ListId

Aplicacao ::= Expressao "(" ListExp ")"

ListExp ::= Expressao \| Expressao ", " ListExp

DecEnum ::= "enum" Id "=" ListConstEnum (+)

ListConstEnum ::= Id \| Id "\|" ListConstEnum (+)

ExpChecagemTipo ::= Expressao "is" Tipo (+)

Tipo ::= {% include code-link.html path="Funcional2/src/lf2/plp/expressions1/util/TipoPrimitivo.java" content='"Inteiro" \| "Booleano" \| "String"' %}\
{% include code-indent.html %}| {% include code-link.html path="Funcional2/src/lf2/plp/functional1/util/TipoEnum.java" content='Id' %} (+)


## Classes auxiliares

{% include code-link.html path="Funcional2/src/lf2/plp/functional2/util/PartialInstantiatorVisitor.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/Ambiente.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/AmbienteCompilacao.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/AmbienteExecucao.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/Contexto.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/ContextoCompilacao.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/expressions2/memory/ContextoExecucao.java" %}\
{% include code-link.html path="Funcional2/src/lf2/plp/functional1/util/TipoEnum.java" %} (+)\
{% include code-link.html path="Funcional2/src/lf2/plp/functional1/util/TipoUniao.java" %} (+)\
{% include code-link.html path="Funcional2/src/lf2/plp/functional1/memory/AmbienteCompilacaoFuncional.java" %} (+)\
{% include code-link.html path="Funcional2/src/lf2/plp/functional1/memory/ContextoCompilacaoFuncional.java" %} (+)

## Exemplos de Programas

{% include code-link.html path="Funcional2/input" %}\
{% include code-link.html path="Funcional2/exemplos/7_4_ramo_inalcancavel" %}\
{% include code-link.html path="Funcional2/exemplos/8_1_enum_casos" %}\
{% include code-link.html path="Funcional2/exemplos/8_2_enum_variavel" %}\
{% include code-link.html path="Funcional2/exemplos/8_3_funcao_inteiro_ou_string" %}\
{% include code-link.html path="Funcional2/exemplos/8_4_valor_uniao" %}\
{% include code-link.html path="Funcional2/exemplos/8_5_is" %}\
{% include code-link.html path="Funcional2/exemplos/8_6_enum_na_uniao" %}\
{% include code-link.html path="Funcional2/exemplos/8_7_enum_else_if" %}\
{% include code-link.html path="Funcional2/exemplos/8_8_uniao_tres_tipos" %}\
{% include code-link.html path="Funcional2/exemplos/8_9_funcao_retorna_uniao" %}\
{% include code-link.html path="Funcional2/exemplos/8_10_estreitamento_sem_eliminar" %}\
{% include code-link.html path="Funcional2/exemplos/8_11_erro_enums_diferentes" %}\
{% include code-link.html path="Funcional2/exemplos/8_11_erro_not_is" %}\
{% include code-link.html path="Funcional2/exemplos/8_11_erro_soma_uniao" %}\
{% include code-link.html path="Funcional2/exemplos/9_1_uniao_com_funcao" %}\
{% include code-link.html path="Funcional2/exemplos/9_2_lambda_com_is" %}\
{% include code-link.html path="Funcional2/exemplos/9_3_enum_alta_ordem" %}\
{% include code-link.html path="Funcional2/exemplos/9_4_erro_aplica_uniao" %}\
{% include code-link.html path="Funcional2/exemplos/9_5_funcao_nao_e_inteiro" %}

## Parser

{% include code-link.html path="Funcional2/src/lf2/plp/functional2/parser/Funcional2.jj" %}