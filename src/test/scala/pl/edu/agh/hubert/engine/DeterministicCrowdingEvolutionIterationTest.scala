package pl.edu.agh.hubert.engine

import java.io.File

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.languages.math.DifferentiationWithFitnessPredictionFitnessFunction

import scalax.io.Resource

class DeterministicCrowdingEvolutionIterationTest extends FunSuite {

  test("should execute experiment with math language") {
    val tmp = File.createTempFile("temp", ".csv")
    Resource.fromFile(tmp).writeStrings(List("#t,varB\n", "1.0,3.0\n", "2.0,4.0\n", "3.0,6.0"))

    val experiment = Experiment(
      1,
      "exp1",
      "desc1",
      10,
      Languages.mathLanguage(),
      new DataSet(tmp.getAbsolutePath, Set("t", "varB")),
      fitnessFunction = classOf[DifferentiationWithFitnessPredictionFitnessFunction].getName
    )

    try {
      new DeterministicCrowdingEvolutionIteration(experiment).evolve()
    } catch {
      case e: Throwable => fail("Exception thrown! ", e)
    }
  }

  test("should throw exception when experiment doesn't contain time variable for math language") {
    val tmp = File.createTempFile("temp", ".csv")
    val experiment = Experiment(
      1,
      "exp1",
      "desc1",
      10,
      Languages.mathLanguage(),
      new DataSet(tmp.getAbsolutePath, Set("varA", "varB")),
      fitnessFunction = classOf[DifferentiationWithFitnessPredictionFitnessFunction].getName
    )

    var exceptionThrown: Boolean = false
    try {
      new DeterministicCrowdingEvolutionIteration(experiment).evolve()
    } catch {
      case e: Throwable => exceptionThrown = true
    }

    assert(exceptionThrown)
  }

}
