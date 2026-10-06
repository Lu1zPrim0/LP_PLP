package lf1.plp.functional1.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions2.expression.Expressao;
import lf1.plp.expressions2.expression.Id;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf1.plp.functional1.util.TipoFuncao;
import lf1.plp.functional1.util.TipoPolimorfico;
import lf1.plp.functional1.util.TipoUniao;

/**
 * Aplicacao de funcao da LF1 estendida com uniao de tipos: um argumento eh
 * aceito por um parametro de tipo uniao quando o seu tipo esta contido na
 * uniao. Funcoes sem parametros de tipo uniao sao verificadas como na
 * Aplicacao.
 */
public class AplicacaoEstendida extends Aplicacao {

	public AplicacaoEstendida(Id f, List<? extends Expressao> expressoes) {
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
		Tipo aux = ambiente.get(getFunc());
		if (!(aux instanceof TipoFuncao) || !temParametroUniao((TipoFuncao) aux)) {
			return super.checaTipo(ambiente);
		}

		TipoFuncao tipoFuncao = (TipoFuncao) aux;
		List<Tipo> dominio = tipoFuncao.getDominio();
		if (dominio.size() != getArgsExpressao().size()) {
			return false;
		}

		boolean result = true;
		Iterator<Tipo> it = dominio.iterator();
		for (Expressao valorReal : getArgsExpressao()) {
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
				getArgsExpressao().size());
		for (Expressao exp : getArgsExpressao()) {
			novaLista.add(exp.clone());
		}
		return new AplicacaoEstendida(getFunc().clone(), novaLista);
	}
}
