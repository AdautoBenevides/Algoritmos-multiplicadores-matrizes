import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.text.DecimalFormat;

public class TCPServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java TCPServer <ordem>");
            return;
        }

        int order = Integer.parseInt(args[0]); // Obtém a ordem da matriz a partir dos argumentos de linha de comando
        int serverPort = 1234;
        int numWorkers = 4;
        List<Socket> workerSockets = new ArrayList<>();

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            System.out.println("Servidor ouvindo na porta " + serverPort);

            // Aguarda todos os trabalhadores se conectarem
            while (workerSockets.size() < numWorkers) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                workerSockets.add(clientSocket);
            }

            Connection connection = new Connection(workerSockets, numWorkers, order);
            connection.start();

        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    static class Connection extends Thread {
        List<Socket> workerSockets;
        int numWorkers;
        int order;

        public Connection(List<Socket> workerSockets, int numWorkers, int order) {
            this.workerSockets = workerSockets;
            this.numWorkers = numWorkers;
            this.order = order;
        }

        public void run() {
            long startTime = System.nanoTime();

            int partSize = order / numWorkers;

            int[][] a = createMatrix(order);
            int[][] b = createMatrix(order);

            int[][] c = new int[order][order];

            try {

                // Envia partes das matrizes para cada trabalhador
                for (int i = 0; i < numWorkers; i++) {
                    Socket workerSocket = workerSockets.get(i);
                    ObjectOutputStream out = new ObjectOutputStream(workerSocket.getOutputStream());

                    // Divide a matriz A para cada trabalhador
                    int[][] aPart = new int[partSize][order];
                    System.arraycopy(a, i * partSize, aPart, 0, partSize);

                    out.writeObject(aPart);
                    out.writeObject(b);

                    out.flush();
                }

                // Recebe as partes da matriz C dos trabalhadores
                for (int i = 0; i < numWorkers; i++) {
                    Socket workerSocket = workerSockets.get(i); 
                    ObjectInputStream in = new ObjectInputStream(workerSocket.getInputStream());

                    int[][] cPart = (int[][]) in.readObject();
                    int startRow = i * partSize;

                    System.arraycopy(cPart, 0, c, startRow, partSize); 
                    in.close(); 

                    workerSocket.close(); 
                }

                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;

                double seconds = executionTime / 1000000000.0;

                System.out.println("Runtime: " + seconds + " seconds");

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao processar matrizes: " + e.getMessage());
            }
        }

        private static int[][] createMatrix(int order) {
            int[][] matriz = new int[order][order];
            for (int row = 0; row < order; row++) {
                for (int column = 0; column < order; column++) {
                    matriz[row][column] = column + 1;
                }
            }
            return matriz;
        }

        private static void printMatrix(int[][] matrix) {
            for (int[] row : matrix) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }
    }
}
