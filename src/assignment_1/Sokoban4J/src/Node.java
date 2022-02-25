import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.actions.EDirection;
import game.actions.compact.CAction;
import game.actions.compact.CPush;
import game.board.compact.BoardCompact;

class Node implements Comparable<Node> {
	final BoardCompact state;
	final List<Position> boxes;
    private final double h;
	private final double cost;
	private final CAction incomingAction;
	private final Node pred;

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