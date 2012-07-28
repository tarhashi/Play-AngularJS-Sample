import org.squeryl.adapters.H2Adapter
import org.squeryl.internals.DatabaseAdapter
import org.squeryl.{Session, SessionFactory}
import play.api.db.DB
import play.api.Application
import play.api.GlobalSettings

object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    SessionFactory.concreteFactory = app.configuration.getString("db.default.driver") match {
      case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
      case _ => sys.error("Database driver must be org.h2.Driver")
    }
  }
  
  def getSession(adapter: DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)
}