package hlysine.friendlymonsters.monsters;

import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import hlysine.friendlymonsters.FriendlyMonsters;

public class MinionMove extends ClickableUIElement {
    private final String ID;
    private String moveDescription;
    private Texture moveImage;
    public final Runnable moveActions;
    public final AbstractFriendlyMonster owner;

    /**
     * Create a new {@link MinionMove} instance.
     *
     * @param ID              The unique ID of this move.
     * @param owner           The minion that this move belongs to.
     * @param moveImage       The image to show next to the minion for the player to choose this move.
     * @param moveDescription The description that shows when the user hovers over this move.
     * @param moveActions     The action to execute when this move is chosen.
     */
    public MinionMove(String ID, AbstractFriendlyMonster owner, Texture moveImage, String moveDescription, Runnable moveActions) {
        super(moveImage, 0, 0, 96.0f, 96.0f);
        this.moveImage = moveImage;
        this.moveActions = moveActions;
        this.ID = ID;
        this.moveDescription = moveDescription;
        this.owner = owner;
    }

    private void doMove() {
        if (moveActions != null) {
            moveActions.run();
        } else {
            FriendlyMonsters.logger.info("MinionMove: " + this.ID + " had no actions!");
        }
    }

    public Hitbox getHitbox() {
        return this.hitbox;
    }

    public String getID() {
        return this.ID;
    }

    public Texture getMoveImage() {
        return this.moveImage;
    }

    public void setMoveImage(Texture moveImage) {
        this.moveImage = moveImage;
    }

    public String getMoveDescription() {
        return this.moveDescription;
    }

    public void setMoveDescription(String newDescription) {
        this.moveDescription = newDescription;
    }

    @Override
    protected void onHover() {
        TipHelper.renderGenericTip(this.x, this.y - 15f * Settings.scale, this.ID, this.moveDescription);
    }

    @Override
    protected void onUnhover() {

    }

    @Override
    protected void onClick() {
        if (!AbstractDungeon.actionManager.turnHasEnded && !this.owner.hasTakenTurn()) {
            this.owner.setTakenTurn(true);
            this.doMove();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.actionManager.turnHasEnded || this.owner.hasTakenTurn()) {
            super.render(sb, Color.GRAY);
        } else {
            super.render(sb);
        }
    }
}
