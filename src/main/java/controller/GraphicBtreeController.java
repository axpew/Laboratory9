package controller;

import domain.BTree;
import domain.TreeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.FXUtility;
import util.TreeVisualizer;

public class GraphicBtreeController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
    @javafx.fxml.FXML
    private Pane buttonPane11;
    @javafx.fxml.FXML
    private Button randomButton;

    private BTree btree;
    private TreeVisualizer visualizer;
    private Pane treePane;

    public void initialize() {
        // Inicializar el árbol y el visualizador
        btree = new BTree();
        visualizer = new TreeVisualizer();

        // Encontrar el ScrollPane y obtener su content pane
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
            treePane.setPrefSize(1000, 650); // Tamaño balanceado
            scrollPane.setContent(treePane);
        }

        // Generar árbol inicial
        generateRandomTree();
    }

    @javafx.fxml.FXML
    public void levelsOnAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            try {
                visualizer.drawTreeWithLevels(btree, treePane);
                txtMessage.setText("Graphic Binary Tree - Levels View");
            } catch (TreeException e) {
                FXUtility.showErrorAlert("Error", "Error mostrando niveles: " + e.getMessage());
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. Genera un nuevo árbol primero.");
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTree();
    }

    @javafx.fxml.FXML
    public void tourInfoOnAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            try {
                StringBuilder info = new StringBuilder();
                info.append("=== INFORMACIÓN DEL ÁRBOL ===\n");
                info.append("• Altura del árbol: ").append(btree.height()).append("\n");
                info.append("• Número de elementos: ").append(btree.size()).append("\n");
                info.append("• Número de hojas: ").append(btree.totalLeaves()).append("\n\n");

                info.append("=== RECORRIDOS ===\n");
                String preOrder = btree.preOrder();
                String inOrder = btree.inOrder();
                String postOrder = btree.postOrder();

                // Limitar longitud de recorridos para que no sea muy largo
                info.append("• PreOrder: ").append(limitString(preOrder, 80)).append("\n");
                info.append("• InOrder: ").append(limitString(inOrder, 80)).append("\n");
                info.append("• PostOrder: ").append(limitString(postOrder, 80)).append("\n\n");

                info.append("=== ANÁLISIS DE NODOS ===\n");
                info.append("• Total de hojas: ").append(btree.totalLeaves()).append("\n");

                // Mostrar solo información resumida
                String leavesInfo = btree.printLeaves();
                info.append("• Hojas: ").append(limitString(leavesInfo.replace("Binary tree - leaves: ", ""), 60));

                showCompactAlert("Información del Árbol", info.toString());

            } catch (TreeException e) {
                FXUtility.showErrorAlert("Error", "Error obteniendo información: " + e.getMessage());
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. Genera un nuevo árbol primero.");
        }
    }

    private String limitString(String str, int maxLength) {
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

    private void showCompactAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Configurar tamaño más compacto
        alert.getDialogPane().setPrefSize(500, 400);
        alert.getDialogPane().setMaxWidth(500);
        alert.getDialogPane().setMaxHeight(400);

        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void pressedRandom(Event event) {
        txtMessage.setText("Generando nuevo árbol...");
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

            txtMessage.setText("Graphic Binary Tree - Nuevo árbol generado (" + btree.size() + " nodos)");

        } catch (Exception e) {
            FXUtility.showErrorAlert("Error", "Error generando árbol: " + e.getMessage());
            e.printStackTrace();
        }
    }
}