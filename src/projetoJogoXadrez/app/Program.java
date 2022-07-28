package projetoJogoXadrez.app;

import java.util.Scanner;

import projetoJogoXadrez.xadrez.PartidaDeXadrez;
import projetoJogoXadrez.xadrez.PecasDeXadrez;
import projetoJogoXadrez.xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		
		
		
		while (true){
			UI.printTabuleiro(partidaDeXadrez.getPecas());
			System.out.println();
			System.out.println("Origem: ");
			PosicaoXadrez procurar = UI.lerPosicaoDaPecaXadrez(sc);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lerPosicaoDaPecaXadrez(sc);
			
			PecasDeXadrez capturandoPeca = partidaDeXadrez.performarMovimentoXadrez(procurar, destino);
		}
	}
} 
