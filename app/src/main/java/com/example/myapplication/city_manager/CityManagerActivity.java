package com.example.myapplication.city_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addIv, backIv, deleteIv;
    ListView cityLv;
    List<DatabaseBean> mData;  //显示列表数据源
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);  // 显示页面activity_city_manager.xml
        addIv = findViewById(R.id.city_iv_add);  // ’添加城市‘按钮
        backIv = findViewById(R.id.city_iv_back);  // ’返回‘按钮
        deleteIv = findViewById(R.id.city_iv_delete);  // ’删除‘按钮
        cityLv = findViewById(R.id.city_lv);  // ’城市‘ListView
        mData = new ArrayList<>();
        // 添加点击监听事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        // 设置适配器
        adapter = new CityManagerAdapter(this, mData);
        cityLv.setAdapter(adapter);
    }

    /* 重新回到此界面后，获取数据库当中真实数据源，添加到原有数据源当中，提示适配器更新*/
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManager.queryAllInfo();
        mData.clear();  // 先清除
        mData.addAll(list);  // 再添加
        adapter.notifyDataSetChanged();  // 提示适配器更新
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 添加城市按钮
            case R.id.city_iv_add:
                //int cityCount = DBManager.getCityCount();
                Intent intentSearch = new Intent(this, SearchCityActivity.class);  // 跳转至搜索城市界面
                startActivity(intentSearch);
//                if (cityCount < 5) {
//                    Intent intentSearch = new Intent(this, SearchCityActivity.class);  // 跳转至搜索城市界面
//                    startActivity(intentSearch);
//                } else {
//                    Toast.makeText(this, "存储城市数量已达上限，请删除后再增加", Toast.LENGTH_SHORT).show();
//                }
                break;
            // 返回按钮
            case R.id.city_iv_back:
                finish();
                break;
            // 删除城市按钮
            case R.id.city_iv_delete:
                Intent intentDelete = new Intent(this, DeleteCityActivity.class);
                startActivity(intentDelete);
                break;
        }
    }
}
