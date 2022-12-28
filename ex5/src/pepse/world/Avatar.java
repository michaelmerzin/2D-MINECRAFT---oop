package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final String avatarImagePath = "animation/static.png";
    private static final String leftRun = "animation/runLeft.png";
    private static final String rightRun = "animation/runRight.png";
    private static final String FlyingUp = "animation/flyingUp.png";
    private static final String FlyingLeft = "animation/flyingLeft.png";
    private static final String FlyingRight = "animation/flyingRight.png";
    private static final float SIZE_RATIO_TO_BASIC = 50;
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 600;
    private static final float ACCELERATION_Y = 500;
    private static final String AVATAR_TAG = "Avatar";
    private static final int MAX_ENERGY = 200;

    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final ImageRenderable leftImage;
    private final ImageRenderable rightImage;
    private final ImageRenderable staticImage;
    private final ImageRenderable flyingUp;
    private final ImageRenderable flyingLeft;
    private final ImageRenderable flyingRight;
    private final Counter energy ;
//    private final Energy energy_text;




    private Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader,Counter energy) {
        super(pos, Vector2.ONES.multX(SIZE_RATIO_TO_BASIC * 1.25f).multY(SIZE_RATIO_TO_BASIC * 1.5f),
                new ImageRenderable(imageReader.readImage(avatarImagePath, true).getImage()));
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;

        this.flyingUp = new ImageRenderable(imageReader.readImage(FlyingUp, true).getImage());
        this.leftImage = new ImageRenderable(imageReader.readImage(leftRun, true).getImage());
        this.rightImage = new ImageRenderable(imageReader.readImage(rightRun, true).getImage());
        this.staticImage = new ImageRenderable(imageReader.readImage(avatarImagePath, true).getImage());
        this.flyingLeft = new ImageRenderable(imageReader.readImage(FlyingLeft, true).getImage());
        this.flyingRight = new ImageRenderable(imageReader.readImage(FlyingRight, true).getImage());
        this.energy=energy;




    }

    public static Avatar create(GameObjectCollection gameObjects, int avatarLayer,
                                Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        Counter energy=new Counter(MAX_ENERGY);
        Avatar avatar = new Avatar(topLeftCorner, inputListener, imageReader,energy);
        gameObjects.addGameObject(avatar, avatarLayer);
        avatar.setTag(AVATAR_TAG);
        //avatar cant enter the inside terrain
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);

        Energy energy_text=new Energy(energy);
        energy_text.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects.addGameObject(energy_text,Layer.UI);

        return avatar;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleMovement();
    }

    private void handleMovement() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            super.renderer().setRenderable(leftImage);
            xVel -= VELOCITY_X;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            super.renderer().setRenderable(rightImage);
            xVel += VELOCITY_X;

        }
        if (!inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)&&!inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            super.renderer().setRenderable(staticImage);
            if(this.energy.value()<MAX_ENERGY) {
                this.energy.increment();
            }
        }

        this.transform().setVelocityX(xVel);

        if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                &&this.energy.value()>0) {
            this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
            this.transform().setVelocityY(VELOCITY_Y);
            this.transform().setAccelerationY(ACCELERATION_Y);
            super.renderer().setRenderable(flyingUp);
            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                super.renderer().setRenderable(flyingRight);
            }
            if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
                super.renderer().setRenderable(flyingLeft);
            }
            if(this.energy.value()>0) {
                this.energy.decrement();
            }

        }

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            this.transform().setVelocityY(VELOCITY_Y);
            this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals("ground")) {
            this.setVelocity(Vector2.ZERO);
        }
        super.onCollisionEnter(other, collision);

    }

}
