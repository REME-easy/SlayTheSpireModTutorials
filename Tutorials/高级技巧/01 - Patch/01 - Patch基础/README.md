# 通过例子学Patch

<b>Q：我想给打击加一个交锋的特效。该怎么做？</b>

首先反编译原版游戏，找到打击`com.megacrit.cardcrawl.cards.red.Strike_Red`。

Strike_Red.java:
```java
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (Settings.isDebug) { // 38 行
      if (Settings.isInfo) { // 39
        this.multiDamage = new int[(AbstractDungeon.getCurrRoom()).monsters.monsters.size()];
        for (int i = 0; i < (AbstractDungeon.getCurrRoom()).monsters.monsters.size(); i++) {
          this.multiDamage[i] = 150;
        }
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
      } else {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, 150, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
      } 
    } else {
      addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL)); // 49
    } 
  }
```

Patch的功能之一是允许我们在方法开头或方法结尾“添加”代码。（具体原理不解释，去看java assist）

找一个地方编写以下代码，例如专门新建一个`patches`文件夹收录你的patch。

```java
    @SpirePatch(clz = Strike_Red.class, method = "use")// 1
    public static class MonsterUpdatePatch {
        public static void Prefix(Strike_Red __instance, AbstractPlayer p, AbstractMonster m) {// 2
          if (m != null) {
            addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new ClashEffect(m.hb.cX, m.hb.cY), 0.1F));
          }
        }
    }
```

`1`行的意思是声明一个patch，告诉mts你要修改`Strike_Red`类的`use`方法。

`2`行`Prefix`方法表明你修改的地方是这个方法的开头。`__instance`是你修改的实例，后面的参数是这个方法原本的参数。

当你打包运行游戏，这段代码就会生效，具体的效果类似于：

```java
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (m != null) {
      addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new ClashEffect(m.hb.cX, m.hb.cY), 0.1F));
    }// 此patch的效果，注意这相当于在一行上添加代码 38行
    if (Settings.isDebug) { // 38 行
      if (Settings.isInfo) { // 39
        this.multiDamage = new int[(AbstractDungeon.getCurrRoom()).monsters.monsters.size()];
        for (int i = 0; i < (AbstractDungeon.getCurrRoom()).monsters.monsters.size(); i++) {
          this.multiDamage[i] = 150;
        }
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
      } else {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, 150, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
      } 
    } else {
      addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL)); // 49
    } 
  }
```

如果你使用的不是`Prefix`而是`Postfix`，那么就会修改该方法的结尾。