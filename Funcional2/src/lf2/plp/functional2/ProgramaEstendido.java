package lf2.plp.functional2;

import lf2.plp.expressions2.expression.Expressao;
import lf2.plp.expressions2.memory.AmbienteCompilacao;
import lf2.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf2.plp.functional1.memory.ContextoCompilacaoFuncional;

/**
 * Programa estendido com enumeracoes e uniao de tipos. A verificacao de tipos
 * usa um ambiente que tambem registra as enumeracoes declaradas.
 */
public class ProgramaEstendido extends Programa {

	public ProgramaEstendido(Expressao exp) {
		super(exp);
	}

	@Override
	public boolean checaTipo()
		throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
		AmbienteCompilacao ambComp = new ContextoCompilacaoFuncional();
		return getExpressao().checaTipo(ambComp);
	}

}
