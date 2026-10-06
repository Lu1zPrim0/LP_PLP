package lf2.plp.functional1.memory;

import lf2.plp.expressions2.expression.Id;
import lf2.plp.expressions2.memory.Contexto;
import lf2.plp.expressions2.memory.ContextoCompilacao;
import lf2.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf2.plp.functional1.util.TipoEnum;

/**
 * Contexto de compilacao com uma segunda pilha de escopos para as
 * enumeracoes. As duas pilhas sao incrementadas e restauradas juntas.
 */
public class ContextoCompilacaoFuncional extends ContextoCompilacao implements
		AmbienteCompilacaoFuncional {

	private Contexto<TipoEnum> contextoEnum;

	public ContextoCompilacaoFuncional() {
		contextoEnum = new Contexto<TipoEnum>();
	}

	@Override
	public void incrementa() {
		super.incrementa();
		contextoEnum.incrementa();
	}

	@Override
	public void restaura() {
		super.restaura();
		contextoEnum.restaura();
	}

	public void mapEnum(Id id, TipoEnum tipo)
			throws VariavelJaDeclaradaException {
		contextoEnum.map(id, tipo);
	}

	public TipoEnum getEnum(Id id) throws VariavelNaoDeclaradaException {
		return contextoEnum.get(id);
	}
}
