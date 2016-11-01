// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TrendRecordActivity$$ViewBinder<T extends com.etcomm.dcare.TrendRecordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558411, "field 'webview'");
    target.webview = finder.castView(view, 2131558411, "field 'webview'");
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
  }

  @Override public void unbind(T target) {
    target.webview = null;
    target.titlebar = null;
  }
}
