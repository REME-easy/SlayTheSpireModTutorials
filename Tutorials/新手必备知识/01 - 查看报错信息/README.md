# 查看报错信息

## 1.异常处理

有时候，游戏崩溃了，你想要查看是哪里的代码出错了。这时候需要查看mts的debug窗口。

*<b>需要在mod加载窗口勾选debug</b>*

![001](https://i.loli.net/2021/11/11/2IcZq8gU4QvSxuB.png)

在`Game crashed.`下便是这次的报错信息。它展示了必备组件的版本，开启的mod和异常信息。<br>

这里显示的是<b>空指针错误</b>（NullPointerException），说明程序将调用对象时却遇到了null。（不懂的可以去看java异常处理）<br>

具体报错的是`ExampleMod`类的
`receiveEditCards`方法，并且是`ExampleMod`的第<b>20</b>行。

如果难以看出是哪个mod出的错，可以向上翻几行，上面有详细的异常处理信息。

![002](https://i.loli.net/2021/11/11/TyUsveD8NgilwL6.png)<br>
*ExampleMod出错了*

最后展示一下杀戮尖塔mod常见的几种异常。
| 名称 | 可能原因 | 如何纠错 |
| --- | --- | --- |
| NullPointerException | 一个变量为空，但你却当它不为空来使用；可能是缺少图片、文本，具体看上下文 | 进行`val != null`的判断；查看自己是否缺少资源 |
| ArrayIndexOutOfBoundsException | 你索引的数组序号在其范围之外。 | 查看数组序号是否超范围。 |
| ConcurrentModificationException | 你在遍历中增加或删除了元素。（常见的在effects中直接添加、怪物直接在怪物列表添加怪物） | 换一个方向遍历，使用正规的添加删除手段 |

*（当遇到`ConcurrentModificationException`时，如果是`effectList`报错，可以通过往`effectQueue`,`topLevelEffects`,`topLevelEffectsQueue`添加尝试解决）*

## 2.输出控制台

有时候你想在一个地方检查某个变量的值。这时候你可以在控制台中打印一些信息。有两种方法。（但不止两种）

* System.out.print(...);
* 使用`logger`。（带有时间，和是哪个类发出的信息，比较格式化）

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// 省略...
public class ExampleMod {
    public static final Logger logger = LogManager.getLogger(ExampleMod.class);

    public ExampleMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ExampleMod();

        // 两种方法
        System.out.print("你好世界！");
        logger.info("你好世界！");

        // 带变量
        int x = 10;
        logger.info("x的值为" + x);
        logger.info(String.format("x的值为%d", x));
    }
}
```