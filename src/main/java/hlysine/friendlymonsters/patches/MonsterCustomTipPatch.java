package hlysine.friendlymonsters.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "renderTip"
)
public class MonsterCustomTipPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractMonster __instance, SpriteBatch sb, ArrayList<PowerTip> ___tips) {
        if (__instance instanceof AbstractFriendlyMonster) {
            ((AbstractFriendlyMonster) __instance).renderCustomTips(sb, ___tips);
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");

            return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0]};
        }
    }
}
