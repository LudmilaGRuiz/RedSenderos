package model;

public class Sendero implements Comparable<Sendero>{
	private Estacion inicio;
	private Estacion fin;
	private int impacto; // 1 a 10

	public Sendero(Estacion inicio, Estacion fin, int impacto) {
		this.inicio = inicio;
		this.fin = fin;
		this.impacto = impacto;
	}
	
    @Override
    public int compareTo(Sendero otro) {
        return Integer.compare(this.impacto, otro.impacto);
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
