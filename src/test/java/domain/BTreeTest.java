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

            String preOrderElements = btree.preOrder();
            String[] elements = preOrderElements.split(" ");

            int testCount = Math.min(5, elements.length);
            System.out.println("\nPrueba de height(element) y contains(element):");

            for (int i = 0; i < testCount; i++) {
                String element = elements[i];
                String numStr = element.substring(0, element.indexOf('('));
                int num = Integer.parseInt(numStr);

                System.out.println("- Elemento " + num + ":");
                System.out.println("  * Existe en el árbol (contains): " + btree.contains(num));
                System.out.println("  * Altura del elemento (height): " + btree.height(num));
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
            String[] updatedElements = updatedPreOrder.split(" ");

            for (String element : updatedElements) {
                if (element.isEmpty()) continue;

                // Extraer el número del formato "valor(path)"
                String numStr = element.substring(0, element.indexOf('('));
                int num = Integer.parseInt(numStr);
                System.out.println("- Elemento " + element + " - Altura: " + btree.height(num));
            }

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testHeight() throws TreeException {
        BTree bTree = new BTree();
        bTree.add(20); bTree.add(30); bTree.add(18); bTree.add(4); bTree.add(5); bTree.add(50); bTree.add(70);

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

            // Agregamos elementos al árbol
            btree.add(14);
            btree.add(17);
            btree.add(48);
            btree.add(35);
            btree.add(4);
            btree.add(44);
            btree.add(39);
            btree.add(10);
            btree.add(47);
            btree.add(42);
            btree.add(21);
            btree.add(28);
            btree.add(1);
            btree.add(33);

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
            System.out.println(btree.printSubTree(42));
            System.out.println(btree.printSubTree(17));
            System.out.println(btree.printSubTree(48));
            System.out.println(btree.printSubTree(33));

            // Mostrar total de hojas
            System.out.println("Binary tree - total leaves: " + btree.totalLeaves());

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}