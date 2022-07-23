package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Pecas;
import projetoJogoXadrez.tabuleiro.Tabuleiro;

public class PecasDeXadrez extends Pecas {

	private Color color;

	public PecasDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}