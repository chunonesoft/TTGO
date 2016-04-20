// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.myself;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyOrder_All$$ViewBinder<T extends com.chunsoft.ttgo.myself.MyOrder_All> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099811, "field 'xlv_all'");
    target.xlv_all = finder.castView(view, 2131099811, "field 'xlv_all'");
  }

  @Override public void unbind(T target) {
    target.xlv_all = null;
  }
}
