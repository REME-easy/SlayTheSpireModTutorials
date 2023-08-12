# 上传mod

1. 打开杀戮尖塔游戏根目录（即`steam/steamapps/common/SlayTheSpire`），在此处打开cmd（在文件管理器上方路径输入cmd并回车）。

2. 在命令行中输入`java -jar mod-uploader.jar new -w [你mod的名字]`新建一个工作区。

3. 此时目录下会出现一个新的文件夹，打开它。
* 该文件夹中，`image.jpg`为你mod的预览图，可以制作一张预览图并替换（注意是`jpg`格式）。
* `config.json`文件为你mod的设置文件，可以设置你mod在工坊里的标题、描述等。</br>
    1. `title`为mod标题，建议写英文。
    2. `description`为mod描述，建议不写或删除该行，之后在工坊里更改。
    3. `visibility`为mod可见性，private为本人可见，public为公开。可以在工坊里更改，不过记得第二次上传前也修改该项。
    4. `changeNote`为更新日志，建议不写或删除该行，之后在工坊里更改。
    5. `tags`为你mod的标签。可以上工坊看看有哪些常用标签，你写其他的也行。例子：`"tags": ["Character", "English"]`
</br>
<b>所有这些设置，都不能写中文，否则工坊里会乱码。</b>
* 将你mod的jar放入`content`文件夹告诉程序你要上传什么。此外也可以自己设置`pom.xml`，打包后自动复制一份到该目录。

4. 回到根目录，打开cmd，输入`java -jar mod-uploader.jar upload -w [你mod的名字]`上传你的mod。上传完成后过一段时间就能看到你的mod了。

5. 方便起见，可以在该目录下新建一个txt文件，把上传的命令复制到该文件，然后将它后缀改为cmd。这样以后上传只需要双击这个文件就可以了。