// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangePasswordActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.login.ChangePasswordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558547, "field 'old_pwd'");
    target.old_pwd = finder.castView(view, 2131558547, "field 'old_pwd'");
    view = finder.findRequiredView(source, 2131558550, "field 'new_pwd'");
    target.new_pwd = finder.castView(view, 2131558550, "field 'new_pwd'");
    view = finder.findRequiredView(source, 2131558552, "field 'renew_pwd'");
    target.renew_pwd = finder.castView(view, 2131558552, "field 'renew_pwd'");
    view = finder.findRequiredView(source, 2131558553, "field 'isShowPwd'");
    target.isShowPwd = finder.castView(view, 2131558553, "field 'isShowPwd'");
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
    view = finder.findRequiredView(source, 2131558546, "field 'old_psd_li'");
    target.old_psd_li = finder.castView(view, 2131558546, "field 'old_psd_li'");
    view = finder.findRequiredView(source, 2131558549, "field 'new_pwd_tv'");
    target.new_pwd_tv = finder.castView(view, 2131558549, "field 'new_pwd_tv'");
    view = finder.findRequiredView(source, 2131558551, "field 'renew_pwd_tv'");
    target.renew_pwd_tv = finder.castView(view, 2131558551, "field 'renew_pwd_tv'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.old_pwd = null;
    target.new_pwd = null;
    target.renew_pwd = null;
    target.isShowPwd = null;
    target.btn_next = null;
    target.old_psd_li = null;
    target.new_pwd_tv = null;
    target.renew_pwd_tv = null;
  }
}
