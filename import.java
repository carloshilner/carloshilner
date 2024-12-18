package br.gov.dataprev.dbloreanapp.gui.view;
 
import java.io.IOException;
import java.util.HashMap;
 
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 
import br.gov.dataprev.dblorean.exception.DBLoreanException;
import br.gov.dataprev.dbloreanapp.enums.Codes;
import br.gov.dataprev.dbloreanapp.enums.Images;
import br.gov.dataprev.dbloreanapp.gui.component.ItemSelector;
import br.gov.dataprev.dbloreanapp.gui.component.WindowView;
import br.gov.dataprev.dbloreanapp.gui.tools.QuickImplementation;
import br.gov.dataprev.dbloreanapp.service.DatabaseManager;
 
public class ExportTreinamentoView extends WindowView {
 
	JLabel lblTabela;
	ItemSelector<String> seletor;
	JTextField criteria;
	JTextField descricao;
	JButton gerar;
	JTextArea dataset;
 
	public ExportTreinamentoView(Window parent) {
		super("Treinamento Export", parent);
		lblTabela = QuickImplementation.buildCenteredLabel("Tabela", "");
		seletor = new ItemSelector<String>("Procurar por", "Escolha uma coluna", "Coluna usada para registro", "");
		criteria = QuickImplementation.buildTextField("Criteria", true);
		descricao = QuickImplementation.buildTextField("Descrição", true);
		gerar = QuickImplementation.buildButton("Gerar", Images.EXPORT.get(), false);
		dataset = QuickImplementation.buildTextArea("Dataset", false);
		this.setNameBorder();
		this.add(lblTabela, 0, 0, 1, 1).setNameBorder();
		this.add(seletor, 1, 0, 1, 1).setNameBorder();
		this.setScrollEnabled();
		this.add(criteria, 2, 0, 1, 1).setNameBorder();
		this.setScrollEnabled();
		this.add(descricao, 3, 0, 1, 1);
		this.add(gerar, 4, 0, 1, 1).setNameBorder();
		this.setScrollEnabled();
		this.add(dataset, 0, 12, 12, 10);
		this.seletor.implementDefaultChooseDo();
 
		// quando nenhum banco selecionado - todos botoes bloqueados
		seletor.setEnabled(false);
		gerar.setEnabled(false);
 
		this.assignKeyCaller(this.seletor, 10);
		this.assignKeyCaller(this.gerar, 11);
 
	}
 
	@Override
	public void update(int code) {
		if (!this.isVisible()) {
			return;
		}
		// quando selecionar um banco - desbloquear os campos/botoes , preencher
		// "tabela" e "procurar por"
		switch (code) {
		case 2:
			try {
				lblTabela.setText(DatabaseManager.getCurrentDatabaseMainTableName());
				seletor.selectItem(DatabaseManager.mainTablePK);
				seletor.addSelected(DatabaseManager.getDatabaseCurrentActiveSearchBy());
				seletor.setEnabled(true);
				gerar.setEnabled(true);
			} catch (DBLoreanException e) {
			}
			break;
		}
	}
 
	@Override
	public void call(int callKey, int state, HashMap<String, Object> variables) {
		switch (callKey) {
		case 10:
			this.searchDo(callKey, state, variables);
			break;
		case 11:
			variables.put("Criteria", this.criteria.getText());
			variables.put("File", this.descricao.getText() + ".xml");
			variables.put("Column", this.seletor.getSelectedItem());
			System.out.println("call " + callKey + " state " + state + " variaveis " + variables);
			this.generateDo(callKey, state, variables);
			break;
		}
	}
 
	private void generateDo(int callKey, int state, HashMap<String, Object> variables) {
		try {
			DatabaseManager.exportToFile((String) variables.get("Criteria"), (String) variables.get("File"),
					(String) variables.get("Column"), false, false, true);
			try {
				String texto = DatabaseManager.readDatasetsFile((String) variables.get("File"), true);
//				System.out.println("--> " + texto);
				this.dataset.setText(texto);
			} catch (IOException e) {
			}
		} catch (DBLoreanException e) {
		}
	}
 
	private void searchDo(int callKey, int state, HashMap<String, Object> variables) {
		switch (state) {
		case 0:
			System.out.println("inicio... ");
			DatabaseManager.updateDependents(Codes.DATABASE_HEAVY_OPERATION_STARTED.get());
			this.call(callKey, 1, 2, variables);
			break;
		case 1:
			try {
				seletor.setListData(DatabaseManager.getCurrentMainTableColumns());
			} catch (DBLoreanException e) {
			}
			break;
		case 2:
			System.out.println("fim... ");
			DatabaseManager.updateDependents(Codes.DATABASE_HEAVY_OPERATION_ENDED.get());
			break;
		}
 
	}
}
