// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Show_All_Pro_FA$$ViewBinder<T extends com.chunsoft.ttgo.home.Show_All_Pro_FA> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099894, "field 'll_change_style'");
    target.ll_change_style = finder.castView(view, 2131099894, "field 'll_change_style'");
    view = finder.findRequiredView(source, 2131099895, "field 'iv_change_style'");
    target.iv_change_style = finder.castView(view, 2131099895, "field 'iv_change_style'");
    view = finder.findRequiredView(source, 2131099893, "field 'btn_search'");
    target.btn_search = finder.castView(view, 2131099893, "field 'btn_search'");
    view = finder.findRequiredView(source, 2131099897, "field 'mPullRefreshList'");
    target.mPullRefreshList = finder.castView(view, 2131099897, "field 'mPullRefreshList'");
    view = finder.findRequiredView(source, 2131099896, "field 'mPullRefresh'");
    target.mPullRefresh = finder.castView(view, 2131099896, "field 'mPullRefresh'");
    view = finder.findRequiredView(source, 2131099891, "field 'll_back'");
    target.ll_back = finder.castView(view, 2131099891, "field 'll_back'");
  }

  @Override public void unbind(T target) {
    target.ll_change_style = null;
    target.iv_change_style = null;
    target.btn_search = null;
    target.mPullRefreshList = null;
    target.mPullRefresh = null;
    target.ll_back = null;
  }
}
