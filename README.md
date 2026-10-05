# TarkovSearch

Minecraft 物资箱搜索插件，采用类似《逃离塔科夫》的逐格搜索流程。物品池支持六种稀有度，玩家通过搜索动画发现并领取物品。

作者：**aojiangQAQ（鳌江）**，曙光团队。

## 环境与构建

- Minecraft / Paper API：`1.20.1`。
- Java 源码和字节码目标：17。
- 构建工具：Maven。

```powershell
git clone https://github.com/aojiangQAQ/TarkovSearch.git
cd TarkovSearch
mvn package
```

产物为 `target/TarkovSearch-1.0.jar`。本项目调用 Paper 的物品名称接口，应使用提供对应 API 的服务端。

## 安装

1. 将 JAR 放入服务端的 `plugins/` 目录并重启服务器。
2. 插件会创建 `plugins/TarkovSearch/config.yml` 和 `messages.yml`。
3. 创建物资箱并设置物品池；箱子位置、物品池和冷却截止时间保存在 `boxes.yml`。

## 命令与权限

`/tarkovsearch` 是 `/ts` 的别名。所有命令只能由玩家执行；创建、编辑和删除时需对准 6 格内的箱子。

| 命令 | 说明 | 描述符声明的权限节点 |
|---|---|---|
| `/ts create` | 将箱子设为物资箱并打开设置界面 | `tarkovsearch.admin.create` |
| `/ts edit` | 编辑已有物资箱 | `tarkovsearch.admin.edit` |
| `/ts delete` | 删除物资箱记录，恢复普通箱子 | `tarkovsearch.admin.delete` |
| `/ts reload` | 重载配置与语言文件 | `tarkovsearch.admin.reload` |

上述权限节点默认授予 OP，但当前命令执行器没有检查这些节点，普通玩家也能调用管理命令。公开服务器需通过外部命令管理限制访问。`tarkovsearch.admin.delete` 目前用于判断玩家是否可以直接破坏物资箱方块。

玩家右键物资箱即可搜索，不需要额外权限。

## 物品池与搜索

设置界面可使用 27 或 54 格，最后一格为确认按钮。左键放入或取出光标物品，右键循环稀有度，双击确认按钮保存。

稀有度依次为：普通、稀有、罕见、史诗、珍品、仙品。搜索界面固定为 54 格，按灰色玻璃板、望远镜、稀有度颜色玻璃板的顺序显示物品状态；已搜索完成的槽位可以领取。

各稀有度的配置权重会除以权重总和，物品池中的每个条目按自身稀有度对应概率独立抽取。物品池非空但未抽中任何物品时，随机补出一个条目。生成结果不写入 `boxes.yml`，重启后会重新抽取。

## 配置

| 配置项 | 默认值 | 用途 |
|---|---|---|
| `probability` | `50 / 25 / 12 / 8 / 4 / 1` | 六种稀有度的抽取权重 |
| `search.min_seconds` | `1` | 单格搜索动画的最短时长，秒 |
| `search.max_seconds` | `5` | 单格搜索动画的最长时长，秒 |
| `cooldown` | `600` | 全部物品领取后的冷却时长，秒 |
| `setting-gui-size` | `54` | 设置界面尺寸，支持 `27` 或 `54` |

权重应为非负数且总和大于零；搜索时长应满足 `0 <= min_seconds <= max_seconds`。`messages.yml` 保存提示语与界面文字；稀有度玻璃板颜色目前固定在 `ItemUtils` 中，`rarity-color` 配置暂未读取，`debug` 也暂未使用。

箱子保护监听器处理方块破坏、方块爆炸和活塞事件。当前冷却到期后不会清空已生成的物品缓存，因此会继续使用此前的生成结果，而不是重新抽取。

## 源码

- `command/`：命令执行与补全。
- `data/`：物资箱、物品池、稀有度和槽位状态。
- `gui/`、`listener/`：设置界面、搜索界面和事件处理。
- `manager/`：箱子存储、物品生成和搜索会话。
- `util/`：语言、物品、位置与调度工具。

这些目录位于 `src/main/java/com/shuguangteam/tarkovsearch/`；默认配置位于 `src/main/resources/`。

## 反馈与许可

问题和改进建议请提交到 [Issues](https://github.com/aojiangQAQ/TarkovSearch/issues)。

采用 [MIT License](LICENSE)。
