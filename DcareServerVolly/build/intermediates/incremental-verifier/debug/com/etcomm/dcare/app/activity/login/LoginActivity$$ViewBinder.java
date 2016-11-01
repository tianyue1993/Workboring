// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.login.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
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
    view = finder.findRequiredView(source, 2131558591, "field 'iv_photo_default'");
    target.iv_photo_default = finder.castView(view, 2131558591, "field 'iv_photo_default'");
    view = finder.findRequiredView(source, 2131558588, "field 'btn_register' and method 'onClick'");
    target.btn_register = finder.castView(view, 2131558588, "field 'btn_register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
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
    view = finder.findRequiredView(source, 2131558595, "field 'bt_login_btn' and method 'onClick'");
    target.bt_login_btn = finder.castView(view, 2131558595, "field 'bt_login_btn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558594, "field 'tv_forget_pwd' and method 'onClick'");
    target.tv_forget_pwd = finder.castView(view, 2131558594, "field 'tv_forget_pwd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
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
  }

  @Override public void unbind(T target) {
    target.et_username = null;
    target.iv_photo_default = null;
    target.btn_register = null;
    target.et_passWord = null;
    target.bt_login_btn = null;
    target.tv_forget_pwd = null;
    target.iv_del_username = null;
    target.iv_del_password = null;
  }
}
