### `MainActivity.java`     主程序，下方那一栏页面

+ `exchangeBg() 换壁纸的函数` 只换下面一栏 在`CityWeatherFragment.java`还会有换壁纸的函数负责换主界面
  getSharedPreferences第一个参数是存储时的名称，第二个参数则是文件的打开方式
  
+ `initPager()`

  + 建立每个城市对应的Fragment（迷你activity），使用bundle传输城市名称至CityManagerActivity类

+ `initPoint()`

  初始化小圆点

  + `setLayoutParams`：动态布局

+ `CityFragmentPaferAdapter.java`

  + `activity_main.xml`中的`main_vp(ViewPager)`的适配器，使用`mainVp.setAdapter(adapter)`的办法
  + `getSupportFragmentManager()`得到Fragment的管理器

+ `setPagerListener`函数

  + `onPageSelected(int position)`：先将所有小圆点设置为白点，将选中的页面对应的小圆点更改为绿点

+ `mainVp.setCurrentItem`函数

  主页显示第几个城市的天气

+ `OnRestart函数`
  再次回到主界面调用，这个函数在页面获取焦点之前进行调用，此处完成ViewPager页数的更新 
  即锁屏或者在后台重新打开

### `CityWeatherFragment.java`   主页面 布局城市天气页面

+ `exchangeBg() 换壁纸的函数`  负责换主界面
+ `onSuccess`：往数据库中加入返回信息（result）
+ `parseShowData`显示发布时间 湿度 气压等信息 并显示今天 以及之后四天的天气情况
+ `onClick`监听 穿衣指数 洗车指数 感冒指数 运动指数 紫外线指数等按钮

### `CityFragmentPagerAdapter.java`    主页面切换

+ `getCount()`    获取当前页面数量
+ `getItemPosition  `   删除页面

### `MoreAcitivity.java`     更多设置界面

+ `setRGListener()` 设置改变背景图片的单选按钮的监听
+ `shareSoftwareMsg()`分享该软件的函数 会跳转到百度网盘下载
+ `clearCache()`清除缓存的函数
+ `getVersionName()`获得应用的版本号

### `CityManagerActivity.java`     城市管理页面

+ `onResume函数` 重新返回此界面
+ `OnClick` 监听返回主界面 删除 和添加城市按钮

### `CityManagerAdapter.java`     城市管理页面

+ `String changeTime`函数 将时间格式化

  + `SimpleDateFormat`中的`parse`：将字符串格式化为指定样式的Date类型数据；`format`：将Date数据格式化为指定格式的时间格式

+ `getView`显示城市管理界面

  +  `View convertView`：convertView主要是为了缓存试图View，用以增加ListView的item view加载效率，先判断convertView是否为空null，如果非空，则直接再次对convertView复用，否则才创建新的View

  + `Gson().fromJson`：Gson提供了fromJson()方法来实现从Json相关对象到Java实体的方法。
  
### `DeleteCityActivity.java`     城市管理中的删除城市界面

+ 监听√和×两个按钮
### `DeleteCityAdapter.java`     城市管理中的删除城市界面

+ 删除城市 删除了提示适配器更新

### `SearchCityActivity.java`     搜索城市界面

+ `onSuccess`请求成功的操作
+ `GetProvice`根据城市查找省份

### `WeatherBean.java`    天气数据处理

+ 将接口上的各种信息保存到各个类当中

### `DBManager.java`     数据库管理

+ `queryAllInfo()`：返回所有城市的**“_id”**，**城市名**，**天气信息（json）**
+ `queryAllCityName()`：返回所有城市的**城市名**
+ `addCityInfo()`：添加城市信息，`param：city, content`
+ `updateInfoByCity()`：根据城市名更新城市数据，`param：city, content`

### `BaseFragment`    声明整体模块，执行网络请求操作

### `DBHelper.java`

建立名为`forecast.dbd`的数据库

