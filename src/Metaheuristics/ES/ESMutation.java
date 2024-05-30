/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.ES;

import Metaheuristics.GA.GABoard;
import Metaheuristics.Metaheuristics;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.Pair;
import org.moeaframework.algorithm.single.SelfAdaptiveNormalVariation;
import org.moeaframework.core.Solution;

/**
 *
 * @author Hans
 */
public class ESMutation extends SelfAdaptiveNormalVariation {

    private final double mutationRate;
    Pair selectedPair;
    Pair emptySpace;
    
     public ESMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }
     
     public Solution[] evolve(Solution[] parents) {
        // Genera un número aleatorio entre 0 y 99
        int percent = Metaheuristics.random.nextInt(100);
        int boxCount = GeneratorUtils.CountCharacters(1, ((GABoard) parents[0].getVariable(0)).GetBoard());
        
        if(boxCount > 1){
            if (percent <= 50) {
                return MoveMutation(parents);
            } else if (percent > 50 && percent <= 75) {
                return AddMutation(parents);
            } else {
                return RemoveMutation(parents);
            }
        }
        else{
            if (percent <= 60) {
                return MoveMutation(parents);
            } else {
                return AddMutation(parents);
            }
        }
    }
     
    // Simula la función MoveMutation
    public Solution[] MoveMutation(Solution[] parents) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_MOVE_MUTATION++;
        
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

    public Solution[] AddMutation(Solution[] parents) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_ADD_MUTATION++;
        
        //Clone current board state
        GABoard parent1 = (GABoard) parents[0].getVariable(0);
        GABoard offspring1 = (GABoard) parent1.copy();  // Define offspring1 as a copy of parent1
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(offspring1.GetBoard());
        
        //Get two empty spaces
        Pair emptySpace_1;
        Pair emptySpace_2;
        do{
        emptySpace_1 = GeneratorUtils.GetEmptySpacePair(cloneBoard);
        emptySpace_2 = GeneratorUtils.GetEmptySpacePair(cloneBoard);
        
        }while(emptySpace_1.IsEquals(emptySpace_2));
        
        cloneBoard[emptySpace_1.i][emptySpace_1.j] = '$';
        cloneBoard[emptySpace_2.i][emptySpace_2.j] = '.';
    
        int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        if(Metaheuristics.Solve(cloneBoard, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            offspring1.SetBoard(cloneBoard);
            
            Solution solution1 = new Solution(1, 1); // 1 variable, 2 objetivos (ejemplo)
            solution1.setVariable(0, offspring1);

            cloneBoard=null;
            return new Solution[]{solution1};  
        }
        
        return parents;
    }
    
     public Solution[] RemoveMutation(Solution[] parents) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_REMOVE_MUTATION++;
        
        //Clone current board state
        GABoard parent1 = (GABoard) parents[0].getVariable(0);
        GABoard offspring1 = (GABoard) parent1.copy();  // Define offspring1 as a copy of parent1
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(offspring1.GetBoard());
        
        //Player : 0 , box: 1 , goal: 2
        int max = GeneratorUtils.CountCharacters(1, cloneBoard);
        Pair box = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, 1,
                Metaheuristics.random.nextInt(max));
        max = GeneratorUtils.CountCharacters(2, cloneBoard);
        Pair goal = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, 2,
                Metaheuristics.random.nextInt(max));
        
        //Remove Box
        if(cloneBoard[box.i][box.j]=='*')
            cloneBoard[box.i][box.j] ='.';
        else
            cloneBoard[box.i][box.j] =' ';
        
        //Remove goal
        if(cloneBoard[goal.i][goal.j]=='*')
            cloneBoard[goal.i][goal.j] ='$';
        else if(cloneBoard[goal.i][goal.j]=='+')
            cloneBoard[goal.i][goal.j] ='@';
        else
            cloneBoard[goal.i][goal.j] =' ';
        
        int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        if(Metaheuristics.Solve(cloneBoard, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            offspring1.SetBoard(cloneBoard);
            
            Solution solution1 = new Solution(1, 1); // 1 variable, 2 objetivos (ejemplo)
            solution1.setVariable(0, offspring1);

            cloneBoard=null;
            return new Solution[]{solution1};  
        }
        
        return parents;
    }

}
