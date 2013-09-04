package se.skltp.mt.web.spec

import geb.Browser
import se.skltp.mt.web.pages.*

public class HandleUsers {

		
	public boolean loginsidanVisas() {
		Browser.drive {
			go()
			waitFor {
				at LoginPage
			}
		} 

	}

	public boolean huvudsidanVisas() {
		Browser.drive {
			waitFor {
				at MainPage
			}
		}
	}
	
	public boolean hanteraAnvändaresidanVisas() {
		println "hantAnvVisas"
		Browser.drive {
			waitFor {
				at HandleUsersPage
			}
		}
	}

	
	public void väljHanteraAnvändare() {
		println "väljHA"
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