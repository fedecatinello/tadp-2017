trait ItemVikingo

case class  Arma(danio: Double) extends ItemVikingo
case class  ItemComestible(porcentaje: Double) extends ItemVikingo
case object SistemaDeVuelo extends ItemVikingo

case class CaracteristicaVikingo(peso: Double, velocidad: Double, barbarosidad: Double, hambre: Double)

abstract class Competidor(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None) {

  // Geters para las caracteristicas

  def peso = caracteristicas.peso
  def velocidad = caracteristicas.velocidad
  def barbarosidad = caracteristicas.barbarosidad
  def hambre = caracteristicas.hambre

  def danioTotal(): Double = {
    item match {
      case Some(Arma(danio)) => barbarosidad + danio
      case _ => barbarosidad
    }
  }

}

case class Vikingo(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None)
  extends Competidor(nombre, caracteristicas, item)

case class Jinete(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None, dragon: Dragon)
  extends Competidor(nombre, caracteristicas, item) {

  override def danioTotal(): Double = super.danioTotal() + dragon.danio

}


