// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Popwindow$ViewHolder$$ViewBinder<T extends com.chunsoft.ttgo.home.Popwindow.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099740, "field 'pop_reduce'");
    target.pop_reduce = finder.castView(view, 2131099740, "field 'pop_reduce'");
    view = finder.findRequiredView(source, 2131099741, "field 'pop_num'");
    target.pop_num = finder.castView(view, 2131099741, "field 'pop_num'");
    view = finder.findRequiredView(source, 2131099739, "field 'tv_size'");
    target.tv_size = finder.castView(view, 2131099739, "field 'tv_size'");
    view = finder.findRequiredView(source, 2131099742, "field 'pop_add'");
    target.pop_add = finder.castView(view, 2131099742, "field 'pop_add'");
    view = finder.findRequiredView(source, 2131099738, "field 'cb'");
    target.cb = finder.castView(view, 2131099738, "field 'cb'");
    view = finder.findRequiredView(source, 2131099709, "field 'tv_money'");
    target.tv_money = finder.castView(view, 2131099709, "field 'tv_money'");
  }

  @Override public void unbind(T target) {
    target.pop_reduce = null;
    target.pop_num = null;
    target.tv_size = null;
    target.pop_add = null;
    target.cb = null;
    target.tv_money = null;
  }
}
