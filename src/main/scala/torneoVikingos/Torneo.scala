package torneoVikingos
import torneoVikingos.Posta.Posta
import scala.util.{Try, Success, Failure}
import torneoVikingos._

object Torneo {

  case class ReglasTorneo(
                          preparacion:  (List[Vikingo], List[Dragon], Posta) => List[Competidor],//((List[Vikingo], List[Dragon], Posta) => List[Competidor]),
                          clasificacion: List[Competidor] => List[Vikingo],
                          desempate: List[Vikingo] => Vikingo
                         ) {}

  val reglasEstandar = ReglasTorneo(???, ???, ???)

  case class Torneo(
                     participantes: List[Vikingo],
                     postas: List[Posta],
                     dragonesDisponibles: List[Dragon],
                     reglasTorneo: ReglasTorneo
                   ) {

    def jugar: Option[Vikingo] = {
      val sobrevivientes = postas.foldLeft(participantes){
        (clasificados: List[Vikingo], posta: Posta) => {

          /*En caso de que quede solo un participante, este es el
          ganador del torneo y no se juegan más postas*/
          if (clasificados.size == 1) {
            clasificados
          }

          val participantesListos = reglasTorneo.preparacion(participantes, dragonesDisponibles, posta)
          val participantesLuegoDePosta = posta.jugar(participantesListos)
          val ganadores = reglasTorneo.clasificacion(participantesLuegoDePosta)
          ganadores
        }
      }

      sobrevivientes match {
        case s if s.size == 0 => None
        case s if s.size == 1 => Some(sobrevivientes.head)
        case _ => Some(reglasTorneo.desempate(sobrevivientes))
      }

      //TODO: OTRA FORMA DE PATTERN MATCHING
//      sobrevivientes match {
//        case Nil => None
//        case x :: Nil => Some(sobrevivientes.head)
//        case xs => Some(reglasTorneo.desempate(xs))
//      }
    }
  }
}
