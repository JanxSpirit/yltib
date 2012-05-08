package com.janxspirit.yltib

import akka.actor._
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import scala.annotation.tailrec

class Resolver extends Actor {

  def receive = {
    case ResolveUrl(source, numLevels) => sender ! resolveUrl(source, numLevels)
  }

  def resolveUrl(source: String, numLevels: Int): UrlResolutionMessage = {

    @tailrec
    def resolveUrlInternal(source: String, 
			   locChain: List[String], 
			   curLevels: Int, 
			   numLevels: Int): List[String] = {
      val url = new URL(source)
      val connection = url.openConnection(Proxy.NO_PROXY).asInstanceOf[HttpURLConnection]
      connection.setInstanceFollowRedirects(false)
      connection.connect()
      val expandedUrl = connection.getHeaderField("Location")
      connection.getInputStream().close()

      connection.getResponseCode match {
	case c: Int if (c >=300 && c < 400 && (curLevels +1) >= numLevels) => {
	  //as far as we go
          locChain :+ expandedUrl
	}
	case c: Int if (c >=300 && c < 400) => {
	  //this is a redirect
	  resolveUrlInternal(expandedUrl, locChain :+ expandedUrl, curLevels + 1, numLevels)
	}
	case c: Int if (c == 200) => {
	  locChain
	}
	case _ => throw new IllegalStateException("received response code %s from url %s"
						  .format(connection.getResponseCode, source))
      }
    }

    //it is probbaly more correct to use Akka's actor supervision than try/catch
    try {
      ResolvedUrl(source, resolveUrlInternal(source, List(source), 0, numLevels))
    } catch {
      case e => ResolveFailure(source, Option(e.getMessage))
    }
  }
}
