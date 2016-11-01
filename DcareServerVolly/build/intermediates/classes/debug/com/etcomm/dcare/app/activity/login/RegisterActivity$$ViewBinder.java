// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.login.RegisterActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558579, "field 'et_username' and method 'afterTextChanged_username'");
    target.et_username = finder.castView(view, 2131558579, "field 'et_username'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          target.afterTextChanged_username(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558580, "field 'et_passWord' and method 'afterTextChanged_password'");
    target.et_passWord = finder.castView(view, 2131558580, "field 'et_passWord'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          target.afterTextChanged_password(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558592, "field 'iv_del_username' and method 'onClick'");
    target.iv_del_username = finder.castView(view, 2131558592, "field 'iv_del_username'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558593, "field 'iv_del_password' and method 'onClick'");
    target.iv_del_password = finder.castView(view, 2131558593, "field 'iv_del_password'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
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
    view = finder.findRequiredView(source, 2131558692, "field 'username_tv'");
    target.username_tv = finder.castView(view, 2131558692, "field 'username_tv'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.et_username = null;
    target.et_passWord = null;
    target.iv_del_username = null;
    target.iv_del_password = null;
    target.getcode = null;
    target.next = null;
    target.username_tv = null;
  }
}
