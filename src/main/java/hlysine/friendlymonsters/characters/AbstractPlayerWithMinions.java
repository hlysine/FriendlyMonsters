package hlysine.friendlymonsters.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;

public abstract class AbstractPlayerWithMinions extends CustomPlayer {
    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, String model, String animation) {
        super(name, playerClass, orbTextures, orbVfxPath, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, String model, String animation) {
        super(name, playerClass, energyOrbInterface, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, String model, String animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, AbstractAnimation animation) {
        super(name, playerClass, energyOrbInterface, animation);
    }

    /**
     * Get the {@link MonsterGroup} containing all minions of this player.
     * <p>
     * Alias for {@code MinionUtils.getMinions(this)}.
     *
     * @return All minions of this player contained in a {@link MonsterGroup}.
     */
    public MonsterGroup getMinions() {
        return MinionUtils.getMinions(this);
    }

    /**
     * Get the {@link AbstractFriendlyMonster} with a matching ID.
     * <p>
     * Alias for {@code MinionUtils.getMinion(this, minionID)}.
     *
     * @return The {@link AbstractFriendlyMonster} with a matching ID.
     */
    public AbstractFriendlyMonster getMinion(String minionID) {
        return MinionUtils.getMinion(this, minionID);
    }

    /**
     * Get whether this player has any minions.
     * <p>
     * Alias for {@code MinionUtils.hasMinions(this)}.
     *
     * @return True if the player has 1 or more minions, false otherwise.
     */
    public boolean hasMinions() {
        return MinionUtils.hasMinions(this);
    }

    /**
     * Get whether this player has a minion with a matching ID.
     * <p>
     * Alias for {@code MinionUtils.hasMinion(this, minionId)}.
     *
     * @return True if the player has a minion with a matching ID, false otherwise.
     */
    public boolean hasMinion(String minionId) {
        return MinionUtils.hasMinion(this, minionId);
    }

    /**
     * Add a minion to this player. This method will do nothing and return false if
     * the current minion count is already >= max minion count.
     * The minion will be initialized and triggered upon adding.
     * <p>
     * Alias for {@code MinionUtils.addMinion(this, minion)}.
     *
     * @return Whether the addition was successful.
     */
    public boolean addMinion(AbstractFriendlyMonster minion) {
        return MinionUtils.addMinion(this, minion);
    }

    /**
     * Remove a minion from the player. This does not kill the minion.
     * <p>
     * Alias for {@code MinionUtils.removeMinion(this, minion)}.
     *
     * @return Whether the player originally had this minion.
     */
    public boolean removeMinion(AbstractFriendlyMonster minion) {
        return MinionUtils.removeMinion(this, minion);
    }

    /**
     * Remove all minions from the player. This does not kill the minions,
     * but does change the {@link MonsterGroup} instance.
     * <p>
     * Alias for {@code MinionUtils.clearMinions(this)}.
     */
    public void clearMinions() {
        MinionUtils.clearMinions(this);
    }

    /**
     * Get a random minion that belongs to the player using the AI RNG.
     * <p>
     * Alias for {@code MinionUtils.getRandomMinion(player, aliveOnly)}
     *
     * @param player    The player to get the minion from.
     * @param aliveOnly Whether to only consider minions that are still alive.
     * @return A random minion.
     */
    public static AbstractMonster getRandomMinion(AbstractPlayer player, boolean aliveOnly) {
        return MinionUtils.getRandomMinion(player, aliveOnly);
    }

    /**
     * Get the default maximum allowed number of minions of a player.
     * The maximum allowed number of minions is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.getBaseMinionCount(this)}.
     *
     * @return The default maximum allowed number of minions of a player.
     */
    public int getBaseMinionCount() {
        return MinionUtils.getBaseMinionCount(this);
    }

    /**
     * Set the default maximum allowed number of minions of a player.
     * The maximum allowed number of minions is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.setBaseMinionCount(this, newVal)}.
     * </p>
     * <br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public void setBaseMinionCount(int newVal) {
        MinionUtils.setBaseMinionCount(this, newVal);
    }

    /**
     * Get the maximum allowed number of minions of a player.
     * This value decides the actual allowed number of minions and is reset at the start of combat.
     * <p>
     * Alias for {@code MinionUtils.getMaxMinionCount(this)}.
     *
     * @return The current maximum allowed number of minions of a player.
     */
    public int getMaxMinionCount() {
        return MinionUtils.getMaxMinionCount(this);
    }

    /**
     * Set the maximum allowed number of minions of a player.
     * This value decides the actual allowed number of minions and is reset at the start of combat.
     * If this value is decreased while the player already has max number of minions, the minions stay
     * but the player cannot create new minions until the number of minions drops below the allowed number.
     * <p>
     * Alias for {@code MinionUtils.setMaxMinionCount(this, newVal)}.
     */
    public void setMaxMinionCount(int newVal) {
        MinionUtils.setMaxMinionCount(this, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.getBaseMinionPowerChance(this)}.
     *
     * @return The default chance of redirecting an applicable power.
     */
    public float getBaseMinionPowerChance() {
        return MinionUtils.getBaseMinionPowerChance(this);
    }

    /**
     * Set the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.setBaseMinionPowerChance(this, newVal)}.
     * </p>
     * <br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public void setBaseMinionPowerChance(float newVal) {
        MinionUtils.setBaseMinionPowerChance(this, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * This value decides the actual allowed chance and is reset at the start of combat.
     * <p>
     * Alias for {@code MinionUtils.getMinionPowerChance(this)}.
     *
     * @return The current chance of redirecting an applicable power.
     */
    public float getMinionPowerChance() {
        return MinionUtils.getMinionPowerChance(this);
    }

    /**
     * Set the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * This value decides the actual allowed chance and is reset at the start of combat.
     * <p>
     * Alias for {@code MinionUtils.setMinionPowerChance(this, newVal)}.
     */
    public void setMinionPowerChance(float newVal) {
        MinionUtils.setMinionPowerChance(this, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.getBaseMinionAttackTargetChance(this)}.
     *
     * @return The default chance of redirecting an attack intent.
     */
    public float getBaseMinionAttackTargetChance() {
        return MinionUtils.getBaseMinionAttackTargetChance(this);
    }

    /**
     * Set the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <p>
     * Alias for {@code MinionUtils.setBaseMinionAttackTargetChance(this, newVal)}.
     * </p>
     * <br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public void setBaseMinionAttackTargetChance(float newVal) {
        MinionUtils.setBaseMinionAttackTargetChance(this, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * This value decides the actual allowed chance and is reset at the start of combat.
     * <p>
     * Alias for {@code MinionUtils.getMinionAttackTargetChance(this)}.
     *
     * @return The current chance of redirecting an attack intent.
     */
    public float getMinionAttackTargetChance() {
        return MinionUtils.getMinionAttackTargetChance(this);
    }

    /**
     * Set the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * This value decides the actual allowed chance and is reset at the start of combat.
     * <p>
     * Alias for {@code MinionUtils.setMinionAttackTargetChance(this, newVal)}.
     */
    public void setMinionAttackTargetChance(float newVal) {
        MinionUtils.setMinionAttackTargetChance(this, newVal);
    }
}
