@SpireInitializer
public class ExampleMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber{
    private static final String MY_CHARACTER_BUTTON = "ExampleModResources/img/char/Character_Button.png";
    private static final String MY_CHARACTER_PORTRAIT = "ExampleModResources/img/char/Character_Portrait.png";
    private static final String BG_ATTACK_512 = "ExampleModResources/img/512/bg_attack_512.png";
    private static final String BG_POWER_512 = "ExampleModResources/img/512/bg_power_512.png";
    private static final String BG_SKILL_512 = "ExampleModResources/img/512/bg_skill_512.png";
    private static final String SMALL_ORB = "ExampleModResources/img/char/small_orb.png";
    private static final String BG_ATTACK_1024 = "ExampleModResources/img/1024/bg_attack.png";
    private static final String BG_POWER_1024 = "ExampleModResources/img/1024/bg_power.png";
    private static final String BG_SKILL_1024 = "ExampleModResources/img/1024/bg_skill.png";
    private static final String BIG_ORB = "ExampleModResources/img/char/card_orb.png";
    private static final String ENERGY_ORB = "ExampleModResources/img/char/cost_orb.png";
    
    public static final Color MY_COLOR = new Color(79.0F / 255.0F, 185.0F / 255.0F, 9.0F / 255.0F, 1.0F);

    public ExampleMod() {
        BaseMod.subscribe(this);
        // 这里的EXAMPLE_GREEN是人物类里的，应写成MyCharacter.PlayerColorEnum.EXAMPLE_GREEN
        BaseMod.addColor(EXAMPLE_GREEN, MY_COLOR, MY_COLOR, MY_COLOR,
            MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512,
            BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024,
            BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB
        );
    }

    public static void initialize() {
        new ExampleMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
    }

    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "ExampleResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "ExampleResources/localization/" + lang + "/characters.json");
    }
}