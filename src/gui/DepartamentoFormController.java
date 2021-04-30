package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidationException;
import model.services.DepartamentoServico;

public class DepartamentoFormController implements Initializable {
	
	private Departamento entidade;
	
	private DepartamentoServico servico;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Label labelErroNome;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	
	public void setDepartamento (Departamento entidade) {
		this.entidade = entidade;
	}
	
	public void setDepartamentoServico (DepartamentoServico servico) {
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

	private Departamento getFormData() {
		Departamento obj =  new Departamento();
		ValidationException exception = new ValidationException("Erro de Validação");
		
		obj.setId(gui.util.Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText()== null || txtNome.getText().trim().equals("")) {
			exception.addErros("nome", "Campo não pode ser vazio!");
		}
		obj.setName(txtNome.getText());
		
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
		Tratamentos.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void atualizarForm () {
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getName());
	}
	
	private void setErrosMensagens (Map<String,String> erros) {
		Set<String> campos = erros.keySet();
		
		if (campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}
	
}
