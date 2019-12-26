package entities.models

trait IClientConfig {

  def dataTimeFormat: DataTimeFormat

  def enums: Seq[FactorName]

  def integers: Seq[FactorName]

  def doubles: Seq[FactorName]

  def predictable: Seq[FactorName]

  def baselined: Seq[FactorName]

  def geo: Boolean
}
