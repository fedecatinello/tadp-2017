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
                     participantes: List[Participante],
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

    def reagruparEquipos(vikingos: List[Vikingo]): List[Equipo] = {
      val originales = participantes // Tengo los originales y recibo la lista de vikingos asi que puedo volver a formar los grupos

      Nil // TODO: Borrar es solo para que tipe
    }

    def jugar: Option[Participante] = {

      val _participantes: List[Vikingo] = participantes.flatMap(p => {
        p match {
          case Equipo(_, listaParticipantes) => listaParticipantes
          case v@Vikingo(_, _, _) => List(v)
        }
      })

      // Los que pasen todas las postas (puede ser una lista vacia en ese caso ninguno supero todas las postas)
      val sobrevivientes: List[Vikingo] = postas.foldLeft(_participantes) {
        (clasificados, posta) => {

          clasificados match {
            case ganador :: Nil => List(ganador)
            case x :: xs => {
              val competidores = reglasTorneo.preparacion(clasificados, dragonesDisponibles, posta)
              val competidoresOrdenados = posta.jugar(competidores)
              val ganadoresDeLaPosta = reglasTorneo.clasificacion(competidoresOrdenados)
              desmontarCompetidores(ganadoresDeLaPosta)
            }
          }
        }
      }

      // Si estaba jugando con equipos


      // Si no

      // Sobrevivientes es una lista de vikingos, si es un torneo de equipos tengo que volver a formar los equipos
      // si no era equipos va esto como antes:
//      sobrevivientes match {
//        case Nil => None
//        case g :: Nil => Some(g) // Si hay un solo elemento en la lista es el ganador
//        case _ => Some(reglasTorneo.desempate(sobrevivientes))
//      }

      None // TODO: Borrar es solo para que tipe
    }

  }
}
