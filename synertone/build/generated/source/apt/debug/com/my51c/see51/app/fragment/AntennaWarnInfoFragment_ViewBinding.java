// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.app.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.my51c.see51.widget.PinnedSectionListView;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AntennaWarnInfoFragment_ViewBinding implements Unbinder {
  private AntennaWarnInfoFragment target;

  @UiThread
  public AntennaWarnInfoFragment_ViewBinding(AntennaWarnInfoFragment target, View source) {
    this.target = target;

    target.pslvContent = Utils.findRequiredViewAsType(source, R.id.pslv_content, "field 'pslvContent'", PinnedSectionListView.class);
    target.emptyView = Utils.findRequiredView(source, R.id.emptyview, "field 'emptyView'");
  }

  @Override
  @CallSuper
  public void unbind() {
    AntennaWarnInfoFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pslvContent = null;
    target.emptyView = null;
  }
}
