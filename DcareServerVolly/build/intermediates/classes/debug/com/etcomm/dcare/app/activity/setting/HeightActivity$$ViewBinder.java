// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeightActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.HeightActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558585, "field 'height_value'");
    target.height_value = finder.castView(view, 2131558585, "field 'height_value'");
    view = finder.findRequiredView(source, 2131558569, "field 'userimage'");
    target.userimage = finder.castView(view, 2131558569, "field 'userimage'");
    view = finder.findRequiredView(source, 2131558570, "field 'height_scrollview'");
    target.height_scrollview = finder.castView(view, 2131558570, "field 'height_scrollview'");
    view = finder.findRequiredView(source, 2131558571, "field 'height_scrollview_ll'");
    target.height_scrollview_ll = finder.castView(view, 2131558571, "field 'height_scrollview_ll'");
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
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.height_value = null;
    target.userimage = null;
    target.height_scrollview = null;
    target.height_scrollview_ll = null;
    target.btn_next = null;
  }
}
