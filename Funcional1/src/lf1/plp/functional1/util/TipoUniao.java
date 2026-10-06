package lf1.plp.functional1.util;

import static lf1.plp.expressions1.util.ToStringProvider.listToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lf1.plp.expressions1.util.Tipo;

/**
 * Uniao de tipos, por exemplo Inteiro | String.
 *
 * Uma uniao nao tem sintaxe propria: ela surge de um if cujos ramos tem tipos
 * diferentes. Ordem e repeticao nao importam, e unioes dentro de unioes sao
 * achatadas. Uma uniao com um unico membro eh o proprio membro.
 *
 * Um membro que eh um tipo polimorfico nao resolvido funciona como curinga:
 * aceita qualquer tipo. Ele surge quando um parametro de funcao eh testado
 * com is e o ramo else nao restringe o seu tipo.
 *
 * As operacoes da linguagem nao se aplicam a uma uniao: eInteiro, eBooleano e
 * eString retornam <code>false</code>, e eIgual so eh verdadeiro contra um
 * tipo polimorfico ainda nao inferido.
 */
public class TipoUniao implements Tipo {

	private List<Tipo> membros;

	private TipoUniao(List<Tipo> membros) {
		this.membros = membros;
	}

	/**
	 * Retorna a uniao dos dois tipos dados, ja achatada e sem repeticoes. Se
	 * restar um unico tipo, retorna esse tipo.
	 */
	public static Tipo unir(Tipo tipo1, Tipo tipo2) {
		List<Tipo> membros = new ArrayList<Tipo>();
		adicionar(membros, tipo1);
		adicionar(membros, tipo2);
		return criar(membros);
	}

	/**
	 * Retorna o tipo mais especifico conhecido para o tipo dado, seguindo as
	 * inferencias e instanciacoes dos tipos polimorficos.
	 */
	public static Tipo resolver(Tipo tipo) {
		while (tipo instanceof TipoPolimorfico && tipo.eValido()) {
			TipoPolimorfico polimorfico = (TipoPolimorfico) tipo;
			Tipo inferido = polimorfico.inferir();
			if (inferido == TipoPolimorfico.CURINGA) {
				inferido = polimorfico.getTipoInstanciado();
			}
			if (inferido == tipo) {
				break;
			}
			tipo = inferido;
		}
		return tipo;
	}

	private static Tipo criar(List<Tipo> membros) {
		if (membros.isEmpty())
			return null;
		if (membros.size() == 1)
			return membros.get(0);
		return new TipoUniao(membros);
	}

	private static void adicionar(List<Tipo> membros, Tipo tipo) {
		tipo = resolver(tipo);
		if (tipo instanceof TipoUniao) {
			for (Tipo membro : ((TipoUniao) tipo).membros) {
				adicionar(membros, membro);
			}
		} else if (!contemMembro(membros, tipo)) {
			membros.add(tipo);
		}
	}

	private static boolean contemMembro(List<Tipo> membros, Tipo tipo) {
		for (Tipo membro : membros) {
			if (mesmoTipo(membro, tipo))
				return true;
		}
		return false;
	}

	private static boolean ehCuringa(Tipo tipo) {
		return tipo instanceof TipoPolimorfico && !tipo.eValido();
	}

	/**
	 * Compara dois tipos conhecidos sem inferir tipos polimorficos: um curinga
	 * so eh igual a ele mesmo. Tipos de especies diferentes (por exemplo, uma
	 * funcao e um inteiro) nunca sao iguais; a comparacao por especie evita
	 * depender do eIgual de cada tipo nesses casos.
	 */
	public static boolean mesmoTipo(Tipo tipo1, Tipo tipo2) {
		if (ehCuringa(tipo1) || ehCuringa(tipo2))
			return tipo1 == tipo2;
		tipo1 = resolver(tipo1);
		tipo2 = resolver(tipo2);
		if (tipo1.getClass() != tipo2.getClass())
			return false;
		return tipo1.eIgual(tipo2);
	}

	public List<Tipo> getMembros() {
		return Collections.unmodifiableList(membros);
	}

	/**
	 * Verifica se um valor do tipo dado pode ser do tipo desta uniao. Se o
	 * tipo dado for uma uniao, cada um de seus membros deve estar contido
	 * nesta. Um tipo polimorfico ainda nao inferido passa a ser esta uniao.
	 */
	public boolean contem(Tipo tipo) {
		tipo = resolver(tipo);
		if (tipo instanceof TipoUniao) {
			for (Tipo membro : ((TipoUniao) tipo).membros) {
				if (!contem(membro))
					return false;
			}
			return true;
		}
		if (ehCuringa(tipo)) {
			return tipo.eIgual(this);
		}
		for (Tipo membro : membros) {
			if (ehCuringa(membro) || mesmoTipo(membro, tipo))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se o tipo dado pode ser um dos membros desta uniao.
	 */
	public boolean possui(Tipo tipo) {
		for (Tipo membro : membros) {
			if (ehCuringa(membro) || mesmoTipo(membro, tipo))
				return true;
		}
		return false;
	}

	/**
	 * Retorna o tipo formado pelos membros desta uniao, exceto o tipo dado.
	 * Retorna <code>null</code> se nao restar nenhum membro.
	 */
	public Tipo remover(Tipo tipo) {
		List<Tipo> restantes = new ArrayList<Tipo>();
		for (Tipo membro : membros) {
			if (!mesmoTipo(membro, tipo))
				restantes.add(membro);
		}
		return criar(restantes);
	}

	public String getNome() {
		return listToString(membros, " |");
	}

	public boolean eInteiro() {
		return false;
	}

	public boolean eBooleano() {
		return false;
	}

	public boolean eString() {
		return false;
	}

	public boolean eIgual(Tipo tipo) {
		if (tipo instanceof TipoPolimorfico) {
			return tipo.eIgual(this);
		}
		return false;
	}

	public boolean eValido() {
		for (Tipo membro : membros) {
			if (!ehCuringa(membro) && !membro.eValido())
				return false;
		}
		return true;
	}

	public Tipo intersecao(Tipo outroTipo) {
		if (outroTipo.eIgual(this))
			return this;
		else
			return null;
	}

	@Override
	public String toString() {
		return getNome();
	}
}
