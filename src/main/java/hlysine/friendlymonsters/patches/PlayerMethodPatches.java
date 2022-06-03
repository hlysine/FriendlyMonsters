package hlysine.friendlymonsters.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import hlysine.friendlymonsters.FriendlyMonsters;
import hlysine.friendlymonsters.enums.MonsterIntentEnum;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;
import hlysine.friendlymonsters.utils.MonsterIntentUtils;

import java.util.ConcurrentModificationException;

public class PlayerMethodPatches {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "initializeClass"
    )
    public static class InitializePatch {
        public static void Prefix(AbstractPlayer __instance) {
            MinionUtils.clearMinions(__instance);
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = {DamageInfo.class}
    )
    public static class DamagePatch {
        public static SpireReturn<Void> Prefix(AbstractPlayer __instance, DamageInfo info) {
            if (!(info.owner instanceof AbstractMonster)) {
                return SpireReturn.Continue();
            }

            AbstractMonster owner = (AbstractMonster) info.owner;
            boolean attackingMinion = MonsterIntentEnum.isMinionIntent(owner.intent);
            AbstractFriendlyMonster target = MonsterIntentUtils.getTarget(owner);

            // Check if the targeted minion can still be attacked
            if (attackingMinion && target != null && !target.isDead && MinionUtils.getMinions(__instance).monsters.contains(target)) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                return SpireReturn.Return();
            } else if (attackingMinion) {
                // attack the player instead if the minion cannot be attacked
                MonsterIntentUtils.switchTarget(owner, null);
                info.applyPowers(info.owner, __instance);
                return SpireReturn.Continue();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class RenderPatch {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                if (AbstractDungeon.getCurrRoom() != null) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
                        minions.render(sb);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "update"
    )
    public static class UpdatePatch {
        public static void Postfix() {
            if (AbstractDungeon.getCurrRoom() != null) {
                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
                        minions.update();
                        minions.monsters.removeIf(minion -> minion.isDead);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PreBattlePatch {
        public static void Postfix(AbstractPlayer __instance) {
            MinionUtils.setMaxMinionCount(__instance, MinionUtils.getBaseMinionCount(__instance));
            MinionUtils.setMinionPowerChance(__instance, MinionUtils.getBaseMinionPowerChance(__instance));
            MinionUtils.setMinionAttackTargetChance(__instance, MinionUtils.getBaseMinionAttackTargetChance(__instance));
            MinionUtils.clearMinions(__instance);
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyEndOfTurnTriggers"
    )
    public static class EndOfTurnPatch {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                FriendlyMonsters.logger.info("----------- Minion Before Attacking --------------");
                try {
                    for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                        minion.takeTurn();
                    }
                    for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                        minion.applyEndOfTurnTriggers();
                    }
                    for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                        for (AbstractPower power : minion.powers) {
                            power.atEndOfRound();
                        }
                    }
                } catch (ConcurrentModificationException ex) {
                    FriendlyMonsters.logger.error("The list of minions is modified while the minions are taking turns. You should not directly add/remove minions at this phase. Enqueue an action to do so instead.");
                    throw ex;
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyTurnPowers"
    )
    public static class ApplyTurnPowersPatch {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                    minion.applyTurnPowers();
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyStartOfTurnPostDrawPowers"
    )
    public static class ApplyStartOfTurnPostDrawPowersPatch {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                    minion.applyStartOfTurnPostDrawPowers();
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyStartOfTurnPowers"
    )
    public static class ApplyStartOfTurnPowersPatch {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                    minion.applyStartOfTurnPowers();
                }
                for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                    minion.loseBlock();
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "updatePowers"
    )
    public static class UpdatePowersPatch {
        public static void Postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                for (AbstractMonster minion : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                    minion.updatePowers();
                }
            }
        }
    }
}
