# 自定义Action

action是构成卡牌、遗物、能力等效果的一个基础单元。比如说抽牌使用的是`DrawCardAction`，伤害使用的是`DamageAction`。

每添加一个action实质上是往一个队列里排队，排在前面的先执行。

下面我们来一步步构造一个“<b>造成伤害，并在斩杀时抽一张牌</b>”的action。

首先新建一个空的action。
```java
public class ExampleAction extends AbstractGameAction {

    public ExampleAction() {
    }

    @Override
    public void update() {
    }

}
```
这里包含一个构造函数和一个`update`方法。`update`方法的作用是每次游戏循环时执行你这个action产生一些效果。

我们需要告诉action你攻击的目标是谁，并在`update`时对其造成伤害。
```java
public class ExampleAction extends AbstractGameAction {
    // 伤害信息
    public DamageInfo info;

    public ExampleAction(AbstractMonster target, DamageInfo info) {
        this.target = target;
        this.info = info;
    }

    @Override
    public void update() {
        // 目标受到伤害
        this.target.damage(this.info);
    }

}
```
受到伤害写好了，接下来写如果斩杀就抽一张牌。
```java
public class ExampleAction extends AbstractGameAction {
    public DamageInfo info;

    public ExampleAction(AbstractMonster target, DamageInfo info) {
        this.target = target;
        this.info = info;
    }

    @Override
    public void update() {
        this.target.damage(this.info);
        if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead
                && !this.target.hasPower("Minion")) {
            this.addToTop(new DrawCardAction(1));
        }
        this.isDone = true;
    }

}
```
`if`里的条件分别表示：
`(this.target.isDying || this.target.currentHealth <= 0`表示造成伤害后使目标的生命降至0或以下，或目标现在濒死。
`!this.target.halfDead`非半死，主要判断是否为觉醒者一阶段。
`this.target.hasPower("Minion")`没有爪牙能力，斩杀的一个条件。

如果上述条件满足，则`addToTop`一个抽牌action。

关于addToBot和addToTop，addToBot表示排在最下边，addToTop表示排在最上边。
![图片.png](https://s2.loli.net/2022/05/30/oji1rFIOqmWpUZY.png)
在action里添加action时，一般使用`addToTop`排在最上边，这样让你的效果马上执行。

*注意当有多个`addToTop`的时候，由于写在下面的被排到最上边了，写在上面的会后执行！*

## 最后最重要的一点
<b>`isDone=true`是必须写的</b>，这表示你这个action执行完了。下次游戏循环检测时，如果你这个action的isDone为true，则不会执行update方法，并将你的action移除，执行下一个action。

<b>但是`isDone`并没有结束代码的作用！像下面的代码，`isDone`后面的内容还是会执行</b>
```java
@Override
public void update() {
    System.out.println("被执行");
    this.isDone = true;
    System.out.println("也被执行");
}
```

这样一个action就写好了。action的使用不需要注册，只要在某些时候加入队列即可。例如往你的卡牌效果里写：
```java
@Override
public void use(AbstractPlayer p, AbstractMonster m) {
    this.addToBot(new ExampleAction(m, new DamageInfo(p, this.damage, DamageType.NORMAL)));
}
```