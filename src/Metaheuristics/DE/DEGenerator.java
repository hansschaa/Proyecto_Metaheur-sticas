/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.DE;

import Metaheuristics.BoardMutation;
import Metaheuristics.GA.GAProblem;
import Metaheuristics.MetaComparator;
import Metaheuristics.MetaInitialize;
import Metaheuristics.Metaheuristics;
import org.moeaframework.algorithm.single.DifferentialEvolution;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Variation;
import org.moeaframework.core.operator.CompoundVariation;
import org.moeaframework.core.selection.DifferentialEvolutionSelection;

/**
 *
 * @author Hans
 */
public class DEGenerator {
    public void Start(){
        System.out.println("Running DEGenerator");
        Metaheuristics.Init();
        
        // Crear operadores de crossover y mutación
        DECrossover gaBoardCrossover = new DECrossover(Metaheuristics.P_CROSSOVER_PROB, .2);
        BoardMutation gaBoardMutation = new BoardMutation(Metaheuristics.P_MUTATION_PROB);
        Variation variation = new CompoundVariation(gaBoardCrossover, gaBoardMutation);
        
        // Crear el comparador para problemas monoobjetivo
        MetaComparator gaComparator = new MetaComparator();
        
        // Problem
        GAProblem gaProblem = new GAProblem(Metaheuristics.application);
 
        DifferentialEvolutionSelection selection = new DifferentialEvolutionSelection();
        
        
        MetaInitialize gaInitialize = new MetaInitialize(gaProblem);
        
        
        Algorithm algDE = new AlgDE(
                gaProblem,
                Metaheuristics.P_POPULATION_COUNT,
                gaComparator, 
                gaInitialize,
                new DifferentialEvolutionSelection(),
                gaBoardCrossover
        );
        
        for (int generation = 0; generation < Metaheuristics.P_GENERATION_COUNT; generation++) {
            algDE.step();
            System.out.println("Generation: " + generation);
        }
        
        
    }
}
