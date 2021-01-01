package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myapplication.city_manager.CityManagerActivity;
import com.example.myapplication.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addCityIv, moreIv;
    LinearLayout pointLayout;
    RelativeLayout outLayout;
    ViewPager mainVp;  // ViewPager的数据源
    List<Fragment> fragmentList;  // 表示需要显示的城市的集合
    List<String> cityList;  // 表示ViewPager的页数指数器显示集合
    List<ImageView> imgList;
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences pref;  // 背景信息存储器
    private int bgNum;  // 背景编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);  // ’添加城市‘按钮
        moreIv = findViewById(R.id.main_iv_more);  // ‘设置’按钮
        pointLayout = findViewById(R.id.main_layout_point);  // 表示城市个数的点的Layout
        outLayout = findViewById(R.id.main_out_layout);  // 设置壁纸
        exchangeBg();
        mainVp = findViewById(R.id.main_vp);
        // ‘添加城市’按钮
        addCityIv.setOnClickListener(this);
        // ‘设置’按钮
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        // cityList = new ArrayList<>();
        cityList = DBManager.queryAllCityName();  // 获取数据库包含的城市信息列表 只获取城市 没有省份
        imgList = new ArrayList<>();

        // 没有城市时显示北京天气
        if (cityList.size() == 0) {
            cityList.add("北京");
        }

        // 因为可能搜索界面点击跳转此界面，会传值，所以此处获取一下
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");//省份 城市 比如 浙江 杭州
            if (!cityList.contains(city.split(" ")[1]) && !TextUtils.isEmpty(city)) {
                cityList.add(city);
            }
        } catch (Exception e) {
            Log.i("animee", "程序出错！");
        }

        // 初始化ViewPager页面
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        // 创建小圆点指示器
        initPoint();
        // 应用启动时显示最后一个城市
        mainVp.setCurrentItem(fragmentList.size() - 1);
        // 设置ViewPager页面监听
        setPagerListener();
    }

    /* 换壁纸的函数 */
    public void exchangeBg() {
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);  // 存储数据，轻量化存储
        bgNum = pref.getInt("bg", 2);  // 如果未设置背景，默认设置为第三张背景
        switch (bgNum) {
            case 0:
                outLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg2);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.bg3);
                break;
        }

    }

    /* 创建Fragment对象，添加到ViewPager数据源当中*/
    private void initPager() {
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", cityList.get(i));  // 依次加入已经存在的城市
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    /* 创建小圆点 ViewPager页面指示器的函数 */
    private void initPoint() {
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);  // a1.jpg：白点，未选中的页面
            // 动态布局
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0, 0, 20, 0);
            
            imgList.add(pIv);
            pointLayout.addView(pIv);  // 加入activity_main.xml中的main_layout_point(LinearLayout)
        }
        imgList.get(imgList.size() - 1).setImageResource(R.mipmap.a2);  // a2.jpg：绿点，选中的页面，默认为第一个
    }

    /* 设置监听页面事件 */
    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1);  // 先将所有小圆点设置为白点
                }
                imgList.get(position).setImageResource(R.mipmap.a2);  // 将选中的页面对应的小圆点更改为绿点
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /* 设置监听点击事件 */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            // 点击‘添加城市’按钮，跳转到设置页面
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            // 点击‘设置’按钮，跳转到设置页面
            case R.id.main_iv_more:
                intent.setClass(this, MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    /* 再次回到主界面调用，这个函数在页面获取焦点之前进行调用，此处完成ViewPager页数的更新 */
    //即锁屏或者在后台重新打开
    @Override
    protected void onRestart() {
        super.onRestart();
        // 获取数据库当中还剩下的城市集合
        List<String> list = DBManager.queryAllCityName();
        if (list.size() == 0) {
            list.add("北京");  // 全部城市被删除，则默认显示北京
        }
        cityList.clear();    //重写加载之前，清空原本数据源
        cityList.addAll(list);  // 剩余城市加入list中，用于创建对应的fragment页面
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();  // 页面数量发生改变，指示器的数量也会发生变化，重写设置添加指示器
        imgList.clear(); // 将布局当中所有元素全部移除
        pointLayout.removeAllViews();  
        initPoint();
        mainVp.setCurrentItem(fragmentList.size() - 1);  // 每次返回主页后显示的为最后一个城市
    }
}
