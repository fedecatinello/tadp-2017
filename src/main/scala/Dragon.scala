import Requisitos.RequisitoMontura

sealed trait EspecieDragon

case class FuriaNocturna(danio: Double) extends EspecieDragon
case object NadderMortífero extends EspecieDragon
case object Gronckle extends EspecieDragon

object Requisitos {

  type RequisitoMontura = ((Dragon, Competidor) => Boolean)

  case class RequisitoGronckle(peso: Double) extends RequisitoMontura {
    def apply(competidor: Competidor) = {
      peso > competidor.peso
    }
  }

  case object RequisitoNadder extends RequisitoMontura {
    def apply(dragon: Dragon, competidor: Competidor) = {
      dragon.danio > competidor.danioTotal
    }
  }

}

case class Dragon(especie: EspecieDragon, velocidadBase: Double = 60, peso: Double,
                  requisitos: List[RequisitoMontura] = List()) {

  require(velocidadBase - peso > 0, "El peso no puede superar la velocidad base")

  def velocidadInicial = velocidadBase - peso

  def capacidadCarga = peso * 0.2

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

  def puedeSerMontadoPor(competidor: Competidor): Boolean = {

    if (competidor.peso >= capacidadCarga && requisitos.exists(requisito => !requisito(competidor)))
      return false

    (especie,requisitos) match {
        case (FuriaNocturna(_), [:]) =>
        case NadderMortífero =>
    }
  }

//  object cumple {
//    def unapply(dragon: Dragon, competidor: Competidor) : Boolean = {
//      dragon.requisitos.
//    }
  }

}