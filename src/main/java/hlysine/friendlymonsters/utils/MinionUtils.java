package hlysine.friendlymonsters.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.patches.PlayerAddSavableFieldsPatch;

public class MinionUtils {
    /**
     * Get the {@link MonsterGroup} containing all minions of this player.
     *
     * @return All minions of this player contained in a {@link MonsterGroup}.
     */
    public static MonsterGroup getMinions(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player);
    }

    /**
     * Get the {@link AbstractFriendlyMonster} with a matching ID.
     *
     * @return The {@link AbstractFriendlyMonster} with a matching ID.
     */
    public static AbstractFriendlyMonster getMinion(AbstractPlayer player, String minionId) {
        return (AbstractFriendlyMonster) PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).getMonster(minionId);
    }

    /**
     * Get whether this player has any minions.
     *
     * @return True if the player has 1 or more minions, false otherwise.
     */
    public static boolean hasMinions(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).monsters.size() > 0;
    }

    /**
     * Get whether this player has a minion with a matching ID.
     *
     * @return True if the player has a minion with a matching ID, false otherwise.
     */
    public static boolean hasMinion(AbstractPlayer player, String minionId) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).monsters.stream().anyMatch(m -> m.id.equals(minionId));
    }

    /**
     * Add a minion to this player. This method will do nothing and return false if
     * the current minion count is already >= max minion count.
     * The minion will be initialized and triggered upon adding.
     *
     * @return Whether the addition was successful.
     */
    public static boolean addMinion(AbstractPlayer player, AbstractFriendlyMonster minionToAdd) {
        MonsterGroup minions = getMinions(player);
        int maxMinions = getMaxMinionCount(player);

        if (minions.monsters.size() >= maxMinions) {
            return false;
        } else {
            minions.add(minionToAdd);
            minionToAdd.init();
            minionToAdd.usePreBattleAction();
            // minionToAdd.useUniversalPreBattleAction(); // Friendly minions should not be affected by enemy blights
            minionToAdd.showHealthBar();
            return true;
        }
    }

    /**
     * Remove a minion from the player. This does not kill the minion.
     *
     * @return Whether the player originally had this minion.
     */
    public static boolean removeMinion(AbstractPlayer player, AbstractFriendlyMonster minionToRemove) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).monsters.remove(minionToRemove);
    }

    /**
     * Remove all minions from the player. This does not kill the minions,
     * but does change the {@link MonsterGroup} instance.
     */
    public static void clearMinions(AbstractPlayer player) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.set(player, new MonsterGroup(new AbstractFriendlyMonster[]{}));
    }

    /**
     * Get a random minion that belongs to the player using the AI RNG.
     *
     * @param player    The player to get the minion from.
     * @param aliveOnly Whether to only consider minions that are still alive.
     * @return A random minion.
     */
    public static AbstractMonster getRandomMinion(AbstractPlayer player, boolean aliveOnly) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).getRandomMonster(null, aliveOnly, AbstractDungeon.aiRng);
    }

    /**
     * Get the default maximum allowed number of minions of a player.
     * The maximum allowed number of minions is reset to this value at the start of combat.
     * This value is persisted in save games.
     *
     * @return The default maximum allowed number of minions of a player.
     */
    public static int getBaseMinionCount(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount;
    }

    /**
     * Set the default maximum allowed number of minions of a player.
     * The maximum allowed number of minions is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <br/><br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public static void setBaseMinionCount(AbstractPlayer player, int newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount = newVal;
    }

    /**
     * Get the maximum allowed number of minions of a player.
     * This value decides the actual allowed number of minions and is reset at the start of combat.
     *
     * @return The current maximum allowed number of minions of a player.
     */
    public static int getMaxMinionCount(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_maxMinionCount.get(player);
    }

    /**
     * Set the maximum allowed number of minions of a player.
     * This value decides the actual allowed number of minions and is reset at the start of combat.
     * If this value is decreased while the player already has max number of minions, the minions stay
     * but the player cannot create new minions until the number of minions drops below the allowed number.
     */
    public static void setMaxMinionCount(AbstractPlayer player, int newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_maxMinionCount.set(player, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     *
     * @return The default chance of redirecting an applicable power.
     */
    public static float getBaseMinionPowerChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionPowerChance;
    }

    /**
     * Set the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <br/><br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public static void setBaseMinionPowerChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionPowerChance = newVal;
    }

    /**
     * Get the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * This value decides the actual allowed chance and is reset at the start of combat.
     *
     * @return The current chance of redirecting an applicable power.
     */
    public static float getMinionPowerChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionPowerChance.get(player);
    }

    /**
     * Set the chance, from 0 to 1, that a Vulnerable, Weak, Frail or Strength power
     * applied by a monster to a player gets redirected to a minion instead.
     * This value decides the actual allowed chance and is reset at the start of combat.
     */
    public static void setMinionPowerChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionPowerChance.set(player, newVal);
    }

    /**
     * Get the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     *
     * @return The default chance of redirecting an attack intent.
     */
    public static float getBaseMinionAttackTargetChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionAttackTargetChance;
    }

    /**
     * Set the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * The actual chance is reset to this value at the start of combat.
     * This value is persisted in save games.
     * <br/><br/>
     * <h3>Typical Usage</h3>
     * Typically, you want to set this in the constructor of your custom character,
     * so that your character starts with the default value, but still has a chance
     * to modify it later on.
     */
    public static void setBaseMinionAttackTargetChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionAttackTargetChance = newVal;
    }

    /**
     * Get the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * This value decides the actual allowed chance and is reset at the start of combat.
     *
     * @return The current chance of redirecting an attack intent.
     */
    public static float getMinionAttackTargetChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionAttackTargetChance.get(player);
    }

    /**
     * Set the chance, from 0 to 1, that a monster intents to attack a minion instead
     * of a player.
     * This value decides the actual allowed chance and is reset at the start of combat.
     */
    public static void setMinionAttackTargetChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionAttackTargetChance.set(player, newVal);
    }

    /**
     * Use the AI RNG to determine if a power should be redirected.
     *
     * @return True if the power should be redirected.
     */
    public static boolean ShouldRedirectPower(AbstractPlayer player) {
        float chance = getMinionPowerChance(player);
        if (chance <= 0 || chance >= 1) return false;
        return AbstractDungeon.aiRng.randomBoolean(chance);
    }

    /**
     * Use the AI RNG to determine if an attack should be redirected.
     *
     * @return True if the attack should be redirected.
     */
    public static boolean ShouldRedirectAttack(AbstractPlayer player) {
        float chance = getMinionAttackTargetChance(player);
        if (chance <= 0 || chance >= 1) return false;
        return AbstractDungeon.aiRng.randomBoolean(chance);
    }
}
