// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.cart;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Submit_Order_FA$$ViewBinder<T extends com.chunsoft.ttgo.cart.Submit_Order_FA> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099751, "field 'tv_order_name'");
    target.tv_order_name = finder.castView(view, 2131099751, "field 'tv_order_name'");
    view = finder.findRequiredView(source, 2131099753, "field 'll_modify'");
    target.ll_modify = finder.castView(view, 2131099753, "field 'll_modify'");
    view = finder.findRequiredView(source, 2131099755, "field 'all_order_num'");
    target.all_order_num = finder.castView(view, 2131099755, "field 'all_order_num'");
    view = finder.findRequiredView(source, 2131099752, "field 'tv_order_phone'");
    target.tv_order_phone = finder.castView(view, 2131099752, "field 'tv_order_phone'");
    view = finder.findRequiredView(source, 2131099758, "field 'all_ph_money'");
    target.all_ph_money = finder.castView(view, 2131099758, "field 'all_ph_money'");
    view = finder.findRequiredView(source, 2131099759, "field 'btn_submit'");
    target.btn_submit = finder.castView(view, 2131099759, "field 'btn_submit'");
    view = finder.findRequiredView(source, 2131099756, "field 'all_order_money'");
    target.all_order_money = finder.castView(view, 2131099756, "field 'all_order_money'");
    view = finder.findRequiredView(source, 2131099757, "field 'all_ph_num'");
    target.all_ph_num = finder.castView(view, 2131099757, "field 'all_ph_num'");
    view = finder.findRequiredView(source, 2131099754, "field 'tv_order_address'");
    target.tv_order_address = finder.castView(view, 2131099754, "field 'tv_order_address'");
    view = finder.findRequiredView(source, 2131099909, "field 'tv_top_txtTitle'");
    target.tv_top_txtTitle = finder.castView(view, 2131099909, "field 'tv_top_txtTitle'");
  }

  @Override public void unbind(T target) {
    target.tv_order_name = null;
    target.ll_modify = null;
    target.all_order_num = null;
    target.tv_order_phone = null;
    target.all_ph_money = null;
    target.btn_submit = null;
    target.all_order_money = null;
    target.all_ph_num = null;
    target.tv_order_address = null;
    target.tv_top_txtTitle = null;
  }
}
