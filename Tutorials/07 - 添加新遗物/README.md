# 为你的人物添加初始遗物

尖塔里每个人物都有自己的初始遗物。本章教学如何添加遗物。

经过之前章节的学习，你大概了解了如何添加一个尖塔内容：

* 在相应文件夹下创建并编写类
* 编写本地化文件，并在类中使用本地化内容
* 向basemod注册该内容

你可以先不看教程尝试如何制作遗物。

<br><br><br>

## 1.创建遗物类

首先创建一个文件夹管理遗物类，再创建一个遗物类。

* examplemod
    * actions
    * cards
    * characters
    * modcore
    * <b>relics</b>
        * <b>MyRelic.java</b> <-

MyRelic.java:
```java
// 继承CustomRelic
public class MyRelic extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath("MyRelic");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "ExampleModResources/img/relics/MyRelic.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public MyRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MyRelic();
    }
}
```

添加效果只需要在你需要触发时机的方法中书写效果即可。例如下方的方法表示战斗开始时抽一张牌。
```java
public class MyRelic extends CustomRelic {
    // ...其余省略
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.addToBot(new DrawCardAction(1));
    }
    // ...
}
```

## 2.本地化内容
遗物类只需要保证ID对的上即可。

relics.json:
```json
{
  "ExampleMod:MyRelic": {
    "NAME": "测试遗物", // 名称
    "FLAVOR": "这个人很懒，什么都没写", // 遗物检视界面的风味描述
    "DESCRIPTIONS": [
      "每场战斗开始时，抽 #b1 张牌。" // 描述。注意不要忘记中括号（表示数组）。这里#b表示染成蓝色，详见新手必备知识。
    ]
  }
}
```

## 3.注册内容
在basemod中注册。

ExampleMod.java:
```java
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber,
EditRelicsSubscriber { // 新增
// ...其余省略

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "ExampleResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "ExampleResources/localization/" + lang + "/characters.json");
        // 添加注册json
        BaseMod.loadCustomStringsFile(RelicStrings.class, "ExampleResources/localization/" + lang + "/relics.json");
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new MyRelic(), RelicType.SHARED); // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
    }
}
```

如果你想添加到初始遗物：

MyCharacter.java:
```java
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MyRelic.ID); // 这里
        return retVal;
    }
```
