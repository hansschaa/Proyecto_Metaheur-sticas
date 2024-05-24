/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics;

import Metaheuristics.GA.GABoard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

/**
 *
 * @author Hans
 */
public class MetaInitialize implements Initialization {
    protected final Problem problem;
    //StringBuilder sb1;
    //StringBuilder sb2;
    public MetaInitialize(Problem problem) {
        super();
        //System.out.println("MetaInitialize");
        this.problem = problem;
    }
    
    /*@Override
    public Solution[] initialize(int populationSize){
    System.out.println("->initialize");
        Solution[] initialPopulation = new Solution[populationSize];
        
        for (int i = 0; i < populationSize; i++) {
                Solution solution = problem.newSolution();
                initialPopulation[i] = solution;
        }
        
        return initialPopulation;
    }*/

    @Override
    public Solution[] initialize(int populationSize) {

        System.out.println("->initialize: "+populationSize);
        
        ArrayList<Solution> initialPopulationAux = new ArrayList<>();
        // Obj is the type of the object to be stored in Set 
        
        for (int i = 0; i < Metaheuristics.P_INITIAL_SEARCH_SIZE; i++) {
                Solution solution= problem.newSolution();
                initialPopulationAux.add(solution);
        }
        
        //Remove duplicates
        /*ArrayList<Integer> toRemove = new ArrayList<>();
        char[][] currentBoard, otherBoard;
        
        for (int i = 0; i < Metaheuristics.P_INITIAL_SEARCH_SIZE; i++) {
            
            if(toRemove.contains(i))
                continue;
            
            currentBoard = ((GABoard)initialPopulationAux.get(i).getVariable(0)).GetBoard();
            
            //Remove duplicates
            for (int j = 0; j < Metaheuristics.P_INITIAL_SEARCH_SIZE; j++) {
                if(j==i) continue;
                
                otherBoard = ((GABoard)initialPopulationAux.get(j).getVariable(0)).GetBoard();
                if(AreEquals(currentBoard, otherBoard)){
                    toRemove.add(j);
                }
            }
        }
        
        System.out.println("Repetidos: " + toRemove.size());
        int cont=0;
        for (int i = 0; i < toRemove.size(); i++) {
            initialPopulationAux.remove(i-cont);
            cont++;
        }
        */        
        // Ordenar la lista de individuos de mayor a menor según la variable fitness
        Collections.sort(initialPopulationAux, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                // Ordenar de mayor a menor
                return Double.compare(o2.getObjective(0), o1.getObjective(0));
            }
        });

        // Obtener los primeros 10 elementos
        List<Solution> topIndividuals = initialPopulationAux.subList(0, Math.min(populationSize, Metaheuristics.P_INITIAL_SEARCH_SIZE));
        /*for (int i = 0; i < initialPopulationAux.size(); i++) {
            ((GABoard)initialPopulationAux.get(i).getVariable(i)).SetBoard(null);
        }*/
        
        initialPopulationAux=null;
        // Crear un array a partir de la lista acortada
        //Solution[] initialPopulation = topIndividuals.toArray(new Solution[0]);
        
        
        //GC
        //System.gc();
        //System.runFinalization();
        //Runtime.getRuntime().gc();
        return topIndividuals.toArray(new Solution[0]);
    }
    
    public boolean AreEquals(char[][] current, char[][] other){
    
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[0].length; j++) {
                if(current[i][j] != other[i][j])
                    return false;
            }
        }
        
        
        return true;
    
    }
    
    
    
}
