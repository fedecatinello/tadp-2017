package torneoVikingos

import torneoVikingos.Posta2.Posta

sealed trait ItemCompetidor

case class  Arma(danio: Double) extends ItemCompetidor
case class  ItemComestible(porcentaje: Double) extends ItemCompetidor
case object SistemaDeVuelo extends ItemCompetidor

case class CaracteristicaCompetidor(peso: Double, velocidad: Double, barbarosidad: Double, hambre: Double) {
  require(peso > 0)
  require(velocidad > 0)
  require(barbarosidad > 0)
  require(hambre > 0)
}

abstract class Competidor(nombre: String, caracteristicas: CaracteristicaCompetidor, item: Option[ItemCompetidor] = None) {

  // Geters para las caracteristicas

  def peso = caracteristicas.peso
  def velocidad = caracteristicas.velocidad
  def barbarosidad = caracteristicas.barbarosidad
  def hambre = caracteristicas.hambre
  def getNombre = nombre


  // Setters para las caracteristicas

  def aumentaHambre(porcentaje: Double) = caracteristicas.copy(hambre = hambre + (hambre * porcentaje)/100)


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
  def esMejorQue(competidor: Competidor)(posta: Posta) : Boolean = {
    posta.ordenar(List(this, competidor)) match {
      case List() => false // TODO: Preguntar ¿Si ninguno de los dos puede participar de la posta es false?
      case List(c1: Competidor, c2: Competidor) => c1 == this
    }
    true
  }

}

case class Vikingo(nombre: String, caracteristicas: CaracteristicaCompetidor, item: Option[ItemCompetidor] = None)
  extends Competidor(nombre, caracteristicas, item) {

  def aumentaCaracteristicas(criterioAumento: => CaracteristicaCompetidor): Vikingo = {
    copy(caracteristicas = criterioAumento)
  }

  // Punto 1 TODO: ver que onda el tema de Try aca (esto no implica puedeMontar(), sino que monta() y si no puede, rompe)
  def montar(dragon: Dragon): Competidor = {
    if(dragon.puedeSerMontadoPor(this))
      Jinete(this, dragon)
    else
      this
  }

}

case class Jinete(vikingo: Vikingo, dragon: Dragon)
  extends Competidor(vikingo.nombre, vikingo.caracteristicas, vikingo.item) {

  override def danioTotal = super.danioTotal() + dragon.danio

  override def velocidad = dragon.velocidad - peso

  def capacidadCarga = peso - dragon.capacidadCarga

}


