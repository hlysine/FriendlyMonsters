package hlysine.friendlymonsters.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;

public abstract class AbstractPlayerWithMinions extends CustomPlayer {
    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, String model, String animation) {
        super(name, playerClass, orbTextures, orbVfxPath, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, String model, String animation) {
        super(name, playerClass, energyOrbInterface, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, String model, String animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, model, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, animation);
    }

    public AbstractPlayerWithMinions(String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, AbstractAnimation animation) {
        super(name, playerClass, energyOrbInterface, animation);
    }

    public MonsterGroup getMinions() {
        return MinionUtils.getMinions(this);
    }

    public AbstractFriendlyMonster getMinion(String minionID) {
        return MinionUtils.getMinion(this, minionID);
    }

    public boolean hasMinions() {
        return MinionUtils.hasMinions(this);
    }

    public boolean hasMinion(String minionId) {
        return MinionUtils.hasMinion(this, minionId);
    }

    public boolean addMinion(AbstractFriendlyMonster minion) {
        return MinionUtils.addMinion(this, minion);
    }

    public boolean removeMinion(AbstractFriendlyMonster minion) {
        return MinionUtils.removeMinion(this, minion);
    }

    public void clearMinions() {
        MinionUtils.clearMinions(this);
    }

    public int getBaseMinionCount() {
        return MinionUtils.getBaseMinionCount(this);
    }

    public void setBaseMinionCount(int newVal) {
        MinionUtils.setBaseMinionCount(this, newVal);
    }

    public int getMaxMinionCount() {
        return MinionUtils.getMaxMinionCount(this);
    }

    public void setMaxMinionCount(int newVal) {
        MinionUtils.setMaxMinionCount(this, newVal);
    }

    public float getBaseMinionPowerChance() {
        return MinionUtils.getBaseMinionPowerChance(this);
    }

    public void setBaseMinionPowerChance(float newVal) {
        MinionUtils.setBaseMinionPowerChance(this, newVal);
    }

    public float getMinionPowerChance() {
        return MinionUtils.getMinionPowerChance(this);
    }

    public void setMinionPowerChance(float newVal) {
        MinionUtils.setMinionPowerChance(this, newVal);
    }

    public float getBaseMinionAttackTargetChance() {
        return MinionUtils.getBaseMinionAttackTargetChance(this);
    }

    public void setBaseMinionAttackTargetChance(float newVal) {
        MinionUtils.setBaseMinionAttackTargetChance(this, newVal);
    }

    public float getMinionAttackTargetChance() {
        return MinionUtils.getMinionAttackTargetChance(this);
    }

    public void setMinionAttackTargetChance(float newVal) {
        MinionUtils.setMinionAttackTargetChance(this, newVal);
    }
}
