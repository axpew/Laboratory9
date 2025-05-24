package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import ucr.laboratory9.HelloApplication;

import java.io.IOException;

public class HelloController {
    @FXML
    private Text txtMessage;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;

    @FXML
    public void initialize() {
        // Inicialización del controlador
        txtMessage.setText("Laboratory No. 9");
    }

    private void load(String form) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(form));
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            showCompactError("Error", "No se pudo cargar la vista: " + form +
                    "\nError: " + e.getMessage());
        }
    }

    @FXML
    public void Home(ActionEvent actionEvent) {
        this.bp.setCenter(ap);
        this.txtMessage.setText("Laboratory No. 9");
    }

    @FXML
    public void btreeOperations(ActionEvent actionEvent) {
        try {
            load("operations-view.fxml");
            txtMessage.setText("Cargando BTree Operations...");
        } catch (Exception e) {
            showCompactError("Error", "Error cargando BTree Operations: " + e.getMessage());
        }
    }

    @FXML
    public void btreeTour(ActionEvent actionEvent) {
        try {
            load("tour-view.fxml");
            txtMessage.setText("Cargando BTree Tour...");
        } catch (Exception e) {
            showCompactError("Error", "Error cargando BTree Tour: " + e.getMessage());
        }
    }

    @FXML
    public void graphicBtree(ActionEvent actionEvent) {
        try {
            load("graphicBtree-view.fxml");
            txtMessage.setText("Cargando Graphic BTree...");
        } catch (Exception e) {
            showCompactError("Error", "Error cargando Graphic BTree: " + e.getMessage());
        }
    }

    @FXML
    public void Exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Deprecated
    public void loadingOnMousePressed(Event event)  {
        this.txtMessage.setText("Estamos cargando tu vista...");
    }

    private void showCompactError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Configurar tamaño compacto
        alert.getDialogPane().setPrefSize(400, 250);
        alert.getDialogPane().setMaxWidth(400);
        alert.getDialogPane().setMaxHeight(250);

        alert.showAndWait();
    }
}