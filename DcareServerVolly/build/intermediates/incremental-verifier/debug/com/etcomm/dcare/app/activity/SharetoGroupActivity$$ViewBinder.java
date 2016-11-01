// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SharetoGroupActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.SharetoGroupActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558702, "field 'attentionpulllistview'");
    target.attentionpulllistview = finder.castView(view, 2131558702, "field 'attentionpulllistview'");
    view = finder.findRequiredView(source, 2131558700, "field 'search_healthnew_et'");
    target.search_healthnew_et = finder.castView(view, 2131558700, "field 'search_healthnew_et'");
    view = finder.findRequiredView(source, 2131558658, "field 'cancel'");
    target.cancel = finder.castView(view, 2131558658, "field 'cancel'");
    view = finder.findRequiredView(source, 2131558701, "field 'attention'");
    target.attention = finder.castView(view, 2131558701, "field 'attention'");
  }

  @Override public void unbind(T target) {
    target.attentionpulllistview = null;
    target.search_healthnew_et = null;
    target.cancel = null;
    target.attention = null;
  }
}
