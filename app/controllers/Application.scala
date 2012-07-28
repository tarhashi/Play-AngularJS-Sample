package controllers

import play.api._
import play.api.mvc._

import com.github.tototoshi.play2.json.LiftJson
import net.liftweb.json._
import org.squeryl.PrimitiveTypeMode._
import models.AppDB
import models.Task
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import views._

object Application extends Controller with LiftJson {
  
  implicit val formats = DefaultFormats
  
  val taskForm = Form(
    mapping(
      "id" -> ignored(0L),
      "label"  -> nonEmptyText,
      "done" ->ignored(false)
    )(Task.apply)(Task.unapply)
  )
  
  def index = Action { implicit request =>
    Ok(views.html.index(this.taskForm))
  }
  
  def tasks = Action { implicit request =>
    
    def tasks = inTransaction {
      val t = from(AppDB.taskTable)(t =>where(t.done === false) select(t))
      t.toList
    }
    Ok(Extraction.decompose(tasks)).as("application/json")
  }
  
  def newTask = Action { implicit request =>
    val t = taskForm.bindFromRequest.value map { task =>
      val t = inTransaction(AppDB.taskTable insert task )
      t
    }
    Ok(Extraction.decompose(t)).as("application/json")
  }
  
  def doneTask(id: Long) = Action { implicit request =>
    val t = inTransaction{
      update(AppDB.taskTable)(t => 
      where(t.id === id)
      set(t.done := true)
      )
    }
    Ok(Extraction.decompose(t)).as("application/json")
  }
  def undoneTask(id: Long) = Action { implicit request =>
    val t = inTransaction{
      update(AppDB.taskTable)(t => 
      where(t.id === id)
      set(t.done := false)
      )
    }
    Ok(Extraction.decompose(t)).as("application/json")
  }
  
  def javascriptRoutes() = Action { implicit request =>
    import play.api.Routes
    Ok(
      Routes.javascriptRouter("jsRouter", Some("jQuery.ajax"))(
          routes.javascript.Application.tasks,
          routes.javascript.Application.newTask,
          routes.javascript.Application.doneTask,
          routes.javascript.Application.undoneTask
      )
    ).as("text/javascript")
  }
  
}