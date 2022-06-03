package hlysine.friendlymonsters.patches;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.FriendlyMonsters;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

public class PlayerAddSavableFieldsPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = SpirePatch.CLASS
    )
    public static class PlayerAddFieldsPatch {
        /**
         * The maximum allowed number of minions. This is reset to base count at the start of each combat.
         */
        public static SpireField<Integer> fm_maxMinionCount = new SpireField<>(() -> 1);
        public static SpireField<MonsterGroup> fm_minions = new SpireField<>(() -> new MonsterGroup(new AbstractFriendlyMonster[1]));

        /**
         * The chance, from 0 to 1, that a Weak/Vulnerable/Frail/Strength power
         * that targets the player gets redirected to a random minion instead.
         * This is reset to base value at the start of each combat.
         */
        public static SpireField<Float> fm_minionPowerChance = new SpireField<>(() -> .33f);

        /**
         * The chance, from 0 to 1, that a monster changes its intent to attack
         * a random minion instead of the player.
         * This is reset to base value at the start of each combat.
         */
        public static SpireField<Float> fm_minionAttackTargetChance = new SpireField<>(() -> .33f);


        public static SpireField<PlayerMinionConfigSavable> fm_minionConfig = new SpireField<>(PlayerMinionConfigSavable::new);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PlayerLoadSavable {
        public static void Postfix(AbstractPlayer __instance) {
            BaseMod.addSaveField(FriendlyMonsters.makeID("player_minion_config"), PlayerAddFieldsPatch.fm_minionConfig.get(__instance));
        }
    }

    public static class PlayerMinionConfig {
        /**
         * The chance, from 0 to 1, that a Weak/Vulnerable/Frail/Strength power
         * that targets the player gets redirected to a random minion instead.
         */
        public float BaseMinionPowerChance = .33f;

        /**
         * The chance, from 0 to 1, that a monster changes its intent to attack
         * a random minion instead of the player.
         */
        public float BaseMinionAttackTargetChance = .33f;

        /**
         * The maximum allowed number of minions.
         */
        public int BaseMinionCount = 1;
    }

    public static class PlayerMinionConfigSavable implements CustomSavable<PlayerMinionConfig> {
        public PlayerMinionConfig config = new PlayerMinionConfig();

        @Override
        public PlayerMinionConfig onSave() {
            return config;
        }

        @Override
        public void onLoad(PlayerMinionConfig object) {
            FriendlyMonsters.logger.info("Player minion config loaded: " + object);
            if (object != null)
                config = object;
        }
    }
}
