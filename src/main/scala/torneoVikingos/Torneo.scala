package torneoVikingos
import torneoVikingos.Posta.Posta

import scala.util.{Failure, Success, Try}
import torneoVikingos._

object Torneo {

  type ReglaPreparacion = (List[Vikingo], List[Dragon], Posta) => List[Competidor]
  type ReglaClasificacion = List[Competidor] => List[Competidor]
  type ReglaDesempate = List[Participante] => Participante

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

    def reagruparEquipos(ganadores: List[Vikingo]): List[Participante] = {
      // Tengo los originales y recibo la lista de vikingos asi que puedo volver a formar los grupos

      val equiposGanadores = participantes.filter(_ match {
        case vikingo:Vikingo => ganadores.contains(vikingo)
        case e:Equipo => e.participantes.exists(ganadores.contains(_))
      })

      equiposGanadores.map(_ match {
        case vikingo:Vikingo => vikingo
        case e:Equipo => e.copy(participantes = e.participantes.filter(ganadores.contains(_)))
      })
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
            case Nil => List()
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

      // Sobrevivientes es una lista de vikingos, si es un torneo de equipos tengo que volver a formar los equipos
      // si no era equipos va esto como antes:


      sobrevivientes match {
        case Nil => None
        case g :: Nil => Some(g) // Si hay un solo elemento en la lista es el ganador
        case _ => participantes.head match{
          case _:Vikingo => Some(reglasTorneo.desempate(sobrevivientes))
          case _:Equipo => Some(reglasTorneo.desempate(reagruparEquipos(sobrevivientes)))
        }
      }
    }

  }
}
