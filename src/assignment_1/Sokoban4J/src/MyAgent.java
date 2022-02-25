import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
		this.goals = findingGoals(board);
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

	private List<Position> findingGoals(BoardCompact board) {
		int[][] tiles = board.tiles;
		int w = board.width();
		int h = board.height();

		List<Position> goals = new LinkedList<>();
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				int tile_type = tiles[i][j];
				if(CTile.forSomeBox(tile_type)) {
					goals.add(new Position(i, j));
				}
			}
		}
		
		return goals;
	}
	
	private List<Position> findBoxes(BoardCompact board) {
		List<Position> result = new LinkedList<>();
		int[][] tiles = board.tiles;
		int w = board.width();
		int h = board.height();

		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				int tile_type = tiles[i][j];
				if(CTile.isSomeBox(tile_type)) {
					result.add(new Position(i, j));
				}
			}
		}
		
		return result;
	}
	
	//put in the desired heuristic to be used
	private int heuristic_function(Node curr) {
		return heuristic_manhattan(curr);
	}

	private int heuristic_manhattan(Node curr) {
		int sum_distance = 0;
		
		// for each box find min distance out of all goals and sum them all
		for(Position box : curr.getBoxes()) {
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
		
		Node current = new Node(init, findBoxes(board), 0, 0);

		pq.add(current);
		explored.add(init);

		do {
			//if (searchedNodes == 10000) return false;
			searchedNodes++;

			current = pq.remove(); 
			BoardCompact currState = current.getState();

			List<CAction> actions = new LinkedList<>();
			
			for (CMove move : CMove.getActions()) {
				if (move.isPossible(currState)) actions.add(move);
			}
			for (CPush push : CPush.getActions()) {
				if (push.isPossible(currState)) actions.add(push);
			}
			
			for (CAction action : actions) {
				BoardCompact nextState = currState.clone();
				action.perform(nextState);
				if (explored.contains(nextState)) continue;
				explored.add(nextState);
				pq.add(new Node(nextState, action, current, heuristic_function(current)));
			}

		} while (!pq.isEmpty() && !current.getState().isVictory());

		current.reconstructPath(result);

		return current.getState().isVictory();
	}

	private boolean dfs(int level, List<EDirection> result) {
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
	}
}

class Node implements Comparable<Node> {
	private BoardCompact state;
	private double h;
	private double cost;
	private final List<Position> boxes;
	public CAction incomingAction;
	public Node pred;

	public Node(BoardCompact state, List<Position> boxes, double cost, double estimate) {
		this.state = state;
		this.h = estimate;
		this.incomingAction = null;
		this.boxes = boxes;
		this.cost = cost;
		this.pred = null;
	}

	public Node(BoardCompact state, CAction incomingAction, Node prev, double h) {
		this.state = state;
		this.h = h;
		this.cost = prev.cost + 1;
		this.incomingAction = incomingAction;
		this.pred = prev;

		// if a box is moved, update the list
		if (incomingAction instanceof CPush) {
			List<Position> boxes = new ArrayList<>(prev.boxes.size());
			int offsetX, offsetY;
			switch (incomingAction.getDirection()) {
				case UP:
					offsetX = 0;
					offsetY = -1;
					break;
				case DOWN:
					offsetX = 0;
					offsetY = 1;
					break;
				case LEFT:
					offsetX = -1;
					offsetY = 0;
					break;
				case RIGHT:
					offsetX = 1;
					offsetY = 0;
					break;
				default:
					offsetX = 0;
					offsetY = 0;
			}
			for (Position box : prev.boxes) {
				//find the box which is at player position + offset
				if (box.x == state.playerX + offsetX && box.y == state.playerY + offsetY) {
					//we assume that a move is valid and do not do bound checks
					boxes.add(new Position(box.x - offsetX, box.y - offsetY));
				} else {
					boxes.add(box);
				}
			}
			this.boxes = boxes;
		} else {
			this.boxes = prev.boxes;
		}
	} 

	public BoardCompact getState() { return state; }

	public double getCost() { return cost; }

	public final List<Position> getBoxes() { return boxes; }

	public void reconstructPath(List<EDirection> actions) {
		Node curr = this;
		while (curr != null && curr.incomingAction != null) {
			actions.add(curr.incomingAction.getDirection());
			curr = curr.pred;
		}
		Collections.reverse(actions);
	}

	@Override
	public int compareTo(Node other) {
		return Double.compare(this.h + this.cost, other.h + other.cost);
	}
}

class Position {
	int x;
	int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(Position other) {
		this.x += other.x;
		this.y += other.y;
	}
}