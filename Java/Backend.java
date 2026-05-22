import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Backend {
    public Graph importGraph(String filename) {
        Graph graph = new Graph();
        String fileNode = "wynik.txt";
        String fileEdge = "edge.txt";

        boolean success = runC(fileNode, fileEdge, graph);

        if (success) {
            readNodes(fileNode, graph);
            readEdges(fileEdge, graph);
            cleanup(fileNode, fileEdge);
        }
        return graph;

    }

    private boolean runC(String fileNode, String fileEdge, Graph graph) {
        try {
            ProcessBuilder builder = new ProcessBuilder("ProjektJIMP2026/graf.exe", "0", fileNode, fileEdge);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int ExitC = process.waitFor();
            if(ExitC != 0) {
                graph.error = output.toString().trim();
                return false;
            }

            return true;
        }
        catch (IOException | InterruptedException e) {
            graph.error = "Blad komunikacji z C: " + e.getMessage();
            return false;
        }
    }

    private void readNodes(String nodes_path, Graph graph)
    {
        File file = new File(nodes_path);
        try (Scanner scanner = new Scanner(file)) {
            scanner.useLocale(Locale.US);

            while(scanner.hasNext())
            {
                int id = scanner.nextInt();
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                graph.nodes.put(id, new Node(id, x, y));
            }
        }
        catch(Exception error){
            graph.error = "Nie udalo sie wczytac wierzcholkow";
        }
    }
    private void readEdges(String edges_path, Graph graph)
    {
        File file = new File(edges_path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" \\s+");
                int Aid = Integer.parseInt(parts[0]);
                int Bid = Integer.parseInt(parts[1]);
                graph.nodes.get(Aid).conn.add(Bid);
                graph.nodes.get(Bid).conn.add(Aid);
            }
        }
        catch (Exception error){
            graph.error = "Nie udalo sie wczytac krawedzi";
        }
    }

    private void cleanup(String fileNode, String fileEdge) {
        new File(fileNode).delete();
        new File(fileEdge).delete();
    }
}
