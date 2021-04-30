package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Tratamentos;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Departamento;
import model.services.DepartamentoServico;

public class DepartamentoFormController implements Initializable {
	
	private Departamento entidade;
	
	private DepartamentoServico servico;
	
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
		Utils.stageAtual(event).close();
		}
		catch (DbException e) {
			Alertas.showAlert("Erro ao tentar Salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getFormData() {
		Departamento obj =  new Departamento();
		obj.setId(gui.util.Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtNome.getText());
		return obj;
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
}
