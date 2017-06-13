import org.scalatest.{FlatSpec, Matchers}
import torneoVikingos._

class VikingoSpec extends FlatSpec with Matchers {

  val hacha = Some(Arma(30))
  val maza = Some(Arma(100))

  val hipo = Vikingo("Hipo", CaracteristicaCompetidor(peso=100, velocidad=15, barbarosidad=50, hambre=30), Some(SistemaDeVuelo))
  val astrid = Vikingo("Astrid", CaracteristicaCompetidor(peso=80, velocidad=30, barbarosidad=150, hambre=10), hacha)
  val patan = Vikingo("Patan", CaracteristicaCompetidor(peso=250, velocidad=50, barbarosidad= 120, hambre=98), maza)
  val patapez = Vikingo("Patapez", CaracteristicaCompetidor(peso=120, velocidad=90, barbarosidad= 80, hambre=70), Some(ItemComestible(20)))

}