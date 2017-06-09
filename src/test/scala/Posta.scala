import org.scalatest.{FlatSpec, Matchers}
import torneoVikingos._

class PostaSpec extends FlatSpec with Matchers {

  // Algunos vikingos
  val asger = Vikingo("Asger", CaracteristicaVikingo(peso=100, velocidad=15, barbarosidad=50, hambre=30), Some(Arma(50)))
  val arvid = Vikingo("Arvid", CaracteristicaVikingo(peso=80, velocidad=30, barbarosidad=150, hambre=10), None)
  val asmund = Vikingo("Asmund", CaracteristicaVikingo(peso=250, velocidad=50, barbarosidad= 120, hambre=175), Some(Arma(50)))

  // Pesca no tiene requisitos de admision, se ordenan por el que mas pescado levanta (50% peso + 2*barbarosidad) y les aumenta 5% el hambre a todos
  val pesca = Posta2(
    criterioOrdenamiento = (c1: Competidor, c2: Competidor) => c1.pescadoPuedeLevantar > c2.pescadoPuedeLevantar,
    efectoColateral = (c: Competidor) => {
      c match {
        case Vikingo(_nombre, _caract, _item) => Vikingo(
          nombre = _nombre, item = _item, caracteristicas = _caract.copy(hambre = _caract.hambre * 1.1)
        )
        case Jinete(_nombre, _caract, _item, _dragon) => Jinete(
          nombre = _nombre, item = _item, caracteristicas = _caract.copy(hambre = _caract.hambre * 1.1), dragon = _dragon
        )
      }
    }
  )

  "Cuando pregunte a la posta pesca quienes de los tres vikingos pueden participar" should
    "Devolver a asger y arvid porque asmud supera el 100% de hambre" in {
    val puedenParticipar = pesca.puedenParticipar(List(asger, arvid, asmund))
    assert(puedenParticipar == List(asger, arvid))
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
