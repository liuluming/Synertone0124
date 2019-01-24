package com.synertone.commonutil.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.synertone.commonutil.R;
public class ConfirmCancelDialog extends BaseNiceDialog {
    private ViewConvertListener convertListener;

    public static ConfirmCancelDialog init() {
        ConfirmCancelDialog confirmCancelDialog = new ConfirmCancelDialog();
        confirmCancelDialog.setMargin(30);
        return confirmCancelDialog;
    }

    @Override
    public int intLayoutId() {
        return layoutId==-1? R.layout.confirm_cancel_dialog :layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }


    public ConfirmCancelDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public ConfirmCancelDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        convertListener = null;
    }
}
