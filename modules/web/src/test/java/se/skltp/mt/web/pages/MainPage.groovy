package se.skltp.mt.web.pages


import geb.Page;

/**
 * Test the main page of the web site
 *
 ** @author mats.olsson@matsotech.se
 */
public class MainPage extends Page {

	public MainPage() {
	}
	
	static at = { $("div.panel-data-header").text() == "Välkommen till Ärendelådan" }
	
	static content = {
		handleUsers { $("a", text: "Hantera användare") }
	}
	
	def gotoHandleUsers() {
		handleUsers.click()
	}
		
}

