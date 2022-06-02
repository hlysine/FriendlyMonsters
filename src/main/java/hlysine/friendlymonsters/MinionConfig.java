package hlysine.friendlymonsters;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MinionConfig {
    /**
     * The chance, from 0 to 1, that a Weak/Vulnerable/Frail/Strength power
     * that targets the player gets redirected to a random minion instead.
     */
    public static float MinionPowerChance = .33f;

    /**
     * The chance, from 0 to 1, that a monster changes its intent to attack
     * a random minion instead of the player.
     */
    public static float MinionAttackTargetChance = .33f;

    /**
     * Use the AI RNG to determine if a power should be redirected.
     *
     * @return True if the power should be redirected.
     */
    public static boolean ShouldRedirectPower() {
        if (MinionPowerChance <= 0 || MinionPowerChance >= 1) return false;
        return AbstractDungeon.aiRng.randomBoolean(MinionPowerChance);
    }

    /**
     * Use the AI RNG to determine if an attack should be redirected.
     *
     * @return True if the attack should be redirected.
     */
    public static boolean ShouldRedirectAttack() {
        if (MinionAttackTargetChance <= 0 || MinionAttackTargetChance >= 1) return false;
        return AbstractDungeon.aiRng.randomBoolean(MinionAttackTargetChance);
    }
}
