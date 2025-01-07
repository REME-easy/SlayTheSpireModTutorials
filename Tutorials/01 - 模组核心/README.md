# 模组核心

想要将自己的mod添加到游戏中，首先需要让Mod The Spire(以后简称mts)知道你创建了一个mod。通过向你的模组核心类添加`@SpireInitializer`注解让mts知道你创建了一个mod，并开始加载你mod的内容。

## 1.创建你的模组核心类

首先创建一个你的mod文件夹（idea为目录右键new package），并在其中创建一个modcore类。（*它的命名可以由你随意更改*）<br>

如果有`Main.java`文件，可以将其删掉。

项目结构看起来像这样：<br>
* src
    * main
        * java
            * examplemod(套一层文件夹防止和其他mod重名)
                * modcore
                    * ExampleMod.java

ExampleMod.java:
```java
@SpireInitializer // 加载mod的注解
public class ExampleMod {
    // 构造方法
    public ExampleMod() {
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new ExampleMod();
    }
}
```

<b>注意！这段代码并没有`package`和`import`，请读者自行添加，参考文件夹下的`ExampleMod.java`。如果你不了解相关知识，请学习一些java知识再查看教程。</b>

## 2.向basemod订阅事件

basemod提供了许多钩子，也就是在特定时间点调用所有订阅了该钩子的类的特定方法的东西。例如可以通过订阅`EditCardsSubscriber`来向basemod注册你的mod卡牌。<br>

要想订阅这些事件，首先你要实现相应接口，然后写相应的触发函数，最后告诉basemod你要订阅事件。

ExampleMod.java:
```java
@SpireInitializer
public class ExampleMod implements EditCardsSubscriber { // 实现接口
    public ExampleMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }

    public static void initialize() {
        new ExampleMod();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        // TODO 这里写添加你卡牌的代码
    }
}
```

关于如何添加之后的章节会提及。


<b>TIPS：可以在文件夹找到本章的代码。</b>