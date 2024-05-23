/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.GA;

import Metaheuristics.Metaheuristics;
import SokoGenerator.GeneratorUtils;
import de.sokoban_online.jsoko.JSoko;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Hans
 */
public class GAProblem extends AbstractProblem {
    
    
    //Global AUX variables
    //public static de.sokoban_online.jsoko.leveldata.solutions.Solution jsokoSolutionAux = null; 
    
    
    //Local aux variables
    //char[][] boardAux;

    public GAProblem(JSoko application) {
        super(1, 1); // 1 variable de decisión (la matriz), 1 objetivo
    }

    @Override
    public void evaluate(Solution solution) {
        //System.out.println("Evaluate");
        Metaheuristics.EVALUATECOUNT++;
        char[][] boardAux = ((GABoard)solution.getVariable(0)).GetBoard();
        
        if(Metaheuristics.Solve(boardAux, true, GeneratorUtils.CountCharacters(1, boardAux))==null)
        {
            //GeneratorUtils.PrintCharArray(board);
            //System.out.println("check");
            solution.setObjective(0, -1); // Maximizar el número de 'A'
        }
        else{
            solution.setObjective(0, Metaheuristics.application.movesHistory.getPushesCount()); // Maximizar el número de 'A'
        } 
    }

    @Override
    public Solution newSolution() {
        
        Metaheuristics.NEWSOlCOUNT++;
        //System.out.println("New solution");
        Solution solution = new Solution(1, 1);
        solution.setObjective(0, 999);
        //Init
        GABoard gaBoard = new GABoard(Metaheuristics.P_BASE_BOARD);
        var fitness = gaBoard.Initialize();
        
        solution.setVariable(0,gaBoard);
        solution.setObjective(0,fitness);
        
        return solution;
    }
    
    
}
