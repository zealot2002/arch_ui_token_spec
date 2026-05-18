# 从工程实践到架构设计：Style 层如何系统性解决代码冗余

> 本文是 UI 架构系列的第三篇，建议先阅读 [第一篇：三层颜色架构](/ui_architecture1)、[第二篇：Drawable 层设计](/ui_architecture2) 了解核心设计理念。

---

## 前言

在多年的 Android 开发实践中，我发现一个普遍存在的问题：**TextView 的属性定义存在大量重复**。

打开任何一个中等规模的 Android 项目，你会发现几乎每个布局文件中都有类似这样的代码：

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textColor="@color/black"
    android:textSize="15sp"
    android:includeFontPadding="false"
    android:text="商品名称" />
```

这些属性被重复定义了成千上万次。想象一下：如果你有 500 个 TextView，有一天设计师说所有文字的字号要统一从 15sp 改成 16sp，或者要关闭字体内边距，你需要打开 500 个文件逐个修改——这简直是噩梦。

我们分析了大量项目中的 TextView 使用情况，遵从 98% 的 TextView 的共性，提炼出这 5 个可以封装的基础属性。

**tv style 层的核心价值，就是通过工程经验的沉淀，将 TextView 这些重复的属性抽取到 style 里，写一次就能在所有 TextView 中复用。**

---

## 一、工程实践中的痛点：TextView 属性冗余

### 1.1 统计数据：一个中等项目的属性重复情况

根据我们对多个项目的分析，一个包含 100 个 Activity/Fragment 的项目：
- 平均每个布局文件有 5-10 个 TextView
- 每个 TextView 平均定义 3-5 个重复属性
- **总重复次数超过 2000 次**

### 1.2 典型的重复模式

```xml
<!-- 模式1：每个 TextView 都重复写宽高 -->
<TextView android:layout_width="wrap_content" />
<TextView android:layout_width="wrap_content" />
<TextView android:layout_width="wrap_content" />

<!-- 模式2：每个 TextView 都要设置 includeFontPadding -->
<TextView android:includeFontPadding="false" />
<TextView android:includeFontPadding="false" />
<TextView android:includeFontPadding="false" />

<!-- 模式3：颜色和字号的组合重复 -->
<TextView android:textColor="@color/black" android:textSize="15sp" />
<TextView android:textColor="@color/black" android:textSize="15sp" />
<TextView android:textColor="@color/black" android:textSize="15sp" />
```

### 1.3 维护成本分析

| 场景 | 无 Style | 有 Style | 节省比例 |
|------|---------|---------|---------|
| 修改默认宽高 | 需要修改所有文件 | 修改一处 | ~99% |
| 修改 includeFontPadding | 需要修改所有文件 | 修改一处 | ~99% |
| 修改字号 | 需要修改所有文件 | 修改一处 | ~99% |
| 修改颜色主题 | 需要修改所有文件 | 修改一处 | ~99% |

---

## 二、经验总结：TextView 必须的 3 个基础属性

经过多个大型项目的实践验证，我们总结出 **TextView 必须定义的 3 个基础属性**：

```xml
<style name="tv_base">
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:includeFontPadding">false</item>
</style>
```

### 2.1 为什么是这 3 个？

| 属性 | 必要性 | 工程经验 |
|------|---------|---------|
| `layout_width="wrap_content"` | **必须** | 95% 的 TextView 不需要撑满一行，需要时在布局中覆盖 |
| `layout_height="wrap_content"` | **必须** | 文字高度应由内容决定，避免固定高度导致截断 |
| `includeFontPadding="false"` | **必须** | Android 默认字体有额外内边距，导致垂直居中困难 |

### 2.2 为什么不再多定义一些？

```xml
<!-- ❌ 不推荐：在 base 中定义过多属性 -->
<style name="tv_base_bad">
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:includeFontPadding">false</item>
    <item name="android:textColor">@color/black</item>  <!-- 不应该在这里定义 -->
    <item name="android:textSize">15sp</item>           <!-- 不应该在这里定义 -->
</style>
```

**原因**：
- **正交性原则**：颜色和字号是正交的属性，应该分开定义
- **组合灵活性**：不同场景需要不同的颜色+字号组合
- **单一职责**：base 只负责"通用基础属性"，不负责"业务属性"

---

## 三、正交组合：颜色 × 字号 的高效复用

### 3.1 设计思路

```
基础层：tv_base（3个核心属性）
    ↓
色系层：tv_black_1 / tv_gray_2 / tv_orange_1（继承 base + 颜色）
    ↓
字号层：tv_black_1_size_15（继承色系 + 字号）
```

### 3.2 色系层定义

```xml
<!-- 黑色系（主文本） -->
<style name="tv_black_1" parent="tv_base">
    <item name="android:textColor">@color/func_black_text_1</item>
</style>
<style name="tv_black_2" parent="tv_base">
    <item name="android:textColor">@color/func_black_text_2</item>
</style>

<!-- 灰色系（辅助文本） -->
<style name="tv_gray_1" parent="tv_base">
    <item name="android:textColor">@color/func_gray_text_1</item>
</style>
<style name="tv_gray_2" parent="tv_base">
    <item name="android:textColor">@color/func_gray_text_2</item>
</style>

<!-- 橙色系（强调文本） -->
<style name="tv_orange_1" parent="tv_base">
    <item name="android:textColor">@color/func_orange_text_1</item>
</style>
```

### 3.3 字号层定义

```xml
<!-- 黑色主文本 + 各种字号 -->
<style name="tv_black_1_size_12" parent="tv_black_1">
    <item name="android:textSize">12sp</item>
</style>
<style name="tv_black_1_size_14" parent="tv_black_1">
    <item name="android:textSize">14sp</item>
</style>
<style name="tv_black_1_size_15" parent="tv_black_1">
    <item name="android:textSize">15sp</item>
</style>
<style name="tv_black_1_size_16" parent="tv_black_1">
    <item name="android:textSize">16sp</item>
</style>

<!-- 灰色辅助文本 + 常用字号 -->
<style name="tv_gray_2_size_12" parent="tv_gray_2">
    <item name="android:textSize">12sp</item>
</style>
<style name="tv_gray_2_size_14" parent="tv_gray_2">
    <item name="android:textSize">14sp</item>
</style>
```

**屏幕适配优势**：这种集中定义的方式还有一个重要好处——便于屏幕适配。如果有一天需要支持多尺寸屏幕，只需将硬编码的 `15sp` 修改为 `@dimen/size_15`，然后在不同的 dimens 文件中定义不同的值即可，整个改动只需要修改 tv style 这一个文件。

### 3.4 组合效果

| 颜色层 | 字号层 | 组合结果 | 用途 |
|------|-------|---------|------|
| `tv_black_1` | `size_15` | `tv_black_1_size_15` | 商品标题、主要内容 |
| `tv_black_2` | `size_13` | `tv_black_2_size_13` | 副标题、次要内容 |
| `tv_gray_2` | `size_12` | `tv_gray_2_size_12` | 提示文字、辅助说明 |
| `tv_orange_1` | `size_14` | `tv_orange_1_size_14` | 强调文字、按钮文字 |

---

## 四、按钮 Style：同样的思路，不同的属性

### 4.1 按钮必须的基础属性

```xml
<style name="BaseButton" parent="android:Widget.Button">
    <item name="android:textSize">15sp</item>
    <item name="android:textStyle">bold</item>
    <item name="android:letterSpacing">0.02</item>
    <item name="android:gravity">center</item>
    <item name="android:minHeight">48dp</item>
    <item name="android:paddingLeft">24dp</item>
    <item name="android:paddingRight">24dp</item>
</style>
```

### 4.2 按钮属性分析

| 属性 | 必要性 | 工程经验 |
|------|---------|---------|
| `textSize=15sp` | **必须** | 按钮文字需要足够大，保证可点击性 |
| `textStyle=bold` | **必须** | 按钮需要视觉强调，加粗效果更好 |
| `letterSpacing=0.02` | **必须** | 适当增加字间距提升可读性 |
| `minHeight=48dp` | **必须** | 符合 Material Design 规范，保证点击区域 |
| `paddingLeft/Right=24dp` | **必须** | 左右内边距保证文字不贴边 |

### 4.3 按钮的正交组合

```xml
<!-- 胶囊按钮 -->
<style name="Btn.Capsule.Primary" parent="BaseButton">
    <item name="android:background">@drawable/sel_orange_interact_capsule_emphasis_default</item>
    <item name="android:textColor">@color/func_white_text_1</item>
</style>

<style name="Btn.Capsule.Neutral" parent="BaseButton">
    <item name="android:background">@drawable/sel_gray_interact_capsule_neutral_default</item>
    <item name="android:textColor">@color/func_black_text_1</item>
</style>

<!-- 小尺寸按钮 -->
<style name="Btn.Capsule.Small" parent="BaseButton">
    <item name="android:minHeight">36dp</item>
    <item name="android:textSize">13sp</item>
    <item name="android:paddingLeft">16dp</item>
    <item name="android:paddingRight">16dp</item>
    <item name="android:background">@drawable/sel_orange_interact_capsule_emphasis_default</item>
    <item name="android:textColor">@color/func_white_text_1</item>
</style>

<!-- 描边按钮 -->
<style name="Btn.Outline.Primary" parent="BaseButton">
    <item name="android:background">@drawable/sel_orange_interact_outline_emphasis_default</item>
    <item name="android:textColor">@color/t_orange_4</item>
</style>
```

---

## 五、复用效果对比

### 5.1 改造前：每个 TextView 都要写完整属性

```xml
<!-- 改造前：每个都要写 4-5 行 -->
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:includeFontPadding="false"
    android:textColor="@color/func_black_text_1"
    android:textSize="15sp"
    android:text="商品名称" />

<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:includeFontPadding="false"
    android:textColor="@color/func_gray_text_2"
    android:textSize="12sp"
    android:text="库存紧张" />

<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:includeFontPadding="false"
    android:textColor="@color/func_red_text_1"
    android:textSize="14sp"
    android:text="¥299" />
```

### 5.2 改造后：一行解决

```xml
<!-- 改造后：只需引用 style -->
<TextView
    style="@style/tv_black_1_size_15"
    android:text="商品名称" />

<TextView
    style="@style/tv_gray_2_size_12"
    android:text="库存紧张" />

<TextView
    style="@style/tv_red_1_size_14"
    android:text="¥299" />
```

### 5.3 量化对比

| 指标 | 改造前 | 改造后 | 节省比例 |
|------|-------|-------|---------|
| 代码行数（3个TextView） | 15 行 | 6 行 | **60%** |
| 属性定义次数 | 12 次 | 0 次 | **100%** |
| 维护点 | 每个 TextView | 1 个 Style 文件 | **99%** |

---

## 六、命名规范：让复用更直观

### 6.1 文字 Style 命名

```
tv_{色系}_{档位}_size_{字号}
```

**示例**：
- `tv_black_1_size_15`：黑色主文本，15sp
- `tv_gray_2_size_12`：灰色辅助文本，12sp
- `tv_orange_1_size_14`：橙色强调文本，14sp

### 6.2 按钮 Style 命名

```
Btn.{形状}.{类型}.{尺寸}
```

**示例**：
- `Btn.Capsule.Primary`：胶囊主按钮
- `Btn.Capsule.Neutral`：胶囊次按钮
- `Btn.Outline.Primary`：描边主按钮
- `Btn.Capsule.Primary.Small`：小尺寸胶囊主按钮

### 6.3 命名原则

1. **语义化**：看到名字就知道用途
2. **层次清晰**：通过分隔符体现继承关系
3. **易于搜索**：统一前缀便于 IDE 搜索

---

## 七、与颜色体系的集成

### 7.1 完整数据流

```
tv_black_1_size_15
    │
    ├── 继承 tv_black_1
    │       └── textColor → @color/func_black_text_1
    │                           └── @color/t_black_1
    │                               └── @color/black_1
    │
    └── textSize → 15sp
```

### 7.2 主题切换支持

由于 Style 通过 `@color/func_*` 引用功能色，而功能色又通过 `@color/t_*` 引用主题色，所以：

```
日间模式：
tv_black_1_size_15 → func_black_text_1 → t_black_1 → black_1 (#333333)

夜间模式：
tv_black_1_size_15 → func_black_text_1 → t_black_1 → white_1 (#FFFFFF)
```

**无需修改任何 Style，自动适配主题**。

---

## 八、实际项目中的最佳实践

### 8.1 渐进式改造

```
阶段1：定义 tv_base 和常用色系样式（1-2天）
阶段2：定义常用字号组合（tv_black_1_size_12/14/15/16）（1天）
阶段3：逐步替换现有布局中的重复属性（持续进行）
阶段4：新增 TextView 时直接使用 Style（日常规范）
```

### 8.2 团队协作规范

```
1. 所有新增 TextView 必须使用 Style
2. 禁止在布局中直接定义 textColor、textSize、includeFontPadding
3. 需要新的颜色+字号组合时，在 styles_tv.xml 中添加
4. 定期审查，清理直接定义属性的代码
```

### 8.3 扩展原则

| 场景 | 做法 |
|------|------|
| 需要新字号 | 在对应色系下添加（如 `tv_black_1_size_17`） |
| 需要新色系 | 添加色系层（如 `tv_purple_1`），再添加字号组合 |
| 需要特殊效果 | 在布局中通过 `android:textStyle` 等属性覆盖 |

---

## 九、总结

Style 层不是简单的"属性集合"，而是**工程经验的沉淀和复用**。

### 核心价值

1. **消除冗余**：将重复属性抽取到 Style 中，写一次用无数次
2. **统一标准**：通过 tv_base 确保所有 TextView 有一致的基础行为
3. **降低维护成本**：修改一处，全局生效
4. **支持主题切换**：与颜色体系无缝集成

### 关键设计原则

1. **最小化基础层**：tv_base 只定义 3 个必须属性
2. **正交组合**：颜色和字号分开定义，灵活组合
3. **语义化命名**：让使用者一目了然

### 预期收益

根据我们的实践经验，引入 Style 层后：
- **代码量减少 30-50%**（布局文件）
- **维护时间减少 80%**（属性修改）
- **视觉一致性提升**（避免手动书写错误）

---

**参考代码（与本文配套的实现仓库一致）**：
- [文字样式定义](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/styles_tv.xml)
- [按钮样式定义](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/styles_button.xml)
- [颜色资源](https://github.com/zealot2002/arch_ui_token_spec/tree/main/app/src/main/res/values)

> 💡 如果你觉得这篇文章对你有帮助，欢迎点赞、收藏、转发。关注我，获取更多Android架构设计干货。
>
> **系列文章**：
> - [第一篇：三层颜色架构](/ui_architecture1)
> - [第二篇：7大色系×6种职能的通用职能层](/ui_architecture2)
> - [第三篇：Drawable 层设计](/ui_architecture2)
> - **第四篇：Style 层设计**（本文）