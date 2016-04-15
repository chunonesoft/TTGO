// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.cart;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Cart_F_new1$$ViewBinder<T extends com.chunsoft.ttgo.cart.Cart_F_new1> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099701, "field 'tv_total_price'");
    target.tv_total_price = finder.castView(view, 2131099701, "field 'tv_total_price'");
    view = finder.findRequiredView(source, 2131099699, "field 'exListView'");
    target.exListView = finder.castView(view, 2131099699, "field 'exListView'");
    view = finder.findRequiredView(source, 2131099700, "field 'cb_check_all'");
    target.cb_check_all = finder.castView(view, 2131099700, "field 'cb_check_all'");
    view = finder.findRequiredView(source, 2131099702, "field 'tv_delete'");
    target.tv_delete = finder.castView(view, 2131099702, "field 'tv_delete'");
    view = finder.findRequiredView(source, 2131099703, "field 'tv_go_to_pay'");
    target.tv_go_to_pay = finder.castView(view, 2131099703, "field 'tv_go_to_pay'");
  }

  @Override public void unbind(T target) {
    target.tv_total_price = null;
    target.exListView = null;
    target.cb_check_all = null;
    target.tv_delete = null;
    target.tv_go_to_pay = null;
  }
}
