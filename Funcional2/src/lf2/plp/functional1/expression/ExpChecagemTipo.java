package lf2.plp.functional1.expression;

import lf2.plp.expressions1.util.Tipo;
import lf2.plp.expressions1.util.TipoPrimitivo;
import lf2.plp.expressions2.expression.Expressao;
import lf2.plp.expressions2.expression.Valor;
import lf2.plp.expressions2.expression.ValorBooleano;
import lf2.plp.expressions2.expression.ValorInteiro;
import lf2.plp.expressions2.expression.ValorString;
import lf2.plp.expressions2.memory.AmbienteCompilacao;
import lf2.plp.expressions2.memory.AmbienteExecucao;
import lf2.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf2.plp.functional1.memory.AmbienteCompilacaoFuncional;
import lf2.plp.functional1.util.TipoEnum;

/**
 * Expressao de verificacao de tipo: e is T
 *
 * Resulta em <code>true</code> se o valor de e for do tipo T, e em
 * <code>false</code> caso contrario. T pode ser Inteiro, Booleano, String ou
 * o nome de uma enumeracao visivel no escopo.
 */
public class ExpChecagemTipo implements Expressao {

	private Expressao expressao;
	private Tipo tipo;

	public ExpChecagemTipo(Expressao expressao, Tipo tipo) {
		this.expressao = expressao;
		this.tipo = tipo;
	}

	public Expressao getExpressao() {
		return expressao;
	}

	public Tipo getTipoTestado() {
		return tipo;
	}

	public Valor avaliar(AmbienteExecucao ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Valor valor = expressao.avaliar(ambiente);
		return new ValorBooleano(valorEhDoTipo(valor));
	}

	/**
	 * Verifica, em tempo de execucao, se o valor eh do tipo testado. Apenas
	 * valores primitivos e constantes de enumeracao podem ser testados; outros
	 * valores (como funcoes) nunca sao de nenhum desses tipos.
	 */
	private boolean valorEhDoTipo(Valor valor) {
		if (valor instanceof ValorInteiro)
			return tipo == TipoPrimitivo.INTEIRO;
		if (valor instanceof ValorBooleano)
			return tipo == TipoPrimitivo.BOOLEANO;
		if (valor instanceof ValorString)
			return tipo == TipoPrimitivo.STRING;
		if (valor instanceof ValorEnum)
			return tipo.eIgual(((ValorEnum) valor).getTipo(null));
		return false;
	}

	/**
	 * Realiza a verificacao de tipos desta expressao. Se T for o nome de uma
	 * enumeracao, ela deve estar declarada no escopo.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return <code>true</code> se os tipos da expressao sao validos;
	 *         <code>false</code> caso contrario.
	 */
	public boolean checaTipo(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		boolean result = expressao.checaTipo(amb);
		if (result && tipo instanceof TipoEnum) {
			try {
				((AmbienteCompilacaoFuncional) amb).getEnum(((TipoEnum) tipo)
						.getId());
			} catch (VariavelNaoDeclaradaException e) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return o tipo Booleano.
	 */
	public Tipo getTipo(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		return TipoPrimitivo.BOOLEANO;
	}

	@Override
	public String toString() {
		return String.format("%s is %s", expressao, tipo);
	}

	public Expressao reduzir(AmbienteExecucao ambiente) {
		this.expressao = this.expressao.reduzir(ambiente);
		return this;
	}

	public ExpChecagemTipo clone() {
		return new ExpChecagemTipo(this.expressao.clone(), this.tipo);
	}
}
