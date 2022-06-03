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

    public static void setMaxMinionCount(AbstractPlayer player, int newMax) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_maxMinionCount.set(player, newMax);
    }

    public static int getBaseMinionCount(AbstractPlayer player) {
        return PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount;
    }

    public static void changeBaseMinionCount(AbstractPlayer player, int newMax) {
        PlayerAddSavableFieldsPatch.PlayerAddFieldsPatch.fm_minionConfig.get(player).config.BaseMinionCount = newMax;
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
