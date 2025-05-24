package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    @Test
    void testBTreeRequirements() {
        try {
            System.out.println("a. Creando árbol e insertando 30 valores aleatorios entre 0 y 50:");
            BTree btree = new BTree();

            for (int i = 0; i < 30; i++) {
                // Para generar números entre 0 y 50
                int value = util.Utility.random(51);
                btree.add(value);
                System.out.println("Valor insertado: " + value);
            }

            System.out.println("\nb. Recorridos del árbol:");
            System.out.println(btree.toString());

            System.out.println("\nc. Pruebas de métodos:");
            System.out.println("- Tamaño del árbol (size): " + btree.size());
            System.out.println("- Altura del árbol (height): " + btree.height());

            // CORREGIDO: El preOrder() solo devuelve números separados por espacios
            String preOrderElements = btree.preOrder();
            String[] elements = preOrderElements.trim().split("\\s+"); // Split por espacios

            int testCount = Math.min(5, elements.length);
            System.out.println("\nPrueba de height(element) y contains(element):");

            for (int i = 0; i < testCount; i++) {
                if (!elements[i].isEmpty()) {
                    try {
                        int num = Integer.parseInt(elements[i].trim());
                        System.out.println("- Elemento " + num + ":");
                        System.out.println("  * Existe en el árbol (contains): " + btree.contains(num));
                        System.out.println("  * Altura del elemento (height): " + btree.height(num));
                    } catch (NumberFormatException e) {
                        System.out.println("- Error parseando elemento: '" + elements[i] + "'");
                    }
                }
            }

            System.out.println("\nd. Verificando existencia de 20 valores aleatorios:");
            int[] randomValues = new int[20];
            for (int i = 0; i < 20; i++) {
                randomValues[i] = util.Utility.random(51); // Valores entre 0-50
                boolean exists = btree.contains(randomValues[i]);
                System.out.println("- Valor " + randomValues[i] +
                        (exists ? " existe" : " NO existe") + " en el árbol");
            }

            System.out.println("\ne. Eliminando elementos que existen en el árbol:");
            for (int value : randomValues) {
                if (btree.contains(value)) {
                    System.out.println("- Eliminando valor " + value);
                    btree.remove(value);
                }
            }

            System.out.println("\nf. Contenido del árbol después de eliminar elementos:");
            System.out.println(btree.toString());

            System.out.println("\ng. Altura de cada elemento en recorrido preOrder:");
            String updatedPreOrder = btree.preOrder();
            String[] updatedElements = updatedPreOrder.trim().split("\\s+");

            for (String element : updatedElements) {
                if (!element.isEmpty()) {
                    try {
                        int num = Integer.parseInt(element.trim());
                        System.out.println("- Elemento " + num + " - Altura: " + btree.height(num));
                    } catch (NumberFormatException e) {
                        System.out.println("- Error parseando elemento: '" + element + "'");
                    }
                }
            }

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testHeight() throws TreeException {
        BTree bTree = new BTree();
        bTree.add(20);
        bTree.add(30);
        bTree.add(18);
        bTree.add(4);
        bTree.add(5);
        bTree.add(50);
        bTree.add(70);

        System.out.println("=== PRUEBA DE ALTURA ===");
        System.out.println(bTree);
        System.out.println("Binary tree - height(20): " + bTree.height(20));
        System.out.println("Binary tree - height(30): " + bTree.height(30));
        System.out.println("Binary tree - height(18): " + bTree.height(18));
        System.out.println("Binary tree - height(4): " + bTree.height(4));
        System.out.println("Binary tree - height(5): " + bTree.height(5));
        System.out.println("Binary tree - height(50): " + bTree.height(50));
        System.out.println("Binary tree - height(70): " + bTree.height(70));
        System.out.println();
        System.out.println("Binary tree - height: " + bTree.height());
    }

    @Test
    void testExtendedMethods() {
        try {
            BTree btree = new BTree();

            System.out.println("=== PRUEBA DE MÉTODOS EXTENDIDOS ===");

            // Agregamos elementos al árbol
            int[] testValues = {14, 17, 48, 35, 4, 44, 39, 10, 47, 42, 21, 28, 1, 33};

            System.out.println("Agregando elementos al árbol:");
            for (int value : testValues) {
                btree.add(value);
                System.out.print(value + " ");
            }
            System.out.println("\n");

            // Mostrar el árbol completo
            System.out.println("Árbol completo:");
            System.out.println(btree.toString());
            System.out.println();

            // Mostrar hojas
            System.out.println(btree.printLeaves());
            System.out.println();

            // Mostrar nodos con 1 hijo
            System.out.println(btree.printNodes1Child());

            // Mostrar nodos con 2 hijos
            System.out.println(btree.printNodes2Children());

            // Mostrar nodos con hijos (1 o 2)
            System.out.println(btree.printNodesWithChildren());

            // Mostrar subárboles para diferentes elementos
            System.out.println("=== SUBÁRBOLES ===");
            int[] subtreeTests = {42, 17, 48, 33};
            for (int element : subtreeTests) {
                if (btree.contains(element)) {
                    System.out.println(btree.printSubTree(element));
                } else {
                    System.out.println("Elemento " + element + " no existe en el árbol");
                }
            }

            // Mostrar total de hojas
            System.out.println("Binary tree - total leaves: " + btree.totalLeaves());

            // Información adicional del árbol
            System.out.println("\n=== INFORMACIÓN ADICIONAL ===");
            System.out.println("Tamaño del árbol: " + btree.size());
            System.out.println("Altura del árbol: " + btree.height());

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testTreeStructureValidation() {
        try {
            System.out.println("=== PRUEBA DE VALIDACIÓN DE ESTRUCTURA ===");

            BTree btree = new BTree();

            // Agregar algunos elementos
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int value : values) {
                btree.add(value);
            }

            System.out.println("Árbol creado con valores: 50, 30, 70, 20, 40, 60, 80");
            System.out.println("Validación de estructura: " + btree.validateStructure());
            System.out.println("Recorridos:");
            System.out.println("PreOrder: " + btree.preOrder());
            System.out.println("InOrder: " + btree.inOrder());
            System.out.println("PostOrder: " + btree.postOrder());

            // Mostrar estructura del árbol
            System.out.println("\nEstructura del árbol:");
            btree.printTreeStructure();

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}