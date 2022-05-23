# 包装卡牌类

将卡牌类包装可以把一些经常用到的代码用更简单的方式实现，或者实现某种共性的功能。

下面分享被我的卡牌封装类：

*没有规定你一定得按我方式的来写，我只是提供一个代码简化思路。*

```java
public abstract class AbstractExampleCard extends CustomCard {
    // useTmpArt表示是否使用测试卡图，当你卡图不够用时可以使用
    public AbstractExampleCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE,
            CardRarity RARITY, CardTarget TARGET) {
        super(ID, strings.NAME, useTmpArt ? GetTmpImgPath(TYPE) : GetImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                GOLDENGLOW_CARD, RARITY, TARGET);
    }

    // 如果按这个方法实现，在cards文件夹下分别放test_attack.png、test_power.png、test_skill.png即可
    private static String GetTmpImgPath(CardType t) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
            case CURSE:
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/test_%s.png"), type);
    }

    // 如果实现这个方法，只要将相应类型的卡牌丢进相应文件夹即可，如攻击牌卡图添加进img/cards/attack/下
    private static String GetImgPath(CardType t, String name) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/%s/%s.png"), type, name.replace(ModHelper.makePath(""), ""));
    }

    //以下三个方法可以快速设置伤害格挡特殊值的基础数值
    protected void setupDamage(int amt) {
        this.baseDamage = amt;
        this.damage = amt;
    }

    protected void setupBlock(int amt) {
        this.baseBlock = amt;
        this.block = amt;
    }

    protected void setupMagicNumber(int amt) {
        this.baseMagicNumber = amt;
        this.magicNumber = amt;
    }

    // 将描述设置为升级描述并更新描述
    protected void upgradeDescription(CardStrings cardStrings) {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    // 简化伤害代码
    public void damageToEnemy(AbstractMonster m, AttackEffect effect) {
        this.addToBot(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.damage), effect));
    }

    // 简化aoe代码
    public void damageToAllEnemies(AttackEffect effect) {
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage,
                this.damageTypeForTurn, effect));
    }

    // 获得等于这张牌格挡数值的格挡
    public void gainBlock() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.block));
    }

    // 获得一定格挡
    public void gainBlock(int amt) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, amt));
    }

    // 抽牌
    public void drawCards(int amt) {
        this.addToBot(new DrawCardAction(amt));
    }

    // 直接给予玩家能力
    public void applyToPlayer(AbstractPower power) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
    }

    // 这里我们重写了upgrade方法，因为有重复代码，大多数时候都能简化（除了灼热攻击）
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            limitedUpgrade();
        }
    }

    // 升级效果写这里即可
    public void limitedUpgrade() {

    }
}
```

这样简化了代码，一张卡牌就可以这样写：
```java
public class ExampleCard extends AbstractExampleCard {
    public static final String ID = ModCore.MakePath(ExampleCard.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ExampleCard() {
        super(ID, cardStrings, 2, SKILL, CardRarity.RARE, CardTarget.NONE);
        this.setupMagicNumber(8);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        drawCards(this.magicNumber);
    }

    @Override
    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeMagicNumber(2);
        this.upgradeDescription(cardStrings);
    }
}
```

代码简化，真的爽啊。