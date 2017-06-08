case class CaracteristicaVikingo(peso: Float, velocidad: Float, barbarosidad: Float, hambre: Float) {

}

trait ItemVikingo

case class  Arma(danio: Float) extends ItemVikingo
case class  ItemComestible(porcentaje: Float) extends ItemVikingo
case object SistemaDeVuelo extends ItemVikingo

trait Competidor

case class Vikingo(nombre: String,
                   caracteristicas: CaracteristicaVikingo,
                   item: Option[ItemVikingo] = None) extends Competidor{

  // Geters para las caracteristicas
  def peso = caracteristicas.peso
  def velocidad = caracteristicas.velocidad
  def barbarosidad = caracteristicas.barbarosidad
  def hambre = caracteristicas.hambre

  def danioTotal(): Float = {
    item match {
      case Some(Arma(danio)) => barbarosidad + danio
      case _ => barbarosidad
    }
  }

  def montar(dragon: Dragon) : Competidor = {
    Jinete(this, ???)
  }

}

case class Jinete(vikingo: Vikingo, dragon: Dragon) extends Competidor {

}