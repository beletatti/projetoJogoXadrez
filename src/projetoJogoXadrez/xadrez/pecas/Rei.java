package projetoJogoXadrez.xadrez.pecas;

import projetoJogoXadrez.tabuleiro.Tabuleiro;
import projetoJogoXadrez.xadrez.Color;
import projetoJogoXadrez.xadrez.PecasDeXadrez;

public class Rei extends PecasDeXadrez{

	public Rei(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}

	@Override
	public String toString() {
		return "R";
	}
}
