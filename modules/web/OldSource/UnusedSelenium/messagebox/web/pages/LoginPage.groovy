package se.skltp.mb.web.pages


import geb.Page;

/**
 * Test the main page of the web site
 *
 ** @author mats.olsson@matsotech.se
 */
public class LoginPage extends Page {

	public LoginPage() {
	}
	static url = "/"
	static at = { $("title").text() == "Login Page" }
	
	static content = {
		username { $("input", name: "j_username") }
		password { $("input", name: "j_password") }
		submitButton { $("input", name: "submit") }
	}
	
	def login(String username, String password) {
		this.username = username
		this.password = password
		submitButton.click()
	}
		
}

