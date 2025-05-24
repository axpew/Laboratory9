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
        //this.root = add(root, element);
        this.root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null)
            node = new BTreeNode(element);
        else{
            int value = util.Utility.random(100);
            if(value%2==0)
                node.left = add(node.left, element);
            else node.right = add(node.right, element);
        }
        return node;
    }

    private BTreeNode add(BTreeNode node, Object element, String path){
        if(node==null)
            node = new BTreeNode(element, path);
        else{
            int value = util.Utility.random(100);
            if(value%2==0)
                node.left = add(node.left, element, path+"/left");
            else node.right = add(node.right, element, path+"/right");
        }
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        //Contribución de Jefferson Varela para que no borre repetidos. Excelente!!!
        //para que el boolean funcione bien en el llamado recursivo, debe ir en un array
        root = remove(root,element, new boolean[]{false});
        //root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element, boolean[] deleted) throws TreeException{
        if(node!=null){
            if(util.Utility.compare(node.data, element)==0){
                deleted[0] = true; // cambia a true porque lo va a eliminar

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
                    //else if (node.left!=null&&node.right!=null) {
                    /* *
                     * El algoritmo de supresión dice que cuando el nodo a suprimir
                     * tiene 2 hijos, entonces busque una hoja del subarbol derecho
                     * y sustituya la data del nodo a suprimir por la data de esa hoja,
                     * luego elimine esa hojo
                     * */
                    Object value = getLeaf(node.right);
                    node.data = value;
                    node.right = removeLeaf(node.right, value, new boolean[]{false});
                }
            }
            if(!deleted[0]) node.left = remove(node.left, element, deleted); //llamado recursivo por la izq
            if(!deleted[0]) node.right = remove(node.right, element, deleted); //llamado recursivo por la der
        }
        return node; //retorna el nodo modificado o no
    }

    /* *
     * Funciona cuando se invoca al metodo remove
     * Sirve para actualizar los labels del nodo removido y sus
     * descendientes (cuando aplica)
     * */
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
        else if(node.left==null&&node.right==null) return node.data; //es una hoja
        else{
            aux = getLeaf(node.left); //siga bajando por el subarbol izq
            if(aux==null) aux = getLeaf(node.right);
        }
        return aux;
    }

    private BTreeNode removeLeaf(BTreeNode node, Object value, boolean[] deleted){
        if(node==null) return null;
            //si es una hoja y esa hoja es la que andamos buscando, la eliminamos
        else if(node.left==null&&node.right==null&&util.Utility.compare(node.data, value)==0) {
            deleted[0] = true; //el elemento fue eliminado
            return null; //es una hoja y la elimina
        }else{
            node.left = removeLeaf(node.left, value, deleted);
            if(!deleted[0]) node.right = removeLeaf(node.right, value, deleted);
        }
        return node; //retorna el subarbol derecho con la hoja eliminada
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return height(root, element, 0);
    }

    //devuelve la altura de un nodo (el número de ancestros)
    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(util.Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        //return height(root, 0); //opción-1
        return height(root); //opción-2
    }

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    private int height(BTreeNode node, int level){
        if(node==null) return level-1;//se le resta 1 al nivel pq no cuente el nulo
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    //opcion-2
    private int height(BTreeNode node){
        if(node==null) return -1; //retorna valor negativo para eliminar el nivel del nulo
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
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            //result = node.data+" ";
            result  = node.data+"("+node.path+")"+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
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
            throw new RuntimeException(e);
        }
        return result;
    }

    // MÉTODOS A IMPLEMENTAR
    // ------------------------------------------------

    // Para imprimir las hojas del árbol
    public String printLeaves() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - leaves: " + printLeaves(root).trim();
    }

    private String printLeaves(BTreeNode node){
        if(node == null) return "";
        else {
            String result = "";
            // Si es una hoja (no tiene hijos)
            if(node.left == null && node.right == null) {
                result += node.data + ", ";
            }
            // Recorre recursivamente el árbol
            result += printLeaves(node.left);
            result += printLeaves(node.right);
            return result;
        }
    }

    // Para imprimir los nodos con exactamente un hijo
    public String printNodes1Child() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes 1 child\n" + printNodes1Child(root);
    }

    private String printNodes1Child(BTreeNode node) {
        if (node == null)
            return "";
        else {
            String result = "";
            // Si tiene exactamente un hijo
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
            // Recorre recursivamente
            result += printNodes1Child(node.left);
            result += printNodes1Child(node.right);
            return result;
        }
    }

    // Para imprimir los nodos con exactamente dos hijos
    public String printNodes2Children() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes 2 children\n" + printNodes2Children(root);
    }

    private String printNodes2Children(BTreeNode node) {
        if (node == null)
            return "";
        else {
            String result = "";
            // Si tiene exactamente dos hijos
            if (node.left != null && node.right != null) {
                result += "Node: " + node.data + ", left son: " + node.left.data +
                        ", right son: " + node.right.data + "\n";
            }
            // Recorre recursivamente
            result += printNodes2Children(node.left);
            result += printNodes2Children(node.right);
            return result;
        }
    }

    // Para imprimir nodos con hijos (uno o dos hijos)
    public String printNodesWithChildren() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return "Binary tree - nodes with children\n" + printNodesWithChildren(root);
    }

    private String printNodesWithChildren(BTreeNode node) {
        if (node == null)
            return "";
        else {
            String result = "";
            // Si tiene al menos un hijo
            if (node.left != null || node.right != null) {
                result += "Node: " + node.data + ", children: ";
                if (node.left != null) {
                    result += node.left.data;
                    if (node.right != null) {
                        result += ", " + node.right.data;
                    }
                } else if (node.right != null) {
                    result += node.right.data;
                }
                result += "\n";
            }
            // Recorre recursivamente
            result += printNodesWithChildren(node.left);
            result += printNodesWithChildren(node.right);
            return result;
        }
    }

    // Para imprimir un subárbol a partir de un elemento
    public String printSubTree(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        if(!contains(element))
            throw new TreeException("Element not found in the tree");
        return "Binary tree - subtree: " + element + "\n" + printSubTree(findNode(root, element));
    }

    // Método auxiliar para encontrar el nodo que contiene el elemento
    private BTreeNode findNode(BTreeNode node, Object element) {
        if(node == null) return null;
        if(util.Utility.compare(node.data, element) == 0) return node;

        BTreeNode leftResult = findNode(node.left, element);
        if(leftResult != null) return leftResult;

        return findNode(node.right, element);
    }

    private String printSubTree(BTreeNode node) {
        if(node == null) return "";

        // Construye el recorrido del subárbol (preorder)
        String result = "";
        result += node.data + ", ";
        result += printSubTree(node.left);
        result += printSubTree(node.right);

        return result;
    }

    // Para contar el número total de hojas
    public int totalLeaves() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return totalLeaves(root);
    }

    private int totalLeaves(BTreeNode node) {
        if(node == null) return 0;
        // Si es hoja, retorna 1
        if(node.left == null && node.right == null) return 1;
        // Si no es hoja, suma las hojas de los subárboles
        return totalLeaves(node.left) + totalLeaves(node.right);
    }
}