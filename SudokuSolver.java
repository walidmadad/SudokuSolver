import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SudokuSolver - Programme pour résoudre des grilles de Sudoku avec une interface graphique.
 *
 * Auteur : @walidMadad
 * Date : 01/10/2024
 */
public class SudokuSolver extends JFrame {

    private static final int GRID_SIZE = 9; // Taille de la grille
    private JTextField[][] fields = new JTextField[GRID_SIZE][GRID_SIZE]; // Grille de champs de texte
    private int[][] board = new int[GRID_SIZE][GRID_SIZE]; // Grille initialisée à vide

    /**
     * Constructeur de SudokuSolver pour initialiser l'interface graphique.
     */
    public SudokuSolver() {
        setTitle("Sudoku Solver");  // Titre de la fenêtre
        setSize(600, 600);  // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Action lors de la fermeture
        setLayout(new BorderLayout());  // Utilisation d'un layout BorderLayout

        // Panneau principal pour la grille de Sudoku
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 5)); // Espacement entre les boxes

        // Initialisation des champs de texte de la grille
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                JPanel boxPanel = new JPanel();
                boxPanel.setLayout(new GridLayout(3, 3)); // Chaque box a un GridLayout 3x3

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int globalRow = boxRow * 3 + row;
                        int globalCol = boxCol * 3 + col;

                        fields[globalRow][globalCol] = new JTextField();
                        fields[globalRow][globalCol].setFont(new Font("Monospaced", Font.BOLD, 24));
                        fields[globalRow][globalCol].setHorizontalAlignment(JTextField.CENTER);
                        fields[globalRow][globalCol].setBorder(new LineBorder(Color.BLACK, 1));

                        boxPanel.add(fields[globalRow][globalCol]); // Ajout de chaque champ au panel de box
                    }
                }
                panel.add(boxPanel); // Ajout de la box au panneau principal
            }
        }

        // Bouton pour résoudre le Sudoku
        JButton solveButton = new JButton("Résoudre le Sudoku");
        solveButton.setBackground(Color.LIGHT_GRAY);  // Couleur de fond
        solveButton.setForeground(Color.BLACK);  // Couleur du texte

        // Modification de la taille et du style de police du texte
        solveButton.setFont(new Font("Monospaced", Font.BOLD, 18));

        // Ajout d'une infobulle pour expliquer la fonction du bouton
        solveButton.setToolTipText("Cliquez ici pour résoudre le Sudoku.");

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readBoardFromUser()) {
                    if (solveBoard(board)) {
                        updateBoard();
                        JOptionPane.showMessageDialog(null, "Sudoku résolu !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pas de solution.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer des nombres valides (0 à 9).");
                }
            }
        });

        // Ajout du panneau de grille et du bouton à l'interface
        add(panel, BorderLayout.CENTER);
        add(solveButton, BorderLayout.SOUTH);

        setVisible(true);  // Rendre la fenêtre visible
    }

    /**
     * Méthode pour lire la grille de Sudoku entrée par l'utilisateur.
     *
     * @return true si l'entrée est valide, false sinon
     */
    private boolean readBoardFromUser() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String text = fields[row][col].getText();
                if (text.isEmpty()) {
                    board[row][col] = 0; // Les cases vides sont considérées comme 0
                } else {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < 0 || value > 9) {
                            return false; // Nombre invalide
                        }
                        board[row][col] = value;
                    } catch (NumberFormatException e) {
                        return false; // Entrée non valide
                    }
                }
            }
        }
        return true; // Si toutes les entrées sont valides
    }

    /**
     * Met à jour l'interface graphique avec les nouvelles valeurs de la grille après résolution.
     */
    private void updateBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                fields[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    /**
     * Méthode principale pour démarrer l'application.
     */
    public static void main(String[] args) {
        new SudokuSolver();  // Crée l'interface graphique
    }

    // ------------------------------------- Méthodes du Solveur -------------------------------------

    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(int[][] board, int number, int row, int column) {
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidPlacement(int[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
                !isNumberInColumn(board, number, column) &&
                !isNumberInBox(board, number, row, column);
    }

    private static boolean solveBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (board[row][column] == 0) {
                    for (int number = 1; number <= GRID_SIZE; number++) {
                        if (isValidPlacement(board, number, row, column)) {
                            board[row][column] = number;

                            if (solveBoard(board)) {
                                return true;
                            } else {
                                board[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
