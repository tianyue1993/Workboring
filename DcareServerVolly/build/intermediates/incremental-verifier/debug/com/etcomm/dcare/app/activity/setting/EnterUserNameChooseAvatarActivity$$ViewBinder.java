// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class EnterUserNameChooseAvatarActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.EnterUserNameChooseAvatarActivity> implements ViewBinder<T> {
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
    view = finder.findRequiredView(source, 2131558799, "field 'et_nickyname' and method 'afterTextChanged_et_nickyname'");
    target.et_nickyname = finder.castView(view, 2131558799, "field 'et_nickyname'");
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
          target.afterTextChanged_et_nickyname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558802, "field 'et_worknumber' and method 'afterTextChanged_et_worknumber'");
    target.et_worknumber = finder.castView(view, 2131558802, "field 'et_worknumber'");
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
          target.afterTextChanged_et_worknumber(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558796, "field 'sex_switch'");
    target.sex_switch = finder.castView(view, 2131558796, "field 'sex_switch'");
    view = finder.findRequiredView(source, 2131558805, "field 'btn_enterusernamechooseavatarnext'");
    target.btn_enterusernamechooseavatarnext = finder.castView(view, 2131558805, "field 'btn_enterusernamechooseavatarnext'");
    view = finder.findRequiredView(source, 2131558797, "field 'iv_del_username1' and method 'onClick'");
    target.iv_del_username1 = finder.castView(view, 2131558797, "field 'iv_del_username1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558800, "field 'iv_del_username2' and method 'onClick'");
    target.iv_del_username2 = finder.castView(view, 2131558800, "field 'iv_del_username2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558803, "field 'iv_del_username3' and method 'onClick'");
    target.iv_del_username3 = finder.castView(view, 2131558803, "field 'iv_del_username3'");
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
    target.et_nickyname = null;
    target.et_worknumber = null;
    target.sex_switch = null;
    target.btn_enterusernamechooseavatarnext = null;
    target.iv_del_username1 = null;
    target.iv_del_username2 = null;
    target.iv_del_username3 = null;
  }
}
