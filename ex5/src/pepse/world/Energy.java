package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Energy extends GameObject {
    private static final int TEXT_WIDTH = 70;
    private static final int TEXT_HEIGHT = 70;
    private static final int ENERGY_RATE_CHANGE = 2;
    private final TextRenderable energyText;
    private final Counter energy;
    /**
     * Creates a new energy object
     * @param position The position of the energy object
     * @param energy The initial energy
     */
    Energy(Counter energyOfAvatar)
    {
        super(Vector2.ZERO,new Vector2(TEXT_WIDTH,TEXT_HEIGHT), null);
        this.energy=energyOfAvatar;
        String str=""+(energyOfAvatar.value()/ENERGY_RATE_CHANGE);
        this.energyText=new TextRenderable(str);
        this.renderer().setRenderable(this.energyText);
    }
    /**
     * Updates the energy text
     */
    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        this.energyText.setString("" + (this.energy.value()/ENERGY_RATE_CHANGE));

    }

}
