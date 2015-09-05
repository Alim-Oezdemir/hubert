package pl.edu.agh.hubert.servlets

import pl.edu.agh.hubert.engine.EvolutionExecutor
import pl.edu.agh.hubert.engine.EvolutionTaskProtocol._
import pl.edu.agh.hubert.experiments.ExperimentProtocol._
import pl.edu.agh.hubert.experiments.{Experiment, ExperimentRepository}
import spray.json._

class ExperimentsServlet(
                          val evolutionExecutor: EvolutionExecutor,
                          val experimentRepository: ExperimentRepository
                          ) extends LoggingServlet {

  get("*") {
    contentType = "application/json"
    
    JsArray(experimentRepository
      .listExperimentExecutions()
      .map(e => e.toJson)
      .toVector
    )
  }

  post("/run") {
    contentType = "application/json"

    val experiment = request.body.parseJson.convertTo[Experiment]

    logger.info("running new experiment: " + experiment)

    val evolutionTask = experimentRepository.recordExperiment(experiment)
    evolutionExecutor.addTask(evolutionTask)

    "{ \"status\": \"started\" }"
  }

  post("/upload") {
    logger.info("uploading new experiment: " + request.body)
  }

  error {
    case t: Throwable => logger.error("Exception while handling request!", t)
  }

}