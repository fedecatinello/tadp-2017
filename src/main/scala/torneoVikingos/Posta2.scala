package torneoVikingos

case class Posta2 (
                    requisitosAdmision: Option[List[(Competidor => Boolean)]] = None,
                    criterioOrdenamiento: ((Competidor, Competidor) => Boolean),
                    efectoColateral: (Competidor => Competidor)
                  ) {

  def puedenParticipar(
                        _participantes : List[Competidor],
                        _requisitos: Option[List[(Competidor => Boolean)]] = requisitosAdmision
                      ) : List[Competidor] = {
    _requisitos match {
      case None => _participantes.filter((c: Competidor) => efectoColateral(c).hambre < 100) // un competidor no puede participar en una posta si su nivel de hambre tras participar en la posta alcanzaría el 100%.
      case Some(unRequisito :: otrosRequisitos) =>
        puedenParticipar(_participantes.filter(unRequisito), Some(otrosRequisitos))
    }
  }

  def jugar(participantes: List[Competidor]) : List[Competidor] = {
    puedenParticipar(participantes).sortWith(criterioOrdenamiento).map(efectoColateral)
  }

}