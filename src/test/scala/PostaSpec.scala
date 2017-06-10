import org.scalatest.{FlatSpec, Matchers}
import torneoVikingos._
import torneoVikingos.Posta2.Posta

class PostaSpec extends FlatSpec with Matchers {

  // Algunos vikingos
  val asger = Vikingo("Asger", CaracteristicaCompetidor(peso=100, velocidad=15, barbarosidad=50, hambre=30), Some(Arma(50)))
  val arvid = Vikingo("Arvid", CaracteristicaCompetidor(peso=80, velocidad=30, barbarosidad=150, hambre=10), None)
  val asmund = Vikingo("Asmund", CaracteristicaCompetidor(peso=250, velocidad=50, barbarosidad= 120, hambre=98), Some(Arma(50)))

  // Pesca sin requisitos de admision, se ordenan por el que mas pescado levanta (50% peso + 2*barbarosidad) y les aumenta 5% el hambre a todos
  val pesca = Posta(
    criterioOrdenamiento = (c1: Competidor, c2: Competidor) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = (c: Competidor) => {
      c match {
        case Vikingo(_nombre, _caract, _item) => Vikingo(
          nombre = _nombre, item = _item, caracteristicas = _caract.copy(hambre = _caract.hambre * 1.1)
        )
        case Jinete(_vikingo, _dragon) =>
          Jinete(_vikingo.aumentaCaracteristicas(_vikingo.aumentaHambre(10)), dragon = _dragon)
      }
    }
  )

  // Posta pesca con un requisito de admision de levantar 200 de peso minimo
  val pescaConRequerimiento = Posta(
    criterioOrdenamiento = (c1, c2) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = (c: Competidor) => {
      c match {
        case Vikingo(_nombre, _caract, _item) => Vikingo(
          nombre = _nombre, item = _item, caracteristicas = _caract.copy(hambre = _caract.hambre * 1.1)
        )
        case Jinete(_vikingo, _dragon) =>
          Jinete(_vikingo.aumentaCaracteristicas(_vikingo.aumentaHambre(10)), dragon = _dragon)
      }
    },
    requisitosAdmision = List((c) => c.pescadoPuedeLevantar > 200 )
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

  "Cuando haga jugar la posta pesca con los tres vikingos" should
    "Devolver la lista primero con arvid que es el que mas peso levanta y despues asger (asmund no puede participar)" in {
    val clasificados = pesca.jugar(List(asger, arvid, asmund))
    assert(clasificados.head.getNombre == "Arvid")
  }

  "Cuando haga jugar la posta pesca arvid (ganador por el test de arriba)" should
    "Tener 11 de hambre porque se aumento un 10% su valor anterior (10)" in {
    val clasificados = pesca.jugar(List(asger, arvid, asmund))
    assert(clasificados.head.hambre == 11)
  }

}
