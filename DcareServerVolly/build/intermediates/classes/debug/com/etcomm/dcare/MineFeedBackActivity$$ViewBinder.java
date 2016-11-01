// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MineFeedBackActivity$$ViewBinder<T extends com.etcomm.dcare.MineFeedBackActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558498, "field 'feedback_commit'");
    target.feedback_commit = finder.castView(view, 2131558498, "field 'feedback_commit'");
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558500, "field 'feedback_contact_tv'");
    target.feedback_contact_tv = finder.castView(view, 2131558500, "field 'feedback_contact_tv'");
    view = finder.findRequiredView(source, 2131558501, "field 'feedback_tv'");
    target.feedback_tv = finder.castView(view, 2131558501, "field 'feedback_tv'");
    view = finder.findRequiredView(source, 2131558502, "field 'contact_info_tv'");
    target.contact_info_tv = finder.castView(view, 2131558502, "field 'contact_info_tv'");
  }

  @Override public void unbind(T target) {
    target.feedback_commit = null;
    target.titlebar = null;
    target.feedback_contact_tv = null;
    target.feedback_tv = null;
    target.contact_info_tv = null;
  }
}
