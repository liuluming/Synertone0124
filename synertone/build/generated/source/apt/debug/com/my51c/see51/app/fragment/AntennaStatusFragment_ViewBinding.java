// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.app.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AntennaStatusFragment_ViewBinding implements Unbinder {
  private AntennaStatusFragment target;

  @UiThread
  public AntennaStatusFragment_ViewBinding(AntennaStatusFragment target, View source) {
    this.target = target;

    target.tvOdutype = Utils.findRequiredViewAsType(source, R.id.tv_odutype, "field 'tvOdutype'", TextView.class);
    target.tvOdunum = Utils.findRequiredViewAsType(source, R.id.tv_odunum, "field 'tvOdunum'", TextView.class);
    target.tvOduver = Utils.findRequiredViewAsType(source, R.id.tv_oduver, "field 'tvOduver'", TextView.class);
    target.tvLocatype = Utils.findRequiredViewAsType(source, R.id.tv_locatype, "field 'tvLocatype'", TextView.class);
    target.tvLnglat = Utils.findRequiredViewAsType(source, R.id.tv_lnglat, "field 'tvLnglat'", TextView.class);
    target.tvAzi = Utils.findRequiredViewAsType(source, R.id.tv_azi, "field 'tvAzi'", TextView.class);
    target.tvElevcarr = Utils.findRequiredViewAsType(source, R.id.tv_elevcarr, "field 'tvElevcarr'", TextView.class);
    target.tvRoll = Utils.findRequiredViewAsType(source, R.id.tv_roll, "field 'tvRoll'", TextView.class);
    target.tvRssi = Utils.findRequiredViewAsType(source, R.id.tv_rssi, "field 'tvRssi'", TextView.class);
    target.tvBucSwitch = Utils.findRequiredViewAsType(source, R.id.tv_bucSwitch, "field 'tvBucSwitch'", TextView.class);
    target.tvTemp = Utils.findRequiredViewAsType(source, R.id.tv_temp, "field 'tvTemp'", TextView.class);
    target.rvAntennatrouble = Utils.findRequiredViewAsType(source, R.id.rv_antenna_trouble, "field 'rvAntennatrouble'", RecyclerView.class);
    target.svContent = Utils.findRequiredViewAsType(source, R.id.sv_content, "field 'svContent'", ScrollView.class);
    target.tvStarElev = Utils.findRequiredViewAsType(source, R.id.tv_star_elev, "field 'tvStarElev'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AntennaStatusFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvOdutype = null;
    target.tvOdunum = null;
    target.tvOduver = null;
    target.tvLocatype = null;
    target.tvLnglat = null;
    target.tvAzi = null;
    target.tvElevcarr = null;
    target.tvRoll = null;
    target.tvRssi = null;
    target.tvBucSwitch = null;
    target.tvTemp = null;
    target.rvAntennatrouble = null;
    target.svContent = null;
    target.tvStarElev = null;
  }
}
