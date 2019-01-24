package com.my51c.see51.app.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.CommonViewHolder;
import com.my51c.see51.app.bean.ModemBean;
import com.my51c.see51.app.bean.ResultBean;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.synertone.commonutil.util.ScreenUtil;
import com.synertone.netAssistant.R;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class InstallParamSetActivity extends BaseActivity {

    private EditText mFrequencyCode;
    private TextView mChooseWave;
    private ImageView mBack;
    private TextView mTittle;
    private RelativeLayout rl_top_bar;
    private TextView tv_next;
    private String mStarCatIp;
    private CommonAdapter<ModemBean> modemAdapter;
    private List<ModemBean> modemBeans=new ArrayList<>();
    private PopupWindow pWindow;
    private ListView listView;
    protected ModemBean dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_param);
        initView();
        initEvent();
    }

    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar_right);
        mBack= (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.modem_set);
        mStarCatIp=getIntent().getStringExtra("ip");
        mFrequencyCode=(EditText)findViewById(R.id.et_frequency_code);
        mFrequencyCode.setTextColor(getResources().getColor(R.color.gray));
        mChooseWave=(TextView)findViewById(R.id.tv_choose_wave);
        initModemList();
        tv_next= (TextView)findViewById(R.id.tv_next);
        tv_next.setText(R.string.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataModel!=null&&dataModel.getBeam().equals(mChooseWave.getText().toString())) {
                    setCatParameter();
                }else{
                    Toast.makeText(mContext,"请选择波束！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChooseWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWindow.showAsDropDown(mChooseWave);
            }
        });
    }
    private void setCatParameter() {
        showDia();
        String getCatParameterUrl = "http://" +  mStarCatIp + "/cgi-bin/instlocation/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        params.addBodyParameter("loc","000M");
        params.addBodyParameter("cluster",dataModel.getCluster());
        AppData.http.send(HttpRequest.HttpMethod.POST, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        try {
                            dismissDia();
                            if(responseInfo!=null){
                                ResultBean resultBean= GsonUtils.fromJson(responseInfo.result.toString(),ResultBean.class);
                                if("1".equals(resultBean.getRes())){
                                    Intent intent=new Intent(mContext,ConfigParamActivity.class);
                                    intent.putExtra("beam", dataModel.getBeam());
                                    intent.putExtra("ip", mStarCatIp);
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void initModemList() {
        pWindow = new PopupWindow();
        pWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        pWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        pWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView = new ListView(this);
        initData();
        modemAdapter = new CommonAdapter<ModemBean>(this, R.layout.item_star_info, modemBeans) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, ModemBean item) {
                viewHolder.setTextForTextView(R.id.tv_star_name, item.getBeam());

            }
        };
        listView.setAdapter(modemAdapter);
        pWindow.setBackgroundDrawable(new ColorDrawable());
        final LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.shape_bottom_corner__gray_bg);
        ll.addView(listView);
        pWindow.setContentView(ll);
        mChooseWave.post(new Runnable() {

            @Override
            public void run() {
                setListViewHeight();
                pWindow.setWidth(mChooseWave.getWidth());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CommonAdapter<ModemBean> adapter = (CommonAdapter) parent.getAdapter();
                dataModel = adapter.getItem(position);
                pWindow.dismiss();
                mChooseWave.setText(dataModel.getBeam());
                mFrequencyCode.setText(dataModel.getCluster());

            }
        });
    }
        private void initData() {
            List<ModemBean> list = DataSupport.findAll(ModemBean.class);
            modemBeans.clear();
            modemBeans.addAll(list);
        }
    private void setListViewHeight() {
        int height = ScreenUtil.dip2px(mContext, 42) * modemAdapter.getCount();
        int scrrenH = (int) (ScreenUtil.getHight(mContext) * 0.55);
        int realH = height >= scrrenH ? scrrenH : height;
        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.height = realH;
    }
    private void initEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        mChooseWave.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(event.getX() < x || event.getX() > (x + mChooseWave.getWidth()) || event.getY() < y || event.getY() > (y + mChooseWave.getHeight())){
            pWindow.dismiss();
        }

        return super.dispatchTouchEvent(event);
    }
}
