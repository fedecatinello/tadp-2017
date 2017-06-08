case class CaracteristicaVikingo(peso: Float, velocidad: Float, barbarosidad: Float, hambre: Float) {

}

trait ItemVikingo

case class  Arma(danio: Float) extends ItemVikingo
case class  ItemComestible(porcentaje: Float) extends ItemVikingo
case object SistemaDeVuelo extends ItemVikingo

case class Vikingo(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None) {

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

}
