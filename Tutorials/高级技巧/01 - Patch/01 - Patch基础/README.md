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


<b>Q：交锋太菜了，我想把它的限制条件取消。该怎么做？</b>