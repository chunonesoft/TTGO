// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.home;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Home_Simple$$ViewBinder<T extends com.chunsoft.ttgo.home.Home_Simple> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099770, "field 'tv_more'");
    target.tv_more = finder.castView(view, 2131099770, "field 'tv_more'");
    view = finder.findRequiredView(source, 2131099769, "field 'my_gridview_kind'");
    target.my_gridview_kind = finder.castView(view, 2131099769, "field 'my_gridview_kind'");
    view = finder.findRequiredView(source, 2131099768, "field 'mAdView'");
    target.mAdView = finder.castView(view, 2131099768, "field 'mAdView'");
    view = finder.findRequiredView(source, 2131099896, "field 'll_search'");
    target.ll_search = finder.castView(view, 2131099896, "field 'll_search'");
    view = finder.findRequiredView(source, 2131099771, "field 'my_gridview_new'");
    target.my_gridview_new = finder.castView(view, 2131099771, "field 'my_gridview_new'");
  }

  @Override public void unbind(T target) {
    target.tv_more = null;
    target.my_gridview_kind = null;
    target.mAdView = null;
    target.ll_search = null;
    target.my_gridview_new = null;
  }
}
