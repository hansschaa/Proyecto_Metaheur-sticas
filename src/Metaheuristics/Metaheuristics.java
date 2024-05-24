/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.leveldata.LevelCollection.Builder;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
import de.sokoban_online.jsoko.solver.SolverGUI;
import java.util.Random;

/**
 *
 * @author Hans
 */
public class Metaheuristics {
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
    public static int P_GENERATION_COUNT = 30;
    public static int P_POPULATION_COUNT = 20;
    public static int P_INITIAL_SEARCH_SIZE = 70;
    public static float P_CROSSOVER_PROB = .95f;
    public static float P_MUTATION_PROB = 0.1f;
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
    
    static Builder builder;
    
    public Metaheuristics(JSoko application) {
        Metaheuristics.application = application;
        Metaheuristics.random = new Random();
    }
    
    public static void Init(){
        Metaheuristics.solverLevel = new Level(application.levelIO.database);
        Metaheuristics.solverGUI = new SolverGUI(application);
        builder =  new LevelCollection.Builder();
    }
    
    public static de.sokoban_online.jsoko.leveldata.solutions.Solution Solve(char[][] board, boolean optimal, int boxCount) {
        
        //solverLevel.setBoardData(GeneratorUtils.ConvertCharArrayToString(board));
        solverLevel.setBoardData(board);
        solverLevel.setBoxCount(boxCount);
        levelCollection = builder.setLevels(solverLevel).build();
        application.setCollectionForPlaying(levelCollection);
        application.setLevelForPlaying(1);
        application.currentLevel = solverLevel;

        //de.sokoban_online.jsoko.leveldata.solutions.Solution solution = null;
        
        if(optimal)
            return new SolverAnySolution(application, solverGUI).searchSolution();
        else{
            //GeneratorUtils.PrintCharArray(board);
            return new SolverAnySolution(application, solverGUI).searchSolution();
        }
        
        /*try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(Metaheuristics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //System.gc();
        
        //return solution;
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
