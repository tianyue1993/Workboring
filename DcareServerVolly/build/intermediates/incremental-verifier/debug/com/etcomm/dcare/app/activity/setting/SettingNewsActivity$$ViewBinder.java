// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingNewsActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.SettingNewsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558532, "field 'btn_next' and method 'onClick'");
    target.btn_next = finder.castView(view, 2131558532, "field 'btn_next'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558721, "field 'iv_avator'");
    target.iv_avator = finder.castView(view, 2131558721, "field 'iv_avator'");
    view = finder.findRequiredView(source, 2131558727, "field 'age'");
    target.age = finder.castView(view, 2131558727, "field 'age'");
    view = finder.findRequiredView(source, 2131558723, "field 'weight'");
    target.weight = finder.castView(view, 2131558723, "field 'weight'");
    view = finder.findRequiredView(source, 2131558725, "field 'height'");
    target.height = finder.castView(view, 2131558725, "field 'height'");
    view = finder.findRequiredView(source, 2131558661, "field 'wl_pickerage'");
    target.wl_pickerage = finder.castView(view, 2131558661, "field 'wl_pickerage'");
    view = finder.findRequiredView(source, 2131558662, "field 'wl_pickerweight'");
    target.wl_pickerweight = finder.castView(view, 2131558662, "field 'wl_pickerweight'");
    view = finder.findRequiredView(source, 2131558663, "field 'wl_pickerheight'");
    target.wl_pickerheight = finder.castView(view, 2131558663, "field 'wl_pickerheight'");
    view = finder.findRequiredView(source, 2131558658, "field 'cancel' and method 'onClick'");
    target.cancel = finder.castView(view, 2131558658, "field 'cancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558660, "field 'sure' and method 'onClick'");
    target.sure = finder.castView(view, 2131558660, "field 'sure'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558657, "field 'layout_wl'");
    target.layout_wl = finder.castView(view, 2131558657, "field 'layout_wl'");
    view = finder.findRequiredView(source, 2131558659, "field 'choosetext'");
    target.choosetext = finder.castView(view, 2131558659, "field 'choosetext'");
    view = finder.findRequiredView(source, 2131558726, "field 'll_age' and method 'onClick'");
    target.ll_age = finder.castView(view, 2131558726, "field 'll_age'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558724, "field 'll_height' and method 'onClick'");
    target.ll_height = finder.castView(view, 2131558724, "field 'll_height'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558722, "field 'll_weight' and method 'onClick'");
    target.ll_weight = finder.castView(view, 2131558722, "field 'll_weight'");
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
    target.btn_next = null;
    target.iv_avator = null;
    target.age = null;
    target.weight = null;
    target.height = null;
    target.wl_pickerage = null;
    target.wl_pickerweight = null;
    target.wl_pickerheight = null;
    target.cancel = null;
    target.sure = null;
    target.layout_wl = null;
    target.choosetext = null;
    target.ll_age = null;
    target.ll_height = null;
    target.ll_weight = null;
  }
}
