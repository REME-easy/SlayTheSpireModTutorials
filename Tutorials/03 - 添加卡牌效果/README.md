# 造成6点伤害

## 1.卡牌效果
本章介绍如何让你的卡牌有造成伤害的效果。我们先来介绍`use`这个方法。

*本章内容有点多，可以先抄写之后再理解。*

### use(AbstractPlayer p, AbstractMonster m)

```java
    /**
     * 当卡牌被使用时，调用这个方法。
     * 
     * @param p 你的玩家实体类。
     * @param m 指向的怪物类。（无指向时为null，包括攻击所有敌人时）
     */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
```

想要让打击具有造成伤害的效果，我们需要向事件队列中添加一个事件。

> 尖塔各种伤害抽牌等效果都是通过添加action来实现的。这个队列类似于现实生活中的排队，先排到的先执行，没有排到第一个的只能排到队伍的最后一个，等到第一个执行完退出队列轮到第二个，重复直到轮到自己。可以总结成四个字，“先进先出”。（当然有插队的方法，这个另外讨论）

```java
    /**
     * 当卡牌被使用时，调用这个方法。
     * 
     * @param p 你的玩家实体类。
     * @param m 指向的怪物类。（无指向时为null，包括攻击所有敌人时）
     */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(
                m,
                new DamageInfo(
                    p,
                    damage,
                    DamageType.NORMAL
                )
            )
        );
    }
```

写成这样方便读者了解结构。让我们一个一个解释。

*你并不需要立刻了解以下这些全部，可以先抄写学习*

### AbstractDungeon.actionManager.addToBottom(...)
这行代码的意思是调用`AbstractDungeon`的变量`actionManager`的方法`addToBottom`。<br>
`AbstractDungeon`是一个拥有游戏大部分代码的类（~~游戏作者矢野偷懒把东西都写在这里了~~）。<br>
`actionManager`是游戏的事件队列管理器。<br>
`addToBottom`将你输入的参数添加到事件队列的末尾。<br>

### DamageAction(AbstractCreature target, DamageInfo info)
造成伤害的事件。该事件的构造函数有许多重载，这里使用两个参数的该重载。<br>
`target`是该事件造成伤害的目标，比如你指向的怪物。<br>
`info`是伤害信息。

### DamageInfo(AbstractCreature source, int base, DamageType type)
描述伤害的信息。<br>
`source`是该伤害的来源（比如说玩家）。<br>
`base`是该伤害的数值（关于伤害计算详见另外章节）。<br>
`type`是伤害类型。攻击伤害使用`NORMAL`，非攻击伤害（荆棘等）使用`THORNS`，失去生命使用`HP_LOSS`。

<b>综上，这段代码的意思是，向事件队列排入一个来源是玩家，造成该卡牌伤害数值的伤害事件，当该事件排队轮到时执行。</b>

*Q:写action好麻烦，能不能不在卡牌中写action？*<br>
*A:不能。现在你也许感受不到，但如果你直接在`use`中写效果，会导致你的判断提前执行，让你的卡牌效果出现异常。*

## 2.卡牌的数值及升级

这张卡牌暂时不能造成伤害，因为我们并没有告诉系统这张牌的伤害数值。在构造方法中这样写。

Strike.java:
```java
    public Strike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }
```
`baseDamage`是卡牌的基础伤害数值，也就是没有计算<b>易伤</b>等之前的伤害。<nr>
`tags`是卡牌的标签，例如添加`STARTER_STRIKE`（基础打击）让潘多拉变化这张牌，添加`STRIKE`（打击）让完美打击计算这张牌。<b>注意添加了`STARTER_STRIKE`并不会视为添加了`STRIKE`。</b>

接下来在`upgrade`方法中这样写。
```java
   public void upgrade() { // 升级调用的方法
    if (!this.upgraded) {
        this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
        this.upgradeDamage(3); // 将该卡牌的伤害提高3点。
    }
   }
```

好了，这样你便做出一张能造成6点伤害，升级变为9点的打击卡了。恭喜！

想知道其他类型的卡牌如何制作，你可以反编译游戏（使用JD-GUI，第一个README.md中提到过）查看原版的卡牌是如何制作的。

下一章我们将介绍本地化，请到时候修改你的卡牌类的内容。

样例代码中进行了优化，可以查看以少写一些代码。