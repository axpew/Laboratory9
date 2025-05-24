package controller;

import domain.BTree;
import domain.TreeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.FXUtility;
import util.TreeVisualizer;

import java.util.Optional;

public class BtreeOperationsController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;

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
    public void nodeHeightAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            TextInputDialog dialog = FXUtility.dialog("Altura del Nodo",
                    "Ingrese el valor del nodo para obtener su altura:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                try {
                    int value = Integer.parseInt(result.get().trim());

                    if (btree.contains(value)) {
                        int height = btree.height(value);
                        FXUtility.showMessage("Altura del Nodo",
                                "El nodo con valor " + value + " tiene una altura de: " + height +
                                        "\n\n(La altura representa el número de niveles desde la raíz hasta el nodo)");
                    } else {
                        FXUtility.showAlert("Elemento no encontrado",
                                "El valor " + value + " no existe en el árbol.");
                    }

                } catch (NumberFormatException e) {
                    FXUtility.showErrorAlert("Error", "Por favor ingrese un número válido.");
                } catch (TreeException e) {
                    FXUtility.showErrorAlert("Error", "Error obteniendo altura: " + e.getMessage());
                }
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. Genera un nuevo árbol primero.");
        }
    }

    @javafx.fxml.FXML
    public void treeHeightAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            try {
                int height = btree.height();
                int size = btree.size();
                int leaves = btree.totalLeaves();

                FXUtility.showMessage("Información del Árbol",
                        "Altura total del árbol: " + height +
                                "\nNúmero de elementos: " + size +
                                "\nNúmero de hojas: " + leaves +
                                "\n\n(La altura del árbol es la distancia máxima desde la raíz hasta cualquier hoja)");

            } catch (TreeException e) {
                FXUtility.showErrorAlert("Error", "Error obteniendo altura: " + e.getMessage());
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. La altura es -1.");
        }
    }

    @javafx.fxml.FXML
    public void removeOnAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            TextInputDialog dialog = FXUtility.dialog("Eliminar Elemento",
                    "Ingrese el valor del elemento a eliminar:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                try {
                    int value = Integer.parseInt(result.get().trim());

                    if (btree.contains(value)) {
                        btree.remove(value);
                        updateTreeVisualization();
                        FXUtility.showMessage("Elemento Eliminado",
                                "El elemento " + value + " ha sido eliminado del árbol.");
                    } else {
                        FXUtility.showAlert("Elemento no encontrado",
                                "El valor " + value + " no existe en el árbol.");
                    }

                } catch (NumberFormatException e) {
                    FXUtility.showErrorAlert("Error", "Por favor ingrese un número válido.");
                } catch (TreeException e) {
                    FXUtility.showErrorAlert("Error", "Error eliminando elemento: " + e.getMessage());
                }
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. No hay elementos para eliminar.");
        }
    }

    @javafx.fxml.FXML
    public void addOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = FXUtility.dialog("Agregar Elemento",
                "Ingrese el valor del elemento a agregar:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                int value = Integer.parseInt(result.get().trim());

                // Verificar si ya existe
                if (btree != null && !btree.isEmpty() && btree.contains(value)) {
                    FXUtility.showAlert("Elemento Duplicado",
                            "El elemento " + value + " ya existe en el árbol. No se permiten duplicados.");
                    return;
                }

                btree.add(value);

                // Validar estructura después de agregar
                if (!btree.validateStructure()) {
                    System.err.println("ADVERTENCIA: Estructura inválida después de agregar " + value);
                    btree.printTreeStructure();
                }

                updateTreeVisualization();
                FXUtility.showMessage("Elemento Agregado",
                        "El elemento " + value + " ha sido agregado al árbol.");

            } catch (NumberFormatException e) {
                FXUtility.showErrorAlert("Error", "Por favor ingrese un número válido.");
            } catch (TreeException e) {
                FXUtility.showErrorAlert("Error", "Error verificando duplicados: " + e.getMessage());
            }
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTree();
    }

    @javafx.fxml.FXML
    public void pressedRandom(Event event) {
        txtMessage.setText("Generando nuevo árbol...");
    }

    @javafx.fxml.FXML
    public void containsOnAction(ActionEvent actionEvent) {
        if (btree != null && !btree.isEmpty()) {
            TextInputDialog dialog = FXUtility.dialog("Buscar Elemento",
                    "Ingrese el valor del elemento a buscar:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                try {
                    int value = Integer.parseInt(result.get().trim());
                    boolean exists = btree.contains(value);

                    if (exists) {
                        int height = btree.height(value);
                        FXUtility.showMessage("Elemento Encontrado",
                                "✓ El elemento " + value + " SÍ existe en el árbol." +
                                        "\nAltura del elemento: " + height);
                    } else {
                        FXUtility.showAlert("Elemento no encontrado",
                                "✗ El elemento " + value + " NO existe en el árbol.");
                    }

                } catch (NumberFormatException e) {
                    FXUtility.showErrorAlert("Error", "Por favor ingrese un número válido.");
                } catch (TreeException e) {
                    FXUtility.showErrorAlert("Error", "Error buscando elemento: " + e.getMessage());
                }
            }
        } else {
            FXUtility.showAlert("Información", "El árbol está vacío. No hay elementos para buscar.");
        }
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

            updateTreeVisualization();
            txtMessage.setText("Graphic Binary Tree - Operations (" + btree.size() + " nodos)");

        } catch (Exception e) {
            FXUtility.showErrorAlert("Error", "Error generando árbol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTreeVisualization() {
        try {
            if (treePane != null) {
                visualizer.drawTree(btree, treePane);
            }
        } catch (TreeException e) {
            FXUtility.showErrorAlert("Error", "Error actualizando visualización: " + e.getMessage());
        }
    }
}