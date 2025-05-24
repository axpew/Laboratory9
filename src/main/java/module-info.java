module ucr.laboratory9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ucr.laboratory9 to javafx.fxml;
    exports ucr.laboratory9;
    exports controller;
    opens controller to javafx.fxml;
}