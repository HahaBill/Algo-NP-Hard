import static java.lang.System.out;
import java.util.ArrayList;
import java.util.function.IntPredicate;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Iterator;

import agents.ArtificialAgent;
import game.actions.EDirection;
import game.actions.compact.*;
import game.board.compact.BoardCompact;
import game.board.compact.CTile;

/**
 * The simplest Tree-DFS agent.
 * @author Jimmy
 */
public class MyAgent extends ArtificialAgent {
	protected BoardCompact board;
	protected List<Position> goals;
	protected int searchedNodes;
	
	@Override
	protected List<EDirection> think(BoardCompact board) {
		this.board = board;
		searchedNodes = 0;
		long searchStartMillis = System.currentTimeMillis();

		List<EDirection> result = new ArrayList<EDirection>();

		//////// GROUP 42 IMPLEMENTATION ///////////
		this.goals = findOnBoard(board, CTile::forSomeBox);
		search(result);

		////////////////////////////////////////////

		//dfs(5, result); // the number marks how deep we will search (the longest plan we will consider)

		long searchTime = System.currentTimeMillis() - searchStartMillis;
        
        if (verbose) {
            out.println("Nodes visited: " + searchedNodes);
            out.printf("Performance: %.1f nodes/sec\n",
                        ((double)searchedNodes / (double)searchTime * 1000));
        }
		
		return result.isEmpty() ? null : result;
	}
	
	private List<Position> findOnBoard(BoardCompact board, IntPredicate predicate) {
		List<Position> result = new LinkedList<>();
		int[][] tiles = board.tiles;

		for (int i = 0; i < board.width(); i++) {
			for (int j = 0; j < board.height(); j++) {
				if (predicate.test(tiles[i][j])) {
					result.add(new Position(i, j));
				}
			}
		}

		return result;
	}

	//put in the desired heuristic to be used
	private int heuristicFunction(Node curr) {
		return 0;//heuristicManhattan(curr);
	}

	private int heuristicBfs(Node curr) {
		//TODO: from each box execute bfs to find the shortest path to the closest goal 
		//idea: keep frontiers for each box; push forward one step at a time for each box
		//use deadSquareDetection to avoid unnecessary search YE BOI
		Iterator<Position> boxes = curr.boxes.iterator();
		LinkedList<Queue<Position>> frontiers = new LinkedList<>();
		LinkedList<Set<Position>> discovereds = new LinkedList<>();
		for (Position box : curr.boxes) {
			Queue<Position> q = new LinkedList<>();
			Set<Position> s = new HashSet<>();
			q.add(box);
			s.add(box);
			frontiers.add(q);
			discovereds.add(s);
		}
		// !!make iterators for discovereds
		// once a goal is reached for some box remove its frontier
		while (!frontiers.isEmpty()) {
			for (Queue<Position> frontier : frontiers) {
				Position box = boxes.next();

			}
		}
		return 0;
	}

	private int heuristicManhattan(Node curr) {
		int sum_distance = 0;
		
		// for each box find min distance out of all goals and sum them
		for(Position box : curr.boxes) {
			int b_x = box.x;
			int b_y = box.y;
			int min_dist = Integer.MAX_VALUE;
			
			for (Position goal : goals) {
				int g_x = goal.x;
				int g_y = goal.y;
				int temp_dist = manhattan(b_x, b_y, g_x, g_y);
				if(min_dist > temp_dist) { min_dist = temp_dist; }
			}

			sum_distance = sum_distance + min_dist;
		}

		return sum_distance;
	}

	private int manhattan(int source_x, int source_y, int destination_x, int destination_y) {
		return Math.abs(destination_x - source_x) + Math.abs(destination_y - source_y);
	}

  	private boolean search(List<EDirection> result) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		Set<BoardCompact> explored = new HashSet<>();
		BoardCompact init = board.clone();
		
		Node current = new Node(init, findOnBoard(board, CTile::isSomeBox), 0, 0);

		pq.add(current);
		explored.add(init);

		do {
			//if (searchedNodes == 10000) return false;
			searchedNodes++;

			current = pq.remove(); 
			BoardCompact currState = current.state;

			List<CAction> actions = new LinkedList<>();
			
			for (CMove move : CMove.getActions()) {
				if (move.isPossible(currState)) actions.add(move);
			}
			for (CPush push : CPush.getActions()) {
				if (push.isPossible(currState)) actions.add(push);
			}

			for (CAction action : actions) {
				// pruning away dead square moves
				if (action instanceof CPush) {
					Position p = Position.getDisplaced(currState.playerX, currState.playerY, action.getDirection(), 2);
					if (deadSquares[p.x][p.y]) continue;
				}
				BoardCompact nextState = currState.clone();
				action.perform(nextState);
				if (explored.contains(nextState)) continue;
				explored.add(nextState);
				pq.add(new Node(nextState, action, current, this::heuristicFunction));
			}

		} while (!pq.isEmpty() && !current.state.isVictory());

		current.reconstructPath(result);

		return current.state.isVictory();
	}

/*	private boolean dfs(int level, List<EDirection> result) {
		if (level <= 0) return false; // DEPTH-LIMITED
		
		++searchedNodes;
		
		// COLLECT POSSIBLE ACTIONS
		
		List<CAction> actions = new ArrayList<CAction>(4);
		
		for (CMove move : CMove.getActions()) {
			if (move.isPossible(board)) {
				actions.add(move);
			}
		}
		for (CPush push : CPush.getActions()) {
			if (push.isPossible(board)) {
				actions.add(push);
			}
		}
		
		// TRY ACTIONS
		for (CAction action : actions) {
			// PERFORM THE ACTION
			result.add(action.getDirection());
			action.perform(board);
			
			// CHECK VICTORY
			if (board.isVictory()) {
				// SOLUTION FOUND!
				return true;
			}
			
			// CONTINUE THE SEARCH
			if (dfs(level - 1, result)) {
				// SOLUTION FOUND!
				return true;
			}
			
			// REVERSE ACTION
			result.remove(result.size()-1);
			action.reverse(board);
		}
		
		return false;
	}	*/
}