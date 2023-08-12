# 包装卡牌类

将卡牌类包装可以把一些经常用到的代码用更简单的方式实现，或者实现某种共性的功能。

下面分享我的卡牌封装类：

*没有规定你一定得按我方式的来写，我只是提供一个代码简化思路。*</br>
*没有规定你一定得按我方式的来写，我只是提供一个代码简化思路。*</br>
*没有规定你一定得按我方式的来写，我只是提供一个代码简化思路。*


```java
public abstract class AbstractExampleCard extends CustomCard {
    // useTmpArt表示是否使用测试卡图，当你卡图不够用时可以使用
    public AbstractExampleCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE,
            CardRarity RARITY, CardTarget TARGET) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                GOLDENGLOW_CARD, RARITY, TARGET);
    }

    // 如果按这个方法实现，在cards文件夹下分别放test_attack.png、test_power.png、test_skill.png即可
    private static String getTmpImgPath(CardType t) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
            case CURSE:
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/test_%s.png"), type);
    }

    // 如果实现这个方法，只要将相应类型的卡牌丢进相应文件夹即可，如攻击牌卡图添加进img/cards/attack/下
    private static String getImgPath(CardType t, String name) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/%s/%s.png"), type, name.replace(ModHelper.makePath(""), ""));
    }
}
```

类似的，可以在这个类里写一些通用的方法以便快捷使用。