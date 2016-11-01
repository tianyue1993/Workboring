// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddNewTopicActivity$$ViewBinder<T extends com.etcomm.dcare.AddNewTopicActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558529, "field 'newtopic_et'");
    target.newtopic_et = finder.castView(view, 2131558529, "field 'newtopic_et'");
    view = finder.findRequiredView(source, 2131558530, "field 'newtopic_sucess_tv'");
    target.newtopic_sucess_tv = finder.castView(view, 2131558530, "field 'newtopic_sucess_tv'");
    view = finder.findRequiredView(source, 2131558531, "field 'newtopic_already_tv'");
    target.newtopic_already_tv = finder.castView(view, 2131558531, "field 'newtopic_already_tv'");
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
    target.newtopic_et = null;
    target.newtopic_sucess_tv = null;
    target.newtopic_already_tv = null;
    target.btn_next = null;
  }
}
