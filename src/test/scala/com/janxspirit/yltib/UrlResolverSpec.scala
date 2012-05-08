package com.janxspirit.yltib

import org.specs2.mutable._

class UrlResolverSpec extends SpecificationWithJUnit {

  "The UrlResolver" should {
    val ur = new UrlResolver(10)
    val res = ur.resolveUrls(List("http://bit.ly/900913"))
    ur.shutdown
    val chain = res("http://bit.ly/900913")
    "resolve http://bit.ly/900913" in {
      "with a chain of length 3" in {
	chain.size must_== 3
      }
      "with a resolved value of http://www.google.com/" in {
	chain.last must_=="http://www.google.com/"
      }
    }
  }

}
