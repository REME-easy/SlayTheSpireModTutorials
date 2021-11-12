public interface IHelper {
    public default String makePath(String id) {
        return "ExampleMod:" + id;
    }

    // 可重用代码堆在这即可（不可编译，因为AbstractCard类也有这个名字的方法）
    // public default void addToBot(AbstractGameAction action) {
    //     AbstractDungeon.actionManager.addToBottom(action);
    // }
}