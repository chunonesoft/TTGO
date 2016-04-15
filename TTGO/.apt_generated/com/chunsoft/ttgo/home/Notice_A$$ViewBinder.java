// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Notice_A$$ViewBinder<T extends com.chunsoft.ttgo.home.Notice_A> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099792, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131099792, "field 'tv_title'");
    view = finder.findRequiredView(source, 2131099742, "field 'x_lv'");
    target.x_lv = finder.castView(view, 2131099742, "field 'x_lv'");
  }

  @Override public void unbind(T target) {
    target.tv_title = null;
    target.x_lv = null;
  }
}
