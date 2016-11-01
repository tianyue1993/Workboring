// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyPointsDetailActivity$$ViewBinder<T extends com.etcomm.dcare.MyPointsDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558533, "field 'leftimage'");
    target.leftimage = finder.castView(view, 2131558533, "field 'leftimage'");
    view = finder.findRequiredView(source, 2131558534, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131558534, "field 'tv_title'");
    view = finder.findRequiredView(source, 2131558619, "field 'title_right_iv'");
    target.title_right_iv = finder.castView(view, 2131558619, "field 'title_right_iv'");
    view = finder.findRequiredView(source, 2131558620, "field 'mytotalpoints'");
    target.mytotalpoints = finder.castView(view, 2131558620, "field 'mytotalpoints'");
    view = finder.findRequiredView(source, 2131558621, "field 'howtoearnpoints'");
    target.howtoearnpoints = finder.castView(view, 2131558621, "field 'howtoearnpoints'");
    view = finder.findRequiredView(source, 2131558622, "field 'refreshlist'");
    target.refreshlist = finder.castView(view, 2131558622, "field 'refreshlist'");
  }

  @Override public void unbind(T target) {
    target.leftimage = null;
    target.tv_title = null;
    target.title_right_iv = null;
    target.mytotalpoints = null;
    target.howtoearnpoints = null;
    target.refreshlist = null;
  }
}
