package com.my51c.see51.app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my51c.see51.BaseActivity;
import com.my51c.see51.app.fragment.AddServiceWarnInfoFragment;
import com.my51c.see51.app.fragment.AntennaWarnInfoFragment;
import com.my51c.see51.app.fragment.SmartGatewayWarnInfoFragment;
import com.synertone.netAssistant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class WarnInfoActivity extends BaseActivity {
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private List<SupportFragment> fragments;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_warn_info_layout);
        initView();
        initEvent();
    }

    private void initEvent() {
        rlTopBar.setOnTouchListener(new ComBackTouchListener());
    }

    private void initView() {
        tvBarTitle.setText("告警信息");
        fragments = new ArrayList<>();
        fragments.add(new AntennaWarnInfoFragment());
        fragments.add(new SmartGatewayWarnInfoFragment());
        fragments.add(new AddServiceWarnInfoFragment());
        vpContent.setAdapter(new WarnInfoAdapter(getSupportFragmentManager(), fragments));
        vpContent.setOffscreenPageLimit(1);
        tabContent.setupWithViewPager(vpContent);
    }

    private class WarnInfoAdapter extends FragmentPagerAdapter {
        String[] tabTitles = new String[]{"天线体", "智能网关", "增值业务"};
        private List<SupportFragment> fragmentsList;

        public WarnInfoAdapter(FragmentManager fm, List<SupportFragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
