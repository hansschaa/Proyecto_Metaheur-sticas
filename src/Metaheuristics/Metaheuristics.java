/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
import de.sokoban_online.jsoko.solver.SolverAStarPushesMoves;
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
    
    //Stats
    public static String I_ALG_NAME;
    public static int R_TOTAL_CROSSOVER;
    public static int R_TOTAL_EFFECTIVE_CROSSOVER;
    public static int R_TOTAL_REPAIR;
    public static int R_TOTAL_EFFECTIVE_REPAIR;
    public static int R_TOTAL_MUTATION;
    public static int R_TOTAL_MOVE_MUTATION;
    public static int R_TOTAL_ADD_MUTATION;
    public static int R_TOTAL_REMOVE_MUTATION;
    public static int R_TOTAL_EFFECTIVE_MUTATION;
    
    //Hyperparameters
    public static int P_GENERATION_COUNT = 15;
    public static int P_POPULATION_COUNT = 15;
    public static int P_INITIAL_SEARCH_SIZE = 80;
    public static float P_CROSSOVER_PROB = .95f;
    public static float P_MUTATION_PROB = 0.1f;
    public static float P_ES_MUTATION_PROB = .9f;
    public static double P_INITIAL_TEMPERATURE =3000;
    public static double P_COOLING_RATE = 0.015;
 
    public static int P_MAX_BOXES = 6;
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
    
    //Others
    public static Runtime runtime;
    
    public Metaheuristics(JSoko application) {
        Metaheuristics.application = application;
        Metaheuristics.random = new Random();
    }
    
    public static void Init(){
        Metaheuristics.solverLevel = new Level(application.levelIO.database);
        Metaheuristics.solverGUI = new SolverGUI(application);
        Metaheuristics.runtime = Runtime.getRuntime();
    }
    
    public static de.sokoban_online.jsoko.leveldata.solutions.Solution Solve(char[][] board, boolean optimal, int boxCount) {
        
        EVALUATECOUNT++;
        
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
    
    public static void PrintMemory(){
         // Convertir de bytes a megabytes
        final long MEGABYTE = 1024L * 1024L;

        // Mostrar la cantidad de memoria máxima que puede usar la JVM
        long maxMemory = runtime.maxMemory() / MEGABYTE;
        System.out.println("Memoria máxima (MB): " + maxMemory);

        // Mostrar la cantidad de memoria total asignada actualmente a la JVM
        long totalMemory = runtime.totalMemory() / MEGABYTE;
        System.out.println("Memoria total asignada (MB): " + totalMemory);

        // Mostrar la cantidad de memoria libre actualmente dentro del total asignado a la JVM
        long freeMemory = runtime.freeMemory() / MEGABYTE;
        System.out.println("Memoria libre (MB): " + freeMemory);

        // Calcular la memoria usada actualmente
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / MEGABYTE;
        System.out.println("Memoria usada (MB): " + usedMemory);
    }

    public static void PrintStatistics() {
        System.out.println("-> STATS: ");
        System.out.println("I_ALG_NAME: " + I_ALG_NAME);
        System.out.println("R_TOTAL_CROSSOVER: " + R_TOTAL_CROSSOVER);
        System.out.println("R_TOTAL_EFFECTIVE_CROSSOVER: " + R_TOTAL_EFFECTIVE_CROSSOVER);
        System.out.println("R_TOTAL_REPAIR: " + R_TOTAL_REPAIR);
        System.out.println("R_TOTAL_EFFECTIVE_REPAIR: " + R_TOTAL_EFFECTIVE_REPAIR);
        System.out.println("R_TOTAL_MUTATION: " + R_TOTAL_MUTATION);
        System.out.println("R_TOTAL_EFFECTIVE_MUTATION: " + R_TOTAL_EFFECTIVE_MUTATION);
        System.out.println("R_TOTAL_MOVE_MUTATION: " + R_TOTAL_MOVE_MUTATION);
        System.out.println("R_TOTAL_ADD_MUTATION: " + R_TOTAL_ADD_MUTATION);
        System.out.println("R_TOTAL_REMOVE_MUTATION: " + R_TOTAL_REMOVE_MUTATION);

        //System.out.println("NEWSOlCOUNT: " + NEWSOlCOUNT);
        System.out.println("EVALUATION COUNT: " + EVALUATECOUNT);
        //System.out.println("COPY: " + COPY);
        //System.out.println("CALCULATEFITNESS: " + CALCULATEFITNESS);
    }
}
