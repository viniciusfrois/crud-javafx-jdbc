package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoServico;

public class DepartamentoListaController implements Initializable {
	
	private DepartamentoServico servico;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColunaID;
	
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsList;
	
	@FXML
	public void onbtNovoAction (ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Departamento obj = new Departamento();
		criarDialogForm(obj,"/gui/DepartamentoForm.fxml", parentStage);
	}
	
	public void setDepartamentoServico (DepartamentoServico servico) {
		this.servico = servico;		
	}	

	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		iniciarNodes();
	}
	
	private void iniciarNodes() {
		tableColunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView () {
		if (servico == null) {
			throw new IllegalStateException("Servico é nulo");
		}
		List <Departamento> list = servico.buscarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}
	
	private void criarDialogForm (Departamento obj, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			Pane pane = loader.load();
			
			DepartamentoFormController controller = loader.getController();
			controller.setDepartamento(obj);
			controller.setDepartamentoServico(new DepartamentoServico());
			controller.atualizarForm();
			
			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Cadastrar novo Departamento");
			dialogoStage.setScene(new Scene(pane));
			dialogoStage.setResizable(false);
			dialogoStage.initOwner(parentStage);
			dialogoStage.initModality(Modality.WINDOW_MODAL);
			dialogoStage.showAndWait();
		}
		catch (IOException e) {
			Alertas.showAlert("IOException", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
		
	}

}
