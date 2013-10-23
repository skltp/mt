/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.skltp.messagebox.types.repository;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Base test case class for handling the dbunit connection. The database is not rollbacked after the test transaction
 * is finished. Utilty method that is always called after the transaction. 
 * Utility methods for retrieving the dbunit database connection.
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public abstract class JpaRepositoryTestBase extends AbstractTransactionalJUnit4SpringContextTests {

    private IDatabaseConnection conn;

    @Autowired
    private DriverManagerDataSource dataSource;

    /**
     * Should not be possible to override the onSetup method that is always called before the transaction.
     * @throws Exception
     */
    @Before
    public final void onSetup() throws Exception {
        conn = new DatabaseDataSourceConnection(dataSource);

        DatabaseConfig config = conn.getConfig();


       config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory()); 
        // set auto commit to false to avoid the data to live over the test suite
        conn.getConnection().setAutoCommit(false);

        onSetup2();
    }

    /**
     * Hook called last from onSetup()
     *
     * @throws Exception
     */
    public void onSetup2() throws Exception {
    }

    /**
     * Returns a dbunit database connection that is not commit to the database.
     * @return {@link IDatabaseConnection}
     */
    public IDatabaseConnection getConnection() {
        return conn;
    }

    /**
     * Should not be possible to override the onTearDown method that is always called after the transaction to 
     * roll back the database. 
     * @throws Exception
     */
    @After
    public final void onTearDown() throws Exception {
        conn.getConnection().rollback();
    }
}
