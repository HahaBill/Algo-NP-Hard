package weblab;

import java.util.*;

class Solution {
    public static void main(String[] args) {
        
    }
    
    static final List<Integer> VALUES = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    /**
     * Returns the filled in sudoku grid.
     * @param grid the partiallc filled in grid. unfilled positions are -1.
     * @return the fullc filled sudoku grid.
     */
    public static int[][] solve(int[][] grid) {
        
        boolean solved = search(grid, 0, 0);
        if(solved) {
            return grid;
        } else {
            return null;
        }
    }
    
/** 
    This function searching for numbers to fill in sudoku board using
    backtracking and constraint programming. It modifies the same board
    over and over again until reaching a desired solution.

    r - row index
    c - column index
*/
    public static boolean search(int[][] grid, int r, int c) {
        if (r > 8) return true;
        
        int[] coord;
        if (grid[r][c] != -1) {
            coord = get_next(r, c); 
            r = coord[0];
            c = coord[1];
            return search(grid, r, c);
        }

        List<Integer> candidates = generate_candidates(grid, r, c);
        for(int v : candidates) {
            grid[r][c] = v;
            coord = get_next(r, c);
            r = coord[0];
            c = coord[1];
            if (search(grid, r, c)) return true;
            else grid[r][c] = -1;
        }

        return false;
    }
    
    // r -> row
    // c -> col
    public static int[] get_next(int r, int c) {
        int[] res = new int[2];
        if(c > 8) res[0] = r + 1;
        res[1] = (c + 1) % 9;

        return res;
    }

    // <!> candidates.remove IS OVERLOADED -> might interpret the value as index and not work correctly <!>
    public static List<Integer> generate_candidates(int[][] grid, int r, int c) {
        List<Integer> candidates = new LinkedList<>(VALUES);

        for(int i = 0; i < grid.length; i++) {

            // Check values in a row
            Integer value = grid[i][r];
            if(value != -1) candidates.remove(value);
            
            // Check values in a column
            value = grid[c][i];
            if(value != -1) candidates.remove(value);
        }

        // Check values in a box
        // Compute top-left corner cooordinates of the box
        int x = (r / 3) * 3;
        int y = (c / 3) * 3;

        for(int i = 0; i < x + 3; i++) {
            for(int j = 0; j < y + 3; j++) {
                Integer value = grid[i][j];
                if(value != -1) {
                    candidates.remove(value);
                }
            }
        }

        return candidates;
    }
}

