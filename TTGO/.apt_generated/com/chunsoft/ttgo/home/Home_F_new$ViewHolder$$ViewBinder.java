// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Home_F_new$ViewHolder$$ViewBinder<T extends com.chunsoft.ttgo.home.Home_F_new.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099766, "field 'tv_content'");
    target.tv_content = finder.castView(view, 2131099766, "field 'tv_content'");
    view = finder.findRequiredView(source, 2131099777, "field 'tv_sale'");
    target.tv_sale = finder.castView(view, 2131099777, "field 'tv_sale'");
    view = finder.findRequiredView(source, 2131099704, "field 'tv_price'");
    target.tv_price = finder.castView(view, 2131099704, "field 'tv_price'");
    view = finder.findRequiredView(source, 2131099776, "field 'image'");
    target.image = finder.castView(view, 2131099776, "field 'image'");
  }

  @Override public void unbind(T target) {
    target.tv_content = null;
    target.tv_sale = null;
    target.tv_price = null;
    target.image = null;
  }
}
