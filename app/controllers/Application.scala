package controllers

import play.api._
import play.api.mvc._

import org.squeryl.PrimitiveTypeMode._
import models.AppDB
import models.Task
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import views._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {
  
  implicit val taskFormat = (
    (__ \ "id").format[Long] and
    (__ \ "label").format[String] and
    (__ \ "done").format[Boolean]
  )(models.Task.apply, unlift(models.Task.unapply))
  
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
    Ok(Json.toJson(tasks)).as("application/json")
  }
  
  def newTask = Action { implicit request =>
    val t = taskForm.bindFromRequest.value map { task =>
      val t = inTransaction(AppDB.taskTable insert task )
      t
    }
    Ok(Json.toJson(t)).as("application/json")
  }
  
  def doneTask(id: Long) = Action { implicit request =>
    val t = inTransaction{
      update(AppDB.taskTable)(t => 
      where(t.id === id)
      set(t.done := true)
      )
    }
    Ok(Json.toJson(t)).as("application/json")
  }
  def undoneTask(id: Long) = Action { implicit request =>
    val t = inTransaction{
      update(AppDB.taskTable)(t => 
      where(t.id === id)
      set(t.done := false)
      )
    }
    Ok(Json.toJson(t)).as("application/json")
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
