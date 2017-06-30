package torneoVikingos

object Posta {

  type Requisito = Competidor => Boolean
  type CriterioOrdenamiento = (Competidor, Competidor) => Boolean
  type EfectoColateral = Competidor => Competidor

  case class Posta(
                    criterioOrdenamiento: CriterioOrdenamiento,
                    efectoColateral: EfectoColateral,
                    requisitosAdmision: List[Requisito] = Nil
                  ) {

    // El requerimiento fijo de todas las postas es que una vez terminada (luego de que se le aplique el
    // efecto colateral) el hambre del competidor no supere el 100.
    val requisitoFijo: Requisito = {
        case c if c.esPatapez => c.hambre > 50
        case c => efectoColateral(c).hambre < 100
    }

    // Recibe una lista de competidores y devuelve otra lista de vikingos
    // filtrados solo con los que pueden participar, es decir cumplen
    // los requisitos de admision y al terminar la posta su hambre no superaria el 100
    def puedenParticipar(_participantes: List[Competidor]): List[Competidor] = {
      val filtros = requisitoFijo :: requisitosAdmision
      _participantes.filter(p => filtros.forall(_ apply p))
    }

    // Filtra a los vikingos que pueden participar de la posta segun los requisitos de esta.
    // Los ordena segun el criterio que define la posta para saber a quien le fue mejor.
    // Aplica el efecto colateral.
    def jugar(participantes: List[Competidor]): List[Competidor] = {
      puedenParticipar(participantes).map(efectoColateral).sortWith(criterioOrdenamiento)
    }

    // Como jugar pero no les aplica el efecto colateral, solo ordena a los que pueden participar
    def ordenar(participantes: => List[Competidor]): List[Competidor] = {
      puedenParticipar(participantes).sortWith(criterioOrdenamiento)
    }

    // Ordena a los participantes segun un criterio dado
    def mejorPuntuacion(participantes: => List[Competidor])(criterio: List[Competidor] => Competidor): Competidor =
      criterio(ordenar(participantes))
  }
}