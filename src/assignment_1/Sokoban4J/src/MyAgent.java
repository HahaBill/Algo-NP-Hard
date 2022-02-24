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

/**
 * The simplest Tree-DFS agent.
 * @author Jimmy
 */
public class MyAgent extends ArtificialAgent {
	protected BoardCompact board;
	protected int searchedNodes;
	
	@Override
	protected List<EDirection> think(BoardCompact board) {
		this.board = board;
		searchedNodes = 0;
		long searchStartMillis = System.currentTimeMillis();
		
		List<EDirection> result = new ArrayList<EDirection>();
		search(result);
		//dfs(5, result); // the number marks how deep we will search (the longest plan we will consider)

		long searchTime = System.currentTimeMillis() - searchStartMillis;
        
        if (verbose) {
            out.println("Nodes visited: " + searchedNodes);
            out.printf("Performance: %.1f nodes/sec\n",
                        ((double)searchedNodes / (double)searchTime * 1000));
        }
		
		return result.isEmpty() ? null : result;
	}
	
	private int heuristic_function(BoardCompact state) {
		return 0;
	}

  	private boolean search(List<EDirection> result) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		Set<BoardCompact> explored = new HashSet<>();
		BoardCompact init = board.clone();
		
		Node current = new Node(init, null, null, 0, heuristic_function(init));

		pq.add(current);
		explored.add(init);

		do {
			if (searchedNodes == 20) return false;
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
				pq.add(new Node(nextState, action, current, current.getCost() + 1, heuristic_function(nextState)));
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
	public CAction incomingAction;
	public Node pred;

	public Node(BoardCompact state, CAction action, Node pred, double cost, double estimate) {
		this.state = state;
		this.h = estimate;
		this.incomingAction = action;
		this.cost = cost;
		this.pred = pred;
	}

	public BoardCompact getState() { return state; }

	public double getCost() { return cost; }

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