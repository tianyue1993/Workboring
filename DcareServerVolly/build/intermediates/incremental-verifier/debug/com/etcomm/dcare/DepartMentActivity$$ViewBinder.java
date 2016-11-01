// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DepartMentActivity$$ViewBinder<T extends com.etcomm.dcare.DepartMentActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558558, "field 'department_tv'");
    target.department_tv = finder.castView(view, 2131558558, "field 'department_tv'");
    view = finder.findRequiredView(source, 2131558556, "field 'listview'");
    target.listview = finder.castView(view, 2131558556, "field 'listview'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.department_tv = null;
    target.listview = null;
  }
}
