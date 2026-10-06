package lf2.plp.functional1.memory;

import lf2.plp.expressions2.expression.Id;
import lf2.plp.expressions2.memory.AmbienteCompilacao;
import lf2.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf2.plp.functional1.util.TipoEnum;

/**
 * Ambiente de compilacao que, alem dos tipos das variaveis e funcoes, registra
 * as enumeracoes visiveis em cada escopo.
 */
public interface AmbienteCompilacaoFuncional extends AmbienteCompilacao {

	public void mapEnum(Id id, TipoEnum tipo)
			throws VariavelJaDeclaradaException;

	public TipoEnum getEnum(Id id) throws VariavelNaoDeclaradaException;
}
