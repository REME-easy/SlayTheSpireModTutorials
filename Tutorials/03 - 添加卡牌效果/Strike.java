import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class Strike extends CustomCard {
    public static final String ID = "Strike";
    public static final String NAME = "打击";
    public static final String IMG_PATH = "ExampleModResources/img/cards/Strike.png";
    public static final int COST = 1;
    public static final String DESCRIPTION = "造成 !D! 点伤害。";
    public static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;
    public static final CardRarity RARITY = CardRarity.BASIC;
    public static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // AbstractCard中实现了addToBot方法，它的效果和AbstractDungeon.actionManager.addToBottom相同
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL)));
    }

}