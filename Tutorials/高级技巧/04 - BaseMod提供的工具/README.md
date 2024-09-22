# BaseMod提供的工具

basemod提供了许多方便写mod的妙妙小工具，你可以不会用，但不能不知道有这些东西。下面简单介绍一下其中一些。

- [AutoAdd](#autoadd)
- [CardModifier](#card-modifier)
- [Custom Savable](#custom-savable)
- [Dynamic Text Blocks](#dynamic-text-blocks)
- [ReflectionHacks](#reflectionhacks)

## AutoAdd

wiki:https://github.com/daviscook477/BaseMod/wiki/AutoAdd

`AutoAdd`让你免受需要一个一个添加卡牌或者遗物的痛苦。

不用再：
```java
@Override
public void receiveEditCards()
{
    BaseMod.addCard(new Card1());
    BaseMod.addCard(new Card2());
    BaseMod.addCard(new Card3());
    BaseMod.addCard(new Card4());
    // ...
}
```

而是：
```java
@Override
public void receiveEditCards()
{
    new AutoAdd("ExampleMod") // 这里填写你在ModTheSpire.json中写的modid
        .packageFilter(AbstractExampleCard.class) // 寻找所有和此类同一个包及内部包的类（本例子是所有卡牌）
        .setDefaultSeen(true) // 是否将卡牌标为可见
        .cards(); // 开始批量添加卡牌
}
```

如果你不想一张卡被自动注册，对其打上`@AutoAdd.Ignore`注解即可。

`AutoAdd`不止可以批量添加卡牌遗物，还可以使用`any()`方法批量添加任意内容。

具体查看wiki和自行查看源码。

## Card Modifier

wiki:https://github.com/daviscook477/BaseMod/wiki/CardModifiers

`CardModifier`可以看成给卡牌上一个buff。你可以使用`CardModifier`给卡牌添加额外的消耗、保留、虚无等，或者各种额外效果。

### 简单用途：
添加：
```java
// 给卡牌添加消耗modifier
CardModifierManager.addModifier(card, new ExhaustMod());
```
这样以后，这张卡牌就带有消耗，描述会加上消耗，*并且在被复制时也会将消耗一同复制。*

移除方式一：
```java
// 通过ID删除modifier
CardModifierManager.removeModifiersById(card, ExhaustMod.ID, false);
```
第三个参数表示你是否希望让一个`modifier`可以有额外检测是否能被移除，可以通过重写`isInherent`方法实现。大多数情况输入`false`即可。

### 示例：
以下是一个示例的自定义标记`modifier`，表示一张卡牌是复制品：
```java
public class CopyModifier extends AbstractCardModifier {
    public static String ID = "ExampleMod:CopyModifier";
    private static final UIStrings STRINGS = CardCrawlGame.languagePack.getUIString(ID);

    // 修改描述
    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(STRINGS.TEXT[0], rawDescription);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CopyModifier();
    }

    // modifier的ID
    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
```
json:
```json
  "ExampleMod:CopyModifier": {
    "TEXT": [
      " *复制品 。 NL %s"
    ]
  },
```
如果要实现`复制一张卡牌，不能复制已经复制过的卡牌`的效果，可以使用`CardModifierManager.hasModifier`检测卡牌是否拥有此`modifier`，如果没有则复制卡牌，使用`CardModifierManager.addModifier`给卡牌加上此`modifier`。

### 注意事项：
如果给`masterDeck`中的卡牌添加`modifier`，它们会自动保存。可以通过打上`@AbstractCardModifier.SaveIgnore`注解跳过被保存。

## Custom Savable

用于局内保存。查看《03 - 保存数据》。

## Dynamic Text Blocks

wiki:https://github.com/daviscook477/BaseMod/wiki/Dynamic-Text-Blocks

这是一个动态卡牌描述的功能，主要用来解决英文文本中的单复数问题。

```json
"DESCRIPTION": "{@@}Draw !M! card{!M!|>1=s}."
```
在文本中添加`{@@}`表示你希望使用此功能。一般放在开头即可。

`{!M!|>1=s}`表示当`magicNumber`大于1时，这个字符串表示为`s`，否则不表示。

因此，当`magicNumber`为1时，文本则为：`Draw 1 card.`

当`magicNumber`大于1时，文本则为：`Draw n cards.`

其他功能和如何注册自定义检测器查看wiki。

## ReflectionHacks

该类提供了java反射相关的工具。它缓存了你读取的信息以提高运行效率，因此如果要使用反射使用此功能更好。

### 例子：

例如，你希望获得`AbstractPlayer`类中的`hoveredMonster`变量，但是可恶的矢野将它设置成了私有变量：
```java
    private AbstractMonster hoveredMonster;
```
因此这样不行：
```java
    AbstractMonster m = AbstractDungeon.player.hoveredMonster;
```
此时需要使用反射：
```java
    AbstractMonster m = ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster");
```

其他用法查看`ReflectionHacks`源码。