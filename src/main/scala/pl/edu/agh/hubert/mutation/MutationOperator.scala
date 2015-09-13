package pl.edu.agh.hubert.mutation

import pl.edu.agh.hubert.{MathIndividual, Individual}
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.generator.IndividualGenerator
import pl.edu.agh.hubert.languages._

import scala.util.Random

trait MutationOperator {

  def mutate(individual: Individual): Individual

}

object MutationOperator {

  def apply(experiment: Experiment): MutationOperator = {
    experiment.language.name match {
      case "math" => return new MathMutationOperator(
        experiment.mutationProbability,
        experiment.maxHeight,
        IndividualGenerator(experiment))

    }
    throw new IllegalArgumentException("Unknown language!")
  }

}

class MathMutationOperator(
                            mutationProbability: Double,
                            maxHeight: Int,
                            individualGenerator: IndividualGenerator
                            ) extends MutationOperator {

  override def mutate(individual: Individual): Individual = {
    val mathIndividual = individual.asInstanceOf[MathIndividual]
    new MathIndividual(mutate(mathIndividual.tree, 0))
  }

  private def mutate(root: LanguageWord, depth: Int): LanguageWord = {
    if (Random.nextDouble() < mutationProbability) {
      individualGenerator.generateGenome(maxHeight - depth)
    } else {

      root match {
        case plus: Plus => new Plus(mutate(plus.leftWord, depth + 1), mutate(plus.rightWord, depth + 1))
        case minus: Minus => new Plus(mutate(minus.leftWord, depth + 1), mutate(minus.rightWord, depth + 1))
        case mul: Mul => new Plus(mutate(mul.leftWord, depth + 1), mutate(mul.rightWord, depth + 1))
        case sin: Sin => new Sin(mutate(sin.internalWord, depth + 1))
        case cos: Cos => new Cos(mutate(cos.internalWord, depth + 1))
        case word: LanguageWord => word
      }
    }
  }
}