import java.util.Queue;
import java.util.Set;

import java.util.HashSet;
import java.util.LinkedList;

import java.lang.ArrayIndexOutOfBoundsException;

import game.board.compact.BoardCompact;
import game.board.compact.CTile;

class DeadSquareDetector {
    public static boolean[][] detect(BoardCompact board) {
        boolean[][] deadSquares = new boolean[board.width()][board.height()];
        int[][] tiles = board.tiles;
        
        // for each board position
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                int startTile = tiles[x][y];
                // if wall or goal skip; goal not dead!
                if (CTile.isWall(startTile)) { 
                    deadSquares[x][y] = true;
                    continue;
                } else if (CTile.forSomeBox(startTile)) {
                    deadSquares[x][y] = false;
                    continue;
                }
                //otherwise bfs
                Queue<Position> frontier = new LinkedList<>();
                Set<Position> explored = new HashSet<>();
                
                frontier.add(new Position(x, y));
                deadSquares[x][y] = true;

                while (!frontier.isEmpty()) {
                    Position curr = frontier.poll();
                    int currTile  = tiles[curr.x][curr.y];
                    explored.add(curr);
    
                    // if we have found a goal, mark the current tile as alive and continue to the next tile
                    if (CTile.forSomeBox(currTile)) {
                        deadSquares[x][y] = false;
                        break;
                    }
                    
                    // (1) check if there is not box or wall to the left ans right of curr            
                    try {
                        int tileLeft  = tiles[curr.x - 1][curr.y];
                        int tileRight = tiles[curr.x + 1][curr.y];
                        if (CTile.isWalkable(tileLeft) && CTile.isWalkable(tileRight)) {
                            Position p = new Position(curr.x + 1, curr.y);
                            if (!explored.contains(p)) frontier.add(p);
                            p = new Position(curr.x - 1, curr.y);
                            if (!explored.contains(p)) frontier.add(p);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) { continue; }

                    // (2) check if there is not box or wall to the up and down of curr
                    try {
                        int tileUp   = tiles[curr.x][curr.y - 1];
                        int tileDown = tiles[curr.x][curr.y + 1];
                        if (CTile.isWalkable(tileUp) && CTile.isWalkable(tileDown)) {
                            Position p = new Position(curr.x, curr.y - 1);
                            if (!explored.contains(p)) frontier.add(p);
                            p = new Position(curr.x, curr.y + 1);
                            if (!explored.contains(p)) frontier.add(p);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) { continue; }
                }
            }
        }

        return deadSquares;
    }
}