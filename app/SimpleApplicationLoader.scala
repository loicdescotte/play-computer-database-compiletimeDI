import play.api._
import play.api.db._
import play.api.i18n._
import play.api.ApplicationLoader.Context
import play.api.db.evolutions._
import router.Routes

class SimpleApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    //logback activation
    Logger.configure(context.environment)
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) 
  with DBComponents with HikariCPComponents with EvolutionsComponents with I18nComponents {       
  lazy val database = {
    applicationEvolutions.start
    dbApi.database("default")
  }
  lazy val applicationController = new controllers.Application(database, messagesApi)
  lazy val assets = new controllers.Assets(httpErrorHandler)
  override lazy val dynamicEvolutions = new DynamicEvolutions
  override lazy val router = new Routes(httpErrorHandler, applicationController, assets)
}
