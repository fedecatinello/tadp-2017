package torneoVikingos

import torneoVikingos.Posta2.Posta
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


  // Setters para las caracteristicas

  def aumentaHambre(porcentaje: Double) = caracteristicas.copy(hambre = hambre + (hambre * porcentaje)/100)


  def danioTotal: Double = {
    item match {
      case Some(Arma(danio)) => barbarosidad + danio
      case _ => barbarosidad
    }
  }

  def pescadoPuedeLevantar : Double = {
    (peso * .5) + (barbarosidad * 2)
  }

  // Punto 2
  def esMejorQue(competidor: Competidor)(posta: Posta) : Boolean = {
    posta.ordenar(List(this, competidor)) match {
      case Nil => return false // TODO: Preguntar ¿Si ninguno de los dos puede participar de la posta es false?
      case List(c1: Competidor, _) => return c1 == this
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
      throw new IllegalStateException("no puede montar a este dragon")
  }

  //Punto 3
  def mejorMontura(dragones: List[Dragon], posta: Posta): Option[Competidor]= {
    if (posta.puedenParticipar(List(this)).isEmpty)
      throw new IllegalStateException("no puede participar en posta")

    val dragonesPosibles = dragones.filter(d => d.puedeSerMontadoPor(this))
    val monturasPosibles = this ::  dragonesPosibles.map(d => this.montar(d))
    monturasPosibles.sortWith(posta.criterioOrdenamiento).headOption
  }

  // ME QUEDA LA DUDA SI EL METODO puedeSerMontadoPor ES VALIDO... SI NO LO ES, SE ME OCURRE
  // HACER ALGO COMO LO QUE ESTOY HACIENDO ABAJO... ME PARECE MUY TIRADO DE LOS PELOS
  /*
  def trySortWith[T]( fc: (T, T) => Boolean, listaAOrdenar: List[T]): List[T] = {
    var auxList = List()
    listaAOrdenar.foreach{v =>
      v match {
        case Success(s) => auxList += s
      }
    }

    /*
    * ACA APLICAR EL FILTRADO NORMAL
    * */
  }

  */
  /*
  // FORMA DE USO
  * val vikngo = Vikingo()
  * val listaDragones = ???
  * val posta: Posta = ???
  * Try(vikingo.mejorMontura(listaDragones, posta)) match {
  *   case Success(jineteORvikingo) => jineteORvikingo
  *   case Failure(e) => print(e.getMesage())
  * }
  * */
}

case class Jinete(vikingo: Vikingo, dragon: Dragon)
  extends Competidor(vikingo.nombre, vikingo.caracteristicas, vikingo.item) {

  override def danioTotal = super.danioTotal + dragon.danio

  override def velocidad = dragon.velocidad - peso

  def capacidadCarga = peso - dragon.capacidadCarga

}


