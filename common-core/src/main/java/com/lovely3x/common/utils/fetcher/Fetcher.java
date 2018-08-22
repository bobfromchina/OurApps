package com.lovely3x.common.utils.fetcher;

/**
 * Created by lovely3x on 17/3/6.
 */

public interface Fetcher<S, R> {

    public R tryToFastFetch(S s);

    public R process(S s);

}
