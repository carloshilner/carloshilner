package br.gov.dataprev.dbloreanapp.test.mvc.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import br.gov.dataprev.dbloreanapp.test.mvc.model.AgendaModel;
import br.gov.dataprev.dbloreanapp.test.mvc.view.AgendaView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gov.dataprev.dbloreanapp.test.mvc.Contato;
import br.gov.dataprev.dbloreanapp.test.mvc.Telefone;

public class AgendaController {
	private AgendaView view;
	private AgendaModel model;

	public AgendaController(AgendaView view, AgendaModel model) {
		this.view = view;
		this.model = model;
		initController();
	}

	private void initController() {
		// Inicializa listeners para interações do usuário
		view.getBtnAddContato().addActionListener(e -> addContato());
		view.getBtnAdicionarTelefone().addActionListener(e -> addTelefone());
		view.getBtnRemoverTelefone().addActionListener(e -> removeTelefone());
		view.getBtnAssociarDados().addActionListener(e -> associarDados());
		view.getBtnSalvarEmJson().addActionListener(e -> salvarEmJson());

		// Carrega contatos existentes na lista
		carregarContatos();
	}

	private void carregarContatos() {
		for (Contato contato : model.getContatos()) {
			view.getListModel().addElement(contato.getNome());
		}
	}

	private void addContato() {
		String nome = view.getTextFieldNome().getText();
		String email = view.getTextFieldEmail().getText();
		String observacoes = view.getTextAreaObservacoes().getText();

		if (nome.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Nome não pode estar vazio", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Contato novoContato = new Contato(nome, email, observacoes);
		model.addContato(novoContato);
		view.getListModel().addElement(nome);

		JOptionPane.showMessageDialog(view, "Contato adicionado com sucesso!", "Sucesso",
				JOptionPane.INFORMATION_MESSAGE);

		limparCamposContato();
	}

	private void addTelefone() {
		String numero = view.getTextFieldTelefone().getText();
		String tipo = (String) view.getComboBoxTipoTelefone().getSelectedItem();

		if (numero.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Número de telefone não pode estar vazio", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefaultTableModel tableModel = (DefaultTableModel) view.getTableTelefones().getModel();
		tableModel.addRow(new Object[] { numero, tipo });

		view.getTextFieldTelefone().setText("");
	}

	private void removeTelefone() {
		int selectedRow = view.getTableTelefones().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Selecione um telefone para remover", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefaultTableModel tableModel = (DefaultTableModel) view.getTableTelefones().getModel();
		tableModel.removeRow(selectedRow);
	}

	private void associarDados() {
		int selectedIndex = view.getListContatos().getSelectedIndex();
		if (selectedIndex == -1) {
			JOptionPane.showMessageDialog(view, "Selecione um contato para associar os dados", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Contato contato = model.getContatos().get(selectedIndex);

		// Limpa telefones da tabela e associa os dados do contato selecionado
		DefaultTableModel tableModel = (DefaultTableModel) view.getTableTelefones().getModel();
		tableModel.setRowCount(0);

		for (Telefone telefone : contato.getTelefones()) {
			tableModel.addRow(new Object[] { telefone.getNumero(), telefone.getTipo() });
		}

		view.getTextFieldNome().setText(contato.getNome());
		view.getTextFieldEmail().setText(contato.getEmail());
		view.getTextAreaObservacoes().setText(contato.getObservacoes());
	}

	private void salvarEmJson() {
		try {
			model.salvarContatosEmJson();
			JOptionPane.showMessageDialog(view, "Contatos salvos em JSON com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Erro ao salvar em JSON", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void limparCamposContato() {
		view.getTextFieldNome().setText("");
		view.getTextFieldEmail().setText("");
		view.getTextAreaObservacoes().setText("");

		DefaultTableModel tableModel = (DefaultTableModel) view.getTableTelefones().getModel();
		tableModel.setRowCount(0);
	}

	public void contatoSelecionado(String nomeSelecionado) {
		Contato contato = model.buscarContatoPorNome(nomeSelecionado);
		if (contato != null) {
			view.atualizarDadosContato(contato);
		}
	}
}
