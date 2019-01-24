package com.my51c.see51.app;

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
import com.my51c.see51.app.fragment.AntennaStatusFragment;
import com.my51c.see51.app.fragment.SmartGatewayStatusFragment;
import com.synertone.netAssistant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class AntennaStatusActivity extends BaseActivity {

    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    List<SupportFragment> fragments = new ArrayList<>();
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antenna_status);
        initView();
        initEvent();
    }
    private void initEvent() {
        rlTopBar.setOnTouchListener(new ComBackTouchListener());
    }

    private void initView() {
        tvBarTitle.setText("运行状态");
        fragments.add(new AntennaStatusFragment());
        fragments.add(new SmartGatewayStatusFragment());
        vpContent.setAdapter(new AntennaStatusAdapter(getSupportFragmentManager(), fragments));
        tabContent.setupWithViewPager(vpContent);
    }

    class AntennaStatusAdapter extends FragmentPagerAdapter {
        String[] tabTitles = new String[]{"天线状态", "智能网关状态"};
        private List<SupportFragment> fragmentsList;

        public AntennaStatusAdapter(FragmentManager fm, List<SupportFragment> fragments) {
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
