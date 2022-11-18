package com.tyt.driver.ui.forgot;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/9/17.
 */

public interface ForgotNavigator extends BaseView {

  void   openLoginActivity();
  void   OpenCountrycodeINvisible();
  void   OpenCountrycodevisible();
  String   getCountryCode();
  void   setCountryFlag(String flag);

  BaseActivity getAttachedContext();
}
