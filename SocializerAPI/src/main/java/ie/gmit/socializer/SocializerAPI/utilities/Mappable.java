package ie.gmit.socializer.SocializerAPI.utilities;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.mapping.Result;

import java.util.List;
import java.util.UUID;

public interface Mappable<T>{

    public boolean createEntry(T model);

    public void createEntryAsync(T model);

    public BoundStatement getDeleteBoundStatement(UUID entryUUID);
   
    public boolean deleteEntry(UUID entryUUID);
    
    public boolean deleteEntryAsync(UUID entryUUID);
    
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName);

    public boolean executeQuery(String query);
    
    public void updateEntry(T model);
    
    public T getEntry(UUID entryUUID);
    
    public Result<T> getMultiple(BoundStatement bound);
}
