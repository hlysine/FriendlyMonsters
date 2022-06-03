package hlysine.friendlymonsters;

import basemod.BaseMod;
import basemod.interfaces.*;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hlysine.friendlymonsters.utils.MinionUtils;

@SpireInitializer
public class FriendlyMonsters implements
        PostBattleSubscriber,
        EditKeywordsSubscriber {

    //Used by @SpireInitializer
    public static void initialize() {
        @SuppressWarnings("unused") FriendlyMonsters friendlyMonsters = new FriendlyMonsters();
    }

    public FriendlyMonsters() {
        BaseMod.subscribe(this);
    }

    public static final String MOD_ID = "FriendlyMonsters";

    public static String makeID(String idText) {
        return MOD_ID + ":" + idText;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        BaseMod.logger.info("End of battle: Clearing players minions.");
        MinionUtils.clearMinions(AbstractDungeon.player);
    }

    @Override
    public void receiveEditKeywords() {
        String[] minionKeyword = new String[]{"minion", "minions"};
        BaseMod.addKeyword(minionKeyword, "A friendly monster that fights for you and has a chance to receive #yVulnerable, #yWeak, #yFrail, or #yStrength loss instead of you.");
    }

}
