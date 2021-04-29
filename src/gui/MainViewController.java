package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartamentoServico;

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
		carregaView("/gui/DepartamentoLista.fxml", (DepartamentoListaController controller) -> {
			controller.setDepartamentoServico(new DepartamentoServico());
			controller.updateTableView();
		});			
	}
	
	@FXML
	public void onMenuItemSobreAction () {
		carregaView("/gui/Sobre.fxml", x -> {});		
	}
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
	}
	
	private <T> void  carregaView (String caminhoDaView, Consumer<T> initialize) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
		VBox newVBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVBox.getChildren().get(0);
		mainVBox.getChildren().clear();
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newVBox.getChildren());
		
		T controller = loader.getController();
		initialize.accept(controller);
		
		} catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
