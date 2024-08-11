import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
    	if (args.length != 2) {
            System.out.println("Uso: java TCPClient <ip_servidor> <ServerPort>");
            return;
        }
        Socket s = null;
        
        String serverIp = args[0];
        String serverPort = args[1];
        
        try {

            
            s = new Socket(serverIp, Integer.parseInt(serverPort));
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

            int[][] aPart = (int[][]) in.readObject();
            int[][] b = (int[][]) in.readObject();

            int order = b.length;
            int partSize = aPart.length;

            // Calcula o número de threads com base no tamanho da matriz
            int numThreads = calculateNumThreads(order);

            // Divide o trabalho em partes para cada nó
            int partPerThread = partSize / numThreads;
            final int[][] cPart = new int[partSize][order];

            // Cria threads para processar as partes da matriz
            Thread[] threads = new Thread[numThreads];
            
            for (int i = 0; i < threads.length; i++) {

                final int threadStartRow = i * partPerThread;
                final int threadEndRow = (i + 1) * partPerThread;
                final int index = i;
                threads[i] = new Thread(() -> {
                    
                    for (int row = threadStartRow; row < threadEndRow; row++) {
                        for (int col = 0; col < order; col++) {
                            cPart[row][col] = multiplyRowAndColumn(aPart, b, row, col, order);
                        }
                    }

                });
                threads[i].start();
            }

            // Espera pela conclusão das threads
            for (Thread thread : threads) {
                thread.join();
            }

            // Envia a parte da matriz "C" de volta ao servidor
            out.writeObject(cPart);
            out.flush();

            in.close();
            out.close();
            s.close();

        } catch (ClassNotFoundException e) {
            System.out.println("Classe não encontrada: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro de IO: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Thread interrompida: " + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar o socket: " + e.getMessage());
                }
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private static int multiplyRowAndColumn(int[][] a, int[][] b, int row, int col, int order) {
        int result = 0;
        for (int i = 0; i < order; i++) {
            result += a[row][i] * b[i][col];
        }
        return result;
    }

    public static int calculateNumThreads(int order) {

        int baseThreads = 2;
        if (order < 64) {
            baseThreads = 1;
        }

        // Calcula a proporção de tamanho em relação à matriz 64x64
        int proporcao = order / 64;

        // A quantidade de threads baseadas na proporção: dobra a cada aumento de 64
        int totalThreads = baseThreads;

        while (proporcao > 1) {
            totalThreads *= 2; // Dobra as threads a cada iteração
            proporcao /= 2; // Reduz pela metade a cada iteração
        }

        return totalThreads; // Retorna o número total de threads calculadas
    }
}
