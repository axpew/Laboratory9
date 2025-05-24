package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class BtreeTourController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
    @javafx.fxml.FXML
    private Pane buttonPane11;
    @javafx.fxml.FXML
    private Text tourLabel;


    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void pressedRandom(Event event) {
    }

    @FXML
    public void inOrderAction(ActionEvent actionEvent) {

        tourLabel.setText("In Order Transversal Tour (L-N-R)");
    }

    @FXML
    public void preOrderAction(ActionEvent actionEvent) {

        tourLabel.setText("Pre Order Transversal Tour (N-L-R)");
    }

    @FXML
    public void postOrderAction(ActionEvent actionEvent) {

        tourLabel.setText("Post Order Transversal Tour (L-R-N)");
    }
}
