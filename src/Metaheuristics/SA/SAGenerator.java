/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.SA;

import Metaheuristics.Metaheuristics;
import SokoGenerator.GeneratorUtils;
import SokoGenerator.Tree.Pair;

/**
 *
 * @author Hans
 */
public class SAGenerator {
    
    private final double initialTemperature;
    private final double coolingRate;
    
    public SAGenerator(double initialTemperature, double coolingRate){
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
    }
    
    public void Start(){
        
        System.out.println("Running SAGenerator");
        Metaheuristics.Init();
        
        SABoard currentSolution = GenerateInitialBoard();
        SABoard bestSolution = new SABoard();
        currentSolution.Copy(bestSolution);

        double temperature = initialTemperature;

        while (temperature > 1) {
            SABoard newSolution = Mutate(currentSolution);
            int newScore = currentSolution.fitness;

            if (acceptanceProbability(currentSolution.fitness, newScore, temperature) > Metaheuristics.random.nextDouble()) {
                currentSolution = newSolution;
            }

            if (currentSolution.fitness > bestSolution.fitness) {
                //bestSolution = problem.cloneBoard(currentSolution);
                //bestScore = currentScore;
                currentSolution.Copy(bestSolution);
            }

            temperature *= 1 - coolingRate;
            System.out.println("Temperature: " + temperature);
        }

        System.out.println("Fin");
        bestSolution.Show();
        
        //return bestSolution;
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
        
        SABoard mutated = new SABoard();
        toMutate.Copy(mutated);
        
        //TO DO mutate mutated
        
        
        return mutated;
    
    }
    
}
