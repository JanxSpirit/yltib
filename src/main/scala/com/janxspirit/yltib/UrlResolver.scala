package com.janxspirit.yltib

import akka.actor._
import akka.dispatch.Await
import akka.dispatch.ExecutionContext
import akka.pattern.ask
import akka.routing.RoundRobinRouter
import akka.util.duration._
 
import java.util.concurrent.Executors

class UrlResolver(poolSize: Int) {
  val ec = ExecutionContext.fromExecutorService(
    Executors.newCachedThreadPool()
  )
  val as = ActorSystem()

  val router = as.actorOf(
    Props[Resolver].withRouter(RoundRobinRouter(poolSize)), name = "router")

  def resolveUrls(urls: List[String]) = {
    val futures = urls.map(url => {
      router.ask(ResolveUrl(url, 10))(30 seconds)
    })

    futures.map(fut => {
      Await.result(fut, 30 seconds)
    }).collect {
      case r: ResolvedUrl => (r.source -> r.resolved)
    }.toMap
  }

  def shutdown = as.shutdown
}
