/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */
package se.skltp.messagebox.util;

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
        // set auto commit to false to aviod the data to live over the test suite
        conn.getConnection().setAutoCommit(false);
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
