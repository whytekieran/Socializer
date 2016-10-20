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
 * @description MessageModelMapperTest - Test cases for message model
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.model;

import ie.gmit.socializer.services.chat.model.MessageModelMapper;
import ie.gmit.socializer.services.chat.model.MessageModel;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Result;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageModelMapperTest {

    static Cluster cluster;
    static MessageModel mm1;
    static MessageModel mm2;
    static MessageModel mm3;
    static UUID[] ids;
    static UUID user_uuid;
    static UUID msession_uuid;
    static final String KEY_SPACE = "app_user_data";

    public MessageModelMapperTest() {
    }

    @Before
    public void setUp() {
        System.out.printf("\nExecuting test for %s class, method test: ", MessageModelMapper.class.getName());
    }

    @BeforeClass
    public static void before() {
        cluster = CassandraConnector.initalizeConnection(KEY_SPACE);
        ids = new UUID[]{UUIDs.random(), UUIDs.random(), UUIDs.random()};
        user_uuid = UUIDs.random();
        msession_uuid = UUIDs.random();
        mm1 = new MessageModel(ids[0], msession_uuid, user_uuid, "Sample message", 1, System.currentTimeMillis(), System.currentTimeMillis());
        mm2 = new MessageModel(ids[1], msession_uuid, user_uuid, "Sample message", 1, System.currentTimeMillis(), System.currentTimeMillis());
        mm3 = new MessageModel(ids[2], msession_uuid, user_uuid, "Sample message", 1, System.currentTimeMillis(), System.currentTimeMillis());
    }

    /**
     * Test of initializeMapper method, of class MessageModelMapper.
     */
    @Test
    public void test_001InitializeMapper() {
        System.out.println("initializeMapper");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        Exception exc = null;

        try {
            instance.initializeMapper();
        } catch (Exception e) {
            exc = e;
        }

        assertEquals(true, null == exc);
    }

    /**
     * Test of createEntry method, of class MessageModelMapper.
     */
    @Test
    public void test_002CreateEntry() {
        System.out.println("createEntry");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.createEntry(mm1); 

        assertEquals(true, result);
    }

    /**
     * Test of createEntry method, of class MessageModelMapper.
     */
    @Test
    public void test_003CreateEntryAsync() {
        System.out.println("createEntryAsync");
        MessageModel modelable = mm2;
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        Exception exc = null;

        try {
            instance.createEntry(modelable);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        assertEquals(true, null == exc);
    }

    /**
     * Test of updateEntry method, of class MessageModelMapper.
     */
    @Test
    public void test_004UpdateEntry() {
        System.out.println("updateEntry");

        MessageModel modelable = (MessageModel) mm1;
        modelable.setContent("New content");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        Exception exc = null;

        try {
            instance.updateEntry(modelable);
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }
        assertEquals(true, null == exc);
    }

    /**
     * Test of getEntry method, of class MessageModelMapper.
     */
    @Test
    public void test_005GetEntry() {
        System.out.println("getEntry");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        MessageModel result = (MessageModel)instance.getEntry(mm1);
        
        assertEquals(true, mm1.getCreated().compareTo(result.getCreated()) == 0);
    }

    /**
     * Test of getMultiple method, of class MessageModelMapper.
     * This test is testing for get multiple entry with failover on not existing. 
     * The result has to be 2 even with 3 keys as the 3rd does not exist.
     */
    @Test
    public void test_006GetMultiple() {
        System.out.println("getMultiple");

        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        PreparedStatement prepared = instance.session.prepare("select * from app_user_data.message where message_uuid in ? ");
        BoundStatement bound = prepared.bind(Arrays.asList(ids));
        Result<MessageModel> result = instance.getMultiple(bound);

        int cnt=0;
        for(MessageModel m : result) cnt++;
            
        assertEquals(true, cnt == 2);
    }

    /**
     * Test of deleteEntry method, of class MessageModelMapper.
     */
    @Test
    public void test_007DeleteEntry() {
        System.out.println("deleteEntry");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        boolean result = instance.deleteEntry(mm1.getMessage_uuid());
        
        assertEquals(true, result);

    }
    
    /**
     * Test of deleteEntries method, of class MessageModelMapper.
     */
    @Test
    public void test_007DeleteEntries() {
        System.out.println("deleteEntry");
        MessageModelMapper instance = new MessageModelMapper(cluster.newSession(), KEY_SPACE);
        instance.createEntry(mm3);
        
        Exception exc = null;

        try {
            instance.deleteEntries(Arrays.asList(ids), "message_uuid");
        } catch (Exception e) {
            exc = e;
            System.out.println(e);
        }

        assertEquals(true, null == exc);
    }
}
