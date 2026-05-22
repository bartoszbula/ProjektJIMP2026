import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
 
public class GraphPanel extends JPanel {
 
    private Graph graph;
 
    private static final int NODE_RADIUS = 20;
    private static final Color NODE_COLOR = new Color(70, 130, 180);
    private static final Color NODE_BORDER_COLOR = new Color(30, 80, 140);
    private static final Color NODE_TEXT_COLOR = Color.WHITE;
    private static final Color EDGE_COLOR = new Color(100, 100, 100);
    private static final Color EDGE_WEIGHT_COLOR = new Color(180, 60, 60);
    private static final Font NODE_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font WEIGHT_FONT = new Font("Arial", Font.PLAIN, 11);
 
    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(new Color(245, 245, 245));
    }
 
    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        if (graph == null || graph.nodes == null || graph.nodes.isEmpty()) {
            drawEmpty(g);
            return;
        }
 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        // Skaluj współrzędne do rozmiaru panelu
        double[] scale = computeScale();
        double scaleX = scale[0];
        double scaleY = scale[1];
        double offsetX = scale[2];
        double offsetY = scale[3];
 
        drawEdges(g2d, scaleX, scaleY, offsetX, offsetY);
        drawNodes(g2d, scaleX, scaleY, offsetX, offsetY);
    }
 
    // Rysuje krawędzie i etykiety wag
    private void drawEdges(Graphics2D g2d, double scaleX, double scaleY, double offsetX, double offsetY) {
        g2d.setColor(EDGE_COLOR);
        g2d.setStroke(new BasicStroke(1.8f));
        g2d.setFont(WEIGHT_FONT);
 
        for (Map.Entry<Integer, Node> entryA : graph.nodes.entrySet()) {
            Node a = entryA.getValue();
            int ax = toScreenX(a.x, scaleX, offsetX);
            int ay = toScreenY(a.y, scaleY, offsetY);
 
            for (int connId : a.conn) {
                Node b = graph.nodes.get(connId);
                if (b == null) continue;
 
                // Rysuj tylko raz każdą krawędź (a.id < b.id)
                if (a.id >= b.id) continue;
 
                int bx = toScreenX(b.x, scaleX, offsetX);
                int by = toScreenY(b.y, scaleY, offsetY);
 
                // Linia krawędzi
                g2d.setColor(EDGE_COLOR);
                g2d.drawLine(ax, ay, bx, by);
 
                // Etykieta wagi - szukamy wagi tej krawędzi
                double weight = getWeight(a.id, b.id);
                if (weight >= 0) {
                    String label = String.format("%.2f", weight);
                    int mx = (ax + bx) / 2;
                    int my = (ay + by) / 2;
 
                    // Białe tło pod etykietą
                    FontMetrics fm = g2d.getFontMetrics(WEIGHT_FONT);
                    int tw = fm.stringWidth(label);
                    int th = fm.getAscent();
                    g2d.setColor(new Color(245, 245, 245, 200));
                    g2d.fillRect(mx - tw / 2 - 2, my - th, tw + 4, th + 4);
 
                    g2d.setColor(EDGE_WEIGHT_COLOR);
                    g2d.drawString(label, mx - tw / 2, my);
                }
            }
        }
    }
 
    // Rysuje wierzchołki jako kółka z numerem
    private void drawNodes(Graphics2D g2d, double scaleX, double scaleY, double offsetX, double offsetY) {
        for (Map.Entry<Integer, Node> entry : graph.nodes.entrySet()) {
            Node node = entry.getValue();
            int cx = toScreenX(node.x, scaleX, offsetX);
            int cy = toScreenY(node.y, scaleY, offsetY);
 
            // Cień
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillOval(cx - NODE_RADIUS + 2, cy - NODE_RADIUS + 2,
                         NODE_RADIUS * 2, NODE_RADIUS * 2);
 
            // Wypełnienie kółka
            g2d.setColor(NODE_COLOR);
            g2d.fillOval(cx - NODE_RADIUS, cy - NODE_RADIUS,
                         NODE_RADIUS * 2, NODE_RADIUS * 2);
 
            // Obramowanie kółka
            g2d.setColor(NODE_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawOval(cx - NODE_RADIUS, cy - NODE_RADIUS,
                         NODE_RADIUS * 2, NODE_RADIUS * 2);
 
            // Numer wierzchołka
            g2d.setColor(NODE_TEXT_COLOR);
            g2d.setFont(NODE_FONT);
            FontMetrics fm = g2d.getFontMetrics(NODE_FONT);
            String label = String.valueOf(node.id);
            int tw = fm.stringWidth(label);
            int th = fm.getAscent();
            g2d.drawString(label, cx - tw / 2, cy + th / 2 - 1);
        }
    }
 
    // Wyświetl komunikat gdy brak grafu
    private void drawEmpty(Graphics g) {
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        String msg = "Brak grafu do wyświetlenia";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(msg)) / 2;
        int y = getHeight() / 2;
        g.drawString(msg, x, y);
    }
 
    // Przelicz współrzędną X grafu na piksele ekranu
    private int toScreenX(double x, double scaleX, double offsetX) {
        return (int) (x * scaleX + offsetX);
    }
 
    // Przelicz współrzędną Y grafu na piksele ekranu
    private int toScreenY(double y, double scaleY, double offsetY) {
        // odwróć Y bo w Javie oś Y idzie w dół
        return (int) (getHeight() - (y * scaleY + offsetY));
    }
 
    // Oblicz skalowanie i offsety żeby graf wypełnił panel z marginesem
    private double[] computeScale() {
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
 
        for (Node n : graph.nodes.values()) {
            if (n.x < minX) minX = n.x;
            if (n.x > maxX) maxX = n.x;
            if (n.y < minY) minY = n.y;
            if (n.y > maxY) maxY = n.y;
        }
 
        int margin = 60;
        int w = getWidth()  - 2 * margin;
        int h = getHeight() - 2 * margin;
 
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
 
        // Zabezpieczenie gdy wszystkie punkty w jednym miejscu
        if (rangeX == 0) rangeX = 1;
        if (rangeY == 0) rangeY = 1;
 
        double scaleX = w / rangeX;
        double scaleY = h / rangeY;
 
        // Użyj mniejszej skali żeby zachować proporcje
        double scale = Math.min(scaleX, scaleY);
 
        double offsetX = margin - minX * scale + (w - rangeX * scale) / 2.0;
        double offsetY = margin - minY * scale + (h - rangeY * scale) / 2.0;
 
        return new double[]{scale, scale, offsetX, offsetY};
    }
 
    // Znajdź wagę krawędzi między wierzchołkami a i b
    private double getWeight(int aid, int bid) {
        if (graph.edges == null) return -1;
        for (double[] edge : graph.edges) {
            // edge format: {aid, bid, weight}
            if ((edge[0] == aid && edge[1] == bid) ||
                (edge[0] == bid && edge[1] == aid)) {
                return edge[2];
            }
        }
        return -1;
    }
}
