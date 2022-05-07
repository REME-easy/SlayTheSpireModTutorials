# 添加新能力

能力的制作很简单，因为不需要注册就能用。

## 1. 编写能力类

要创建一个新能力，首先创建一个powers文件夹管理，然后新建一个类。

```java
public class ExamplePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.MakePath("ExamplePower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ExamplePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = ModHelper.MakeAssetPath("img/powers/Example84.png");
        String path48 = ModHelper.MakeAssetPath("img/powers/Example32.png");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
```

## 2.添加效果

这里我们举例做一个受到未被格挡的伤害时回复能力层数血量的能力。只要在能力类里重载受到伤害时方法，再实现效果就行。

当你需要特定条件的能力时，只要思考原版什么能力可以参考去看就行。

```java
public class ExamplePower extends AbstractPower {
// 省略其他

    // 被攻击时
    public int onAttacked(DamageInfo info, int damageAmount) {
        // 非荆棘伤害，非生命流失伤害，伤害来源不为空，伤害来源不是能力持有者本身，伤害大于0
        if (info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0) {
            // 能力闪烁一下
            this.flash();

            // 添加回复action
            this.addToTop(new HealAction(owner, owner, this.amount));
        }

        // 如果该能力不会修改受到伤害的数值，按原样返回即可
        return damageAmount;
    }
// 省略其他
}
```

## 3.本地化文本

我们已经写过很多本地化文本了，相信你已经得心应手了。

powers.json:
```json
{
  "ExampleMod:ExamplePower": {
    "NAME": "示例能力",
    "DESCRIPTIONS": [ // 注意这里最后的S，很多新手都没注意所以出了问题！！！！！！！！！
      "当你受到 #y未被格挡 的攻击伤害时，回复 #b",
      " 点生命。" // #y #b 表示把文本染成黄色和蓝色。
    ]
  }
}
```

modcore.java:
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
        // 添加power json
        BaseMod.loadCustomStringsFile(PowerStrings.class, "ExampleResources/localization/" + lang + "/powers.json");
    }
```

## 4.一些优化

### 4.1.描述翻新

像原版那样，一段描述拆成两个字符串真的太反人类了。我们可以使用格式化字符串等优化。如下：

powers.json:
```json
{
  "ExampleMod:ExamplePower": {
    "NAME": "示例能力",
    "DESCRIPTIONS": [ // 注意这里最后的S，很多新手都没注意所以出了问题！！！！！！！！！
      "当你受到 #y未被格挡 的攻击伤害时，回复 #b%d 点生命。" // %d表示能被格式化成int，不懂的建议学习java字符串相关知识
    ]
  }
}
```

ExamplePower.java:
```java
public class ExamplePower extends AbstractPower {
// 省略其他

        // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }
// 省略其他
}
```

## 5.最后

这样，使用`ApplyPowerAction`就能添加power了。（如何使用这个action参考其他给予能力的卡牌）

文件夹下放了原版的能力图，可以作为学习被直接使用。