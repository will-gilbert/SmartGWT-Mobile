package com.smartgwt.mobile.client.internal.util;

import com.smartgwt.mobile.SGWTInternal;

/**
 * A visitor for an operation resulting in an end result.
 * 
 * <p><b>NOTE:</b> This may still be used with operations that do not result in a meaningful
 * value by simply using the <code>java.lang.Void</code> type for <code>&lt;R&gt;</code>.
 * 
 * @param <R> the result type.
 * @param <P1> the parameter type.
 */
@SGWTInternal
public interface Visitor<R, P1> {

    /**
     * @return <code>null</code> if the operation should continue; otherwise, the end result.
     */
    public R visit(P1 param1);
}
