/*
 * JENES
 * A time and memory efficient Java library for genetic algorithms and more 
 * Copyright (C) 2011 Intelligentia srl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package jenes.tutorials.old.problem2;

import jenes.GeneticAlgorithm;
import jenes.chromosome.IntegerChromosome;
import jenes.population.Individual;
import jenes.population.Population;

/**
 * Tutorial showing how to extend <code>GeneticAlgorithm</code> and how to use
 * the flexible and configurable breeding structure in Jenes.
 * The problem consists in searching a pattern of integers with a given precision.
 * Solutions flow through two different crossovers in parallel. Some are processed by
 * a single point crossover, the other by a double point crossover.
 * After solutions are mutated.
 *
 * This class implements the algorithm by extending <code>GeneticAlgorithm</code>.
 *
 * @version 1.0
 * 
 * @since 1.0
 */
public class PatternGA extends GeneticAlgorithm<IntegerChromosome>{
    
    private int[] target;
    private int precision;
    
    public PatternGA(Population<IntegerChromosome> pop, int numGen) {
        super(pop,numGen);
        this.setBiggerIsBetter(false);
    }
    
    public void setTarget(int[] target) {
        this.target = target;
    }
    
    public void setPrecision(int precision) {
        this.precision = precision;
    }
    
    @Override
    public void evaluateIndividual(Individual<IntegerChromosome> individual) {
        IntegerChromosome chrom = individual.getChromosome();
        int diff = 0;
        int length=chrom.length();
        for(int i=0;i<length;i++){
            diff += Math.abs(chrom.getValue(i)-this.target[i]);
        }
        individual.setScore(diff);
    }
    
    @Override
    protected boolean end(){
        jenes.population.Population.Statistics stat = this.getCurrentPopulation().getStatistics();
        return stat.getLegalLowestScore() <= this.precision;
    }
}
