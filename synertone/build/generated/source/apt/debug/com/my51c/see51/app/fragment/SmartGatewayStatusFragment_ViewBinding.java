// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.app.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SmartGatewayStatusFragment_ViewBinding implements Unbinder {
  private SmartGatewayStatusFragment target;

  @UiThread
  public SmartGatewayStatusFragment_ViewBinding(SmartGatewayStatusFragment target, View source) {
    this.target = target;

    target.rlvContent = Utils.findRequiredViewAsType(source, R.id.rlv_content, "field 'rlvContent'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SmartGatewayStatusFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlvContent = null;
  }
}
