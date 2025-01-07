保存数据分为两类：全局保存和局内保存。其中全局保存一般使用`SpireConfig`，局内保存一般使用`CustomSavable`，这两个功能分别在`ModTheSpire`Wiki和`Basemod`Wiki上有说明。

# 全局保存（SpireConfig）
首先自己选择一个时刻初始化保存数据，例如模组核心的构造函数，`initialize`函数，或者`implements PostInitializeSubscriber`后使用`receivePostInitialize`等接口。本例子使用`receivePostInitialize`接口。

读取数据：
```java
    // 为模组核心添加新的字段
    public static boolean saveField;

    @Override
    public void receivePostInitialize() {
        try {
            // 设置默认值
            Properties defaults = new Properties();
            defaults.setProperty("save_field", "false");
            // defaults.setProperty("save_field_2", "false");

            // 第一个字符串输入你的modid
            SpireConfig config = new SpireConfig("ExampleMod", "Common", defaults);
            // 如果之前有数据，则读取本地保存的数据，没有就使用上面设置的默认数据
            saveField = config.getBool("save_field");
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }
```
除了`boolean`类型，你还可以使用其他数据类型，例如`int`等。

当你想要保存时：
```java
    ExampleMod.saveField = true;
    try {
        SpireConfig config = new SpireConfig("ExampleMod", "Common");
        config.setBool("save_field", true);
        config.save();
    } catch (IOException e) {
        e.printStackTrace();
    }
```

# 局内保存（CustomSavable）

`CustomSavable`用于保存某局游戏的数据，例如你可以实现自己的**仪式匕首**、**遗传算法**等。

当你想保存`CustomCard`、`CustomRelic`、`CustomPotion`的数据时，非常简单：
```java
// 接入CustomSavable接口，泛型填你要保存的、可以转换为json元素的任意类型
public class Strike extends CustomCard implements CustomSavable<Integer> {
    public int value = 0;

    // 保存
    @Override
    public Integer onSave() {
        return value;
    }

    // 读取
    @Override
    public void onLoad(Integer save) {
        this.value = save;
    }
}
```

当你想要保存多个数据时，可以使用`ArrayList`或`HashMap`，或者新建一个类进行保存：
```java
// 接入CustomSavable接口，泛型填ModSaveData
public class Strike extends CustomCard implements CustomSavable<ModSaveData> {
    public int val1 = 0;
    public ArrayList<String> val2 = new ArrayList<>();

    // 保存
    @Override
    public ModSaveData onSave() {
        return new ModSaveData(val1, val2);
    }

    // 读取
    @Override
    public void onLoad(ModSaveData save) {
        this.val1 = save.val1;
        this.val2 = save.val2;
    }

    public static class ModSaveData {
        public int val1;
        public ArrayList<String> val2;

        public ModSaveData(int val1, ArrayList<String> val2) {
            this.val1 = val1;
            this.val2 = val2;
        }
    }
}
```

当你想要在其他地方，例如模组核心处保存，需要额外做以下工作：
1. 接入**至少一个继承了`ISubscriber`的接口**，例如`EditCardsSubscriber`等。如果你不想产生额外函数，接入`ISubscriber`即可。
2. 在构造函数使用`BaseMod.subscribe`和`BaseMod.addSaveField`。
```java
// 
@SpireInitializer
public class ExampleMod implements ISubscriber, CustomSavable<ModSaveData>  {
    public ExampleMod() {
        // 至少订阅一次
        BaseMod.subscribe(this);
        // 字符串填不会和其他SaveField重名的id
        BaseMod.addSaveField("ExampleMod:ModSaveData", this);
    }
}
```
之后正常编写`onSave`和`onLoad`函数即可。