package com.janxspirit.yltib

case class ResolveUrl(source: String,
		      numLevels: Int = 1)

case class ResolvedUrl(source: String,
		       resolved: List[String]) extends UrlResolutionMessage {
  val successful = true
  val errorMsg = None
}

case class ResolveFailure(source: String, errorMsg: Option[String]) extends UrlResolutionMessage {
  val successful = false
}

trait UrlResolutionMessage {
  def successful: Boolean
  def errorMsg: Option[String]
}
