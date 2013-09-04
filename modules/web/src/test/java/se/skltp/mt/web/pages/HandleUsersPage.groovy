package se.skltp.mt.web.pages

import geb.Page;

/**
 * Represents the handle-users page of the web site
 *
 * @author mats.olsson@matsotech.se
 */
public class HandleUsersPage extends Page {

	public HandleUsersPage() {
	}
	
	static at = { $("div.panel-data-header").text() == "Skapa användare" }
	
	static content = {
		mainPage { $("a", text: "Nuläge") }
		username { $("input", name: "username") }
		password { $("input", name: "password") }
		saveButton { $("input", name: "button.save") }
		userTable { $("div table") }
		userRowsWithHeaders { $("div table tr") } 
	}
	
	def gotoMainPage() {
		mainPage.click()
	}
	
	def countUsers() {
		return userRowsWithHeaders.size() - 1
	}
	
	def createUser(String username, String password) {
		this.username = username
		this.password = password
		saveButton.click()
	} 
	
	def deleteUser(String username) {
		def userNameCell = userRowsWithHeaders.find("td", 0, text: username)
		if (userNameCell.size() == 1) {
			// find the first href-entry in the row for the user and click it
			// not very stable, but if we did this for real we would have an id 
			// #<username>-delete-link attached directly to the link
			userNameCell.parent().find("a").click()
		} else {
			throw new RuntimeException("Found " + userNameCell.size() + " users named " + username)
		}
			
	}	
	
}

