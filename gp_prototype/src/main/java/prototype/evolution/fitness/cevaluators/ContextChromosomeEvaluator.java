package prototype.evolution.fitness.cevaluators;

import org.jgap.gp.impl.ProgramChromosome;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:31
 */
public interface ContextChromosomeEvaluator<T> {
    double evaluateChromosomeInContext(ProgramChromosome chromosome, T context);
}