package com.example.myapplication.city_manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener {
    EditText searchEt;
    ImageView submitIv;
    GridView searchGv;
    String[] hotCities = {"北京", "上海", "广州", "深圳", "珠海", "佛山", "南京", "苏州", "厦门",
            "长沙", "成都", "福州", "杭州", "武汉", "青岛", "西安", "太原", "沈阳", "重庆", "天津", "南宁"};
    String url1 = "https://wis.qq.com/weather/common?source=pc&weather_type=observe|index|rise|alarm|air|tips|forecast_24h&province=";
    String url2 = "&city=";
    String city;
    String province;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);  // 设置此类的内容视图为activity_search_city.xml
        searchEt = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        searchGv = findViewById(R.id.search_gv);
        submitIv.setOnClickListener(this);
        // 设置适配器
        adapter = new ArrayAdapter<>(this, R.layout.item_hotcity, hotCities);
        searchGv.setAdapter(adapter);
        setListener();
    }

    /* 设置省份列表监听事件*/
    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = hotCities[position];
                // 获取省份
                province = GetProvice(city);
                String url = url1 + province + url2 + city;
                loadData(url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv_submit:
                CityProvinceInfo search = new CityProvinceInfo();
                city = searchEt.getText().toString();
                if (!TextUtils.isEmpty(city)) {
                    // 判断是否有输入城市
                    // province = GetProvice(city);
                    province = search.findObjectProvince(city);
                    String url = url1 + province + url2 + city;
                    loadData(url);
                } else {
                    Toast.makeText(this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (weatherBean.getData().getIndex().getClothes() != null) {
            // 存储添加的城市，返回主界面MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            city = province + " " + city;
            intent.putExtra("city", city);
            startActivity(intent);
        } else {
            Toast.makeText(this, "暂时未收入此城市天气信息...", Toast.LENGTH_SHORT).show();
        }
    }

    private String GetProvice(String city) {
        String[] Citys = {"北京", "上海", "广东省 广州", "广东省 深圳", "广东省 珠海", "广东省 佛山",
                "江苏省 南京", "江苏省 苏州", "福建省 厦门", "湖南省 长沙", "四川省 成都", "福建省 福州", "浙江省 杭州",
                "湖北省 武汉", "山东省 青岛", "陕西省 西安", "山西省 太原", "辽宁省 沈阳", "重庆", "天津", "广西省 南宁"};
        for (int i = 0; i < Citys.length; i++) {
            if (Citys[i].contains(city)) {
                if (Citys[i].split(" ").length > 1) {
                    province = Citys[i].split(" ")[0];
                } else {
                    province = Citys[i].split(" ")[0];
                }
                break;
            }
        }
        return province;
    }
}
