/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.DE;

import Metaheuristics.GA.GABoard;
import Metaheuristics.GA.GAProblem;
import Metaheuristics.MetaComparator;
import Metaheuristics.MetaInitialize;
import Metaheuristics.Metaheuristics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;
import org.moeaframework.core.selection.DifferentialEvolutionSelection;

/**
 *
 * @author Hans
 */
public class DEGenerator {
    
    //List for results
    ArrayList<Double> bestFitnessPerGeneration = new ArrayList<>();
    ArrayList<Double> worstFitnessPerGeneration = new ArrayList<>();
    ArrayList<Float> avgFitnessPerGeneration = new ArrayList<>();
    ArrayList<String> stdDevFitnessPerGeneration = new ArrayList<>();
    
    public void Start(){
        System.out.println("Running DEGenerator");
        Metaheuristics.Init();
        
        // Crear operadores de crossover y mutación
        DECrossover gaBoardCrossover = new DECrossover(Metaheuristics.P_CROSSOVER_PROB_DE, .2);
        
        // Crear el comparador para problemas monoobjetivo
        MetaComparator gaComparator = new MetaComparator();
        
        // Problem
        GAProblem gaProblem = new GAProblem(Metaheuristics.application);
        
        MetaInitialize gaInitialize = new MetaInitialize(gaProblem);
        
        
        Algorithm algDE = new AlgDE(
                gaProblem,
                Metaheuristics.P_POPULATION_COUNT,
                gaComparator, 
                gaInitialize,
                new DifferentialEvolutionSelection(),
                gaBoardCrossover
        );
        Population population = null;
        // Ejecutar el algoritmo por un número determinado de generaciones
        for (int generation = 0; !Metaheuristics.STOP ; generation++) {
            System.out.println("Generation: " + generation);
            Metaheuristics.S_TIME = new Date().getTime();  
            algDE.step();
            Metaheuristics.E_TIME = new Date().getTime();
            Metaheuristics.TOTALTIME += Metaheuristics.E_TIME-Metaheuristics.S_TIME;
            
            // Registrar estadísticas de la población actual
            double bestFitness = Double.NEGATIVE_INFINITY;
            double worstFitness = Double.POSITIVE_INFINITY;
            double totalFitness = 0.0;
            double totalFitnessSquared = 0.0;
            
            // Obtener la población actual
            population = ((AlgDE) algDE).getPopulation();
            Solution solution;
            // Imprimir la población actual
            for (int j = 0; j < population.size(); j++) {

                solution = population.get(j);
                double fitness = solution.getObjective(0); 

                if (fitness > bestFitness) {
                    bestFitness = fitness;
                }
                if (fitness < worstFitness) {
                    worstFitness = fitness;
                }
                
                totalFitness += fitness;
                totalFitnessSquared += fitness * fitness;
            }
            
            
            float avgFitness = (float)(totalFitness / population.size());
            float stdDevFitness = (float)(Math.sqrt((totalFitnessSquared / population.size()) - (avgFitness * avgFitness)));

            solution=null;
            population=null;
            
            bestFitnessPerGeneration.add(bestFitness);
            worstFitnessPerGeneration.add(worstFitness);
            avgFitnessPerGeneration.add(avgFitness);
            stdDevFitnessPerGeneration.add(String.format("%.2f", stdDevFitness).replace(',', '.'));
            
            if(generation%2 == 0){
                //System.gc();
                //System.runFinalization();
                //Runtime.getRuntime().gc();
                Metaheuristics.PrintMemory();
            } 
        }

        GetResults(algDE);
        
    }
    
    public void GetResults(Algorithm alg){
        // Obtener el directorio de trabajo
        String directoryPath = "."; // Puedes especificar un directorio diferente si es necesario

        // Contar archivos en el directorio
        File directory = new File(directoryPath+"/Tests");
        int fileCount = directory.list().length;

        // Obtener la fecha actual en el formato deseado
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Construir el nombre del archivo
        String fileName = String.format("Tests/GA_Results_%d_%s.csv", fileCount + 1, date);

        // Obtener el mejor tablero del algoritmo
        Solution bestSolution = GetBestSOlution(alg);
        char[][] board = ((GABoard)bestSolution.getVariable(0)).GetBoard();

        // Exportar resultados a un archivo CSV
        try (FileWriter csvWriter = new FileWriter(fileName)) {
            csvWriter.append(Metaheuristics.I_ALG_NAME + "\n");
            // Crear encabezados
            csvWriter.append("Generation,Best Fitness,Worst Fitness,Average Fitness,Standard Deviation\n");

            // Rellenar datos
            for (int i = 0; i < bestFitnessPerGeneration.size(); i++) {
                csvWriter.append(String.valueOf(i)).append(",")
                        .append(String.valueOf(bestFitnessPerGeneration.get(i))).append(",")
                        .append(String.valueOf(worstFitnessPerGeneration.get(i))).append(",")
                        .append(String.valueOf(Metaheuristics.round(avgFitnessPerGeneration.get(i), 1))).append(",")
                        .append(String.valueOf(stdDevFitnessPerGeneration.get(i)));

                csvWriter.append("\n");
            }

            csvWriter.append("\n");

            for (char[] board1 : board) {
                for (int col = 0; col < board1.length; col++)
                    csvWriter.append(String.valueOf(board1[col]));

                csvWriter.append("\n");
            }

            csvWriter.append(bestSolution.getObjective(0)+"");
            System.out.println("Resultados exportados a " + fileName);

        } catch (IOException e) {} 

        //alg.getResult().display();
        Metaheuristics.PrintStatistics();
    }

    private Solution GetBestSOlution(Algorithm alg) {
        Solution bestSolution = null;
        double bestFitness = Double.NEGATIVE_INFINITY;
        Population lastPopulation = ((AlgDE) alg).getPopulation();
        for (Solution solution : lastPopulation) {
            double fitness = solution.getObjective(0);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestSolution = solution;
            }
        }
        
        //GeneratorUtils.PrintCharArray(((GABoard)bestSolution.getVariable(0)).GetBoard());
        //System.out.println(bestSolution.getObjective(0));
        
        Metaheuristics.BESTFITNESS = bestSolution.getObjective(0);
        Metaheuristics.BESTBOARD = ((GABoard)bestSolution.getVariable(0)).GetBoard();
        
        return bestSolution;
    }
}