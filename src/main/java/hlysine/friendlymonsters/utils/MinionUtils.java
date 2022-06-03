package hlysine.friendlymonsters.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.patches.PlayerAddSavableFieldsPatch;

public class MinionUtils {
    public static MonsterGroup getMinions(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player);
    }

    public static int getMaxMinionCount(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_maxMinionCount.get(player);
    }

    public static void setMaxMinionCount(AbstractPlayer player, int newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_maxMinionCount.set(player, newVal);
    }

    public static int getBaseMinionCount(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount;
    }

    public static void setBaseMinionCount(AbstractPlayer player, int newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount = newVal;
    }

    public static float getMinionPowerChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionPowerChance.get(player);
    }

    public static void setMinionPowerChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionPowerChance.set(player, newVal);
    }

    public static float getBaseMinionPowerChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionPowerChance;
    }

    public static void setBaseMinionPowerChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionPowerChance = newVal;
    }

    public static float getMinionAttackTargetChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionAttackTargetChance.get(player);
    }

    public static void setMinionAttackTargetChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionAttackTargetChance.set(player, newVal);
    }

    public static float getBaseMinionAttackTargetChance(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionAttackTargetChance;
    }

    public static void setBaseMinionAttackTargetChance(AbstractPlayer player, float newVal) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionAttackTargetChance = newVal;
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

    public static boolean addMinion(AbstractPlayer player, AbstractFriendlyMonster minionToAdd) {
        MonsterGroup minions = getMinions(player);
        int maxMinions = getMaxMinionCount(player);

        if (minions.monsters.size() >= maxMinions) {
            return false;
        } else {
            minionToAdd.init();
            minionToAdd.usePreBattleAction();
            // minionToAdd.useUniversalPreBattleAction(); // Friendly minions should not be affected by enemy blights
            minionToAdd.showHealthBar();
            minions.add(minionToAdd);
            return true;
        }
    }

    public static boolean removeMinion(AbstractPlayer player, AbstractFriendlyMonster minionToRemove) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).monsters.remove(minionToRemove);
    }

    public static boolean hasMinions(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).monsters.size() > 0;
    }

    public static void clearMinions(AbstractPlayer player) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.set(player, new MonsterGroup(new AbstractFriendlyMonster[]{}));
    }

    public static AbstractMonster getRandomMinion(AbstractPlayer player, boolean aliveOnly) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minions.get(player).getRandomMonster(null, aliveOnly, AbstractDungeon.aiRng);
    }
}
