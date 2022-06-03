package hlysine.friendlymonsters.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;

public class MinionMoveGroup {
    private final ArrayList<MinionMove> moves;
    private static final float IMAGE_SIZE = 96.0F;
    private float xStart;
    private float yStart;

    public MinionMoveGroup(float xStart, float yStart) {
        this(new ArrayList<>(), xStart, yStart);
    }

    public MinionMoveGroup(ArrayList<MinionMove> moves, float xStart, float yStart) {
        this.moves = moves;
        this.xStart = xStart;
        this.yStart = yStart;
        updatePositions();
    }

    public void updatePositions() {
        int currentIndex = 0;
        for (MinionMove move : this.moves) {
            move.getHitbox().x = xStart + (IMAGE_SIZE * currentIndex * Settings.scale) - IMAGE_SIZE * Settings.scale;
            move.getHitbox().y = yStart - IMAGE_SIZE * Settings.scale;
            move.setX(xStart + (IMAGE_SIZE * currentIndex * Settings.scale) - IMAGE_SIZE * Settings.scale);
            move.setY(yStart - IMAGE_SIZE * Settings.scale);
            currentIndex++;
        }
    }

    public void addMove(MinionMove move) {
        moves.add(move);
        updatePositions();
    }

    public ArrayList<MinionMove> getMoves() {
        return new ArrayList<>(this.moves);
    }

    public boolean hasMove(String id) {
        return this.moves.stream().anyMatch(move -> move.getID().equals(id));
    }

    public MinionMove removeMove(String moveID) {
        for (MinionMove move : moves) {
            if (move.getID().equals(moveID)) {
                moves.remove(move);
                updatePositions();
                return move;
            }
        }
        updatePositions();
        return null;
    }

    public void clearMoves() {
        this.moves.clear();
        updatePositions();
    }

    public void render(SpriteBatch sb) {
        for (MinionMove move : moves) {
            move.getHitbox().render(sb);
            sb.setColor(Color.WHITE);
            drawMoveImage(move, sb, move.getMoveImage());
        }
    }

    public void update() {
        moves.forEach(MinionMove::update);
    }

    public void setXStart(float xStart) {
        this.xStart = xStart;
    }

    public void setYStart(float yStart) {
        this.yStart = yStart;
    }

    public float getXStart() {
        return this.xStart;
    }

    public float getYStart() {
        return this.yStart;
    }

    protected void drawMoveImage(MinionMove move, SpriteBatch sb, Texture moveImage) {
        sb.draw(
                moveImage,
                move.getHitbox().x, move.getHitbox().y,
                48.0f, 48.0f,
                IMAGE_SIZE, IMAGE_SIZE,
                Settings.scale * 1.5f, Settings.scale * 1.5f,
                0.0f,
                0, 0,
                moveImage.getWidth(), moveImage.getHeight(),
                false, false
        );
    }
}
