// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AntennaStatusActivity_ViewBinding implements Unbinder {
  private AntennaStatusActivity target;

  @UiThread
  public AntennaStatusActivity_ViewBinding(AntennaStatusActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AntennaStatusActivity_ViewBinding(AntennaStatusActivity target, View source) {
    this.target = target;

    target.tvBarTitle = Utils.findRequiredViewAsType(source, R.id.tv_bar_title, "field 'tvBarTitle'", TextView.class);
    target.tabContent = Utils.findRequiredViewAsType(source, R.id.tab_content, "field 'tabContent'", TabLayout.class);
    target.vpContent = Utils.findRequiredViewAsType(source, R.id.vp_content, "field 'vpContent'", ViewPager.class);
    target.rlTopBar = Utils.findRequiredViewAsType(source, R.id.rl_top_bar, "field 'rlTopBar'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AntennaStatusActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvBarTitle = null;
    target.tabContent = null;
    target.vpContent = null;
    target.rlTopBar = null;
  }
}
