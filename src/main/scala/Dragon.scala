trait razaDragon {
  val requisitos: List[Any]
  val peso: Float
  val velocidadBase: Float

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = {
    true
  }

}

case class FuriaNocturna(peso: Float, velocidadBase: Float, requisitos: List[Any]) extends razaDragon {

}