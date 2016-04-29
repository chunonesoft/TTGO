// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Home_Simple$ViewHolder$$ViewBinder<T extends com.chunsoft.ttgo.home.Home_Simple.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099704, "field 'tv_price'");
    target.tv_price = finder.castView(view, 2131099704, "field 'tv_price'");
    view = finder.findRequiredView(source, 2131099784, "field 'tv_sale'");
    target.tv_sale = finder.castView(view, 2131099784, "field 'tv_sale'");
    view = finder.findRequiredView(source, 2131099783, "field 'image'");
    target.image = finder.castView(view, 2131099783, "field 'image'");
    view = finder.findRequiredView(source, 2131099766, "field 'tv_content'");
    target.tv_content = finder.castView(view, 2131099766, "field 'tv_content'");
  }

  @Override public void unbind(T target) {
    target.tv_price = null;
    target.tv_sale = null;
    target.image = null;
    target.tv_content = null;
  }
}
