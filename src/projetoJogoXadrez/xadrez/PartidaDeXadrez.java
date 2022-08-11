package projetoJogoXadrez.xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import projetoJogoXadrez.tabuleiro.Peca;
import projetoJogoXadrez.tabuleiro.Posicao;
import projetoJogoXadrez.tabuleiro.Tabuleiro;
import projetoJogoXadrez.xadrez.pecas.Bispo;
import projetoJogoXadrez.xadrez.pecas.Cavalo;
import projetoJogoXadrez.xadrez.pecas.Peao;
import projetoJogoXadrez.xadrez.pecas.Rei;
import projetoJogoXadrez.xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;
	private int turno;
	private Color jogadorAtual;
	private boolean check;
	private boolean checkMate;

	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Color.WHITE;
		configInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Color getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
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

	public PecasDeXadrez performarMovimentoXadrez(PosicaoXadrez origemPosicao, PosicaoXadrez destinoPosicao) {
		Posicao origem = origemPosicao.toPosicao();
		Posicao destino = destinoPosicao.toPosicao();
		validacaoOrigemPosicao(origem);
		validacaoPosicaoDestino(origem, destino);
		Peca capturandoPeca = realizarMovimento(origem, destino);
		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturandoPeca);
			throw new XadrezException("Você não pode se colocar em check.");
		}

		check = (testCheck(oponente(jogadorAtual))) ? true : false;
		if (testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}
		return (PecasDeXadrez) capturandoPeca;
	}

	private void validacaoOrigemPosicao(Posicao posicao) {
		if (!tabuleiro.haUmaPeca(posicao)) {
			throw new XadrezException("Aqui não existe peça na devida posição procurada.");
		}
		if (jogadorAtual != ((PecasDeXadrez) tabuleiro.peca(posicao)).getColor()) {
			throw new XadrezException("A peça escolhida não é sua.");
		}
		if (!tabuleiro.peca(posicao).possivelFazerAlgumMovimento()) {
			throw new XadrezException("Não a movimentos possíveis para a peça escolhida.");
		}
	}

	public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoDeOrigem) {
		Posicao posicao = posicaoDeOrigem.toPosicao();
		validacaoOrigemPosicao(posicao);
		return tabuleiro.peca(posicao).possiveisMovimentos();
	}

	private Peca realizarMovimento(Posicao procura, Posicao destino) {
		PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(procura);
		p.increaseMoveCount();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturarPeca) {
		PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(destino);
		p.decreaseMoveCount();
		tabuleiro.colocarPeca(p, origem);

		if (capturarPeca != null) {
			tabuleiro.colocarPeca(capturarPeca, destino);
			pecasCapturadas.remove(capturarPeca);
			pecasNoTabuleiro.add(capturarPeca);
		}
	}

	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).possivelMovimento(destino)) {
			throw new XadrezException("A peça escolhida não pode ser movida para a posição de destino.");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private void colocarNovaPeca(char coluna, int linha, PecasDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private PecasDeXadrez rei(Color color) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecasDeXadrez) x).getColor() == color)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecasDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não existe Rei da cor " + color + " no tabuleiro");
	}

	private boolean testCheck(Color color) {
		Posicao reiPosicao = rei(color).getPosicaoXadrez().toPosicao();
		List<Peca> pecasDoOponente = pecasNoTabuleiro.stream()
				.filter(x -> ((PecasDeXadrez) x).getColor() == oponente(color)).collect(Collectors.toList());
		for (Peca p : pecasDoOponente) {
			boolean[][] mat = p.possiveisMovimentos();
			if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}

		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecasDeXadrez) x).getColor() == color)
				.collect(Collectors.toList());

		for (Peca p : list) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecasDeXadrez) p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = realizarMovimento(origem, destino);
						boolean testCheck = testCheck(color);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private Color oponente(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private void configInicial() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Color.WHITE));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Color.BLACK));
	}
}
