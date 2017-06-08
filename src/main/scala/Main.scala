object Main {
  def main(args: Array[String]): Unit = {
    val vikingo1 : Vikingo = Vikingo("Juan", CaracteristicaVikingo(1, 5, 5, 1), Some(Arma(100)))
    val vikingo2 : Vikingo = Vikingo("Jorge", CaracteristicaVikingo(2, 5, 3, 4), Some(Arma(10)))
    val vikingo3 : Vikingo = Vikingo("Pedro", CaracteristicaVikingo(1, 2, 1, 2), Some(ItemComestible(59)))

    val primeroTorneo = Torneo(List(vikingo1, vikingo2, vikingo3), ???, ???)

  }
}
