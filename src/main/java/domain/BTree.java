package domain;

public class BTree implements Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public int size() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null) return false;
        else if(util.Utility.compare(node.data, element)==0) return true;
        return binarySearch(node.left, element) || binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        // Verificar si el elemento ya existe para evitar duplicados
        try {
            if (!isEmpty() && contains(element)) {
                return; // No agregar duplicados
            }
        } catch (TreeException e) {
            // Si hay error verificando, continuar con la inserción
        }

        this.root = addBalanced(root, element, "root");
    }

    // Nuevo algoritmo de inserción más balanceado y correcto
    private BTreeNode addBalanced(BTreeNode node, Object element, String path) {
        if (node == null) {
            return new BTreeNode(element, path);
        }

        // Inserción balanceada: comparar alturas de subárboles
        int leftHeight = getSubtreeHeight(node.left);
        int rightHeight = getSubtreeHeight(node.right);

        if (leftHeight <= rightHeight) {
            // Insertar en subárbol izquierdo si es menor o igual
            node.left = addBalanced(node.left, element, path + "/left");
        } else {
            // Insertar en subárbol derecho si el izquierdo es mayor
            node.right = addBalanced(node.right, element, path + "/right");
        }

        return node;
    }

    // Método auxiliar para calcular altura de subárbol
    private int getSubtreeHeight(BTreeNode node) {
        if (node == null) return -1;
        return Math.max(getSubtreeHeight(node.left), getSubtreeHeight(node.right)) + 1;
    }

    // Método para debuggear la estructura del árbol
    public void printTreeStructure() {
        System.out.println("=== ESTRUCTURA DEL ÁRBOL ===");
        if (isEmpty()) {
            System.out.println("Árbol vacío");
            return;
        }
        printTreeStructure(root, "", true);
    }

    private void printTreeStructure(BTreeNode node, String prefix, boolean isLast) {
        if (node != null) {
            System.out.println(prefix + (isLast ? "└── " : "├── ") + node.data + " (" + node.path + ")");

            // Verificar referencias
            boolean hasLeft = node.left != null;
            boolean hasRight = node.right != null;

            if (hasLeft || hasRight) {
                if (hasLeft) {
                    printTreeStructure(node.left, prefix + (isLast ? "    " : "│   "), !hasRight);
                }
                if (hasRight) {
                    printTreeStructure(node.right, prefix + (isLast ? "    " : "│   "), true);
                }
            }
        }
    }

    // Método para validar que no hay ciclos ni referencias duplicadas
    public boolean validateStructure() {
        if (isEmpty()) return true;

        java.util.Set<BTreeNode> visited = new java.util.HashSet<>();
        java.util.Set<Object> values = new java.util.HashSet<>();

        return validateStructure(root, visited, values, null);
    }

    private boolean validateStructure(BTreeNode node, java.util.Set<BTreeNode> visited,
                                      java.util.Set<Object> values, BTreeNode parent) {
        if (node == null) return true;

        // Verificar si ya visitamos este nodo (indica ciclo)
        if (visited.contains(node)) {
            System.err.println("ERROR: Ciclo detectado en nodo " + node.data);
            return false;
        }

        // Verificar si ya existe este valor (indica duplicado)
        if (values.contains(node.data)) {
            System.err.println("ERROR: Valor duplicado detectado: " + node.data);
            return false;
        }

        visited.add(node);
        values.add(node.data);

        // Validar recursivamente los hijos
        return validateStructure(node.left, visited, values, node) &&
                validateStructure(node.right, visited, values, node);
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        root = remove(root,element, new boolean[]{false});
    }

    private BTreeNode remove(BTreeNode node, Object element, boolean[] deleted) throws TreeException{
        if(node!=null){
            if(util.Utility.compare(node.data, element)==0){
                deleted[0] = true;

                //caso 1. es un nodo si hijos, es una hoja
                if(node.left==null && node.right==null) return null;
                    //caso 2-a. el nodo solo tien un hijo, el hijo izq
                else if (node.left!=null&&node.right==null) {
                    node.left = newPath(node.left, node.path);
                    return node.left;
                } //caso 2-b. el nodo solo tien un hijo, el hijo der
                else if (node.left==null&&node.right!=null) {
                    node.right = newPath(node.right, node.path);
                    return node.right;
                }
                //caso 3. el nodo tiene dos hijos
                else{
                    Object value = getLeaf(node.right);
                    node.data = value;
                    node.right = removeLeaf(node.right, value, new boolean[]{false});
                }
            }
            if(!deleted[0]) node.left = remove(node.left, element, deleted);
            if(!deleted[0]) node.right = remove(node.right, element, deleted);
        }
        return node;
    }

    private BTreeNode newPath(BTreeNode node,String label){
        if(node!=null){
            node.path = label;
            node.left = newPath(node.left,label+"/left");
            node.right = newPath(node.right,label+"/right");
        }
        return node;
    }

    private Object getLeaf(BTreeNode node){
        Object aux;
        if(node==null) return null;
        else if(node.left==null&&node.right==null) return node.data;
        else{
            aux = getLeaf(node.left);
            if(aux==null) aux = getLeaf(node.right);
        }
        return aux;
    }

    private BTreeNode removeLeaf(BTreeNode node, Object value, boolean[] deleted){
        if(node==null) return null;
        else if(node.left==null&&node.right==null&&util.Utility.compare(node.data, value)==0) {
            deleted[0] = true;
            return null;
        }else{
            node.left = removeLeaf(node.left, value, deleted);
            if(!deleted[0]) node.right = removeLeaf(node.right, value, deleted);
        }
        return node;
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return height(root, element, 0);
    }

    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(util.Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, level+1),
                    height(node.right, element, level+1));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return height(root);
    }

    private int height(BTreeNode node){
        if(node==null) return -1;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        return null;
    }

    @Override
    public Object max() throws TreeException {
        return null;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return preOrder(root).trim();
    }

    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = node.data + " ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return inOrder(root).trim();
    }

    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return postOrder(root).trim();
    }

    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }

    @Override
    public String toString() {
        String result="Binary Tree Content:";
        try {
            result = "PreOrder: "+preOrder();
            result+= "\nInOrder: "+inOrder();
            result+= "\nPostOrder: "+postOrder();

        } catch (TreeException e) {
            result = "Binary Tree is empty";
        }
        return result;
    }

    // MÉTODOS ADICIONALES REQUERIDOS POR EL LABORATORIO

    public String printLeaves() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        String result = printLeaves(root).trim();
        return "Binary tree - leaves: " + (result.endsWith(", ") ? result.substring(0, result.length()-2) : result);
    }

    private String printLeaves(BTreeNode node){
        if(node == null) return "";

        String result = "";
        if(node.left == null && node.right == null) {
            result += node.data + ", ";
        }
        result += printLeaves(node.left);
        result += printLeaves(node.right);
        return result;
    }

    public String printNodes1Child() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes 1 child\n" + printNodes1Child(root);
    }

    private String printNodes1Child(BTreeNode node) {
        if (node == null) return "";

        String result = "";
        if ((node.left != null && node.right == null) || (node.left == null && node.right != null)) {
            result += "Node: " + node.data;
            if (node.left != null) {
                result += ", left son: " + node.left.data;
            }
            if (node.right != null) {
                result += ", right son: " + node.right.data;
            }
            result += "\n";
        }
        result += printNodes1Child(node.left);
        result += printNodes1Child(node.right);
        return result;
    }

    public String printNodes2Children() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes 2 children\n" + printNodes2Children(root);
    }

    private String printNodes2Children(BTreeNode node) {
        if (node == null) return "";

        String result = "";
        if (node.left != null && node.right != null) {
            result += "Node: " + node.data + ", left son: " + node.left.data +
                    ", right son: " + node.right.data + "\n";
        }
        result += printNodes2Children(node.left);
        result += printNodes2Children(node.right);
        return result;
    }

    public String printNodesWithChildren() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes with children\n" + printNodesWithChildren(root);
    }

    private String printNodesWithChildren(BTreeNode node) {
        if (node == null) return "";

        String result = "";
        if (node.left != null || node.right != null) {
            result += "Node: " + node.data + ", children: ";
            if (node.left != null && node.right != null) {
                result += node.left.data + ", " + node.right.data;
            } else if (node.left != null) {
                result += "left son: " + node.left.data;
            } else {
                result += "right son: " + node.right.data;
            }
            result += "\n";
        }
        result += printNodesWithChildren(node.left);
        result += printNodesWithChildren(node.right);
        return result;
    }

    public String printSubTree(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        if(!contains(element))
            throw new TreeException("Element not found in the tree");

        BTreeNode foundNode = findNode(root, element);
        String subtreeContent = printSubTree(foundNode).trim();
        return "Binary tree - subtree: " + (subtreeContent.endsWith(", ") ?
                subtreeContent.substring(0, subtreeContent.length()-2) : subtreeContent);
    }

    private BTreeNode findNode(BTreeNode node, Object element) {
        if(node == null) return null;
        if(util.Utility.compare(node.data, element) == 0) return node;

        BTreeNode leftResult = findNode(node.left, element);
        if(leftResult != null) return leftResult;

        return findNode(node.right, element);
    }

    private String printSubTree(BTreeNode node) {
        if(node == null) return "";

        String result = node.data + ", ";
        result += printSubTree(node.left);
        result += printSubTree(node.right);
        return result;
    }

    public int totalLeaves() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return totalLeaves(root);
    }

    private int totalLeaves(BTreeNode node) {
        if(node == null) return 0;
        if(node.left == null && node.right == null) return 1;
        return totalLeaves(node.left) + totalLeaves(node.right);
    }
}