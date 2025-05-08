package model;

public class Sendero {
	private Estacion inicio;
	private Estacion fin;
	private int impacto; // 1 a 10

	public Sendero(Estacion inicio, Estacion fin, int impacto) {
		this.inicio = inicio;
		this.fin = fin;
		this.impacto = impacto;
	}

	// Getters
	public Estacion getInicio() {
		return inicio;
	}

	public Estacion getFin() {
		return fin;
	}

	public int getImpacto() {
		return impacto;
	}
}
