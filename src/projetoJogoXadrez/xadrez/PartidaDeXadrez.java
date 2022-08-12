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
import projetoJogoXadrez.xadrez.pecas.Rainha;
import projetoJogoXadrez.xadrez.pecas.Rei;
import projetoJogoXadrez.xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;
	private int turno;
	private Color jogadorAtual;
	private boolean check;
	private boolean checkMate;
	private PecasDeXadrez enPassantVulnerable;

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

	public PecasDeXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
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

		PecasDeXadrez pecaMovida = (PecasDeXadrez) tabuleiro.peca(destino);

		check = (testCheck(oponente(jogadorAtual))) ? true : false;
		if (testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		// MOVIMENTO ESPECIAL: EN PASSANT
		if (pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2
				|| pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() + 2))) {
			enPassantVulnerable = pecaMovida;
		} else {
			enPassantVulnerable = null;
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

	private Peca realizarMovimento(Posicao origem, Posicao destino) {
		PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(origem);
		p.increaseMoveCount();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		// MOVIMENTO ESPECIAL ROQUE REI DO LADO PEQUENO DA TORRE (LADO DIREITO)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.increaseMoveCount();
		}

		// MOVIMENTO ESPECIAL ROQUE REI DO LADO GRANDE DA TORRE (LADO ESQUERDO)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.increaseMoveCount();
		}

		// MOVIMENTO ESPECIAL: EN PASSANT
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if (p.getColor() == Color.WHITE) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removePeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(destino);
		p.decreaseMoveCount();
		tabuleiro.colocarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		// MOVIMENTO ESPECIAL ROQUE REI DO LADO PEQUENO DA TORRE (LADO DIREITO)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(destinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);
			torre.decreaseMoveCount();
		}

		// MOVIMENTO ESPECIAL ROQUE REI DO LADO GRANDE DA TORRE (LADO ESQUERDO)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(destinoTorre);
			tabuleiro.colocarPeca(pecaCapturada, origemTorre);
			torre.decreaseMoveCount();
		}

		// MOVIMENTO ESPECIAL: EN PASSANT
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulnerable) {
				PecasDeXadrez peao = (PecasDeXadrez) tabuleiro.removePeca(destino);
				Posicao posicaoPeao;
				if (p.getColor() == Color.WHITE) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.colocarPeca(peao, posicaoPeao);
			}
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
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Color.WHITE, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Color.WHITE, this));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Color.BLACK, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Color.BLACK, this));
	}
}
