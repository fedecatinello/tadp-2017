package torneoVikingos
import torneoVikingos.Posta.Posta
import torneoVikingos.Utils.ListVikingo

import scala.util.{Failure, Success, Try}
import torneoVikingos._

object Torneo {

  // TODO: PROPUESTA PARA PODER TENER EQUIPOS EN EL TORNEO
//  case class  UnidadCompetidora[T](integrantes: List[T])// puede ser 1 vikingo o un EQUIPO de vikingos
//
//  case class ReglasTorneo(
//                           preparacion: (List[UnidadCompetidora[Vikingo]], List[Dragon], Posta) => List[UnidadCompetidora[Competidor]],
//                           clasificacion: List[UnidadCompetidora[Competidor]] => List[UnidadCompetidora[Vikingo]],
//                           desempate: List[UnidadCompetidora[Vikingo]] => Vikingo
//                         )

  type ReglaPreparacion = (List[Vikingo], List[Dragon], Posta) => List[Competidor]
  type ReglaClasificacion = List[Competidor] => List[Competidor]
  type ReglaDesempate = List[Vikingo] => Vikingo

  case class ReglasTorneo(
                          preparacion: ReglaPreparacion,
                          clasificacion: ReglaClasificacion,
                          desempate: ReglaDesempate
                         ) {}

  case class Torneo(
                     participantes: List[Vikingo],
                     postas: List[Posta],
                     dragonesDisponibles: List[Dragon],
                     reglasTorneo: ReglasTorneo
                   ) {

    def desmontarCompetidores(competidores: List[Competidor]): List[Vikingo] = {
      competidores.map {
        case Jinete(v, _) => v
        case vik@Vikingo(_, _, _) => vik
      }
    }

    def jugar: Option[Vikingo] = {

     val lista = participantes match {
      case ListVikingo(_) => println("lista de vikingos")
      case ListVikingo(x) :: xs => println("lista de listas")
      case _ => throw new RuntimeException("No me cabe una")
      }

      //TODO: terminar

      // Los que pasen todas las postas (puede ser una lista vacia en ese caso ninguno supero todas las postas)
      val sobrevivientes: List[Vikingo] = postas.foldLeft(participantes) {
        (clasificados, posta) => {
          val competidores = reglasTorneo.preparacion(clasificados, dragonesDisponibles, posta)
          val competidoresOrdenados = posta.jugar(competidores)
          val ganadoresDeLaPosta = reglasTorneo.clasificacion(competidoresOrdenados)
          desmontarCompetidores(ganadoresDeLaPosta)
        }
      }

      sobrevivientes match {
        case Nil => None
        case g :: Nil => Some(g) // Si hay un solo elemento en la lista es el ganador
        case _ => Some(reglasTorneo.desempate(sobrevivientes))
      }

    }

  }
}
