package pl.edu.agh.hubert.experiments

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.engine.EvolutionTask
import pl.edu.agh.hubert.languages.Languages

class MemoryExperimentRepositoryTest extends FunSuite {
  val experiment = new Experiment(1, "Test", "some experiment", 1, Languages.mathLanguage(), new DataSet("/some", Set()))

  test("should add experiment") {
    val repo = new MemoryExperimentRepository

    val evolutionTask = repo.recordExperiment(experiment)
    val experiments = repo.listExperimentExecutions()
    assert(experiments.contains(evolutionTask))
  }

  test("should return EvolutionTask") {
    val repo = new MemoryExperimentRepository

    assert(repo.recordExperiment(experiment) != null)
  }
}