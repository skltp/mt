package se.skltp.mt.core.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.skltp.mt.core.entity.User;
import se.skltp.mt.core.service.UserService;
import se.skltp.mt.exception.MessageException;
import se.skltp.mt.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends JpaRepositoryTestBase {

    @Autowired
    private UserService userService;

    @PersistenceContext
    EntityManager entityManager;

    @Ignore("uses non-transactional getConnection()") @Test
    public void createUser() throws Exception {
        User user = userService.createUser("test", "test1", "Kalle", "Anka");

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getFirstname(), "Kalle");

        try {
            user = userService.createUser("test", "test1", "Kalle", "Anka");
            Assert.fail("Should not be able to create user with same name");
        } catch (MessageException e) {
            Assert.assertEquals("error.user.alreadyexist", e.getMessageCode());
        }

        user = userService.createUser("test1", "test1", "Kalle", "Anka");

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), "test1");
        ITable result = getConnection().createQueryTable("AUTHORITIES", "SELECT * FROM AUTHORITIES WHERE USERNAME = 'test1'");
        Assert.assertEquals("ROLE_USER", result.getValue(0, "AUTHORITY"));
    }

    @Ignore("uses non-transactional getConnection()") @Test
    public void deleteUser() throws Exception {
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getXmlDataSet("user.xml"));
        userService.deleteUser("kajsa");

        entityManager.flush();

        ITable result = getConnection().createQueryTable("USER", "SELECT * FROM USERS WHERE USERNAME = 'kajsa'");
        Assert.assertEquals(0, result.getRowCount());
        
        result = getConnection().createQueryTable("USER", "SELECT * FROM AUTHORITIES WHERE ID = 3");
        Assert.assertEquals(0, result.getRowCount());
    }

    @Ignore("uses non-transactional getConnection()") @Test
    public void findAll() throws Exception {
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getXmlDataSet("user.xml"));
        List<User> users = userService.findAllUsers();

        Assert.assertEquals(3, users.size());
    }
}
