package pl.edu.agh.hubert.evolution.engine.dc;

import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPGenotypeHacker;
import org.jgap.gp.impl.GPPopulation;
import pl.edu.agh.hubert.evolution.engine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DeterministicCrowdingEvolutionIteration implements EvolutionIteration {

    private static final Logger logger = Logger.getLogger(DeterministicCrowdingEvolutionIteration.class);
    private static final GPGenotypeHacker GP_GENOTYPE_HACKER = new GPGenotypeHacker();

    private ExecutorService executorService;
    private GPConfiguration configuration;
    private RandomGenerator randomGenerator;
    private double crossProbability;
    private double mutationProbability;
    private Mutator mutator;
    private Selector<Integer> selector;
    private Tournament tournament;
    private Crossover crossover;

    DeterministicCrowdingEvolutionIteration() {
    }

    void setConfiguration(GPConfiguration configuration) {
        this.configuration = configuration;
        this.randomGenerator = configuration.getRandomGenerator();
        this.crossProbability = configuration.getCrossoverProb();
        this.mutationProbability = configuration.getMutationProb();
    }

    void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    void setSelector(Selector<Integer> selector) {
        this.selector = selector;
    }

    void setMutator(Mutator mutator) {
        this.mutator = mutator;
    }

    void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    @Override
    public void evolve(GPGenotype genotype) {
        assumeProperPopulationSize();

        GPPopulation oldPop = genotype.getGPPopulation();
        GPPopulation newPop = crossAndMutatePopulation(oldPop);

        GP_GENOTYPE_HACKER.setGPPopulation(genotype, newPop);

        genotype.calcFitness();
        configuration.incrementGenerationNr();
    }

    @Override
    public void shutdown() {
        executorService.shutdownNow();
    }

    private IGPProgram select(GPPopulation population, List<Integer> freeIndexes) {
        Integer selectedIndex = selector.select(freeIndexes);
        freeIndexes.remove(selectedIndex);
        return population.getGPProgram(selectedIndex);
    }

    private List<Integer> prepareFreeIndexes(int size) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indexes.add(i);
        }
        return indexes;
    }

    private GPPopulation crossAndMutatePopulation(GPPopulation oldPopulation) {
        IGPProgram[] parents = makeRandomParentPairs(oldPopulation);
        IGPProgram[] children = crossParentPairs(parents);
        IGPProgram[] mutatedChildren = mutateChildren(children);
        IGPProgram[] newIndividuals = generationsTournament(parents, mutatedChildren);
        incrementChromosomeAge(newIndividuals);
        return createPopulationWithNewIndividuals(oldPopulation, newIndividuals);
    }

    private void incrementChromosomeAge(IGPProgram[] newIndividuals) {
        for (IGPProgram program : newIndividuals) {
            incrementChromosomeAge(program);
        }
    }

    private void incrementChromosomeAge(IGPProgram program) {
        for (int i = 0; i < program.size(); i++) {
            AgeTrackedProgramChromosome chromosome = (AgeTrackedProgramChromosome) program.getChromosome(i);
            chromosome.incrementAge();
        }
    }

    private IGPProgram[] mutateChildren(IGPProgram[] children) {
        for (int i = 0; i < children.length; i++) {
            children[i] = mutate(children[i]);
        }

        return children;
    }

    private IGPProgram[] generationsTournament(final IGPProgram[] parents, final IGPProgram[] children) {
        return tournament.getWinners(parents, children);
    }

    private IGPProgram[] crossParentPairs(IGPProgram[] parents) {
        IGPProgram[] children = new IGPProgram[configuration.getPopulationSize()];

        for (int individualIdx = 0; individualIdx < parents.length; individualIdx += 2) {
            IGPProgram[] crossed = cross(parents[individualIdx], parents[individualIdx + 1]);
            children[individualIdx] = crossed[0];
            children[individualIdx + 1] = crossed[1];
        }

        return children;
    }

    private IGPProgram[] makeRandomParentPairs(GPPopulation oldPopulation) {
        IGPProgram[] parents = new IGPProgram[configuration.getPopulationSize()];
        List<Integer> freeIndexes = prepareFreeIndexes(configuration.getPopulationSize());

        int parentIdx = 0;
        while (!freeIndexes.isEmpty()) {
            configuration.clearStack();
            parents[parentIdx++] = select(oldPopulation, freeIndexes);
        }

        return parents;
    }

    private GPPopulation createPopulationWithNewIndividuals(GPPopulation oldPopulation, IGPProgram[] newIndividuals) {
        try {
            GPPopulation gpPopulation = new GPPopulation(oldPopulation, false);

            for (int i = 0; i < newIndividuals.length; i++) {
                gpPopulation.setGPProgram(i, newIndividuals[i]);
            }

            return gpPopulation;
        } catch (InvalidConfigurationException e) {
            logger.error("Can't create new population!", e);
            // TODO: do something smarter
            return null;
        }
    }

    private IGPProgram mutate(IGPProgram toMutate) {
        if (shouldMutate()) {
            return mutator.mutate(toMutate);
        }

        return toMutate;
    }

    private IGPProgram[] cross(IGPProgram individualA, IGPProgram individualB) {
        if (shouldCrossOver()) {
            return crossover.cross(individualA, individualB);
        }

        return new IGPProgram[]{individualA, individualB};
    }

    private boolean shouldMutate() {
        return randomGenerator.nextFloat() <= mutationProbability;
    }

    private boolean shouldCrossOver() {
        return randomGenerator.nextFloat() <= crossProbability;
    }

    private void assumeProperPopulationSize() {
        int popSize = configuration.getPopulationSize();

        if (popSize % 2 != 0) {
            throw new IllegalArgumentException("Population size must be dividable by 2!");
        }
    }

}