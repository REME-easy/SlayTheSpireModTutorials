package ExampleMod.ModCore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.EditCharactersSubscriber;

@SpireInitializer
public class ExampleMod implements EditCharactersSubscriber {
    public ExampleMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ExampleMod();
    }

    @Override
    public void receiveEditCharacters() {
    }
}