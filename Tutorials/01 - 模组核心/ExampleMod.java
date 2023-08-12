package examplemod.modcore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;

@SpireInitializer
public class ExampleMod implements EditCardsSubscriber {
    public ExampleMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ExampleMod();
    }

    @Override
    public void receiveEditCards() {
    }
}