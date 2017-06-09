package torneoVikingos

sealed trait EspecieDragon

case class FuriaNocturna(danio: Double) extends EspecieDragon
case object NadderMortífero extends EspecieDragon
case class Gronckle(peso: Double) extends EspecieDragon

case class Dragon(especie: EspecieDragon, velocidadBase: Double = 60, peso: Double,
                  requisitos: List[(Vikingo) => Boolean] = List()) {

  require(velocidadBase - peso > 0, "El peso no puede superar la velocidad base")

  def velocidadInicial = velocidadBase - peso

  def capacidadCarga = peso * 0.2

  def velocidad: Double = {
    especie match {
      case FuriaNocturna(_) => velocidadInicial * 3
      case Gronckle(_) => velocidadInicial / 2
      case _ => velocidadInicial
    }
  }

  def danio: Double = {
    especie match {
      case FuriaNocturna(danio) => danio
      case NadderMortífero => 150
      case Gronckle(pesoSoporta) => pesoSoporta * 5
    }
  }

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = {

    if (vikingo.peso >= capacidadCarga && requisitos.exists(requisito => !requisito(vikingo)))
      return false

    especie match {
        case NadderMortífero => danio > vikingo.danioTotal
        case Gronckle(pesoSoporta) => pesoSoporta > vikingo.peso
        case _ => true
    }
  }

}