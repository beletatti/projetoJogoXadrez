package projetoJogoXadrez.app;

import java.util.InputMismatchException;
import java.util.Scanner;

import projetoJogoXadrez.xadrez.PartidaDeXadrez;
import projetoJogoXadrez.xadrez.PecasDeXadrez;
import projetoJogoXadrez.xadrez.PosicaoXadrez;
import projetoJogoXadrez.xadrez.XadrezException;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		
		
		
		while (true){
			try {
			UI.clearScreen();
			UI.printTabuleiro(partidaDeXadrez.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez procurar = UI.lerPosicaoDaPecaXadrez(sc);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lerPosicaoDaPecaXadrez(sc);
			
			PecasDeXadrez capturandoPeca = partidaDeXadrez.performarMovimentoXadrez(procurar, destino);
			}
			catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}
