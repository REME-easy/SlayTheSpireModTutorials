这篇教程只讲解如何制作基础的怪物和添加怪物，如要制作**觉醒者**、**小黑**这类特殊的怪物请读者自行使用**jd-gui**反编译原版游戏进行代码查阅和学习。


```java
public class MyMonster extends CustomMonster {
    // 怪物ID（此处的ModHelper在“04 - 本地化”中提到）
    private static final String ID = ModHelper.makePath("MyMonster");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    // 怪物的图片，请自行添加
    public static final String IMG = "";

    public MyMonster(float x, float y) {
        // 参数的作用分别是：
        // 名字
        // ID
        // 最大生命值，由于在之后还会设置可以随意填写
        // hitbox偏移量 - x方向
        // hitbox偏移量 - y方向
        // hitbox大小 - x方向（会影响血条宽度）
        // hitbox大小 - y方向
        // 图片
        // 怪物位置（x,y）
        super(NAME, ID, 140, 0.0F, 0.0F, 250.0F, 270.0F, IMG, x, y);

        // 如果你要做进阶改变血量和伤害意图等，这样写
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(155, 166);
        } else {
            this.setHp(150, 160);
        }

        // 怪物伤害意图的数值
        int slashDmg;
        int multiDmg;
        if (AbstractDungeon.ascensionLevel >= 2) {
            slashDmg = 20;
            multiDmg = 5;
        } else {
            slashDmg = 18;
            multiDmg = 7;
        }
        // 意图0的伤害
        this.damage.add(new DamageInfo(this, slashDmg));
        // 意图1的伤害
        this.damage.add(new DamageInfo(this, multiDmg));
    }

    // 战斗开始时
    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RitualPower(this, 5, false)));
    }

    // 当怪物roll意图的时候，这里设置其意图。num是一个0~99的随机数。
    @Override
    public void getMove(int num) {
        // 有40%的概率执行意图0，60%执行意图1
        int move;
        if (num < 40) {
            this.setMove((byte)0, Intent.ATTACK, damage.get(0).base);
        } else {
            this.setMove((byte)1, Intent.ATTACK, damage.get(1).base, 3, true);        
        }
    }

    // 执行动作
    @Override
    public void takeTurn() {
        // nextMove就是roll到的意图，0就是意图0，1就是意图1
        switch(this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 1:
                for(int i = 0 ; i < 2 ; i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }

        // 要加一个rollmove的action，重roll怪物的意图
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
}
```

由于没有添加怪物的接口，在你的主类里，你可以写在任意初始化接口中：
```java
    @Override
    public void receivePostInitialize() {
        receiveEditMonsters();
    }

    private void receiveEditMonsters() {
        // 注册怪物组合，你可以多添加几个怪物
        BaseMod.addMonster("ExampleMod:MyMonster", MyMonster.NAME, () -> new MyMonster(0.0F, 0.0F));
        // 两个异鸟
        // BaseMod.addMonster("ExampleMod:2 Byrds", "", () -> new MonsterGroup(new AbstractMonster[] { new Byrd(-80.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(200.0F, MathUtils.random(25.0F, 70.0F)) }));

        // 添加战斗遭遇
        // 在第二章添加精英遭遇，权重为1.0，权重越高越可能遇到
        BaseMod.addEliteEncounter("TheCity", new MonsterInfo("ExampleMod:MyMonster", 1.0F));
    }
```
如果要添加弱怪、强怪，使用`BaseMod.addMonsterEncounter`和`BaseMod.addStrongMonsterEncounter`。

<b>此外，不要忘了添加本地化文件。</b>

```java
public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "ExampleResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "ExampleResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "ExampleResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "ExampleResources/localization/" + lang + "/powers.json");
        // 新的
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "ExampleResources/localization/" + lang + "/monsters.json");
    }
```

monsters.json:
```json
{
  "ExampleMod:MyMonster":{
    "NAME":"示例怪物",
    "DIALOG": [""]
  }
}
```