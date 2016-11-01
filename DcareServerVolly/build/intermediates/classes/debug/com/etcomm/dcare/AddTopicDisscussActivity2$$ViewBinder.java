// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddTopicDisscussActivity2$$ViewBinder<T extends com.etcomm.dcare.AddTopicDisscussActivity2> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558537, "field 'picgridview'");
    target.picgridview = finder.castView(view, 2131558537, "field 'picgridview'");
    view = finder.findRequiredView(source, 2131558536, "field 'topicdisscuss' and method 'afterTextChanged_topicdisscuss'");
    target.topicdisscuss = finder.castView(view, 2131558536, "field 'topicdisscuss'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          target.afterTextChanged_topicdisscuss(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558535, "field 'title_right_tv'");
    target.title_right_tv = finder.castView(view, 2131558535, "field 'title_right_tv'");
    view = finder.findRequiredView(source, 2131558534, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131558534, "field 'tv_title'");
    view = finder.findRequiredView(source, 2131558538, "field 'addpictext'");
    target.addpictext = finder.castView(view, 2131558538, "field 'addpictext'");
    view = finder.findRequiredView(source, 2131558533, "field 'leftimage'");
    target.leftimage = finder.castView(view, 2131558533, "field 'leftimage'");
  }

  @Override public void unbind(T target) {
    target.picgridview = null;
    target.topicdisscuss = null;
    target.title_right_tv = null;
    target.tv_title = null;
    target.addpictext = null;
    target.leftimage = null;
  }
}
