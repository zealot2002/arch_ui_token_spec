# Drawable 架构规范

## 概述

Drawable 是 Android 资源系统的核心组件，负责定义界面元素的视觉表现。本规范定义了 Drawable 的命名规则、结构组织和使用模式，确保项目中 Drawable 资源的一致性和可维护性。

---

## 一、命名规范

### 1.1 命名格式

```
{类型}_{色系}_{用途}_{状态}_{档位}
```

### 1.2 组成说明

| 组成部分 | 说明 | 允许值 |
|---------|------|--------|
| **类型** | Drawable 类别标识 | `bg`（背景）、`sel`（选择器）、`ic`（图标） |
| **色系** | 颜色分类 | `gray`、`orange`、`red`、`blue`、`green`、`black`、`white`、`yellow` |
| **用途** | 使用场景描述 | `surface`、`fill`、`stroke`、`field`、`interact`、`gradient`、`scrim`、`panel`、`card`、`float`、`lift`、`page`、`block` |
| **状态** | 交互状态标识 | `idle`、`alert`、`hint`、`emphasis`、`neutral`、`default` |
| **档位** | 优先级/层级 | 数字 `1`（主要）、`2`（次要）、`3`（第三）、`0`（默认/透明） |

### 1.3 命名示例

```
bg_gray_surface_card_1      // 灰色卡片表面背景，主档位
bg_orange_fill_interact_1   // 橙色交互填充背景，主档位
sel_orange_interact_capsule_emphasis_default  // 强调型胶囊选择器
bg_gray_stroke_interact_3   // 灰色描边交互，第三档位
bg_black_fill_scrim_1       // 黑色遮罩填充
```

### 1.4 禁止规则

**严禁使用业务场景命名**，Drawable 资源必须保持通用和可复用，不得与具体业务模块耦合。

| ❌ 错误命名 | ✅ 正确命名 | 说明 |
|-----------|-----------|------|
| `bg_home_header_gradient` | `bg_gray_gradient_panel_1` | 移除业务标识 `home` |
| `bg_login_button_orange` | `bg_orange_fill_interact_1` | 移除业务标识 `login` |
| `bg_order_list_item` | `bg_gray_surface_card_1` | 移除业务标识 `order` |
| `sel_product_card_pressed` | `sel_gray_interact_capsule_neutral_default` | 移除业务标识 `product` |

---

## 二、类型分类

### 2.1 类型前缀定义

| 前缀 | 含义 | 适用场景 | 示例 |
|------|------|---------|------|
| **bg_** | 静态背景 | 纯色、渐变、描边等静态图形 | `bg_gray_surface_card_1` |
| **sel_** | 状态选择器 | 根据状态切换不同背景 | `sel_orange_interact_capsule_emphasis_default` |
| **ic_** | 图标资源 | 矢量图标或位图 | `ic_theme_day` |

---

## 三、用途分类

### 3.1 用途标识说明

| 用途 | 说明 | 示例 |
|------|------|------|
| **surface_** | 表面层 | 页面背景、卡片背景、浮动层 | `bg_gray_surface_page_1` |
| **fill_** | 填充层 | 按钮、可点击区域的背景 | `bg_orange_fill_interact_1` |
| **stroke_** | 描边层 | 边框、轮廓线 | `bg_gray_stroke_interact_3` |
| **field_** | 输入框 | 文本输入框背景 | `bg_gray_field_rect_1` |
| **gradient_** | 渐变背景 | 渐变色背景 | `bg_gray_gradient_panel_1` |
| **scrim_** | 遮罩层 | 半透明遮罩 | `bg_black_fill_scrim_1` |

---

## 四、颜色引用规范

### 4.1 依赖层次

Drawable 资源必须遵循以下单向依赖原则：

```
Drawable → 功能色层 (func_*) → 主题层 (t_*) → 基础色层
```

### 4.2 正确示例

```xml
<!-- ✅ 正确：引用功能色层 -->
<solid android:color="@color/func_gray_bg_1" />
<stroke android:color="@color/func_gray_border_2" />
<gradient android:startColor="@color/func_gray_bg_1" />

<!-- ❌ 错误：直接引用主题层或基础层 -->
<solid android:color="@color/t_black_2" />
<solid android:color="@color/black_2" />
```

### 4.3 透明色处理

透明背景应使用预定义的透明色：

```xml
<!-- 使用预定义透明色 -->
<solid android:color="@color/func_clear_1" />
```

---

## 五、Selector 状态规范

### 5.1 状态定义

| 状态 | 属性 | 说明 |
|------|------|------|
| 禁用 | `android:state_enabled="false"` | 控件不可用 |
| 按下 | `android:state_pressed="true"` | 用户触摸按下 |
| 焦点 | `android:state_focused="true"` | 获取焦点 |
| 默认 | 无状态属性 | 默认显示 |

### 5.2 状态顺序

Selector 中状态项必须按照**优先级从高到低**排列：

```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 1. 禁用状态（最高优先级） -->
    <item
        android:drawable="@drawable/bg_gray_fill_interact_2"
        android:state_enabled="false" />
    
    <!-- 2. 按下状态 -->
    <item
        android:drawable="@drawable/bg_orange_fill_interact_2"
        android:state_pressed="true" />
    
    <!-- 3. 焦点状态 -->
    <item
        android:drawable="@drawable/bg_blue_field_rect_1"
        android:state_focused="true" />
    
    <!-- 4. 默认状态（最低优先级，必须在最后） -->
    <item android:drawable="@drawable/bg_orange_fill_interact_1" />
</selector>
```

---

## 六、尺寸规范

### 6.1 圆角尺寸

| 圆角类型 | 尺寸引用 | 适用场景 |
|---------|---------|---------|
| 小圆角 | `@dimen/draw_corner_sm` | 小标签、小按钮 |
| 中圆角 | `@dimen/draw_corner_md` | 卡片、面板 |
| 大圆角 | `@dimen/draw_corner_lg` | 大面板、弹窗 |
| 胶囊圆角 | `@dimen/draw_corner_capsule` | 按钮、胶囊形状 |

### 6.2 描边尺寸

| 描边类型 | 尺寸引用 | 说明 |
|---------|---------|------|
| 发丝描边 | `@dimen/draw_stroke_hair` | 约 1dp，最细描边 |
| 细描边 | `@dimen/draw_stroke_sm` | 常规细描边 |
| 中等描边 | `@dimen/draw_stroke_md` | 较粗描边 |

---

## 七、层级规范

### 7.1 视觉层级结构

```
层级1: surface_page_1        → 页面背景（最底层）
    ↓
层级2: surface_panel_1       → 面板背景
    ↓
层级3: surface_card_1        → 卡片背景
    ↓
层级4: surface_lift_1        → 悬浮卡片
    ↓
层级5: surface_float_1       → 浮动层
    ↓
层级6: fill_interact_1       → 交互元素填充
    ↓
层级7: stroke_interact_1     → 描边
    ↓
层级8: scrim_1               → 遮罩层（最顶层）
```

### 7.2 档位规则

- **档位 1**：主要状态、正常状态、默认状态
- **档位 2**：次要状态、按下状态、禁用状态
- **档位 3**：第三状态、边缘状态

---

## 八、文件组织

### 8.1 目录结构

```
res/
├── drawable/                # 通用 drawable
│   ├── bg_*.xml             # 背景类
│   ├── sel_*.xml            # 选择器类
│   └── ic_*.xml             # 图标类
├── drawable-night/          # 夜间模式 drawable（仅需覆写特殊项）
│   └── ...
└── drawable-v24/            # API 24+ 专用
    └── ...
```

### 8.2 夜间模式处理

- 普通 drawable 通过引用 `func_*` 颜色自动适配夜间模式
- 仅在需要特殊处理时，在 `drawable-night/` 目录中覆写

---

## 九、最佳实践

### 9.1 复用原则

```xml
<!-- 错误：重复定义 -->
<shape>
    <solid android:color="@color/func_gray_bg_1" />
    <corners android:radius="@dimen/draw_corner_md" />
</shape>

<!-- 正确：定义一次，selector 引用 -->
<!-- bg_gray_surface_card_1.xml -->
<shape>
    <solid android:color="@color/func_gray_bg_1" />
    <corners android:radius="@dimen/draw_corner_md" />
</shape>

<!-- sel_card_xxx.xml 引用 -->
<item android:drawable="@drawable/bg_gray_surface_card_1" />
```

### 9.2 避免硬编码

```xml
<!-- ❌ 错误：硬编码颜色值 -->
<solid android:color="#F5F5F5" />

<!-- ✅ 正确：引用颜色资源 -->
<solid android:color="@color/func_gray_bg_1" />
```

### 9.3 选择器命名

选择器名称应体现其用途和状态：

```
sel_{用途}_{形状}_{状态}_{默认行为}

示例：
sel_orange_interact_capsule_emphasis_default  # 交互胶囊，强调型，默认行为
sel_gray_field_rect_idle_default             # 输入框矩形，空闲状态
sel_red_field_rect_alert_static             # 输入框矩形，警告状态，静态
```

---

## 十、示例代码

### 10.1 静态背景

```xml
<!-- bg_gray_surface_card_1.xml -->
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/func_gray_bg_1" />
    <corners android:radius="@dimen/draw_corner_md" />
</shape>
```

### 10.2 描边背景

```xml
<!-- bg_gray_stroke_interact_3.xml -->
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/func_clear_1" />
    <stroke
        android:width="@dimen/draw_stroke_hair"
        android:color="@color/func_gray_border_2" />
    <corners android:radius="@dimen/draw_corner_capsule" />
</shape>
```

### 10.3 渐变背景

```xml
<!-- bg_gray_gradient_panel_1.xml -->
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <gradient
        android:angle="270"
        android:endColor="@color/func_gray_bg_2"
        android:startColor="@color/func_gray_bg_1"
        android:type="linear" />
    <corners android:radius="@dimen/draw_corner_md" />
</shape>
```

### 10.4 状态选择器

```xml
<!-- sel_orange_interact_capsule_emphasis_default.xml -->
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 禁用状态 -->
    <item
        android:drawable="@drawable/bg_gray_fill_interact_2"
        android:state_enabled="false" />
    
    <!-- 按下状态 -->
    <item
        android:drawable="@drawable/bg_orange_fill_interact_2"
        android:state_pressed="true" />
    
    <!-- 默认状态 -->
    <item android:drawable="@drawable/bg_orange_fill_interact_1" />
</selector>
```

---

## 十一、检查清单

在创建或修改 Drawable 资源时，请检查以下事项：

| 检查项 | 说明 |
|--------|------|
| ✅ 命名符合规范 | 使用 `{类型}_{色系}_{用途}_{状态}_{档位}` 格式 |
| ✅ 颜色引用正确 | 仅引用 `func_*` 功能色 |
| ✅ 尺寸引用正确 | 使用 `@dimen/draw_*` 尺寸 |
| ✅ 选择器状态顺序 | 禁用 → 按下/焦点 → 默认 |
| ✅ 复用已有资源 | 避免重复定义相同样式 |
| ✅ 文件位置正确 | 根据需要放置在 `drawable/` 或 `drawable-night/` |

---

## 附录

### A. 颜色映射表

| 色系 | 功能色前缀 | 说明 |
|------|-----------|------|
| 灰色 | `func_gray_*` | 通用背景、边框、文本 |
| 橙色 | `func_orange_*` | 品牌主色、强调按钮 |
| 红色 | `func_red_*` | 错误、警告、删除 |
| 蓝色 | `func_blue_*` | 链接、信息提示 |
| 绿色 | `func_green_*` | 成功、完成状态 |
| 黑色 | `func_black_*` | 文本、遮罩 |
| 白色 | `func_white_*` | 背景、浅色元素 |
| 黄色 | `func_yellow_*` | 警告、提醒 |

### B. 用途缩写对照表

| 缩写 | 全称 | 说明 |
|------|------|------|
| bg | background | 背景 |
| sel | selector | 选择器 |
| ic | icon | 图标 |
| surf | surface | 表面 |
| fill | fill | 填充 |
| strk | stroke | 描边 |
| fld | field | 输入框 |
| intr | interact | 交互 |
| grad | gradient | 渐变 |
| scrm | scrim | 遮罩 |