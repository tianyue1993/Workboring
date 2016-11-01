// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangeNickNameActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.ChangeNickNameActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar' and method 'onClick'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558623, "field 'nickname_et'");
    target.nickname_et = finder.castView(view, 2131558623, "field 'nickname_et'");
    view = finder.findRequiredView(source, 2131558532, "field 'btn_next'");
    target.btn_next = finder.castView(view, 2131558532, "field 'btn_next'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.nickname_et = null;
    target.btn_next = null;
  }
}
