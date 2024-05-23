/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.GA;

import SokoGenerator.GeneratorUtils;
import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
import de.sokoban_online.jsoko.solver.SolverAStarPushesMoves;
import de.sokoban_online.jsoko.solver.SolverGUI;
import java.util.Random;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author Hans
 */
public class GAProblem extends AbstractProblem {
    
    //JSoko
    public static Random random;
    public static JSoko application;
    public static Level solverLevel;
    public static LevelCollection levelCollection;
    public static SolverGUI solverGUI;
    //public static de.sokoban_online.jsoko.leveldata.solutions.Solution currentSolution;
    
    //Stats
    public static int R_TOTAL_CROSSOVER;
    public static int R_TOTAL_EFFECTIVE_CROSSOVER;
    public static int R_TOTAL_MUTATION;
    public static int R_TOTAL_EFFECTIVE_MUTATION;
    public static int R_TOTAL_REPAIR;
    public static int R_TOTAL_EFFECTIVE_REPAIR;
    
    //Hyperparameters
    public static int P_GENERATION_COUNT = 25;
    public static int P_POPULATION_COUNT = 10;
    public static int P_INITIAL_SEARCH_SIZE = 60;
    public static float P_CROSSOVER_PROB = .95f;
    public static float P_MUTATION_PROB = 0.01f;
    public static int P_MAX_BOXES = 5;
    public static int P_CROSS_SPACING = 2;
    public static char[][] P_BASE_BOARD = {
    {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
    {'#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', '#', '#', '#', '#', '#', '#', ' ', ' ', '#', '#'},
    {'#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', '#', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
    {'#', ' ', ' ', '#', ' ', ' ', ' ', ' ', '#', '#', '#'},
    {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}};
    public static int NEWSOlCOUNT = 0;
    public static int EVALUATECOUNT = 0;
    public static int COPY = 0;
    public static int CALCULATEFITNESS = 0; 
    //Global AUX variables
    //public static de.sokoban_online.jsoko.leveldata.solutions.Solution jsokoSolutionAux = null; 
    
    
    //Local aux variables
    //char[][] boardAux;

    public GAProblem(JSoko application) {
        super(1, 1); // 1 variable de decisión (la matriz), 1 objetivo
        //System.out.println("GAProblem"); 
        this.application = application;
        this.solverLevel = new Level(application.levelIO.database);
        this.solverGUI = new SolverGUI(application);
        this.random = new Random();
        
        //boardAux = null;
    }

    @Override
    public void evaluate(Solution solution) {
        //System.out.println("Evaluate");
        EVALUATECOUNT++;
        char[][] boardAux = ((GABoard)solution.getVariable(0)).GetBoard();
        
        if(Solve(boardAux, true, GeneratorUtils.CountCharacters(1, boardAux))==null)
        {
            //GeneratorUtils.PrintCharArray(board);
            //System.out.println("check");
            solution.setObjective(0, -1); // Maximizar el número de 'A'
        }
        else{
            solution.setObjective(0, application.movesHistory.getPushesCount()); // Maximizar el número de 'A'
        } 
    }

    @Override
    public Solution newSolution() {
        
        NEWSOlCOUNT++;
        //System.out.println("New solution");
        Solution solution = new Solution(1, 1);
        solution.setObjective(0, 999);
        //Init
        GABoard gaBoard = new GABoard(GAProblem.P_BASE_BOARD);
        var fitness = gaBoard.Initialize();
        
        solution.setVariable(0,gaBoard);
        solution.setObjective(0,fitness);
        
        return solution;
    }
    
    public static de.sokoban_online.jsoko.leveldata.solutions.Solution Solve(char[][] board, boolean optimal, int boxCount) {
        
        //solverLevel.setBoardData(GeneratorUtils.ConvertCharArrayToString(board));
        solverLevel.setBoardData(board);
        solverLevel.setBoxCount(boxCount);
        levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{solverLevel}).build();
        application.setCollectionForPlaying(levelCollection);
        application.setLevelForPlaying(1);
        application.currentLevel = solverLevel;

        de.sokoban_online.jsoko.leveldata.solutions.Solution solution = null;
        
        if(optimal)
            solution = new SolverAStarPushesMoves(application, solverGUI).searchSolution();
        else{
            //GeneratorUtils.PrintCharArray(board);
            solution = new SolverAnySolution(application, solverGUI).searchSolution();
        }
        
        return solution;
    }

    public static void printStatistics() {
        System.out.println("R_TOTAL_CROSSOVER: " + R_TOTAL_CROSSOVER);
        System.out.println("R_TOTAL_EFFECTIVE_CROSSOVER: " + R_TOTAL_EFFECTIVE_CROSSOVER);
        System.out.println("R_TOTAL_MUTATION: " + R_TOTAL_MUTATION);
        System.out.println("R_TOTAL_EFFECTIVE_MUTATION: " + R_TOTAL_EFFECTIVE_MUTATION);
        System.out.println("R_TOTAL_REPAIR: " + R_TOTAL_REPAIR);
        System.out.println("R_TOTAL_EFFECTIVE_REPAIR: " + R_TOTAL_EFFECTIVE_REPAIR);
        System.out.println("NEWSOlCOUNT: " + NEWSOlCOUNT);
        System.out.println("EVA: " + EVALUATECOUNT);
        System.out.println("COPY: " + COPY);
        System.out.println("CALCULATEFITNESS: " + CALCULATEFITNESS);
    }
}
