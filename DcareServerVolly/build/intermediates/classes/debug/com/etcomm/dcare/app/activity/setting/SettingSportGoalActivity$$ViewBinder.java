// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingSportGoalActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.SettingSportGoalActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558729, "field 'goal_avator'");
    target.goal_avator = finder.castView(view, 2131558729, "field 'goal_avator'");
    view = finder.findRequiredView(source, 2131558730, "field 'goal_age_tv'");
    target.goal_age_tv = finder.castView(view, 2131558730, "field 'goal_age_tv'");
    view = finder.findRequiredView(source, 2131558728, "field 'nickname'");
    target.nickname = finder.castView(view, 2131558728, "field 'nickname'");
    view = finder.findRequiredView(source, 2131558731, "field 'goal_height_tv'");
    target.goal_height_tv = finder.castView(view, 2131558731, "field 'goal_height_tv'");
    view = finder.findRequiredView(source, 2131558732, "field 'goal_weight_tv'");
    target.goal_weight_tv = finder.castView(view, 2131558732, "field 'goal_weight_tv'");
    view = finder.findRequiredView(source, 2131558733, "field 'progressbar'");
    target.progressbar = finder.castView(view, 2131558733, "field 'progressbar'");
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
    target.goal_avator = null;
    target.goal_age_tv = null;
    target.nickname = null;
    target.goal_height_tv = null;
    target.goal_weight_tv = null;
    target.progressbar = null;
    target.btn_next = null;
  }
}
