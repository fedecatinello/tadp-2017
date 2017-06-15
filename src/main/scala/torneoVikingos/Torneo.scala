package torneoVikingos
import torneoVikingos.Posta.Posta
import scala.util.{Try, Success, Failure}
import torneoVikingos._

object Torneo {

  /*
  * Los participantes se preparan para la primer posta según las reglas y la juegan,
  * luego se decide quienes avanzan a la siguiente también según las reglas.
  * */
  type ReglaDePreparacion = Competidor => Competidor
  type ReglaDeAprobacion = List[Competidor] => List[Competidor]

  case class Torneo(participantes: List[Competidor],
                    postas: List[Posta],
                    dragonesDisponibles: List[Dragon],
                    reglasPrep: Map[Posta, List[ReglaDePreparacion]] = Map(),
                    reglasAprob: Map[Posta, ReglaDeAprobacion] = Map()) {

    def prepararParticipantes(participantes: List[Competidor], posta: Posta) : List[Competidor] = {
      participantes.map(p => reglasPrep(posta).foldLeft(p){(acum,reglaPrep)=>
        reglaPrep(p)
      })
    }

    def jugarTorneo(): List[Competidor] = {

      val rdoParticipantes = postas.foldLeft(participantes){(acum, posta) =>

        val participantesPosibles = posta.puedenParticipar(acum)
        if (participantesPosibles.size == 0){
          /* ... Si ninguno pueda entrar a una posta, entonces no hay ganador. */
          List()
        }

        if (participantesPosibles.size == 1){
          /*En caso de que las postas que conforman el torneo poseen un requisito para entrar a ella,
           y solo califique uno, si el mismo supera la posta, será el ganador de la posta...*/
          val reglasDeAprobParaPosta = reglasAprob(posta)
          reglasDeAprobParaPosta(participantesPosibles)
        }

        if (acum.size == 1){
          /* Este participante ya gano porque es el ultimo de pie. */
          acum
        } else if (acum.size == 0) {
            /*
            * En caso de que en algún momento queden todos los participantes descalificados,
            * no hay ganador y se finaliza el torneo.
            * */
          List()

        } else {
          var ganadoresPosta = posta.jugar(participantes)
          val reglasDeAprobParaPosta = reglasAprob(posta)
          reglasDeAprobParaPosta(ganadoresPosta)
        }
      }

      if (rdoParticipantes.size == 1 || rdoParticipantes.size == 0){
        /*
        * En caso de que quede solo un participante, este es el ganador del torneo y no se juegan
        * más postas, por lo que aunque exista una posta en la que no pueda participar no le
        * importa porque ya ganó.
        * */
      }

      if (rdoParticipantes.size > 1) {
        /*
        * En caso de que se hayan finalizado todas las postas que comprendía el torneo y haya más
        * de un jugador en pie, el ganador se decide según las reglas del torneo.
        * */
      }

      rdoParticipantes
    }
  }
}
