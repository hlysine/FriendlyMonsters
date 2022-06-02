package hlysine.friendlymonsters.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import hlysine.friendlymonsters.MinionConfig;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        }
)
public class ApplyPowerActionPatch {
    public static SpireReturn<Void> Prefix(ApplyPowerAction applyPowerAction, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (!(source instanceof AbstractMonster) || source instanceof AbstractFriendlyMonster) {
            return SpireReturn.Continue();
        }
        if (!(target instanceof AbstractPlayer)) {
            return SpireReturn.Continue();
        }
        if (!validPower(powerToApply)) {
            return SpireReturn.Continue();
        }
        if (!MinionUtils.hasMinions(AbstractDungeon.player)) {
            return SpireReturn.Continue();
        }
        if (!MinionConfig.ShouldRedirectPower()) {
            return SpireReturn.Continue();
        }

        applyPowerAction.isDone = true;
        AbstractMonster newTarget = MinionUtils.getRandomMinion(AbstractDungeon.player);
        powerToApply.owner = newTarget;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newTarget, source, powerToApply, stackAmount, isFast, effect));
        return SpireReturn.Return();
    }

    private static boolean validPower(AbstractPower powerToApply) {
        return powerToApply instanceof VulnerablePower
                || powerToApply instanceof WeakPower
                || powerToApply instanceof FrailPower
                || powerToApply instanceof StrengthPower;
    }
}
