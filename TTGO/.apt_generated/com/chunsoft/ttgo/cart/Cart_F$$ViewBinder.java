// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.cart;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Cart_F$$ViewBinder<T extends com.chunsoft.ttgo.cart.Cart_F> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099695, "field 'tv_cart_select_num'");
    target.tv_cart_select_num = finder.castView(view, 2131099695, "field 'tv_cart_select_num'");
    view = finder.findRequiredView(source, 2131099696, "field 'mFavorite'");
    target.mFavorite = finder.castView(view, 2131099696, "field 'mFavorite'");
    view = finder.findRequiredView(source, 2131099694, "field 'tv_cart_total'");
    target.tv_cart_total = finder.castView(view, 2131099694, "field 'tv_cart_total'");
    view = finder.findRequiredView(source, 2131099691, "field 'mCheckAll'");
    target.mCheckAll = finder.castView(view, 2131099691, "field 'mCheckAll'");
    view = finder.findRequiredView(source, 2131099697, "field 'mDelete'");
    target.mDelete = finder.castView(view, 2131099697, "field 'mDelete'");
    view = finder.findRequiredView(source, 2131099692, "field 'cart_rl_allprie_total'");
    target.cart_rl_allprie_total = finder.castView(view, 2131099692, "field 'cart_rl_allprie_total'");
    view = finder.findRequiredView(source, 2131099834, "field 'tv_top_edit'");
    target.tv_top_edit = finder.castView(view, 2131099834, "field 'tv_top_edit'");
    view = finder.findRequiredView(source, 2131099698, "field 'content_view'");
    target.content_view = finder.castView(view, 2131099698, "field 'content_view'");
  }

  @Override public void unbind(T target) {
    target.tv_cart_select_num = null;
    target.mFavorite = null;
    target.tv_cart_total = null;
    target.mCheckAll = null;
    target.mDelete = null;
    target.cart_rl_allprie_total = null;
    target.tv_top_edit = null;
    target.content_view = null;
  }
}
