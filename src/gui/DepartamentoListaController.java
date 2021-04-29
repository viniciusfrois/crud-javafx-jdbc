package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onbtNovoAction () {
		System.out.println("Botao");
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

}
