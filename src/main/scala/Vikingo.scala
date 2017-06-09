sealed trait ItemVikingo

case class  Arma(danio: Double) extends ItemVikingo
case class  ItemComestible(porcentaje: Double) extends ItemVikingo
case object SistemaDeVuelo extends ItemVikingo

case class CaracteristicaVikingo(peso: Double, velocidad: Double, barbarosidad: Double, hambre: Double) {
  require(peso > 0)
  require(velocidad > 0)
  require(barbarosidad > 0)
  require(hambre > 0)
}

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

  // Punto 1
  def montar(dragon: Dragon): Competidor = {
    if(dragon.puedeSerMontadoPor(this))
      Jinete(nombre, caracteristicas, item, dragon)
    else
      this
  }

//  def participar(posta: Posta): (Competidor, Double /* puntaje posta */) = {
//    if (puedeParticipar())
//      posta(this)
//    else
//      (this, -1)
//  }
//
//  def puedeParticipar(): Boolean = {
//    if (hambre >= 100) false
//
//    objeto match {
//      case Some(ItemComestible(_)) => hambre <= 50
//      case _ => true
//    }
//  }

}

case class Vikingo(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None)
  extends Competidor(nombre, caracteristicas, item)

case class Jinete(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None, dragon: Dragon)
  extends Competidor(nombre, caracteristicas, item) {

  override def danioTotal = super.danioTotal() + dragon.danio

  override def velocidad = dragon.velocidad - peso

  def capacidadCarga = peso - dragon.capacidadCarga

}


