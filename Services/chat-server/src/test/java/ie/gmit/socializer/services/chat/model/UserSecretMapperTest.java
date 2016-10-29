/**
 * Copyright (C) 2016 Peter Nagy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ======================================================================
 *
 * @author Peter Nagy - peternagy.ie
 * @since October 2016
 * @version 0.1
 * @description UserSecretMapperTest - Test cases for UserSecretMapper
 * @package ie.gmit.socializer.services.chat.model
 */
package ie.gmit.socializer.services.chat.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Result;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

//Required to keep database clean after test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserSecretMapperTest {
    
    static Cluster cluster;
    static UserSecret us1;
    static UserSecret us2;
    static UserSecret us3;
    static UUID[] ids;
    static final String KEY_SPACE = "app_user_data";
    
    public UserSecretMapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        cluster = CassandraConnector.initalizeConnection(KEY_SPACE);
        ids = new UUID[]{UUIDs.random(), UUIDs.random(), UUIDs.random()};
        
        us1 = new UserSecret(ids[0]);
        us2 = new UserSecret(ids[1]);
        us3 = new UserSecret(ids[2]);
        
    }
    
    
    @Before
    public void setUp() {
        System.out.printf("\nExecuting test for %s class, method test: ", OauthTokenMapper.class.getName());
    }
    

    /**
     * Test of initializeMapper method, of class UserSecretMapper.
     */
    @Test
    public void test_010InitializeMapper() {
        System.out.println("initializeMapper");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        Exception exc = null;

        try {
            instance.initializeMapper();
        } catch (Exception e) {
            exc = e;
        }

        assertEquals(true, null == exc);
    }

    /**
     * Test of createEntry method, of class UserSecretMapper.
     */
    @Test
    public void test_020CreateEntry() {
        System.out.println("createEntry");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        
        boolean result = instance.createEntry(us1);
        
        assertEquals(true, result);
    }

    /**
     * Test of createEntryAsync method, of class UserSecretMapper.
     */
    @Test
    public void test_030CreateEntryAsync() {
        System.out.println("createEntryAsync");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        
        Exception exc = null;

        try {
            instance.createEntryAsync(us2);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        
        assertEquals(true, null == exc);
    }

        /**
     * Test of updateEntry method, of class UserSecretMapper.
     */
    @Test
    public void test_040UpdateEntry() {
        System.out.println("updateEntry");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        us1.setEnc_private_key("asdasda");
        Exception exc = null;

        try {
            instance.updateEntry(us1);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        
        assertEquals(true, null == exc);
    }

    /**
     * Test of getEntry method, of class UserSecretMapper.
     */
    @Test
    public void test_050GetEntry() {
        System.out.println("getEntry");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        UserSecret result = instance.getEntry(us1.getUser_uuid());
        
        assertEquals(true, result != null && result.getCreated().equals(us1.getCreated()));
    }

    /**
     * Test of getMultiple method, of class UserSecretMapper.
     */
    @Test
    public void test_060GetMultiple() {
        System.out.println("getMultiple");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        PreparedStatement prepared = instance.session.prepare(String.format("select * from %s.user_secret where user_uuid in ?", KEY_SPACE));
        
        BoundStatement bound = prepared.bind(Arrays.asList(ids));
        Result<UserSecret> result = instance.getMultiple(bound);
        
        int iCnt = 0;
        
        for(UserSecret us : result) iCnt++;
        
        assertEquals(true, 2 == iCnt);
    }
    
    /**
     * Test of deleteEntry method, of class UserSecretMapper.
     */
    @Test
    public void test_070DeleteEntry() {
        System.out.println("deleteEntry");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.deleteEntry(ids[1]);
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntryAsync method, of class UserSecretMapper.
     */
    @Test
    public void test_080DeleteEntryAsync() {
        System.out.println("deleteEntryAsync");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(us3);
        boolean result = instance.deleteEntryAsync(ids[2]);
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntries method, of class UserSecretMapper.
     */
    @Test
    public void test_090DeleteEntries() {
        System.out.println("deleteEntries");
        UserSecretMapper instance = new UserSecretMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(us2);
        boolean result = instance.deleteEntries(Arrays.asList(ids), "user_uuid");
        
        assertEquals(true, result);
    }    
}
