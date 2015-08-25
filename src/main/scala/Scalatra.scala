import javax.servlet.ServletContext

import org.scalatra.LifeCycle
import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.configuration.Configuration
import pl.edu.agh.hubert.engine.EvolutionExecutor
import pl.edu.agh.hubert.servlets._

class ScalatraBootstrap extends LifeCycle {

  val logger =  LoggerFactory.getLogger(getClass)

  override def init(context: ServletContext) {

    logger.info("Initializing Scalatra")
    val evolutionExecutor = new EvolutionExecutor(
      Configuration.threads,
      Configuration.taskWaitTime
    )
    
    logger.info("Servlets")
    context mount (new ExperimentsServlet(evolutionExecutor), "/experiments")
    context mount (new DatasetsServlet, "/datasets")
    context mount (new SystemConfigServlet, "/config")
  }
}