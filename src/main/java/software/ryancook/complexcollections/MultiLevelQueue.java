package software.ryancook.complexcollections;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.Map.Entry;

public class MultiLevelQueue<E>
{
    private int size;
    private TreeMap<String, Queue<E>> levels;

    /**
     * Create a new Multi Level Queue
     */
    public MultiLevelQueue()
    {
        levels = new TreeMap<>();
    }

    /**
     * Adds a new level with an empty queue.
     *
     * @param levelName  the name to be used as the level key, not null
     * @throws InvalidParameterException if levelName is null
     * @throws DuplicateLevelException if levelName is not unique
     */
    public void addLevel(String levelName)
    {
        if (levelName == null) {
            throw new InvalidParameterException();
        }
        checkIfLevelNameIsUnique(levelName);
        levels.put(levelName, new LinkedList<>());
    }

    private void checkIfLevelNameIsUnique(String levelName)
    {
        if (levels.containsKey(levelName)) {
            throw new DuplicateLevelException();
        }
    }

    /**
     * Removes the queue at the current level.
     */
    public void removeLevel()
    {
        clearLevelAndDecreaseCount();
    }

    private void clearLevelAndDecreaseCount()
    {
        Queue<E> level = getHeadLevel();
        size -= level.size();
        level.clear();
    }

    private Queue<E> getHeadLevel()
    {
        Entry<String, Queue<E>> firstEntry = levels.firstEntry();
        if (firstEntry == null) {
            throw new LevelNotFoundException();
        }
        return firstEntry.getValue();
    }

    /**
     * Adds an object to the queue at the current level.
     *
     * @param obj
     */
    public void add(E obj)
    {
        Queue<E> level = getTailLevel();
        level.add(obj);
        size++;
    }

    private Queue<E> getTailLevel()
    {
        Entry<String, Queue<E>> lastEntry = levels.lastEntry();
        if (lastEntry == null) {
            throw new LevelNotFoundException();
        }
        return lastEntry.getValue();
    }

    /**
     * Removes the first element from the first queue and returns that element.
     *
     * @return
     */
    public E next()
    {
        removeLevelIfEmpty();
        if (noLevelsExist()) {
            return null;
        }
        return popNextObject();
    }

    private E popNextObject()
    {
        Queue<E> level = getHeadLevel();
        if (level.size() == 0) {
            return null;
        }
        size--;
        return level.remove();
    }

    private void removeLevelIfEmpty()
    {
        while (levels.size() > 0) {
            Entry<String, Queue<E>> levelEntry = levels.firstEntry();
            if (levelEntry.getValue().size() == 0) {
                levels.remove(levelEntry.getKey());
                continue;
            }
            break;
        }
    }

    private boolean noLevelsExist()
    {
        return levels.size() == 0;
    }

    /**
     * Returns the total number of elements in the multilevel queue.
     *
     * @return
     */
    public int size()
    {
        return size;
    }
}
