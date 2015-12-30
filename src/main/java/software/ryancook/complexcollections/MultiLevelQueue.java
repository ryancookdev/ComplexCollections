package software.ryancook.complexcollections;

import java.util.*;
import java.util.Map.Entry;

public class MultiLevelQueue<E>
{
    private int size;
    final private TreeMap<String, Queue<E>> levels;

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
     * @throws NullPointerException if levelName is null
     * @throws DuplicateLevelException if levelName is not unique
     */
    public void addLevel(final String levelName)
    {
        if (levelName == null) {
            throw new NullPointerException("Level name cannot be null.");
        }
        checkIfLevelNameIsUnique(levelName);
        levels.put(levelName, new LinkedList<>());
    }

    private void checkIfLevelNameIsUnique(final String levelName)
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
        final Queue<E> level = getHeadLevel();
        size -= level.size();
        level.clear();
    }

    private Queue<E> getHeadLevel()
    {
        final Entry<String, Queue<E>> firstEntry = levels.firstEntry();
        if (firstEntry == null) {
            throw new LevelNotFoundException();
        }
        return firstEntry.getValue();
    }

    /**
     * Adds an object to the queue at the current level.
     *
     * @param obj Element to be added
     */
    public void add(final E obj)
    {
        final Queue<E> level = getTailLevel();
        level.add(obj);
        size++;
    }

    private Queue<E> getTailLevel()
    {
        final Entry<String, Queue<E>> lastEntry = levels.lastEntry();
        if (lastEntry == null) {
            throw new LevelNotFoundException();
        }
        return lastEntry.getValue();
    }

    /**
     * Removes the first element from the first queue and returns that element.
     *
     * @return First element from the first queue
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
        final Queue<E> level = getHeadLevel();
        if (level.size() == 0) {
            return null;
        }
        size--;
        return level.remove();
    }

    private void removeLevelIfEmpty()
    {
        while (levels.size() > 0) {
            final Entry<String, Queue<E>> levelEntry = levels.firstEntry();
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
     * @return Size of the multilevel queue
     */
    public int size()
    {
        return size;
    }
}
