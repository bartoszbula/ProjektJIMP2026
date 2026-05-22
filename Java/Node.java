import java.util.ArrayList;
import java.util.List;

class Node {
    public int id;
    public double x, y;
    public List<Integer> conn;

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.conn = new ArrayList<>();
    }
}
