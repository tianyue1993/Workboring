// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ForgotPasswordActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.login.ForgotPasswordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558579, "field 'et_username'");
    target.et_username = finder.castView(view, 2131558579, "field 'et_username'");
    view = finder.findRequiredView(source, 2131558580, "field 'et_passWord'");
    target.et_passWord = finder.castView(view, 2131558580, "field 'et_passWord'");
    view = finder.findRequiredView(source, 2131558581, "field 'getcode' and method 'onClick'");
    target.getcode = finder.castView(view, 2131558581, "field 'getcode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558582, "field 'next' and method 'onClick'");
    target.next = finder.castView(view, 2131558582, "field 'next'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558578, "field 'tv_username'");
    target.tv_username = finder.castView(view, 2131558578, "field 'tv_username'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.et_username = null;
    target.et_passWord = null;
    target.getcode = null;
    target.next = null;
    target.tv_username = null;
  }
}
