package projetoJogoXadrez.xadrez;

import projetoJogoXadrez.tabuleiro.Peca;
import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;
import projetoJogoXadrez.xadrez.pecas.Rei;
import projetoJogoXadrez.xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configInicial();
	}

	public PecasDeXadrez[][] getPecas() {
		PecasDeXadrez[][] mat = new PecasDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecasDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public PecasDeXadrez performarMovimentoXadrez(PosicaoXadrez procurarPosicao, PosicaoXadrez posicaoDestino) {
		Posicao procurar = procurarPosicao.toPosicao();
		Posicao destino = posicaoDestino.toPosicao();
		validacaoProcurarPosicao(procurar);
		Peca capturandoPeca = realizarMovimento(procurar, destino);
		return (PecasDeXadrez) capturandoPeca;
	}
	
	private Peca realizarMovimento(Posicao procura, Posicao destino) {
		Peca p = tabuleiro.removePeca(procura);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		return pecaCapturada;
	}

	private void validacaoProcurarPosicao(Posicao procurar) {
		if(!tabuleiro.haUmaPeca(procurar)) {
			throw new XadrezException("Aqui não existe peça na devida posição procurada.");
		}
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecasDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configInicial() {
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Color.WHITE));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Color.BLACK));
	}
}
