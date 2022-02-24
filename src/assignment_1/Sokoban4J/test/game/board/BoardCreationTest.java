package game.board;

import java.util.List;

import game.board.compact.BoardCompact;
import game.board.compressed.BoardCompressed;
import game.board.oop.Board;
import game.board.slim.BoardSlim;

public class BoardCreationTest {

	public void test() {
		List<Board> boards = SokobanBoards.loadBoards();
		
		Board board = boards.get(1);
		BoardCompact compact = board.makeBoardCompact();
		BoardSlim slim = compact.makeBoardSlim();
		BoardCompressed compressed = compact.makeBoardCompressed();
		
		System.out.println("--- BOARD OOP ---");
		board.debugPrint();
		
		System.out.println();
		System.out.println("--- BOARD COMPACT ---");
		compact.debugPrint();
		
		System.out.println();
		System.out.println("--- BOARD SLIM ---");
		slim.debugPrint();
		
		System.out.println();
		System.out.println("--- BOARD COMPRESSED ---");
		compressed.debugPrint();
		
		
		System.out.println("--// TEST END //--");
	}
	
	public static void main(String[] args) {
		BoardCreationTest test = new BoardCreationTest();
		test.test();
	} 
	
}
