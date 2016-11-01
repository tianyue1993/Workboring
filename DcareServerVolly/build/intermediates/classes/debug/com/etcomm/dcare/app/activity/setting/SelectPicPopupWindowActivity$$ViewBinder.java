// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectPicPopupWindowActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.SelectPicPopupWindowActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558684, "field 'btn_take_photo' and method 'onClick'");
    target.btn_take_photo = finder.castView(view, 2131558684, "field 'btn_take_photo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558683, "field 'btn_pick_photo' and method 'onClick'");
    target.btn_pick_photo = finder.castView(view, 2131558683, "field 'btn_pick_photo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558685, "field 'btn_cancel' and method 'onClick'");
    target.btn_cancel = finder.castView(view, 2131558685, "field 'btn_cancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558682, "field 'layout'");
    target.layout = finder.castView(view, 2131558682, "field 'layout'");
  }

  @Override public void unbind(T target) {
    target.btn_take_photo = null;
    target.btn_pick_photo = null;
    target.btn_cancel = null;
    target.layout = null;
  }
}
