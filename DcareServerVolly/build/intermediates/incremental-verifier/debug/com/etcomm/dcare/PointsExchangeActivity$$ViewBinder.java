// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PointsExchangeActivity$$ViewBinder<T extends com.etcomm.dcare.PointsExchangeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558686, "field 'mypoints'");
    target.mypoints = finder.castView(view, 2131558686, "field 'mypoints'");
    view = finder.findRequiredView(source, 2131558687, "field 'myexchange'");
    target.myexchange = finder.castView(view, 2131558687, "field 'myexchange'");
    view = finder.findRequiredView(source, 2131558622, "field 'refreshlist'");
    target.refreshlist = finder.castView(view, 2131558622, "field 'refreshlist'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.mypoints = null;
    target.myexchange = null;
    target.refreshlist = null;
  }
}
