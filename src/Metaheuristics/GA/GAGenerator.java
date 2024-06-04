/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.GA;

import Metaheuristics.MetaInitialize;
import Metaheuristics.MetaComparator;
import Metaheuristics.BoardMutation;
import Metaheuristics.BoardCrossover;
import Metaheuristics.Metaheuristics;
import de.sokoban_online.jsoko.JSoko;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.operator.CompoundVariation;

/**
 *
 * @author Hans
 */
public class GAGenerator {

    //List for results
    ArrayList<Double> bestFitnessPerGeneration = new ArrayList<>();
    ArrayList<Double> worstFitnessPerGeneration = new ArrayList<>();
    ArrayList<Float> avgFitnessPerGeneration = new ArrayList<>();
    ArrayList<String> stdDevFitnessPerGeneration = new ArrayList<>();
        
    public GAGenerator(){
        bestFitnessPerGeneration = new ArrayList<>();
        worstFitnessPerGeneration = new ArrayList<>();
        avgFitnessPerGeneration = new ArrayList<>();
        stdDevFitnessPerGeneration = new ArrayList<>();
    }
    
    public void Start(){
        System.out.println("Running GAGenerator");
        Metaheuristics.Init();
        
        // Crear operadores de crossover y mutación
        BoardCrossover gaBoardCrossover = new BoardCrossover(Metaheuristics.P_CROSSOVER_PROB);
        BoardMutation gaBoardMutation = new BoardMutation(Metaheuristics.P_MUTATION_PROB);
        Variation variation = new CompoundVariation(gaBoardCrossover, gaBoardMutation);
        
         // Crear el comparador para problemas monoobjetivo
        MetaComparator gaComparator = new MetaComparator();
        
        // Problem
        GAProblem gaProblem = new GAProblem(Metaheuristics.application);
 
        // Crear la población inicial aleatoria
        MetaInitialize gaInitialize = new MetaInitialize(gaProblem);
        
        // Crear el algoritmo genético
        Algorithm ga = new AlgGA(
                gaProblem,
                Metaheuristics.P_POPULATION_COUNT,
                gaComparator, 
                gaInitialize,
                variation
        );
        Population population = null;
        // Ejecutar el algoritmo por un número determinado de generaciones
        for (int generation = 0; generation < Metaheuristics.P_GENERATION_COUNT; generation++) {
            ga.step();
            System.out.println("Generation: " + generation);

            // Registrar estadísticas de la población actual
            double bestFitness = Double.NEGATIVE_INFINITY;
            double worstFitness = Double.POSITIVE_INFINITY;
            double totalFitness = 0.0;
            double totalFitnessSquared = 0.0;
            
             // Obtener la población actual
            population = ((AlgGA) ga).getPopulation();
            Solution solution;
            // Imprimir la población actual
            for (int j = 0; j < population.size(); j++) {

                solution = population.get(j);
                double fitness = solution.getObjective(0); 
                /*System.out.println("----------------------------: " +  j);
                System.out.println( solution.getVariable(0));
                System.out.println( fitness);  */

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

        GetResults(ga);
        
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
            for (int i = 0; i < Metaheuristics.P_GENERATION_COUNT; i++) {
                csvWriter.append(String.valueOf(i)).append(",")
                        .append(String.valueOf(bestFitnessPerGeneration.get(i))).append(",")
                        .append(String.valueOf(worstFitnessPerGeneration.get(i))).append(",")
                        .append(String.valueOf(avgFitnessPerGeneration.get(i))).append(",")
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

        alg.getResult().display();
        Metaheuristics.PrintStatistics();
    }

    private Solution GetBestSOlution(Algorithm alg) {
        Solution bestSolution = null;
        double bestFitness = Double.NEGATIVE_INFINITY;
        Population lastPopulation = ((AlgGA) alg).getPopulation();
        for (Solution solution : lastPopulation) {
            double fitness = solution.getObjective(0);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestSolution = solution;
            }
        }
        
        return bestSolution;
    }
}
