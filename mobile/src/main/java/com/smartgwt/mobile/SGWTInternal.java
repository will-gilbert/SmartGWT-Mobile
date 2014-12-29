package com.smartgwt.mobile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target of the annotation is meant only for internal use by the
 * SmartGWT.mobile API.
 * 
 * <p>This is complementary to the naming convention of internal symbols, which is to prefix
 * the identifier with an underscore.  This helps developers to recognize internal symbols
 * in IDE code completion dialogs.
 * 
 * <p><b>NOTE:</b> Any identifier beginning with an underscore should be regarded as internal,
 * whether or not it is annotated with <code>@SGWTInternal</code>.
 * 
 * <p><b style='color:red'>WARNING:</b> Use of internal APIs is not recommended because they
 * may change in behavior or effect or be removed from SmartGWT.mobile without notice.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
public @interface SGWTInternal {
}
