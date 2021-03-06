package pl.edu.agh.hubert.datasets

import spray.json.DefaultJsonProtocol

case class DataSet(
                    path: String,
                    variables: Set[String]
                    )


object DataSetProtocol extends DefaultJsonProtocol {

  implicit val dataSetJsonFormat = jsonFormat2(DataSet)

}