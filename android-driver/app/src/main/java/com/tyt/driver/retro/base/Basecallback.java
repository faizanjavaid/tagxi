package com.tyt.driver.retro.base;

import com.tyt.driver.utilz.exception.CustomException;

/*
  Created by root on 9/27/17.
 */

/** Gives API callbacks **/
public interface Basecallback<T>   {

    void onSuccessfulApi(long taskId, T response);
    void onFailureApi(long taskId, CustomException e);
}
