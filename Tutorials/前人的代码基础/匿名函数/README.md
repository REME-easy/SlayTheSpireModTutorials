# 匿名函数（Lambda表达式）

> Lambda 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。
> Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。
> 使用 Lambda 表达式可以使代码变的更加简洁紧凑。

Lambda可以让你不写类就能实现某种效果。在写尖塔mod时有用的地方比如：可以不用新写action类。

一种实现方法：
在你的`ModHelper`或你的通用接口里写：
```java
public class ModHelper {
    public static void addToBotAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public static void addToTopAbstract(Lambda  func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public interface Lambda extends Runnable {}
}
```

这样你就能很轻松的写卡牌效果什么的了，并且执行顺序也有保障：
```java
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
        }
        this.addToBot(new ApplyPowerAction(m, p, new MarkPower(m, this.magicNumber), this.magicNumber));
        // 这里
        ModHelper.addToBotAbstract(() -> {
            if (m.hasPower(MarkPower.POWER_ID))
                addToTop(new GainBlockAction(p, p, m.getPower(MarkPower.POWER_ID).amount));
        });
    }
```

上面的代码相当于下面的添加action和一个新的action类：
```java
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
        }
        this.addToBot(new ApplyPowerAction(m, p, new MarkPower(m, this.magicNumber), this.magicNumber));
        // 这里
        this.addToBot(new ATestAction(m));
    }
```
```java
public class ATestAction extends AbstractGameAction {
    public ATestAction(AbstractMonster target) {
        this.target = target;
    }

    @Overrider
    public void update() {
        if (this.target.hasPower(MarkPower.POWER_ID)) {
            AbstractPlayer p = AbstractDungeon.player; 
            addToTop(new GainBlockAction(p, p, this.target.getPower(MarkPower.POWER_ID).amount));
            isDone = true;
        }
    }
}
```