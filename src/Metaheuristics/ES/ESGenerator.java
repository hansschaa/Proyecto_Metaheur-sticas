/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.ES;

import Metaheuristics.GA.GAProblem;
import Metaheuristics.MetaComparator;
import Metaheuristics.MetaInitialize;
import Metaheuristics.Metaheuristics;

/**
 *
 * @author Hans
 */
public class ESGenerator {
    public void Start(){
        System.out.println("Running ESGenerator");
        Metaheuristics.Init();
        
        // Crear operadores de crossover y mutación
        ESMutation esMutation = new ESMutation(Metaheuristics.P_ES_MUTATION_PROB);
        
        // Crear el comparador para problemas monoobjetivo
        MetaComparator gaComparator = new MetaComparator();
        
        // Problem
        GAProblem gaProblem = new GAProblem(Metaheuristics.application);
        MetaInitialize gaInitialize = new MetaInitialize(gaProblem);
   
        AlgES de = new AlgES(
                gaProblem,
                Metaheuristics.P_POPULATION_COUNT,
                gaComparator, 
                gaInitialize,
                esMutation
        );
        
        for (int generation = 0; generation < Metaheuristics.P_GENERATION_COUNT; generation++) {
            de.step();
            System.out.println("Generation: " + generation);
        }
        
        de.getResult().display();
        Metaheuristics.PrintStatistics();
    }
}
