// Generated code from Butter Knife. Do not modify!
package com.my51c.see51.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.came.viewbguilib.ButtonBgUi;
import com.my51c.see51.widget.ReSpinner;
import com.synertone.netAssistant.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SuperSetRefActivity_ViewBinding implements Unbinder {
  private SuperSetRefActivity target;

  @UiThread
  public SuperSetRefActivity_ViewBinding(SuperSetRefActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SuperSetRefActivity_ViewBinding(SuperSetRefActivity target, View source) {
    this.target = target;

    target.tv_bar_title = Utils.findRequiredViewAsType(source, R.id.tv_bar_title, "field 'tv_bar_title'", TextView.class);
    target.rl_top_bar = Utils.findRequiredViewAsType(source, R.id.rl_top_bar, "field 'rl_top_bar'", RelativeLayout.class);
    target.tv_compass = Utils.findRequiredViewAsType(source, R.id.tv_compass, "field 'tv_compass'", TextView.class);
    target.bt_compass = Utils.findRequiredViewAsType(source, R.id.bt_compass, "field 'bt_compass'", ButtonBgUi.class);
    target.ll_compass = Utils.findRequiredViewAsType(source, R.id.ll_compass, "field 'll_compass'", LinearLayout.class);
    target.bt_save_default = Utils.findRequiredViewAsType(source, R.id.bt_save_default, "field 'bt_save_default'", ButtonBgUi.class);
    target.tv_satellite_number = Utils.findRequiredViewAsType(source, R.id.tv_satellite_number, "field 'tv_satellite_number'", TextView.class);
    target.et_satellite_number = Utils.findRequiredViewAsType(source, R.id.et_satellite_number, "field 'et_satellite_number'", EditText.class);
    target.tv_satellite_longitude = Utils.findRequiredViewAsType(source, R.id.tv_satellite_longitude, "field 'tv_satellite_longitude'", TextView.class);
    target.sp_longitude_type = Utils.findRequiredViewAsType(source, R.id.sp_longitude_type, "field 'sp_longitude_type'", Spinner.class);
    target.et_satellite_longitude = Utils.findRequiredViewAsType(source, R.id.et_satellite_longitude, "field 'et_satellite_longitude'", EditText.class);
    target.tv_mobility_support = Utils.findRequiredViewAsType(source, R.id.tv_mobility_support, "field 'tv_mobility_support'", TextView.class);
    target.tb_mobility_support_switch = Utils.findRequiredViewAsType(source, R.id.tb_mobility_support_switch, "field 'tb_mobility_support_switch'", ToggleButton.class);
    target.tv_aim_satellite_mode = Utils.findRequiredViewAsType(source, R.id.tv_aim_satellite_mode, "field 'tv_aim_satellite_mode'", TextView.class);
    target.sp_aim_satellite_mode = Utils.findRequiredViewAsType(source, R.id.sp_aim_satellite_mode, "field 'sp_aim_satellite_mode'", ReSpinner.class);
    target.tv_aim_satellite_frequency = Utils.findRequiredViewAsType(source, R.id.tv_aim_satellite_frequency, "field 'tv_aim_satellite_frequency'", TextView.class);
    target.et_aim_satellite_frequency = Utils.findRequiredViewAsType(source, R.id.et_aim_satellite_frequency, "field 'et_aim_satellite_frequency'", EditText.class);
    target.ll_aim_satellite_frequency = Utils.findRequiredViewAsType(source, R.id.ll_aim_satellite_frequency, "field 'll_aim_satellite_frequency'", LinearLayout.class);
    target.tv_zai_bo = Utils.findRequiredViewAsType(source, R.id.tv_zai_bo, "field 'tv_zai_bo'", TextView.class);
    target.et_zai_bo_frequency = Utils.findRequiredViewAsType(source, R.id.et_zai_bo_frequency, "field 'et_zai_bo_frequency'", EditText.class);
    target.ll_zai_bo_rate = Utils.findRequiredViewAsType(source, R.id.ll_zai_bo_rate, "field 'll_zai_bo_rate'", LinearLayout.class);
    target.tv_zai_bo_bandwidth = Utils.findRequiredViewAsType(source, R.id.tv_zai_bo_bandwidth, "field 'tv_zai_bo_bandwidth'", TextView.class);
    target.sp_zai_bo_bandwidth = Utils.findRequiredViewAsType(source, R.id.sp_zai_bo_bandwidth, "field 'sp_zai_bo_bandwidth'", Spinner.class);
    target.ll_zai_bo_bandwidth = Utils.findRequiredViewAsType(source, R.id.ll_zai_bo_bandwidth, "field 'll_zai_bo_bandwidth'", LinearLayout.class);
    target.tv_center_frequency = Utils.findRequiredViewAsType(source, R.id.tv_center_frequency, "field 'tv_center_frequency'", TextView.class);
    target.et_center_frequency = Utils.findRequiredViewAsType(source, R.id.et_center_frequency, "field 'et_center_frequency'", EditText.class);
    target.ll_center_frequency = Utils.findRequiredViewAsType(source, R.id.ll_center_frequency, "field 'll_center_frequency'", LinearLayout.class);
    target.tv_symbol_rate = Utils.findRequiredViewAsType(source, R.id.tv_symbol_rate, "field 'tv_symbol_rate'", TextView.class);
    target.et_symbol_rate = Utils.findRequiredViewAsType(source, R.id.et_symbol_rate, "field 'et_symbol_rate'", EditText.class);
    target.ll_symbol_rate = Utils.findRequiredViewAsType(source, R.id.ll_symbol_rate, "field 'll_symbol_rate'", LinearLayout.class);
    target.tv_polarization_type = Utils.findRequiredViewAsType(source, R.id.tv_polarization_type, "field 'tv_polarization_type'", TextView.class);
    target.sp_polarization_type = Utils.findRequiredViewAsType(source, R.id.sp_polarization_type, "field 'sp_polarization_type'", Spinner.class);
    target.bt_antenna_stop = Utils.findRequiredViewAsType(source, R.id.bt_antenna_stop, "field 'bt_antenna_stop'", ButtonBgUi.class);
    target.tv_location_type = Utils.findRequiredViewAsType(source, R.id.tv_location_type, "field 'tv_location_type'", TextView.class);
    target.sp_location_type = Utils.findRequiredViewAsType(source, R.id.sp_location_type, "field 'sp_location_type'", Spinner.class);
    target.tv_current_lon = Utils.findRequiredViewAsType(source, R.id.tv_current_lon, "field 'tv_current_lon'", TextView.class);
    target.sp_current_lon_type = Utils.findRequiredViewAsType(source, R.id.sp_current_lon_type, "field 'sp_current_lon_type'", Spinner.class);
    target.et_current_lon = Utils.findRequiredViewAsType(source, R.id.et_current_lon, "field 'et_current_lon'", EditText.class);
    target.tv_current_lat = Utils.findRequiredViewAsType(source, R.id.tv_current_lat, "field 'tv_current_lat'", TextView.class);
    target.sp_current_lat_type = Utils.findRequiredViewAsType(source, R.id.sp_current_lat_type, "field 'sp_current_lat_type'", Spinner.class);
    target.et_current_lat = Utils.findRequiredViewAsType(source, R.id.et_current_lat, "field 'et_current_lat'", EditText.class);
    target.tv_rth = Utils.findRequiredViewAsType(source, R.id.tv_rth, "field 'tv_rth'", TextView.class);
    target.et_rth = Utils.findRequiredViewAsType(source, R.id.et_rth, "field 'et_rth'", EditText.class);
    target.tv_rsd = Utils.findRequiredViewAsType(source, R.id.tv_rsd, "field 'tv_rsd'", TextView.class);
    target.et_rsd = Utils.findRequiredViewAsType(source, R.id.et_rsd, "field 'et_rsd'", EditText.class);
    target.tv_pitch_compensation_angle = Utils.findRequiredViewAsType(source, R.id.tv_pitch_compensation_angle, "field 'tv_pitch_compensation_angle'", TextView.class);
    target.et_pitch_compensation_angle = Utils.findRequiredViewAsType(source, R.id.et_pitch_compensation_angle, "field 'et_pitch_compensation_angle'", EditText.class);
    target.tv_lnb_local_oscillator = Utils.findRequiredViewAsType(source, R.id.tv_lnb_local_oscillator, "field 'tv_lnb_local_oscillator'", TextView.class);
    target.et_lnb_local_oscillator = Utils.findRequiredViewAsType(source, R.id.et_lnb_local_oscillator, "field 'et_lnb_local_oscillator'", EditText.class);
    target.tv_buc = Utils.findRequiredViewAsType(source, R.id.tv_buc, "field 'tv_buc'", TextView.class);
    target.sp_buc_type = Utils.findRequiredViewAsType(source, R.id.sp_buc_type, "field 'sp_buc_type'", Spinner.class);
    target.ll_buc_type = Utils.findRequiredViewAsType(source, R.id.ll_buc_type, "field 'll_buc_type'", LinearLayout.class);
    target.tv_buc_switch = Utils.findRequiredViewAsType(source, R.id.tv_buc_switch, "field 'tv_buc_switch'", TextView.class);
    target.tb_buc_switch = Utils.findRequiredViewAsType(source, R.id.tb_buc_switch, "field 'tb_buc_switch'", ToggleButton.class);
    target.ll_buc_switch = Utils.findRequiredViewAsType(source, R.id.ll_buc_switch, "field 'll_buc_switch'", LinearLayout.class);
    target.tv_buc_local_oscillator = Utils.findRequiredViewAsType(source, R.id.tv_buc_local_oscillator, "field 'tv_buc_local_oscillator'", TextView.class);
    target.et_buc_local_oscillator = Utils.findRequiredViewAsType(source, R.id.et_buc_local_oscillator, "field 'et_buc_local_oscillator'", EditText.class);
    target.ll_buc_local_oscillator = Utils.findRequiredViewAsType(source, R.id.ll_buc_local_oscillator, "field 'll_buc_local_oscillator'", LinearLayout.class);
    target.tv_buc_gain_attenuation = Utils.findRequiredViewAsType(source, R.id.tv_buc_gain_attenuation, "field 'tv_buc_gain_attenuation'", TextView.class);
    target.et_buc_gain_attenuation = Utils.findRequiredViewAsType(source, R.id.et_buc_gain_attenuation, "field 'et_buc_gain_attenuation'", EditText.class);
    target.ll_buc_gain_attenuation = Utils.findRequiredViewAsType(source, R.id.ll_buc_gain_attenuation, "field 'll_buc_gain_attenuation'", LinearLayout.class);
    target.tv_network_detection = Utils.findRequiredViewAsType(source, R.id.tv_network_detection, "field 'tv_network_detection'", TextView.class);
    target.tb_network_detection_switch = Utils.findRequiredViewAsType(source, R.id.tb_network_detection_switch, "field 'tb_network_detection_switch'", ToggleButton.class);
    target.ll_network_detection = Utils.findRequiredViewAsType(source, R.id.ll_network_detection, "field 'll_network_detection'", LinearLayout.class);
    target.tv_debug_model = Utils.findRequiredViewAsType(source, R.id.tv_debug_model, "field 'tv_debug_model'", TextView.class);
    target.iv_right_arrow = Utils.findRequiredViewAsType(source, R.id.iv_right_arrow, "field 'iv_right_arrow'", ImageView.class);
    target.ll_debug_model = Utils.findRequiredViewAsType(source, R.id.ll_debug_model, "field 'll_debug_model'", LinearLayout.class);
    target.scrollview_compass = Utils.findRequiredViewAsType(source, R.id.scrollview_compass, "field 'scrollview_compass'", ScrollView.class);
    target.bt_save = Utils.findRequiredViewAsType(source, R.id.bt_save, "field 'bt_save'", ButtonBgUi.class);
    target.bt_aim_satellite = Utils.findRequiredViewAsType(source, R.id.bt_aim_satellite, "field 'bt_aim_satellite'", ButtonBgUi.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SuperSetRefActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_bar_title = null;
    target.rl_top_bar = null;
    target.tv_compass = null;
    target.bt_compass = null;
    target.ll_compass = null;
    target.bt_save_default = null;
    target.tv_satellite_number = null;
    target.et_satellite_number = null;
    target.tv_satellite_longitude = null;
    target.sp_longitude_type = null;
    target.et_satellite_longitude = null;
    target.tv_mobility_support = null;
    target.tb_mobility_support_switch = null;
    target.tv_aim_satellite_mode = null;
    target.sp_aim_satellite_mode = null;
    target.tv_aim_satellite_frequency = null;
    target.et_aim_satellite_frequency = null;
    target.ll_aim_satellite_frequency = null;
    target.tv_zai_bo = null;
    target.et_zai_bo_frequency = null;
    target.ll_zai_bo_rate = null;
    target.tv_zai_bo_bandwidth = null;
    target.sp_zai_bo_bandwidth = null;
    target.ll_zai_bo_bandwidth = null;
    target.tv_center_frequency = null;
    target.et_center_frequency = null;
    target.ll_center_frequency = null;
    target.tv_symbol_rate = null;
    target.et_symbol_rate = null;
    target.ll_symbol_rate = null;
    target.tv_polarization_type = null;
    target.sp_polarization_type = null;
    target.bt_antenna_stop = null;
    target.tv_location_type = null;
    target.sp_location_type = null;
    target.tv_current_lon = null;
    target.sp_current_lon_type = null;
    target.et_current_lon = null;
    target.tv_current_lat = null;
    target.sp_current_lat_type = null;
    target.et_current_lat = null;
    target.tv_rth = null;
    target.et_rth = null;
    target.tv_rsd = null;
    target.et_rsd = null;
    target.tv_pitch_compensation_angle = null;
    target.et_pitch_compensation_angle = null;
    target.tv_lnb_local_oscillator = null;
    target.et_lnb_local_oscillator = null;
    target.tv_buc = null;
    target.sp_buc_type = null;
    target.ll_buc_type = null;
    target.tv_buc_switch = null;
    target.tb_buc_switch = null;
    target.ll_buc_switch = null;
    target.tv_buc_local_oscillator = null;
    target.et_buc_local_oscillator = null;
    target.ll_buc_local_oscillator = null;
    target.tv_buc_gain_attenuation = null;
    target.et_buc_gain_attenuation = null;
    target.ll_buc_gain_attenuation = null;
    target.tv_network_detection = null;
    target.tb_network_detection_switch = null;
    target.ll_network_detection = null;
    target.tv_debug_model = null;
    target.iv_right_arrow = null;
    target.ll_debug_model = null;
    target.scrollview_compass = null;
    target.bt_save = null;
    target.bt_aim_satellite = null;
  }
}
