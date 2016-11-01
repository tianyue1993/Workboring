// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MsgSettingActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.MsgSettingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558610, "field 'msgsetting_msg_switch'");
    target.msgsetting_msg_switch = finder.castView(view, 2131558610, "field 'msgsetting_msg_switch'");
    view = finder.findRequiredView(source, 2131558613, "field 'msgsetting_msgdianzan_switch' and method 'onCheckedChanged'");
    target.msgsetting_msgdianzan_switch = finder.castView(view, 2131558613, "field 'msgsetting_msgdianzan_switch'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.onCheckedChanged(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131558616, "field 'msgsetting_msgpinglun_switch' and method 'onCheckedChanged'");
    target.msgsetting_msgpinglun_switch = finder.castView(view, 2131558616, "field 'msgsetting_msgpinglun_switch'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.onCheckedChanged(p0, p1);
        }
      });
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.msgsetting_msg_switch = null;
    target.msgsetting_msgdianzan_switch = null;
    target.msgsetting_msgpinglun_switch = null;
  }
}
