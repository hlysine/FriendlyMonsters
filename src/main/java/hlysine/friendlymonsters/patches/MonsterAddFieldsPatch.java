package hlysine.friendlymonsters.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = SpirePatch.CLASS
)
public class MonsterAddFieldsPatch {
    /**
     * The minion that this monster intends to attack this turn. Set to null to attack the player.
     */
    public static SpireField<AbstractFriendlyMonster> fm_attackTarget = new SpireField<>(() -> null);
}
