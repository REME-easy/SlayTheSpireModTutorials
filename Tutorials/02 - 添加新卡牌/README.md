# “你好，打击！”

相对于编程入门的打印“hello world！”，制作出一张能运行的打击卡就算是mod制作入门了。<br>

## 1.创建卡牌类

首先，你需要创建一个卡牌类。在那之前创建一个文件夹管理所有卡牌。<br>

* examplemod
    * cards
        * Strike.java
    * modcore

要让java或basemod知道你这个类是一张卡牌，你需要继承CustomCard类。<br>

Strike.java:
```java
// 这段代码不能编译
public class Strike extends CustomCard {
    
}
```

这时候，你的代码编辑器会提醒你出错了（出现红色波浪线）。鼠标移到错误上，它提醒你没有继承抽象方法。你需要重写`upgrade()`方法和`use()`方法，因为这两个方法分别代表<b>升级</b>卡牌需要调用的方法和<b>使用</b>卡牌调用的方法，这对于一张卡牌是必须的。

> 如果你使用的代码编辑器有智能纠正功能，你可以将光标移动到错误代码处，然后按下纠错键（idea为<kbd>alt</kbd>+<kbd>enter</kbd>，vscode为<kbd>ctrl</kbd>+<kbd>.</kbd>）选择你需要的纠错选项。

除此以外，你还需要添加一个构造方法。

Strike.java:
```java
// 这段代码不能编译
public class Strike extends CustomCard {
    public Strike() {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    // 这些方法怎么写，之后再讨论
    @Override
    public void upgrade() {
        
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
    
}
```

## 2.添加卡牌信息

你发现这段代码还是一片红。电脑告诉你像`id`、`name`等这些参数没有定义。你需要告诉它你这张卡牌的信息。本教程建议你把这些信息写成常量方便管理。

Strike.java:
```java
public class Strike extends CustomCard {
    public static final String ID = "ExampleMod:Strike";
    private static final String NAME = "打击";
    private static final String IMG_PATH = "ExampleModResources/img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = "造成 !D! 点伤害。";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
    
}
```

一下子写了很多信息可能一下子反应不过来。我们一个一个解释。

### ID
ID是一张卡牌的同种类标识，用来区分不同卡名的卡牌。例如：
* <b>打击</b>和<b>防御</b>的ID不同。
* <b>打击</b>和<b>打击+</b>的ID相同。
* 一张<b>痛击</b>和另一张<b>痛击</b>的ID相同。
* 不同颜色的<b>打击</b>的ID是不同的。（例如蓝色打击为"Strike_B"，红色打击为"Strike_R"）

为了和其他mod区分开来，你需要在ID之前加上你的modid前缀。
```java
    public static final String ID = "ExampleMod:Strike";
```

### NAME
NAME是显示在卡牌上方的卡牌名字。

### IMG_PATH
IMG_PATH是该卡牌卡图的路径。它从`resources`文件夹开始查找。你可以把一张处理好的卡图放在该文件夹下，再为这个变量赋值你输入的路径即可配对卡图。
> 1.对于资源路径，也建议套一层文件夹以防和其他mod的资源重名。

> 2.尖塔的卡图需要一张图的两个尺寸，你需要准备一张大小为500 * 380的xxx_p.png(注意后缀)和一张大小为250 * 190的xxx.png，并将他们放在同一个文件夹下。读取时只需要读取无后缀名的png。注意为了美观裁剪成尖塔需要的形状。（文件夹中准备了两张打击图，可直接复制学习用）

> 3.该路径为相对路径，从`resources`文件夹开始查找，例如下方的路径查找的是`resources/ExampleModResources/img/cards/Strike.png`。

*例子：*<br>

目录：
* java
* resources
    * ExampleModResources <- 套一层自己的文件夹
        * img
            * cards
                * Strike.png
                * Strike_p.png

*注意并不是java的子文件夹，是和java文件夹平行的resources文件夹*

Strike.java:
```java
    private static final String IMG_PATH = "ExampleModResources/img/cards/Strike.png";
```

你可以自己按分类创建攻击卡，技能卡和能力卡的卡图文件夹，怎么管理资源就见仁见智了。

### COST
卡牌的费用。<br>

特殊的：-2费不显示能量图标（如诅咒卡状态卡等），-1费为X费（旋风斩等）。

### DESCRIPTION
卡牌的描述。<br>

尖塔使用了自己的描述方式，该方面见另外的教程。本教程中，` !D! `（前后空格）将被伤害数值替代。<br>
//TODO
```java
    private static final String DESCRIPTION = "造成 !D! 点伤害。";
```

### TYPE
卡牌类型。（攻击牌、技能牌、能力牌、诅咒牌、状态牌）

*Q:能自定义卡牌类型吗？*<br>
*A:自定义卡牌类型需要修改很多地方，并且极大可能与其他mod不兼容，一般禁止自定义卡牌类型，但可以修改卡牌类型的描述文字（卡牌中间）。关于如何修改详见patch章节。*

### COLOR
卡牌颜色，比如原版的红、绿、蓝、紫、无色，诅咒。

### RARITY
卡牌稀有度。

| 枚举 | 名称 | 出现在战斗奖励和商店 | 卡框颜色 | 说明 |
| --- | --- | --- | --- | --- |
| BASIC | 基础 | X | 灰 | 打击、防御、痛击等 |
| COMMON | 普通 | √ | 灰 | |
| UNCOMMON | 罕见 | √ | 蓝 | |
| RARE | 稀有 | √ | 金 | |
| SPECIAL | 特殊 | X | 灰 | 小刀等衍生牌，JAX等事件牌 |
| CURSE | 诅咒 | X | 灰 | 诅咒需要卡牌颜色和稀有度都为CURSE。 |

### TARGET
卡牌指向类型的目标。实际功能只有是否指向敌人的区分。

这样，一张卡牌就制作完成了。在你的modcore类注册该卡牌，打包后运行游戏即可在图书馆看到这张卡。

ExampleMod.java:
```java
    public void receiveEditCards() {
        // 向basemod注册卡牌
        BaseMod.addCard(new Strike());
    }
```

在游戏控制台键入`hand add [card_id]`。如果出现了这张牌，说明你成功注册了它。如果没有，检查你的代码直到出现。

> 如何使用[控制台](https://github.com/daviscook477/BaseMod/wiki/Console)?

*如果你的代码报错并且输出了报错信息（前提是mts中勾选debug），你可以查看哪里出错。详见其他章节。*

事例代码依旧在文件夹下。注意这些代码缺少了package，并不能实际运行。（~~就是别照抄的意思~~）

然后你发现这张卡牌并没有任何效果。查看下一章如何书写卡牌效果。