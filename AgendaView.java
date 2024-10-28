package br.gov.dataprev.dbloreanapp.test.mvc.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import br.gov.dataprev.dbloreanapp.test.mvc.Contato;

import java.awt.*;

public class AgendaView extends JFrame {
    private JPanel contentPane;
    private JList<String> listContatos;
    private DefaultListModel<String> listModel;
    private JTextField textFieldNome;
    private JTextField textFieldTelefone;
    private JTextField textFieldEmail;
    private JTextArea textAreaObservacoes;
    private JButton btnAssociarDados;
    private JButton btnSalvarEmJson;
    private JComboBox<String> comboBoxTipoTelefone;
    private JTable tableTelefones;
    private DefaultTableModel tableModel;
    private JButton btnAddContato;
    private JButton btnAdicionarTelefone;
    private JButton btnRemoverTelefone;

    public AgendaView() {
        setupFrame();
        initializeComponents();
        layoutComponents();
    }

    private void setupFrame() {
        setTitle("Agenda Telefônica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);
    }

    private void initializeComponents() {
        listModel = new DefaultListModel<>();
        listContatos = new JList<>(listModel);
        listContatos.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.black, 1, true), "Contatos"));
        
        textFieldNome = new JTextField(20);
        textFieldEmail = new JTextField(20);
        textFieldTelefone = new JTextField(10);
        textAreaObservacoes = new JTextArea();
        comboBoxTipoTelefone = new JComboBox<>(new String[]{"Fixo", "Celular", "Zap"});

        btnAddContato = new JButton("+");
        btnAdicionarTelefone = new JButton("Adicionar Telefone");
        btnRemoverTelefone = new JButton("Remover Telefone");
        btnAssociarDados = new JButton("Associar Dados");
        btnSalvarEmJson = new JButton("Salvar em JSON");

        String[] columnNames = {"Número", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableTelefones = new JTable(tableModel);
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        // Configuração da lista de contatos
        gbc.gridx = 0;
        gbc.gridy = 0;
//        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(new JScrollPane(listContatos), gbc);
        // Botão de adicionar contato
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 5, 5);
        btnAddContato.setPreferredSize(new Dimension(45, 45));
        contentPane.add(btnAddContato, gbc);
        // TabbedPane para dados de contato e observações
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, getGridBagConstraints(2, 0, 2, 1.0, 1.0));

        JPanel panelDadosBasicos = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Configuração de campos de dados básicos
        addLabelAndComponent(panelDadosBasicos, "Nome:", textFieldNome, 0);
        addLabelAndComponent(panelDadosBasicos, "Email:", textFieldEmail, 1);
        addLabelAndComponent(panelDadosBasicos, "Telefone:", textFieldTelefone, 2);
        addLabelAndComponent(panelDadosBasicos, "Tipo de Telefone:", comboBoxTipoTelefone, 3);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelDadosBasicos.add(btnAdicionarTelefone, gbc);

        gbc.gridx = 1;
        panelDadosBasicos.add(btnRemoverTelefone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panelDadosBasicos.add(new JScrollPane(tableTelefones), gbc);

        tabbedPane.add("Dados Básicos", panelDadosBasicos);
        tabbedPane.add("Observações", new JScrollPane(textAreaObservacoes));

        // Botões de ação
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(btnSalvarEmJson, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        contentPane.add(btnAssociarDados, gbc);
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private GridBagConstraints getGridBagConstraints(int gridx, int gridy, int gridwidth, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    // Getters para os componentes para que o Controller possa interagir com a View
    public JList<String> getListContatos() { return listContatos; }
    public DefaultListModel<String> getListModel() { return listModel; }
    public JTextField getTextFieldNome() { return textFieldNome; }
    public JTextField getTextFieldEmail() { return textFieldEmail; }
    public JTextField getTextFieldTelefone() { return textFieldTelefone; }
    public JTextArea getTextAreaObservacoes() { return textAreaObservacoes; }
    public JComboBox<String> getComboBoxTipoTelefone() { return comboBoxTipoTelefone; }
    public JTable getTableTelefones() { return tableTelefones; }
    public JButton getBtnAddContato() { return btnAddContato; }
    public JButton getBtnAssociarDados() { return btnAssociarDados; }
    public JButton getBtnSalvarEmJson() { return btnSalvarEmJson; }
    public JButton getBtnAdicionarTelefone() { return btnAdicionarTelefone; }
    public JButton getBtnRemoverTelefone() { return btnRemoverTelefone; }

    public void atualizarDadosContato(Contato contato) {
        textFieldNome.setText(contato.getNome());
        textFieldEmail.setText(contato.getEmail());
//        textFieldTelefone.setText(contato.getTelefones());
        textAreaObservacoes.setText(contato.getObservacoes());
    }
}
