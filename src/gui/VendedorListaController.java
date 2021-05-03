package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.DepartamentoServico;
import model.services.VendedorServico;

public class VendedorListaController implements Initializable, DataChangeListener {

	private VendedorServico servico;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColunaID;

	@FXML
	private TableColumn<Vendedor, String> tableColunaNome;
	
	@FXML
	private TableColumn<Vendedor, String> tableColunaEmail;
	
	@FXML
	private TableColumn<Vendedor, Date> tableColunaDataNascimento;
	
	@FXML
	private TableColumn<Vendedor, Double> tableColunaSalario;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaEdit;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaRemove;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onbtNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Vendedor obj = new Vendedor();
		criarDialogForm(obj, "/gui/VendedorForm.fxml", parentStage);
	}

	public void setVendedorServico(VendedorServico servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniciarNodes();
	}

	private void iniciarNodes() {
		tableColunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatTableColumnDate(tableColunaDataNascimento, "dd/MM/yyyy");
		tableColunaSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
		Utils.formatTableColumnDouble(tableColunaSalario, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (servico == null) {
			throw new IllegalStateException("Servico é nulo");
		}
		List<Vendedor> list = servico.buscarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogForm(Vendedor obj, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			Pane pane = loader.load();

			VendedorFormController controller = loader.getController();
			controller.setVendedor(obj);
			controller.setServicos(new VendedorServico(), new DepartamentoServico());
			controller.loadObjetosAssociados();
			controller.subscribeDataChangeListener(this);
			controller.atualizarForm();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Cadastrar novo Vendedor");
			dialogoStage.setScene(new Scene(pane));
			dialogoStage.setResizable(false);
			dialogoStage.initOwner(parentStage);
			dialogoStage.initModality(Modality.WINDOW_MODAL);
			dialogoStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alertas.showAlert("IOException", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}

	} 

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColunaEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogForm(obj, "/gui/VendedorForm.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColunaRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaRemove.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> resultado = Alertas.showConfirmation("Confirmação", "Tem certeza que quer deletar?");
		if (resultado.get() == ButtonType.OK) {
			try {
			servico.remove(obj);
			updateTableView();
			}
			catch (DbException e) {
				Alertas.showAlert("Erro ao deletar", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
