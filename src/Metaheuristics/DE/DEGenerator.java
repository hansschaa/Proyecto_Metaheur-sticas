/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.DE;

import Metaheuristics.BoardCrossover;
import Metaheuristics.BoardMutation;
import Metaheuristics.GA.GAProblem;
import Metaheuristics.Metaheuristics;
import org.moeaframework.core.Variation;
import org.moeaframework.core.operator.CompoundVariation;

/**
 *
 * @author Hans
 */
public class DEGenerator {
    public void Start(){
        System.out.println("Running DEGenerator");
        Metaheuristics.Init();
        
        // Crear operadores de crossover y mutación
        BoardCrossover gaBoardCrossover = new BoardCrossover(Metaheuristics.P_CROSSOVER_PROB);
        BoardMutation gaBoardMutation = new BoardMutation(Metaheuristics.P_MUTATION_PROB);
        Variation variation = new CompoundVariation(gaBoardCrossover, gaBoardMutation);
        
        
    }
}
