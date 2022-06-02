package hlysine.friendlymonsters.utils;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.enums.MonsterIntentEnum;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.patches.MonsterAddFieldsPatch;

public class MonsterIntentUtils {
    public static void setTarget(AbstractMonster monster, AbstractFriendlyMonster target) {
        MonsterAddFieldsPatch.fm_attackTarget.set(monster, target);
    }

    public static AbstractFriendlyMonster getTarget(AbstractMonster monster) {
        return MonsterAddFieldsPatch.fm_attackTarget.get(monster);
    }

    /**
     * Switch the attack target of a monster and update its intent accordingly.
     *
     * @param monster   The monster to have its target switched.
     * @param newTarget The new target, can be a minion or null for the player.
     */
    public static void switchTarget(AbstractMonster monster, AbstractFriendlyMonster newTarget) {
        AbstractMonster.Intent intent = monster.intent;

        if (newTarget == null) {
            if (intent == MonsterIntentEnum.ATTACK_MINION) {
                monster.intent = AbstractMonster.Intent.ATTACK;
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_BUFF) {
                monster.intent = AbstractMonster.Intent.ATTACK_BUFF;
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF) {
                monster.intent = AbstractMonster.Intent.ATTACK_DEBUFF;
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_DEFEND) {
                monster.intent = AbstractMonster.Intent.ATTACK_DEFEND;
            }
        } else {
            if (intent == AbstractMonster.Intent.ATTACK) {
                monster.intent = MonsterIntentEnum.ATTACK_MINION;
            } else if (intent == AbstractMonster.Intent.ATTACK_BUFF) {
                monster.intent = MonsterIntentEnum.ATTACK_MINION_BUFF;
            } else if (intent == AbstractMonster.Intent.ATTACK_DEBUFF) {
                monster.intent = MonsterIntentEnum.ATTACK_MINION_DEBUFF;
            } else if (intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                monster.intent = MonsterIntentEnum.ATTACK_MINION_DEFEND;
            }
        }

        setTarget(monster, newTarget);
        monster.applyPowers();
    }
}
