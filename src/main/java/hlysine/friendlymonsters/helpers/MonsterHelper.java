package hlysine.friendlymonsters.helpers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.enums.MonsterIntentEnum;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.patches.MonsterAddFieldsPatch;

public class MonsterHelper {

    public static void setTarget(AbstractMonster monster, AbstractFriendlyMonster target) {
        MonsterAddFieldsPatch.f_target.set(monster, target);
    }

    public static AbstractFriendlyMonster getTarget(AbstractMonster monster) {
        return MonsterAddFieldsPatch.f_target.get(monster);
    }


    /**
     * Use to switch a monsters current target to someone else. Passing in null will cause
     * the target to be the player.
     * @param monster
     * @param newTarget
     */
    public static void switchTarget(AbstractMonster monster, AbstractFriendlyMonster newTarget) {

        AbstractMonster.Intent intent = monster.intent;

        if(newTarget == null) {
            if(intent == MonsterIntentEnum.ATTACK_MINION) {
                monster.intent = AbstractMonster.Intent.ATTACK;
            }
            else if(intent == MonsterIntentEnum.ATTACK_MINION_BUFF) {
                monster.intent = AbstractMonster.Intent.ATTACK_BUFF;
            }
            else if(intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF) {
                monster.intent = AbstractMonster.Intent.ATTACK_DEBUFF;
            }
            else if(intent == MonsterIntentEnum.ATTACK_MINION_DEFEND) {
                monster.intent = AbstractMonster.Intent.ATTACK_DEFEND;
            }

        }

        setTarget(monster, newTarget);
        monster.applyPowers();
    }
}
