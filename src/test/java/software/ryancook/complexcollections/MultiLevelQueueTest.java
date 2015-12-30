package software.ryancook.complexcollections;

import org.junit.*;
import static org.junit.Assert.*;

public class MultiLevelQueueTest
{
    private MultiLevelQueue<String> moves;

    @Before
    public void setUp() throws Exception
    {
        moves = new MultiLevelQueue<>();
    }

    @Test
    public void singleLevel() throws Exception
    {
        moves.addLevel("Test");
        moves.add("a1-a2");
        moves.add("a1-a3");
        moves.add("a1-a4");

        String move = moves.next();
        Assert.assertEquals("a1-a2", move);

        move = moves.next();
        Assert.assertEquals("a1-a3", move);

        move = moves.next();
        Assert.assertEquals("a1-a4", move);

        move = moves.next();
        assertEquals(null, move);
    }

    @Test
    public void multiLevelCrossOver() throws Exception
    {
        setMultiLevelMoves();
        moves.next();
        moves.next();

        final String lastMoveInFirstLevel = moves.next();
        assertEquals("a1-a4", lastMoveInFirstLevel);

        final String firstMoveInSecondLevel = moves.next();
        assertEquals("a1-b2", firstMoveInSecondLevel);
    }

    @Test
    public void removeLevelAndCrossOver() throws Exception
    {
        setMultiLevelMoves();

        final String lastMoveInFirstLevel = moves.next();
        assertEquals("a1-a2", lastMoveInFirstLevel);

        moves.removeLevel();

        final String firstMoveInSecondLevel = moves.next();
        assertEquals("a1-b2", firstMoveInSecondLevel);
    }

    @Test
    public void multiLevelEnd() throws Exception
    {
        setMultiLevelMoves();
        for (int i = 0; i < 8; i++) {
            moves.next();
        }

        assertTrue(moves.size() > 0);

        final String lastLevelLastMove = moves.next();
        assertEquals("a1-d1", lastLevelLastMove);

        assertFalse(moves.size() > 0);

        final String nullMove = moves.next();
        assertEquals(null, nullMove);
    }

    private void setMultiLevelMoves()
    {
        moves.addLevel("Level 1");
        moves.add("a1-a2");
        moves.add("a1-a3");
        moves.add("a1-a4");

        moves.addLevel("Level 2");
        moves.add("a1-b2");
        moves.add("a1-c3");
        moves.add("a1-d4");

        moves.addLevel("Level 3");
        moves.add("a1-b1");
        moves.add("a1-c1");
        moves.add("a1-d1");
    }
}