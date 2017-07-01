package torneoVikingos
import torneoVikingos.Posta.Posta

import scala.util.{Failure, Success, Try}
import torneoVikingos._

object Torneo {

  // TODO: PROPUESTA PARA PODER TENER EQUIPOS EN EL TORNEO (BUZZA TKB)
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
                         )
  {
      def conPreparacion(nuevaRegla: ReglaPreparacion): ReglasTorneo = copy(preparacion = nuevaRegla)
      def conClasificacion(nuevaRegla: ReglaClasificacion): ReglasTorneo = copy(clasificacion = nuevaRegla)
      def conDesempate(nuevaRegla: ReglaDesempate): ReglasTorneo = copy(desempate = nuevaRegla)
  }

  case class Torneo(
                     participantes: List[Vikingo],
                     postas: List[Posta],
                     dragonesDisponibles: List[Dragon],
                     reglasTorneo: ReglasTorneo
                   ) {

    def desmontarCompetidores(competidores: List[Competidor]): List[Vikingo] = {
      competidores.map {
        case Jinete(v, _) => v
        case vik:Vikingo => vik
      }
    }

    def jugar: Option[Vikingo] = {

      // Mientras haya mas de un participante se sigue jugando
      val sobrevivientes: List[Vikingo] = postas.foldLeft(participantes) {
        (clasificados, posta) => {
          clasificados match {
            case ganador :: Nil => List(ganador)
            case x :: xs =>
              val competidores = reglasTorneo.preparacion(x :: xs, dragonesDisponibles, posta)
              val competidoresOrdenados = posta.jugar(competidores)
              var ganadoresDeLaPosta = reglasTorneo.clasificacion(competidoresOrdenados)
              desmontarCompetidores(ganadoresDeLaPosta)
          }
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
