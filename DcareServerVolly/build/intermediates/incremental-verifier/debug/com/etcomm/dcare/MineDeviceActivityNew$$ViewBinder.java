// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MineDeviceActivityNew$$ViewBinder<T extends com.etcomm.dcare.MineDeviceActivityNew> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558499, "field 'titlebar'");
    target.titlebar = finder.castView(view, 2131558499, "field 'titlebar'");
    view = finder.findRequiredView(source, 2131558597, "field 'framelayout'");
    target.framelayout = finder.castView(view, 2131558597, "field 'framelayout'");
    view = finder.findRequiredView(source, 2131558598, "field 'searchbluedevices'");
    target.searchbluedevices = finder.castView(view, 2131558598, "field 'searchbluedevices'");
    view = finder.findRequiredView(source, 2131558599, "field 'search_device'");
    target.search_device = finder.castView(view, 2131558599, "field 'search_device'");
    view = finder.findRequiredView(source, 2131558556, "field 'listview'");
    target.listview = finder.castView(view, 2131558556, "field 'listview'");
    view = finder.findRequiredView(source, 2131558603, "field 'bindeddevice'");
    target.bindeddevice = finder.castView(view, 2131558603, "field 'bindeddevice'");
    view = finder.findRequiredView(source, 2131558604, "field 'device_code'");
    target.device_code = finder.castView(view, 2131558604, "field 'device_code'");
    view = finder.findRequiredView(source, 2131558605, "field 'device_status'");
    target.device_status = finder.castView(view, 2131558605, "field 'device_status'");
    view = finder.findRequiredView(source, 2131558602, "field 'tvConnGsm' and method 'onClick'");
    target.tvConnGsm = finder.castView(view, 2131558602, "field 'tvConnGsm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558601, "field 'etGsm'");
    target.etGsm = finder.castView(view, 2131558601, "field 'etGsm'");
    view = finder.findRequiredView(source, 2131558606, "field 'unbinddevice'");
    target.unbinddevice = finder.castView(view, 2131558606, "field 'unbinddevice'");
  }

  @Override public void unbind(T target) {
    target.titlebar = null;
    target.framelayout = null;
    target.searchbluedevices = null;
    target.search_device = null;
    target.listview = null;
    target.bindeddevice = null;
    target.device_code = null;
    target.device_status = null;
    target.tvConnGsm = null;
    target.etGsm = null;
    target.unbinddevice = null;
  }
}
