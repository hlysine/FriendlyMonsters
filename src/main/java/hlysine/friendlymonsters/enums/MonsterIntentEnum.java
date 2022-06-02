package hlysine.friendlymonsters.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterIntentEnum {

    @SpireEnum
    public static AbstractMonster.Intent ATTACK_MINION;

    @SpireEnum
    public static AbstractMonster.Intent ATTACK_MINION_BUFF;

    @SpireEnum
    public static AbstractMonster.Intent ATTACK_MINION_DEBUFF;

    @SpireEnum
    public static AbstractMonster.Intent ATTACK_MINION_DEFEND;

    public static boolean isMinionIntent(AbstractMonster.Intent intent) {
        return intent == MonsterIntentEnum.ATTACK_MINION
                || intent == MonsterIntentEnum.ATTACK_MINION_BUFF
                || intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF
                || intent == MonsterIntentEnum.ATTACK_MINION_DEFEND;
    }
}
