/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import Metaheuristics.GA.GABoard;
import SokoGenerator.GeneratorUtils;
import org.moeaframework.algorithm.single.AggregateObjectiveComparator;
import org.moeaframework.core.Solution;

/**
 *
 * @author Hans
 */
public class MetaComparator implements AggregateObjectiveComparator{

    @Override
    public double[] getWeights() {
        return null; 
    }

    @Override
    public double calculateFitness(Solution solution) {
        System.out.println("Compute fitness");
        Metaheuristics.CALCULATEFITNESS++;
        
        int boxCount = GeneratorUtils.CountCharacters(1, ((GABoard) solution.getVariable(0)).GetBoard());
        Metaheuristics.Solve(((GABoard) solution.getVariable(0)).GetBoard(), true, boxCount);
        
        //GeneratorUtils.PrintCharArray(board);
        //System.out.println("GAProblem.application.movesHistory.getPushesCount(): " + GAProblem.application.movesHistory.getPushesCount());
        return Metaheuristics.application.movesHistory.getPushesCount(); // Modificar para devolver el valor de ajuste adecuado
    }

    @Override
    public int compare(Solution solution1, Solution solution2) {

        /*double fitness1= solution1.getObjective(0);
        double fitness2= solution2.getObjective(0);
        
        /*if(fitness1==999)
            fitness1 = calculateFitness(solution1);
        if(fitness2==999)
            fitness2 = calculateFitness(solution2);*/
        
        // Devuelve un valor negativo si solution1 es mejor que solution2,
        // un valor positivo si solution2 es mejor que solution1,
        // o 0 si son igualmente buenos
        return -Double.compare(solution1.getObjective(0), solution2.getObjective(0));
    }
}
