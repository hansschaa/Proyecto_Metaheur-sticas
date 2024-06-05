/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.SA;

import Metaheuristics.Metaheuristics;
import static Metaheuristics.Metaheuristics.I_ALG_NAME;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.Pair;
import java.util.Date;

/**
 *
 * @author Hans
 */
public class SAGenerator {
    
    private final double initialTemperature;
    private final double coolingRate;
    
    Pair selectedPair;
    Pair emptySpace;
    SABoard bestSolution;
    public SAGenerator(double initialTemperature, double coolingRate){
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.bestSolution = new SABoard();
    }
    
    public void Start(){
        
        System.out.println("Running SAGenerator");
        Metaheuristics.Init();
        
        SABoard currentSolution = GenerateInitialBoard();
        
        currentSolution.Copy(bestSolution);

        double temperature = initialTemperature;
        Metaheuristics.S_TIME = new Date().getTime(); 
        //while (temperature > 1) 
        while (!Metaheuristics.STOP) {
              
            SABoard newSolution = Mutate(currentSolution);
            int newScore = currentSolution.fitness;

            if (acceptanceProbability(currentSolution.fitness, newScore, temperature) > Metaheuristics.random.nextDouble()) {
                currentSolution = newSolution;
            }

            if (currentSolution.fitness > bestSolution.fitness) {
                currentSolution.Copy(bestSolution);
            }

            temperature *= 1 - coolingRate;
            //System.out.println("Temperature: " + temperature);
        }
        Metaheuristics.E_TIME = new Date().getTime();
        Metaheuristics.TOTALTIME += Metaheuristics.E_TIME-Metaheuristics.S_TIME;

        bestSolution.Show();
        ShowStadistics();
        //return bestSolution;
    }
    
    public void ShowStadistics(){
        
        Metaheuristics.BESTBOARD = bestSolution.board;
        Metaheuristics.BESTFITNESS = bestSolution.fitness;
        
        System.out.println(Metaheuristics.I_ALG_NAME + "," + initialTemperature + "," + coolingRate+ ","
                + Metaheuristics.BESTFITNESS+ ","+ Metaheuristics.TOTALTIME);
    }
    
    private double acceptanceProbability(int currentScore, int newScore, double temperature) {
        if (newScore > currentScore) {
            return 1.0;
        }
        return Math.exp((currentScore - newScore) / temperature);
    }
    
    public SABoard GenerateInitialBoard(){
        
        SABoard newBoard = new SABoard();
        de.sokoban_online.jsoko.leveldata.solutions.Solution jsokoSolution = null;
        Pair pair;
        do{
            //System.out.println("->Probar");
            newBoard.board = GeneratorUtils.CloneCharArray(Metaheuristics.P_BASE_BOARD);

            pair = GeneratorUtils.GetEmptySpacePair(newBoard.board);
            newBoard.board[pair.i][pair.j] = '$';

            pair = GeneratorUtils.GetEmptySpacePair(newBoard.board);
            newBoard.board[pair.i][pair.j] = '.';

            pair = GeneratorUtils.GetEmptySpacePair(newBoard.board);
            newBoard.board[pair.i][pair.j] = '@';

            jsokoSolution = Metaheuristics.Solve(newBoard.board, false, 1); 
            newBoard.fitness = Metaheuristics.application.movesHistory.getPushesCount();
        }while(jsokoSolution== null);
        
        return newBoard;
    }
    
    public SABoard Mutate(SABoard toMutate){
               
        //TO DO mutate mutated
        // Genera un número aleatorio entre 0 y 99
        int percent = Metaheuristics.random.nextInt(100);
        int boxCount = GeneratorUtils.CountCharacters(1, toMutate.board);
        
        if(boxCount > 1){
            if(boxCount == Metaheuristics.P_MAX_BOXES){
                if (percent <= 60) {
                    return MoveMutation(toMutate);
                } else {
                    return RemoveMutation(toMutate);
                }
            }
            
            else{
                if (percent <= 50) {
                    return MoveMutation(toMutate);
                } else if (percent > 50 && percent <= 75) {
                    return AddMutation(toMutate);
                } else {
                    return RemoveMutation(toMutate);
                }
            }
        }
        else{
            if (percent <= 60) {
                return MoveMutation(toMutate);
            } else {
                return AddMutation(toMutate);
            }
        }    
    }
    
    // Simula la función MoveMutation
    public SABoard MoveMutation(SABoard toMutate) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_MOVE_MUTATION++;
        
        //Clone current board state
        SABoard mutated = new SABoard();
        toMutate.Copy(mutated);
        
        //Player : 0 , box: 1 , goal: 2
        var randomElementIndex = Metaheuristics.random.nextInt(3);
        int max = GeneratorUtils.CountCharacters(randomElementIndex, mutated.board);
        selectedPair = GeneratorUtils.FindCharacterPairIndexBased(mutated.board, randomElementIndex,
                Metaheuristics.random.nextInt(max));
        
        //Get a empty space
        emptySpace = GeneratorUtils.GetEmptySpacePair(mutated.board);
        
        //Replace
        if(randomElementIndex == 0){
            if(mutated.board[selectedPair.i][selectedPair.j] == '+')
                mutated.board[selectedPair.i][selectedPair.j] = '.';
            else
                mutated.board[selectedPair.i][selectedPair.j] = ' ';
                
            mutated.board[emptySpace.i][emptySpace.j] = '@';
        }
        
        
        else if(randomElementIndex == 1){
            if(mutated.board[selectedPair.i][selectedPair.j] == '*')
                mutated.board[selectedPair.i][selectedPair.j] = '.';
            else
                mutated.board[selectedPair.i][selectedPair.j] = ' ';
                
            mutated.board[emptySpace.i][emptySpace.j] = '$';
        }
        
        else if(randomElementIndex == 2){
            if(mutated.board[selectedPair.i][selectedPair.j] == '*')
                mutated.board[selectedPair.i][selectedPair.j] = '$';
            else if(mutated.board[selectedPair.i][selectedPair.j] == '+')
                mutated.board[selectedPair.i][selectedPair.j] = '@';
            else
                mutated.board[selectedPair.i][selectedPair.j] = ' ';
                
            mutated.board[emptySpace.i][emptySpace.j] = '.';
        }
        
        
        int boxCount = GeneratorUtils.CountCharacters(1, mutated.board);
        if(Metaheuristics.Solve(mutated.board, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            mutated.fitness = Metaheuristics.application.movesHistory.getPushesCount();
            return mutated;  
        }

        mutated = null;
        
        return toMutate;
    }

    public SABoard AddMutation(SABoard toMutate) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_ADD_MUTATION++;
        
        //Clone current board state
        SABoard mutated = new SABoard();
        toMutate.Copy(mutated);
        
        //Get two empty spaces
        Pair emptySpace_1;
        Pair emptySpace_2;
        do{
        emptySpace_1 = GeneratorUtils.GetEmptySpacePair(mutated.board);
        emptySpace_2 = GeneratorUtils.GetEmptySpacePair(mutated.board);
        
        }while(emptySpace_1.IsEquals(emptySpace_2));
        
        mutated.board[emptySpace_1.i][emptySpace_1.j] = '$';
        mutated.board[emptySpace_2.i][emptySpace_2.j] = '.';
    
        int boxCount = GeneratorUtils.CountCharacters(1, mutated.board);
        if(Metaheuristics.Solve(mutated.board, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            mutated.fitness = Metaheuristics.application.movesHistory.getPushesCount();
            return mutated;  
        }
        
        return toMutate;
    }
    
     public SABoard RemoveMutation(SABoard toMutate) {
        //return "MoveMutation";
        Metaheuristics.R_TOTAL_MUTATION++;
        Metaheuristics.R_TOTAL_REMOVE_MUTATION++;
        
        //Clone current board state
        SABoard mutated = new SABoard();
        toMutate.Copy(mutated);
        
        //Player : 0 , box: 1 , goal: 2
        int max = GeneratorUtils.CountCharacters(1, mutated.board);
        Pair box = GeneratorUtils.FindCharacterPairIndexBased(mutated.board, 1,
                Metaheuristics.random.nextInt(max));
        max = GeneratorUtils.CountCharacters(2, mutated.board);
        Pair goal = GeneratorUtils.FindCharacterPairIndexBased(mutated.board, 2,
                Metaheuristics.random.nextInt(max));
        
        //Remove Box
        if(mutated.board[box.i][box.j]=='*')
            mutated.board[box.i][box.j] ='.';
        else
            mutated.board[box.i][box.j] =' ';
        
        //Remove goal
        if(mutated.board[goal.i][goal.j]=='*')
            mutated.board[goal.i][goal.j] ='$';
        else if(mutated.board[goal.i][goal.j]=='+')
            mutated.board[goal.i][goal.j] ='@';
        else
            mutated.board[goal.i][goal.j] =' ';
        
        int boxCount = GeneratorUtils.CountCharacters(1, mutated.board);
        if(Metaheuristics.Solve(mutated.board, false, boxCount) != null){
            Metaheuristics.R_TOTAL_EFFECTIVE_MUTATION++;
            mutated.fitness = Metaheuristics.application.movesHistory.getPushesCount();
            return mutated;  
        }
        
        return toMutate;
    }
    
}
