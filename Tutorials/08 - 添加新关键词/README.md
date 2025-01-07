# 添加关键词

BaseMod为Mod作者提供了一种添加自定义关键词的方式：
```java
public class ExampleMod implements EditKeywordsSubscriber {
    //...省略
    @Override
    public void receiveEditKeywords() {
        BaseMod.addKeyword("examplemod", "流血", new String[] {"流血"}, "拥有 #y流血 的角色在受到伤害时失去等量生命。");
    }
    //...省略
}
```

`addKeyword(String modID, String proper, String[] names, String description)`

`modID`:你的mod的id，用于和其他mod的关键词区分。当你使用modID时，你的关键词需要加上前缀，如："examplemod:流血"。

`proper`:关键词的正确名称，显示在关键词提示框中。

`names`:所有能别识别的名称，例如，如果你`proper`设置为“法术（X）”，`names`设置为“法术”，“法术的”，那么描述中“examplemod:法术”和“examplemod:法术的”都会被识别为该关键词，提示框的标题为“法术（X）”。

`description`:关键词描述。

## 使用JSON加载关键词

理论上，这样可以添加关键词，但是修改起来十分麻烦，~~还可能因为硬编码被群里的作者打~~。这里提供一种使用json加载关键词的方式。

```java
public class ExampleMod implements EditKeywordsSubscriber {
    //...省略
    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "eng";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "zhs";
        }

        String json = Gdx.files.internal("ExampleModResources/localization/Keywords_" + lang + ".json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("examplemod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
    //...省略
```

这样的话，只要写json就能添加关键词了，还可以处理本地化问题。

keywords_zhs.json:
```json
// 注意！！！如果使用这种写法，最外层不是{而是[
[
  {
    "NAMES": [
      "恐惧"
    ],
    "DESCRIPTION": "拥有 #y恐惧 的角色造成的伤害减少。"
  }
]
```

在卡牌描述中使用：
```json
    "DESCRIPTION": "造成 !D! 点伤害。 NL 给予 !M! 层 examplemod:恐惧 。"
```

在遗物描述中使用：
```json
    "DESCRIPTIONS": [
      "战斗开始时，给予随机敌人 !M! 层 #yexamplemod:恐惧 。"
    ]
```

能力、关键词描述中不会再解析关键词，只需要`#y恐惧`即可。