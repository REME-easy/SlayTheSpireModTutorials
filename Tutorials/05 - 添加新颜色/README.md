# 添加新颜色

之后的几章是关于添加新人物的。人物涉及许多美术资源，你可以先使用文件夹下来自刻俄柏mod的临时资源，之后再慢慢替换。

## 1.选择你的颜色
根据你的人物颜色基调在modcore类新建一个RGB的`Color`颜色变量。如果你对什么是RGB颜色不熟悉，可以打开画图编辑颜色，右下角红绿蓝三色的数值经过计算后可以填入`Color`的构造函数。

> 前三个参数表示RGB，范围为0.0~1.0。而画图中的RGB范围为0~255，所以前三个参数需要除以255。第四个参数表示透明度。

![001](https://i.loli.net/2021/11/12/Y9oB4upTDtLblyk.png)

```java
// ...省略
public class ExampleMod implements EditCardsSubscriber {
    // 除以255得出需要的参数。你也可以直接写出计算值。
    private static final Color MY_COLOR = new Color(79.0F / 255.0F, 185.0F / 255.0F, 9.0F / 255.0F, 1.0F);

    public ExampleMod() {
        BaseMod.subscribe(this);
    }
// ...省略
}

```
> 数字后面加f或F表示该数字为浮点数。<br>


## 2.添加颜色
接下来是向basemod注册自己的颜色。这里需要填的东西很多，它们代表的意思本教程一一列举在旁边。你也可以查看对应路径的图片。

```java
// 这段代码不能编译
public class ExampleMod implements EditStringsSubscriber,EditCardsSubscriber {
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "ExampleModResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "ExampleModResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "ExampleModResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "ExampleModResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "ExampleModResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String small_orb = "ExampleModResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "ExampleModResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "ExampleModResources/img/1024/bg_power.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_1024 = "ExampleModResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String big_orb = "ExampleModResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String energy_orb = "ExampleModResources/img/char/cost_orb.png";
    private static final Color MY_COLOR = new Color(79.0F / 255.0F, 185.0F / 255.0F, 9.0F / 255.0F, 1.0F);


    public ExampleModModCore() {
            BaseMod.subscribe(this);
            // 这里注册颜色
            BaseMod.addColor(EXAMPLE_CARD, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,energy_orb,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,big_orb,small_orb);
    }
```
*这里缺少一个卡牌颜色的枚举。将在下一章介绍。*<br>
查看接下来一章了解如何添加新人物。

<br><br><br>

## 进阶：简化代码
在获取资源的时候，总是要写`"ExampleModResources/"`这样一层自己套的路径，很麻烦。可以在自己的小帮手中封装一个新的方法。（小帮手类详见前一章进阶）

```java
public class ModHelper {
    public static String MakePath(String id) {
        return "ExampleMod:" + id;
    }

    // 制作资源路径
    public static String MakeAssetPath(String id) {
        return "ExampleModResources/" + id;
    }
}
```

于是一些路径的写法就可以简化了。

```java
    String path = ModHelper.MakeAssetPath("img/....");
```