import java.text.DecimalFormat;

public class ConcurrentMatrixMultiplication {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int row = 0;
        int column = 0;
        int order = 2;
        int blockSize = 8;

        if (args.length == 1) {
            order = Integer.parseInt(args[0]);
        } else {
            System.out.println("Wrong number of arguments.");
            System.out.println("Using default values.");
        }

        int[][] a = new int[order][order];
        int[][] b = new int[order][order];
        int[][] c = new int[order][order];

        // preenche as matrizes a e b
        for (row = 0; row < order; row++) {
            for (column = 0; column < order; column++) {
                a[row][column] = column + 1;
                b[row][column] = column + 1;
            }
        }

        Thread[] threads = new Thread[order / blockSize];

        for (int i = 0; i < order / blockSize; i++) {

            final int startRow = i * blockSize;
            final int endRow = startRow + blockSize;
            final int finalOrder = order;

            // criação das threads e lógica da multiplicação
            Thread thread = new Thread(() -> {
                for (int j = startRow; j < endRow; j++) {
                    for (int k = 0; k < finalOrder; k++) {
                        c[j][k] = multiplyRowAndColumn(a, b, j, k, finalOrder);
                    }
                }
            });

            thread.start();
            threads[i] = thread;
        }

        // Espera até que todas as threads tenham concluído
        for (int i = 0; i < order / blockSize; i++) {
            try {
                if (threads[i] != null) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // System.out.println("A");
        // for (int i = 0; i < order; i++) {
        // for (int j = 0; j < order; j++) {
        // System.out.print(a[i][j] + " ");
        // }
        // System.out.println();
        // }
        // System.out.println("A");
        // for (int i = 0; i < order; i++) {
        // for (int j = 0; j < order; j++) {
        // System.out.print(b[i][j] + " ");
        // }
        // System.out.println();
        // }
        // for (int i = 0; i < order; i++) {
        // for (int j = 0; j < order; j++) {
        // System.out.print(c[i][j] + " ");
        // }
        // System.out.println();
        // }

        // Calcula o tempo de execução
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        int numberOfDecimalPlaces = 4;

        // Formata e exibe o tempo de execução
        String pattern = "0." + new String(new char[numberOfDecimalPlaces]).replace('\0', '0');
        DecimalFormat df = new DecimalFormat(pattern);

        double seconds = executionTime / 1000.0;
        String formattedSeconds = df.format(seconds);

        System.out.println("Runtime: " + formattedSeconds + " seconds");

    }

    // função que realiza a multiplicação das matrizes
    public static int multiplyRowAndColumn(int[][] a, int[][] b, int row, int col, int order) {
        int result = 0;
        for (int i = 0; i < order; i++) {
            result += a[row][i] * b[i][col];
        }
        return result;
    }
}
