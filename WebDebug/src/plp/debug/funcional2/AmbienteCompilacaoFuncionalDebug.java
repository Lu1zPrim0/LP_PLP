package plp.debug.funcional2;

import lf2.plp.expressions2.expression.Id;
import lf2.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf2.plp.functional1.memory.AmbienteCompilacaoFuncional;
import lf2.plp.functional1.util.TipoEnum;

/**
 * Estende {@link AmbienteCompilacaoDebug} para a LF1 com enumerações e união
 * de tipos: além de observar o ambiente, delega o registro das enumerações
 * ({@link AmbienteCompilacaoFuncional}) ao ambiente real. Funcional1 e
 * {@link AmbienteCompilacaoDebug} permanecem inalterados.
 */
public class AmbienteCompilacaoFuncionalDebug extends AmbienteCompilacaoDebug
		implements AmbienteCompilacaoFuncional {

	private final AmbienteCompilacaoFuncional target;

	public AmbienteCompilacaoFuncionalDebug(AmbienteCompilacaoFuncional target) {
		super(target);
		this.target = target;
	}

	@Override
	public void mapEnum(Id id, TipoEnum tipo) throws VariavelJaDeclaradaException {
		target.mapEnum(id, tipo);
	}

	@Override
	public TipoEnum getEnum(Id id) throws VariavelNaoDeclaradaException {
		return target.getEnum(id);
	}
}
