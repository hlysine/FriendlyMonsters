package hlysine.friendlymonsters.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import hlysine.friendlymonsters.enums.MonsterIntentEnum;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;
import hlysine.friendlymonsters.utils.MonsterIntentUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MonsterIntentPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "createIntent"
    )
    public static class CreateIntentPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractMonster __instance) {
            AbstractMonster.Intent _intent = __instance.intent;
            if (MinionUtils.hasMinions(AbstractDungeon.player)) {
                if (MonsterIntentEnum.isMinionIntent(_intent) && MonsterIntentUtils.getTarget(__instance) == null) {
                    AbstractFriendlyMonster target = (AbstractFriendlyMonster) MinionUtils.getRandomMinion(AbstractDungeon.player);
                    MonsterIntentUtils.setTarget(__instance, target);
                }
            }
        }

        public static void Prefix(AbstractMonster __instance) {
            MonsterIntentUtils.setTarget(__instance, null);
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "updateIntentTip");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractMonster.class,
            method = "getIntentImg"
    )
    public static class GetIntentImagePatch {
        public static SpireReturn<Texture> Prefix(AbstractMonster __instance, boolean ___isMultiDmg, int ___intentDmg, int ___intentMultiAmt) {
            AbstractMonster.Intent intent = __instance.intent;

            if (MonsterIntentEnum.isMinionIntent(intent)) {
                int tmp;
                if (___isMultiDmg) {
                    tmp = ___intentDmg * ___intentMultiAmt;
                } else {
                    tmp = ___intentDmg;
                }
                return SpireReturn.Return(getAttackIntent(__instance, tmp));
            }

            return SpireReturn.Continue();
        }

        private static Texture getAttackIntent(AbstractMonster monster, int totalDamage) {
            // If the attacking target of this monster is a minion with valid attack intent images,
            // use those images to show that the specific minion is being attacked.
            AbstractFriendlyMonster target = MonsterIntentUtils.getTarget(monster);
            if (target != null) {
                if (target.getAttackIntents() != null && target.getAttackIntents().length >= 7 && Arrays.stream(target.getAttackIntents()).noneMatch(Objects::isNull)) {
                    Texture[] attackIntents = target.getAttackIntents();
                    if (totalDamage < 5) {
                        return attackIntents[0];
                    } else if (totalDamage < 10) {
                        return attackIntents[1];
                    } else if (totalDamage < 15) {
                        return attackIntents[2];
                    } else if (totalDamage < 20) {
                        return attackIntents[3];
                    } else if (totalDamage < 25) {
                        return attackIntents[4];
                    } else if (totalDamage < 30) {
                        return attackIntents[5];
                    } else {
                        return attackIntents[6];
                    }
                }
            }

            // In other cases, use the generic minion attack intent images.
            if (totalDamage < 5) {
                return new Texture("images/intents/attack_monster_intent_1.png");
            } else if (totalDamage < 10) {
                return new Texture("images/intents/attack_monster_intent_2.png");
            } else if (totalDamage < 15) {
                return new Texture("images/intents/attack_monster_intent_3.png");
            } else if (totalDamage < 20) {
                return new Texture("images/intents/attack_monster_intent_4.png");
            } else if (totalDamage < 25) {
                return new Texture("images/intents/attack_monster_intent_5.png");
            } else if (totalDamage < 30) {
                return new Texture("images/intents/attack_monster_intent_6.png");
            } else {
                return new Texture("images/intents/attack_monster_intent_7.png");
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateIntentVFX"
    )
    public static class UpdateIntentVFXPatch {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, @ByRef float[] ___intentParticleTimer, ArrayList<AbstractGameEffect> ___intentVfx) {
            if (__instance.intentAlpha > 0.0f) {
                if (__instance.intent == MonsterIntentEnum.ATTACK_MINION_DEFEND) {
                    ___intentParticleTimer[0] = ___intentParticleTimer[0] - Gdx.graphics.getDeltaTime();
                    float valIntentParticleTime = ___intentParticleTimer[0];
                    if (valIntentParticleTime < 0.0F) {
                        ___intentParticleTimer[0] = 0.5f;
                        ___intentVfx.add(new ShieldParticleEffect(__instance.intentHb.cX, __instance.intentHb.cY));
                        return SpireReturn.Return();
                    }
                } else if (__instance.intent == MonsterIntentEnum.ATTACK_MINION_BUFF) {
                    ___intentParticleTimer[0] = ___intentParticleTimer[0] - Gdx.graphics.getDeltaTime();
                    float valIntentParticleTime = ___intentParticleTimer[0];
                    if (valIntentParticleTime < 0.0F) {
                        ___intentParticleTimer[0] = 0.1f;
                        ___intentVfx.add(new BuffParticleEffect(__instance.intentHb.cX, __instance.intentHb.cY));
                        return SpireReturn.Return();
                    }
                } else if (__instance.intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF) {
                    ___intentParticleTimer[0] = ___intentParticleTimer[0] - Gdx.graphics.getDeltaTime();
                    float valIntentParticleTime = ___intentParticleTimer[0];
                    if (valIntentParticleTime < 0.0F) {
                        ___intentParticleTimer[0] = 1.0f;
                        ___intentVfx.add(new DebuffParticleEffect(__instance.intentHb.cX, __instance.intentHb.cY));
                        return SpireReturn.Return();
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateIntentTip"
    )
    public static class UpdateIntentTipPatch {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, PowerTip ___intentTip, boolean ___isMultiDmg, int ___intentDmg, int ___intentMultiAmt) {
            AbstractMonster.Intent intent = __instance.intent;
            if (!MonsterIntentEnum.isMinionIntent(intent)) {
                return SpireReturn.Continue();
            }

            AbstractFriendlyMonster target = MonsterIntentUtils.getTarget(__instance);
            MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);

            if (target == null || target.isDead || !minions.monsters.contains(target)) {
                MonsterIntentUtils.switchTarget(__instance, null);
                return SpireReturn.Return();
            }

            if (intent == MonsterIntentEnum.ATTACK_MINION) {
                ___intentTip.header = "Aggressive";

                if (___isMultiDmg) {
                    ___intentTip.body = "This enemy intends to NL #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage #b" + ___intentMultiAmt + " times.";
                } else {
                    ___intentTip.body = "This enemy intends to NL #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage";
                }

                ___intentTip.img = ReflectionHacks.privateMethod(AbstractMonster.class, "getAttackIntentTip").invoke(__instance);
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_BUFF) {
                ___intentTip.header = "Aggressive";

                if (___isMultiDmg) {
                    ___intentTip.body = "This enemy intends to use a #yBuff and #yAttack a #y " + target.name + " for #b" + ___intentDmg + " damage #b" + ___intentMultiAmt + " times.";
                } else {
                    ___intentTip.body = "This enemy intends to use a #yBuff and #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage.";
                }

                ___intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF) {
                ___intentTip.header = "Strategic";
                ___intentTip.body = "This enemy intends to inflict a #yNegative #yEffect on you and #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage.";
                ___intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
            } else if (intent == MonsterIntentEnum.ATTACK_MINION_DEFEND) {
                ___intentTip.header = "Aggressive";

                if (___isMultiDmg) {
                    ___intentTip.body = "This enemy intends to #yBlock and #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage #b" + ___intentMultiAmt + " times.";
                } else {
                    ___intentTip.body = "This enemy intends to #yBlock and #yAttack a #y" + target.name + " for #b" + ___intentDmg + " damage.";
                }

                ___intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
            } else {
                return SpireReturn.Continue();
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "calculateDamage",
            paramtypez = {int.class}
    )
    public static class CalculateDamagePatch {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, int dmg, @ByRef int[] ___intentDmg) {
            AbstractMonster.Intent intent = __instance.intent;

            if (MonsterIntentEnum.isMinionIntent(intent)) {
                AbstractFriendlyMonster target = MonsterIntentUtils.getTarget(__instance);

                if (target == null) {
                    return SpireReturn.Continue();
                } else {
                    float tmp = dmg;
                    for (final AbstractPower p : __instance.powers) {
                        tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (final AbstractPower p : target.powers) {
                        tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (final AbstractPower p : __instance.powers) {
                        tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (final AbstractPower p : target.powers) {
                        tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    dmg = MathUtils.floor(tmp);
                    if (dmg < 0) dmg = 0;
                    ___intentDmg[0] = dmg;
                }

                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "applyPowers"
    )
    public static class ApplyPowersPatch {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, EnemyMoveInfo ___move, @ByRef Texture[] ___intentImg) {
            ReflectionHacks.RMethod calculateDamage = ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class);
            ReflectionHacks.RMethod getIntentImg = ReflectionHacks.privateMethod(AbstractMonster.class, "getIntentImg");
            ReflectionHacks.RMethod updateIntentTip = ReflectionHacks.privateMethod(AbstractMonster.class, "updateIntentTip");

            AbstractFriendlyMonster target = MonsterIntentUtils.getTarget(__instance);

            if (target == null) {
                return SpireReturn.Continue();
            }

            for (final DamageInfo dmg : __instance.damage) {
                dmg.applyPowers(__instance, target);
            }
            if (___move.baseDamage > -1) {
                calculateDamage.invoke(__instance, ___move.baseDamage);
            }
            ___intentImg[0] = getIntentImg.invoke(__instance);
            updateIntentTip.invoke(__instance);
            return SpireReturn.Return();
        }
    }
}
