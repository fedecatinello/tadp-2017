package torneoVikingos
import torneoVikingos.Posta.Posta
import scala.util.{Try, Success, Failure}
import torneoVikingos._



object Torneo {

//  abstract class ReglasTorneo {
//    def prepararParticipantes(vikingos: List[Vikingo], dragones: List[Dragon], posta: Posta): List[Competidor] = {
//      Nil
//    }
//
//    def pasanSiguienteRonda(competidores: List[Competidor]): List[Competidor] = {
//      Nil
//    }
//
//    def ganador(vikingo: List[Vikingo]): Vikingo = {
//      ???
//    }
//  }
//
//  class Eliminacion extends  ReglasTorneo {
//    override def ganador(vikingo: List[Vikingo]): Vikingo = super.ganador(vikingo)
//  }

  case class ReglasTorneo(
                          preparacion: (List[Vikingo], List[Dragon], Posta) => List[Competidor],
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
      postas.foldLeft(participantes){
        (clasificados: List[Vikingo], posta: Posta) => {
          val participantesListos: List[Competidor] = reglasTorneo.preparacion(???, ???, ???)

        }
      }
      None
    }

  }

}
