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
