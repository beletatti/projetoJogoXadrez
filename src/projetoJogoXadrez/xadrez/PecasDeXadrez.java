package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Peca;
import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;

public abstract class PecasDeXadrez extends Peca {

	private Color color;
	private int moveCount;

	public PecasDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void increaseMoveCount() {
		moveCount++;
	}

	public void decreaseMoveCount() {
		moveCount--;
	}

	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.fromPosicao(posicao);
	}

	protected boolean existePecaAdversaria(Posicao posicao) {
		PecasDeXadrez p = (PecasDeXadrez) getTabuleiro().peca(posicao);
		return p != null && p.getColor() != color;
	}
}