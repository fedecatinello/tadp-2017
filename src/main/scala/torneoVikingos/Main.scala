package torneoVikingos

import torneoVikingos.Utils.{ListListVikingo, ListVikingo}

object Main {
  def main(args: Array[String]): Unit = {

    val asger = Vikingo("asger", CaracteristicaCompetidor(100, 15, 80, 50), Some(Arma(50)))
    val arvid = Vikingo("arvid", CaracteristicaCompetidor(80, 30, 80, 50), Some(Arma(50)))

    var participantes = List(arvid,asger)

    val lista = participantes match {
      case ListVikingo(_) => println("lista de vikingos")
      case ListVikingo(x) :: xs => println("lista de listas")
      case _ => throw new RuntimeException("No me cabe una")
    }

  }
}

object Utils {

  abstract class ListExtractor[A](implicit ct: reflect.ClassTag[A]) {
    def unapply(o: Any): Option[List[A]] = o match {
      case lst: List[_] if lst.forall(ct.unapply(_).isDefined) =>
        Some(lst.asInstanceOf[List[A]])
      case _ => None
    }
  }

  object ListVikingo extends ListExtractor[Vikingo]
  object ListListVikingo extends ListExtractor[ListExtractor[Vikingo]]

}
