package projetoJogoXadrez.tabuleiro;

public class Posicao {

	private int linha;
	private int coluna;

	public Posicao(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int fileira) {
		this.linha = fileira;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	@Override
	public String toString() {
		return linha + ", " + coluna;
	}

	public void setValor(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
}
