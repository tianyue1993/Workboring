// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.SettingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558703, "field 'exit_btn' and method 'onClick'");
    target.exit_btn = finder.castView(view, 2131558703, "field 'exit_btn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558704, "field 'setting_personal_rl' and method 'onClick'");
    target.setting_personal_rl = finder.castView(view, 2131558704, "field 'setting_personal_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558705, "field 'setting_goal_rl' and method 'onClick'");
    target.setting_goal_rl = finder.castView(view, 2131558705, "field 'setting_goal_rl'");
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
    view = finder.findRequiredView(source, 2131558715, "field 'setting_screenon_rl'");
    target.setting_screenon_rl = finder.castView(view, 2131558715, "field 'setting_screenon_rl'");
    view = finder.findRequiredView(source, 2131558717, "field 'sb_ios'");
    target.sb_ios = finder.castView(view, 2131558717, "field 'sb_ios'");
    view = finder.findRequiredView(source, 2131558707, "field 'setting_changepassword_rl' and method 'onClick'");
    target.setting_changepassword_rl = finder.castView(view, 2131558707, "field 'setting_changepassword_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558709, "field 'setting_relateworknumber_rl' and method 'onClick'");
    target.setting_relateworknumber_rl = finder.castView(view, 2131558709, "field 'setting_relateworknumber_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558711, "field 'setting_msgsetting_rl' and method 'onClick'");
    target.setting_msgsetting_rl = finder.castView(view, 2131558711, "field 'setting_msgsetting_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558719, "field 'setting_aboutus_rl' and method 'onClick'");
    target.setting_aboutus_rl = finder.castView(view, 2131558719, "field 'setting_aboutus_rl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.exit_btn = null;
    target.setting_personal_rl = null;
    target.setting_goal_rl = null;
    target.setting_submit_rl = null;
    target.setting_screenon_rl = null;
    target.sb_ios = null;
    target.setting_changepassword_rl = null;
    target.setting_relateworknumber_rl = null;
    target.setting_msgsetting_rl = null;
    target.setting_aboutus_rl = null;
  }
}
