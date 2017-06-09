package torneoVikingos

import torneoVikingos.Posta.ActividadPosta

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

  def getNombre = nombre

  def danioTotal(): Double = {
    item match {
      case Some(Arma(danio)) => barbarosidad + danio
      case _ => barbarosidad
    }
  }

  def pescadoPuedeLevantar() : Double = {
    (peso * .5) + (barbarosidad * 2)
  }

  // Punto 2
  def esMejorQue(competidor: Competidor)(posta: ActividadPosta) : Boolean = {
    posta(List(this, competidor)).head == this
  }

}

case class Vikingo(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None)
  extends Competidor(nombre, caracteristicas, item) {

  // Punto 1
  def montar(dragon: Dragon): Competidor = {
    if(dragon.puedeSerMontadoPor(this))
      Jinete(nombre, caracteristicas, item, dragon)
    else
      this
  }

}

case class Jinete(nombre: String, caracteristicas: CaracteristicaVikingo, item: Option[ItemVikingo] = None, dragon: Dragon)
  extends Competidor(nombre, caracteristicas, item) {

  override def danioTotal = super.danioTotal() + dragon.danio

  override def velocidad = dragon.velocidad - peso

  def capacidadCarga = peso - dragon.capacidadCarga

}


