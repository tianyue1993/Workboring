// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PedometerActivity$$ViewBinder<T extends com.etcomm.dcare.PedometerActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131559053, "field 'pedometer_switch'");
    target.pedometer_switch = finder.castView(view, 2131559053, "field 'pedometer_switch'");
    view = finder.findRequiredView(source, 2131559055, "field 'screenlongon_switch'");
    target.screenlongon_switch = finder.castView(view, 2131559055, "field 'screenlongon_switch'");
    view = finder.findRequiredView(source, 2131558718, "field 'tv1'");
    target.tv1 = finder.castView(view, 2131558718, "field 'tv1'");
    view = finder.findRequiredView(source, 2131559054, "field 'pedometer_tv1'");
    target.pedometer_tv1 = finder.castView(view, 2131559054, "field 'pedometer_tv1'");
    view = finder.findRequiredView(source, 2131559061, "field 'pedometer_device_rl' and method 'onClick'");
    target.pedometer_device_rl = finder.castView(view, 2131559061, "field 'pedometer_device_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558713, "field 'setting_submit_rl' and method 'onClick'");
    target.setting_submit_rl = finder.castView(view, 2131558713, "field 'setting_submit_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559057, "field 'pedometer_plus' and method 'onClick'");
    target.pedometer_plus = finder.castView(view, 2131559057, "field 'pedometer_plus'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559058, "field 'pedometer_sensitivity'");
    target.pedometer_sensitivity = finder.castView(view, 2131559058, "field 'pedometer_sensitivity'");
    view = finder.findRequiredView(source, 2131559059, "field 'pedometer_minus' and method 'onClick'");
    target.pedometer_minus = finder.castView(view, 2131559059, "field 'pedometer_minus'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559062, "field 'pedometer_device_imageView1'");
    target.pedometer_device_imageView1 = finder.castView(view, 2131559062, "field 'pedometer_device_imageView1'");
    view = finder.findRequiredView(source, 2131559063, "field 'wrist_device_control_tv'");
    target.wrist_device_control_tv = finder.castView(view, 2131559063, "field 'wrist_device_control_tv'");
    view = finder.findRequiredView(source, 2131559056, "field 'app_sensitivityrl'");
    target.app_sensitivityrl = finder.castView(view, 2131559056, "field 'app_sensitivityrl'");
    view = finder.findRequiredView(source, 2131558842, "field 'device_imageView1'");
    target.device_imageView1 = finder.castView(view, 2131558842, "field 'device_imageView1'");
    view = finder.findRequiredView(source, 2131559060, "field 'app_sensitivitytv'");
    target.app_sensitivitytv = finder.castView(view, 2131559060, "field 'app_sensitivitytv'");
    view = finder.findRequiredView(source, 2131558715, "field 'setting_screenon_rl'");
    target.setting_screenon_rl = finder.castView(view, 2131558715, "field 'setting_screenon_rl'");
    view = finder.findRequiredView(source, 2131558716, "field 'setting_screenon_imageView1'");
    target.setting_screenon_imageView1 = finder.castView(view, 2131558716, "field 'setting_screenon_imageView1'");
    view = finder.findRequiredView(source, 2131559052, "field 'setting_pedometer_imageView1'");
    target.setting_pedometer_imageView1 = finder.castView(view, 2131559052, "field 'setting_pedometer_imageView1'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.pedometer_switch = null;
    target.screenlongon_switch = null;
    target.tv1 = null;
    target.pedometer_tv1 = null;
    target.pedometer_device_rl = null;
    target.setting_submit_rl = null;
    target.pedometer_plus = null;
    target.pedometer_sensitivity = null;
    target.pedometer_minus = null;
    target.pedometer_device_imageView1 = null;
    target.wrist_device_control_tv = null;
    target.app_sensitivityrl = null;
    target.device_imageView1 = null;
    target.app_sensitivitytv = null;
    target.setting_screenon_rl = null;
    target.setting_screenon_imageView1 = null;
    target.setting_pedometer_imageView1 = null;
  }
}
