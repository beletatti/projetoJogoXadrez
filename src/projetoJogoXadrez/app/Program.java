package projetoJogoXadrez.app;

import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;

public class Program {

	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(8,8);
		
		System.out.println(tabuleiro);
	}
}
