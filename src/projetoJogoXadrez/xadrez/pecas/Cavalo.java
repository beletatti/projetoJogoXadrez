package projetoJogoXadrez.xadrez.pecas;

import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;
import projetoJogoXadrez.xadrez.Color;
import projetoJogoXadrez.xadrez.PecasDeXadrez;

public class Cavalo extends PecasDeXadrez {
	public Cavalo(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}

	@Override
	public String toString() {
		return "C";
	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);

		p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		return mat;
	}

	public boolean podeMover(Posicao posicao) {
		PecasDeXadrez p = (PecasDeXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getColor() != getColor();
	}
}
