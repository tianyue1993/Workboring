// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity.setting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutUsActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.setting.AboutUsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar' and method 'onClick'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558512, "field 'app_version'");
    target.app_version = finder.castView(view, 2131558512, "field 'app_version'");
    view = finder.findRequiredView(source, 2131558511, "field 'app_name'");
    target.app_name = finder.castView(view, 2131558511, "field 'app_name'");
    view = finder.findRequiredView(source, 2131558525, "field 'version_update' and method 'onClick'");
    target.version_update = finder.castView(view, 2131558525, "field 'version_update'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.app_version = null;
    target.app_name = null;
    target.version_update = null;
  }
}
