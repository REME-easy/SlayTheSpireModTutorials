# 本地化内容

通常你想向不同语言的玩家提供不同语言的内容。这时候需要按照杀戮尖塔的方式本地化。

## 1. 创建文本资源

1. 让我们先来创建一个文件夹管理本地化资源。

* src
    * main
        * java
        * resources
            * ExampleResources
                * <b>localization</b>
                * images
            * ModTheSpire.json

2. 你可以创建不同文件夹放置不同语言的资源。

* localization
    * ZHS
    * ENG
    * JPN
    * ...

3. 我们先编写一个中文版本。在`ZHS`（简体中文）文件夹下创建一个新文件`cards.json`，并依葫芦画瓢填写。

cards.json:
```json
{
  "ExampleMod:Strike": { // 这里填写你卡牌的ID
    "NAME": "打击", // 卡名
    "DESCRIPTION": "造成 !D! 点伤害。", // 原始描述
    "UPGRADE_DESCRIPTION": " 固有 。 NL 造成 !D! 点伤害。" // [可选]，升级描述，若升级前后只是数值的变化可不写
  }
}
```

<b>*请在你的文件里删除括号及其里面的注释！！！！！！！！*</b>

*你发现伤害数值需要用`!D!`代替，关键词前后需要有空格，并且不显示升级后的文本。这些内容在另外的章节介绍。（见新手必备知识-杀戮尖塔描述写法）*

## 2.注册资源和使用资源

和注册一张卡牌一样，注册本地化内容需要用到basemod的钩子。以下更新了模组核心。

```java
@SpireInitializer
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber {
    public ExampleMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ExampleMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "ExampleResources/localization/" + lang + "/cards.json"); // 加载相应语言的卡牌本地化内容。
        // 如果是中文，加载的就是"ExampleResources/localization/ZHS/cards.json"
    }
}
```

<i>你可以自己尝试改成根据语言名字读取文件，而不是上面的ifelse类型的。</i>

这样资源就成功加入了。接下来我们修改打击的代码，让它使用本地化资源。

Strike.java:
```java
public class Strike extends CustomCard {
    public static final String ID = "ExampleMod:Strike";
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
    // 省略...
}
```
好了！现在它能在中文语言下运行。如果你还写了英文文本，那么这张牌在中文和英文语言下都能运行了。

如果遇到任何错误，查看<b>查看报错信息</b>一节了解哪步出错了。也可以浏览其他mod是如何处理本地化文本的。

## 升级描述

如果你发现你的卡牌升级并没有改变描述，是因为你没有写这些：
```java
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);

            // 加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
```

<br><br><br>

*可以先跳过，学习之后的章节*
## 进阶：简化代码

### 简化卡牌ID

你发现卡牌ID每张都要写前缀太麻烦了。

> 02 - 添加新卡牌<br>为了和其他mod区分开来，你需要在ID之前加上你的modid前缀。

为了偷懒，你可以写一个帮手方法。首先创建一个帮手类。

* modcore
* cards
* helpers
    * ModHelper.java <-这里

ModHelper.java:
```java
public class ModHelper {
    public static String makePath(String id) {
        return "ExampleMod:" + id;
    }
}
```

通过调用这个方法，你就只需要写实际的ID了。

```java
    public static final String ID = ModHelper.makePath("Strike");
    // ID 实际等于 "ExampleMod:Strike"
```

### 还能再简化？

通常，你的卡牌名和类名是一致的，所以直接获取类名即可，此外还可以骗过文本编辑器，让它在复制时直接修改类名，就不用修改ID了。

```java
    public static final String ID = ModHelper.makePath(Strike.class.getSimpleName());
```

### 小帮手

关于可重用代码不仅可以放在帮手类，也可以做一个帮手接口，通过实现默认方法就可以直接调用了。详见文件夹下事例代码。

### 为什么只有ID是public的？

> java 修饰符：<br>
> public表示对所有类可见，也就是所有类都能获取这个变量或调用这个方法。private只有在这个类内部可以获取或调用。

Strike.java:
```java
public class Strike extends CustomCard {
    public static final String ID = ModHelper.makePath("Strike");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
}
```

直接调用这个常量难道不比手写方便很多。
```java
    card.cardID == "ExampleMod:Strike" // no
    Strike.ID.equals(card.cardID)// yes
```