package se.skltp.mb.ws;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
// Genererar felet
//@SuiteClasses({ ListMessagesIntegrationTest.class,
//		ReceiveMessagesIntegrationTest.class })
@SuiteClasses({ GetMessagesIntegrationTest.class,
ReceiveMessagesIntegrationTest.class })
public class AllTests {

}
