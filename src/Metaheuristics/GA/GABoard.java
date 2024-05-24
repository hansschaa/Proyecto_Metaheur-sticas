package Metaheuristics.GA;

import Metaheuristics.Metaheuristics;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.Pair;
import org.moeaframework.core.Variable;
import java.util.Arrays;

public class GABoard implements Variable {
    private char[][] board;
    
    public GABoard(char[][] board) {
        this.board = board;
    }

    public char[][] GetBoard() {
        return board;
    }

    public void SetBoard(char[][] newBoard) {
        this.board = newBoard;
    }

    
    @Override
    public Variable copy() {
        
        char[][] cloneBoard = new char[board.length][board[0].length];
        
        for (int i = 0; i < board.length; i++) {
            cloneBoard[i] = Arrays.copyOf(this.board[i], board[0].length);
        }
        
        GABoard copy = new GABoard(cloneBoard);
        
        return copy;
        
        //return new GABoard(GeneratorUtils.CloneCharArray(board));
    }
    
    @Override
    public String encode() {
        System.out.println("Encode");
        return "";
    }

    @Override
    public void decode(String string) {
        System.out.println("Decode");
    }

    @Override
    public void randomize() {
        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }

    int Initialize() {
        //System.out.println("--->Initialize");

        char[][] newBoard = null;
        de.sokoban_online.jsoko.leveldata.solutions.Solution jsokoSolution = null;
        
        Pair pair;
        do{
            //System.out.println("->Probar");
            newBoard = GeneratorUtils.CloneCharArray(board);

            pair = GeneratorUtils.GetEmptySpacePair(newBoard);
            newBoard[pair.i][pair.j] = '$';

            pair = GeneratorUtils.GetEmptySpacePair(newBoard);
            newBoard[pair.i][pair.j] = '.';

            pair = GeneratorUtils.GetEmptySpacePair(newBoard);
            newBoard[pair.i][pair.j] = '@';

            //GeneratorUtils.PrintCharArray(newBoard);
            jsokoSolution = Metaheuristics.Solve(newBoard, false, 1); 
            
        }while(jsokoSolution== null);

        board = GeneratorUtils.CloneCharArray(newBoard);
        
        newBoard = null;
        jsokoSolution = null;
        
        return Metaheuristics.application.movesHistory.getPushesCount();
        
    }
}
