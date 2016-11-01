// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RelateWorkNumberActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.RelateWorkNumberActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558693, "field 'enternumber_li'");
    target.enternumber_li = finder.castView(view, 2131558693, "field 'enternumber_li'");
    view = finder.findRequiredView(source, 2131558694, "field 'enterworknumber_et'");
    target.enterworknumber_et = finder.castView(view, 2131558694, "field 'enterworknumber_et'");
    view = finder.findRequiredView(source, 2131558696, "field 'alreadyentered_tv'");
    target.alreadyentered_tv = finder.castView(view, 2131558696, "field 'alreadyentered_tv'");
    view = finder.findRequiredView(source, 2131558699, "field 'prompt'");
    target.prompt = finder.castView(view, 2131558699, "field 'prompt'");
    view = finder.findRequiredView(source, 2131558698, "field 'commit_btn' and method 'onClick'");
    target.commit_btn = finder.castView(view, 2131558698, "field 'commit_btn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558695, "field 'line'");
    target.line = view;
    view = finder.findRequiredView(source, 2131558697, "field 'job_number'");
    target.job_number = finder.castView(view, 2131558697, "field 'job_number'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.enternumber_li = null;
    target.enterworknumber_et = null;
    target.alreadyentered_tv = null;
    target.prompt = null;
    target.commit_btn = null;
    target.line = null;
    target.job_number = null;
  }
}
