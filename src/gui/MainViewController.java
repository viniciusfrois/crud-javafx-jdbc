package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAction () {
		System.out.println("Vend");		
	}
	
	@FXML
	public void onMenuItemDepartamentoAction () {
		System.out.println("Dep");		
	}
	
	@FXML
	public void onMenuItemSobreAction () {
		carregaView("/gui/Sobre.fxml");		
	}
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
	}
	
	private void carregaView (String caminhoDaView) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
		VBox newVBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVBox.getChildren().get(0);
		mainVBox.getChildren().clear();
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newVBox.getChildren());
		
		} catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
