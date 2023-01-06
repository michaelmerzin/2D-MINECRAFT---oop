package pepse.world;


import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;
import java.util.function.Function;

public class Block extends GameObject {

    public static final int SIZE = 30;
    private Function<Float, Float> heightAtX;
    private Vector2 windowDimensions;

    /**
     *
     * @param topLeftCorner the top left corner of the block
     * @param renderable the renderable
     * @param heightAtX the function that returns the height of the block at a given x position
     * @param windowDimensions the dimensions of the window
     */
    public Block(Vector2 topLeftCorner, Renderable renderable, Function<Float, Float> heightAtX, Vector2 windowDimensions) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.heightAtX = heightAtX;
        this.windowDimensions = windowDimensions;
    }

    /**
     *
     * @param topLeftCorner the top left corner of the block
     * @param renderable the renderable
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    public void onCollisionEnter(GameObject other, Collision collision) {
        if (this.getTag().equals("leaves")) {
            this.setVelocity(Vector2.ZERO);
        }
    }


}
