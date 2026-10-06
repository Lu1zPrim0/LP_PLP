package lf3.plp.functional1.util;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.expressions2.expression.Id;

/**
 * Tipo de uma enumeracao declarada pelo programador.
 *
 * Exemplo: let enum Cor = Vermelho | Verde | Azul in ...
 *
 * O tipo eh nominal: dois tipos enumerados sao iguais apenas se tiverem o
 * mesmo nome.
 */
public class TipoEnum implements Tipo {

	private Id nome;

	public TipoEnum(Id nome) {
		this.nome = nome;
	}

	public Id getId() {
		return nome;
	}

	public String getNome() {
		return nome.toString();
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
		if (tipo instanceof TipoEnum) {
			return this.nome.equals(((TipoEnum) tipo).nome);
		}
		return false;
	}

	public boolean eValido() {
		return nome != null;
	}

	public Tipo intersecao(Tipo outroTipo) {
		if (outroTipo.eIgual(this))
			return this;
		else
			return null;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TipoEnum && nome.equals(((TipoEnum) obj).nome);
	}

	@Override
	public int hashCode() {
		return nome.hashCode();
	}

	@Override
	public String toString() {
		return getNome();
	}
}
