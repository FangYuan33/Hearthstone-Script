![GitHub release](https://img.shields.io/github/release/xjw580/Hearthstone-Script.svg)  ![GitHub](https://img.shields.io/github/license/xjw580/Hearthstone-Script?style=flat-square)

# 炉石传说脚本（网易暴雪复婚，本项目恢复更新，下个稳定版将在炉石国服可玩后更新）
![favicon.ico](Hearthstone-Script/src/main/resources/fxml/img/favicon.png)



### 免责申明

本项目仅供学习交流 `Java` 和 `炉石传说` 玩法，不得用于任何违反法律法规及游戏协议的地方😡！！！。



### 问题反馈

> 目前处于项目维护阶段，只处理脚本运行状况问题，不处理出牌策略等相关问题。

1. **任何问题都要通过提issue的方式提问**
2. 在提issue时，请及时地回复作者的消息
3. 可以的话附上日志文件，日志在log目录下



### 使用环境

- Windows（仅在Win10和Win11上测试过）
- JDK 21（**如无安装，第一次启动脚本会打开浏览器下载，然后安装就行了**）



### 注意事项

- ~~该脚本会控制你的鼠标键盘，影响正常使用，建议用另一台电脑或在虚拟机里使用~~



### 支持的卡组

- ~~偶数萨（有瑕疵但推荐）~~
- ~~核心骑（由核心卡组成的套牌，哪里亮了点哪里，不推荐）~~
- 基础套牌(通用策略，未对卡牌和卡组适配，自行组一套无战吼无法术的套牌即可)



### 使用步骤

- **下载脚本**
  - 在[release](https://gitee.com/zergqueen/Hearthstone-Script/releases)处下载
  - HearthstoneScript.zip包含完整依赖，一般下载此文件
  
- **初次使用**：
  - 配置卡组：鼠标悬浮在日志栏里的卡组代码上点击复制按钮，然后自行到游戏里创建卡组，再将此卡组移动到一号卡组位
  
- **启动脚本**
  1. 双击 `hs-script.exe` 文件
  
  2. 点击开始按钮或者使用热键 `Ctrl` + `P`
  
- **关闭脚本**

  - 双击 `stop.bat` 文件
  - 使用热键 `Alt` + `P`  
  - 程序托盘处点击退出



### 插件支持

> 支持套牌和卡牌插件

1. 在[release](https://gitee.com/zergqueen/Hearthstone-Script/releases)处下载zip包，解压将lib目录下的**hs-script-deck-sdk.jar** , **hs-script-card-sdk.jar** ,  **hs-script-plugin-sdk.jar** , **hs-script-base.jar**包引入项目
2. 参考 [Hearthstone-Script-Base-Card](Hearthstone-Script-Base-Card)  [Hearthstone-Script-Base-Deck](Hearthstone-Script-Base-Deck) 两个插件项目



### [项目文档](https://hearthstone-script-documentation.vercel.app/)



### [更新历史](HISTRORY.md)
