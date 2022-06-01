package hlysine.friendlymonsters.patches;

import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.*;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import hlysine.friendlymonsters.helpers.BasePlayerMinionHelper;
import hlysine.friendlymonsters.helpers.MinionConfigHelper;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

import java.lang.reflect.Field;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.actions.common.ApplyPowerAction",
        method = SpirePatch.CONSTRUCTOR,
        paramtypes = {"com.megacrit.cardcrawl.core.AbstractCreature",
                "com.megacrit.cardcrawl.core.AbstractCreature",
                "com.megacrit.cardcrawl.powers.AbstractPower",
                "int", "boolean", "com.megacrit.cardcrawl.actions.AbstractGameAction$AttackEffect"}
)
public class ApplyPowerActionPatch {


    public static SpireReturn<Void> Prefix(ApplyPowerAction applyPowerAction, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if ((target instanceof AbstractPlayerWithMinions || BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) && source instanceof AbstractMonster) {
            if (AbstractDungeon.aiRng.randomBoolean(MinionConfigHelper.MinionPowerChance) && validPower(powerToApply)) {
                try {
                    Field doneField = AbstractGameAction.class.getField("isDone");
                    doneField.setAccessible(true);
                    doneField.setBoolean(applyPowerAction, true);

                    if (!(AbstractDungeon.player instanceof AbstractPlayerWithMinions)) {
                        if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) {
                            MonsterGroup minions = PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
                            AbstractMonster newTarget = minions.getRandomMonster();
                            powerToApply.owner = newTarget;
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newTarget, source, powerToApply, stackAmount, isFast, effect));
                            return SpireReturn.Return();
                        }
                    } else {
                        AbstractPlayerWithMinions player = (AbstractPlayerWithMinions) AbstractDungeon.player;
                        if (player.hasMinions()) {
                            AbstractMonster newTarget = player.getMinions().getRandomMonster();
                            powerToApply.owner = newTarget;
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newTarget, source, powerToApply, stackAmount, isFast, effect));
                            return SpireReturn.Return();
                        }
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return SpireReturn.Continue();
    }

    private static boolean validPower(AbstractPower powerToApply) {

        //Makes sure powers only from Monsters are effected by this.
        boolean fromMonster = powerToApply.owner instanceof AbstractMonster && !(powerToApply.owner instanceof AbstractFriendlyMonster);
        boolean notTransient = !(powerToApply.owner instanceof Transient);

        return fromMonster && (powerToApply instanceof VulnerablePower
                || powerToApply instanceof WeakPower
                || powerToApply instanceof FrailPower
                || powerToApply instanceof StrengthPower) && notTransient;

    }

}
