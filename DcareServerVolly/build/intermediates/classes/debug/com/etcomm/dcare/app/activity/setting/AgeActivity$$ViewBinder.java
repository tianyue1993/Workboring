// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AgeActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.AgeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558540, "field 'age_value'");
    target.age_value = finder.castView(view, 2131558540, "field 'age_value'");
    view = finder.findRequiredView(source, 2131558542, "field 'userinfo_staff'");
    target.userinfo_staff = finder.castView(view, 2131558542, "field 'userinfo_staff'");
    view = finder.findRequiredView(source, 2131558544, "field 'age_scrollview'");
    target.age_scrollview = finder.castView(view, 2131558544, "field 'age_scrollview'");
    view = finder.findRequiredView(source, 2131558545, "field 'age_scrollview_ll'");
    target.age_scrollview_ll = finder.castView(view, 2131558545, "field 'age_scrollview_ll'");
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
    target.age_value = null;
    target.userinfo_staff = null;
    target.age_scrollview = null;
    target.age_scrollview_ll = null;
    target.btn_next = null;
  }
}
