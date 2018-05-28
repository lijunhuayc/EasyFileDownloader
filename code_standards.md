
## 摘要

* [1 前言](#1-前言)
* [2 AS 规范](#2-as-规范)
* [3 命名规范](#3-命名规范)
* [4 代码样式规范](#4-代码样式规范)
* [5 资源文件规范](#5-资源文件规范)
* [6 版本统一规范](#6-版本统一规范)
* [7 第三方库规范](#7-第三方库规范)
* [8 注释规范](#8-注释规范)
* [9 测试规范](#9-测试规范)
* [10 其他的一些规范](#10-其他的一些规范)


### 1 前言

为了有利于项目维护、增强代码可读性、提升 Code Review 效率以及规范团队安卓开发，故提出以下安卓开发规范，若确实存在不合理之处可再商议修正。


### 2 AS 规范

工欲善其事，必先利其器。

1. 尽量使用最新的稳定版的 IDE 进行开发。目前项目采用 AndroidStudio3.0.1+稳定版，若因追求使用最新版影响到团队其他人员需负担一定责任；JDK版本使用Java8；其余相关编译环境不得私自修改（比如升级特定AndroidStudio时提示升级Gradle版本）；
2. 编码格式统一为 **UTF-8**；包括 **.java**、**.xml**、**.properties**、**.gradle**等等所有跟项目有关的文件（AndroidStudio Setting中将编码格式统一设置为 **UTF-8**）；
3. 编辑完 .java、.xml 等文件后一定要 **格式化，格式化，格式化**； Java源文件中绝对禁止有冗余的import；所有源代码文件中禁止存在两行及两行以上的空行；Layout布局文件禁止存在非本View的属性代码（例如：RelativeLayout子View中使用android:weight属性等等）；View的依赖id属性值不能使用“@+id/xxx”形式（例如：android:layout_alignTop="@+id/xxId"应该是android:layout_alignTop="@id/xxId"）;View需要在初始化时动态设值的情况禁止在Layout中填入默认值，调试时可以使用tools调试布局（例如：资料页账户名对应的TextView，不能在代码中写入android:text="张三丰"，而应该使用 tools:text="李时珍"）;非ViewGroup的View应该使用单标签形式（例如应该使用 &lt;View />而非&lt;View>&lt;/View>）；

### 3 命名规范

代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。正确的英文拼写和语法可以让阅读者易于理解，避免歧义。
所有的命名的单词应与之对应功能或者业务相关（例如“用户数据处理工具类”命名为“UserDataHandleUtil”，“登陆布局页面”命名为activity_login_layout）。

> 注意：即使纯拼音命名方式也要避免采用。但 `alibaba`、`taobao`、`youku`、`hangzhou` 等国际通用的名称，可视同英文。

#### 3.1 包名

包名全部小写，连续的单词只是简单地连接起来，采用反域名命名规则，全部使用小写字母。一级包名是顶级域名，通常为 `com`、`edu`、`gov`、`net`、`org` 等，二级包名为公司名，三级包名根据应用进行命名，后面就是对包名的划分了，关于包名的划分，推荐采用 PBF（按功能分包 Package By Feature）。例如谷歌的MVP示例项目：**[todo-mvp][todo-mvp]**，其结构如下所示，很值得学习。

```
com
└── example
    └── android
        └── architecture
            └── blueprints
                └── todoapp
                    ├── BasePresenter.java
                    ├── BaseView.java
                    ├── addedittask
                    │   ├── AddEditTaskActivity.java
                    │   ├── AddEditTaskContract.java
                    │   ├── AddEditTaskFragment.java
                    │   └── AddEditTaskPresenter.java
                    ├── data
                    │   ├── Task.java
                    │   └── source
                    │       ├── TasksDataSource.java
                    │       ├── TasksRepository.java
                    │       ├── local
                    │       │   ├── TasksDbHelper.java
                    │       │   ├── TasksLocalDataSource.java
                    │       │   └── TasksPersistenceContract.java
                    │       └── remote
                    │           └── TasksRemoteDataSource.java
                    ├── statistics
                    │   ├── StatisticsActivity.java
                    │   ├── StatisticsContract.java
                    │   ├── StatisticsFragment.java
                    │   └── StatisticsPresenter.java
                    ├── taskdetail
                    │   ├── TaskDetailActivity.java
                    │   ├── TaskDetailContract.java
                    │   ├── TaskDetailFragment.java
                    │   └── TaskDetailPresenter.java
                    ├── tasks
                    │   ├── ScrollChildSwipeRefreshLayout.java
                    │   ├── TasksActivity.java
                    │   ├── TasksContract.java
                    │   ├── TasksFilterType.java
                    │   ├── TasksFragment.java
                    │   └── TasksPresenter.java
                    └── util
                        ├── ActivityUtils.java
                        ├── EspressoIdlingResource.java
                        └── SimpleCountingIdlingResource.java
```

参考以上的代码结构，按功能分包具体可以这样做：

```
com
└── xxx
    └── xxxx
        ├── App.java 定义 Application 类
        ├── Config.java 定义配置数据
        ├── custom_view 自定义视图
        ├── data 数据处理
        │   ├── DataManager.java 数据管理器，
        │   ├── Constants.java 全局通用常量，
        │   ├── local 来源于本地的数据，比如 SP，Database，File
        │   └── remote 来源于远端的数据（网络库封装）
        ├── feature 功能
        │   ├── base 业务层各类型的统一base类
        │   ├── feature0 功能 0
        │   │   ├── model 业务模块相关的model、bean
        │   │   ├── feature0Activity.java
        │   │   ├── feature0Fragment.java
        │   │   ├── xxAdapter.java
        │   │   └── ... 其他 class
        │   └── ...其他功能
        ├── injection 依赖注入
        ├── util 工具类
        └── ...其他
```


#### 3.2 类名

类名都以 `UpperCamelCase` 风格驼峰命名规则编写。

类名名以类的功能名词或名词短语采用大驼峰命名法，尽量避免缩写，除非该缩写是众所周知的， 比如 HTML、URL，如果类名称中包含单词缩写，则单词缩写的每个字母均应大写。

| 类                     | 描述                        | 例如                                       |
| :-------------------- | :------------------------ | :--------------------------------------- |
| `Activity` 类          | `Activity` 为后缀标识          | 欢迎页面类 `WelcomeActivity`                  |
| `Adapter` 类           | `Adapter` 为后缀标识           | 新闻详情适配器 `NewsDetailAdapter`              |
| 解析类                   | `Parser` 为后缀标识            | 首页解析类 `HomePosterParser`                 |
| 工具方法类                 | `Utils` 或 `Manager` 为后缀标识 | 线程池管理类：`ThreadPoolManager`<br>SP数据工具类：`SharedPerferencesUtils`<br>打印工具类：`PrintUtils` |
| `Service` 类           | 以 `Service` 为后缀标识         | 时间服务 `TimeService`                       |
| `BroadcastReceiver` 类 | 以 `Receiver` 为后缀标识        | 推送接收 `JPushReceiver`                     |
| `ContentProvider` 类   | 以 `Provider` 为后缀标识        | `ShareProvider`                          |
| 自定义的共享基础类             | 以 `Base` 开头               | `BaseActivity`, `BaseFragment`           |

测试类的命名以它要测试的类的名称开始，以 Test 结束。例如：`HashTest` 或 `HashIntegrationTest`。

接口（interface）：命名规则与类一样采用大驼峰命名法，多以 Listener 或 Callback 结尾，如 `OnClickListener`、`AppCompatCallback`。

> 注意：如果项目采用 MVP，所有 Model、View、Presenter 的接口都以 I 为前缀，不加后缀，其他的接口采用上述命名规则。


#### 3.3 方法名

方法名都以 `lowerCamelCase` 风格驼峰命名规则编写。

方法名通常是动词或动词短语。

| 方法                          | 说明                                       |
| :-------------------------- | ---------------------------------------- |
| `initXX()`                  | 初始化相关方法，使用 init 为前缀标识，如初始化布局 `initView()` |
| `isXX()`, `checkXX()`       | 方法返回值为 boolean 型的请使用 is/check 等前缀标识      |
| `getXX()`                   | 返回某个值的方法，使用 get 为前缀标识                    |
| `setXX()`                   | 设置某个属性值                                  |
| `handleXX()`, `processXX()` | 对数据进行处理的方法                               |
| `displayXX()`, `showXX()`   | 弹出提示框和提示信息，使用 display/show 为前缀标识         |
| `updateXX()`                | 更新数据                                     |
| `saveXX()`, `insertXX()`    | 保存或插入数据                                  |
| `resetXX()`                 | 重置数据                                     |
| `clearXX()`                 | 清除数据                                     |
| `removeXX()`, `deleteXX()`  | 移除数据或者视图等，如 `removeView()`               |
| `drawXX()`                  | 绘制数据或效果相关的，使用 draw 前缀标识                  |

以上方法命名方式非强制，但尽量使用类似比较具象的命名规则。

#### 3.4 常量名

常量名命名模式为 `CONSTANT_CASE`，全部字母大写，用下划线分隔单词。

#### 3.5 非常量字段名

非常量字段名以 `lowerCamelCase` 风格的驼峰命名规则。

> 注意：所有的 VO（值对象）统一采用标准的 lowerCamelCase 风格编写；所有的 DTO（数据传输对象）就可按照接口文档中定义的字段名编写，若使用lowerCamelCase 风格则可以使用JSON解析库的字段映射来匹配。

##### 3.5.1 scope（范围）

静态字段命名以 `s` 开头。

非静态，非公有字段命名以 `m` 开头。

非静态，公有字段命名以小写字母开头。

例如：

```java
public class MyClass {
    public int publicField;
    private static MyClass sSingleton;
    int mPackagePrivate;
    private int mPrivate;
    protected int mProtected;
}
```

##### 3.5.2 Type0（控件类型）

考虑到 Android 众多的 UI 控件，为避免控件和普通成员变量混淆以及更好地表达意思，所有用来表示控件的成员变量统一加上控件全名或者控件首字母全大写的缩写或者控件名的最后一个单词作为后缀（具体见附录 [UI 控件缩写表](#ui-控件缩写表)）。

例如：`mAvatarIV`、`lineView`、`containerLayout`、`adListView`。

##### 3.5.3 Type1（数据类型）

对于表示集合或者数组的非常量字段名，我们可以添加后缀来增强字段的可读性，比如：

集合添加如下后缀：List、Map、Set。

数组添加如下后缀：Arr。

例如：`mIvAvatarList`、`userArr`、`firstNameSet`。

> 注意：如果数据类型不确定的话，比如表示的是很多书，那么使用其复数形式来表示也可，例如 `mBooks`。


#### 3.6 参数名

参数名以 `lowerCamelCase` 风格驼峰命名规则编写，参数应该避免用单个字符命名。参数命名推荐使用p作为前缀，例如 `pName`


#### 3.7 局部变量名

局部变量名以 `lowerCamelCase` 风格驼峰命名规则编写，比起其它类型的名称，局部变量名可以有更为宽松的缩写。

虽然缩写更宽松，但还是要避免用单字符进行命名，除了临时变量和循环变量。

即使局部变量是 `final` 和不可改变的，也不应该把它示为常量，自然也不能用常量的规则去命名它。


#### 3.8 临时变量

临时变量通常被取名为 `i`、`j`、`k`、`m` 和 `n`，它们一般用于整型；`c`、`d`、`e`，它们一般用于字符型。 如：`for (int i = 0; i < len; i++)`。


#### 3.9 类型变量名

类型变量可用以下两种风格之一进行命名：

1. 单个的大写字母，后面可以跟一个数字（如：`E`, `T`, `X`, `T2`）。
2. 以类命名方式（参考[3.2 类名](#32-类名)），后面加个大写的 T（如：`RequestT`, `FooBarT`）。

更多还可参考：**[阿里巴巴 Java 开发手册][阿里巴巴 Java 开发手册]**


### 4 代码样式规范

#### 4.1 使用标准大括号样式

左大括号不单独占一行，与其前面的代码位于同一行：

```java
class MyClass {
    int func() {
        if (something) {
            // ...
        } else if (somethingElse) {
            // ...
        } else {
            // ...
        }
    }
}
```

我们需要在条件语句周围添加大括号。注意：if的子句只有一行时也必须加上大括号。例如：

```java
if (condition) {
    body();
}
```

不接受以下样式：

```java
if (condition) body();
```

也不接受以下样式：

```java
if (condition)
    body();  // bad!
```


#### 4.2 编写高内聚高复用方法

编写方法时应尽量遵守单一职责原则，同时应该考虑方法的可复用性。例如：页面启动初始化时，获取网络数据的代码不应该放再 initView()中执行，而应该剥离为 requestXxxData()方法。


#### 4.3 类成员的顺序

统一规范的成员排序有助于提高代码的可读性，推荐使用如下排序：

1. 常量
2. 字段
3. Constructor方法
4. Override方法
5. 业务方法
7. 内部类或接口

其中“Override方法”和“业务方法”也可以采用业务相近的方式放置，即Override方法中调用的业务方法可以靠近自己方便阅读。
对于继承 Android 组件（例如`Activity`或`Fragment`）时的 Override 方法之间应按周期顺序排序放置。

例如：

```java
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String mTitle;
    private TextView mTextViewTitle;

    @Override
    public void onCreate() {
        ...
    }

    public void setTitle(String title) {
    	mTitle = title;
    }

    private void setUpView() {
        ...
    }

    static class AnInnerClass {

    }
}
```

例如，`Activity` 实现了 `onCreate()`、`onDestroy()`、`onPause()`、`onResume()`，它的正确排序如下所示：

```java
public class MainActivity extends Activity {
    //Order matches Activity lifecycle
    @Override
    public void onCreate() {}

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onDestroy() {}
}
```


#### 4.4 函数参数的排序

在 Android 开发过程中，`Context` 在函数参数中是再常见不过的了，我们最好把 `Context` 作为其第一个参数。

正相反，我们把回调接口应该作为其最后一个参数。

例如：

```java
// Context always goes first
public User loadUser(Context context, int userId);

// Callbacks always go last
public void loadUserAsync(Context context, int userId, UserCallback callback);
```


#### 4.5 字符串常量的命名和值

Android SDK 中的很多类都用到了键值对函数，比如 `SharedPreferences`、`Bundle`、`Intent`，所以，即便是一个小应用，我们最终也不得不编写大量的字符串常量。

当时用到这些类的时候，我们 **必须** 将它们的键定义为 `static final` 字段，并遵循以下指示作为前缀。

| 类                  | 字段名前缀       |
| ------------------ | ----------- |
| SharedPreferences  | `PREF_`或`SP_`     |
| Bundle             | `BUNDLE_`   |
| Fragment Arguments | `ARGUMENT_` |
| Intent Action      | `ACTION_`   |

说明：虽然 `Fragment.getArguments()` 得到的也是 `Bundle` ，但因为这是 `Bundle` 的常用用法，所以特意为此定义一个不同的前缀。

例如：

```java
// 注意：字段的值与名称相同以避免重复问题
static final String PREF_EMAIL = "PREF_EMAIL";
static final String SP_USER_ACCOUNT = "SP_USER_ACCOUNT";
static final String BUNDLE_AGE = "BUNDLE_AGE";
static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
static final String EXTRA_SURNAME = "EXTRA_SURNAME";
static final String ACTION_OPEN_USER = "ACTION_OPEN_USER";
```


#### 4.6 Activities 和 Fragments 的传参

当 `Activity` 或 `Fragment` 传递数据通过 `Intent` 或 `Bundle` 时，不同值的键须遵循上一条所提及到的。

当 `Fragment` 启动需要传递参数时，那么它需要提供一个 `public static` 的函数来帮助启动或创建它。

如下所示：

```java
public static MainFragment newInstance(User user) {
      Bundle args = new Bundle();
      args.putParcelable(ARGUMENT_USER, user);
      MainFragment fragment = new MainFragment();
      fragment.setArguments(args);
      return fragment;
}
```

##### 4.7 换行策略

这没有一个准确的解决方案来决定如何换行，通常不同的解决方案都是有效的，但是有一些规则可以应用于常见的情况。

##### 4.7.1 操作符的换行

除赋值操作符之外，我们把换行符放在操作符之前，例如：

```java
int longName = anotherVeryLongVariable + anEvenLongerOne - thisRidiculousLongOne
        + theFinalOne;
```

赋值操作符的换行我们放在其后，例如：

```java
int longName =
        anotherVeryLongVariable + anEvenLongerOne - thisRidiculousLongOne + theFinalOne;
```


##### 4.7.2 函数链的换行

当同一行中调用多个函数时（比如使用构建器时），对每个函数的调用应该在新的一行中，我们把换行符插入在 `.` 之前。

例如：

```java
Picasso.with(context).load("https://blankj.com/images/avatar.jpg").into(ivAvatar);
```

我们应该使用如下规则：

```java
Picasso.with(context)
        .load("https://blankj.com/images/avatar.jpg")
        .into(ivAvatar);
```


##### 4.7.3 多参数的换行

当一个方法有很多参数或者参数很长的时候，我们应该在每个 `,` 后面进行换行。

比如：

```java
loadPicture(context, "https://blankj.com/images/avatar.jpg", ivAvatar, "Avatar of the user", clickListener);
```

我们应该使用如下规则：

```java
loadPicture(context,
        "https://blankj.com/images/avatar.jpg",
        ivAvatar,
        "Avatar of the user",
        clickListener);
```

### 5 资源文件规范

#### 5.1 动画资源文件（anim/ 和 animator/）

安卓主要包含属性动画和视图动画，其视图动画包括补间动画和逐帧动画。属性动画文件需要放在 `res/animator/` 目录下，视图动画文件需放在 `res/anim/` 目录下。

命名规则：`{模块名_}逻辑名称`。

说明：`{}` 中的内容为可选，`逻辑名称` 可由多个单词加下划线组成。

例如：`refresh_progress.xml`、`market_cart_add.xml`、`market_cart_remove.xml`。

如果是普通的补间动画或者属性动画，可采用：`动画类型_方向` 的命名方式。

例如：

| 名称                  | 说明      |
| ------------------- | ------- |
| `fade_in`           | 淡入      |
| `fade_out`          | 淡出      |
| `push_down_in`      | 从下方推入   |
| `push_down_out`     | 从下方推出   |
| `push_left`         | 推向左方    |
| `slide_in_from_top` | 从头部滑动进入 |
| `zoom_enter`        | 变形进入    |
| `slide_in`          | 滑动进入    |
| `shrink_to_middle`  | 中间缩小    |


#### 5.2 颜色资源文件（color/）

专门存放颜色相关的资源文件。

颜色资源禁止放于 `res/drawable/` 目录引用。


#### 5.3 图片资源文件（drawable/ 和 mipmap/）

`res/drawable/` 目录下放的是位图文件（.png、.9.png、.jpg、.gif）或编译为可绘制对象资源子类型的 XML 文件，而 `res/mipmap/` 目录下放的是不同密度的启动图标，所以 `res/mipmap/` 只用于存放启动图标，其余图片资源文件都应该放到 `res/drawable/` 目录下。

参考：

| 名称                        | 说明                       |
| ------------------------- | ------------------------ |
| `btn_main_about.png`      | 主页关于按键 `类型_模块名_逻辑名称`     |
| `btn_back.png`            | 返回按键 `类型_逻辑名称`           |
| `divider_maket_white.png` | 商城白色分割线 `类型_模块名_颜色`      |
| `ic_edit.png`             | 编辑图标 `类型_逻辑名称`           |
| `bg_main.png`             | 主页背景 `类型_逻辑名称`           |
| `btn_red.png`             | 红色按键 `类型_颜色`             |
| `btn_red_big.png`         | 红色大按键 `类型_颜色`            |
| `ic_head_small.png`       | 小头像图标 `类型_逻辑名称`          |
| `bg_input.png`            | 输入框背景 `类型_逻辑名称`          |
| `divider_white.png`       | 白色分割线 `类型_颜色`            |
| `bg_main_head.png`        | 主页头部背景 `类型_模块名_逻辑名称`     |
| `def_search_cell.png`     | 搜索页面默认单元图片 `类型_模块名_逻辑名称` |
| `ic_more_help.png`        | 更多帮助图标 `类型_逻辑名称`         |
| `divider_list_line.png`   | 列表分割线 `类型_逻辑名称`          |
| `sel_search_ok.xml`       | 搜索界面确认选择器 `类型_模块名_逻辑名称`  |
| `shape_music_ring.xml`    | 音乐界面环形形状 `类型_模块名_逻辑名称`   |

如果有多种形态，如按钮选择器：`sel_btn_xx.xml`，采用如下命名：

| 名称                      | 说明                            |
| ----------------------- | ----------------------------- |
| `sel_btn_xx`            | 作用在 `btn_xx` 上的 `selector`    |
| `btn_xx_normal`         | 默认状态效果                        |
| `btn_xx_pressed`        | `state_pressed` 点击效果          |
| `btn_xx_focused`        | `state_focused` 聚焦效果          |
| `btn_xx_disabled`       | `state_enabled` 不可用效果         |
| `btn_xx_checked`        | `state_checked` 选中效果          |
| `btn_xx_selected`       | `state_selected` 选中效果         |
| `btn_xx_hovered`        | `state_hovered` 悬停效果          |
| `btn_xx_checkable`      | `state_checkable` 可选效果        |
| `btn_xx_activated`      | `state_activated` 激活效果        |
| `btn_xx_window_focused` | `state_window_focused` 窗口聚焦效果 |

> 注意：使用 Android Studio 的插件 SelectorChapek 可以快速生成 selector，前提是命名要规范。


#### 5.4 布局资源文件（layout/）

命名规则：`类型_模块名_后缀`、`类型{_模块名}_逻辑名称_后缀`。

说明：`{}` 中的内容为可选。其中页面布局使用 `_layout`作为后缀；其他布局使用 `_view` 作为后缀；avtivity布局使用 `avtivity_`为前缀；fragment布局使用 `fragment_`为前缀；ListView 或 GridView 或 RecyclerView 的复用Item布局使用 `item_view`为前缀或者`item_`为前缀 `_view`为后缀。

例如：

| 名称                          | 说明                          |
| --------------------------- | --------------------------- |
| `activity_main_layout.xml`         | 主窗体 `类型_模块名`                |
| `activity_main_head_layout.xml`    | 主窗体头部 `类型_模块名_逻辑名称`         |
| `fragment_music_layout.xml`        | 音乐片段 `类型_模块名`               |
| `fragment_music_player_layout.xml` | 音乐片段的播放器 `类型_模块名_逻辑名称`      |
| `dialog_loading_view.xml`        | 加载对话框 `类型_逻辑名称`             |
| `ppw_info_view.xml`              | 信息弹窗（PopupWindow） `类型_逻辑名称` |
| `item_main_song_view.xml`        | 主页歌曲列表项 `类型_模块名_逻辑名称`       |


#### 5.5 菜单资源文件（menu/）

菜单相关的资源文件应放在该目录下。

命名规则：`{模块名_}逻辑名称`

说明：`{}` 中的内容为可选。

例如：`main_drawer.xml`、`navigation.xml`。


#### 5.6 values 资源文件（values/）

`values/` 资源文件下的文件都以 `s` 结尾，如 `attrs.xml`、`colors.xml`、`dimens.xml`，起作用的不是文件名称，而是 `<resources>` 标签下的各种标签，比如 `<style>` 决定样式，`<color>` 决定颜色，所以，可以把一个大的 `xml` 文件分割成多个小的文件，比如可以有多个 `style` 文件，如 `styles.xml`、`styles_home.xml`、`styles_item_details.xml`、`styles_forms.xml`。

colors.xml、dimens.xml、strings.xml、styles.xml、themes.xml 不做强制命名，但需遵循基本命名规则，且命名需具备分类特性。

#### 5.7 id 命名

id命名尽量采用 3.5.2 规范的控件命名规则，以提高代码编写效率（可复制粘贴id作为变量名，或者使用 ButterKnife Zelezny等插件生成基础代码）以及代码的可读性。

### 6 版本统一规范

项目采用baseconfig.gradle、dependencies.gradle、flavors_congif.gradle三个基本gradle配置文件来规范项目的gradle，其中baseconfig.gradle中为通用gradle配置，所有module均引用此gradle配置文件统一依赖环境，dependencies.gradle中定义一些全局的常量，由项目根目录的build.gradle导入，全局gradle文件引用其中定义的统一变量值，flavors_config.gradle中定义了渠道打包配置。
涉及到的so的引用，只保留armeabi和armeabi-v7a两类so库。

### 7 第三方库规范

在需要新引入第三方库时，需对要引入的库进行必要的资料查找和库的可使用性调研。禁止使用稳定性和适配性不足的三方库。
第三方库统一采用独立module的形式隔离导入，针对第三方库编写的特殊代码也应该放入第三方库对应的module中，例如针对腾讯信鸽推送需要编写的 Receiver 子类应该放入信鸽module中而不是放入主module（app module）中。

### 8 注释规范

为了减少他人阅读你代码的痛苦值，请在关键地方做好注释。
其中所有类必须在类名上方注释类的功能，接口同理；所有核心业务方法应该注释此方法的功能；不容易理解的变量名也应该写明注释（对于接口对应的module或者bean类，所有字段必须添加注释，如果没有就找接口编写者提供补全）；比较核心的代码块也应当提供必要的注释。

#### 8.1 类注释

每个类完成后应该有作者姓名和联系方式的注释，对自己的代码负责。

```java
/**
 * Desc: 消息列表
 * Created by Junhua.Li
 * Date: 2018/03/23 17:25
 */
public class MessageListActivity {
    ...
}
```

具体可以在 AS 中自己配制，进入 Settings -> Editor -> File and Code Templates -> Includes -> File Header，输入

```java
/**
 * Desc: 
 * Created by Junhua.Li
 * Date: ${YEAR}/${MONTH}/${DAY} ${TIME}
 */
```
这样便可在每次新建类的时候自动加上该头注释。

#### 8.2 方法注释

每一个成员方法（包括自定义成员方法、覆盖方法、属性方法）的方法头都必须做方法头注释，在方法前一行输入 `/** + 回车` 或者设置 `Fix doc comment`（Settings -> Keymap -> Fix doc comment）快捷键，AS 便会帮你生成模板，我们只需要补全参数即可，如下所示。

```java
/**
 * bitmap 转 byte Array
 *
 * @param bitmap bitmap 对象
 * @param format 格式
 * @return 字节数组
 */
public static byte[] bitmap2Bytes(Bitmap bitmap, CompressFormat format) {
    if (bitmap == null) return null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(format, 100, baos);
    return baos.toByteArray();
}
```


#### 8.3 块注释

块注释与其周围的代码在同一缩进级别。它们可以是 `/* ... */` 风格，也可以是 `// ...` 风格（**`//` 后最好带一个空格**）。对于多行的 `/* ... */` 注释，后续行必须从 `*` 开始， 并且与前一行的 `*` 对齐。以下示例注释都是 OK 的。

```java
/*
 * This is
 * okay.
 */

// And so
// is this.

```

> Tip：在写多行注释时，如果你希望在必要时能重新换行（即注释像段落风格一样），那么使用 `/* ... */`。


#### 8.4 其他一些注释

AS 已帮你集成了一些注释模板，我们只需要直接使用即可，在代码中输入 `todo`、`fixme` 等这些注释模板，回车后便会出现如下注释。

```java
// TODO: 17/3/14 需要实现，但目前还未实现的功能的说明
// FIXME: 17/3/14 需要修正，甚至代码是错误的，不能工作，需要修复的说明
```


### 9 测试规范

业务开发完成之后，开发人员需要做必要的自测，测试代码覆盖率达到一定程度后再提测。

### 10 其他的一些规范

1. 合理布局，有效运用 `<merge>`、`<ViewStub>`、`<include>` 标签；
2. `Activity` 和 `Fragment` 里面有许多重复的操作以及操作步骤，所以我们都需要提供一个 `BaseActivity` 和 `BaseFragment`，让所有的 `Activity` 和 `Fragment` 都继承这个基类。
3. 方法基本上都按照调用的先后顺序在各自区块中排列；
4. 相关功能作为小区块放在一起（或者封装掉）；
5. 当一个类有多个构造函数，或是多个同名函数，这些函数应该按顺序出现在一起，中间不要放进其它函数；
6. 数据提供统一的入口。比如操作“SharedPreferences”的应该使用一个工具类`SharedPreferencesUtils`等等；
7. 派生类尽量保持在四级以内；
8. 提取方法，去除重复代码。对于必要的工具类抽取也很重要，这在以后的项目中是可以重用的。
9. 尽量减少对变量的重复计算；

    如下面的操作：

    ```java
    for (int i = 0; i < list.size(); i++) {
          ...
    }
    ```

    建议替换为：

    ```java
    for (int i = 0, len = list.size(); i < len; i++) {
          ...
    }
    ```

10. 尽量采用懒加载的策略，即在需要的时候才创建；

    例如：

    ```java
    String str = "aaa";
    if (i == 1) {
          list.add(str);
    }
    ```

    建议替换为：

    ```java
    if (i == 1) {
          String str = "aaa";
          list.add(str);
    }
    ```

11. 不要在循环中使用 `try…catch…`，应该把其放在最外层；
12. 使用带缓冲的输入输出流进行 IO 操作；
13. 尽量使用 `HashMap`、`ArrayList`、`StringBuilder`，除非线程安全需要，否则不推荐使用 `HashTable`、`Vector`、`StringBuffer`，后三者由于使用同步机制而导致了性能开销；
14. 尽量在合适的场合使用单例；
    使用单例可以减轻加载的负担、缩短加载的时间、提高加载的效率，但并不是所有地方都适用于单例，简单来说，单例主要适用于以下三个方面：
    1. 控制资源的使用，通过线程同步来控制资源的并发访问。
    2. 控制实例的产生，以达到节约资源的目的。
    3. 控制数据的共享，在不建立直接关联的条件下，让多个不相关的线程之间实现通信。
15. 尽量不要使用，`数据 + ""` 形式拼接或者转换字符串；
16. 最后不要忘了内存泄漏的检测；


---

### 常见的英文单词缩写表

| 名称                   | 缩写                                       |
| -------------------- | ---------------------------------------- |
| average              | avg                                      |
| background           | bg（主要用于布局和子布局的背景）                        |
| buffer               | buf                                      |
| control              | ctrl                                     |
| current              | cur                                      |
| default              | def                                      |
| delete               | del                                      |
| document             | doc                                      |
| error                | err                                      |
| escape               | esc                                      |
| icon                 | ic（主要用在 App 的图标）                         |
| increment            | inc                                      |
| information          | info                                     |
| initial              | init                                     |
| image                | img                                      |
| Internationalization | I18N                                     |
| length               | len                                      |
| library              | lib                                      |
| message              | msg                                      |
| password             | pwd                                      |
| position             | pos                                      |
| previous             | pre                                      |
| selector             | sel（主要用于某一 view 多种状态，不仅包括 ListView 中的 selector，还包括按钮的 selector） |
| server               | srv                                      |
| string               | str                                      |
| temporary            | tmp                                      |
| window               | win                                      |

程序中使用单词缩写原则：不要用缩写，除非该缩写是约定俗成的。


## 参考
[Android 开发规范（完结版）][Android 开发规范（完结版）]

[Android 包命名规范][Android 包命名规范]

[Android 开发最佳实践][Android 开发最佳实践]

[Android 编码规范][Android 编码规范]

[阿里巴巴 Java 开发手册][阿里巴巴 Java 开发手册]

[Project and code style guidelines][Project and code style guidelines]

[Google Java 编程风格指南][Google Java 编程风格指南]

[小细节，大用途，35 个 Java 代码性能优化总结！][小细节，大用途，35 个 Java 代码性能优化总结！]

[Android 开发规范（完结版）]: https://github.com/Blankj/AndroidStandardDevelop/blob/master/README.md
[todo-mvp]: https://github.com/googlesamples/android-architecture/tree/todo-mvp/
[Android 包命名规范]: http://www.ayqy.net/blog/android%E5%8C%85%E5%91%BD%E5%90%8D%E8%A7%84%E8%8C%83/
[Android 开发最佳实践]: https://github.com/futurice/android-best-practices/blob/master/translations/Chinese/README.cn.md
[Android 编码规范]: http://www.jianshu.com/p/0a984f999592
[阿里巴巴 Java 开发手册]: https://github.com/alibaba/p3c/blob/master/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C%EF%BC%88%E7%BA%AA%E5%BF%B5%E7%89%88%EF%BC%89.pdf
[Project and code style guidelines]: https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
[Google Java 编程风格指南]: http://www.hawstein.com/posts/google-java-style.html
[小细节，大用途，35 个 Java 代码性能优化总结！]: http://www.jianshu.com/p/436943216526
