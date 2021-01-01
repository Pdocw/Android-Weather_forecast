package com.example.myapplication.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.WeatherBean;
import com.example.myapplication.db.DatabaseBean;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DatabaseBean> mDatas;

    public CityManagerAdapter(Context context, List<DatabaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果有缓存，则加载缓存，否则创建新的View
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_manager, null);  // 设定ListView中的一个对象的样式为item_city_manager.xml中的样式
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DatabaseBean bean = mDatas.get(position);  // 获取第position个城市的数据类
        holder.cityTv.setText(bean.getCity());  // 设置城市名
        WeatherBean weatherBean = new Gson().fromJson(bean.getContent(), WeatherBean.class);  // 将城市数据json转换为WeatherBean类
        // 获取今日天气情况
        WeatherBean.DataBean.ObserveBean dataBean = weatherBean.getData().getObserve();  // 获取今日基本天气信息
        holder.conTv.setText(dataBean.getWeather_short());  // 天气预报简单版本（如：晴， 多云......）
        holder.currentTempTv.setText(dataBean.getDegree() + "℃");  // 温度
        holder.windTv.setText("湿度：" + dataBean.getHumidity() + "%");  // 湿度
        try {
            holder.tempRangeTv.setText(changeTime(dataBean.getUpdate_time()));  // 更新时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    // 时间格式化
    private String changeTime(String update_time) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sfstr = "";
        sfstr = sf2.format(sf1.parse(update_time));
        return sfstr;
    }

    class ViewHolder {
        TextView cityTv, conTv, currentTempTv, windTv, tempRangeTv;

        public ViewHolder(View itemView) {
            cityTv = itemView.findViewById(R.id.item_city_tv_city);//城市
            conTv = itemView.findViewById(R.id.item_city_tv_condition);//天气情况
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);//温度
            windTv = itemView.findViewById(R.id.item_city_wind);//风
            tempRangeTv = itemView.findViewById(R.id.item_city_temprange);//温度范围

        }
    }
}
