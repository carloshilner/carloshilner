package br.gov.dataprev.dbloreanapp.test.mvc;

import java.util.ArrayList;
import java.util.List;

public class Contato {
	private String nome;
	private String email;
	private String observacoes;
	private List<Telefone> telefones; // Lista de telefones

	public Contato(String nome) {
		this.nome = nome;
		this.telefones = new ArrayList<>();
	}

	public Contato(String nome, String email, String observacoes) {
		this.nome = nome;
		this.email = email;
		this.observacoes = observacoes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void adicionarTelefone(Telefone telefone) {
		this.telefones.add(telefone);
	}

	public void removerTelefone(Telefone telefone) {
		this.telefones.remove(telefone);
	}
}
