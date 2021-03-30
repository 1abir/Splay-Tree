
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Node<T extends Comparable<T>>
{
    Node<T> left, right, parent;
    T data;

    public Node(T data) {
        this.data = data;
    }
}
class BTreePrinter {

    public static <T extends Comparable<T>> void printNode(Node<T> root) {
        int maxLevel = BTreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<T>> void printNodeInternal
            (List<Node<T>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BTreePrinter.printWhitespaces(firstSpaces);

        List<Node<T>> newNodes = new ArrayList<Node<T>>();
        for (Node<T> node : nodes) {
            if (node != null) {
                System.out.print(node.data);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BTreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BTreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).left != null)
                    System.out.print("/");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(i + i - 1);

                if (nodes.get(j).right != null)
                    System.out.print("\\");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<T>> int maxLevel(Node<T> node) {
        if (node == null)
            return 0;

        return Math.max(BTreePrinter.maxLevel(node.left), BTreePrinter.maxLevel(node.right)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }
        return true;
    }

}

class BTreePrinterTest {
  
   public static Node<Integer> test()
    {
        Node<Integer> root = new Node<Integer>(21);
        Node<Integer> n11 = new Node<Integer>(7);
        Node<Integer> n12 = new Node<Integer>(51);
        Node<Integer> n21 = new Node<Integer>(2);
        Node<Integer> n22 = new Node<Integer>(11);
        Node<Integer> n23 = new Node<Integer>(91);
        Node<Integer> n31 = new Node<Integer>(9);
        Node<Integer> n32 = new Node<Integer>(18);
        Node<Integer> n33 = new Node<Integer>(71);

        root.left = n11;
        n11.parent=root;
        root.right = n12;
        n12.parent=root;

        n11.left = n21;
        n11.right = n22;

        n21.parent=n11;
        n22.parent=n11;

        n12.right = n23;
        n22.left = n31;

        n23.parent=n12;
        n31.parent= n22;

        n22.right = n32;
        n23.left = n33;

        n32.parent=n22;
        n33.parent=n23;

        return root;
    }
}

class SplayTree<T extends Comparable<T>> {
    Node<T> root;

    public SplayTree(Node<T> root) {
        this.root = root;
    }

    public SplayTree() {
    }

    public SplayTree(T rootElement) {
        root = new Node<>(rootElement);
    }


    public void splay(Node<T> x) {
        int l = level(x);
        if (l > 2) {
            Node<T> parent = x.parent;
            Node<T> grandParent = parent.parent;
            if (grandParent.left == parent && parent.left == x) {
                rightRotate(grandParent);
                rightRotate(parent);
            } else if (grandParent.right == parent && parent.right == x) {
                leftRotate(grandParent);
                leftRotate(parent);
            } else if (grandParent.left == parent && parent.right == x) {
                leftRotate(parent);
                rightRotate(grandParent);
            } else if (grandParent.right == parent && parent.left == x) {
                rightRotate(parent);
                leftRotate(grandParent);
            }
            splay(x);
        } else if (l == 2) {
            Node<T> parent = x.parent;
            if (parent.right == x)
                leftRotate(parent);
            else rightRotate(parent);
        }
        root = x;
    }
    public void splay(T x)
    {
        search(x);
    }
    public void insert(T x) {
        Node<T> node = root;
        if(root==null)
        {
            root=new Node<>(x);
            return;
        }
        Node<T> parent =root.parent;
        Node<T> in = new Node<>(x);
        int lr = 0;
        while (node != null) {
            if (x.compareTo(node.data) >= 0) {
                parent=node;
                node = node.right;
                lr = 1;
            } else if (x.compareTo(node.data) < 0) {
                parent = node;
                node = node.left;
                lr = -1;
            }
        }
        if (lr == 1) parent.right = in;
        else if (lr == -1) parent.left = in;
        in.parent = parent;
        splay(in);
    }

    public boolean search(T x)
    {
        Node<T> node = root;
        if(root==null)
        {
            return false;
        }
        Node<T> parent=node.parent;
        while (node != null) {
            if (x.compareTo(node.data) > 0) {
                parent=node;
                node = node.right;
            } else if (x.compareTo(node.data) < 0) {
                parent = node;
                node = node.left;
            }
            else {
                splay(node);
                return true;
            }
        }
        if (parent!=null)
        {
            splay(parent);
        }
        return false;
    }

    public void delete(T x)
    {
        if(!search(x))
            return;
        Node<T> left=root.left;
        Node<T> right=root.right;
        left.parent=null;
        right.parent=null;
        root=join(left,right);
    }
    public void print()
    {
        BTreePrinter.printNode(root);
    }
    
	public Node<T> join(Node<T> left,Node<T> right)
    {
        if(left==null)
        {
            if(right!=null)
                right.parent=null;
            return right;
        }
        Node<T> al=heightest(left);
        left.parent=null;
        splay(al);
        al.right=right;
        if(right!=null)
            right.parent=al;
        return al;
    }
    public Node<T> heightest(Node<T> rNode)
    {
        Node<T> node= rNode;
        while (node.right!=null)
            node=node.right;
        return node;
    }
    int level(Node<T> node) {
        if (node == null)
            return 0;
        else if (node.parent == null)
            return 1;
        return level(node.parent) + 1;
    }

    int maxLevel(Node<T> node) {
        if (node == null)
            return 0;
        return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
    }

    Node<T> leftRotate(Node<T> x) {
        if (maxLevel(x) >= 1 && x.right != null) {
            Node<T> parent = x.parent, rightChild = x.right, rl = rightChild.left;
            if (parent != null)
                if (parent.right == x) {
                    parent.right = rightChild;
                } else {
                    parent.left = rightChild;
                }
            rightChild.parent = parent;

            x.parent = rightChild;
            rightChild.left = x;

            x.right = rl;
            if (rl != null)
                rl.parent = x;
            return rightChild;
        }
        return x;
    }

    Node<T> rightRotate(Node<T> x) {
        if (maxLevel(x) >= 1 && x.left != null) {
            Node<T> parent = x.parent, leftChild = x.left, lr = leftChild.right;
            if (parent != null)
                if (parent.right == x) {
                    parent.right = leftChild;
                } else {
                    parent.left = leftChild;
                }
            leftChild.parent = parent;

            x.parent = leftChild;
            leftChild.right = x;

            x.left = lr;
            if (lr != null)
                lr.parent = x;
            return leftChild;
        }
        return x;
    }
}

public class Main {

    public static void main(String[] args) {
        Node<Integer> n=BTreePrinterTest.test();
        SplayTree<Integer> splayTree=new SplayTree<>(n);
        splayTree.print();
        splayTree.splay(9);
        splayTree.print();
        splayTree.insert(11);
        splayTree.print();
        splayTree.delete(18);
        splayTree.print();
        System.out.println(splayTree.search(9));
        splayTree.print();
    }
}
