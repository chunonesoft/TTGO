// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Popwindow$$ViewBinder<T extends com.chunsoft.ttgo.home.Popwindow> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099847, "field 'all_num'");
    target.all_num = finder.castView(view, 2131099847, "field 'all_num'");
    view = finder.findRequiredView(source, 2131099846, "field 'mylv'");
    target.mylv = finder.castView(view, 2131099846, "field 'mylv'");
    view = finder.findRequiredView(source, 2131099844, "field 'tv_store'");
    target.tv_store = finder.castView(view, 2131099844, "field 'tv_store'");
    view = finder.findRequiredView(source, 2131099848, "field 'all_money'");
    target.all_money = finder.castView(view, 2131099848, "field 'all_money'");
    view = finder.findRequiredView(source, 2131099845, "field 'pop_del'");
    target.pop_del = finder.castView(view, 2131099845, "field 'pop_del'");
    view = finder.findRequiredView(source, 2131099709, "field 'tv_money'");
    target.tv_money = finder.castView(view, 2131099709, "field 'tv_money'");
    view = finder.findRequiredView(source, 2131099724, "field 'iv_pic'");
    target.iv_pic = finder.castView(view, 2131099724, "field 'iv_pic'");
    view = finder.findRequiredView(source, 2131099766, "field 'tv_content'");
    target.tv_content = finder.castView(view, 2131099766, "field 'tv_content'");
    view = finder.findRequiredView(source, 2131099849, "field 'pop_ok'");
    target.pop_ok = finder.castView(view, 2131099849, "field 'pop_ok'");
  }

  @Override public void unbind(T target) {
    target.all_num = null;
    target.mylv = null;
    target.tv_store = null;
    target.all_money = null;
    target.pop_del = null;
    target.tv_money = null;
    target.iv_pic = null;
    target.tv_content = null;
    target.pop_ok = null;
  }
}
