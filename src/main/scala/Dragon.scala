sealed trait EspecieDragon

case class FuriaNocturna(danio: Double) extends EspecieDragon
case object NadderMortífero extends EspecieDragon
case object Gronckle extends EspecieDragon

case class Dragon(especie: EspecieDragon, velocidadBase: Double = 60, peso: Double) {

  require(velocidadBase - peso > 0, "El peso no puede superar la velocidad base")

  def velocidadInicial = velocidadBase - peso

  def velocidad: Double = {
    especie match {
        case FuriaNocturna(_) => velocidadInicial * 3
        case Gronckle => velocidadInicial / 2
        case _ => velocidadInicial
    }
  }

  def danio: Double = {
    especie match {
        case FuriaNocturna(danio) => danio
        case NadderMortífero => 150
        case Gronckle => peso * 5
    }
  }
}

// TODO: definir como un vikingo monta a un dragon !!


