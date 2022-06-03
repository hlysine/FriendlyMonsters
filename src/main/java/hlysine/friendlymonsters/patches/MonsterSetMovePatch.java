package hlysine.friendlymonsters.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import hlysine.friendlymonsters.enums.MonsterIntentEnum;
import hlysine.friendlymonsters.utils.MinionUtils;
import hlysine.friendlymonsters.utils.MonsterIntentUtils;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "setMove",
        paramtypez = {String.class, byte.class, AbstractMonster.Intent.class, int.class, int.class, boolean.class}
)
public class MonsterSetMovePatch {
    public static void Postfix(AbstractMonster monster, String moveName, byte nextMove, AbstractMonster.Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        if (MinionUtils.hasMinions(AbstractDungeon.player)) {
            switch (intent) {
                case ATTACK:
                    maybeChangeIntent(monster, MonsterIntentEnum.ATTACK_MINION, nextMove, baseDamage, multiplier, isMultiDamage);
                    break;
                case ATTACK_BUFF:
                    maybeChangeIntent(monster, MonsterIntentEnum.ATTACK_MINION_BUFF, nextMove, baseDamage, multiplier, isMultiDamage);
                    break;
                case ATTACK_DEBUFF:
                    maybeChangeIntent(monster, MonsterIntentEnum.ATTACK_MINION_DEBUFF, nextMove, baseDamage, multiplier, isMultiDamage);
                    break;
                case ATTACK_DEFEND:
                    maybeChangeIntent(monster, MonsterIntentEnum.ATTACK_MINION_DEFEND, nextMove, baseDamage, multiplier, isMultiDamage);
                    break;
            }
        }
    }


    private static void maybeChangeIntent(AbstractMonster monster, AbstractMonster.Intent possibleNewIntent, byte nextMove, int intentBaseDmg, int multiplier, boolean isMultiDamage) {
        System.out.println("--------- Maybe Change Intent -----------");

        if (MinionUtils.ShouldRedirectAttack(AbstractDungeon.player)) {
            System.out.println("-------- Changing Intent -----------");

            EnemyMoveInfo newInfo = new EnemyMoveInfo(nextMove, possibleNewIntent, intentBaseDmg, multiplier, isMultiDamage);
            ReflectionHacks.setPrivate(monster, AbstractMonster.class, "move", newInfo);
        } else {
            MonsterIntentUtils.setTarget(monster, null);
        }

        System.out.println("-------- End Change Intent -------------");
    }

}
