// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SearchHealthNewsActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.SearchHealthNewsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558700, "field 'search_healthnew_et'");
    target.search_healthnew_et = finder.castView(view, 2131558700, "field 'search_healthnew_et'");
    view = finder.findRequiredView(source, 2131558497, "field 'pulllistview'");
    target.pulllistview = finder.castView(view, 2131558497, "field 'pulllistview'");
    view = finder.findRequiredView(source, 2131558658, "field 'cancel'");
    target.cancel = finder.castView(view, 2131558658, "field 'cancel'");
  }

  @Override public void unbind(T target) {
    target.search_healthnew_et = null;
    target.pulllistview = null;
    target.cancel = null;
  }
}
