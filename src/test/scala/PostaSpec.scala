import org.scalatest.{FlatSpec, Matchers}
import torneoVikingos._
import torneoVikingos.PostaObject.{Posta, Requisito}
import torneoVikingos.TorneoObject._

class PostaSpec extends FlatSpec with Matchers {

  // Algunos vikingos
  val asger: Vikingo = Vikingo("Asger", CaracteristicaCompetidor(peso = 100, velocidad = 15, barbarosidad = 50, hambre = 30), Some(Arma(50)))
  val arvid: Vikingo = Vikingo("Arvid", CaracteristicaCompetidor(peso = 80, velocidad = 30, barbarosidad = 150, hambre = 10), None)
  val asmund: Vikingo = Vikingo("Asmund", CaracteristicaCompetidor(peso = 250, velocidad = 50, barbarosidad = 120, hambre = 98), Some(Arma(50)))

  val ragnar: Vikingo = Vikingo("Ragnar", CaracteristicaCompetidor(peso = 150, velocidad = 100, barbarosidad = 200, hambre = 70), Some(Arma(100)))
  val bjorn: Vikingo = Vikingo("Bjorn", CaracteristicaCompetidor(peso = 90, velocidad = 90, barbarosidad = 140, hambre = 90), Some(Arma(60)))
  val ivar: Vikingo = Vikingo("Ivar", CaracteristicaCompetidor(peso = 80, velocidad = 200, barbarosidad = 100, hambre = 50), None)

  // Algunos dragones

  val d1: Dragon = Dragon("Sarasa", NadderMortífero, 75, 25)
  val d2: Dragon = Dragon("Chispita", FuriaNocturna(100), 100, 15)
  val d3: Dragon = Dragon("Aaa", Gronckle(100), 80, 75)

  // Para abstraer el efecto en el hambre de las diferentes postas
  def efectoColateralEnPosta(porcentaje: Double): (Competidor => Competidor) =

    (c: Competidor) => {
      c match {
        case Vikingo(_, _, Some(ItemComestible(hambreItem))) if c.esPatapez =>
          Vikingo(nombre = c.getNombre, item = c.getItem, caracteristicas = c.aumentaHambre(2 * porcentaje - hambreItem))
        case Vikingo(_nombre, _, _item) =>
          Vikingo(nombre = _nombre, item = _item, caracteristicas = c.aumentaHambre(porcentaje))
        case Jinete(_vikingo, _dragon) =>
          Jinete(_vikingo.aumentaCaracteristicas(c.aumentaHambre(5)), dragon = _dragon) // Jinete incrementan 5% de hambre para toda posta
      }
    }

  // Pesca sin requisitos de admision, se ordenan por el que mas pescado levanta (50% peso + 2*barbarosidad) y les aumenta 5% el hambre a todos
  val pesca = Posta(
    criterioOrdenamiento = (c1, c2) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = efectoColateralEnPosta(5)
  )

  // Posta pesca con un requisito de admision de levantar 200 de peso minimo
  val pescaConRequerimiento = Posta(
    criterioOrdenamiento = (c1, c2) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = efectoColateralEnPosta(5),
    requisitosAdmision = List(c => c.pescadoPuedeLevantar > 200)
  )

  // Posta combate, se ordenan por el que mas daño produce, luego aumenta 10% de hambre
  // Debe tener al menos un grado de barbaridad mínimo o un arma equipada para participar de esta posta
  val requisitoPostaCombate: Requisito = c => c.tieneArma || c.barbarosidad > 80

  val combate = Posta(
    criterioOrdenamiento = (c1, c2) => c1.danioTotal > c2.danioTotal,
    efectoColateral = efectoColateralEnPosta(10),
    requisitosAdmision = List(requisitoPostaCombate)
  )

  // Posta carrera de 10 km, se ordenan por el mas veloz, aumentan el hambre en base a los km de carrera

  val carrera = Posta(
    criterioOrdenamiento = (c1, c2) => c1.velocidad > c2.velocidad,
    efectoColateral = efectoColateralEnPosta(10)
  )

  /**
    * TORNEOS
    **/

  /** Estandar **/

  // Los participantes eligen el dragon que mejor les sirve para la posta en orden.
  // Una vez que lo elige ya no esta disponible para los otros
  val preparacionEstandar: ReglaPreparacion = (vikingos, dragones, posta) => {

    var dragonesDisponibles = dragones
    vikingos.map(v => {
      v.mejorMontura(dragonesDisponibles, posta)() match {
        case Some(d) =>
          dragonesDisponibles = dragonesDisponibles.filter(_ != d) // Saco el dragon de la lista
          v.montar(d)
        case _ => v
      }
    })

  }

  // Clasifica la mitad de los que mejor les fue, como ya vienen ordenados por jugar la posta, es la mitad de la izq
  val clasificacionEstandar: ReglaClasificacion = competidores => competidores.take(competidores.length / 2)

  // Si quedan varios gana el que le haya ido mejor en la última
  val desempateEstandar: ReglaDesempate = _.head

  val reglasEstandar = ReglasTorneo(
    preparacionEstandar,
    clasificacionEstandar,
    desempateEstandar
  )

  val torneoEstandar = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(combate),
    List(d1, d2, d3),
    reglasEstandar
  )


  /** Eliminacion **/

  // Avanzan todos excepto los últimos N, donde N se da en la regla.
  val clasificacionEliminacion: Int => ReglaClasificacion = numeroASacar => _ dropRight numeroASacar

  val torneoEliminacion = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(carrera, pescaConRequerimiento, pesca, combate),
    List(d1, d2, d3),
    reglasEstandar.conClasificacion(clasificacionEliminacion(10))
  )


  /** Inverso **/

  // Avanza la mitad a la que peor le haya ido
  val clasificacionInverso: ReglaClasificacion = competidores => competidores.drop(competidores.length / 2)

  // Si terminan varios gana el que haya salido en último lugar en la posta final.
  val desempateInverso: ReglaDesempate = _.last

  val torneoInverso = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(carrera, pescaConRequerimiento, pesca, combate),
    List(d1, d2, d3),
    reglasEstandar.conClasificacion(clasificacionInverso).conDesempate(desempateInverso)
  )

  /** Veto de Dragones **/

  // De los dragones del torneo sólo están disponibles aquellos que cumplen cierta condición
  val preparacionVeto: (Dragon => Boolean) => ReglaPreparacion = condicion => (vikingos, dragones, posta) => {

    var dragonesDisponibles = dragones.filter(condicion)
    vikingos.map(v => {
      v.mejorMontura(dragonesDisponibles, posta)() match {
        case Some(d) =>
          dragonesDisponibles = dragonesDisponibles.filter(_ != d) // Saco el dragon de la lista
          v.montar(d)
        case _ => v
      }
    })

  }

  val torneoVeto = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(carrera, pescaConRequerimiento, pesca, combate),
    List(d1, d2, d3),
    reglasEstandar.conPreparacion(preparacionVeto(_.especie == NadderMortífero))
  )

  /** Handicap **/

  // Los jugadores eligen montura en el orden inverso (aquellos que les haya ido peor en la última posta)
  val preparacionHandicap: ReglaPreparacion = (vikingos, dragones, posta) => {

    var dragonesDisponibles = dragones
    vikingos.map(v => {
      v.mejorMontura(dragonesDisponibles, posta)(_.reverse.headOption) match {
        case Some(d) =>
          dragonesDisponibles = dragonesDisponibles.filter(_ != d) // Saco el dragon de la lista
          v.montar(d)
        case _ => v
      }
    })

  }

  val torneoHandicap = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(carrera, pescaConRequerimiento, pesca, combate),
    List(d1, d2, d3),
    reglasEstandar.conPreparacion(preparacionHandicap)
  )

  /** Por equipos **/

  val desempatePorEquipos: ReglaDesempate = {
    case equipos: List[Equipo] => equipos.maxBy(_.participantes.size)
    case _ => throw new RuntimeException("Se esperaba lista de equipos")
  }

  val torneoPorEquipos = Torneo(
    List(asger, arvid, asmund, ragnar, bjorn, ivar),
    List(carrera, pescaConRequerimiento, pesca, combate),
    List(d1, d2, d3),
    reglasEstandar.conDesempate(desempatePorEquipos)
  )

  /**
    * TESTS
    */

  "Cuando pregunte a la posta pesca quienes de los tres vikingos pueden participar" should
    "Devolver a asger y arvid porque asmud va a superar el 100% de hambre cuando termine la posta" in {
    val puedenParticipar = pesca.puedenParticipar(List(asger, arvid, asmund))
    assert(puedenParticipar == List(asger, arvid))
  }

  "Cuando pregunte a la posta pescaConRequerimiento quienes de los tres vikingos pueden participar" should
    "Devolver a arvid porque es el unico que cumple ambos requerimientos (pueden levantar mas de 200 y su hambre quedaria < 100)" in {
    val puedenParticipar = pescaConRequerimiento.puedenParticipar(List(asger, arvid, asmund))
    assert(puedenParticipar == List(arvid))
  }

  "Cuando pregunte si ragnar y bjorn pueden participar de la posta combate" should
    "devolver la lista con ambos porque los dos cumplen los requisitos de admision" in {
    val puedenParticipar = combate.puedenParticipar(List(bjorn, ragnar))
    assert(puedenParticipar == List(bjorn, ragnar))
  }

  "Cuando pregunte si ragnar es mejor que bjorn en el combate" should
    "devolver true ya que ragnar produce mas daño" in {
    val comparacionCombate = ragnar.esMejorQue(bjorn)(combate)
    assert(comparacionCombate)
  }

  "Cuando pregunte quienes puede competir en carrera con Ivar y Bjorn" should
    "devolver los dos porque ambos cumplen los requerimientos" in {
    val puedenCorrer = carrera.puedenParticipar(List(bjorn, ivar))
    assert(puedenCorrer == List(bjorn, ivar))
  }

  "Cuando pregunte si ivar es mejor que bjorn en la carrera" should
    "devolver true ya que ivar es mas veloz" in {
    val comparacionCarrera = ivar.esMejorQue(bjorn)(carrera)
    assert(comparacionCarrera)
  }

  "Cuando haga jugar la posta pesca con los tres vikingos" should
    "Devolver la lista primero con arvid que es el que mas peso levanta y despues asger (asmund no puede participar)" in {
    val clasificados = pesca.jugar(List(asger, arvid, asmund))
    assert(clasificados.head.getNombre == "Arvid")
  }

  "Cuando haga jugar la posta pesca arvid (ganador por el test de arriba)" should
    "Tener 10.5 de hambre porque se aumento un 5% su valor anterior (10)" in {
    val clasificados = pesca.jugar(List(asger, arvid, asmund))
    assert(clasificados.head.hambre == 10.5)
  }

  "Cuando haga jugar la posta combate" should
    "ragnar tener 77 de hambre porque se aumento un 10% su valor anterior (70)" in {
    val losLothbrok = combate.jugar(List(ragnar, ivar, bjorn))
    assert(losLothbrok.head.hambre == 77)
  }

  "Cuando haga jugar la posta combate" should
    "ivar tener 70 de hambre porque se aumento un 10% su valor anterior (50)" in {
    val losLothbrok = carrera.jugar(List(ivar, ragnar, bjorn))
    assert(losLothbrok.head.hambre == 55)
  }

  "Cuando pregunte la mejor montura para Ragnar en la posta carrera con los dragones d1, d2, y d3" should
    "devolver d2 porque es el que lo hace mas rapido" in {
    val mejorMontura = ragnar.mejorMontura(List(d1, d2, d3), carrera)()
    assert(mejorMontura.get == d2)
  }

  "Cuando se juege un torneo estandar con los parametros seteados anteriormente" should
    "devolver a Arvid" in {

    val participanteGanadorEstandar = torneoEstandar.jugar.get.asInstanceOf[Vikingo]
    participanteGanadorEstandar.nombre.shouldEqual("Arvid")

  }

//  "Cuando se juege un torneo estandar con los parametros seteados anteriormente" should
//    "devolver a alguien que no sabemos todavia"
//
//    torneoInverso.jugar.shouldEqual(None)
//
//  "Cuando se juege un torneo estandar con los parametros seteados anteriormente" should
//    "devolver a alguien que no sabemos todavia"
//
//    torneoVeto.jugar.shouldEqual(None)
//
//  "Cuando se juege un torneo estandar con los parametros seteados anteriormente" should
//    "devolver a alguien que no sabemos todavia"
//
//    torneoHandicap.jugar.shouldEqual(None)

//  "Cuando se juege un torneo estandar con los parametros seteados anteriormente" should
//    s"gano el equipo ${equipoGanador.get.asInstanceOf[Equipo].nombre}"
//
//    val equipoGanador = torneoPorEquipos.jugar
//    equipoGanador.get.asInstanceOf[Equipo].nombre.shouldEqual("TUVIEJA")

}
