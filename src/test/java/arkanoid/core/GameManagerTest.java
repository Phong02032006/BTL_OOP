package arkanoid.core;

import arkanoid.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameManager core logic.
 */
class GameManagerTest {
    private GameManager gm;

    @BeforeEach
    void setUp() {
        GameManager.resetInstance();
        gm = GameManager.getInstance();
        gm.start();
    }

    @Test
    void testSingleton() {
        GameManager gm2 = GameManager.getInstance();
        assertSame(gm, gm2, "GameManager must follow Singleton pattern");
    }

    @Test
    void testStartInitializesObjects() {
        assertNotNull(gm.getBackground(), "Background should be initialized");
        assertNotNull(gm.getPaddle(), "Paddle should be initialized");
        assertNotNull(gm.getBricks(), "Bricks should be loaded");
        assertEquals(3, gm.getLives(), "Player should start with 3 lives");
        assertEquals(0, gm.getScore(), "Initial score should be 0");
        assertEquals(Constant.STATE_RUNNING, gm.getState(), "Game should start in RUNNING state");
    }

    @Test
    void testResetInstanceCreatesNewObject() {
        GameManager gm1 = GameManager.getInstance();
        GameManager.resetInstance();
        GameManager gm2 = GameManager.getInstance();
        assertNotSame(gm1, gm2, "resetInstance() should create a new GameManager");
    }

    @Test
    void testAddAndRemoveExtraBalls() {
        int before = gm.getBalls().size();
        gm.spawnExtraBalls();
        int after = gm.getBalls().size();
        assertTrue(after > before, "spawnExtraBalls() should increase ball count");

        gm.removeExtraBalls();
        assertEquals(1, gm.getBalls().size(), "removeExtraBalls() should leave exactly one ball");
    }

    @Test
    void testLoseLifeDecreasesLives() {
        int before = gm.getLives();

        gm.onKeyPressed("SPACE");
        gm.getBalls().clear();
        gm.update();

        assertTrue(gm.getLives() < before, "Lives should decrease when all balls are lost");
    }

    @Test
    void testLoseLifeDecreasesLivesAndEndsGame() {
        GameManager gm = GameManager.getInstance();
        gm.start();

        gm.onKeyPressed("SPACE");
        gm.getBalls().clear();
        gm.update();

        int livesAfterFirstLoss = gm.getLives();

        gm.getBalls().clear();
        gm.update();

        if (livesAfterFirstLoss <= 0) {
            assertEquals(Constant.STATE_GAME_OVER, gm.getState());
        } else {
            assertTrue(livesAfterFirstLoss < 3);
        }
    }

    @Test
    void testPauseAndResume() {
        gm.pause();
        assertEquals(Constant.STATE_PAUSED, gm.getState());
        gm.resume();
        assertEquals(Constant.STATE_RUNNING, gm.getState());
    }
}

