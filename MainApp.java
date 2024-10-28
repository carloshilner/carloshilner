// MainApp.java
package br.gov.dataprev.dbloreanapp.test.mvc;

import br.gov.dataprev.dbloreanapp.test.mvc.controller.AgendaController;
import br.gov.dataprev.dbloreanapp.test.mvc.model.AgendaModel;
import br.gov.dataprev.dbloreanapp.test.mvc.view.AgendaView;

public class MainApp {
    public static void main(String[] args) {
        AgendaModel model = new AgendaModel();
        AgendaView view = new AgendaView();
        AgendaController controller = new AgendaController(view, model);
        view.setVisible(true);
    }
}
