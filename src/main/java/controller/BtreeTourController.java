package controller;

import domain.BTree;
import domain.TreeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.FXUtility;
import util.TreeVisualizer;

public class BtreeTourController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
    @javafx.fxml.FXML
    private Pane buttonPane11;
    @javafx.fxml.FXML
    private Text tourLabel;

    private BTree btree;
    private TreeVisualizer visualizer;
    private Pane treePane;

    public void initialize() {
        // Inicializar el árbol y el visualizador
        btree = new BTree();
        visualizer = new TreeVisualizer();

        // Encontrar el ScrollPane y configurarlo
        javafx.scene.control.ScrollPane scrollPane = null;
        for (javafx.scene.Node node : mainPain.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                for (javafx.scene.Node child : pane.getChildren()) {
                    if (child instanceof javafx.scene.control.ScrollPane) {
                        scrollPane = (javafx.scene.control.ScrollPane) child;
                        break;
                    }
                }
            }
        }

        if (scrollPane != null) {
            treePane = new Pane();
            // NO establecer tamaño fijo - se ajustará dinámicamente
            scrollPane.setContent(treePane);

            // Configurar el ScrollPane para mejor funcionamiento
            scrollPane.setFitToWidth(false);
            scrollPane.setFitToHeight(false);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }

        // Generar árbol inicial
        generateRandomTree();
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTree();
        tourLabel.setText("Transversal Tour");
    }

    @FXML
    public void pressedRandom(Event event) {
        txtMessage.setText("Generando nuevo árbol...");
    }

    @FXML
    public void inOrderAction(ActionEvent actionEvent) {
        tourLabel.setText("In Order Transversal Tour (L-N-R)");
        showTour("inorder");
    }

    @FXML
    public void preOrderAction(ActionEvent actionEvent) {
        tourLabel.setText("Pre Order Transversal Tour (N-L-R)");
        showTour("preorder");
    }

    @FXML
    public void postOrderAction(ActionEvent actionEvent) {
        tourLabel.setText("Post Order Transversal Tour (L-R-N)");
        showTour("postorder");
    }

    private void generateRandomTree() {
        try {
            btree.clear();

            // Generar valores únicos para evitar duplicados
            java.util.Set<Integer> uniqueValues = new java.util.HashSet<>();

            // Generar hasta 30 valores únicos entre 0 y 50
            while (uniqueValues.size() < 30) {
                int value = util.Utility.random(51); // 0 a 50
                uniqueValues.add(value);
            }

            // Insertar los valores únicos en orden aleatorio
            java.util.List<Integer> valuesList = new java.util.ArrayList<>(uniqueValues);
            java.util.Collections.shuffle(valuesList);

            for (Integer value : valuesList) {
                btree.add(value);
            }

            // Validar estructura antes de visualizar
            if (!btree.validateStructure()) {
                System.err.println("ADVERTENCIA: Estructura del árbol inválida detectada");
                btree.printTreeStructure();
            }

            // Visualizar el árbol
            if (treePane != null) {
                visualizer.drawTree(btree, treePane);
            }

            txtMessage.setText("Graphic Binary Tree - Transversal Tour (" + btree.size() + " nodos)");

        } catch (Exception e) {
            FXUtility.showErrorAlert("Error", "Error generando árbol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showTour(String tourType) {
        if (btree != null && !btree.isEmpty()) {
            try {
                visualizer.drawTreeWithTour(btree, treePane, tourType);

                // Mostrar información compacta del recorrido
                StringBuilder info = new StringBuilder();
                info.append("=== ").append(tourType.toUpperCase()).append(" TOUR ===\n\n");

                switch (tourType.toLowerCase()) {
                    case "preorder":
                        info.append("Orden: Nodo → Izquierda → Derecha\n");
                        info.append("Resultado: ").append(btree.preOrder()).append("\n\n");
                        info.append("En PreOrder visitamos primero el nodo actual, ");
                        info.append("luego el subárbol izquierdo y finalmente el derecho.");
                        break;

                    case "inorder":
                        info.append("Orden: Izquierda → Nodo → Derecha\n");
                        info.append("Resultado: ").append(btree.inOrder()).append("\n\n");
                        info.append("En InOrder visitamos primero el subárbol izquierdo, ");
                        info.append("luego el nodo actual y finalmente el derecho.");
                        break;

                    case "postorder":
                        info.append("Orden: Izquierda → Derecha → Nodo\n");
                        info.append("Resultado: ").append(btree.postOrder()).append("\n\n");
                        info.append("En PostOrder visitamos primero el subárbol izquierdo, ");
                        info.append("luego el derecho y finalmente el nodo actual.");
                        break;
                }

                showCompactAlert("Información del Recorrido", info.toString());

            } catch (TreeException e) {
                FXUtility.showErrorAlert("Error", "Error mostrando recorrido: " + e.getMessage());
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. Genera un nuevo árbol primero.");
        }
    }

    private void showCompactAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Configurar tamaño más compacto
        alert.getDialogPane().setPrefSize(450, 300);
        alert.getDialogPane().setMaxWidth(450);
        alert.getDialogPane().setMaxHeight(300);

        alert.showAndWait();
    }
}