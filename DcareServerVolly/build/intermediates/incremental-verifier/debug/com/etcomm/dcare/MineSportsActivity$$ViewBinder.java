// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MineSportsActivity$$ViewBinder<T extends com.etcomm.dcare.MineSportsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558556, "field 'listview'");
    target.listview = finder.castView(view, 2131558556, "field 'listview'");
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
  }

  @Override public void unbind(T target) {
    target.listview = null;
    target.titlebar = null;
  }
}
