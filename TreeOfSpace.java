import java.util.*;

public class TreeOfSpace {
    static class Node {
        boolean isLocked;
        int uid;
        Node parent;
        int ans_locked = 0;
        int des_locked = 0;
        String name = "";
        List<Node> children = new ArrayList<>();
    }

    private static boolean lock(Node node, int uid) {
        if (node.isLocked || node.des_locked > 0 || node.ans_locked > 0)
            return false;

        node.isLocked = true;
        node.uid = uid;
        updateChild(node, 1);
        updateParent(node, 1);
        return true;
    }

    private static boolean unlock(Node node, int uid) {
        if (!node.isLocked || node.uid != uid)
            return false;
        node.isLocked = false;
        node.uid = -1;
        updateChild(node, -1);
        updateParent(node, -1);
        return true;
    }

    private static boolean upgrade(Node node, int uid) {
        if (node.isLocked || node.des_locked == 0)
            return false;
        if (isUpgradePossible(node, uid)) {
            lock(node, uid);
            return true;
        }
        return false;
    }

    private static boolean isUpgradePossible(Node node, int uid) {
        if (node == null)
            return true;
        if (node.isLocked && node.uid != uid)
            return false;
        for (Node child : node.children) {
            if (!isUpgradePossible(child, uid)) {
                return false;
            }
        }
        for (Node child : node.children) {
            if (child.isLocked && child.uid == uid) {
                unlock(child, uid);
            }
        }
        return true;
    }

    private static void updateParent(Node node, int val) {
        Node head = node.parent;
        while (head != null) {
            head.des_locked += val;
            head = head.parent;
        }
    }

    private static void updateChild(Node node, int val) {
        if (node == null)
            return;
        for (Node child : node.children) {
            child.ans_locked += val;
            updateChild(child, val);
        }
    }

    // Function to print the tree level-wise
    public static void printTreeLevelWise(Node root) {
        if (root == null)
            return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // Number of nodes at the current level

            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                System.out.print(current.name + " "); // Print the name of the current node
                if (current.parent != null) {
                    System.out.println(current.parent.name);
                }
                for (Node child : current.children) {
                    queue.add(child);
                }
            }

            System.out.println(); // Move to the next line after printing the current level
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        HashMap<String, Node> hm = new HashMap<>();
        int n = sc.nextInt();
        int k = sc.nextInt();
        int nq = sc.nextInt();
        sc.nextLine(); // To handle the newline character after integers
        String nodeAr[] = new String[n];
        for (int i = 0; i < n; i++) {
            nodeAr[i] = sc.nextLine();
        }

        int in = 0;

        Node root = new Node();
        root.name = nodeAr[0];
        hm.put(nodeAr[0], root);

        Queue<Node> q = new LinkedList<>();
        q.add(root);
        in++;

        while (!q.isEmpty() && in < n) {
            Node cur = q.poll();
            for (int i = 0; i < k && in < n; i++) {
                Node newNode = new Node();
                newNode.name = nodeAr[in];
                newNode.parent = cur;
                hm.put(nodeAr[in], newNode);
                cur.children.add(newNode);
                q.add(newNode);
                in++;
            }
        }

        ArrayList<Boolean> ans = new ArrayList<>();

        for (int i = 0; i < nq; i++) {
            int op = sc.nextInt();
            String nodeName = sc.next();
            int uid = sc.nextInt();

            boolean result = false;
            switch (op) {
                case 1:
                    result = lock(hm.get(nodeName), uid);
                    break;
                case 2:
                    result = unlock(hm.get(nodeName), uid);
                    break;
                case 3:
                    result = upgrade(hm.get(nodeName), uid);
                    break;

                default:
                    break;
            }
            ans.add(result);
        }
        System.out.println(ans);
    }
}
