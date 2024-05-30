/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import Metaheuristics.GA.GABoard;
import Metaheuristics.GA.GAProblem;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.Pair;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;

/**
 *
 * @author Hans
 */
public class BoardMutation implements Variation{

    private final double mutationRate;
    Pair selectedPair;
    Pair emptySpace;
    public BoardMutation(double mutationRate) {
        //System.out.println("BoardMutation");
        this.mutationRate = mutationRate;
    }
    
    @Override
    public String getName() {
        return "GABoardMutation";
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public Solution[] evolve(Solution[] parents) {
        //System.out.println("Mutation");

        if (Metaheuristics.random.nextFloat()> Metaheuristics.P_MUTATION_PROB) return parents;
        //System.out.println("Mutation");
        Metaheuristics.R_TOTAL_MUTATION++;
        
        //Clone current board state
        GABoard parent1 = (GABoard) parents[0].getVariable(0);
        GABoard offspring1 = (GABoard) parent1.copy();  // Define offspring1 as a copy of parent1
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(offspring1.GetBoard());
        
        //Player : 0 , box: 1 , goal: 2
        var randomElementIndex = Metaheuristics.random.nextInt(3);
        int max = GeneratorUtils.CountCharacters(randomElementIndex, cloneBoard);
        selectedPair = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, randomElementIndex,
                Metaheuristics.random.nextInt(max));
        
        //Get a empty space
        emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
        
        //Replace
        if(randomElementIndex == 0){
            if(cloneBoard[selectedPair.i][selectedPair.j] == '+')
                cloneBoard[selectedPair.i][selectedPair.j] = '.';
            else
                cloneBoard[selectedPair.i][selectedPair.j] = ' ';
                
            cloneBoard[emptySpace.i][emptySpace.j] = '@';
        }
        
        
        else if(randomElementIndex == 1){
            if(cloneBoard[selectedPair.i][selectedPair.j] == '*')
                cloneBoard[selectedPair.i][selectedPair.j] = '.';
            else
                cloneBoard[selectedPair.i][selectedPair.j] = ' ';
                
            cloneBoard[emptySpace.i][emptySpace.j] = '$';
        }
        
        else if(randomElementIndex == 2){
            if(cloneBoard[selectedPair.i][selectedPair.j] == '*')
                cloneBoard[selectedPair.i][selectedPair.j] = '$';
            else if(cloneBoard[selectedPair.i][selectedPair.j] == '+')
                cloneBoard[selectedPair.i][selectedPair.j] = '@';
            else
                cloneBoard[selectedPair.i][selectedPair.j] = ' ';
                
            cloneBoard[emptySpace.i][emptySpace.j] = '.';
        }
        
        
        int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        if(Metaheuristics.Solve(cloneBoard, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            offspring1.SetBoard(cloneBoard);
            
            Solution solution1 = new Solution(1, 1); // 1 variable, 2 objetivos (ejemplo)
            solution1.setVariable(0, offspring1);

            
            cloneBoard=null;
            return new Solution[]{solution1};  
        }

        cloneBoard=null;
        return parents;
    }    
}
