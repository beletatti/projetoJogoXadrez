package projetoJogoXadrez.app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import projetoJogoXadrez.xadrez.PartidaDeXadrez;
import projetoJogoXadrez.xadrez.PecasDeXadrez;
import projetoJogoXadrez.xadrez.PosicaoXadrez;
import projetoJogoXadrez.xadrez.XadrezException;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		List<PecasDeXadrez> capturados = new ArrayList<>();

		while (true) {
			try {
				UI.clearScreen();
				UI.printPartida(partidaDeXadrez, capturados);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoDaPecaXadrez(sc);

				boolean[][] possiveisMovimentos = partidaDeXadrez.possiveisMovimentos(origem);
				UI.clearScreen();
				UI.printTabuleiro(partidaDeXadrez.getPecas(), possiveisMovimentos);

				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = UI.lerPosicaoDaPecaXadrez(sc);

				PecasDeXadrez capturandoPeca = partidaDeXadrez.performarMovimentoXadrez(origem, destino);

				if (capturandoPeca != null) {
					capturados.add(capturandoPeca);
				}

			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}