package projetoJogoXadrez.app;

import projetoJogoXadrez.xadrez.PartidaDeXadrez;

public class Program {

	public static void main(String[] args) {
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		UI.printTabuleiro(partidaDeXadrez.getPecas());
		
	}
}
