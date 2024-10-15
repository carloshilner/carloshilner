package br.gov.dataprev.dbloreanapp.v01;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AgendaTelaApp extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JList<String> listContatos;
	private DefaultListModel<String> listModel;
	private ArrayList<Contato> contatos; // Lista de contatos
	private JTextField textFieldNome, textFieldTelefone, textFieldEmail;
	private JTextArea textAreaObservacoes; // Área de texto para observações
	private JButton btnAssociarDados, btnSalvarEmJson; // Botões para salvar
	private JComboBox<String> comboBoxTipoTelefone; // ComboBox para o tipo de telefone
	private JTable tableTelefones; // JTable para exibir os telefones
	private DefaultTableModel tableModel; // Modelo da tabela

	private static final String[] COLUMN_NAMES = { "Número", "Tipo" };

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				AgendaTelaApp frame = new AgendaTelaApp();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public AgendaTelaApp() {
		initializeComponents();
		configureMenu();
		configureContentPane();
		configureListSelectionListener();
	}

	private void initializeComponents() {
		contatos = new ArrayList<>();
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
		tableTelefones = new JTable(tableModel);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Configurações gerais da janela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		listModel = new DefaultListModel<>();
		listContatos = new JList<>(listModel);
	}

	private void configureMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Arquivo");
		menuBar.add(mnNewMenu);

		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(e -> abrirArquivoJson());
		mnNewMenu.add(mntmAbrir);
	}

	private void configureContentPane() {
		GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(layout);

		configureContactListPanel();
		configureButtons();
		configureTabbedPane();
	}

	private void configureContactListPanel() {
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);

		panel.setLayout(new GridBagLayout());
		listContatos.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.black, 1, true), "Contatos"));

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		panel.add(listContatos, gbc_list);
	}

	private void configureButtons() {
		JButton btnNewButton = new JButton("+");
		btnNewButton.setPreferredSize(new Dimension(45, 45));
		btnNewButton.addActionListener(e -> adicionarContato());

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		btnSalvarEmJson = new JButton("Salvar em JSON");
		btnSalvarEmJson.addActionListener(e -> salvarDadosEmJson());
		GridBagConstraints gbc_btnSalvarEmJson = new GridBagConstraints();
		gbc_btnSalvarEmJson.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSalvarEmJson.gridx = 0;
		gbc_btnSalvarEmJson.gridy = 1;
		contentPane.add(btnSalvarEmJson, gbc_btnSalvarEmJson);
	}

	private void configureTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 2;
		gbc_tabbedPane.gridy = 0;
		gbc_tabbedPane.gridheight = 2; // TabbedPane ocupa 2 linhas
		contentPane.add(tabbedPane, gbc_tabbedPane);

		configurarAbaDadosBasicos(tabbedPane);
		configurarAbaObservacoes(tabbedPane);
		configurarBotaoAssociarDados(tabbedPane);
	}

	private void configurarAbaDadosBasicos(JTabbedPane tabbedPane) {
		JPanel panelDadosBasicos = new JPanel();
		panelDadosBasicos.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Labels e TextFields para dados básicos
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelDadosBasicos.add(new JLabel("Nome:"), gbc);
		gbc.gridx = 1;
		textFieldNome = new JTextField(20);
		panelDadosBasicos.add(textFieldNome, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelDadosBasicos.add(new JLabel("Email:"), gbc);
		gbc.gridx = 1;
		textFieldEmail = new JTextField(20);
		panelDadosBasicos.add(textFieldEmail, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panelDadosBasicos.add(new JLabel("Telefone:"), gbc);
		gbc.gridx = 1;
		textFieldTelefone = new JTextField(10);
		panelDadosBasicos.add(textFieldTelefone, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		panelDadosBasicos.add(new JLabel("Tipo de Telefone:"), gbc);
		gbc.gridx = 1;
		comboBoxTipoTelefone = new JComboBox<>(new String[] { "Fixo", "Celular", "Zap" });
		panelDadosBasicos.add(comboBoxTipoTelefone, gbc);

		// Botões para adicionar/remover telefone
		JButton buttonAdicionarTelefone = new JButton("Adicionar Telefone");
		buttonAdicionarTelefone.addActionListener(e -> adicionarTelefone());
		gbc.gridx = 0;
		gbc.gridy = 4;
		panelDadosBasicos.add(buttonAdicionarTelefone, gbc);

		JButton buttonRemoverTelefone = new JButton("Remover Telefone");
		buttonRemoverTelefone.addActionListener(e -> removerTelefone());
		gbc.gridx = 1;
		panelDadosBasicos.add(buttonRemoverTelefone, gbc);

		// Adiciona a JTable com scroll
		JScrollPane scrollPane = new JScrollPane(tableTelefones);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		panelDadosBasicos.add(scrollPane, gbc);

		// Adiciona a aba com o novo painel
		tabbedPane.addTab("Dados Básicos", panelDadosBasicos);
	}

	private void configurarAbaObservacoes(JTabbedPane tabbedPane) {
		textAreaObservacoes = new JTextArea();
		tabbedPane.addTab("Observações", new JScrollPane(textAreaObservacoes));
	}

	private void configurarBotaoAssociarDados(JTabbedPane tabbedPane) {
		btnAssociarDados = new JButton("Associar Dados");
		btnAssociarDados.addActionListener(e -> associarDados());
		tabbedPane.addTab("Associar Dados", btnAssociarDados);
	}

	private void configureListSelectionListener() {
		listContatos.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					exibirContatoSelecionado();
				}
			}
		});
	}

	private void adicionarContato() {
		String nome = JOptionPane.showInputDialog("Digite o nome do novo contato:");
		if (nome != null && !nome.trim().isEmpty()) {
			Contato novoContato = new Contato(nome);
			contatos.add(novoContato);
			listModel.addElement(novoContato.getNome());
		}
	}

	private void adicionarTelefone() {
		String numero = textFieldTelefone.getText();
		String tipo = (String) comboBoxTipoTelefone.getSelectedItem();
		tableModel.addRow(new Object[] { numero, tipo });
	}

	private void removerTelefone() {
		int selectedRow = tableTelefones.getSelectedRow();
		if (selectedRow != -1) {
			tableModel.removeRow(selectedRow);
		}
	}

	private void exibirContatoSelecionado() {
		int selectedIndex = listContatos.getSelectedIndex();
		if (selectedIndex != -1) {
			Contato contatoSelecionado = contatos.get(selectedIndex);
			mostrarDetalhesContato(contatoSelecionado);
		}
	}

	private void mostrarDetalhesContato(Contato contato) {
		textFieldNome.setText(contato.getNome());
		textFieldEmail.setText(contato.getEmail());
		textAreaObservacoes.setText(contato.getObservacoes());
		// Limpa a tabela atual
		tableModel.setRowCount(0);
		for (Telefone telefone : contato.getTelefones()) {
			tableModel.addRow(new Object[] { telefone.getNumero(), telefone.getTipo() });
		}
	}

	private void associarDados() {
		int selectedIndex = listContatos.getSelectedIndex();
		if (selectedIndex != -1) {
			Contato contato = contatos.get(selectedIndex);

			// Atualiza os dados do contato com as informações dos JTextFields
			contato.setNome(textFieldNome.getText());
			contato.setEmail(textFieldEmail.getText());
			contato.setObservacoes(textAreaObservacoes.getText());

//			contato.setTelefone(textFieldTelefone.getText());
//			contato.setTipoTelefone((String) comboBoxTipoTelefone.getSelectedItem()); // Salvar o tipo de telefone

			JOptionPane.showMessageDialog(this, "Dados salvos para o contato: " + contato.getNome());
		}
	}

	private void salvarDadosEmJson() {
		JSONArray jsonArray = new JSONArray();
		for (Contato contato : contatos) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("nome", contato.getNome());
			jsonObject.put("email", contato.getEmail());
			jsonObject.put("observacoes", contato.getObservacoes());

			JSONArray telefonesArray = new JSONArray();
			for (Telefone telefone : contato.getTelefones()) {
				JSONObject telefoneObject = new JSONObject();
				telefoneObject.put("numero", telefone.getNumero());
				telefoneObject.put("tipo", telefone.getTipo());
				telefonesArray.put(telefoneObject);
			}
			jsonObject.put("telefones", telefonesArray); // Salva a lista de telefones

			jsonArray.put(jsonObject);
		}

		try (FileWriter file = new FileWriter("contatos.json")) {
			file.write(jsonArray.toString(4)); // Escreve o JSON formatado
			JOptionPane.showMessageDialog(this, "Dados salvos com sucesso em contatos.json");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao salvar os dados em JSON: " + e.getMessage());
		}
	}

	private void abrirArquivoJson() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try (FileReader reader = new FileReader(file)) {
				JSONTokener tokener = new JSONTokener(reader);
				JSONArray jsonArray = new JSONArray(tokener);

				contatos.clear();
				listModel.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Contato contato = new Contato(jsonObject.getString("nome"));
					contato.setEmail(jsonObject.getString("email"));
					contato.setObservacoes(jsonObject.getString("observacoes"));

					JSONArray telefonesArray = jsonObject.getJSONArray("telefones");
					for (int j = 0; j < telefonesArray.length(); j++) {
						JSONObject telefoneObject = telefonesArray.getJSONObject(j);
						Telefone telefone = new Telefone(telefoneObject.getString("numero"),
								telefoneObject.getString("tipo"));
						contato.adicionarTelefone(telefone); // Adiciona cada telefone ao contato
					}
					contatos.add(contato);
					listModel.addElement(contato.getNome());
				}
				JOptionPane.showMessageDialog(this, "Dados carregados com sucesso de " + file.getName());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Erro ao carregar os dados: " + e.getMessage());
			}
		}
	}
}
