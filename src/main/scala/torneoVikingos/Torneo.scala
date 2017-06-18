package torneoVikingos
import torneoVikingos.Posta.Posta
import scala.util.{Try, Success, Failure}
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
        case Vikingo(n, caract, i) => Vikingo(n, caract, i)
      }
    }

    def jugar: Option[Vikingo] = {

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
        case List(g) => Some(g) // Si hay un solo elemento en la lista es el ganador
        case _ => Some(reglasTorneo.desempate(sobrevivientes))
      }

    }

//    def jugar: Option[Vikingo] = {
//
//      val sobrevivientes = postas.foldLeft(participantes){
//        (clasificados: List[Vikingo], posta: Posta) => {
//
//          /*En caso de que quede solo un participante, este es el
//          ganador del torneo y no se juegan más postas*/
//          if (clasificados.size == 1) {
//            clasificados
//          }
//
//          val participantesListos = reglasTorneo.preparacion(participantes, dragonesDisponibles, posta)
//          val participantesLuegoDePosta = posta.jugar(participantesListos)
//          val ganadores = reglasTorneo.clasificacion(participantesLuegoDePosta)
//          ganadores
//        }
//      }
//
//      sobrevivientes match {
//        case s if s.size == 0 => None
//        case s if s.size == 1 => Some(sobrevivientes.head)
//        case _ => Some(reglasTorneo.desempate(sobrevivientes))
//      }
//
//      //TODO: OTRA FORMA DE PATTERN MATCHING
////      sobrevivientes match {
////        case Nil => None
////        case x :: Nil => Some(sobrevivientes.head)
////        case xs => Some(reglasTorneo.desempate(xs))
////      }
//    }

  }
}
