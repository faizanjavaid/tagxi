package com.tyt.driver.retro.base;


import com.tyt.driver.utilz.exception.CustomException;

/**
 * Created by guru on 1/6/2017.
 */

public interface IModelListener<T> {

    void onSuccessfulApi(long taskId, T response);

    void onFailureApi(long taskId, CustomException e);
}
