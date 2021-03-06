import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

import game.actions.EDirection;
import game.actions.compact.CAction;
import game.actions.compact.CPush;
import game.board.compact.BoardCompact;

class Node implements Comparable<Node> {
	final BoardCompact state;
	final List<Position> boxes;
    private final int h;
	private final int cost;
	private final CAction incomingAction;
	private final Node pred;

	public Node(BoardCompact state, List<Position> boxes, int cost, int estimate) {
		this.state = state;
		this.h = estimate;
		this.incomingAction = null;
		this.boxes = boxes;
		this.cost = cost;
		this.pred = null;
	}

	public Node(BoardCompact state, CAction incomingAction, Node prev, ToIntFunction<Node> fH) {
		this.state = state;
		//this.h = h;
		this.cost = prev.cost + 1;
		this.incomingAction = incomingAction;
		this.pred = prev;

		// if a box is moved, update the list
		if (incomingAction instanceof CPush) {
			List<Position> boxes = new ArrayList<>(prev.boxes.size());
			
			for (Position box : prev.boxes) {
				Position offset = Position.getDisplaced(state.playerX, state.playerY, incomingAction.getDirection(), 1);
				//find the box which is at player position + offset
				if (box.x == offset.x && box.y == offset.y) {
					//we assume that a move is valid and do not do bound checks
					boxes.add(new Position(box.x - offset.x, box.y - offset.y));
				} else {
					boxes.add(box);
				}
			}
			this.boxes = boxes;			
			this.h = fH.applyAsInt(this);
		} else {
			this.boxes = prev.boxes;
			this.h = prev.h; //fine as long as only box position is considered by the heuristic, otherwise recompute every time!!
		}
	}

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