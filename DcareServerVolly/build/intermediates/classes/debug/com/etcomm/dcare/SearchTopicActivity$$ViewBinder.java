// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SearchTopicActivity$$ViewBinder<T extends com.etcomm.dcare.SearchTopicActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558497, "field 'pulllistview'");
    target.pulllistview = finder.castView(view, 2131558497, "field 'pulllistview'");
    view = finder.findRequiredView(source, 2131558496, "field 'search_topic_et'");
    target.search_topic_et = finder.castView(view, 2131558496, "field 'search_topic_et'");
    view = finder.findRequiredView(source, 2131558495, "field 'back'");
    target.back = finder.castView(view, 2131558495, "field 'back'");
  }

  @Override public void unbind(T target) {
    target.pulllistview = null;
    target.search_topic_et = null;
    target.back = null;
  }
}
