package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Tabuleiro;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8,8);
	}
	
	public PecasDeXadrez[][] getPecas() {
		PecasDeXadrez[][] mat = new PecasDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0;  j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecasDeXadrez) tabuleiro.pecas(i, j);
			}
		}
		return mat;
	}
}
