import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static int N, M, P;
    static boolean found = false;
    static int iterationCount = 0;
    static List<List<Character>> solution = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input file name (e.g., input_1.txt): ");
        String inputFileName = scanner.nextLine();

        String inputFilePath = new File("").getAbsolutePath() + "/Tucil1_10123004/test/" + inputFileName;

        try (Scanner fileScanner = new Scanner(new File(inputFilePath))) {
            N = fileScanner.nextInt();
            M = fileScanner.nextInt();
            P = fileScanner.nextInt();
            fileScanner.nextLine();

            String type = fileScanner.nextLine();

            char[][] matrix = new char[N][M];

            if (type.equals("CUSTOM")) {
                for (int i = 0; i < N; i++) {
                    String line = fileScanner.nextLine().trim();
                    for (int j = 0; j < M; j++) {
                        matrix[i][j] = (line.charAt(j) == 'X') ? '*' : '.';
                    }
                }
            } else {
                for (char[] row : matrix) {
                    Arrays.fill(row, '*');
                }
            }

            List<List<String>> piecesStrings = new ArrayList<>();
            String currentPiece = null;
            List<String> currentList = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isEmpty()) continue;
            
                int firstNonWhitespaceIndex = 0;
                while (firstNonWhitespaceIndex < line.length() && Character.isWhitespace(line.charAt(firstNonWhitespaceIndex))) {
                    firstNonWhitespaceIndex++;
                }
            
                if (firstNonWhitespaceIndex >= line.length()) continue;
            
                char firstChar = line.charAt(firstNonWhitespaceIndex);
            
                

                if (currentPiece == null || firstChar != currentPiece.charAt(0)) {
                    currentPiece = line;
                    currentList = new ArrayList<>();
                    piecesStrings.add(currentList);
                }
                currentList.add(line);
            }

            List<List<List<Character>>> pieces = new ArrayList<>();
            for (List<String> pieceStr : piecesStrings) {
                if (!pieceStr.isEmpty()) {
                    // displayPiece(stringify(pieceStr));
                    pieces.add(stringify(pieceStr));
                }
            }
            long startTime = System.currentTimeMillis(); // Waktu awal eksekusi
            bruteforce(matrix, pieces, 0);

            if (!found) {
                System.out.println("No solution found!");
            }

            long endTime = System.currentTimeMillis(); // Waktu akhir eksekusi
            long executionTime = endTime - startTime; // Waktu eksekusi dalam milidetik

            // Menampilkan jumlah iterasi dan waktu eksekusi
            System.out.println("Total iterations: " + iterationCount);
            System.out.println("Execution time: " + executionTime + " ms");

            System.out.print("Do you want to save the output to a file? (yes/no): ");
            String saveOption = scanner.nextLine().trim().toLowerCase();

            if (saveOption.equals("yes")) {
                System.out.print("Enter the output file name (e.g., output_1.txt): ");
                String outputFileName = scanner.nextLine();

                String outputFilePath = new File("").getAbsolutePath() + "/Tucil1_10123004/test/" + outputFileName;

                try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
                    if (found) {
                        writer.println("Solution found:");
                        for (int i = 0; i < N; i++) {
                            for (int j = 0; j < M; j++) {
                                writer.print(solution.get(i).get(j) + " ");
                            }
                            writer.println();
                        }
                    } else {
                        writer.println("No solution found!");
                    }
                    writer.println("Total iterations: " + iterationCount);
                    writer.println("Execution time: " + executionTime + " ms");
                    System.out.println("Output saved to " + outputFilePath);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        scanner.close();
    }

    public static List<List<Character>> stringify(List<String> S) {
        List<List<Character>> result = new ArrayList<>();
        if (S.isEmpty()) return result;

        int maxLen = 0;
        for (String s : S) {
            maxLen = Math.max(maxLen, s.length());
        }

        for (String s : S) {
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == ' ') {
                    row.add('*');
                } else {
                    row.add(c);
                }
            }
            while (row.size() < maxLen) {
                row.add('*');
            }
            result.add(row);
        }

        return result;
    }

    public static boolean cekFit(char[][] matrix, List<List<Character>> piece, int y, int x) {
        int pieceHeight = piece.size();
        if (pieceHeight == 0) return true;
        int pieceWidth = piece.get(0).size();

        if (y + pieceHeight > N || x + pieceWidth > M) {
            return false;
        }

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (piece.get(i).get(j) != '*' && matrix[y + i][x + j] != '*') {
                    return false;
                }
            }
        }
        return true;
    }

    public static char[][] place(char[][] original, List<List<Character>> piece, int y, int x) {
        char[][] newMatrix = new char[N][M];
        for (int i = 0; i < N; i++) {
            System.arraycopy(original[i], 0, newMatrix[i], 0, M);
        }

        int pieceHeight = piece.size();
        if (pieceHeight == 0) return newMatrix;
        int pieceWidth = piece.get(0).size();

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (piece.get(i).get(j) != '*') {
                    newMatrix[y + i][x + j] = piece.get(i).get(j);
                }
            }
        }
        return newMatrix;
    }

    public static List<List<Character>> rotate(List<List<Character>> piece, String rot) {
        int height = piece.size();
        if (height == 0) return piece;
        int width = piece.get(0).size();

        List<List<Character>> rotated;

        switch (rot) {
            case "0":
                return piece;

            case "90":
                rotated = new ArrayList<>();
                for (int j = 0; j < width; j++) {
                    List<Character> newRow = new ArrayList<>();
                    for (int i = height - 1; i >= 0; i--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    rotated.add(newRow);
                }
                return rotated;

            case "180":
                rotated = new ArrayList<>();
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    rotated.add(newRow);
                }
                return rotated;

            case "270":
                rotated = new ArrayList<>();
                for (int j = width - 1; j >= 0; j--) {
                    List<Character> newRow = new ArrayList<>();
                    for (int i = 0; i < height; i++) {
                        newRow.add(piece.get(i).get(j));
                    }
                    rotated.add(newRow);
                }
                return rotated;

            default:
                return piece;
        }
    }

    public static List<List<Character>> flip(List<List<Character>> piece, String flipType) {
        int height = piece.size();
        if (height == 0) return piece;
        int width = piece.get(0).size();

        List<List<Character>> flipped = new ArrayList<>();

        switch (flipType) {
            case "no":
                return piece;

            case "horizontal":
                for (int i = 0; i < height; i++) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    flipped.add(newRow);
                }
                return flipped;

            case "vertical":
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>(piece.get(i));
                    flipped.add(newRow);
                }
                return flipped;

            case "both":
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    flipped.add(newRow);
                }
                return flipped;

            default:
                return piece;
        }
    }

    public static boolean isBoardComplete(char[][] matrix) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (matrix[i][j] == '*') {
                    return false;
                }
            }
        }
        return true;
    }
	
    public static void displayPiece(List<List<Character>> piece) {
        for (List<Character> row : piece) {
            for (Character c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void display(char[][] matrix) {
        Map<Character, String> colorMap = new HashMap<>();
        colorMap.put('A', "\u001B[31m");
        colorMap.put('B', "\u001B[32m");
        colorMap.put('C', "\u001B[33m");
        colorMap.put('D', "\u001B[34m");
        colorMap.put('E', "\u001B[35m");
        colorMap.put('F', "\u001B[36m");
        colorMap.put('G', "\u001B[37m");
        colorMap.put('H', "\u001B[91m");
        colorMap.put('I', "\u001B[92m");
        colorMap.put('J', "\u001B[93m");
        colorMap.put('K', "\u001B[94m");
        colorMap.put('L', "\u001B[95m");
        colorMap.put('M', "\u001B[96m");
        colorMap.put('N', "\u001B[97m");
        colorMap.put('O', "\u001B[41m");
        colorMap.put('P', "\u001B[42m");
        colorMap.put('Q', "\u001B[43m");
        colorMap.put('R', "\u001B[44m");
        colorMap.put('S', "\u001B[45m");
        colorMap.put('T', "\u001B[46m");
        colorMap.put('U', "\u001B[47m");
        colorMap.put('V', "\u001B[101m");
        colorMap.put('W', "\u001B[102m");
        colorMap.put('X', "\u001B[103m");
        colorMap.put('Y', "\u001B[104m");
        colorMap.put('Z', "\u001B[105m");
    
        String resetColor = "\u001B[0m";
    
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                char c = matrix[i][j];
                if (c == '.') {
                    System.out.print(" ");
                } else {
                    String color = colorMap.getOrDefault(c, resetColor);
                    System.out.print(color + c + resetColor);
                }
            }
            System.out.println();
        }
    }

    public static void bruteforce(char[][] matrix, List<List<List<Character>>> pieces, int idx) {
        if (found){
            return;
        }

        if (idx >= pieces.size()) {
            if (isBoardComplete(matrix)) {
                for (int i = 0; i < N; i++) {
                    List<Character> row = new ArrayList<>();
                    for (int j = 0; j < M; j++) {
                        row.add(matrix[i][j]);
                    }
                    solution.add(row);
                }
                display(matrix);
                System.out.println();
                found = true;
            }
            return;
        }

        String[] rotations = {"0", "90", "180", "270"};
        String[] flips = {"no", "horizontal", "vertical", "both"};

        for (String rot : rotations) {
            for (String flipType : flips) {
                List<List<Character>> transformedPiece = rotate(pieces.get(idx), rot);
                transformedPiece = flip(transformedPiece, flipType);

                if (pieces.get(idx).size() == pieces.get(idx).get(0).size()) { // Square piece
                    if (rot.equals("180") && flipType.equals("both")) continue;
                    if (rot.equals("90") && flipType.equals("horizontal")) continue;
                    if (rot.equals("270") && flipType.equals("vertical")) continue;
                }

                for (int y = 0; y < N; y++) {
                    for (int x = 0; x < M; x++) {
                        
                        if (cekFit(matrix, transformedPiece, y, x)) {
                            iterationCount++; // Menghitung setiap iterasi
                            char[][] newMatrix = place(matrix, transformedPiece, y, x);
                            bruteforce(newMatrix, pieces, idx + 1);
                            if (found) return;
                        }
                    }
                }
            }
        }
        return;
    }
}