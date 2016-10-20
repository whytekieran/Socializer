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
 * @description MessageSessionModelMapperTest - Test cases for MessageSessionModelMapper
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.model;

import ie.gmit.socializer.services.chat.model.MessageModelMapper;
import ie.gmit.socializer.services.chat.model.MessageSessionModel;
import ie.gmit.socializer.services.chat.model.MessageSessionModelMapper;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Result;
import static ie.gmit.socializer.services.chat.model.MessageModelMapperTest.cluster;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

//Required to keep database clean after test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageSessionModelMapperTest {
    
    static Cluster cluster;
    static MessageSessionModel msm1;
    static MessageSessionModel msm2;
    static MessageSessionModel msm3;
    static UUID[] ids;
    static UUID[] user_uuids;
    static final String KEY_SPACE = "app_user_data";
    
    public MessageSessionModelMapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        long currenTime = System.currentTimeMillis();
        cluster = CassandraConnector.initalizeConnection(KEY_SPACE);
        ids = new UUID[]{UUIDs.random(), UUIDs.random(), UUIDs.random()};
        user_uuids = new UUID[]{UUIDs.random(), UUIDs.random()};
        
        msm1 = new MessageSessionModel(ids[0], Arrays.asList(user_uuids), "Test session 1", 1, currenTime, currenTime);
        msm2 = new MessageSessionModel(ids[1], Arrays.asList(user_uuids), "Test session 2", 1, currenTime, currenTime);
        msm3 = new MessageSessionModel(ids[2], Arrays.asList(user_uuids), "Test session 3", 1, currenTime, currenTime);
    }
    
    
    @Before
    public void setUp() {
        System.out.printf("\nExecuting test for %s class, method test: ", MessageModelMapper.class.getName());
    }

    /**
     * Test of initializeMapper method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_001InitializeMapper() {
        System.out.println("initializeMapper");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
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
     * Test of createEntry method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_002CreateEntry() {
        System.out.println("createEntry");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.createEntry(msm1);
        
        assertEquals(true, result);
    }

    /**
     * Test of createEntryAsync method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_003CreateEntryAsync() {
        System.out.println("createEntryAsync");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        
        Exception exc = null;

        try {
            instance.createEntryAsync(msm2);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        
        assertEquals(true, null == exc);
    }

    /**
     * Test of getEntry method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_004GetEntry() {
        System.out.println("getEntry");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        MessageSessionModel result = (MessageSessionModel)instance.getEntry(msm1.getMsession_uuid());
        
        assertEquals(true, null != result && result.getMsession_uuid().compareTo(msm1.getMsession_uuid()) == 0);
    }

    /**
     * Test of getMultiple method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_005GetMultiple() {
        System.out.println("getMultiple");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        PreparedStatement prepared = instance.session.prepare("select * from app_user_data.message_session where msession_uuid in ?");
        BoundStatement bound = prepared.bind(Arrays.asList(ids));
        Result<MessageSessionModel> result = instance.getMultiple(bound);
        int iCnt = 0;
        
        for(MessageSessionModel ms : result) iCnt++;
        
        assertEquals(true, 2 == iCnt);
    }

    /**
     * Test of updateEntry method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_006UpdateEntry() {
        System.out.println("updateEntry");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        msm1.setName("Updated name");
        Exception exc = null;

        try {
            instance.updateEntry(msm2);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        assertEquals(true, null == exc);
    }
    
    /**
     * Test of deleteEntry method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_007DeleteEntry() {
        System.out.println("deleteEntry");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.deleteEntry(msm2.getMsession_uuid());
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntryAsync method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_008DeleteEntryAsync() {
        System.out.println("deleteEntryAsync");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(msm3);
        
        boolean result = instance.deleteEntryAsync(msm3.getMsession_uuid());
        
        assertEquals(true, result);
    }

    /**
     * Test of deleteEntries method, of class MessageSessionModelMapper.
     */
    @Test
    public void test_009DeleteEntries() {
        System.out.println("deleteEntries");
        MessageSessionModelMapper instance = new MessageSessionModelMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(msm2);
        boolean result = instance.deleteEntries(Arrays.asList(ids), "msession_uuid");
        
        assertEquals(true, result);
    }
}
