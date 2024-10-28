package br.gov.dataprev.dbloreanapp.test.mvc;

public class Telefone {
	private String numero;
	private String tipo;

	public Telefone(String numero, String tipo) {
		this.numero = numero;
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public String getTipo() {
		return tipo;
	}
}
