# 我重构了电商App的颜色体系，解决了90%的夜间模式和维护难题

> 本文首发于个人技术博客，转载请注明出处。
>
> 作为一名Android架构师，我见过太多项目因为颜色管理混乱而陷入维护地狱。今天我将分享一套经过线上验证的三层颜色架构，彻底解决命名混乱、重复定义、夜间模式适配难等行业通病。

---

## 前言

你是否也遇到过这些问题：
- 同一个色值在项目中被定义了几十次
- 改一个品牌色要全局搜索替换几百个地方
- 夜间模式适配时要修改所有颜色文件
- 新同事加入要花一周时间才能搞懂各种奇怪的颜色命名

如果你也被这些问题困扰，那么这篇文章就是为你准备的。

经过多个电商项目的踩坑和迭代，我总结出了一套**可扩展、易维护、支持一键切换日夜模式**的三层颜色架构。这套架构已经在日活百万级的电商App中稳定运行了两年，帮助团队将颜色维护成本降低了90%以上。

---

## 一、传统颜色管理的四大痛点

在介绍新架构之前，我们先看看传统颜色管理方式到底有多糟糕。

### ❌ 反例：真实项目中的颜色定义

```xml
<!-- 这是我从一个真实项目中截取的代码片段 -->
<color name="bg_login">#FFFFFF</color>      <!-- 登录页背景 -->
<color name="bg_home">#F5F5F5</color>      <!-- 首页背景 -->
<color name="bg_profile">#FFFFFF</color>    <!-- 个人中心背景 -->
<color name="text_title">#141414</color>   <!-- 标题文字 -->
<color name="text_desc">#808080</color>    <!-- 描述文字 -->
<color name="text_hint">#BFBFBF</color>    <!-- 提示文字 -->
<color name="button_red">#DB121F</color>   <!-- 红色按钮 -->
<color name="red_button">#DB121F</color>   <!-- 另一个红色按钮 -->
```

### 问题分析

1. **重复定义**：`#FFFFFF` 和 `#DB121F` 都出现了多次，改一处要改所有地方
2. **业务绑定**：`bg_login`、`bg_home` 这种命名把颜色和具体页面绑定，无法复用
3. **命名混乱**：`button_red` 和 `red_button` 同时存在，新人根本不知道该用哪个
4. **夜间模式灾难**：要适配夜间模式，需要把上面所有颜色都复制一份到 `values-night` 目录下，工作量巨大

---

## 二、三层颜色架构设计理念

针对这些问题，我设计了一套**严格单向依赖**的三层颜色架构：

```
┌─────────────────────────────────────┐
│      业务功能层 (func_*)            │
│  ┌─────────────────────────────┐    │
│  │    主题适配层 (t_*)         │    │
│  │  ┌─────────────────────┐    │    │
│  │  │    全局基础色层      │    │    │
│  │  │  (色系_数字格式)     │    │    │
│  │  └─────────────────────┘    │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

### ✅ 架构核心原则（大厂标准）

| 层级 | 唯一职责 | 命名规则 | 是否承载业务语义 | 修改频率 |
|------|----------|----------|----------------|----------|
| **基础色层** | 定义原始色值 | `色系_数字`（如 `white_1`） | ❌ 否 | 极低（品牌升级时才改） |
| **主题层** | 日间/夜间模式映射 | `t_色系_数字`（如 `t_white_1`） | ❌ 否 | 低（新增主题时才改） |
| **功能层** | 业务场景封装 | `func_分类_数字`（如 `func_text_1`） | ✅ 是 | 中（新增业务场景时改） |

### 🔄 严格单向依赖规则

```
基础色层 → 主题层 → 功能层
    ↑         ↑         ↑
   改色      换肤     改用途
```

**为什么必须严格遵守单向依赖？**
- 避免循环依赖导致的颜色定义混乱
- 每层只做自己的事，职责清晰
- 改一处全局生效，维护成本极低
- 多人开发不会互相冲突

---

## 三、分层详解：每一层该怎么设计

### 第一层：全局基础色层

**定位**：纯原始色值定义，不做任何主题绑定和业务关联

**命名规则**：`色系_数字` 下划线格式，数字按明度/饱和度递增

**设计原则**：
- 白色系：数字越小越浅
- 黑色系：数字越小越浅
- 彩色系：数字越小饱和度越低
- 不参与日夜切换，保持绝对稳定

**完整基础色表（与仓库实现一致，随项目迭代以 `values/colors.xml` 为准）**：

| 色系 | 档位数 | 命名区间 / 说明 |
|------|--------|----------------|
| 白色系 | 4 | `white_1` ~ `white_4` |
| 黑色系 | 8 | `black_1` ~ `black_8` |
| 灰色系 | 7 | `gray_1` ~ `gray_7` |
| 红色系 | 4 | `red_1` ~ `red_4` |
| 橙色系 | 4 | `orange_1` ~ `orange_4`；并含供多主题映射用的 `orange_0`、`orange_2b`、`orange_3b` |
| 黄色系 | 4 | `yellow_1` ~ `yellow_4` |
| 蓝色系 | 4 | `blue_1` ~ `blue_4` |
| 绿色系 | 4 | `green_1` ~ `green_4` |
| 补间色 | 按需 | 如 `black_5a`（介于 `black_5` 与 `black_6` 之间的深灰阶，供暗色背景映射） |
| 透明度变体 | 按需 | `基础色名_alpha{透明度}`，如 `white_1_alpha20`、`black_8_alpha80`、`red_4_alpha40`；彩色半透明亦在基础层定义 |

**透明度命名规则**：`基础色名_alpha透明度`，例如 `white_1_alpha50` 代表 `white_1` 加上约 50% 透明度（以具体 ARGB 为准）。

色系需要扩展时，只在基础层增加新档位；主题层、功能层继续遵守单向依赖。

### ❌ 反例 vs ✅ 正例

```xml
<!-- ❌ 错误：语义不清，无法扩展 -->
<color name="background_white">#FFFFFF</color>
<color name="text_black">#000000</color>
<color name="primary_red">#DB121F</color>
<color name="light_gray">#F0F0F0</color>

<!-- ✅ 正确：统一色系_数字命名（档位数以项目仓为准，此处与参考仓库一致） -->
<color name="white_1">#FFFFFF</color>
<color name="black_8">#000000</color>
<color name="red_4">#DB121F</color>
<color name="black_1">#F0F0F0</color>
```

---

### 第二层：主题适配层（t_）

**定位**：唯一的日夜模式映射层，只做色值代理，不承载任何业务语义

**命名规则**：`t_基础色名`，与基础色一一对应

**设计原则**：
- 只引用基础色层变量，绝对不写具体色值
- 日间/夜间模式通过Android资源限定符自动切换
- 保持与基础色完全一致的命名，便于维护

**这是整个架构最核心、最容易被误解的一层**。很多人会在这里犯错误，把业务语义加到主题层里。

### ❌ 错误做法（90%的人都会踩的坑）

```xml
<!-- values/colors.xml -->
<color name="t_bg_page">#FFFFFF</color>
<color name="t_text_main">#000000</color>

<!-- values-night/colors.xml -->
<color name="t_bg_page">#141414</color>
<color name="t_text_main">#FFFFFF</color>
```

**问题**：主题层承载了业务语义，破坏了分层原则。如果以后要加一个深色主题，你需要把所有 `t_bg_page`、`t_text_main` 都复制一遍，工作量巨大。

### ✅ 正确做法

```xml
<!-- values/colors.xml（日间模式） -->
<color name="t_white_1">@color/white_1</color>
<color name="t_black_8">@color/black_8</color>

<!-- values-night/colors.xml（夜间模式）：同样只引用基础层，不写 #RRGGBB -->
<color name="t_white_1">@color/black_8</color>   <!-- 背景色反转 -->
<color name="t_black_8">@color/white_1</color>   <!-- 主文字色反转 -->
```

**优势**：
- 主题层只做颜色映射，不关心用途
- 新增主题只需要加一个资源目录，不用改任何其他代码
- 夜间模式切换完全由Android系统自动处理，零代码侵入

---

### 第三层：业务功能层（func_）

**定位**：唯一承载业务语义的一层，面向具体业务场景

**命名规则**：`func_分类_数字`，数字按重要性/深浅递增

**设计原则**：
- 只引用主题层变量，绝对不直接引用基础色
- 采用「分类+数字层级」模式，保留无限扩展能力
- 覆盖所有常用业务场景

### 为什么我反对固定语义命名？

很多人喜欢这样定义功能色：
```xml
<color name="func_text_main">@color/t_black_8</color>
<color name="func_text_sub">@color/t_black_5</color>
<color name="func_text_aux">@color/t_black_3</color>
```

**这看起来很美好，但在实际项目中一定会出问题**。

业务永远会出现**中间态**：来了一个比次级浅一点、比辅助深一点的文字色，你该怎么命名？
- `func_text_mid`？
- `func_text_light_sub`？
- `func_text_sub_sub`？

很快你的命名就会失控，变成一场灾难。

### ✅ 最佳实践：分类+数字层级

```xml
<!-- 文本色：数字越小越重要、越深 -->
<color name="func_text_1">@color/t_black_8</color>  <!-- 主标题、商品名称 -->
<color name="func_text_2">@color/t_black_7</color>   <!-- 副标题、描述 -->
<color name="func_text_3">@color/t_black_5</color>   <!-- 辅助信息 -->
<color name="func_text_4">@color/t_black_3</color>   <!-- 提示、备注 -->

<!-- 背景色：数字越小越浅 -->
<color name="func_bg_1">@color/t_white_1</color>     <!-- 页面背景 -->
<color name="func_bg_2">@color/t_white_2</color>     <!-- 卡片背景 -->
<color name="func_bg_3">@color/t_white_3</color>     <!-- 浅灰背景 -->

<!-- 边框分割线 -->
<color name="func_border_1">@color/t_black_2</color> <!-- 常规边框 -->
<color name="func_border_2">@color/t_black_1</color> <!-- 浅边框 -->
```

**无限扩展能力**：
- 以后需要在 `func_text_2` 和 `func_text_3` 之间加一个中间色，直接加 `func_text_25` 即可
- 不用造任何奇怪的新名词
- 规范永远不会崩坏

### 完整功能色分类

| 分类 | 命名格式 | 用途 |
|------|----------|------|
| 文本色 | func_text_1~5 | 各种层级的文字 |
| 背景色 | func_bg_1~4 | 页面、卡片、弹窗背景 |
| 边框分割 | func_border_1~3 | 边框、分割线 |
| 品牌色 | func_brand_1~3 | 主按钮、强调色 |
| 状态色 | func_state_1~4 | 成功、错误、警告、信息 |
| 交互色 | func_interact_1~2 | 链接、可点击文本 |

---

## 四、两个关键争议点的深度解析

### 争议1：主题层到底该不该承载业务语义？

**我的答案：绝对不应该**。

主题层的本质是**颜色代理**，它的唯一职责是根据当前主题返回对应的基础色。它不应该关心这个颜色是用来做背景还是文字。

如果主题层承载了业务语义，那么：
- 每新增一个主题，你都要复制所有业务语义的颜色
- 业务语义变化时，你需要修改所有主题文件
- 架构的解耦性被完全破坏

### 争议2：功能层用数字命名会不会可读性差？

**不会，只要约定好规则**。

我们只需要在团队内部约定：
- 文本色：数字越小越重要、越深
- 背景色：数字越小越浅
- 边框色：数字越小越深

新人只需要花5分钟就能记住这些规则，比记住几十个不同的语义名称要容易得多。

而且，数字命名带来的**无限扩展能力**，是固定语义命名永远无法比拟的。

---

## 五、核心优势：为什么这套架构能解决所有问题？

### 1. 维护成本降低90%

**改色流程对比**：
| 操作 | 传统做法 | 三层架构 |
|------|----------|----------|
| 改品牌色 | 全局搜索替换几百个地方 | 修改基础色层一处 |
| 适配夜间模式 | 修改所有颜色定义 | 自动切换，零代码 |
| 新增页面 | 定义5-10个新颜色 | 复用已有功能色 |

### 2. 无限扩展能力

- 新增色系：只需要在基础色层添加
- 新增主题：只需要加一个资源目录
- 新增业务场景：只需要在功能层顺延数字编号

### 3. 团队协作零成本

- 统一的命名规范，没有歧义
- 新成员5分钟就能上手
- 多人开发不会互相冲突

### 4. 完全符合Android官方规范

- 使用Android原生资源限定符实现夜间模式
- 没有引入任何第三方库
- 性能零损耗

---

## 六、实际应用示例

### 场景1：主按钮样式

```xml
<!-- res/drawable/btn_primary.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/func_brand_1" />
    <corners android:radius="24dp" />
</shape>
```

### 场景2：文本样式

```xml
<!-- res/styles.xml -->
<style name="Text.Title">
    <item name="android:textColor">@color/func_text_1</item>
    <item name="android:textSize">18sp</item>
    <item name="android:textStyle">bold</item>
</style>

<style name="Text.Body">
    <item name="android:textColor">@color/func_text_2</item>
    <item name="android:textSize">14sp</item>
</style>
```

### 场景3：状态提示

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="支付成功"
    android:textColor="@color/func_green_text_1" />
```

---

## 七、适用场景与扩展

### 适用场景
- 电商App、社交App、工具类App等中大型项目
- 需要支持日间/夜间模式的项目
- 多人协作、长期迭代的项目
- 对代码质量和可维护性有要求的团队

### 不适用场景
- 只有几个页面的小型Demo
- 不需要夜间模式的简单项目

### 扩展方向
- 支持多主题切换（浅色、深色、护眼模式等）
- 支持动态换肤
- 与Design Token系统集成
- 自动生成颜色文档

---

## 八、总结

这套三层颜色架构的核心思想其实很简单：**解耦**。

- 把色值定义和主题适配解耦
- 把主题适配和业务语义解耦
- 每一层只做自己最擅长的事

通过这种方式，我们彻底解决了颜色管理的所有痛点。无论是改色、适配夜间模式还是新增业务场景，都变得异常简单。

我见过太多团队在颜色管理上浪费了大量的时间和精力。其实只要稍微花一点时间设计一个好的架构，就能一劳永逸地解决这些问题。

希望这篇文章能对你有所帮助。如果你在颜色管理中遇到过什么问题，或者有更好的解决方案，欢迎在评论区交流。

---

**参考代码（与本文配套的实现仓库一致）**：
- [基础色 + 日间 `t_*` 主题层 + `func_*` 通用职能层：`values/colors.xml`](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/colors.xml)
- [夜间主题层覆写（仅 `t_*`）：`values-night/colors.xml`](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values-night/colors.xml)

> 💡 如果你觉得这篇文章对你有帮助，欢迎点赞、收藏、转发。关注我，获取更多Android架构设计干货。