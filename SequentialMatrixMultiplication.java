import java.text.DecimalFormat;

public class SequentialMatrixMultiplication {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int row;
        int column;
        int order = 2;

        if (args.length == 1) {
            order = Integer.parseInt(args[0]);
        } else {
            System.out.println("Wrong number of arguments.");
            System.out.println("Using default values.");
        }

        int[][] a = new int[order][order];
        int[][] b = new int[order][order];
        int[][] c = new int[order][order];

        // Preeche as matrizes a e b
        for (row = 0; row < order; row++) {
            for (column = 0; column < order; column++) {
                a[row][column] = column + 1;
                b[row][column] = column + 1;
            }
            for (column = 0; column < order; column++) {
            }
        }

        // lógica da multiplicação das matrizes
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                c[i][j] = multiplyRowAndColumn(a, b, i, j, order);
            }
        }

        // Calcula o tempo de execução
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        int numberOfDecimalPlaces = 10;

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
