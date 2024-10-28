package br.gov.dataprev.dbloreanapp.test.mvc.model;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import br.gov.dataprev.dbloreanapp.test.mvc.Contato;


public class AgendaModel {
	private List<Contato> contatos;
	private final String FILE_PATH = "contatos.json"; // Caminho do arquivo JSON

	public AgendaModel() {
		this.contatos = new ArrayList<>();
		carregarContatosDeJson();
	}

	// Adiciona um novo contato à lista
	public void addContato(Contato contato) {
		contatos.add(contato);
	}

	// Retorna a lista de contatos
	public List<Contato> getContatos() {
		return contatos;
	}

	// Remove um contato da lista (pode ser implementado conforme necessário)
	public void removeContato(Contato contato) {
		contatos.remove(contato);
	}

	// Salva a lista de contatos em um arquivo JSON
	public void salvarContatosEmJson() throws IOException {
		Gson gson = new Gson();
		try (FileWriter writer = new FileWriter(FILE_PATH)) {
			gson.toJson(contatos, writer);
		}
	}

	// Carrega a lista de contatos de um arquivo JSON
	private void carregarContatosDeJson() {
		Gson gson = new Gson();
		try (FileReader reader = new FileReader(FILE_PATH)) {
			Type contatoListType = new TypeToken<ArrayList<Contato>>() {
			}.getType();
			contatos = gson.fromJson(reader, contatoListType);
			if (contatos == null) {
				contatos = new ArrayList<>(); // Inicializa uma nova lista se o arquivo estiver vazio
			}
		} catch (IOException e) {
			contatos = new ArrayList<>(); // Se ocorrer um erro, inicializa uma nova lista vazia
		}
	}

	public Contato buscarContatoPorNome(String nome) {
		for (Contato contato : contatos) {
			if (contato.getNome().equalsIgnoreCase(nome)) {
				return contato;
			}
		}
		return null;
	}
}
