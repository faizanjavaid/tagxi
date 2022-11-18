package com.tyt.client.retro.base;

import com.tyt.client.utilz.exception.CustomException;

/**
 * Created By Mahi in 2021
 */

/** Gives API callbacks
 * onSuccessfulApi - used for  Successful API Callbacks
 * onFailureApi - used for Failure API Callbacks
 * **/

public interface Basecallback<T> {
    void onSuccessfulApi(long taskId, T response);

    void onFailureApi(long taskId, CustomException e);
}
