package torneoVikingos

import torneoVikingos.Posta.Posta
import scala.util.{Try, Success, Failure}

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
  def getItem = item

  // Setters para las caracteristicas

  def aumentaHambre(porcentaje: Double) = caracteristicas.copy(hambre = hambre + (hambre * porcentaje)/100)

  def esPatapez: Boolean = this.nombre == "Patapez" && this.item.contains(ItemComestible(_))

  def danioTotal: Double = {
    item match {
      case Some(Arma(danio)) => barbarosidad + danio
      case _ => barbarosidad
    }
  }

  def pescadoPuedeLevantar: Double = (peso * .5) + (barbarosidad * 2)

  // Punto 2
  def esMejorQue(competidor: Competidor)(posta: Posta) : Boolean = {
    val participantesOrdenados = posta.ordenar(List(this, competidor))
    participantesOrdenados match {
      case Nil => return false
      case x :: _ => return x == this
    }
  }

  def tieneArma: Boolean = {
    item match {
      case Some(Arma(_)) => true
      case _ => false
    }
  }
}

case class Vikingo(nombre: String, caracteristicas: CaracteristicaCompetidor, item: Option[ItemCompetidor] = None)
  extends Competidor(nombre, caracteristicas, item) {

  def aumentaCaracteristicas(criterioAumento: => CaracteristicaCompetidor): Vikingo = {
    copy(caracteristicas = criterioAumento)
  }

  // Punto 1
  def montar(dragon: Dragon): Jinete = {
    if(dragon puedeSerMontadoPor this)
      Jinete(this, dragon)
    else
      throw new IllegalStateException("No puede montar a este dragon")
  }

  // Punto 3 Nico F

  def mejorMontura(dragones: List[Dragon], posta: Posta): Option[Dragon] = {
    val dragonesQuePuedeMontar: List[Dragon] = dragones.filter(_ puedeSerMontadoPor this)
    val posibilidadesComoJinete: List[Jinete] = dragonesQuePuedeMontar.map(montar)

    posta.ordenar(this :: posibilidadesComoJinete).head match {
      case Jinete(_, d) => Some(d)
      case _ => None
    }
  }

  // TODO: Version Ivi y Fede

//  def mejorPerformance(dragones: List[Dragon], posta: Posta): Option[Competidor] = {
//
//    Try(posta.jugar(this :: dragones.map(montar))) match {
//      case Success(listaParticipantes) => listaParticipantes.headOption
//      case Failure(_) => None
//    }
//  }
//
//  def mejorMontura(dragones: List[Dragon], posta: Posta): Option[Dragon] = {
//
//    mejorPerformance(dragones, posta) match {
//      case Some(jinete@Jinete(_,_)) => Some(jinete.dragon)
//      case _ => None
//    }
//  }

}

case class Jinete(vikingo: Vikingo, dragon: Dragon)
  extends Competidor(vikingo.nombre, vikingo.caracteristicas, vikingo.item) {

  override def danioTotal = super.danioTotal + dragon.danio

  override def velocidad = dragon.velocidad - peso

  def capacidadCarga = peso - dragon.capacidadCarga

}