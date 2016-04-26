// Generated code from Butter Knife. Do not modify!
package com.chunsoft.ttgo.myself;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Add_Adress$$ViewBinder<T extends com.chunsoft.ttgo.myself.Add_Adress> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099670, "field 'et_phone'");
    target.et_phone = finder.castView(view, 2131099670, "field 'et_phone'");
    view = finder.findRequiredView(source, 2131099671, "field 'et_postalcode'");
    target.et_postalcode = finder.castView(view, 2131099671, "field 'et_postalcode'");
    view = finder.findRequiredView(source, 2131099669, "field 'et_name'");
    target.et_name = finder.castView(view, 2131099669, "field 'et_name'");
    view = finder.findRequiredView(source, 2131099673, "field 'et_detailadress'");
    target.et_detailadress = finder.castView(view, 2131099673, "field 'et_detailadress'");
    view = finder.findRequiredView(source, 2131099831, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131099831, "field 'tv_title'");
    view = finder.findRequiredView(source, 2131099675, "field 'll_choose_adress'");
    target.ll_choose_adress = finder.castView(view, 2131099675, "field 'll_choose_adress'");
    view = finder.findRequiredView(source, 2131099672, "field 'tv_adress'");
    target.tv_adress = finder.castView(view, 2131099672, "field 'tv_adress'");
    view = finder.findRequiredView(source, 2131099674, "field 'btn_save'");
    target.btn_save = finder.castView(view, 2131099674, "field 'btn_save'");
  }

  @Override public void unbind(T target) {
    target.et_phone = null;
    target.et_postalcode = null;
    target.et_name = null;
    target.et_detailadress = null;
    target.tv_title = null;
    target.ll_choose_adress = null;
    target.tv_adress = null;
    target.btn_save = null;
  }
}
