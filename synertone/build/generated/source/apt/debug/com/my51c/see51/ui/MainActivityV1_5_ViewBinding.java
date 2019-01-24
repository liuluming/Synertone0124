// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.my51c.see51.widget.NewSwitch;
import com.my51c.see51.widget.ReboundScrollView;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivityV1_5_ViewBinding implements Unbinder {
  private MainActivityV1_5 target;

  @UiThread
  public MainActivityV1_5_ViewBinding(MainActivityV1_5 target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivityV1_5_ViewBinding(MainActivityV1_5 target, View source) {
    this.target = target;

    target.tvBarTitle = Utils.findRequiredViewAsType(source, R.id.tv_bar_title, "field 'tvBarTitle'", TextView.class);
    target.rlTopBar = Utils.findRequiredViewAsType(source, R.id.rl_top_bar, "field 'rlTopBar'", RelativeLayout.class);
    target.btnNewCamera = Utils.findRequiredViewAsType(source, R.id.btnNewCamera, "field 'btnNewCamera'", LinearLayout.class);
    target.btnFourViews = Utils.findRequiredViewAsType(source, R.id.btnFourViews, "field 'btnFourViews'", LinearLayout.class);
    target.btnMyCamera = Utils.findRequiredViewAsType(source, R.id.btnMyCamera, "field 'btnMyCamera'", LinearLayout.class);
    target.txLocalList = Utils.findRequiredViewAsType(source, R.id.txLocalList, "field 'txLocalList'", TextView.class);
    target.btnLocalCamera = Utils.findRequiredViewAsType(source, R.id.btnLocalCamera, "field 'btnLocalCamera'", LinearLayout.class);
    target.showWarnSwitch = Utils.findRequiredViewAsType(source, R.id.showWarnSwitch, "field 'showWarnSwitch'", NewSwitch.class);
    target.showWarn = Utils.findRequiredViewAsType(source, R.id.showWarn, "field 'showWarn'", LinearLayout.class);
    target.warnMsg = Utils.findRequiredViewAsType(source, R.id.warnMsg, "field 'warnMsg'", LinearLayout.class);
    target.localVideo = Utils.findRequiredViewAsType(source, R.id.local_video, "field 'localVideo'", LinearLayout.class);
    target.reboundscrollview = Utils.findRequiredViewAsType(source, R.id.reboundscrollview, "field 'reboundscrollview'", ReboundScrollView.class);
    target.setListView = Utils.findRequiredViewAsType(source, R.id.setListView, "field 'setListView'", ListView.class);
    target.btnProgramExit = Utils.findRequiredViewAsType(source, R.id.btnProgramExit, "field 'btnProgramExit'", Button.class);
    target.btnLogin = Utils.findRequiredViewAsType(source, R.id.btnLogin, "field 'btnLogin'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivityV1_5 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvBarTitle = null;
    target.rlTopBar = null;
    target.btnNewCamera = null;
    target.btnFourViews = null;
    target.btnMyCamera = null;
    target.txLocalList = null;
    target.btnLocalCamera = null;
    target.showWarnSwitch = null;
    target.showWarn = null;
    target.warnMsg = null;
    target.localVideo = null;
    target.reboundscrollview = null;
    target.setListView = null;
    target.btnProgramExit = null;
    target.btnLogin = null;
  }
}
