package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.db._
import play.api.data.Forms._

import play.api.Play.current
import play.api.i18n._

import anorm._

import views._
import models._

/**
 * Manage a database of computers
 */
class Application(database: Database, messages: MessagesApi) extends Controller with I18nSupport { 
  
  override def messagesApi = messages
 
  val computerDao = new ComputerDAO(database)
  val companyDao = new CompanyDAO(database)
  val options = companyDao.options

  /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.Application.list(0, 2, ""))
  
  /**
   * Describe the computer form (used in both edit and create screens).
   */ 
  val computerForm = Form(
    mapping(
      "id" -> ignored(None:Option[Long]),
      "name" -> nonEmptyText,
      "introduced" -> optional(date("yyyy-MM-dd")),
      "discontinued" -> optional(date("yyyy-MM-dd")),
      "company" -> optional(longNumber)
    )(Computer.apply)(Computer.unapply)
  )
  
  // -- Actions

  /**
   * Handle default path requests, redirect to computers list
   */  
  def index = Action { Home }
  
  /**
   * Display the paginated list of computers.
   *
   * @param page Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter Filter applied on computer names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
    Ok(html.list(
      computerDao.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }
  
  /**
   * Display the 'edit form' of a existing Computer.
   *
   * @param id Id of the computer to edit
   */
  def edit(id: Long) = Action {
    computerDao.findById(id).map { computer =>
      Ok(html.editForm(id, computerForm.fill(computer), options))
    }.getOrElse(NotFound)
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the computer to edit
   */
  def update(id: Long) = Action { implicit request =>
    computerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.editForm(id, formWithErrors, options)),
      c => {
        computerDao.update(id, c)
        Home.flashing("success" -> "Computer %s has been updated".format(c.name))
      }
    )
  }
  
  /**
   * Display the 'new computer form'.
   */
  def create = Action {
    Ok(html.createForm(computerForm, options))
  }
  
  /**
   * Handle the 'new computer form' submission.
   */
  def save = Action { implicit request =>
    computerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors, options)),
      c => {
        computerDao.insert(c)
        Home.flashing("success" -> "Computer %s has been created".format(c.name))
      }
    )
  }
  
  /**
   * Handle computer deletion.
   */
  def delete(id: Long) = Action {
    computerDao.delete(id)
    Home.flashing("success" -> "Computer has been deleted")
  }

}
            
