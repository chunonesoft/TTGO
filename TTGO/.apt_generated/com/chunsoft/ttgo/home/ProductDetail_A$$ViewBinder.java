// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ProductDetail_A$$ViewBinder<T extends com.chunsoft.ttgo.home.ProductDetail_A> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099847, "field 'all_choice_layout'");
    target.all_choice_layout = finder.castView(view, 2131099847, "field 'all_choice_layout'");
    view = finder.findRequiredView(source, 2131099704, "field 'tv_price'");
    target.tv_price = finder.castView(view, 2131099704, "field 'tv_price'");
    view = finder.findRequiredView(source, 2131099842, "field 'iv_baby_collection'");
    target.iv_baby_collection = finder.castView(view, 2131099842, "field 'iv_baby_collection'");
    view = finder.findRequiredView(source, 2131099679, "field 'tv_name'");
    target.tv_name = finder.castView(view, 2131099679, "field 'tv_name'");
    view = finder.findRequiredView(source, 2131099821, "field 'iv_kf'");
    target.iv_kf = finder.castView(view, 2131099821, "field 'iv_kf'");
    view = finder.findRequiredView(source, 2131099846, "field 'buy_now'");
    target.buy_now = finder.castView(view, 2131099846, "field 'buy_now'");
    view = finder.findRequiredView(source, 2131099844, "field 'tv_storenum'");
    target.tv_storenum = finder.castView(view, 2131099844, "field 'tv_storenum'");
    view = finder.findRequiredView(source, 2131099845, "field 'put_in'");
    target.put_in = finder.castView(view, 2131099845, "field 'put_in'");
    view = finder.findRequiredView(source, 2131099843, "field 'tv_salenum'");
    target.tv_salenum = finder.castView(view, 2131099843, "field 'tv_salenum'");
  }

  @Override public void unbind(T target) {
    target.all_choice_layout = null;
    target.tv_price = null;
    target.iv_baby_collection = null;
    target.tv_name = null;
    target.iv_kf = null;
    target.buy_now = null;
    target.tv_storenum = null;
    target.put_in = null;
    target.tv_salenum = null;
  }
}
