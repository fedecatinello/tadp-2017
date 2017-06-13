import org.scalatest.{FlatSpec, Matchers}
import torneoVikingos._
import torneoVikingos.Posta2.Posta

class PostaSpec extends FlatSpec with Matchers {

  // Algunos vikingos
  val asger = Vikingo("Asger", CaracteristicaCompetidor(peso=100, velocidad=15, barbarosidad=50, hambre=30), Some(Arma(50)))
  val arvid = Vikingo("Arvid", CaracteristicaCompetidor(peso=80, velocidad=30, barbarosidad=150, hambre=10), None)
  val asmund = Vikingo("Asmund", CaracteristicaCompetidor(peso=250, velocidad=50, barbarosidad= 120, hambre=98), Some(Arma(50)))

  val ragnar = Vikingo("Ragnar", CaracteristicaCompetidor(peso=150, velocidad=100, barbarosidad= 200, hambre=70), Some(Arma(100)))
  val bjorn = Vikingo("Bjorn", CaracteristicaCompetidor(peso=90, velocidad=90, barbarosidad= 140, hambre=90), Some(Arma(60)))
  val ivar = Vikingo("Ivar", CaracteristicaCompetidor(peso=80, velocidad=200, barbarosidad= 100, hambre=50), None)


  // Para abstraer el efecto en el hambre de las diferentes postas
  def efectoColateralEnPosta(porcentaje: Double): (Competidor => Competidor) =

    (c: Competidor) => {
      c match {
        case Vikingo(_nombre, _, _item) =>
          Vikingo(nombre = _nombre, item = _item, caracteristicas = c.aumentaHambre(porcentaje))
        case Jinete(_vikingo, _dragon) =>
          Jinete(_vikingo.aumentaCaracteristicas(c.aumentaHambre(porcentaje)), dragon = _dragon)
      }

  }

  // Pesca sin requisitos de admision, se ordenan por el que mas pescado levanta (50% peso + 2*barbarosidad) y les aumenta 5% el hambre a todos
  val pesca = Posta(
    criterioOrdenamiento = (c1: Competidor, c2: Competidor) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = efectoColateralEnPosta(5)
  )

  // Posta pesca con un requisito de admision de levantar 200 de peso minimo
  val pescaConRequerimiento = Posta(
    criterioOrdenamiento = (c1, c2) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = efectoColateralEnPosta(5),
    requisitosAdmision = List((c) => c.pescadoPuedeLevantar > 200)
  )

  // Posta combate, se ordenan por el que mas daño produce, luego aumenta 10% de hambre
  val combate = Posta(
    criterioOrdenamiento = (c1,c2) => c1.danioTotal > c2.danioTotal,
    efectoColateral = efectoColateralEnPosta(10)
  )

  // Posta carrera de 10 km, se ordenan por el mas veloz, aumentan el hambre en base a los km de carrera
  val carrera = Posta(
    criterioOrdenamiento = (c1, c2) => c1.velocidad > c2.velocidad,
    efectoColateral = efectoColateralEnPosta(10)
  )

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

  "Cuando pregunte si ragnar es mejor que bjorn en el combate" should
    "devolver true ya que ragnar produce mas daño" in {
    val comparacionCombate = ragnar.esMejorQue(bjorn)(combate)
    assert(comparacionCombate)
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
    val losLothbrok = combate.jugar(List(ragnar,ivar,bjorn))
    assert(losLothbrok.head.hambre == 77)
  }

  "Cuando haga jugar la posta combate" should
    "ivar tener 70 de hambre porque se aumento un 10% su valor anterior (50)" in {
    val losLothbrok = carrera.jugar(List(ivar,ragnar,bjorn))
    assert(losLothbrok.head.hambre == 55)
  }

}
