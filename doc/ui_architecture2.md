# 从「业务功能」到「通用职能」：7大色系×6种职能的App级颜色体系

> 本文是颜色架构系列的第二篇，建议先阅读 [第一篇：三層顏色架構](/ui_architecture1) 了解核心設計理念。

---

## 前言

在第一篇文章发布后，很多开发者问我：「func_功能_数字」这种命名虽然扩展性好，但好像还是有点抽象？

比如 `func_text_1`、`func_bg_1` 这样的命名，虽然技术上是正确的，但在实际使用时，我还需要去查这个数字对应的到底是什么颜色。

这个问题问得很好。今天这篇文章，就是来回答这个问题的。

经过多个项目的实践和迭代，我们总结出了一套**更直观、更易用**的颜色体系：**7大色系 × 6种职能**。

---

## 一、问题的本质

让我们先思考一个问题：颜色在App中到底扮演什么角色？

从UI设计的角度来看，颜色在App中只有**两种职能**：
1. **承载信息**：比如错误用红色、成功用绿色
2. **构建层次**：比如背景用浅色、文字用深色

而颜色的具体「用途」是**业务层**的事情，不是颜色体系该管的事情。

所以，我们把原来的 `func_text_1`、`func_bg_1` 这种按「用途」分类的方式，改成了按「色系+职能」分类。

---

## 二、7大色系 × 6种职能

经过实践验证，我们发现一个App中常用的颜色职能只需要**6种**：

| 职能 | 用途 | 示例 |
|------|------|------|
| **文本色** | 文字显示 | 标题、正文、辅助文字 |
| **背景色** | 填充区域 | 页面背景、卡片背景 |
| **边框色** | 勾勒边界 | 输入框边框、卡片边框 |
| **分割线** | 分隔内容 | 列表分割线、区域分隔 |
| **透明度** | 半透明效果 | 遮罩、玻璃效果 |
| **阴影色** | 阴影效果 | 卡片阴影、按钮阴影 |

而这6种职能，组合**7大色系**，就构成了一个完整的颜色体系。

### 为什么是这7大色系？

| 色系 | 特点 | 主要用途 |
|------|------|----------|
| **黑色系** | 从纯黑到浅灰的完整灰阶 | 正文、次要文字、背景 |
| **灰色系** | 独立的灰色梯度 | 辅助信息、禁用状态 |
| **红色系** | 高饱和度警示色 | 错误、删除、警告 |
| **黄色系** | 温和警示色 | 待处理、进行中 |
| **橙色系** | 强调色 | 热销、促销、主按钮 |
| **蓝色系** | 信任色 | 链接、信息、成功 |
| **绿色系** | 正向色 | 成功、增长、完成 |

这7大色系覆盖了App中所有的颜色使用场景。

---

## 三、完整的颜色矩阵

### 文本色（7色系 × 4档 = 28个）

| 色系 | 命名 | 深浅/饱和度 |
|------|------|------------|
| 黑色系 | `func_black_text_1` ~ `func_black_text_4` | 1最深 → 4最浅 |
| 灰色系 | `func_gray_text_1` ~ `func_gray_text_4` | 1最深 → 4最浅 |
| 红色系 | `func_red_text_1` ~ `func_red_text_4` | 1高饱和 → 4低饱和 |
| 黄色系 | `func_yellow_text_1` ~ `func_yellow_text_4` | 1高饱和 → 4低饱和 |
| 橙色系 | `func_orange_text_1` ~ `func_orange_text_4` | 1高饱和 → 4低饱和 |
| 蓝色系 | `func_blue_text_1` ~ `func_blue_text_4` | 1高饱和 → 4低饱和 |
| 绿色系 | `func_green_text_1` ~ `func_green_text_4` | 1高饱和 → 4低饱和 |

### 背景色（7色系 × 2档 = 14个）

| 色系 | 命名 | 用途 |
|------|------|------|
| 黑色系 | `func_black_bg_1`, `func_black_bg_2` | 深色背景 |
| 灰色系 | `func_gray_bg_1`, `func_gray_bg_2` | 浅灰背景、卡片 |
| 红色系 | `func_red_bg_1`, `func_red_bg_2` | 错误背景 |
| 黄色系 | `func_yellow_bg_1`, `func_yellow_bg_2` | 警告背景 |
| 橙色系 | `func_orange_bg_1`, `func_orange_bg_2` | 强调背景 |
| 蓝色系 | `func_blue_bg_1`, `func_blue_bg_2` | 信息背景 |
| 绿色系 | `func_green_bg_1`, `func_green_bg_2` | 成功背景 |

### 边框色（7色系 × 2档 = 14个）

| 色系 | 命名 | 用途 |
|------|------|------|
| 黑色系 | `func_black_border_1`, `func_black_border_2` | 常规边框 |
| 灰色系 | `func_gray_border_1`, `func_gray_border_2` | 辅助边框 |
| 红色系 | `func_red_border_1`, `func_red_border_2` | 错误边框 |
| 黄色系 | `func_yellow_border_1`, `func_yellow_border_2` | 警告边框 |
| 橙色系 | `func_orange_border_1`, `func_orange_border_2` | 强调边框 |
| 蓝色系 | `func_blue_border_1`, `func_blue_border_2` | 信息边框 |
| 绿色系 | `func_green_border_1`, `func_green_border_2` | 成功边框 |

### 分割线（7色系 × 2档 = 14个）

| 色系 | 命名 | 用途 |
|------|------|------|
| 黑色系 | `func_black_divider_1`, `func_black_divider_2` | 常规分割 |
| 灰色系 | `func_gray_divider_1`, `func_gray_divider_2` | 辅助分割 |
| 红色系 | `func_red_divider_1`, `func_red_divider_2` | 强调分割 |
| 黄色系 | `func_yellow_divider_1`, `func_yellow_divider_2` | 警告分割 |
| 橙色系 | `func_orange_divider_1`, `func_orange_divider_2` | 强调分割 |
| 蓝色系 | `func_blue_divider_1`, `func_blue_divider_2` | 信息分割 |
| 绿色系 | `func_green_divider_1`, `func_green_divider_2` | 成功分割 |

### 透明度色（7色系 × 2档 = 14个）

| 色系 | 命名 | 用途 |
|------|------|------|
| 黑色系 | `func_black_alpha_1`, `func_black_alpha_2` | 深色半透明遮罩 |
| 灰色系 | `func_gray_alpha_1`, `func_gray_alpha_2` | 中性半透明遮罩 |
| 红色系 | `func_red_alpha_1`, `func_red_alpha_2` | 红色半透明效果 |
| 黄色系 | `func_yellow_alpha_1`, `func_yellow_alpha_2` | 黄色半透明效果 |
| 橙色系 | `func_orange_alpha_1`, `func_orange_alpha_2` | 橙色半透明效果 |
| 蓝色系 | `func_blue_alpha_1`, `func_blue_alpha_2` | 蓝色半透明效果 |
| 绿色系 | `func_green_alpha_1`, `func_green_alpha_2` | 绿色半透明效果 |

### 阴影色（7色系 × 2档 = 14个）

| 色系 | 命名 | 用途 |
|------|------|------|
| 黑色系 | `func_black_shadow_1`, `func_black_shadow_2` | 常规阴影 |
| 灰色系 | `func_gray_shadow_1`, `func_gray_shadow_2` | 中性阴影 |
| 红色系 | `func_red_shadow_1`, `func_red_shadow_2` | 红色阴影效果 |
| 黄色系 | `func_yellow_shadow_1`, `func_yellow_shadow_2` | 黄色阴影效果 |
| 橙色系 | `func_orange_shadow_1`, `func_orange_shadow_2` | 橙色阴影效果 |
| 蓝色系 | `func_blue_shadow_1`, `func_blue_shadow_2` | 蓝色阴影效果 |
| 绿色系 | `func_green_shadow_1`, `func_green_shadow_2` | 绿色阴影效果 |

**总计：7 × 6 × 2-4 = 84个颜色定义**，覆盖App所有通用UI场景。

---

## 四、核心原则：职能无关，业务通用

### 为什么叫「通用职能」而非「业务功能」？

因为这套颜色体系是**业务无关**的。

同一个 `func_red_text_1`：
- 在电商App中可以用作「商品下架」提示
- 在社交App中可以用作「被拉黑」提示
- 在金融App中可以用作「交易失败」提示

颜色本身不承载业务语义，业务语义由**使用场景**决定。

### ❌ 反例：业务绑定

```xml
<!-- ❌ 错误：业务绑定，无法复用 -->
<color name="func_product_delete_text">@color/t_red_5</color>    <!-- 商品删除文字 -->
<color name="func_order_cancel_text">@color/t_red_5</color>    <!-- 订单取消文字 -->
```

问题：每新增一个「红色文字」场景，就要加一个新的颜色定义。

### ✅ 正例：职能无关

```xml
<!-- ✅ 正确：职能无关，业务通用 -->
<color name="func_red_text_1">@color/t_red_5</color>    <!-- 任何需要红色文字的地方 -->
<color name="func_red_text_2">@color/t_red_4</color>    <!-- 任何需要浅红文字的地方 -->
```

优势：同一个颜色可以用于多个业务场景，维护成本极低。

---

## 五、为什么不用固定语义命名？

很多人会问：为什么不直接定义 `func_error`、`func_success` 这种语义明确的颜色？

### 固定语义的致命缺陷

```
业务场景：需要比「错误」稍微轻一点的提示色
可选命名：
- func_error_light？
- func_error_sub？
- func_warning_light？

命名很快失控...
```

### 数字层级的无限扩展

```
func_red_text_1      ← 错误提示（最重）
func_red_text_2      ← 警告提示
func_red_text_3      ← 轻微提示
func_red_text_4      ← 次要提示

中间再插一个？
func_red_text_25     ← 直接顺延编号，无需造词
```

**数字命名 = 无限扩展 + 永不乱序**

---

## 六、深入思考：状态色应该放在哪里？

在设计这套体系时，我们曾讨论过是否需要添加「禁用态」「选中态」「按下态」等状态相关的颜色。

### 结论：状态色应该放在 drawable 层

| 状态类型 | 建议位置 | 原因 |
|----------|----------|------|
| **禁用态** | drawable层 | 通过selector组合实现 |
| **选中态** | drawable层 | 通过selector组合实现 |
| **按下态** | drawable层 | 通过selector组合实现 |

### 为什么不在颜色层定义状态色？

1. **职责分离**：颜色层负责提供「原料」，drawable层负责组合「成品」
2. **灵活性**：同一个颜色可以在不同状态下有不同表现
3. **可复用性**：一套颜色可以组合出多种状态效果

### 状态效果的正确实现方式（第三篇预告）

```xml
<!-- drawable/selector_btn_primary.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_enabled="false">
        <shape>
            <solid android:color="@color/func_black_bg_2" />  <!-- 禁用态用灰色背景 -->
            <corners android:radius="24dp" />
        </shape>
    </item>
    <item android:state_pressed="true">
        <shape>
            <solid android:color="@color/func_orange_bg_2" />  <!-- 按下态用浅色 -->
            <corners android:radius="24dp" />
        </shape>
    </item>
    <item>
        <shape>
            <solid android:color="@color/func_orange_bg_1" />  <!-- 常态用主色 -->
            <corners android:radius="24dp" />
        </shape>
    </item>
</selector>
```

---

## 七、与夜间模式的完美配合

得益于三层架构的设计，这套颜色体系可以**一键切换夜间模式**。

### 只需要在 `values-night/colors.xml` 中定义主题层：

```xml
<!-- values/colors.xml（日间模式） -->
<color name="t_white_1">@color/white_1</color>
<color name="t_red_4">@color/red_4</color>

<!-- values-night/colors.xml（夜间模式）：只写 @color，色值仅在基础层定义 -->
<color name="t_white_1">@color/black_8</color>  <!-- 背景反转 -->
<color name="t_red_4">@color/red_3</color>     <!-- 夜间主红略收敛，与仓库一致 -->
```

**功能层完全不用改**，自动跟随主题切换。

---

## 八、实际应用示例

### 场景1：错误提示

```xml
<!-- 错误文字 -->
<TextView
    android:textColor="@color/func_red_text_1"
    android:text="支付失败" />

<!-- 错误背景 -->
<TextView
    android:background="@color/func_red_bg_2"
    android:text="余额不足" />
```

### 场景2：成功状态

```xml
<!-- 成功文字 -->
<TextView
    android:textColor="@color/func_green_text_1"
    android:text="支付成功" />

<!-- 成功边框 -->
<EditText
    android:background="@drawable/bg_success_border" />
```

```xml
<!-- drawable/bg_success_border.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/func_green_bg_1" />
    <corners android:radius="8dp" />
    <stroke android:width="1dp" android:color="@color/func_green_border_1" />
</shape>
```

### 场景3：信息展示

```xml
<!-- 信息分割线 -->
<View
    android:background="@color/func_blue_divider_1" />

<!-- 链接文字 -->
<TextView
    android:textColor="@color/func_blue_text_1"
    android:text="查看详情" />
```

### 场景4：半透明遮罩

```xml
<!-- 弹窗遮罩 -->
<View
    android:background="@color/func_black_alpha_1" />

<!-- 浅色半透明（参考仓库可在功能层增加 func_* 指向 t_white_1_alpha20） -->
<View
    android:background="@color/t_white_1_alpha20" />
```

---

## 九、完整命名规范速查表

### 命名公式

```
func_{色系}_{职能}_{档位}
```

### 色系前缀

| 前缀 | 色系 | 示例 |
|------|------|------|
| `black` | 黑色系 | `func_black_text_1` |
| `gray` | 灰色系 | `func_gray_text_1` |
| `red` | 红色系 | `func_red_text_1` |
| `yellow` | 黄色系 | `func_yellow_text_1` |
| `orange` | 橙色系 | `func_orange_text_1` |
| `blue` | 蓝色系 | `func_blue_text_1` |
| `green` | 绿色系 | `func_green_text_1` |

### 职能后缀

| 后缀 | 职能 | 档位 |
|------|------|------|
| `_text` | 文本色 | 1-4档 |
| `_bg` | 背景色 | 1-2档 |
| `_border` | 边框色 | 1-2档 |
| `_divider` | 分割线 | 1-2档 |
| `_alpha` | 透明度 | 1-2档 |
| `_shadow` | 阴影色 | 1-2档 |

---

## 十、与三层架构的完整关系

```
┌─────────────────────────────────────────────────────────────┐
│         drawable 层（状态逻辑）                              │
│   - selector（按下/选中/禁用态）                              │
│   - shape（圆角、描边、渐变）                                 │
│   - layer-list（多层叠加）                                    │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│       func_ 功能色层（通用职能，84个）                        │
│   - 文本色、背景色、边框色、分割线                             │
│   - 透明度、阴影色                                            │
│   - 7大色系 × 6种职能 × 2-4档                               │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│        t_ 主题层（日夜切换）                                  │
│   - 日间/夜间模式颜色映射                                    │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│        基础色层（纯色值）                                     │
│   - 色系_数字格式                                            │
└─────────────────────────────────────────────────────────────┘
```

---

## 十一、系列文章预告

### 第三篇：Drawable 层设计（即将发布）

下一篇文章，我们将深入探讨：
1. 如何用 selector 组合出各种状态效果
2. 通用 shape 形状的设计规范
3. 按钮、输入框、卡片等常用组件的 drawable 实现
4. 如何实现响应式阴影和渐变效果

---

## 十二、总结

这套「7大色系 × 6种职能」的颜色体系，是对第一篇文章中「func_分类_数字」命名方式的**优化升级**。

### 核心改进

1. **更直观**：看到 `func_red_text_1`，立刻知道这是「红色文字的第一档」
2. **更易用**：不需要记住「错误用func_error_1」，只需要记住「红色系用于警示」
3. **更完整**：新增透明度和阴影职能，覆盖所有视觉效果需求
4. **更扩展**：任何新的「红色文字」需求，都用 `func_red_text_` 系列，不用新造词

### 保持的优势

1. **职能无关**：颜色本身不承载业务语义，业务层自行决定如何使用
2. **无限扩展**：数字层级模式，任意中间态都能顺延编号
3. **维护成本低**：三层架构，改一处全局生效
4. **夜间模式友好**：自动跟随主题切换
5. **职责清晰**：状态逻辑放在 drawable 层，颜色层只提供基础原料

---

## 附录：完整颜色清单

### 文本色（28个）
- `func_black_text_1` ~ `func_black_text_4`
- `func_gray_text_1` ~ `func_gray_text_4`
- `func_red_text_1` ~ `func_red_text_4`
- `func_yellow_text_1` ~ `func_yellow_text_4`
- `func_orange_text_1` ~ `func_orange_text_4`
- `func_blue_text_1` ~ `func_blue_text_4`
- `func_green_text_1` ~ `func_green_text_4`

### 背景色（14个）
- `func_black_bg_1`, `func_black_bg_2`
- `func_gray_bg_1`, `func_gray_bg_2`
- `func_red_bg_1`, `func_red_bg_2`
- `func_yellow_bg_1`, `func_yellow_bg_2`
- `func_orange_bg_1`, `func_orange_bg_2`
- `func_blue_bg_1`, `func_blue_bg_2`
- `func_green_bg_1`, `func_green_bg_2`

### 边框色（14个）
- `func_black_border_1`, `func_black_border_2`
- `func_gray_border_1`, `func_gray_border_2`
- `func_red_border_1`, `func_red_border_2`
- `func_yellow_border_1`, `func_yellow_border_2`
- `func_orange_border_1`, `func_orange_border_2`
- `func_blue_border_1`, `func_blue_border_2`
- `func_green_border_1`, `func_green_border_2`

### 分割线（14个）
- `func_black_divider_1`, `func_black_divider_2`
- `func_gray_divider_1`, `func_gray_divider_2`
- `func_red_divider_1`, `func_red_divider_2`
- `func_yellow_divider_1`, `func_yellow_divider_2`
- `func_orange_divider_1`, `func_orange_divider_2`
- `func_blue_divider_1`, `func_blue_divider_2`
- `func_green_divider_1`, `func_green_divider_2`

### 透明度色（14个）
- `func_black_alpha_1`, `func_black_alpha_2`
- `func_gray_alpha_1`, `func_gray_alpha_2`
- `func_red_alpha_1`, `func_red_alpha_2`
- `func_yellow_alpha_1`, `func_yellow_alpha_2`
- `func_orange_alpha_1`, `func_orange_alpha_2`
- `func_blue_alpha_1`, `func_blue_alpha_2`
- `func_green_alpha_1`, `func_green_alpha_2`

### 阴影色（14个）
- `func_black_shadow_1`, `func_black_shadow_2`
- `func_gray_shadow_1`, `func_gray_shadow_2`
- `func_red_shadow_1`, `func_red_shadow_2`
- `func_yellow_shadow_1`, `func_yellow_shadow_2`
- `func_orange_shadow_1`, `func_orange_shadow_2`
- `func_blue_shadow_1`, `func_blue_shadow_2`
- `func_green_shadow_1`, `func_green_shadow_2`

---

> 💡 完整代码实现见仓库：[`values/colors.xml`](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values/colors.xml)（基础色 + 日间主题 + 功能层）、[`values-night/colors.xml`](https://github.com/zealot2002/arch_ui_token_spec/blob/main/app/src/main/res/values-night/colors.xml)（夜间 `t_*`）。
>
> 关注我，获取更多Android架构设计干货。
>
> **系列文章**：
> - [第一篇：三層顏色架構](/ui_architecture1)
> - **第二篇：7大色系×6种职能的通用职能层**（本文）
> - 第三篇：Drawable 层设计（即将发布）