package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alertas;
import gui.util.Tratamentos;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Vendedor;
import model.exceptions.ValidationException;
import model.services.VendedorServico;

public class VendedorFormController implements Initializable {
	
	private Vendedor entidade;
	
	private VendedorServico servico;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;	
	@FXML
	private TextField txtNome;	
	@FXML
	private TextField txtEmail;	
	@FXML
	private DatePicker dpDataNascimento;	
	@FXML
	private TextField txtSalario;	
	@FXML
	private Label labelErroNome;	
	@FXML
	private Label labelErroEmail;
	@FXML
	private Label labelErroDataNascimento;
	@FXML
	private Label labelErroSalario;
	@FXML
	private Button btnSalvar;	
	@FXML
	private Button btnCancelar;
	
	public void setVendedor (Vendedor entidade) {
		this.entidade = entidade;
	}
	
	public void setVendedorServico (VendedorServico servico) {
		this.servico = servico;
	}
	
	@FXML
	public void onBtnSalvarAction (ActionEvent event) {
		try {
		entidade = getFormData();
		servico.SalvarOuAtualizar(entidade);
		notifyDataChangeListeners();
		Utils.stageAtual(event).close();
		}
		catch (ValidationException e) {
			setErrosMensagens(e.getErros());
		}
		catch (DbException e) {
			Alertas.showAlert("Erro ao tentar Salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	private Vendedor getFormData() {
		Vendedor obj =  new Vendedor();
		ValidationException exception = new ValidationException("Erro de Validação");
		
		obj.setId(gui.util.Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText()== null || txtNome.getText().trim().equals("")) {
			exception.addErros("nome", "Campo não pode ser vazio!");
		}
		obj.setNome(txtNome.getText());
		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtnCancelarAction (ActionEvent event) {
		Utils.stageAtual(event).close();
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializaNodes();
	}
	
	private void inicializaNodes () {
		Tratamentos.setTextFieldInteger(txtId);
		Tratamentos.setTextFieldMaxLength(txtNome, 60);
		Tratamentos.setTextFieldDouble(txtSalario);
		Tratamentos.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
	}
	
	public void atualizarForm () {
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalario.setText(String.format("%.2f", entidade.getSalario()));
		if (entidade.getDataNascimento() != null) {
		dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getDataNascimento().toInstant(), ZoneId.systemDefault()));
		}
	}
	
	private void setErrosMensagens (Map<String,String> erros) {
		Set<String> campos = erros.keySet();
		
		if (campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}
	
}
