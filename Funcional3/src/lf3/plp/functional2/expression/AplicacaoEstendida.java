package lf3.plp.functional2.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.expressions2.expression.Expressao;
import lf3.plp.expressions2.expression.Id;
import lf3.plp.expressions2.memory.AmbienteCompilacao;
import lf3.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf3.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf3.plp.functional1.util.TipoFuncao;
import lf3.plp.functional1.util.TipoPolimorfico;
import lf3.plp.functional1.util.TipoUniao;

/**
 * Aplicacao de funcao estendida com uniao de tipos: um argumento eh aceito
 * por um parametro de tipo uniao quando o seu tipo esta contido na uniao.
 * Uma expressao cujo tipo nao eh funcao (por exemplo, uma uniao) nao pode ser
 * aplicada. Funcoes sem parametros de tipo uniao sao verificadas como na
 * Aplicacao.
 */
public class AplicacaoEstendida extends Aplicacao {

	public AplicacaoEstendida(Expressao f, List<? extends Expressao> expressoes) {
		super(f, expressoes);
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
	public boolean checaTipo(AmbienteCompilacao ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Tipo tipo = getTipoFuncao(ambiente);
		if (!(tipo instanceof TipoFuncao)) {
			return false;
		}
		TipoFuncao tipoFuncao = (TipoFuncao) tipo;
		if (!temParametroUniao(tipoFuncao)) {
			return super.checaTipo(ambiente);
		}

		List<Tipo> dominio = tipoFuncao.getDominio();
		List<? extends Expressao> argumentos = getArgumentos();
		if (dominio.size() != argumentos.size()) {
			return false;
		}

		boolean result = true;
		Iterator<Tipo> it = dominio.iterator();
		for (Expressao valorReal : argumentos) {
			result &= valorReal.checaTipo(ambiente);

			Tipo tipoArg = valorReal.getTipo(ambiente);
			Tipo tipoDom = it.next();

			if (tipoDom instanceof TipoUniao) {
				result &= ((TipoUniao) tipoDom).contem(tipoArg);
			} else {
				result &= tipoArg.eIgual(tipoDom);
			}
		}
		limparTiposCuringas(tipoFuncao);
		return result;
	}

	/**
	 * Retorna o tipo da expressao aplicada, como faz a Aplicacao: se ela for
	 * um identificador ou uma funcao anonima, seu proprio tipo; caso
	 * contrario, uma funcao ainda sem tipo definido.
	 */
	private Tipo getTipoFuncao(AmbienteCompilacao ambiente) {
		Tipo tipoFuncao = null;
		if (getFunc() instanceof Id) {
			tipoFuncao = ambiente.get((Id) getFunc());
		} else if (getFunc() instanceof ValorFuncao) {
			tipoFuncao = ((ValorFuncao) getFunc()).getTipo(ambiente);
		}

		if (tipoFuncao == null || tipoFuncao instanceof TipoPolimorfico) {
			ArrayList<Tipo> params = new ArrayList<Tipo>();
			for (Expressao valorReal : getArgumentos()) {
				params.add(valorReal.getTipo(ambiente));
			}
			tipoFuncao = new TipoFuncao(params, new TipoPolimorfico());
		}
		return tipoFuncao;
	}

	private List<? extends Expressao> getArgumentos() {
		if (getArgsExpressao() == null) {
			return Collections.<Expressao> emptyList();
		}
		return getArgsExpressao();
	}

	private boolean temParametroUniao(TipoFuncao tipoFuncao) {
		for (Tipo tipoDom : tipoFuncao.getDominio()) {
			if (tipoDom instanceof TipoUniao)
				return true;
		}
		return false;
	}

	/**
	 * Limpa os tipos curingas instanciados nesta aplicacao, como faz o
	 * TipoFuncao, para nao influenciar a proxima aplicacao.
	 */
	private void limparTiposCuringas(TipoFuncao tipoFuncao) {
		for (Tipo tDom : tipoFuncao.getDominio()) {
			if (tDom instanceof TipoPolimorfico) {
				((TipoPolimorfico) tDom).limpar();
			}
		}
		if (tipoFuncao.getImagem() instanceof TipoPolimorfico) {
			((TipoPolimorfico) tipoFuncao.getImagem()).limpar();
		}
	}

	@Override
	public AplicacaoEstendida clone() {
		ArrayList<Expressao> novaLista = new ArrayList<Expressao>(
				getArgumentos().size());
		for (Expressao exp : getArgumentos()) {
			novaLista.add(exp.clone());
		}
		return new AplicacaoEstendida(getFunc().clone(), novaLista);
	}
}
