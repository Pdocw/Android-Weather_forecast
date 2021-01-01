package com.example.myapplication;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment>fragmentList;
    int childCount = 0;   //表示ViewPager包含的页数

    public CityFragmentPagerAdapter(FragmentManager fm, List<Fragment>fragmentLis) {
        super(fm);
        this.fragmentList = fragmentLis;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /* 返回当前有效视图的数量 */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // 当ViewPager的页数发生改变时，必须要重写两个函数
    /* 更新Fragment(小型Activity) */
    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    /* 删除页面 */
    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0) {
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
