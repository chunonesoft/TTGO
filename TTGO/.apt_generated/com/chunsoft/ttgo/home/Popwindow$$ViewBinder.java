// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Popwindow$$ViewBinder<T extends com.chunsoft.ttgo.home.Popwindow> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099787, "field 'pop_ok'");
    target.pop_ok = finder.castView(view, 2131099787, "field 'pop_ok'");
    view = finder.findRequiredView(source, 2131099783, "field 'pop_del'");
    target.pop_del = finder.castView(view, 2131099783, "field 'pop_del'");
    view = finder.findRequiredView(source, 2131099785, "field 'all_num'");
    target.all_num = finder.castView(view, 2131099785, "field 'all_num'");
    view = finder.findRequiredView(source, 2131099786, "field 'all_money'");
    target.all_money = finder.castView(view, 2131099786, "field 'all_money'");
    view = finder.findRequiredView(source, 2131099784, "field 'mylv'");
    target.mylv = finder.castView(view, 2131099784, "field 'mylv'");
    view = finder.findRequiredView(source, 2131099715, "field 'tv_money'");
    target.tv_money = finder.castView(view, 2131099715, "field 'tv_money'");
    view = finder.findRequiredView(source, 2131099699, "field 'iv_pic'");
    target.iv_pic = finder.castView(view, 2131099699, "field 'iv_pic'");
    view = finder.findRequiredView(source, 2131099782, "field 'tv_store'");
    target.tv_store = finder.castView(view, 2131099782, "field 'tv_store'");
    view = finder.findRequiredView(source, 2131099728, "field 'tv_content'");
    target.tv_content = finder.castView(view, 2131099728, "field 'tv_content'");
  }

  @Override public void unbind(T target) {
    target.pop_ok = null;
    target.pop_del = null;
    target.all_num = null;
    target.all_money = null;
    target.mylv = null;
    target.tv_money = null;
    target.iv_pic = null;
    target.tv_store = null;
    target.tv_content = null;
  }
}