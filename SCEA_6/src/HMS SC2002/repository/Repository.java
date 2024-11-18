//repository/Repository.java

package repository;

import entity.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code Repository} interface defines the basic operations that can be performed on an entity in a repository.
 * It is a generic interface that works with any entity type {@code T} and a unique identifier type {@code ID}.
 * Implementations of this interface should provide the actual behavior for managing the entities.
 *
 * @param <T> The type of entity that the repository will manage (e.g., {@code Patient}, {@code Medicine}).
 * @param <ID> The type of the unique identifier for the entity (e.g., {@code String}, {@code Integer}).
 */
public interface Repository<T, ID> {
    
    /**
     * Saves the given entity to the repository.
     * If the entity already exists, it may be updated or overwritten depending on the implementation.
     *
     * @param entity The entity to be saved.
     * @return The saved entity.
     */
    T save(T entity);
    
    /**
     * Finds an entity by its unique identifier.
     * 
     * @param id The unique identifier of the entity.
     * @return An {@code Optional} containing the entity if found, or an empty {@code Optional} if not found.
     */
    Optional<T> findById(ID id);
    
    /**
     * Retrieves all entities in the repository.
     * 
     * @return A list containing all the entities.
     */
    List<T> findAll();
    
    /**
     * Deletes an entity from the repository by its unique identifier.
     * 
     * @param id The unique identifier of the entity to be deleted.
     */
    void delete(ID id);
    
    /**
     * Checks if an entity exists in the repository by its unique identifier.
     * 
     * @param id The unique identifier of the entity to check.
     * @return {@code true} if the entity exists, {@code false} otherwise.
     */
    boolean exists(ID id);
    
    /**
     * Clears all entities from the repository.
     * This will remove all stored entities.
     */
    void clearAll();
}

