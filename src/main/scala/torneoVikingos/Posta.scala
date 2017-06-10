package torneoVikingos

//TODO: object deprecado, remover si corresponde

object Posta {

  type ActividadPosta = List[Competidor] => List[Competidor]

  case class ActividadPesca(pesoMinimo: Option[Double]) extends ActividadPosta {
    def apply(competidores: List[Competidor]): List[Competidor] = {
      // Se ordenan segun los que mas peso puedan cargar
      competidores.sortBy( (c: Competidor) => (c.peso * .5) + (c.barbarosidad * 2) )
    }

  }

  case object ActividadCombate extends ActividadPosta {
    def apply(competidores: List[Competidor]): List[Competidor] = {
      competidores
    }
  }

  abstract class Posta(
                        participantes: List[Competidor],
                        actividad: ActividadPosta,
                        requisitosAdmision: Option[List[(Competidor => Boolean)]] = None
                      ) {}

  case class Pesca(
                    participantes: List[Competidor],
                    actividad: ActividadPosta,
                    requisitosAdmision: Option[List[(Competidor => Boolean)]] = None
                  ) extends Posta(participantes, actividad, requisitosAdmision) {

    def puedenParticipar(
                          participantes2: List[Competidor] = participantes,
                          requisitos2:  Option[List[(Competidor => Boolean)]] = requisitosAdmision
                        ) : List[Competidor] = {
      requisitos2 match {
        case None => participantes
        case Some(requisito :: otrosRequisitos) => puedenParticipar(participantes2.filter(requisito), Some(otrosRequisitos))
      }
    }



  }

}

