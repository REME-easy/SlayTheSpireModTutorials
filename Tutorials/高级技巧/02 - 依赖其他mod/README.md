# 依赖

## 必要前置依赖

如果你要使用`stslib`，`actlikeit`或`lazy man kits`这样的前置mod以减少自己的代码工作量，看这一节。

1. 先找到那个mod在steam的位置。例如stslib是在`xxx\steam\steamapps\workshop\content\646570\1609158507`这个位置。

2. 打开你项目中的pom.xml，找到`<dependencies></denpendencies>`这个结构，像已经写好的`modthespire`和`basemod`一样，这么写：

```xml
    <dependencies>
        <dependency>
            <groupId>com.megacrit.cardcrawl</groupId>
            <artifactId>slaythespire</artifactId>
            <version>2020-11-30</version>
            <scope>system</scope>
            <systemPath>${Steam.path}/common/SlayTheSpire/desktop-1.0.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>basemod</groupId>
            <artifactId>basemod</artifactId>
            <version>5.33.1</version>
            <scope>system</scope>
            <systemPath>${Steam.path}/workshop/content/646570/1605833019/BaseMod.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>com.evacipated.cardcrawl</groupId>
            <artifactId>ModTheSpire</artifactId>
            <version>3.23.2</version>
            <scope>system</scope>
            <systemPath>${Steam.path}/workshop/content/646570/1605060445/ModTheSpire.jar</systemPath>
        </dependency>

        <!-- 新的内容写在这里 -->
        <dependency>
            <groupId>com.evacipated.cardcrawl.mod</groupId>
            <artifactId>stslib</artifactId>
            <version>2.5.0</version>
            <scope>system</scope>
            <systemPath>${Steam.path}/workshop/content/646570/1609158507/StsLib.jar</systemPath>
        </dependency>
    </dependencies>
```

3. 如果你使用`idea`，写完后如果右上角有一个刷新按钮，就点击刷新，完成后你就可以使用`stslib`的东西了。

4. 最后你要在`ModTheSpire.json`里添加你加入的依赖，这样mts就能知道你使用了该前置mod。
（`dependencies`中写该mod的`modid`。你可以在那个mod的`ModTheSpire.json`中找到。）

```json
{
    "dependencies": ["basemod", "stslib"]
}
```

## 可选前置依赖（进阶）

进阶一点，如果你想使用`遗物升级lib`或`危机合约`这样的前置mod，但你并不想让它们作为必要的前置，仅在开启相应mod时有联动，看这一节。

1. 先像添加前置依赖一样将你需要的mod写在`pom.xml`里。

2. 在`ModTheSpire.json`里添加`optional_dependencies`。

```json
{
    "optional_dependencies": ["RelicUpgradeLib"]
}
```

3. 创建一个新的类，<b>并且只在这个类里使用你想依赖的mod的内容。</b>（如果你想使用`遗物升级lib`，可以参考`testmod`是怎么写的，~~`REMEMod`的结构过于混乱不建议参考~~）

```java
import relicupgradelib.RelicUpgradeLib;

public class RelicUpgradeMgr {

    public static void addAllUpgradeRelics() {
        // 在这里注册
        RelicUpgradeLib.addUpgrade(...);
    }
}
```

4. 找一个合适的初始化时机（例如，如果你想依赖`遗物升级lib`，可以在`receiveEditRelics`里，所有遗物注册完后写），判断是否开启该mod。

```java
public void receiveEditRelics() {
    // 省略

    if (Loader.isModLoaded("RelicUpgradeLib")) {
        // 这里调用你对那个mod的管理类的初始化函数
        RelicUpgradeMgr.addAllUpgradeRelics();
    }
}
```

这样做的话，如果不开启该mod，那么你的mod就不会尝试调用那个mod的内容。