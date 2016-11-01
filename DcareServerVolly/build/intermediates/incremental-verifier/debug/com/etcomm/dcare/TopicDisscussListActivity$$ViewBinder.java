// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TopicDisscussListActivity$$ViewBinder<T extends com.etcomm.dcare.TopicDisscussListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558745, "field 'adddisscuss_iv' and method 'onClick'");
    target.adddisscuss_iv = finder.castView(view, 2131558745, "field 'adddisscuss_iv'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar' and method 'onClick'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558497, "field 'pulllistview'");
    target.pulllistview = finder.castView(view, 2131558497, "field 'pulllistview'");
    view = finder.findRequiredView(source, 2131558744, "field '_root'");
    target._root = finder.castView(view, 2131558744, "field '_root'");
    view = finder.findRequiredView(source, 2131558563, "field 'emptyview'");
    target.emptyview = view;
  }

  @Override public void unbind(T target) {
    target.adddisscuss_iv = null;
    target.titlebar = null;
    target.pulllistview = null;
    target._root = null;
    target.emptyview = null;
  }
}
