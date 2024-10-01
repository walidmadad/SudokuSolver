/**
 * SudokuSolver - Programme pour résoudre des grilles de Sudoku en utilisant une approche de backtracking.
 *
 * Ce solveur prend une grille de Sudoku incomplète et la résout en remplissant les cases vides de manière
 * récursive. Le solveur vérifie si chaque placement de nombre est valide en considérant les règles du Sudoku.
 *
 * Auteur : @walidmadad
 * Date : 01/10/2024
 */
public class SudokuSolver {

    // Taille standard de la grille de Sudoku (9x9)
    private static final int GRID_SIZE = 9;

    /**
     * Point d'entrée principal du programme. Il initialise la grille de Sudoku et appelle
     * la fonction de résolution.
     *
     * @param args Les arguments de ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        // Définition d'une grille de Sudoku incomplète
        int[][] board = {
                {0, 2, 0, 6, 0, 8, 0, 0, 0},
                {5, 8, 0, 0, 0, 9, 7, 0, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {3, 7, 0, 0, 0, 0, 5, 0, 0},
                {6, 0, 0, 0, 0, 0, 0, 0, 4},
                {0, 0, 8, 0, 0, 0, 0, 1, 3},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 9, 8, 0, 0, 0, 3, 6},
                {0, 0, 0, 3, 0, 6, 0, 9, 0}
        };

        // Appel de la fonction pour résoudre le Sudoku
        if (solveBoard(board)) {
            // Si la solution est trouvée, afficher la grille
            printBoard(board);
        } else {
            // Si aucune solution n'existe, afficher un message d'erreur
            System.out.println("No solution exists.");
        }
    }

    /**
     * Affiche la grille de Sudoku.
     *
     * @param board La grille de Sudoku à afficher (tableau 2D de 9x9).
     */
    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                System.out.print(board[row][column] + " ");  // Affichage de chaque élément avec un espace
            }
            System.out.println();  // Nouvelle ligne après chaque rangée
        }
    }

    /**
     * Vérifie si un nombre existe déjà dans une rangée spécifique de la grille.
     *
     * @param board La grille de Sudoku (tableau 2D de 9x9).
     * @param number Le nombre à vérifier.
     * @param row L'indice de la rangée à vérifier.
     * @return true si le nombre existe dans la rangée, sinon false.
     */
    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;  // Le nombre existe déjà dans la rangée
            }
        }
        return false;  // Le nombre n'existe pas dans la rangée
    }

    /**
     * Vérifie si un nombre existe déjà dans une colonne spécifique de la grille.
     *
     * @param board La grille de Sudoku (tableau 2D de 9x9).
     * @param number Le nombre à vérifier.
     * @param column L'indice de la colonne à vérifier.
     * @return true si le nombre existe dans la colonne, sinon false.
     */
    private static boolean isNumberInColumn(int[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;  // Le nombre existe déjà dans la colonne
            }
        }
        return false;  // Le nombre n'existe pas dans la colonne
    }

    /**
     * Vérifie si un nombre existe déjà dans une boîte 3x3 spécifique de la grille.
     *
     * @param board La grille de Sudoku (tableau 2D de 9x9).
     * @param number Le nombre à vérifier.
     * @param row L'indice de la rangée.
     * @param column L'indice de la colonne.
     * @return true si le nombre existe dans la boîte 3x3, sinon false.
     */
    private static boolean isNumberInBox(int[][] board, int number, int row, int column) {
        // Calcul des indices de la boîte 3x3
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        // Parcourt chaque case de la boîte 3x3
        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++) {
                if (board[i][j] == number) {
                    return true;  // Le nombre existe déjà dans la boîte
                }
            }
        }
        return false;  // Le nombre n'existe pas dans la boîte
    }

    /**
     * Vérifie si un placement de nombre est valide, c'est-à-dire s'il respecte
     * les règles du Sudoku dans la rangée, la colonne et la boîte 3x3.
     *
     * @param board La grille de Sudoku (tableau 2D de 9x9).
     * @param number Le nombre à placer.
     * @param row L'indice de la rangée où placer le nombre.
     * @param column L'indice de la colonne où placer le nombre.
     * @return true si le placement est valide, sinon false.
     */
    private static boolean isValidPlacement(int[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
                !isNumberInColumn(board, number, column) &&
                !isNumberInBox(board, number, row, column);  // Renvoie true si le placement est valide
    }

    /**
     * Résout la grille de Sudoku en utilisant la récursion et le backtracking.
     * Parcourt chaque case de la grille et essaie d'insérer des nombres valides.
     *
     * @param board La grille de Sudoku à résoudre (tableau 2D de 9x9).
     * @return true si la grille est résolue, sinon false.
     */
    private static boolean solveBoard(int[][] board) {
        // Parcourt chaque case de la grille
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                // Vérifie si la case est vide (contient 0)
                if (board[row][column] == 0) {
                    // Essaye d'insérer des nombres de 1 à 9 dans la case vide
                    for (int number = 1; number <= GRID_SIZE; number++) {
                        if (isValidPlacement(board, number, row, column)) {
                            board[row][column] = number;  // Insère le nombre si le placement est valide

                            // Appelle récursivement solveBoard pour continuer à résoudre
                            if (solveBoard(board)) {
                                return true;  // Si la solution est trouvée, retourne true
                            } else {
                                board[row][column] = 0;  // Sinon, annule (backtracking) et réinitialise la case
                            }
                        }
                    }
                    return false;  // Retourne false si aucun nombre valide ne peut être placé
                }
            }
        }
        return true;  // Retourne true si la grille est entièrement remplie et résolue
    }
}
