import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class Strike extends CustomCard {
    public static final String ID = ModHelper.makePath("Strike");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    // private static final String NAME = "打击";
    private static final String NAME = CARD_STRINGS.NAME; // 读取本地化的名字
    private static final String IMG_PATH = "";
    private static final int COST = 1;
    // private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; // 读取本地化的描述
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }
}