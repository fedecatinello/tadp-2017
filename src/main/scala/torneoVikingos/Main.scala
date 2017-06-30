package torneoVikingos

object Main {
  def main(args: Array[String]): Unit = {

    val asger = Vikingo("asger", CaracteristicaCompetidor(100, 15, 80, 50), Some(Arma(50)))
    val arvid = Vikingo("arvid", CaracteristicaCompetidor(80, 30, 80, 50), Some(Arma(50)))

    var participantes = List(arvid,asger)

  }
}
