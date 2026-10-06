package lf3.plp.functional1.expression;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.expressions2.expression.ValorConcreto;
import lf3.plp.expressions2.memory.AmbienteCompilacao;
import lf3.plp.functional1.util.TipoEnum;

/**
 * Objetos desta classe encapsulam uma constante de enumeracao. O valor eh o
 * proprio nome da constante.
 */
public class ValorEnum extends ValorConcreto<String> {

	private TipoEnum tipo;

	/**
	 * Cria <code>ValorEnum</code> com o nome da constante e o tipo da
	 * enumeracao que a declarou.
	 */
	public ValorEnum(String valor, TipoEnum tipo) {
		super(valor);
		this.tipo = tipo;
	}

	/**
	 * Retorna o tipo desta constante: a enumeracao que a declarou.
	 *
	 * @param amb
	 *            o ambiente de compilacao.
	 * @return o tipo desta constante.
	 */
	public Tipo getTipo(AmbienteCompilacao amb) {
		return tipo;
	}

	public ValorEnum clone() {
		return new ValorEnum(this.valor(), this.tipo);
	}
}
