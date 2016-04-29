// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.group;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Group_F$$ViewBinder<T extends com.chunsoft.ttgo.group.Group_F> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099780, "field 'x_lv'");
    target.x_lv = finder.castView(view, 2131099780, "field 'x_lv'");
    view = finder.findRequiredView(source, 2131099842, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131099842, "field 'tv_title'");
  }

  @Override public void unbind(T target) {
    target.x_lv = null;
    target.tv_title = null;
  }
}
