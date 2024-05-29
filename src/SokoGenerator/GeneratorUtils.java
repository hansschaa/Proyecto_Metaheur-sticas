/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator;

import Metaheuristics.GA.GAProblem;
import Metaheuristics.Metaheuristics;
import SokoGenerator.Tree.Pair;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Hans
 */
public class GeneratorUtils {
    
    private static final ArrayList<Character> playerChars = new ArrayList<>(Arrays.asList('@', '+'));
    private static final ArrayList<Character> boxChars = new ArrayList<>(Arrays.asList('$', '*'));
    private static final ArrayList<Character> goalChars = new ArrayList<>(Arrays.asList('*', '.', '+'));

    private static int length = 0;

    public static String ConvertCharArrayToString(char[][] charArray) {
        StringBuilder sb = new StringBuilder();

        for (char[] row : charArray) {
            for (char c : row) {
                sb.append(c);
            }
            sb.append('\n');
        }

        return sb.toString();
    }
    
    public static void PrintCharArray(char[][] charArray) {
        for (char[] row : charArray) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
    
    public static char[][] CloneCharArray(char[][] originalArray) {
        Metaheuristics.COPY++;
        
        length = originalArray.length;
        char[][] clonedArray  = new char[length][originalArray[0].length];

        for (int i = 0; i < length; i++) {
            clonedArray[i] = originalArray[i].clone();
        }
        
        //System.out.println("a");
        //PrintCharArray(clonedArray);
        
        return clonedArray;
        
        // Copiar el array bidimensional
        //return Arrays.copyOf(originalArray, originalArray.length);
        
        /*GAProblem.COPY++;

        int length = originalArray.length;
        char[][] clonedArray = new char[length][];
        int rowLength = originalArray[0].length;
        
        for (int i = 0; i < length; i++) {
            clonedArray[i] = new char[rowLength];
            System.arraycopy(originalArray[i], 0, clonedArray[i], 0, rowLength);
        }

        return clonedArray;*/
    }


    public static Pair GetEmptySpacePair(char[][] board) {

        Pair pair = new Pair(0,0);
        do{
            pair.i = Metaheuristics.random.nextInt( board.length );
            pair.j = Metaheuristics.random.nextInt( board[0].length );
       
        }while(board[pair.i][pair.j] != ' ');
        return pair;
    }
    
    public static Pair FindCharacterPairIndexBased(char[][] board, int characterID, int specificCount) {
        int rows = board.length;
        int columns = board[0].length;
        
        Pair pair = new Pair(0,0);
        int currentCount = 0;
        
        ArrayList<Character> chars = null;
        switch(characterID){
            case 0:
                chars = playerChars;
                break;
            case 1:
                chars = boxChars;
                break;
            case 2:
                chars = goalChars;
                break;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = board[i][j];
                if (chars.contains(currentChar)) {
                    if(currentCount == specificCount){
                        pair.i = i;
                        pair.j = j;
                        return pair;
                    }
                    
                    currentCount++;
                }
            }
        }

        // El carácter no ha sido encontrado
        GeneratorUtils.PrintCharArray(board);
        System.out.println("Carácter " + characterID + " de specific id: " + specificCount+  " no encontrado en la matriz.");
        return null;
    }
    
    //ID 0: Player, ID 1: Boxes, ID 2: Goals
    public static int CountCharacters(int characterID, char[][] board) {
        int rows = board.length;
        int columns = board[0].length;
        int count = 0;
        
        ArrayList<Character> chars = null;
        switch(characterID){
            case 0 :
                chars = playerChars;
                break;
            case 1 :
                chars = boxChars;
                break;
            case 2: chars = goalChars;
                break;
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = board[i][j];
                if (chars.contains(currentChar)) {
                    count++;
                }
            }
        }

        return count; // Carácter no encontrado
    }
    
    public static int GetBoardManhattanDistance(char[][] genes){
    
        int manhattanDistance = 0;
        int cont = 0;
        int maxBox = GeneratorUtils.CountCharacters(1, genes);
        
        Pair boxPair, goalPair;
        
        while(cont < maxBox){
        
            //Get cont box and goal pair
            boxPair = GeneratorUtils.FindCharacterPairIndexBased(genes, 1, cont);
            goalPair = GeneratorUtils.FindCharacterPairIndexBased(genes, 2, cont);
            
            manhattanDistance += GetManhattamDistance(boxPair, goalPair);
            
            cont++;
        }
     
        return manhattanDistance;
    }
    
    public static int GetManhattamDistance(Pair boxPair, Pair goalPair){
        return Math.abs(boxPair.i - goalPair.i) + Math.abs(boxPair.j - goalPair.j);
    }
    
    
    
    
    
    /*public static BoxGoal GetBoxGoalList(ArrayList<BoxGoal> boxGoalList, Pair boxPos, char[][] genes, Character charAt){
        
        for(BoxGoal boxGoal : boxGoalList){
            if(boxGoal.boxPos.i == boxPos.i && boxGoal.boxPos.j == boxPos.j){
                return boxGoal;
            }
        }
        
        
        System.out.println("El boxPos que entro es: " + boxPos.toString());
        for(BoxGoal boxGoal : boxGoalList){
            boxGoal.Print();
        }
        System.out.println("Tablero: ");
        GeneratorUtils.PrintCharArray(genes);
        System.out.println("Letra: " + charAt);
        System.out.println("No se encontró el boxgoal de la lista");
        return null;
    }*/

    private static Pair GetPosEndPlayer(char[][] genes, String LURD, Pair playerPos) {
        
        LURD = LURD.toLowerCase();
        for(int i = 0; i < LURD.length();i++){
            
            char charAt = LURD.charAt(i);
            Pair dir = null;
            
            switch(charAt){
            case 'l' : dir = new Pair(0,-1); break;
            case 'u' : dir = new Pair(-1,0); break;
            case 'r' : dir = new Pair(0,1); break;
            case 'd' : dir = new Pair(1,0); break;
            }
            
            playerPos  = playerPos.plus(dir);
        
        }
        
        return playerPos;
    }

    public static Pair RemoveRandomElementByType(int elementID, int maxID, Pair excludePair, char[][] genes) {
        
        Pair pair = null;
        int randomID;
        do{
            randomID = Metaheuristics.random.nextInt(maxID);
            pair = GeneratorUtils.FindCharacterPairIndexBased(genes, elementID, randomID);
        }while(pair.i == excludePair.i && pair.j == excludePair.j);
        
        return pair;
    }

    static String Encode(Pair boxPair, Pair goalPair, Pair playerPair) {
        
        String encode = Integer.toString(boxPair.i)+Integer.toString(boxPair.j)+
                        Integer.toString(goalPair.i)+Integer.toString(goalPair.j)+
                        Integer.toString(playerPair.i)+Integer.toString(playerPair.j);
        
        return encode;
    }


}
        
        
        


