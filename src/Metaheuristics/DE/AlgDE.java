/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.DE;

import org.moeaframework.algorithm.single.AggregateObjectiveComparator;
import org.moeaframework.algorithm.single.LinearDominanceComparator;
import org.moeaframework.algorithm.single.SingleObjectiveEvolutionaryAlgorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Settings;
import org.moeaframework.core.Solution;
import org.moeaframework.core.comparator.DominanceComparator;
import org.moeaframework.core.initialization.RandomInitialization;
import org.moeaframework.core.operator.real.DifferentialEvolutionVariation;
import org.moeaframework.core.selection.DifferentialEvolutionSelection;

/**
 *
 * @author Hans
 */
public class AlgDE extends SingleObjectiveEvolutionaryAlgorithm {
	
	/**
	 * The differential evolution selection operator.
	 */
	private final DifferentialEvolutionSelection selection;
	
	/**
	 * Constructs a new single-objective differential evolution algorithm with default settings.
	 * 
	 * @param problem the problem
	 */
	public AlgDE(Problem problem) {
		this(problem,
				Settings.DEFAULT_POPULATION_SIZE,
				new LinearDominanceComparator(),
				new RandomInitialization(problem),
				new DifferentialEvolutionSelection(),
				new DifferentialEvolutionVariation());
	}

	/**
	 * Constructs a new instance of the single-objective differential evolution (DE) algorithm.
	 * 
	 * @param problem the problem
	 * @param initialPopulationSize the initial population size
	 * @param comparator the aggregate objective comparator
	 * @param initialization the initialization method
	 * @param selection the differential evolution selection operator
	 * @param variation the differential evolution variation operator
	 */
	public AlgDE(Problem problem, int initialPopulationSize, AggregateObjectiveComparator comparator,
			Initialization initialization, DifferentialEvolutionSelection selection,
			DifferentialEvolutionVariation variation) {
		super(problem, initialPopulationSize, new Population(), null, comparator, initialization, variation);
		
		
		
		this.selection = selection;
	}
	
	@Override
	protected void iterate() {
            Population population = getPopulation();
            DifferentialEvolutionVariation variation = getVariation();
            Population children = new Population();

            //generate children
            for (int i = 0; i < population.size(); i++) {
                    selection.setCurrentIndex(i);

                    Solution[] parents = selection.select(variation.getArity(), population);
                    children.add(variation.evolve(parents)[0]);
            }

            //evaluate children
            //evaluateAll(children);

            //greedy selection of next population
            for (int i = 0; i < population.size(); i++) {
                    if (((DominanceComparator)comparator).compare(children.get(i), population.get(i)) < 0) {
                            population.replace(i, children.get(i));
                    }
            }
	}

	@Override
	public NondominatedPopulation getResult() {
		NondominatedPopulation result = new NondominatedPopulation(comparator);
		result.addAll(getPopulation());
		return result;
	}
	
	@Override
	public DifferentialEvolutionVariation getVariation() {
		return (DifferentialEvolutionVariation)super.getVariation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVariation(DifferentialEvolutionVariation variation) {
		super.setVariation(variation);
	}
}