package torneoVikingos

sealed trait EspecieDragon

case class FuriaNocturna(danio: Double) extends EspecieDragon
case object NadderMortífero extends EspecieDragon
case class Gronckle(pesoSoporta: Double) extends EspecieDragon

case class Dragon(nombre: String, especie: EspecieDragon, velocidadBase: Double = 60, peso: Double,
                  requisitos: List[Vikingo => Boolean] = Nil) {

  require(velocidadBase - peso > 0, "El peso no puede superar la velocidad base")

  def velocidadInicial: Double = velocidadBase - peso
  def capacidadCarga: Double = peso * 0.2

  val requisitoFijo: Vikingo => Boolean = v => v.peso > capacidadCarga

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

    val requisitosDragon = requisitoFijo :: requisitos

    if (requisitosDragon.forall(r => r(vikingo)))
      especie match {
          case NadderMortífero => danio > vikingo.danioTotal
          case Gronckle(pesoSoporta) => pesoSoporta > vikingo.peso
          case _ => true
      }
    else
      false
  }

}