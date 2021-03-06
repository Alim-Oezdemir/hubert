package pl.edu.agh.hubert.engine

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.engine.ExperimentProtocol._
import spray.json._

class ExperimentTest extends FunSuite {

  val experiment = new Experiment(
    1,
    "name of experiment",
    "description",
    10,
    Languages.mathLanguage(),
    DataSet("/some/path", Set("varA", "varB")),
    fitnessFunction = "fitnessFunction"
  )

  test("serialize and deserialize experiment") {
    val experimentAsJson = experiment.toJson.toString()
    val deserializedExperiment = experimentAsJson.parseJson.convertTo[Experiment]

    assert(experiment.name == deserializedExperiment.name)
    assert(experiment.description == deserializedExperiment.description)

    // TODO: learn how to implement equals in scala and fix the test!
    assert(experiment.language == deserializedExperiment.language)
    assert(experiment.id == deserializedExperiment.id)
  }

}
