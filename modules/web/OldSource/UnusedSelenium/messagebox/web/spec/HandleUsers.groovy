package se.skltp.messagebox.web.spec

import geb.Browser
import se.skltp.messagebox.web.pages.*
import MainPage
import HandleUsersPage
import LoginPage

public class HandleUsers {

	public void starta() {
		Browser.drive {
			go()
		}
	}
		
	public boolean loginsidanVisas() {
		def result = false
		Browser.drive {
			waitFor {
				at LoginPage
				result = true
			}
		}
		return result
	}

	public boolean huvudsidanVisas() {
		def result = false
		Browser b = Browser.drive {
			waitFor {
				at MainPage
				result = true
			}
		}
		println "Browser driver: " + b.driver
		return result
	}
	
	public boolean hanteraAnvändaresidanVisas() {
		def result = false
		Browser.drive {
			waitFor {
				at HandleUsersPage
				result = true
			}
		}
		return result
	}

	
	public void väljHanteraAnvändare() {
		Browser.drive {
			at MainPage
			page.gotoHandleUsers()
		}
	}
	
	public int räknaAnvändare() {
		int result = 0
		Browser.drive {
			try {
				at HandleUsersPage
				result = page.countUsers()
			} catch (e) {
				println e
			}
		}
		return result
	}
	
	public void skapaAnvändareMedLösen(String username, String password) {
		Browser.drive {
			at HandleUsersPage
			page.createUser(username, password)
		}
	}
	
	public void taBortAnvändare(String username) {
		Browser.drive {
			at HandleUsersPage
			page.deleteUser(username)
		}
	}
	
	public void väljHuvudsida() {
		Browser.drive {
			at HandleUsersPage
			page.gotoMainPage()
		}
	}
	
	public void loggaPåSomAnvändareMedLösen(username, password) {
		Browser.drive {
			at LoginPage
			page.login(username, password)
		}
	}

}