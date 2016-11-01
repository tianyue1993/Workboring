// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DisscussConentListActivity$$ViewBinder<T extends com.etcomm.dcare.DisscussConentListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558560, "field 'comment_et'");
    target.comment_et = finder.castView(view, 2131558560, "field 'comment_et'");
    view = finder.findRequiredView(source, 2131558559, "field 'bottom_bar'");
    target.bottom_bar = finder.castView(view, 2131558559, "field 'bottom_bar'");
    view = finder.findRequiredView(source, 2131558561, "field 'comment_send' and method 'onClick'");
    target.comment_send = finder.castView(view, 2131558561, "field 'comment_send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558562, "field 'commmentpulllist'");
    target.commmentpulllist = finder.castView(view, 2131558562, "field 'commmentpulllist'");
    view = finder.findRequiredView(source, 2131558563, "field 'emptyview'");
    target.emptyview = view;
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.comment_et = null;
    target.bottom_bar = null;
    target.comment_send = null;
    target.commmentpulllist = null;
    target.emptyview = null;
  }
}
