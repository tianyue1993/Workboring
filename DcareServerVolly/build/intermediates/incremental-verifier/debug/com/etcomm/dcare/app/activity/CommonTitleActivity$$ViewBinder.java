// Generated code from Butter Knife. Do not modify!
package com.etcomm.dcare.app.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommonTitleActivity$$ViewBinder<T extends com.etcomm.dcare.app.activity.CommonTitleActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559121, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131559121, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131559122, "field 'titleRight'");
    target.titleRight = finder.castView(view, 2131559122, "field 'titleRight'");
    view = finder.findRequiredView(source, 2131559120, "field 'mllView'");
    target.mllView = finder.castView(view, 2131559120, "field 'mllView'");
    view = finder.findRequiredView(source, 2131559123, "field 'mLeftBack' and method 'onClick'");
    target.mLeftBack = finder.castView(view, 2131559123, "field 'mLeftBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559125, "field 'ivSearch' and method 'onClick'");
    target.ivSearch = finder.castView(view, 2131559125, "field 'ivSearch'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559126, "field 'ivEdit' and method 'onClick'");
    target.ivEdit = finder.castView(view, 2131559126, "field 'ivEdit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559124, "field 'mLeftView'");
    target.mLeftView = finder.castView(view, 2131559124, "field 'mLeftView'");
    view = finder.findRequiredView(source, 2131559127, "field 'mTvBack'");
    target.mTvBack = finder.castView(view, 2131559127, "field 'mTvBack'");
    view = finder.findRequiredView(source, 2131558459, "field 'icon' and method 'onClick'");
    target.icon = finder.castView(view, 2131558459, "field 'icon'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559043, "field 'text' and method 'onClick'");
    target.text = finder.castView(view, 2131559043, "field 'text'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131559128, "field 'mRightView'");
    target.mRightView = finder.castView(view, 2131559128, "field 'mRightView'");
  }

  @Override public void unbind(T target) {
    target.mTitle = null;
    target.titleRight = null;
    target.mllView = null;
    target.mLeftBack = null;
    target.ivSearch = null;
    target.ivEdit = null;
    target.mLeftView = null;
    target.mTvBack = null;
    target.icon = null;
    target.text = null;
    target.mRightView = null;
  }
}
