# 独立计算的能力

你可能需要炸弹那样独立计算的能力。查看`ApplyPowerAction`，我们发现能力叠加检查是看能力的id是否相同，所以只要使你添加的每个同名buff的id不同即可。一种实现方式如下：

```java
public class ExamplePower extends AbstractPower {

    private static int postfix = 0;

    public ExamplePower(AbstractCreature owner, int Amount) {
        // 确保每个能力的id都不同
        this.ID = POWER_ID + postfix++;
        
        this.owner = owner;
        this.amount = Amount;
    }
}
```