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
 * @description OauthTokenMapperTest - Collection of test cases for OauthTokenMapper
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

//Required to keep database clean after test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OauthTokenMapperTest {
    
    static Cluster cluster;
    static OauthToken ot1;
    static OauthToken ot2;
    static OauthToken ot3;
    static UUID[] ids;
    static UUID user_uuid;
    static UUID client_uuid;
    static final String KEY_SPACE = "app_data";
    
    public OauthTokenMapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        long currenTime = System.currentTimeMillis();
        cluster = CassandraConnector.initalizeConnection(KEY_SPACE);
        ids = new UUID[]{UUIDs.random(), UUIDs.random(), UUIDs.random()};
        user_uuid = UUIDs.random();
        client_uuid = UUIDs.random();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3600);
        
        ot1 = new OauthToken(client_uuid, user_uuid, calendar.getTime(), "full", "client_credentials");
        ot2 = new OauthToken(client_uuid, user_uuid, calendar.getTime(), "full", "client_credentials");
        ot3 = new OauthToken(client_uuid, user_uuid, calendar.getTime(), "full", "client_credentials");
    }
    
    @Before
    public void setUp() {
        System.out.printf("\nExecuting test for %s class, method test: ", MessageMapper.class.getName());
    }
    
    /**
     * Test of initializeMapper method, of class OauthTokenMapper.
     */
    @Test
    public void test_010InitializeMapper() {
        System.out.println("initializeMapper");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        instance.initializeMapper();
        
        Exception exc = null;

        try {
            instance.initializeMapper();
        } catch (Exception e) {
            exc = e;
        }

        assertEquals(true, null == exc);
    }

    /**
     * Test of createEntry method, of class OauthTokenMapper.
     */
    @Test
    public void test_020CreateEntry() {
        System.out.println("createEntry");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        
        boolean result = instance.createEntry(ot1);
        
        assertEquals(true, result);
    }

    /**
     * Test of createEntryAsync method, of class OauthTokenMapper.
     */
    @Test
    public void test_030CreateEntryAsync() {
        System.out.println("createEntryAsync");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        Exception exc = null;

        try {
            instance.createEntryAsync(ot2);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        
        assertEquals(true, null == exc);
    }
    
    /**
     * Test of getEntry method, of class OauthTokenMapper.
     */
    @Test
    public void test_040GetEntry() {
        System.out.println("getEntry");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        OauthToken result = instance.getEntry(ot1.getAccess_token());
        
        assertEquals(true, result != null && result.getExpires().equals(ot1.getExpires()));
    }

    /**
     * Test of getMultiple method, of class OauthTokenMapper.
     */
    @Test
    public void test_050GetMultiple() {
        System.out.println("getMultiple");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        PreparedStatement prepared = instance.session.prepare(String.format("select * from %s.oauth2_data where access_token in ?", KEY_SPACE));
        
        BoundStatement bound = prepared.bind(Arrays.asList(new UUID[]{ot1.getAccess_token(), ot2.getAccess_token()}));
        Result<OauthToken> result = instance.getMultiple(bound);
        int iCnt = 0;
        
        for(OauthToken ms : result) iCnt++;
        
        assertEquals(true, 2 == iCnt);
    }
    
    /**
     * Test of updateEntry method, of class OauthTokenMapper.
     */
    @Test
    public void test_060UpdateEntry() {
        System.out.println("updateEntry");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        ot1.setScope("user");
        Exception exc = null;

        try {
            instance.updateEntry(ot1);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        
        assertEquals(true, null == exc);
    }

    /**
     * Test of deleteEntry method, of class OauthTokenMapper.
     */
    @Test
    public void test_070DeleteEntry() {
        System.out.println("deleteEntry");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.deleteEntry(ot2.getAccess_token());
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntryAsync method, of class OauthTokenMapper.
     */
    @Test
    public void test_080DeleteEntryAsync() {
        System.out.println("deleteEntryAsync");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(ot3);
        boolean result = instance.deleteEntryAsync(ot3.getAccess_token());
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntries method, of class OauthTokenMapper.
     */
    @Test
    public void test_090DeleteEntries() {
        System.out.println("deleteEntries");
        OauthTokenMapper instance = new OauthTokenMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(ot2);
        boolean result = instance.deleteEntries(Arrays.asList(ids), "access_token");
        
        assertEquals(true, result);
    }
    
    /**
     * Test of getDateDiff method, of class OauthTokenMapper.
     */
    @Test
    public void test_091getDateDiff(){
        System.out.println("getDateDiff");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 3);
        
        long difference = OauthTokenMapper.getDateDiff(new Date(), calendar.getTime(), TimeUnit.MINUTES);
        
        assertEquals(true, difference >= 2);
    }
    
}
