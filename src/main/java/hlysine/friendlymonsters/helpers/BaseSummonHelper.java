package hlysine.friendlymonsters.helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.characters.AbstractPlayerWithMinions;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

public class BaseSummonHelper {

    public static void summonMinion(AbstractFriendlyMonster monster) {
        AbstractPlayerWithMinions player;
        if(AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
            player = (AbstractPlayerWithMinions)AbstractDungeon.player;
            player.addMinion(monster);
        }
        else {
            BasePlayerMinionHelper.addMinion(AbstractDungeon.player, monster);
        }
    }

}
