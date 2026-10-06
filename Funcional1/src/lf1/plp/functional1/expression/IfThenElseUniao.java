package lf1.plp.functional1.expression;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions2.expression.Expressao;
import lf1.plp.expressions2.expression.Id;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf1.plp.functional1.util.TipoPolimorfico;
import lf1.plp.functional1.util.TipoUniao;

/**
 * If da LF1 estendido com uniao de tipos.
 *
 * Formacao: se os ramos tem tipos diferentes, o tipo do if eh a uniao deles.
 *
 * Estreitamento: em "if x is T then e1 else e2", com x um identificador, x eh
 * tratado como sendo do tipo T em e1 e como sendo dos demais tipos possiveis
 * em e2. Um ramo que nunca eh executado nao eh verificado e nao contribui para
 * o tipo do if. Se x for um parametro de funcao ainda sem tipo, seu tipo
 * passa a ser a uniao de T com o tipo exigido pelo ramo else.
 *
 * A avaliacao eh a mesma do IfThenElse.
 */
public class IfThenElseUniao extends IfThenElse {

	public IfThenElseUniao(Expressao teste, Expressao thenExpressao,
			Expressao elseExpressao) {
		super(teste, thenExpressao, elseExpressao);
	}

	/**
	 * Realiza a verificacao de tipos desta expressao.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return <code>true</code> se os tipos da expressao sao validos;
	 *         <code>false</code> caso contrario.
	 */
	@Override
	public boolean checaTipo(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Id idTestado = getIdTestado();
		if (idTestado == null) {
			// Mesma ordem do IfThenElse, para preservar a inferencia da LF1.
			boolean ret = getCondicao().checaTipo(amb);
			ret &= getThen().checaTipo(amb);
			ret &= getElseExpressao().checaTipo(amb);

			Tipo condicaoTipo = getCondicao().getTipo(amb);
			Tipo thenTipo = getThen().getTipo(amb);
			Tipo elseTipo = getElseExpressao().getTipo(amb);

			return ret && condicaoTipo.eBooleano()
					&& compativeis(thenTipo, elseTipo);
		}

		// A condicao tem a forma "x is T", que eh sempre booleana.
		boolean ret = getCondicao().checaTipo(amb);
		Tipo tipoTestado = getTipoTestado();
		Tipo tipoId = resolverTipoTestado(amb, idTestado, tipoTestado);
		Tipo tipoThen = tipoNoThen(tipoId, tipoTestado);
		Tipo tipoElse = tipoNoElse(tipoId, tipoTestado);

		if (tipoThen != null) {
			ret &= checaTipoRamo(getThen(), amb, idTestado, tipoThen);
		}
		if (tipoElse != null) {
			ret &= checaTipoRamo(getElseExpressao(), amb, idTestado, tipoElse);
		}
		if (ret && tipoThen != null && tipoElse != null) {
			ret = compativeis(
					getTipoRamo(getThen(), amb, idTestado, tipoThen),
					getTipoRamo(getElseExpressao(), amb, idTestado, tipoElse));
		}
		return ret;
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return os tipos possiveis desta expressao.
	 */
	@Override
	public Tipo getTipo(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Id idTestado = getIdTestado();
		if (idTestado == null) {
			return combinar(getThen().getTipo(amb), getElseExpressao()
					.getTipo(amb));
		}

		Tipo tipoTestado = getTipoTestado();
		Tipo tipoId = resolverTipoTestado(amb, idTestado, tipoTestado);
		Tipo tipoThen = tipoNoThen(tipoId, tipoTestado);
		Tipo tipoElse = tipoNoElse(tipoId, tipoTestado);

		if (tipoElse == null) {
			return getTipoRamo(getThen(), amb, idTestado, tipoThen);
		}
		if (tipoThen == null) {
			return getTipoRamo(getElseExpressao(), amb, idTestado, tipoElse);
		}
		return combinar(getTipoRamo(getThen(), amb, idTestado, tipoThen),
				getTipoRamo(getElseExpressao(), amb, idTestado, tipoElse));
	}

	/**
	 * Retorna o identificador testado quando a condicao tem a forma "x is T".
	 * Caso contrario, retorna <code>null</code>.
	 */
	private Id getIdTestado() {
		if (getCondicao() instanceof ExpChecagemTipo) {
			Expressao testada = ((ExpChecagemTipo) getCondicao())
					.getExpressao();
			if (testada instanceof Id) {
				return (Id) testada;
			}
		}
		return null;
	}

	private Tipo getTipoTestado() {
		return ((ExpChecagemTipo) getCondicao()).getTipoTestado();
	}

	/**
	 * Retorna o tipo atual do identificador testado. Se for um parametro de
	 * funcao ainda sem tipo, infere-o como a uniao do tipo testado com o tipo
	 * que o ramo else exige dele.
	 */
	private Tipo resolverTipoTestado(AmbienteCompilacao amb, Id id,
			Tipo tipoTestado) throws VariavelNaoDeclaradaException,
			VariavelJaDeclaradaException {
		Tipo tipoId = TipoUniao.resolver(amb.get(id));
		if (tipoId instanceof TipoPolimorfico) {
			TipoPolimorfico tipoRestante = new TipoPolimorfico();
			checaTipoRamo(getElseExpressao(), amb, id, tipoRestante);
			tipoId.eIgual(TipoUniao.unir(tipoTestado, tipoRestante));
			tipoId = TipoUniao.resolver(amb.get(id));
		}
		return tipoId;
	}

	/**
	 * Retorna o tipo do identificador no ramo then, ou <code>null</code> se o
	 * ramo nunca for executado.
	 */
	private Tipo tipoNoThen(Tipo tipoId, Tipo tipoTestado) {
		if (tipoId instanceof TipoUniao) {
			return ((TipoUniao) tipoId).possui(tipoTestado) ? tipoTestado
					: null;
		}
		return tipoId.eIgual(tipoTestado) ? tipoTestado : null;
	}

	/**
	 * Retorna o tipo do identificador no ramo else, ou <code>null</code> se o
	 * ramo nunca for executado.
	 */
	private Tipo tipoNoElse(Tipo tipoId, Tipo tipoTestado) {
		if (tipoId instanceof TipoUniao) {
			return ((TipoUniao) tipoId).remover(tipoTestado);
		}
		return tipoId.eIgual(tipoTestado) ? null : tipoId;
	}

	private boolean checaTipoRamo(Expressao ramo, AmbienteCompilacao amb,
			Id id, Tipo tipoId) throws VariavelNaoDeclaradaException,
			VariavelJaDeclaradaException {
		amb.incrementa();
		try {
			amb.map(id, tipoId);
			return ramo.checaTipo(amb);
		} finally {
			amb.restaura();
		}
	}

	private Tipo getTipoRamo(Expressao ramo, AmbienteCompilacao amb, Id id,
			Tipo tipoId) throws VariavelNaoDeclaradaException,
			VariavelJaDeclaradaException {
		amb.incrementa();
		try {
			amb.map(id, tipoId);
			return ramo.getTipo(amb);
		} finally {
			amb.restaura();
		}
	}

	/**
	 * Ramos com tipos conhecidos sao sempre compativeis: se forem diferentes,
	 * formam uma uniao. Se algum tipo ainda nao foi inferido, os tipos sao
	 * unificados como no IfThenElse.
	 */
	private boolean compativeis(Tipo thenTipo, Tipo elseTipo) {
		if (thenTipo.eValido() && elseTipo.eValido()) {
			return true;
		}
		return thenTipo.eIgual(elseTipo);
	}

	private Tipo combinar(Tipo thenTipo, Tipo elseTipo) {
		if (thenTipo.eValido() && elseTipo.eValido()) {
			if (thenTipo.eIgual(elseTipo)) {
				return thenTipo;
			}
			return TipoUniao.unir(thenTipo, elseTipo);
		}
		return thenTipo.intersecao(elseTipo);
	}

	@Override
	public IfThenElseUniao clone() {
		return new IfThenElseUniao(getCondicao().clone(), getThen().clone(),
				getElseExpressao().clone());
	}
}
