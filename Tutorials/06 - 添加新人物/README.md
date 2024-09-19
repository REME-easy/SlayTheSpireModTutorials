# 添加新人物

添加颜色之后，就可以添加新人物了。我们先来创建一个人物类。首先新建一个文件夹管理你的人物类。

* examplemod
    * cards
    * <b>characters</b> <-这里添加
        * <b>MyCharacter.java</b>
    * modcore

接下来也是繁杂的理解并填写的过程，你可以直接复制并慢慢修改，但要注意哪些能修改哪些不能修改的提醒。同样的本教程将准备事例资源。

> 技巧：当你需要修改同一指代的单词时（如一个类名），idea可以<kbd>shift</kbd>+<kbd>f6</kbd>修改，vscode按下<kbd>f2</kbd>。


MyCharacter.java:
```java
// 继承CustomPlayer类
public class MyCharacter extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = "ExampleModResources/img/char/shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = "ExampleModResources/img/char/shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = "ExampleModResources/img/char/corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            "ExampleModResources/img/UI/orb/layer5.png",
            "ExampleModResources/img/UI/orb/layer4.png",
            "ExampleModResources/img/UI/orb/layer3.png",
            "ExampleModResources/img/UI/orb/layer2.png",
            "ExampleModResources/img/UI/orb/layer1.png",
            "ExampleModResources/img/UI/orb/layer6.png",
            "ExampleModResources/img/UI/orb/layer5d.png",
            "ExampleModResources/img/UI/orb/layer4d.png",
            "ExampleModResources/img/UI/orb/layer3d.png",
            "ExampleModResources/img/UI/orb/layer2d.png",
            "ExampleModResources/img/UI/orb/layer1d.png"
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("ExampleMod:MyCharacter");

    public MyCharacter(String name) {
        super(name, MY_CHARACTER,ORB_TEXTURES,"ExampleModResources/img/UI/orb/vfx.png", LAYER_SPEED, null, null);


        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 150.0F * Settings.scale);


        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                "ExampleModResources/img/char/character.png", // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                0.0F, 0.0F,
                200.0F, 220.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );


        // 如果你的人物没有动画，那么这些不需要写
        // this.loadAnimation("ExampleModResources/img/char/character.atlas", "ExampleModResources/img/char/character.json", 1.8F);
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
        // e.setTimeScale(1.2F);


    }

    // 初始卡组的ID，可直接写或引用变量
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for(int x = 0; x<5; x++) {
            retVal.add(Strike.ID);
        }
        retVal.add("ExampleMod:Strike");
        return retVal;
    }

    // 初始遗物的ID，可以先写个原版遗物凑数
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Vajra.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                75, // 当前血量
                75, // 最大血量
                0, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return EXAMPLE_GREEN;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return ExampleMod.MY_COLOR;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel("ExampleModResources/img/char/Victory1.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("ExampleModResources/img/char/Victory2.png"));
        panels.add(new CutscenePanel("ExampleModResources/img/char/Victory3.png"));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new MyCharacter(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return ExampleMod.MY_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return ExampleMod.MY_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    // 以下为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用

    // 注意此处是在 MyCharacter 类内部的静态嵌套类中定义的新枚举值
    // 不可将该定义放在外部的 MyCharacter 类中，具体原因见《高级技巧 / 01 - Patch / SpireEnum》
    public static class PlayerColorEnum {
        // 修改为你的颜色名称，确保不会与其他mod冲突
        @SpireEnum
        public static PlayerClass MY_CHARACTER;

        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***
        @SpireEnum
        public static AbstractCard.CardColor EXAMPLE_GREEN;
    }

    public static class PlayerLibraryEnum {
        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***

        // 这个变量未被使用（呈现灰色）是正常的
        @SpireEnum
        public static CardLibrary.LibraryType EXAMPLE_GREEN;
    }
}
```

最下面我们添加了一些必要的枚举，你可以在你之前添加颜色的地方引用它。

```java
// 主类
import static examplemod.characters.MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;

// 省略其他
public ExampleMod() {
            BaseMod.subscribe(this);
            // 这里注册颜色
            BaseMod.addColor(EXAMPLE_GREEN, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENEYGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }
// 省略其他
```
*如果你想让你的卡牌也使用这个卡牌颜色，改变以下变量：*

```java
// 卡牌类
import static examplemod.characters.MyCharacter.PlayerColorEnum.EXAMPLE_GREEN;

public class Strike extends CustomCard {
    private static final CardColor COLOR = EXAMPLE_GREEN;
```

和给卡牌添加本地化文本一样，我们需要给人物添加本地化内容。首先新建`characters.json`文件。

* ExampleModResources
    * localization
        * ZHS
            * cards.json
            * <b>characters.json</b> <-localization/ZHS文件夹下新建

characters.json:
```json
{
  "ExampleMod:MyCharacter": { // ID要和人物类中getCharStrings的参数一致
    "NAMES": [
      "自定义人物"
    ],
    "TEXT": [
      "这是一段人物描述", // 上面提到的人物描述
      "“你就是心脏？”" // 上面提到的面对心脏的文本
    ]
  }
}
```

还要向basemod注册。

ExampleMod.java:
```java
import static examplemod.characters.MyCharacter.PlayerColorEnum.MY_CHARACTER;

@SpireInitializer
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber,
EditCharactersSubscriber { // 添加EditCharactersSubscriber
    //...省略

    // 当开始添加人物时，调用这个方法
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "ExampleResources/localization/" + lang + "/cards.json");
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "ExampleResources/localization/" + lang + "/characters.json");
    }

}
```

*并未实际运行，如果出错请在issues或评论留言*

好了！如果你能选择自己的人物，那么算是正式入了mod制作的大门了。在下一章<b>添加新遗物</b>之后，我们将通过几个例子拓宽制作mod的思路。

如果你在关卡结束后报错了，原因是卡牌太少，每个类型至少需要3张牌。
