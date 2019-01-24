package com.my51c.see51.app.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.CommonViewHolder;
import com.synertone.commonutil.util.ScreenUtil;
import com.synertone.netAssistant.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public  class WarnDetailDialogFragment extends DialogFragment {
   private Context mContext;

    public WarnDetailDialogFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.dialog_warn_detail_layout,
                container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onActivityCreated(arg0);
        Bundle bundle = getArguments();
        final ArrayList<String> warnList = bundle.getStringArrayList("value");
        final ListView lv_content = (ListView) getView().findViewById(
                R.id.lv_content);
        CommonAdapter<String> adapter = new CommonAdapter<String>(
                mContext, R.layout.item_warn_layout, warnList) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, String item) {
                viewHolder.setTextForTextView(R.id.tv_warn_content,
                        item);
				/*viewHolder.setTextForTextView(R.id.tv_warn_time,
						item.getWarnTime());*/
            }
        };
        lv_content.setAdapter(adapter);
        lv_content.post(new Runnable() {

            @Override
            public void run() {
                int height = lv_content.getHeight();
                int Screenhight = (int) (ScreenUtil.getHight(mContext) * 0.8);
                ViewGroup.LayoutParams layoutParams = lv_content.getLayoutParams();
                layoutParams.height = height >= Screenhight ? Screenhight
                        : height;
                lv_content.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 必须放在onresume 才能填充屏幕
        setDialogLayout(getDialog());
    }

    private void setDialogLayout(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 注意此处
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (ScreenUtil.getWidth(mContext) * 0.85);
        window.setAttributes(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_guide);
    }

}
