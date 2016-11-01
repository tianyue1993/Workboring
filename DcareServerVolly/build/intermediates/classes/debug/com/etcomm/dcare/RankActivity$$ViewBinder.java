// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RankActivity$$ViewBinder<T extends com.etcomm.dcare.RankActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558533, "field 'leftimage' and method 'onClick'");
    target.leftimage = finder.castView(view, 2131558533, "field 'leftimage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558619, "field 'title_right_iv'");
    target.title_right_iv = finder.castView(view, 2131558619, "field 'title_right_iv'");
    view = finder.findRequiredView(source, 2131558688, "field 'leftindicator'");
    target.leftindicator = view;
    view = finder.findRequiredView(source, 2131558689, "field 'rightindicator'");
    target.rightindicator = view;
    view = finder.findRequiredView(source, 2131558690, "field 'around_tab_attationed'");
    target.around_tab_attationed = finder.castView(view, 2131558690, "field 'around_tab_attationed'");
    view = finder.findRequiredView(source, 2131558691, "field 'around_tab_notattationed'");
    target.around_tab_notattationed = finder.castView(view, 2131558691, "field 'around_tab_notattationed'");
    view = finder.findRequiredView(source, 2131558411, "field 'webview'");
    target.webview = finder.castView(view, 2131558411, "field 'webview'");
  }

  @Override public void unbind(T target) {
    target.leftimage = null;
    target.title_right_iv = null;
    target.leftindicator = null;
    target.rightindicator = null;
    target.around_tab_attationed = null;
    target.around_tab_notattationed = null;
    target.webview = null;
  }
}
