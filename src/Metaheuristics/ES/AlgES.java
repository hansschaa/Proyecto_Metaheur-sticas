/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.ES;

import org.moeaframework.algorithm.single.AggregateObjectiveComparator;
import org.moeaframework.algorithm.single.LinearDominanceComparator;
import org.moeaframework.algorithm.single.SelfAdaptiveNormalVariation;
import org.moeaframework.algorithm.single.SingleObjectiveEvolutionaryAlgorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Settings;
import org.moeaframework.core.Solution;
import org.moeaframework.core.configuration.Property;
import org.moeaframework.core.initialization.RandomInitialization;

/**
 *
 * @author Hans
 */
public class AlgES extends SingleObjectiveEvolutionaryAlgorithm {

    /**
	 * Constructs a new instance of the evolution strategy (ES) algorithm with default settings.
	 * 
	 * @param problem the problem to solve
	 */
	public AlgES(Problem problem) {
		this(problem,
				Settings.DEFAULT_POPULATION_SIZE,
				new LinearDominanceComparator(),
				new RandomInitialization(problem),
				new SelfAdaptiveNormalVariation());
	}

	/**
	 * Constructs a new instance of the evolution strategy (ES) algorithm.
	 * 
	 * @param problem the problem to solve
	 * @param initialPopulationSize the initial population size
	 * @param comparator the aggregate objective comparator
	 * @param initialization the initialization method
	 * @param variation the variation operator
	 */
	public AlgES(Problem problem, int initialPopulationSize, AggregateObjectiveComparator comparator,
			Initialization initialization, SelfAdaptiveNormalVariation variation) {
		super(problem, initialPopulationSize, new Population(), null, comparator, initialization, variation);

	}

    @Override
    protected void iterate() {
        Population population = getPopulation();
        SelfAdaptiveNormalVariation variation = (SelfAdaptiveNormalVariation) getVariation();
        Population offspring = new Population();
        int populationSize = population.size();

        for (int i = 0; i < population.size(); i++) {
                Solution[] parents = new Solution[] { population.get(i) };
                Solution[] children = variation.evolve(parents);

                offspring.addAll(children);
        }

        evaluateAll(offspring);

        population.addAll(offspring);
        population.truncate(populationSize, comparator);
    }

    @Override
    public SelfAdaptiveNormalVariation getVariation() {
            return (SelfAdaptiveNormalVariation)super.getVariation();
    }

    /**
     * {@inheritDoc}
     */
    @Property("operator")
    public void setVariation(SelfAdaptiveNormalVariation variation) {
            super.setVariation(variation);
    }
}
