/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import Metaheuristics.GA.GABoard;
import Metaheuristics.GA.GAProblem;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.CrossPair;
import SokoGenerator.Tree.Pair;
import java.util.ArrayList;
import java.util.Random;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;


/**
 *
 * @author Hans
 */
public class BoardCrossover implements Variation {
    private final double crossoverRate;
    private final Random random = new Random();
    de.sokoban_online.jsoko.leveldata.solutions.Solution solution;
    private Pair pivot;
    private Pair pivotAux;
    private final Pair right;
    private final Pair down;
    
    ArrayList<CrossPair> interestingPivots;
    ArrayList<Character> regionChars;
   
            
    public BoardCrossover(double crossoverRate) {
        System.out.println("GABoardCrossover");
        this.crossoverRate = crossoverRate;
        
        pivot = new Pair(0,0);
        pivotAux = new Pair(0,0);
        right = new Pair(0,1);
        down = new Pair(1,0);
        
        interestingPivots = new ArrayList<>();
        regionChars = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "GABoardCrossover";
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Solution[] evolve(Solution[] parents) {

        if (random.nextFloat() > crossoverRate) return parents;
      
        Metaheuristics.R_TOTAL_CROSSOVER++;
        
        //Parent 1
        GABoard parent1 = (GABoard) parents[0].getVariable(0);
        GABoard parent2 = (GABoard) parents[1].getVariable(0);
        //offspring1 = (GABoard) parent1.copy();  // Define offspring1 as a copy of parent1
        char[][] cloneBoard_1 = GeneratorUtils.CloneCharArray(parent1.GetBoard());
        
        //Get candidates
        GetInterestingPivots(parent2.GetBoard());
        
        
        //Crossover
        if(interestingPivots.isEmpty()){
            System.out.println("-> interestingPivotsList de largo 0");
        }
            
        else{
            //Select a random region
            var randomInterestingPivot = interestingPivots.get(Metaheuristics.random.nextInt(interestingPivots.size()));

            //Put random region in clone
            /*System.out.println("ANTES DE PONER LA REGION");
            GeneratorUtils.PrintCharArray(cloneBoard_1);*/
            for(int i = 0 ; i < Metaheuristics.P_CROSS_SPACING ; i++){
                var charClone =  parent2.GetBoard()
                        [randomInterestingPivot.pair.i][randomInterestingPivot.pair.j];
                /*System.out.println("PONER EL CHAR: " + charClone);
                System.out.println("En: " +randomInterestingPivot);*/
                
                cloneBoard_1[randomInterestingPivot.pair.i][randomInterestingPivot.pair.j] = charClone;

                randomInterestingPivot.pair = randomInterestingPivot.pair.plus(randomInterestingPivot.dir);
            }
            
            //Check if clone is legal
            boolean isLegal = IsLegal(cloneBoard_1, randomInterestingPivot);
            if(isLegal){
                int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard_1);
                
                solution = Metaheuristics.Solve(cloneBoard_1, false, boxCount);
                if(solution != null){
                    Metaheuristics.R_TOTAL_EFFECTIVE_CROSSOVER++;
                    
                    GABoard offspring1 = new GABoard(cloneBoard_1);
                    
                    Solution solution1 = new Solution(1, 1); // 1 variable, 2 objetivos (ejemplo)
                    solution1.setVariable(0, offspring1);
                    solution1.setObjective(0, Metaheuristics.application.movesHistory.getPushesCount());
                    cloneBoard_1=null;
                    return new Solution[]{solution1};  
                }
            }
            else{
                //repair illegal
                RepairIllegal(cloneBoard_1);
                
                //Retry
                int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard_1);
                solution = Metaheuristics.Solve(cloneBoard_1, false, boxCount);
                if(solution != null){
                    Metaheuristics.R_TOTAL_EFFECTIVE_REPAIR++;
                    Metaheuristics.R_TOTAL_EFFECTIVE_CROSSOVER++;
                    GABoard offspring1 = new GABoard(cloneBoard_1);
                    
                    Solution solution1 = new Solution(1, 1); // 1 variable, 2 objetivos (ejemplo)
                    solution1.setVariable(0, offspring1);
                    solution1.setObjective(0, Metaheuristics.application.movesHistory.getPushesCount());

                    cloneBoard_1=null;

                    return new Solution[]{solution1};  
                }
            }
        }

        cloneBoard_1=null;
        return new Solution[]{parents[0]};   
    }
    
    public void RepairIllegal(char[][] cloneBoard){
        
        Metaheuristics.R_TOTAL_REPAIR++;
        
        //Check illegality
        int playerCount = GeneratorUtils.CountCharacters(0, cloneBoard);
        int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        int goalCount = GeneratorUtils.CountCharacters(2, cloneBoard);
        
        //For player
        if(playerCount > 1){
            int specificPlayerCount = Metaheuristics.random.nextInt(2);
            Pair playerIndex = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, 0, specificPlayerCount);
            if(cloneBoard[playerIndex.i][playerIndex.j] == '+')
                cloneBoard[playerIndex.i][playerIndex.j] = '.';
            else
                cloneBoard[playerIndex.i][playerIndex.j] = ' ';
        }
        else if (playerCount == 0){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '@';
        }
        
        //For boxes
        if(boxCount > goalCount){
            int diff = boxCount - goalCount;
            
            while(diff != 0){
                
                Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
                cloneBoard[emptySpace.i][emptySpace.j] = '.';
                goalCount++;
                diff--;
            }
        }
        
        else if(boxCount == 0 ){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '$';
            boxCount++;
        }
        
        //For goals
        if(goalCount > boxCount){
            int diff = goalCount - boxCount;
            
            while(diff != 0){
                
                Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
                cloneBoard[emptySpace.i][emptySpace.j] = '$';
                boxCount++;
                diff--;
            }
        }
        
        else if(goalCount == 0 ){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '.';
            goalCount++;
        }
        
        //System.out.println("Despúes");
        //GeneratorUtils.PrintCharArray(cloneBoard);
        //System.out.println("----------------------");
    }
    
    
    
    public void GetInterestingPivots(char[][] otherGenes){
                
        interestingPivots.clear();
        regionChars.clear();
        
        for(int i = 0 ; i < otherGenes.length ; i++){
            for(int j = 0 ; j < otherGenes[0].length ; j++){
                
                pivot = new Pair(i,j);
                if(otherGenes[pivot.i][pivot.j] == '#') continue;
                
                pivotAux.i = pivot.i;
                pivotAux.j = pivot.j;

                regionChars.clear();
                //Check horizontal
                for(int k = 0 ; k < Metaheuristics.P_CROSS_SPACING; k++){
                   
                    if(pivotAux.j > otherGenes[0].length - Metaheuristics.P_CROSS_SPACING || otherGenes[pivotAux.i][pivotAux.j] == '#') {
                        break;
                    }
                    
                    regionChars.add(otherGenes[pivotAux.i][pivotAux.j]);
                    pivotAux = pivotAux.plus(right);

                }
                
                //Try add
                if(regionChars.size() == Metaheuristics.P_CROSS_SPACING){
                    if((regionChars.contains('@') || regionChars.contains('+') || regionChars.contains('$') || 
                           regionChars.contains('.') || regionChars.contains('*')) && !regionChars.contains('#')){
                        interestingPivots.add(new CrossPair( new Pair(pivot.i, pivot.j), new Pair(0,1)));
                    }
                }
                
                pivotAux.i = pivot.i;
                pivotAux.j = pivot.j;
                regionChars.clear();
                //Check vertical
                for(int k = 0 ; k < Metaheuristics.P_CROSS_SPACING; k++){
                    
                    if(pivotAux.i > otherGenes.length - Metaheuristics.P_CROSS_SPACING || otherGenes[pivotAux.i][pivotAux.j] == '#'){
                        break;
                    } 
                    
                    regionChars.add(otherGenes[pivotAux.i][pivotAux.j]);
                    pivotAux = pivotAux.plus(down);
                }
                
                  //Try add
                if(regionChars.size() == Metaheuristics.P_CROSS_SPACING){
                    if(regionChars.contains('@') || regionChars.contains('+') || regionChars.contains('$') || 
                           regionChars.contains('.') || regionChars.contains('*')){
                    interestingPivots.add(new CrossPair( new Pair(pivot.i, pivot.j), new Pair(1,0)));
                    }
                }
            }
        }
        
        
    }
    
    public boolean IsLegal(char[][] board, CrossPair newCrossPair){
        
        int playerCount =  GeneratorUtils.CountCharacters(0, board);
        int boxCount = GeneratorUtils.CountCharacters(1, board);
        int goalCount = GeneratorUtils.CountCharacters(2, board);
      
        if(boxCount > Metaheuristics.P_MAX_BOXES){
            
            Pair boxToRemove = GeneratorUtils.RemoveRandomElementByType(1,boxCount,newCrossPair.pair, board);
            Pair goalToRemove = GeneratorUtils.RemoveRandomElementByType(2,goalCount,newCrossPair.pair, board);
            
            //Replace box
            if(board[boxToRemove.i][boxToRemove.j] == '$')
                board[boxToRemove.i][boxToRemove.j] = ' ';
            else if(board[boxToRemove.i][boxToRemove.j] == '*')
                board[boxToRemove.i][boxToRemove.j] = '.';
            
            //Replace goal
            switch (board[goalToRemove.i][goalToRemove.j]) {
                case '.':
                    board[goalToRemove.i][goalToRemove.j] = ' ';
                    break;
                case '*':
                    board[goalToRemove.i][goalToRemove.j] = '$';
                    break;
                case '+':
                    board[goalToRemove.i][goalToRemove.j] = '@';
                    break;
                default:
                    break;
            }
        }
        
        if(playerCount != 1)
            return false;
        
        if(boxCount == 0 || goalCount == 0)
            return false;
        
        return boxCount == goalCount;
    }
}
