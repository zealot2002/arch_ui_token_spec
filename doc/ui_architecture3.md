# 从颜色到形态：Drawable 层的规范设计与最佳实践

> 本文是颜色架构系列的第三篇，建议先阅读 [第一篇：三层颜色架构](/ui_architecture1) 和 [第二篇：7大色系×6种职能](/ui_architecture2) 了解核心设计理念。

---

## 前言

在前两篇文章中，我们建立了一套完整的颜色体系。但颜色只是UI的基础，真正让UI活起来的是**形态和交互**。

一个按钮不仅需要颜色，还需要：
- 圆角、描边、阴影
- 按下、禁用、选中的状态变化
- 渐变、透明等视觉效果

这些都需要通过 Drawable 来实现。

今天这篇文章，我们将深入探讨 Drawable 层的设计规范，让你的UI组件不仅好看，而且**易于维护、高度复用**。

---

## 一、为什么需要 Drawable 层规范？

### ❌ 反例：混乱的 Drawable 管理

```xml
<!-- btn_primary.xml -->
<shape>
    <solid android:color="#FF4B00" />
    <corners android:radius="24dp" />
</shape>

<!-- btn_primary_pressed.xml -->
<shape>
    <solid android:color="#CC3D00" />
    <corners android:radius="24dp" />
</shape>

<!-- btn_disabled.xml -->
<shape>
    <solid android:color="#CCCCCC" />
    <corners android:radius="24dp" />
</shape>
```

**问题分析**：
1. **重复定义**：每个状态都要单独定义一个文件
2. **颜色硬编码**：直接写色值，无法跟随主题切换
3. **命名混乱**：`btn_primary`、`button_normal` 等命名不统一
4. **难以维护**：改一个圆角需要修改所有文件

---

## 二、Drawable 层的三层架构

借鉴颜色体系的设计思路，我们同样采用**三层架构**来管理 Drawable：

```
┌─────────────────────────────────────────────────────────────┐
│              组件层（Component）                            │
│   - 按钮、输入框、卡片等具体组件的完整样式                    │
│   - 通过 selector 组合基础形状和颜色                         │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              形状层（Shape）                                │
│   - 纯形状定义：圆角、描边、渐变、阴影                        │
│   - 不包含具体颜色，只定义形态                              │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              颜色层（Color）                                │
│   - 通过 func_* 引用功能色层                               │
│   - 支持日间/夜间模式自动切换                               │
└─────────────────────────────────────────────────────────────┘
```

### 核心原则

| 层级 | 职责 | 特点 |
|------|------|------|
| **组件层** | 组合状态和形态 | 通过 selector 实现状态切换 |
| **形状层** | 定义视觉形态 | 纯形状，不包含颜色 |
| **颜色层** | 提供颜色原料 | 通过功能色引用，支持主题切换 |

---

## 三、命名规范：让每个文件都有明确的含义

### 命名公式

```
{类型}_{色系}_{用途}_{状态}_{档位}
```

### 组成说明

| 组成部分 | 说明 | 允许值 |
|---------|------|--------|
| **类型** | Drawable 类别 | `bg`（背景）、`sel`（选择器）、`ic`（图标） |
| **色系** | 颜色分类 | `gray`、`orange`、`red`、`blue`、`green`、`black`、`white` |
| **用途** | 使用场景 | `surface`、`fill`、`stroke`、`field`、`interact`、`gradient` |
| **状态** | 交互状态 | `idle`、`alert`、`hint`、`emphasis`、`neutral`、`default` |
| **档位** | 优先级 | 数字 `1`（主要）、`2`（次要）、`3`（第三） |

### 命名示例

```
bg_orange_fill_interact_1    // 橙色交互填充背景，主档位
bg_gray_surface_card_1       // 灰色卡片表面背景
sel_orange_interact_outline_emphasis_default  // 橙色描边交互选择器
bg_black_fill_scrim_1        // 黑色遮罩填充
```

---

## 四、形状层设计：纯形态定义

### 4.1 基础形状类型

| 形状 | 用途 | 示例 |
|------|------|------|
| **fill** | 实心填充 | 按钮背景、卡片背景 |
| **stroke** | 描边空心 | 输入框边框、幽灵按钮 |
| **flat** | 扁平透明 | 文字按钮、点击区域 |
| **gradient** | 渐变效果 | 渐变背景、光晕效果 |
| **surface** | 表面效果 | 卡片、浮层、阴影 |

### 4.2 形状定义规范

```xml
<!-- bg_orange_fill_interact_1.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/func_orange_bg_1" />
    <corners android:radius="@dimen/draw_corner_capsule" />
</shape>

<!-- bg_orange_stroke_interact_1.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/func_clear_1" />  <!-- 透明填充 -->
    <stroke 
        android:width="@dimen/draw_stroke_reg" 
        android:color="@color/func_orange_border_1" />
    <corners android:radius="@dimen/draw_corner_capsule" />
</shape>
```

**关键设计点**：
- 使用 `@dimen` 引用尺寸，避免硬编码
- 使用 `@color/func_*` 引用功能色，支持主题切换
- 形状与颜色分离，同一个形状可以搭配不同颜色

---

## 五、组件层设计：状态组合

### 5.1 Selector 的规范写法

```xml
<!-- sel_orange_interact_capsule_emphasis_default.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 禁用态 -->
    <item 
        android:drawable="@drawable/bg_gray_fill_interact_2" 
        android:state_enabled="false" />
    <!-- 按下态 -->
    <item 
        android:drawable="@drawable/bg_orange_fill_interact_2" 
        android:state_pressed="true" />
    <!-- 常态 -->
    <item android:drawable="@drawable/bg_orange_fill_interact_1" />
</selector>
```

### 5.2 状态优先级

```
禁用态 > 选中态 > 按下态 > 焦点态 > 常态
```

### 5.3 输入框状态示例

```xml
<!-- sel_gray_field_rect_idle_default.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item 
        android:drawable="@drawable/bg_gray_field_rect_2" 
        android:state_enabled="false" />
    <item 
        android:drawable="@drawable/bg_blue_field_rect_1" 
        android:state_focused="true" />
    <item android:drawable="@drawable/bg_gray_field_rect_1" />
</selector>
```

---

## 六、常用组件的 Drawable 实现

### 6.1 按钮组件

| 按钮类型 | Drawable 配置 |
|---------|--------------|
| **主按钮** | `sel_orange_interact_capsule_emphasis_default` |
| **次按钮** | `sel_gray_interact_capsule_neutral_default` |
| **文字按钮** | `sel_gray_interact_flat_neutral_default` |
| **幽灵按钮** | `sel_orange_interact_outline_emphasis_default` |

### 6.2 输入框组件

| 状态 | Drawable 配置 |
|------|--------------|
| **常态** | `sel_gray_field_rect_idle_default` |
| **警告态** | `sel_yellow_field_rect_hint_static` |
| **错误态** | `sel_red_field_rect_alert_static` |

### 6.3 卡片组件

| 卡片类型 | Drawable 配置 |
|---------|--------------|
| **普通卡片** | `bg_gray_surface_card_1` |
| **浮层卡片** | `bg_gray_surface_float_1` |
| **面板卡片** | `bg_gray_surface_panel_1` |

---

## 七、尺寸管理：统一的 Dimen 规范

### 7.1 圆角尺寸

```xml
<!-- dimens_drawable.xml -->
<dimen name="draw_corner_none">0dp</dimen>
<dimen name="draw_corner_sm">4dp</dimen>
<dimen name="draw_corner_reg">8dp</dimen>
<dimen name="draw_corner_lg">12dp</dimen>
<dimen name="draw_corner_capsule">999dp</dimen>  <!-- 胶囊形 -->
```

### 7.2 描边尺寸

```xml
<dimen name="draw_stroke_thin">0.5dp</dimen>
<dimen name="draw_stroke_reg">1dp</dimen>
<dimen name="draw_stroke_thick">2dp</dimen>
```

### 7.3 阴影尺寸

```xml
<dimen name="draw_shadow_elevation_1">2dp</dimen>
<dimen name="draw_shadow_elevation_2">4dp</dimen>
<dimen name="draw_shadow_elevation_3">8dp</dimen>
```

---

## 八、与颜色体系的集成

### 8.1 颜色引用规范

```xml
<!-- ✅ 正确：引用功能色层 -->
<solid android:color="@color/func_orange_bg_1" />
<stroke android:color="@color/func_orange_border_1" />

<!-- ❌ 错误：直接引用主题色或基础色 -->
<solid android:color="@color/t_orange_4" />  <!-- 不推荐 -->
<solid android:color="@color/orange_4" />    <!-- 禁止 -->
```

### 8.2 夜间模式自动适配

得益于三层颜色架构，Drawable 层**无需任何修改**即可支持夜间模式：

```
日间模式：
func_orange_bg_1 → t_orange_4 → orange_4 (#FF4B00)

夜间模式：
func_orange_bg_1 → t_orange_4 → orange_3b (#FF8833)
```

---

## 九、性能优化：Drawable 的最佳实践

### 9.1 避免过度绘制

```xml
<!-- ❌ 不好：多层叠加导致过度绘制 -->
<layer-list>
    <item>
        <shape>
            <solid android:color="@color/func_orange_bg_1" />
        </shape>
    </item>
    <item android:top="2dp">
        <shape>
            <solid android:color="@color/func_white_bg_1" />
        </shape>
    </item>
</layer-list>

<!-- ✅ 好：使用 padding 替代叠加 -->
<shape>
    <solid android:color="@color/func_orange_bg_1" />
    <padding android:top="2dp" />
</shape>
```

### 9.2 使用 Vector Drawable

对于图标，优先使用 Vector Drawable：

```xml
<!-- ic_theme_day.xml -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/func_orange_text_1"
        android:pathData="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 15l-5-5 1.41-1.41L11 14.17l7.59-7.59L20 8l-9 9z"/>
</vector>
```

### 9.3 复用 Drawable 资源

```xml
<!-- ✅ 好：复用基础形状 -->
<selector>
    <item android:state_pressed="true" 
        android:drawable="@drawable/bg_orange_fill_interact_2" />
    <item android:drawable="@drawable/bg_orange_fill_interact_1" />
</selector>
```

---

## 十、与 View 的集成方式

### 10.1 通过 Style 引用

```xml
<!-- styles_button.xml -->
<style name="Btn.Capsule.Primary" parent="BaseButton">
    <item name="android:background">@drawable/sel_orange_interact_capsule_emphasis_default</item>
    <item name="android:textColor">@color/func_white_text_1</item>
</style>
```

### 10.2 在布局中直接使用

```xml
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/sel_orange_interact_capsule_emphasis_default"
    android:text="主操作" />
```

---

## 十一、完整架构总结

```
┌─────────────────────────────────────────────────────────────┐
│                    View 层（布局文件）                      │
│   - 通过 style 或直接引用 Drawable                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                  Drawable 层（形态与状态）                   │
│   - sel_*：状态选择器（按钮、输入框等）                       │
│   - bg_*：静态背景（卡片、填充等）                           │
│   - ic_*：图标资源                                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              func_ 功能色层（通用职能）                       │
│   - 7大色系 × 6种职能 × 2-4档                              │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│               t_ 主题层（日夜切换）                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                 基础色层（纯色值）                            │
└─────────────────────────────────────────────────────────────┘
```

---

## 十二、系列文章回顾

### 架构全景图

```
┌─────────────────────────────────────────────────────────────┐
│  第一层：Drawable 层                                         │
│  - 形态定义、状态组合、组件样式                               │
│  - 本章内容                                                 │
├─────────────────────────────────────────────────────────────┤
│  第二层：功能色层（func_*）                                   │
│  - 7大色系 × 6种职能 × 2-4档                                │
│  - 第二篇内容                                               │
├─────────────────────────────────────────────────────────────┤
│  第三层：主题层（t_*）                                        │
│  - 日间/夜间模式颜色映射                                     │
│  - 第一篇内容                                               │
├─────────────────────────────────────────────────────────────┤
│  第四层：基础色层                                            │
│  - 纯原始色值定义                                           │
│  - 第一篇内容                                               │
└─────────────────────────────────────────────────────────────┘
```

### 核心价值

| 层级 | 价值 |
|------|------|
| **Drawable** | 统一形态和状态管理 |
| **func_*** | 统一业务职能的颜色封装 |
| **t_*** | 一键切换主题模式 |
| **基础色** | 稳定的颜色原料供应 |

---

## 十三、总结

Drawable 层是连接颜色和UI组件的桥梁，它的设计直接影响到：
- UI 的一致性和美观度
- 代码的可维护性
- 主题切换的灵活性

通过遵循以下原则，你可以构建一套优秀的 Drawable 体系：

1. **分离关注点**：形状与颜色分离，状态与组件分离
2. **统一命名规范**：`{类型}_{色系}_{用途}_{状态}_{档位}`
3. **复用优先**：避免重复定义，提高资源利用率
4. **性能优化**：减少过度绘制，使用 Vector Drawable
5. **与颜色体系集成**：通过 func_* 引用，支持主题切换

这套架构已经在多个大型项目中验证有效，希望能对你有所帮助。

---

**参考代码（与本文配套的实现仓库一致）**：
- [Drawable 资源目录](https://github.com/zealot2002/arch_ui_token_spec/tree/main/app/src/main/res/drawable)
- [样式定义](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/styles_button.xml)
- [尺寸定义](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/dimens_drawable.xml)

> 💡 如果你觉得这篇文章对你有帮助，欢迎点赞、收藏、转发。关注我，获取更多Android架构设计干货。
>
> **系列文章**：
> - [第一篇：三层颜色架构](/ui_architecture1)
> - [第二篇：7大色系×6种职能的通用职能层](/ui_architecture2)
> - **第三篇：Drawable 层设计**（本文）