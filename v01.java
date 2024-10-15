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
	private JTextField textFieldNome;
	private JTextField textFieldTelefone;
	private JTextField textFieldEmail;
	private JTextArea textAreaObservacoes; // Área de texto para observações
	private JButton btnAssociarDados; // Botão para salvar os dados associados
	private JButton btnSalvarEmJson; // Botão para salvar os dados em JSON
	private JComboBox<String> comboBoxTipoTelefone; // ComboBox para o tipo de telefone

	private JTable tableTelefones; // JTable para exibir os telefones
	private DefaultTableModel tableModel; // Modelo da tabela

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AgendaTelaApp frame = new AgendaTelaApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AgendaTelaApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(600, 400);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contatos = new ArrayList<>(); // Lista de contatos

		String[] columnNames = { "Número", "Tipo" };
		tableModel = new DefaultTableModel(columnNames, 0);
		tableTelefones = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(tableTelefones);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Arquivo");
		menuBar.add(mnNewMenu);

		// JMenuItem para abrir arquivo JSON
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Abrir");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirArquivoJson();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0 }; // Adicionando mais uma linha para os botões
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		// Lista de contatos
		listModel = new DefaultListModel<>();
		listContatos = new JList<>(listModel);
		listContatos.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.black, 1, true), "Contatos"));
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		panel.add(listContatos, gbc_list);

		// Botão de adicionar novo contato
		JButton btnNewButton = new JButton("+");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarContato();
			}
		});
		btnNewButton.setPreferredSize(new Dimension(45, 45));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		// Botão para adicionar telefone
		JButton buttonAdicionarTelefone = new JButton("Adicionar Telefone");
		buttonAdicionarTelefone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarTelefone();
			}
		});

		// Botão para remover telefone
		JButton buttonRemoverTelefone = new JButton("Remover Telefone");
		buttonRemoverTelefone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removerTelefone();
			}
		});

		// Botão de salvar dados em arquivo JSON
		btnSalvarEmJson = new JButton("Salvar em JSON");
		btnSalvarEmJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarDadosEmJson();
			}
		});

		GridBagConstraints gbc_btnSalvarEmJson = new GridBagConstraints();
		gbc_btnSalvarEmJson.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSalvarEmJson.insets = new Insets(0, 0, 0, 5);
		gbc_btnSalvarEmJson.gridx = 0;
		gbc_btnSalvarEmJson.gridy = 1;
		contentPane.add(btnSalvarEmJson, gbc_btnSalvarEmJson);

		// TabbedPane para exibir os dados dos contatos
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 2;
		gbc_tabbedPane.gridy = 0;
		gbc_tabbedPane.gridheight = 2; // TabbedPane ocupa 2 linhas
		contentPane.add(tabbedPane, gbc_tabbedPane);

		// Aba para dados básicos com JTextFields e JLabels
		JPanel panelDadosBasicos = new JPanel();
		panelDadosBasicos.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Label e TextField para o Nome
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelDadosBasicos.add(new JLabel("Nome:"), gbc);

		gbc.gridx = 1;
		textFieldNome = new JTextField(20);
		panelDadosBasicos.add(textFieldNome, gbc);

		// Label e TextField para o Email
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelDadosBasicos.add(new JLabel("Email:"), gbc);

		gbc.gridx = 1;
		textFieldEmail = new JTextField(20);
		panelDadosBasicos.add(textFieldEmail, gbc);

		// Label e TextField para o Telefone
		gbc.gridx = 0;
		gbc.gridy = 2;
		panelDadosBasicos.add(new JLabel("Telefone:"), gbc);

		gbc.gridx = 1;
		textFieldTelefone = new JTextField(10);
		panelDadosBasicos.add(textFieldTelefone, gbc);

		// Label e ComboBox para o Tipo de Telefone
		gbc.gridx = 0;
		gbc.gridy = 3;
		panelDadosBasicos.add(new JLabel("Tipo de Telefone:"), gbc);

		gbc.gridx = 1;
		comboBoxTipoTelefone = new JComboBox<>(new String[] { "Fixo", "Celular", "Zap" });
		panelDadosBasicos.add(comboBoxTipoTelefone, gbc);

		// Adiciona o botão para adicionar telefone
		gbc.gridx = 0;
		gbc.gridy = 4;
		panelDadosBasicos.add(buttonAdicionarTelefone, gbc);

		// Adiciona o botão para remover telefone
		gbc.gridx = 1;
		panelDadosBasicos.add(buttonRemoverTelefone, gbc);

		// Adiciona a JTable com scroll
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		panelDadosBasicos.add(scrollPane, gbc);

		// Adiciona a aba com o novo painel
		tabbedPane.addTab("Dados Básicos", panelDadosBasicos);

		// Aba para observações com JTextArea
		textAreaObservacoes = new JTextArea();
		tabbedPane.addTab("Observações", new JScrollPane(textAreaObservacoes));

		// Botão "Salvar Dados" para associar os dados ao contato
		btnAssociarDados = new JButton("Associar Dados");
//		btnSalvarDados.setVisible(false);

		btnAssociarDados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarDadosContato();
			}
		});
		GridBagConstraints gbc_btnSalvarDados = new GridBagConstraints();
		gbc_btnSalvarDados.insets = new Insets(0, 0, 0, 0); // Sem espaçamento extra
		gbc_btnSalvarDados.fill = GridBagConstraints.HORIZONTAL; // Preenche horizontalmente
		gbc_btnSalvarDados.gridx = 2; // Na mesma coluna que o tabbedPane
		gbc_btnSalvarDados.gridy = 2; // Nova linha abaixo do tabbedPane
		contentPane.add(btnAssociarDados, gbc_btnSalvarDados);

		// Listener para seleção de contatos
		listContatos.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedIndex = listContatos.getSelectedIndex();
					if (selectedIndex != -1) {
						Contato contatoSelecionado = contatos.get(selectedIndex);
						mostrarDetalhesContato(contatoSelecionado);

						// Verifica se o contato está no JSON
//						boolean existeNoJson = contatoExisteNoJson(contatoSelecionado.getNome());

						// Mostra o botão "Associar Dados" se o contato não estiver salvo no JSON
//						btnAssociarDados.setVisible(!existeNoJson);
					}
				}
			}
		});
	}

	// Função para exibir os detalhes do contato nos JTextFields
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

	// Função para adicionar um telefone à lista
	private void adicionarTelefone() {
		String numero = textFieldTelefone.getText();
		String tipo = (String) comboBoxTipoTelefone.getSelectedItem();

		if (!numero.isEmpty() && tipo != null) {
			Telefone novoTelefone = new Telefone(numero, tipo);
			int selectedIndex = listContatos.getSelectedIndex();
			if (selectedIndex != -1) {
				contatos.get(selectedIndex).adicionarTelefone(novoTelefone);
				tableModel.addRow(new Object[] { numero, tipo });
				textFieldTelefone.setText(""); // Limpa o campo após adicionar
			} else {
				JOptionPane.showMessageDialog(this, "Selecione um contato para adicionar o telefone.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Por favor, preencha o número e selecione o tipo.");
		}
	}

	// Função para remover um telefone da lista
	private void removerTelefone() {
		int selectedRow = tableTelefones.getSelectedRow();
		if (selectedRow != -1) {
			int selectedIndex = listContatos.getSelectedIndex();
			if (selectedIndex != -1) {
				// Obtém o telefone a ser removido
				String numero = (String) tableModel.getValueAt(selectedRow, 0);
				String tipo = (String) tableModel.getValueAt(selectedRow, 1);
				Telefone telefoneParaRemover = new Telefone(numero, tipo);
				contatos.get(selectedIndex).removerTelefone(telefoneParaRemover);
				tableModel.removeRow(selectedRow);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Selecione um telefone para remover.");
		}
	}

	// Função para salvar os dados modificados do contato
	private void salvarDadosContato() {
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

	// Função para adicionar um novo contato
	private void adicionarContato() {
		String nome = JOptionPane.showInputDialog("Digite o nome do novo contato:");
		if (nome != null && !nome.trim().isEmpty()) {
			Contato novoContato = new Contato(nome);
			contatos.add(novoContato);
			listModel.addElement(novoContato.getNome());
		}
	}

	// Função para salvar os dados em arquivo JSON
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

	// Função para abrir o arquivo JSON e carregar os dados
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

	// Verifica se um contato existe no JSON
	private boolean contatoExisteNoJson(String nomeContato) {
		for (Contato contato : contatos) {
			if (contato.getNome().equalsIgnoreCase(nomeContato)) {
				return true;
			}
		}
		return false;
	}
}
