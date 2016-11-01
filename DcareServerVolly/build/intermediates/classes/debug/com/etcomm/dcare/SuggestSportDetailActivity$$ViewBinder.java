// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SuggestSportDetailActivity$$ViewBinder<T extends com.etcomm.dcare.SuggestSportDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131559107, "field 'sugget_sports_image_iv'");
    target.sugget_sports_image_iv = finder.castView(view, 2131559107, "field 'sugget_sports_image_iv'");
    view = finder.findRequiredView(source, 2131559111, "field 'sport_signup' and method 'onClick'");
    target.sport_signup = finder.castView(view, 2131559111, "field 'sport_signup'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559108, "field 'sport_time_tv'");
    target.sport_time_tv = finder.castView(view, 2131559108, "field 'sport_time_tv'");
    view = finder.findRequiredView(source, 2131559109, "field 'sport_deadline'");
    target.sport_deadline = finder.castView(view, 2131559109, "field 'sport_deadline'");
    view = finder.findRequiredView(source, 2131559110, "field 'sport_entrynumber'");
    target.sport_entrynumber = finder.castView(view, 2131559110, "field 'sport_entrynumber'");
    view = finder.findRequiredView(source, 2131559112, "field 'sport_detail'");
    target.sport_detail = finder.castView(view, 2131559112, "field 'sport_detail'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.sugget_sports_image_iv = null;
    target.sport_signup = null;
    target.sport_time_tv = null;
    target.sport_deadline = null;
    target.sport_entrynumber = null;
    target.sport_detail = null;
  }
}
