// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Bill_History$$ViewBinder<T extends com.chunsoft.ttgo.home.Bill_History> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099699, "field 'all_price'");
    target.all_price = finder.castView(view, 2131099699, "field 'all_price'");
    view = finder.findRequiredView(source, 2131099700, "field 'xlv_bill'");
    target.xlv_bill = finder.castView(view, 2131099700, "field 'xlv_bill'");
  }

  @Override public void unbind(T target) {
    target.all_price = null;
    target.xlv_bill = null;
  }
}
