import play.api.test._
import play.api.test.Helpers._

import collection.mutable.Stack
import org.scalatestplus.play._

import play.api._
import play.api.ApplicationLoader.Context
import java.io.File


object WithDepsApplication {

  def app = {
    val appLoader = new SimpleApplicationLoader
    val context = ApplicationLoader.createContext(
      new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
    )

    appLoader.load(context)
  }
}

class ApplicationSpec extends PlaySpec with OneAppPerSuite {

  override implicit lazy val app = WithDepsApplication.app

  "Application" must {   

     "list computers on the the first page" in {        
        val result = route(FakeRequest(GET, "/computers")).get
        status(result) mustBe(OK)
        contentAsString(result) must include("574 computers found")                  
    }

  }
  
}