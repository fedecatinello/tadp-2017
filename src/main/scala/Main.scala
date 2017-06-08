object Main {
  def main(args: Array[String]): Unit = {
    val hipo : Vikingo = Vikingo("Juan", CaracteristicaVikingo(1, 5, 5, 1), Some(Arma(100)))
    val astrid : Vikingo = Vikingo("Jorge", CaracteristicaVikingo(2, 5, 3, 4), Some(Arma(10)))
    val patan : Vikingo = Vikingo("Pedro", CaracteristicaVikingo(1, 2, 1, 2), Some(ItemComestible(59)))

    val primeroTorneo = Torneo(List(hipo, astrid, patan), ???, ???)

    //Creando dragones
    val dragonNocturno = Dragon(FuriaNocturna(0), peso = 50)

    val dragonNadderMortífero = Dragon(NadderMortífero, peso = 50)
    val dragonGronckle = Dragon(Gronckle, peso = 50)

    val competidor1 : Competidor =
      Jinete("Caballero del Zodiaco", CaracteristicaVikingo(1,1,1,1), Some(Arma(10)), dragonGronckle)

  }
}
