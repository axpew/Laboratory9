package util;

import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria con métodos adicionales para trabajar con árboles binarios
 */
public class TreeUtils {

    /**
     * Convierte un árbol binario a una representación textual con estructura visual
     */
    public static String treeToString(BTree tree) {
        try {
            if (tree.isEmpty()) {
                return "Árbol vacío";
            }

            BTreeNode root = getRoot(tree);
            StringBuilder sb = new StringBuilder();
            sb.append("Estructura del Árbol:\n");
            sb.append("====================\n");
            printTree(root, "", true, sb);
            return sb.toString();

        } catch (Exception e) {
            return "Error generando representación del árbol: " + e.getMessage();
        }
    }

    private static void printTree(BTreeNode node, String prefix, boolean isLast, StringBuilder sb) {
        if (node != null) {
            sb.append(prefix);
            sb.append(isLast ? "└── " : "├── ");
            sb.append(node.data).append("\n");

            if (node.left != null || node.right != null) {
                if (node.left != null) {
                    printTree(node.left, prefix + (isLast ? "    " : "│   "), node.right == null, sb);
                }
                if (node.right != null) {
                    printTree(node.right, prefix + (isLast ? "    " : "│   "), true, sb);
                }
            }
        }
    }

    /**
     * Obtiene todos los elementos del árbol en forma de lista
     */
    public static List<Integer> getAllElements(BTree tree) {
        List<Integer> elements = new ArrayList<>();
        try {
            if (!tree.isEmpty()) {
                BTreeNode root = getRoot(tree);
                collectElements(root, elements);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo elementos: " + e.getMessage());
        }
        return elements;
    }

    private static void collectElements(BTreeNode node, List<Integer> elements) {
        if (node != null) {
            elements.add((Integer) node.data);
            collectElements(node.left, elements);
            collectElements(node.right, elements);
        }
    }

    /**
     * Calcula estadísticas del árbol
     */
    public static String getTreeStatistics(BTree tree) {
        try {
            if (tree.isEmpty()) {
                return "El árbol está vacío";
            }

            StringBuilder stats = new StringBuilder();
            stats.append("=== ESTADÍSTICAS DEL ÁRBOL ===\n");
            stats.append("Número de elementos: ").append(tree.size()).append("\n");
            stats.append("Altura del árbol: ").append(tree.height()).append("\n");
            stats.append("Número de hojas: ").append(tree.totalLeaves()).append("\n");

            List<Integer> elements = getAllElements(tree);
            if (!elements.isEmpty()) {
                int min = elements.stream().min(Integer::compareTo).orElse(0);
                int max = elements.stream().max(Integer::compareTo).orElse(0);
                double avg = elements.stream().mapToInt(Integer::intValue).average().orElse(0.0);

                stats.append("Valor mínimo: ").append(min).append("\n");
                stats.append("Valor máximo: ").append(max).append("\n");
                stats.append("Promedio: ").append(String.format("%.2f", avg)).append("\n");
            }

            return stats.toString();

        } catch (TreeException e) {
            return "Error calculando estadísticas: " + e.getMessage();
        }
    }

    /**
     * Verifica si el árbol está balanceado
     */
    public static boolean isBalanced(BTree tree) {
        try {
            if (tree.isEmpty()) return true;
            BTreeNode root = getRoot(tree);
            return checkBalance(root) != -1;
        } catch (Exception e) {
            return false;
        }
    }

    private static int checkBalance(BTreeNode node) {
        if (node == null) return 0;

        int leftHeight = checkBalance(node.left);
        if (leftHeight == -1) return -1;

        int rightHeight = checkBalance(node.right);
        if (rightHeight == -1) return -1;

        if (Math.abs(leftHeight - rightHeight) > 1) return -1;

        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Genera un reporte completo del árbol
     */
    public static String generateCompleteReport(BTree tree) {
        StringBuilder report = new StringBuilder();

        report.append("=== REPORTE COMPLETO DEL ÁRBOL BINARIO ===\n\n");

        try {
            if (tree.isEmpty()) {
                report.append("El árbol está vacío.\n");
                return report.toString();
            }

            // Estadísticas básicas
            report.append(getTreeStatistics(tree)).append("\n");

            // Información de balance
            report.append("¿Está balanceado?: ").append(isBalanced(tree) ? "SÍ" : "NO").append("\n\n");

            // Recorridos
            report.append("=== RECORRIDOS ===\n");
            report.append("PreOrder:  ").append(tree.preOrder()).append("\n");
            report.append("InOrder:   ").append(tree.inOrder()).append("\n");
            report.append("PostOrder: ").append(tree.postOrder()).append("\n\n");

            // Información detallada de nodos
            report.append("=== INFORMACIÓN DETALLADA DE NODOS ===\n");
            report.append(tree.printLeaves()).append("\n\n");
            report.append(tree.printNodes1Child()).append("\n");
            report.append(tree.printNodes2Children()).append("\n");
            report.append(tree.printNodesWithChildren()).append("\n");

            // Estructura visual
            report.append("\n=== ESTRUCTURA VISUAL ===\n");
            report.append(treeToString(tree));

        } catch (TreeException e) {
            report.append("Error generando reporte: ").append(e.getMessage());
        }

        return report.toString();
    }

    /**
     * Método auxiliar para obtener la raíz del árbol usando reflexión
     */
    private static BTreeNode getRoot(BTree tree) throws Exception {
        Field rootField = BTree.class.getDeclaredField("root");
        rootField.setAccessible(true);
        return (BTreeNode) rootField.get(tree);
    }
}