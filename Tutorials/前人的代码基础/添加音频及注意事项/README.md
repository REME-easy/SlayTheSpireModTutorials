

# 一些注意事项

如果 Mod 作者想要添加新的音效，可以使用 `BaseMod.addAudio`。

由于原版的**硬编码**处理的过程较多，特此添加一些注意事项。
## 背景音乐
原版中处理音频的类有以下这些，都在包 `com.megacrit.cardcrawl.audio` 下：

```
MusicMaster.class
SoundMaster.class
MainMusic.class
TempMusic.class
```

其中 `SoundMaster.class` 是处理音效的类，例如打击音效、观者的真言音效等等。

而 `MusicMaster.class` 是处理音乐的类，例如商店的背景音乐、各层的战斗背景音乐等等。

如果 Mod 作者想要类似于**打醒乐嘉维林会改变背景音乐**的效果，让我们先来看看原版是怎么实现的：

怪物乐嘉维林类（反编译的部分代码）：

```
com.megacrit.cardcrawl.monsters.exordium.Lagavulin.class
```

```java
    // 战斗开始前乐嘉维林的准备
    public void usePreBattleAction() {
        // this.asleep 对应的是在开始战斗时乐嘉维林的行为：进入精英房的初始意图是睡觉，并且没有播放音乐；在摸尸体事件中初始意图为强 Debuff，并且立即播放音乐
        if (this.asleep) {
            // 这一行的意思是缓存乐嘉维林被打醒时的音乐
            // 字符串 "ELITE" 对应 TempMusic.class getSong 方法中的 switch 的一个 case
            CardCrawlGame.music.precacheTempBgm("ELITE");

            // 获得 8 点格挡，获得 8 点金属化（对应乐嘉在战斗开始时的行为）
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 8));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 8), 8));
        } else {
            // 取消静音背景音乐
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();

            // 立即播放乐嘉维林的音乐
            CardCrawlGame.music.playTempBgmInstantly("ELITE");

            // 设置意图为强 Debuff
            setMove(DEBUFF_NAME, 1, AbstractMonster.Intent.STRONG_DEBUFF);
        }
    }

    // 其他方法...

    // 改变乐嘉维林状态的方法    
    public void changeState(String stateName) {
        if (stateName.equals("ATTACK")) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } else if (stateName.equals("DEBUFF")) {
            this.state.setAnimation(0, "Debuff", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } else if (stateName.equals("OPEN") && !this.isDying) {
            // 如果乐嘉的音乐变为 打开 状态并且不是已被击杀的
            this.isOut = true;
            updateHitbox(0.0F, -25.0F, 320.0F, 360.0F);
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3], 0.5F, 2.0F));
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, "Metallicize", 8));

            // 取消静音背景音乐
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();

            // 立即播放之前缓存的音乐（这里是 usePreBattleAction() 中缓存的 "ELITE" ）
            CardCrawlGame.music.playPrecachedTempBgm();

            this.state.setAnimation(0, "Coming_out", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } 
    }
    
    // 其他方法...

    // 乐嘉维林在死亡时的方法
    public void die() {
        super.die();

        // 乐嘉维林在死亡时淡出背景音乐
        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();
    }
```

从上面这些代码可以得知，如果 Mod 作者想要类似于**打醒乐嘉维林会改变背景音乐**的效果：

可以使用原版的 `CardCrawlGame.music.playTempBgmInstantly(String key)` 和 `CardCrawlGame.music.playPrecachedTempBgm()` 方法来**分别** *立即播放* 临时背景音乐 和 *立即播放* 之前 *缓存的* 临时背景音乐。

让我们来看看原版的这些方法是怎么写的呢？（反编译的部分代码）

**请看** `TempMusic.class` 中 `getSong(String key)` 方法的**注意事项**：

```
com.megacrit.cardcrawl.audio.MusicMaster.class
```
```java
    public void playTempBgmInstantly(String key) {
        if (key != null) {
            logger.info("Playing " + key);

            // new 了一个新的 TempMusic 并加入 tempTrack
            this.tempTrack.add(new TempMusic(key, true));

            // 静音其他主背景音乐
            for (MainMusic m : this.mainTrack)
                m.silenceInstantly(); 
        }
    }
  
    public void precacheTempBgm(String key) {
        if (key != null) {
            logger.info("Pre-caching " + key);

            // new 了一个新的 TempMusic 并加入 tempTrack
            this.tempTrack.add(new TempMusic(key, true, true, true));
        }
    }
  
    public void playPrecachedTempBgm() {
        if (!this.tempTrack.isEmpty()) {
            // 播放之前加入进 tempTrack 的音乐
            this.tempTrack.get(0).playPrecached();

            // 静音其他主背景音乐
            for (MainMusic m : this.mainTrack)
                m.silenceInstantly(); 
        } 
    }
  
    public void playTempBgmInstantly(String key, boolean loop) {
        if (key != null) {
            logger.info("Playing " + key);

            // new 了一个新的 TempMusic 并加入 tempTrack
            this.tempTrack.add(new TempMusic(key, true, loop));

            // 静音其他主背景音乐
            for (MainMusic m : this.mainTrack)
                m.silenceInstantly(); 
        } 
    }
```

**请看** `TempMusic.class` 中 `getSong(String key)` 方法的**注意事项**：

```
com.megacrit.cardcrawl.audio.TempMusic.class
```
```java
    public TempMusic(String key, boolean isFast) {
        this(key, isFast, true);
    }
    
    public TempMusic(String key, boolean isFast, boolean loop) {
        this.key = key;
        this.music = getSong(key);
        if (isFast) {
            this.fadeTimer = 0.25F;
            this.fadeTime = 0.25F;
        } else {
            this.fadeTimer = 4.0F;
            this.fadeTime = 4.0F;
        } 
        this.music.setLooping(loop);
        this.music.play();
        this.music.setVolume(0.0F);
    }
  
    public TempMusic(String key, boolean isFast, boolean loop, boolean precache) {
        this.key = key;
        this.music = getSong(key);
        if (isFast) {
            this.fadeTimer = 0.25F;
            this.fadeTime = 0.25F;
        } else {
            this.fadeTimer = 4.0F;
            this.fadeTime = 4.0F;
        } 
        this.music.setLooping(loop);
        this.music.setVolume(0.0F);
    }
  
    private Music getSong(String key) {
        // 注意到所有的构造 TempMusic 方法都使用了 getSong(String key) 来将 key 映射到某个具体的音乐
        // 杀戮尖塔的开发组在这里使用了 **硬编码** 来对应各个音乐
        // 显然如果 Mod 作者直接传入对应的 key 的话，需要注意下面几点：
        // 1. Mod 作者的音频文件路径和原版一致时这个方法可以正常进行，但这样有 文件冲突 的风险
        // 2. 如果不一致，需要使用 Patch 来捕获 key 并提前处理（具体请参见 高级技巧/01 - Patch），否则会导致 NullPointerException
        // 可以参考 ActLikeIt Mod 框架下的 TempMusicPatch：actlikeit.patches.TempMusicPatch.class

        switch (key) {
            case "SHOP":
                return MainMusic.newMusic("audio/music/STS_Merchant_NewMix_v1.ogg");
            case "SHRINE":
                return MainMusic.newMusic("audio/music/STS_Shrine_NewMix_v1.ogg");
            case "MINDBLOOM":
                return MainMusic.newMusic("audio/music/STS_Boss1MindBloom_v1.ogg");
            case "BOSS_BOTTOM":
                return MainMusic.newMusic("audio/music/STS_Boss1_NewMix_v1.ogg");
            case "BOSS_CITY":
                return MainMusic.newMusic("audio/music/STS_Boss2_NewMix_v1.ogg");
            case "BOSS_BEYOND":
                return MainMusic.newMusic("audio/music/STS_Boss3_NewMix_v1.ogg");
            case "BOSS_ENDING":
                return MainMusic.newMusic("audio/music/STS_Boss4_v6.ogg");
            case "ELITE":
                return MainMusic.newMusic("audio/music/STS_EliteBoss_NewMix_v1.ogg");
            case "CREDITS":
                return MainMusic.newMusic("audio/music/STS_Credits_v5.ogg");
        }
        // 如果你的文件路径和原版不同，那么 "audio/music/" + key 可能无法构成合法路径，进而导致 NullPointerException
        return MainMusic.newMusic("audio/music/" + key);
    }
```
