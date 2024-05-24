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
    
    public static int GetCounterIntuitiveMoves(char[][] genes, String LURD){
    
        //System.out.println("GetCounterIntuitiveMoves");
        
        int counterIntuitiveMoves = 0;
        StringBuilder lurdReversed = new StringBuilder(LURD).reverse();
        ArrayList<BoxGoal> boxgoalList = new ArrayList<>();
        
        //Generate win board
        char[][] winBoard = GeneratorUtils.CloneCharArray(genes);
        
        //Set to win state
        for(int i = 0; i < winBoard.length;i++){
            for(int j = 0; j < winBoard[0].length;j++){
                if(winBoard[i][j] == '.' || winBoard[i][j] == '*'){
                    winBoard[i][j] = '*';

                    boxgoalList.add(new BoxGoal(new Pair(i,j), new Pair(i,j)));
                }
                
                else if(winBoard[i][j] == '$')
                    winBoard[i][j] = ' ';
            }
        }
        
        //Set the player for the solution state
        Pair startPlayerPos = GeneratorUtils.FindCharacterPairIndexBased(winBoard, 0, 0);
        winBoard[startPlayerPos.i][startPlayerPos.j] = ' ';
        Pair endPosPlayer = GetPosEndPlayer(genes, LURD, startPlayerPos);
        winBoard[endPosPlayer.i][endPosPlayer.j] = '@';

        //System.out.println("Entro al bucle de doMove");
        //GeneratorUtils.PrintCharArray(winBoard);
        
        boolean nextMoveIsContraintuitive = false;
        //System.out.println("-------------------------------");
        for(int i = 0 ; i < lurdReversed.length();i++){
            //System.out.println("i: " + i + " | Char: " + lurdReversed.charAt(i));
            //GeneratorUtils.PrintCharArray(winBoard);
            nextMoveIsContraintuitive = DoMove(winBoard, boxgoalList, lurdReversed.charAt(i));
            if(nextMoveIsContraintuitive){
                nextMoveIsContraintuitive = false;
                counterIntuitiveMoves++;
            }
        }
        ///System.out.println("-------------------------------");
        
        return counterIntuitiveMoves;
    }
    
    public static boolean DoMove(char[][] genes, ArrayList<BoxGoal> boxgoalList,char charAt) {
        //System.out.println("-----------------------------");
        //System.out.println("DoMove: ");
        //System.out.println("Tablero Antes: ");
        //GeneratorUtils.PrintCharArray(genes);
        //System.out.println("Letra: " + charAt);
        
        //Direction
        int pista = 0;
        boolean boxPosChange = false;
        int oldManhattanDistance = 0;
        boolean isCounterIntuitive = false;
        //char[][] backup = CloneCharArray(genes);
        
        Pair dirPair = null;
        switch(charAt){
            case 'l' : dirPair = new Pair(0,-1); break;
            case 'L' : {dirPair = new Pair(0,-1); boxPosChange = true;}break;
            case 'u' : dirPair = new Pair(-1,0);break;
            case 'U' : {dirPair = new Pair(-1,0); boxPosChange = true;}break;
            case 'r' : dirPair = new Pair(0,1);break;
            case 'R' : {dirPair = new Pair(0,1); boxPosChange = true;}break;
            case 'd' : dirPair = new Pair(1,0);break;
            case 'D' : {dirPair = new Pair(1,0); boxPosChange = true;}break;
        }

        //check if next to player have a box
        Pair playerPos = GeneratorUtils.FindCharacterPairIndexBased(genes, 0, 0);
        Pair backToPlayer = playerPos.minus(dirPair);
        Pair nextToPlayer = playerPos.plus(dirPair);
        
        
        if(boxPosChange){
            
            
            BoxGoal currentBoxGoal = GetBoxGoalList(boxgoalList, nextToPlayer, genes, charAt);
           
            //Update boxgoal pos
            currentBoxGoal.boxPos.Copy(playerPos); 
            //Get old manhattan Distance
            oldManhattanDistance = currentBoxGoal.manhattanDistance;
            //Update manhattan distance
            currentBoxGoal.UpdateManhattanDistance();
            //Check counterintuitive
            if(oldManhattanDistance > currentBoxGoal.manhattanDistance)
                isCounterIntuitive = true;
            
            //Update old
            if(genes[backToPlayer.i][backToPlayer.j] == ' '){
                genes[backToPlayer.i][backToPlayer.j] = '@';
            } 
            
            else if(genes[backToPlayer.i][backToPlayer.j] == '.'){
                genes[backToPlayer.i][backToPlayer.j] = '+';
            } 
            
            //restore old
            if(genes[playerPos.i][playerPos.j] == '@')
                genes[playerPos.i][playerPos.j] = ' ';
            else if(genes[playerPos.i][playerPos.j] == '+')
                genes[playerPos.i][playerPos.j] = '.';
            
            
            //Update center
            if( genes[playerPos.i][playerPos.j] == '.' ){
                genes[playerPos.i][playerPos.j] =  '*';
            }
            
            else if(genes[playerPos.i][playerPos.j] == ' '){
                genes[playerPos.i][playerPos.j] =  '$';
            }
            
            //restore
            if(genes[nextToPlayer.i][nextToPlayer.j] == '*'){
                genes[nextToPlayer.i][nextToPlayer.j] =  '.';
            }
            
            else if(genes[nextToPlayer.i][nextToPlayer.j] == '$'){
                genes[nextToPlayer.i][nextToPlayer.j] =  ' ';
            }
            
            
            /*if(genes[playerPos.i][playerPos.j] == '+'){
                genes[playerPos.i][playerPos.j] = '.';
                if(genes[backToPlayer.i][backToPlayer.j] == ' '){
                    genes[backToPlayer.i][backToPlayer.j] = '@';
                } 
                else if(genes[backToPlayer.i][backToPlayer.j] == '.'){
                    genes[backToPlayer.i][backToPlayer.j] = '+';
                } 
                pista = 1;
            }
            
            else if(genes[playerPos.i][playerPos.j] == '@'){
                
                genes[playerPos.i][playerPos.j] = ' ';

                if(genes[backToPlayer.i][backToPlayer.j] == ' '){
                    genes[backToPlayer.i][backToPlayer.j] = '@';
                } 
                else if(genes[backToPlayer.i][backToPlayer.j] == '.'){
                    genes[backToPlayer.i][backToPlayer.j] = '+';
                } 
                
                pista = 2;
            }
            
            //Update center
            if(genes[playerPos.i][playerPos.j] == '.'){
                genes[playerPos.i][playerPos.j] = '*';
                
                pista = 3;
            }
            
            else if(genes[playerPos.i][playerPos.j] == ' '){
                genes[playerPos.i][playerPos.j] = '$';
                
                pista = 4;
            }
            
            //Update old box pos
            if(genes[nextToPlayer.i][nextToPlayer.j] == '*'){
                genes[nextToPlayer.i][nextToPlayer.j] = '.';
                
                pista = 5;
            }
            
            else if(genes[nextToPlayer.i][nextToPlayer.j] == '$'){
                genes[nextToPlayer.i][nextToPlayer.j] = ' ';
                
                pista = 6;
            }
            
            
            /*if(isCounterIntuitive){
                System.out.println("---------------------------------");
                System.out.println("Counterintuitive Move detected: ");
                System.out.println("Estado sucesor");
                GeneratorUtils.PrintCharArray(backup);
                System.out.println("Estado antesesor");
                GeneratorUtils.PrintCharArray(genes);
                System.out.println("---------------------------------");    
            }*/
        }
            
        
        //El player se mueve sin empujar ninguna caja
        else{
            
            //System.out.println("NO Cambiar caja");
            
            //Update player
            if(genes[playerPos.i][playerPos.j] == '+'){
                genes[playerPos.i][playerPos.j] = '.';
                if(genes[backToPlayer.i][backToPlayer.j] == ' '){
                    genes[backToPlayer.i][backToPlayer.j] = '@';
                    pista = 11;
                } 
                else if(genes[backToPlayer.i][backToPlayer.j] == '.'){
                    genes[backToPlayer.i][backToPlayer.j] = '+';
                    pista = 111;
                } 
               
            }
            
            else if(genes[playerPos.i][playerPos.j] == '@'){
                
                genes[playerPos.i][playerPos.j] = ' ';

                if(genes[backToPlayer.i][backToPlayer.j] == ' '){
                    genes[backToPlayer.i][backToPlayer.j] = '@';
                    pista = 22;
                } 
                else if(genes[backToPlayer.i][backToPlayer.j] == '.'){
                    genes[backToPlayer.i][backToPlayer.j] = '+';
                    pista = 222;
                } 
            }
        }
        
        //int playerCount = GeneratorUtils.CountCharacters(0,genes);
        /*if(playerCount==0){
            GeneratorUtils.PrintCharArray(genes);
            System.out.println("player 0: " + pista);
            System.out.println("error");
        }*/
        
        //System.out.println("Despues");
        //GeneratorUtils.PrintCharArray(genes);
                   
        return isCounterIntuitive;
    }
    
    public static BoxGoal GetBoxGoalList(ArrayList<BoxGoal> boxGoalList, Pair boxPos, char[][] genes, Character charAt){
        
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
    }

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
        
        
        


