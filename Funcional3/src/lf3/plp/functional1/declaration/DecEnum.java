package lf3.plp.functional1.declaration;

import static lf3.plp.expressions1.util.ToStringProvider.listToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lf3.plp.expressions2.expression.Id;
import lf3.plp.expressions2.memory.AmbienteCompilacao;
import lf3.plp.expressions2.memory.AmbienteExecucao;
import lf3.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf3.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf3.plp.functional1.expression.ValorEnum;
import lf3.plp.functional1.memory.AmbienteCompilacaoFuncional;
import lf3.plp.functional1.util.TipoEnum;

/**
 * Declaracao de uma enumeracao: enum E = C1 | ... | Cn
 *
 * Introduz o tipo E e as constantes C1, ..., Cn, todas do tipo E. As
 * constantes sao identificadores comuns, com as mesmas regras de escopo das
 * variaveis.
 */
public class DecEnum implements DeclaracaoFuncional {
	private Id id;
	private List<Id> constantes;
	private TipoEnum tipo;

	public DecEnum(Id idArg, List<Id> constantesArg) {
		id = idArg;
		constantes = constantesArg;
		tipo = new TipoEnum(idArg);
	}

	/**
	 * Retorna uma representacao String desta declaracao. Util para depuracao.
	 *
	 * @return uma representacao String desta declaracao.
	 */
	@Override
	public String toString() {
		return String.format("enum %s = %s", id, listToString(constantes, " |"));
	}

	public Id getId() {
		return id;
	}

	public List<Id> getConstantes() {
		return constantes;
	}

	public TipoEnum getTipo() {
		return tipo;
	}

	/**
	 * Realiza a verificacao de tipos desta declaracao: as constantes devem ser
	 * distintas entre si.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return <code>true</code> se a declaracao eh valida;
	 *         <code>false</code> caso contrario.
	 */
	public boolean checaTipo(AmbienteCompilacao ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Set<Id> distintas = new HashSet<Id>(constantes);
		return distintas.size() == constantes.size();
	}

	public DecEnum clone() {
		List<Id> novaLista = new ArrayList<Id>(constantes.size());
		for (Id constante : constantes) {
			novaLista.add(constante.clone());
		}
		return new DecEnum(this.id.clone(), novaLista);
	}

	public void elabora(AmbienteExecucao amb, AmbienteExecucao aux) throws VariavelJaDeclaradaException {
		for (Id constante : constantes) {
			aux.map(constante, new ValorEnum(constante.toString(), tipo));
		}
	}

	public void elabora(AmbienteCompilacao amb, AmbienteCompilacao aux) throws VariavelJaDeclaradaException {
		for (Id constante : constantes) {
			aux.map(constante, tipo);
		}
	}

	public void incluir(AmbienteExecucao amb, AmbienteExecucao aux) throws VariavelJaDeclaradaException {
		for (Id constante : constantes) {
			amb.map(constante, aux.get(constante));
		}
	}

	public void incluir(AmbienteCompilacao amb, AmbienteCompilacao aux, boolean incluirCuringa) throws VariavelJaDeclaradaException {
		for (Id constante : constantes) {
			amb.map(constante, aux.get(constante));
		}
		((AmbienteCompilacaoFuncional) amb).mapEnum(id, tipo);
	}

	/**
	 * Durante a reducao, cada constante ja tem seu valor conhecido: o proprio
	 * ValorEnum. Assim, uma constante usada no corpo de uma funcao eh
	 * substituida pelo seu valor, como acontece com as demais variaveis.
	 */
	public void reduzir(AmbienteExecucao amb) {
		for (Id constante : constantes) {
			amb.map(constante, new ValorEnum(constante.toString(), tipo));
		}
	}

}
