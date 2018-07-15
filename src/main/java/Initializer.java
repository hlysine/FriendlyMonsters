import basemod.BaseMod;
import basemod.interfaces.*;
import cards.MonsterCard;
import characters.AbstractPlayerWithMinions;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helpers.BasePlayerMinionHelper;

@SpireInitializer
public class Initializer implements EditCardsSubscriber, PostBattleSubscriber {

    //Used by @SpireInitializer
    public static void initialize(){
        Initializer initializer = new Initializer();
    }

    public Initializer() {
        BaseMod.subscribe(this);
    }


    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new MonsterCard());
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        BaseMod.logger.info("End of battle: Clearing players minions.");
        if(!(AbstractDungeon.player instanceof AbstractPlayerWithMinions)) {
            BasePlayerMinionHelper.clearMinions(AbstractDungeon.player);
        } else {
            ((AbstractPlayerWithMinions)AbstractDungeon.player).clearMinions();
        }
    }

}
