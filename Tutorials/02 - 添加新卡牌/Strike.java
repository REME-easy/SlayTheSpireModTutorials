import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class Strike extends CustomCard {
    public static final String ID = "Strike";
    private static final String NAME = "打击";
    private static final String IMG_PATH = "ExampleModResources/img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

}