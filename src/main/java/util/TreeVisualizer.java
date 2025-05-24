package util;

import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TreeVisualizer {
    private static final int NODE_RADIUS = 18;
    private static final int LEVEL_HEIGHT = 70; // Aumentado para mejor separación
    private static final int MIN_HORIZONTAL_SPACING = 90; // Aumentado para evitar superposiciones
    private static final Color NODE_COLOR = Color.LIGHTBLUE;
    private static final Color NODE_BORDER_COLOR = Color.DARKBLUE;
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color LEVEL_LINE_COLOR = Color.RED;

    public void drawTree(BTree tree, Pane pane) throws TreeException {
        pane.getChildren().clear();

        if (tree.isEmpty()) {
            Text emptyText = new Text(350, 200, "Árbol vacío");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            emptyText.setFill(Color.GRAY);
            pane.getChildren().add(emptyText);
            return;
        }

        BTreeNode root = getRoot(tree);
        if (root != null) {
            // Validar estructura del árbol antes de dibujar
            if (!validateTreeStructure(root)) {
                Text errorText = new Text(250, 200, "Error: Estructura de árbol inválida");
                errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                errorText.setFill(Color.RED);
                pane.getChildren().add(errorText);
                return;
            }

            // Calcular dimensiones necesarias
            int treeHeight = tree.height();
            int nodeCount = tree.size();

            // Calcular dimensiones dinámicas
            double requiredHeight = calculateRequiredHeight(treeHeight);
            double requiredWidth = calculateRequiredWidth(nodeCount, treeHeight);

            // Ajustar el tamaño del pane
            pane.setPrefSize(requiredWidth, requiredHeight);
            pane.setMinSize(requiredWidth, requiredHeight);

            Map<BTreeNode, NodePosition> positions = calculatePositions(root, requiredWidth);

            // Primero dibujar las conexiones (líneas) - CORREGIDO
            drawConnections(pane, root, positions);

            // Luego dibujar los nodos encima
            drawNodes(pane, positions);
        }
    }

    public void drawTreeWithLevels(BTree tree, Pane pane) throws TreeException {
        pane.getChildren().clear();

        if (tree.isEmpty()) {
            Text emptyText = new Text(350, 200, "Árbol vacío");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            emptyText.setFill(Color.GRAY);
            pane.getChildren().add(emptyText);
            return;
        }

        BTreeNode root = getRoot(tree);
        int height = tree.height();
        int nodeCount = tree.size();

        // Validar estructura
        if (!validateTreeStructure(root)) {
            Text errorText = new Text(250, 200, "Error: Estructura de árbol inválida");
            errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            errorText.setFill(Color.RED);
            pane.getChildren().add(errorText);
            return;
        }

        // Calcular dimensiones necesarias
        double requiredHeight = calculateRequiredHeight(height);
        double requiredWidth = calculateRequiredWidth(nodeCount, height);

        // Ajustar el tamaño del pane
        pane.setPrefSize(requiredWidth, requiredHeight);
        pane.setMinSize(requiredWidth, requiredHeight);

        // Primero dibujar las líneas de nivel
        drawLevelLines(pane, height, requiredWidth);

        // Luego dibujar el árbol
        Map<BTreeNode, NodePosition> positions = calculatePositions(root, requiredWidth);
        drawConnections(pane, root, positions);
        drawNodes(pane, positions);
    }

    public void drawTreeWithTour(BTree tree, Pane pane, String tourType) throws TreeException {
        pane.getChildren().clear();

        if (tree.isEmpty()) {
            Text emptyText = new Text(350, 200, "Árbol vacío");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            emptyText.setFill(Color.GRAY);
            pane.getChildren().add(emptyText);
            return;
        }

        BTreeNode root = getRoot(tree);
        int height = tree.height();
        int nodeCount = tree.size();

        // Validar estructura
        if (!validateTreeStructure(root)) {
            Text errorText = new Text(250, 200, "Error: Estructura de árbol inválida");
            errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            errorText.setFill(Color.RED);
            pane.getChildren().add(errorText);
            return;
        }

        // Calcular dimensiones necesarias
        double requiredHeight = calculateRequiredHeight(height) + 50; // Extra espacio para el texto del tour
        double requiredWidth = calculateRequiredWidth(nodeCount, height);

        // Ajustar el tamaño del pane
        pane.setPrefSize(requiredWidth, requiredHeight);
        pane.setMinSize(requiredWidth, requiredHeight);

        Map<BTreeNode, NodePosition> positions = calculatePositions(root, requiredWidth);

        // Dibujar conexiones
        drawConnections(pane, root, positions);

        // Dibujar nodos
        drawNodes(pane, positions);

        // Dibujar numeración del tour
        drawTourNumbers(pane, root, positions, tourType);

        // Mostrar resultado del recorrido
        String tourResult = "";
        switch (tourType.toLowerCase()) {
            case "preorder":
                tourResult = tree.preOrder();
                break;
            case "inorder":
                tourResult = tree.inOrder();
                break;
            case "postorder":
                tourResult = tree.postOrder();
                break;
        }

        Text tourText = new Text(30, requiredHeight - 20,
                tourType.toUpperCase() + " Tour: " + tourResult);
        tourText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        tourText.setFill(Color.DARKGREEN);
        pane.getChildren().add(tourText);
    }

    // Método para calcular la altura necesaria basada en la altura del árbol
    private double calculateRequiredHeight(int treeHeight) {
        // Espacio para el primer nivel + (altura del árbol * espacio entre niveles) + margen inferior
        return 45 + (treeHeight * LEVEL_HEIGHT) + 100;
    }

    // Método para calcular el ancho necesario basado en el número de nodos y altura
    private double calculateRequiredWidth(int nodeCount, int treeHeight) {
        // Estimación: más nodos requieren más espacio horizontal
        // En el peor caso, todos los nodos estarían en el último nivel
        int maxNodesInLevel = (int) Math.pow(2, treeHeight);
        double requiredWidth = Math.max(1200, maxNodesInLevel * MIN_HORIZONTAL_SPACING * 0.8);

        // Limitar a un máximo razonable para evitar que sea demasiado ancho
        return Math.min(requiredWidth, 2500);
    }

    private BTreeNode getRoot(BTree tree) {
        try {
            Field rootField = BTree.class.getDeclaredField("root");
            rootField.setAccessible(true);
            return (BTreeNode) rootField.get(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Validar que cada nodo tenga máximo un padre - SIMPLIFICADO
    private boolean validateTreeStructure(BTreeNode root) {
        if (root == null) return true;

        java.util.Set<BTreeNode> visited = new java.util.HashSet<>();
        return validateNode(root, visited, null);
    }

    private boolean validateNode(BTreeNode node, java.util.Set<BTreeNode> visited, BTreeNode parent) {
        if (node == null) return true;

        // Si ya visitamos este nodo, hay un ciclo o referencia duplicada
        if (visited.contains(node)) {
            System.err.println("Error: Nodo duplicado detectado - " + node.data);
            return false;
        }

        visited.add(node);

        // Validar hijos
        return validateNode(node.left, visited, node) &&
                validateNode(node.right, visited, node);
    }

    private Map<BTreeNode, NodePosition> calculatePositions(BTreeNode root, double canvasWidth) {
        Map<BTreeNode, NodePosition> positions = new HashMap<>();

        // Calcular el ancho total necesario para cada subárbol
        Map<BTreeNode, Double> subtreeWidths = new HashMap<>();
        calculateSubtreeWidths(root, subtreeWidths);

        // Posicionar nodos centrados en el canvas
        positionNodeAdvanced(root, canvasWidth / 2, 45, positions, subtreeWidths);

        return positions;
    }

    private double calculateSubtreeWidths(BTreeNode node, Map<BTreeNode, Double> widths) {
        if (node == null) return 0;

        double leftWidth = calculateSubtreeWidths(node.left, widths);
        double rightWidth = calculateSubtreeWidths(node.right, widths);

        double totalWidth;
        if (leftWidth == 0 && rightWidth == 0) {
            // Nodo hoja - más compacto
            totalWidth = MIN_HORIZONTAL_SPACING * 0.7;
        } else {
            // Nodo interno - suma controlada
            totalWidth = leftWidth + rightWidth + (MIN_HORIZONTAL_SPACING * 0.6);
        }

        widths.put(node, totalWidth);
        return totalWidth;
    }

    private void positionNodeAdvanced(BTreeNode node, double x, double y,
                                      Map<BTreeNode, NodePosition> positions,
                                      Map<BTreeNode, Double> subtreeWidths) {
        if (node == null) return;

        positions.put(node, new NodePosition(x, y));

        double nextY = y + LEVEL_HEIGHT;

        if (node.left != null && node.right != null) {
            // Ambos hijos existen - usar separación más balanceada
            double leftWidth = subtreeWidths.getOrDefault(node.left, (double)MIN_HORIZONTAL_SPACING);
            double rightWidth = subtreeWidths.getOrDefault(node.right, (double)MIN_HORIZONTAL_SPACING);

            // Separación más controlada pero aumentada para evitar superposiciones
            double separation = Math.max(MIN_HORIZONTAL_SPACING * 0.8, Math.min(leftWidth, rightWidth) / 2.2);

            double leftX = x - separation;
            double rightX = x + separation;

            positionNodeAdvanced(node.left, leftX, nextY, positions, subtreeWidths);
            positionNodeAdvanced(node.right, rightX, nextY, positions, subtreeWidths);

        } else if (node.left != null) {
            // Solo hijo izquierdo
            positionNodeAdvanced(node.left, x - MIN_HORIZONTAL_SPACING / 2.8, nextY, positions, subtreeWidths);

        } else if (node.right != null) {
            // Solo hijo derecho
            positionNodeAdvanced(node.right, x + MIN_HORIZONTAL_SPACING / 2.8, nextY, positions, subtreeWidths);
        }
    }

    private void drawNodes(Pane pane, Map<BTreeNode, NodePosition> positions) {
        for (Map.Entry<BTreeNode, NodePosition> entry : positions.entrySet()) {
            BTreeNode node = entry.getKey();
            NodePosition pos = entry.getValue();

            // Dibujar el círculo del nodo
            Circle circle = new Circle(pos.x, pos.y, NODE_RADIUS);
            circle.setFill(NODE_COLOR);
            circle.setStroke(NODE_BORDER_COLOR);
            circle.setStrokeWidth(2.5);

            // Dibujar el texto del nodo
            Text text = new Text(node.data.toString());
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            text.setFill(Color.BLACK);

            // Centrar el texto en el círculo
            text.setX(pos.x - text.getBoundsInLocal().getWidth() / 2);
            text.setY(pos.y + text.getBoundsInLocal().getHeight() / 4);

            pane.getChildren().addAll(circle, text);
        }
    }

    // MÉTODO CORREGIDO: Solo dibuja conexiones directas padre-hijo
    private void drawConnections(Pane pane, BTreeNode node, Map<BTreeNode, NodePosition> positions) {
        if (node == null) return;

        NodePosition currentPos = positions.get(node);
        if (currentPos == null) return;

        // Dibujar línea al hijo izquierdo (solo si existe)
        if (node.left != null) {
            NodePosition leftPos = positions.get(node.left);
            if (leftPos != null) {
                Line line = new Line(currentPos.x, currentPos.y + NODE_RADIUS,
                        leftPos.x, leftPos.y - NODE_RADIUS);
                line.setStroke(LINE_COLOR);
                line.setStrokeWidth(2.5);
                pane.getChildren().add(line);
            }
        }

        // Dibujar línea al hijo derecho (solo si existe)
        if (node.right != null) {
            NodePosition rightPos = positions.get(node.right);
            if (rightPos != null) {
                Line line = new Line(currentPos.x, currentPos.y + NODE_RADIUS,
                        rightPos.x, rightPos.y - NODE_RADIUS);
                line.setStroke(LINE_COLOR);
                line.setStrokeWidth(2.5);
                pane.getChildren().add(line);
            }
        }

        // Continuar recursivamente con los hijos
        drawConnections(pane, node.left, positions);
        drawConnections(pane, node.right, positions);
    }

    private void drawLevelLines(Pane pane, int maxHeight, double canvasWidth) {
        for (int level = 0; level <= maxHeight; level++) {
            double y = 45 + (level * LEVEL_HEIGHT);

            // Línea horizontal del nivel
            Line levelLine = new Line(30, y, canvasWidth - 30, y);
            levelLine.setStroke(LEVEL_LINE_COLOR);
            levelLine.setStrokeWidth(1.5);
            levelLine.getStrokeDashArray().addAll(8d, 4d);
            levelLine.setOpacity(0.7);

            // Etiqueta del nivel
            Text levelLabel = new Text(5, y + 4, "Nivel " + level);
            levelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            levelLabel.setFill(LEVEL_LINE_COLOR);

            pane.getChildren().addAll(levelLine, levelLabel);
        }
    }

    private void drawTourNumbers(Pane pane, BTreeNode root, Map<BTreeNode, NodePosition> positions, String tourType) {
        Map<BTreeNode, Integer> tourOrder = new HashMap<>();
        int[] counter = {1}; // Array para pasar por referencia

        switch (tourType.toLowerCase()) {
            case "preorder":
                calculatePreOrder(root, tourOrder, counter);
                break;
            case "inorder":
                calculateInOrder(root, tourOrder, counter);
                break;
            case "postorder":
                calculatePostOrder(root, tourOrder, counter);
                break;
        }

        // Dibujar números debajo de los nodos
        for (Map.Entry<BTreeNode, Integer> entry : tourOrder.entrySet()) {
            BTreeNode node = entry.getKey();
            Integer order = entry.getValue();
            NodePosition pos = positions.get(node);

            if (pos != null) {
                Text numberText = new Text(String.valueOf(order));
                numberText.setFont(Font.font("Arial", FontWeight.BOLD, 11));
                numberText.setFill(Color.BLUE);

                // Calcular el centro del texto después de crear el nodo
                pane.getChildren().add(numberText); // Agregar temporalmente para calcular bounds
                double textWidth = numberText.getBoundsInLocal().getWidth();
                pane.getChildren().remove(numberText); // Remover temporalmente

                // Posicionar centrado debajo del nodo
                numberText.setX(pos.x - textWidth / 2);
                numberText.setY(pos.y + NODE_RADIUS + 18);

                pane.getChildren().add(numberText);
            }
        }
    }

    private void calculatePreOrder(BTreeNode node, Map<BTreeNode, Integer> order, int[] counter) {
        if (node == null) return;
        order.put(node, counter[0]++);
        calculatePreOrder(node.left, order, counter);
        calculatePreOrder(node.right, order, counter);
    }

    private void calculateInOrder(BTreeNode node, Map<BTreeNode, Integer> order, int[] counter) {
        if (node == null) return;
        calculateInOrder(node.left, order, counter);
        order.put(node, counter[0]++);
        calculateInOrder(node.right, order, counter);
    }

    private void calculatePostOrder(BTreeNode node, Map<BTreeNode, Integer> order, int[] counter) {
        if (node == null) return;
        calculatePostOrder(node.left, order, counter);
        calculatePostOrder(node.right, order, counter);
        order.put(node, counter[0]++);
    }

    // Clase interna para representar posiciones de nodos
    private static class NodePosition {
        double x, y;

        NodePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}