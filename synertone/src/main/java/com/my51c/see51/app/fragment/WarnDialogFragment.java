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
import android.widget.TextView;

import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.util.ScreenUtil;
import com.synertone.netAssistant.R;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class WarnDialogFragment extends DialogFragment {

    private WarnDetailDialogFragment warnDetailDialogFragment;
    private ArrayList<String> warnList;
    private Context mContext;

    public WarnDialogFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.dialog_warn_layout, container,
                false);
        return view;
    }

    public void reFreshData(Bundle bundle) {
        warnList = bundle.getStringArrayList("value");
        TextView tv_warn_count = (TextView) getView().findViewById(
                R.id.tv_warn_count);
        tv_warn_count.setText(warnList.size() + "");
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onActivityCreated(arg0);
        Bundle bundle = getArguments();
        warnList = bundle.getStringArrayList("value");
        TextView tv_warn_count = (TextView) getView().findViewById(
                R.id.tv_warn_count);
        tv_warn_count.setText(warnList.size() + "");
        if (warnDetailDialogFragment != null && warnDetailDialogFragment.isVisible()) {
            warnDetailDialogFragment.dismiss();
        }
        getView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                showWarnDetailsDialog(warnList);
                SharedPreferenceManager.saveBoolean(mContext,"isNoRead",false);


            }
        });

    }

    protected void showWarnDetailsDialog(ArrayList<String> warnList) {
        warnDetailDialogFragment = new WarnDetailDialogFragment(mContext);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("value", warnList);
        warnDetailDialogFragment.setArguments(bundle);
        warnDetailDialogFragment.show(getActivity().getSupportFragmentManager(), "");

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
        params.width = (int) (ScreenUtil.getWidth(mContext) * 0.65);
        window.setAttributes(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_guide);
    }

}
