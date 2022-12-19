package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 73);
    private static final double GROUND_RADIO = 0.3;
    private static final int TERRAIN_DEPTH = 20;
    private static final String BLOCK_TAG = "ground";
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private int height;

    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.groundLayer = groundLayer;
        this.gameObjects = gameObjects;
        this.groundHeightAtX0 = (float) (windowDimensions.y() * GROUND_RADIO);
        this.height = (int) windowDimensions.y();
    }

    public float groundHeightAt(float x) {
        return groundHeightAtX0;
    }

    public void createInRange(int minX, int maxX) {
        int startX = calcStart(minX);
        int endX = calcEnd(maxX);
        Block block;
        int y;
        RectangleRenderable render = new RectangleRenderable(BASE_GROUND_COLOR);
        for (int x = startX; x < endX; x += Block.SIZE) {
            y = calcTopY(x);
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                y -= Block.SIZE;
                block = createBlock(new Vector2(x, height - y), render);
                gameObjects.addGameObject(block, groundLayer);
                block.setTag(BLOCK_TAG);
            }
        }


    }

    private int calcTopY(int x) {
        return (int) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
    }

    private int calcStart(int start) {
        return start - (start % Block.SIZE);

    }

    private int calcEnd(int end) {
        return end + (end % Block.SIZE);
    }

    private Block createBlock(Vector2 cords, Renderable renderable) {
        return new Block(cords, renderable);
    }
}
