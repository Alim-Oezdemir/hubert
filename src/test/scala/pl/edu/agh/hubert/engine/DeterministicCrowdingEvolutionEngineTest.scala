package pl.edu.agh.hubert.engine

import java.io.File

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.languages.Languages

class DeterministicCrowdingEvolutionEngineTest extends FunSuite {

  test("should execute experiment with math language") {
    val tmp = File.createTempFile("temp",".csv")
    val experiment = Experiment(
      1,
      "exp1",
      "desc1",
      10,
      Languages.mathLanguage(),
      new DataSet(tmp.getAbsolutePath, Set("varA", "varB"))
    )

    try {
      new DeterministicCrowdingEvolutionEngine(experiment).evolve()
    } catch {
      case e: Throwable => fail("Exception thrown! " + e)
    }
  }

}
