// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.my51c.see51.widget.DeviceListView;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocalDeviceActivity_ViewBinding implements Unbinder {
  private LocalDeviceActivity target;

  @UiThread
  public LocalDeviceActivity_ViewBinding(LocalDeviceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocalDeviceActivity_ViewBinding(LocalDeviceActivity target, View source) {
    this.target = target;

    target.tvBarTitle = Utils.findRequiredViewAsType(source, R.id.tv_bar_title, "field 'tvBarTitle'", TextView.class);
    target.rlTopBar = Utils.findRequiredViewAsType(source, R.id.rl_top_bar, "field 'rlTopBar'", RelativeLayout.class);
    target.refreshImg = Utils.findRequiredViewAsType(source, R.id.refreshImg, "field 'refreshImg'", ImageView.class);
    target.listView = Utils.findRequiredViewAsType(source, R.id.dlv_content, "field 'listView'", DeviceListView.class);
    target.waitTextView = Utils.findRequiredViewAsType(source, R.id.loading, "field 'waitTextView'", TextView.class);
    target.progressView = Utils.findRequiredViewAsType(source, R.id.progress_get_devices_image, "field 'progressView'", LinearLayout.class);
    target.emptyView = Utils.findRequiredViewAsType(source, R.id.emptyView, "field 'emptyView'", LinearLayout.class);
    target.emptyDevice = Utils.findRequiredViewAsType(source, R.id.emptydevice, "field 'emptyDevice'", TextView.class);
    target.devListLayout = Utils.findRequiredViewAsType(source, R.id.devListLayout, "field 'devListLayout'", LinearLayout.class);
    target.mapBtn = Utils.findRequiredViewAsType(source, R.id.mapBtn, "field 'mapBtn'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocalDeviceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvBarTitle = null;
    target.rlTopBar = null;
    target.refreshImg = null;
    target.listView = null;
    target.waitTextView = null;
    target.progressView = null;
    target.emptyView = null;
    target.emptyDevice = null;
    target.devListLayout = null;
    target.mapBtn = null;
  }
}
