# 学习他人代码

你想看原版某张卡或某个遗物是怎么写的，这个时候需要反编译。以下提供三个方法的反编译。<br>
<b>注意：如果仅仅学习代码，哪个都可以。如果你要查看Patch的行数，请使用JD-GUI，idea和vscode自带的行数不准确。</b>

## idea
查看左侧项目结构，下方的`External Libraries`中可以找到游戏源码的位置。游戏的资源一般裸露在外（~~因为矢野没有套一层文件夹~~），游戏的代码放在`com.megacrit.cardcrawl`中。<br>

![](https://i.loli.net/2021/11/13/slyCzBcuFfRnVmA.png)

或者，直接输入你想看的类，然后<kbd>ctrl</kbd>+单击即可。

## vscode
在`JAVA PROJECTS`中找到`Maven Dependencies`。游戏的资源一般裸露在外（~~因为矢野没有套一层文件夹~~），游戏的代码放在`com.megacrit.cardcrawl`中。<br>

![](https://i.loli.net/2021/11/13/rIELMcztuyDTRCi.png)

或直接输入你想看的类，然后<kbd>ctrl</kbd>+单击即可。

## JD-GUI
首先下载[JD-GUI](http://java-decompiler.github.io/)。打开软件，将游戏根目录下的`desktop-1.0.jar`直接拖入该软件（如果不知道如何查找steam游戏根目录可以百度）即可查看。