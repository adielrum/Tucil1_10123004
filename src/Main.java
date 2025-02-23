import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static int N, M, P;
    static boolean found = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for the input file name
        System.out.print("Enter the input file name (e.g., input_1.txt): ");
        String inputFileName = scanner.nextLine();

        // Construct the full path to the input file
        String inputFilePath = new File("").getAbsolutePath() + "/Tucil1_10123004/test/" + inputFileName;

        // Read input from the file
        try (Scanner fileScanner = new Scanner(new File(inputFilePath))) {
            // Read board dimensions and number of pieces
            N = fileScanner.nextInt();
            M = fileScanner.nextInt();
            P = fileScanner.nextInt();
            fileScanner.nextLine(); // Consume the rest of the line

            // Read the puzzle type
            String type = fileScanner.nextLine();

            // Initialize the board
            char[][] matrix = new char[N][M];

            // If the puzzle type is CUSTOM, read the board
            if (type.equals("CUSTOM")) {
                for (int i = 0; i < N; i++) {
                    String line = fileScanner.nextLine().trim();
                    for (int j = 0; j < M; j++) {
                        // Replace 'X' with '*' and keep other characters as is
                        matrix[i][j] = (line.charAt(j) == 'X') ? '*' : line.charAt(j);
                    }
                }
            } else {
                // For DEFAULT, initialize the board with '*'
                for (char[] row : matrix) {
                    Arrays.fill(row, '*');
                }
            }

            // Read all pieces
            List<List<String>> piecesStrings = new ArrayList<>();
            String currentPiece = null;
            List<String> currentList = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue; // Skip empty lines

                // Check if the line starts a new piece
                if (currentPiece == null || line.charAt(0) != currentPiece.charAt(0)) {
                    currentPiece = line;
                    currentList = new ArrayList<>();
                    piecesStrings.add(currentList);
                }
                currentList.add(line);
            }

            // Convert string representation to character matrices
            List<List<List<Character>>> pieces = new ArrayList<>();
            for (List<String> pieceStr : piecesStrings) {
                if (!pieceStr.isEmpty()) {
                    pieces.add(stringify(pieceStr));
                }
            }

            // Start solving
            bruteforce(matrix, pieces, 0);

            if (!found) {
                System.out.println("No solution found!");
            }

            // Ask if the user wants to save the output to a file
            System.out.print("Do you want to save the output to a file? (yes/no): ");
            String saveOption = scanner.nextLine().trim().toLowerCase();

            if (saveOption.equals("yes")) {
                System.out.print("Enter the output file name (e.g., output_1.txt): ");
                String outputFileName = scanner.nextLine();

                // Construct the full path to the output file
                String outputFilePath = "test/" + outputFileName;

                // Save the output to the file
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
                    if (found) {
                        writer.println("Solution found:");
                        for (int i = 0; i < N; i++) {
                            for (int j = 0; j < M; j++) {
                                writer.print(matrix[i][j] + " ");
                            }
                            writer.println();
                        }
                    } else {
                        writer.println("No solution found!");
                    }
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

    // Convert a piece represented as strings to a character matrix
    public static List<List<Character>> stringify(List<String> S) {
        List<List<Character>> result = new ArrayList<>();
        if (S.isEmpty()) return result;

        // Find the maximum length of strings in S
        int maxLen = 0;
        for (String s : S) {
            maxLen = Math.max(maxLen, s.length());
        }

        // Process each string in S
        for (String s : S) {
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                // Replace spaces with '*'
                if (c == ' ') {
                    row.add('*');
                } else {
                    row.add(c);
                }
            }
            // Pad with '*' to ensure rectangular shape
            while (row.size() < maxLen) {
                row.add('*');
            }
            result.add(row);
        }

        return result;
    }

    // Check if a piece can be placed at position (y, x)
    public static boolean cekFit(char[][] matrix, List<List<Character>> piece, int y, int x) {
        int pieceHeight = piece.size();
        if (pieceHeight == 0) return true;
        int pieceWidth = piece.get(0).size();

        // Check if the piece goes out of bounds
        if (y + pieceHeight > N || x + pieceWidth > M) {
            return false;
        }

        // Check for conflicts with existing pieces on the board
        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (piece.get(i).get(j) != '*' && matrix[y + i][x + j] != '*') {
                    return false;
                }
            }
        }
        return true;
    }

    // Place a piece on the board
    public static char[][] place(char[][] original, List<List<Character>> piece, int y, int x) {
        // Create a deep copy of the original matrix
        char[][] newMatrix = new char[N][M];
        for (int i = 0; i < N; i++) {
            System.arraycopy(original[i], 0, newMatrix[i], 0, M);
        }

        // Place the piece on the new matrix
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

    // Rotate a piece
    public static List<List<Character>> rotate(List<List<Character>> piece, String rot) {
        int height = piece.size();
        if (height == 0) return piece;
        int width = piece.get(0).size();

        List<List<Character>> rotated;

        switch (rot) {
            case "0": // No rotation
                return piece;

            case "90": // Rotate 90° clockwise
                rotated = new ArrayList<>();
                for (int j = 0; j < width; j++) {
                    List<Character> newRow = new ArrayList<>();
                    for (int i = height - 1; i >= 0; i--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    rotated.add(newRow);
                }
                return rotated;

            case "180": // Rotate 180°
                rotated = new ArrayList<>();
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    rotated.add(newRow);
                }
                return rotated;

            case "270": // Rotate 270° clockwise (or 90° counterclockwise)
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
                return piece; // Invalid rotation, return original piece
        }
    }

    // Flip a piece
    public static List<List<Character>> flip(List<List<Character>> piece, String flipType) {
        int height = piece.size();
        if (height == 0) return piece;
        int width = piece.get(0).size();

        List<List<Character>> flipped = new ArrayList<>();

        switch (flipType) {
            case "no": // No flip
                return piece;

            case "horizontal": // Flip horizontally
                for (int i = 0; i < height; i++) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    flipped.add(newRow);
                }
                return flipped;

            case "vertical": // Flip vertically
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>(piece.get(i));
                    flipped.add(newRow);
                }
                return flipped;

            case "both": // Flip both horizontally and vertically
                for (int i = height - 1; i >= 0; i--) {
                    List<Character> newRow = new ArrayList<>();
                    for (int j = width - 1; j >= 0; j--) {
                        newRow.add(piece.get(i).get(j));
                    }
                    flipped.add(newRow);
                }
                return flipped;

            default:
                return piece; // Invalid flip type, return original piece
        }
    }

    // Check if the board is completely filled
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
	
	// Display the piece
	public static void displayPiece(List<List<Character>> piece) {
    for (List<Character> row : piece) {
        for (Character c : row) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}
    // Display the board
    public static void display(char[][] matrix) {
        // Define colors for each piece identifier (A-Z)
        Map<Character, String> colorMap = new HashMap<>();
        colorMap.put('A', "\u001B[31m");  // Red
        colorMap.put('B', "\u001B[32m");  // Green
        colorMap.put('C', "\u001B[33m");  // Yellow
        colorMap.put('D', "\u001B[34m");  // Blue
        colorMap.put('E', "\u001B[35m");  // Magenta
        colorMap.put('F', "\u001B[36m");  // Cyan
        colorMap.put('G', "\u001B[37m");  // White
        colorMap.put('H', "\u001B[91m");  // Bright Red
        colorMap.put('I', "\u001B[92m");  // Bright Green
        colorMap.put('J', "\u001B[93m");  // Bright Yellow
        colorMap.put('K', "\u001B[94m");  // Bright Blue
        colorMap.put('L', "\u001B[95m");  // Bright Magenta
        colorMap.put('M', "\u001B[96m");  // Bright Cyan
        colorMap.put('N', "\u001B[97m");  // Bright White
        colorMap.put('O', "\u001B[41m");  // Background Red
        colorMap.put('P', "\u001B[42m");  // Background Green
        colorMap.put('Q', "\u001B[43m");  // Background Yellow
        colorMap.put('R', "\u001B[44m");  // Background Blue
        colorMap.put('S', "\u001B[45m");  // Background Magenta
        colorMap.put('T', "\u001B[46m");  // Background Cyan
        colorMap.put('U', "\u001B[47m");  // Background White
        colorMap.put('V', "\u001B[101m"); // Bright Background Red
        colorMap.put('W', "\u001B[102m"); // Bright Background Green
        colorMap.put('X', "\u001B[103m"); // Bright Background Yellow
        colorMap.put('Y', "\u001B[104m"); // Bright Background Blue
        colorMap.put('Z', "\u001B[105m"); // Bright Background Magenta
    
        // Reset color after each character
        String resetColor = "\u001B[0m";
    
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                char c = matrix[i][j];
                if (c == '*') {
                    System.out.print(" "); // Print '*' without color
                } else {
                    // Get the color for the current piece identifier
                    String color = colorMap.getOrDefault(c, resetColor);
                    System.out.print(color + c + resetColor);
                }
            }
            System.out.println();
        }
    }

    // Main recursive solving function
    public static void bruteforce(char[][] matrix, List<List<List<Character>>> pieces, int idx) {
        // If a solution has already been found, stop
        if (found) return;

        // If all pieces have been placed
        if (idx >= pieces.size()) {
            // Check if the board is completely filled
            if (isBoardComplete(matrix)) {
                display(matrix);
                System.out.println();
                found = true;
            }
            return;
        }

        // Define possible rotations and flips
        String[] rotations = {"0", "90", "180", "270"};
        String[] flips = {"no", "horizontal", "vertical", "both"};

        // Try all combinations of rotations and flips
        for (String rot : rotations) {
            for (String flipType : flips) {
                // Skip redundant transformations for square pieces
                if (pieces.get(idx).size() == pieces.get(idx).get(0).size()) { // Square piece
                    if (rot.equals("180") && flipType.equals("both")) continue;
                    if (rot.equals("90") && flipType.equals("horizontal")) continue;
                    if (rot.equals("270") && flipType.equals("vertical")) continue;
                }

                // Transform the piece
                List<List<Character>> transformedPiece = rotate(pieces.get(idx), rot);
                transformedPiece = flip(transformedPiece, flipType);

                // Try placing the transformed piece at every possible position on the board
                for (int y = 0; y < N; y++) {
                    for (int x = 0; x < M; x++) {
                        if (cekFit(matrix, transformedPiece, y, x)) {
                            // Place the piece on the board
                            char[][] newMatrix = place(matrix, transformedPiece, y, x);
                            // Recursively try to place the next piece
                            bruteforce(newMatrix, pieces, idx + 1);
                            // If a solution is found, stop
                            if (found) return;
                        }
                    }
                }
            }
        }
    }
}