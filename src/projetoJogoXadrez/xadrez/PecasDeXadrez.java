package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Peca;
import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;

public abstract class PecasDeXadrez extends Peca {

	private Color color;

	public PecasDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	protected boolean existePecaAdversaria(Posicao posicao) {
		PecasDeXadrez p = (PecasDeXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getColor() != color;
	}
}