# TarkovSearch (搜索) Plugin

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Mohist](https://img.shields.io/badge/Mohist-Compatible-blue)
![License](https://img.shields.io/badge/license-MIT-blue)

> **TarkovSearch**（游戏内中文名 **“搜索”**）是一个为 **Minecraft&nbsp;1.20.1**（Spigot / Paper API，兼容 Mohist）开发的插件，模拟《逃离塔科夫》的物资搜索体验——管理员配置可搜索的物资箱，玩家通过动画化搜索流程随机获取战利品。

---

## ✨ 功能亮点
- **GUI 交互**  
  - 管理员：54 格 Chest GUI 拖拽配置物品，右键循环稀有度，双击确认保存  
  - 玩家：灰板 ➜ 望远镜 ➜ 彩板逐格搜索动画（1–5 s 随机）
- **稀有度与概率**  
  - 普通 / 稀有 / 罕见 / 史诗 / 珍品 / 仙品  
  - `config.yml` 自定义掉落概率，插件自动归一化
- **冷却&保护**  
  - 箱子被取空后进入冷却（默认 10 min，可配置）  
  - 防爆炸、防破坏、防活塞，只有管理员可删除
- **反馈效果**  
  - 领取物品时私聊提示 + 经验音效  
  - 聊天提示显示物品自定义名称 / 翻译名称，无 `item.minecraft.*`

---

## 📂 目录结构
```
TarkovSearch/
├── pom.xml
├── README.md
├── LICENSE
├── .gitignore
└── src/
└── main/
├── java/
│   └── com/shuguangteam/tarkovsearch/...
└── resources/
├── plugin.yml
├── config.yml
└── messages.yml
```

---

## 🛠 本地构建
确保使用 **JDK 17**：

```bash
# 克隆仓库
git clone https://github.com/YourUserName/TarkovSearch.git
cd TarkovSearch

# Maven 打包
mvn clean package
# 生成 target/TarkovSearch-1.0.jar
```

---

## 🚀 安装与配置
1. 将 `TarkovSearch-1.0.jar` 复制到服务器 `plugins/` 目录
2. 启动或重载服务器，插件会自动生成 `plugins/TarkovSearch/` 配置文件夹
3. 按需修改 `config.yml`（概率、冷却时间等）及 `messages.yml`（多语言）

---

## 📝 指令 & 权限

| 指令 | 描述 | 默认权限 |
|------|------|----------|
| `/ts create` | 将准星所指箱子设为物资箱并打开设置 GUI | `tarkovsearch.admin.create` (OP) |
| `/ts edit`   | 编辑已创建的物资箱 | `tarkovsearch.admin.edit` (OP) |
| `/ts delete` | 删除物资箱（恢复普通箱子）| `tarkovsearch.admin.delete` (OP) |
| `/ts reload` | 重载 `config.yml` & `messages.yml` | `tarkovsearch.admin.reload` (OP) |

玩家无须任何权限即可搜索。

---

## 🔧 开发环境
- **Java:** 17
- **Build:** Maven 3.9+
- **API:** Paper-API 1.20.1 (与 Spigot API 兼容)
- **服务器测试:** Mohist-1.20.1-47.x

---

## 🤝 贡献
欢迎 Issue / PR！

1. Fork 本仓库
2. 创建新分支: `git checkout -b feature/awesome`
3. 提交更改: `git commit -m "Add awesome feature"`
4. 推送分支: `git push origin feature/awesome`
5. 发起 Pull Request

---

## ⚖️ License
TarkovSearch 使用 **MIT License**，详见 [LICENSE](LICENSE)。
```
