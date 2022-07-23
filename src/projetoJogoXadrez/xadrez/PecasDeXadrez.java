package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Peca;
import projetoJogoXadrez.tabuleiro.Tabuleiro;

public class PecasDeXadrez extends Peca {

	private Color color;

	public PecasDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}