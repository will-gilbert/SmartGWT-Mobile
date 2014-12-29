package com.smartgwt.mobile.client.internal.widgets;

import com.google.gwt.core.client.GWT;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.types.AndroidWindowSoftInputMode;

@SGWTInternal
public class CanvasStaticImpl {

    public static class IsHistoryEnabledImpl {
        public boolean isHistoryEnabled() {
            return false;
        }
    }

    public static class IsHistoryEnabledImplHistoryEnabled extends IsHistoryEnabledImpl {
        @Override
        public final boolean isHistoryEnabled() {
            return true;
        }
    }

    public static class GetAndroidWindowSoftInputModeImpl {
        public AndroidWindowSoftInputMode getAndroidWindowSoftInputMode() {
            return null;
        }
    }

    public static class GetAndroidWindowSoftInputModeImplAdjustResize extends GetAndroidWindowSoftInputModeImpl {
        public AndroidWindowSoftInputMode getAndroidWindowSoftInputMode() {
            return AndroidWindowSoftInputMode.ADJUST_RESIZE;
        }
    }

    public static class GetAndroidWindowSoftInputModeImplAdjustPan extends GetAndroidWindowSoftInputModeImpl {
        public AndroidWindowSoftInputMode getAndroidWindowSoftInputMode() {
            return AndroidWindowSoftInputMode.ADJUST_PAN;
        }
    }

    public static class GetHideTabBarDuringKeyboardFocusImpl {
        public boolean getHideTabBarDuringKeyboardFocus() {
            return false;
        }
    }

    public static class GetHideTabBarDuringKeyboardFocusImplYes extends GetHideTabBarDuringKeyboardFocusImpl {
        @Override
        public boolean getHideTabBarDuringKeyboardFocus() {
            return true;
        }
    }

    public static class GetFixNavigationBarPositionDuringKeyboardFocusImpl {
        public boolean getFixNavigationBarPositionDuringKeyboardFocus() {
            return false;
        }
    }

    public static class GetFixNavigationBarPositionDuringKeyboardFocusImplYes extends GetFixNavigationBarPositionDuringKeyboardFocusImpl {
        @Override
        public boolean getFixNavigationBarPositionDuringKeyboardFocus() {
            return true;
        }
    }

    public static class IsUIWebViewImpl {
        public boolean isUIWebView() {
            return false;
        }
    }

    public static class IsUIWebViewImplUIWebView extends IsUIWebViewImpl {
        @Override
        public final boolean isUIWebView() {
            return true;
        }
    }

    public static class IsIOS5Impl {
        public boolean isIOS5() {
            return false;
        }
    }

    public static class IsIOS5ImplIOS5 extends IsIOS5Impl {
        @Override
        public boolean isIOS5() {
            return true;
        }
    }

    public static class IsIOSMin6_0Impl {
        public boolean isIOSMin6_0() {
            return false;
        }
    }

    public static class IsIOSMin6_0ImplIOSMin6_0 extends IsIOSMin6_0Impl {
        @Override
        public boolean isIOSMin6_0() {
            return true;
        }
    }

    public static class IsIOSMin7_0Impl {
        public boolean isIOSMin7_0() {
            return false;
        }
    }

    public static class IsIOSMin7_0ImplIOSMin7_0 extends IsIOSMin7_0Impl {
        @Override
        public boolean isIOSMin7_0() {
            return true;
        }
    }

    public static class IsIOSMin7_1Impl {
        public boolean isIOSMin7_1() {
            return false;
        }
    }

    public static class IsIOSMin7_1ImplIOSMin7_1 extends IsIOSMin7_1Impl {
        @Override
        public boolean isIOSMin7_1() {
            return true;
        }
    }

    public static class IsStandaloneModeImpl {
        public boolean isStandaloneMode() {
            return false;
        }
    }

    public static class IsStandaloneModeImplYes extends IsStandaloneModeImpl {
        @Override
        public boolean isStandaloneMode() {
            return true;
        }
    }

    public static class UseIOSNativeScrollingImpl {
        public boolean useIOSNativeScrolling() {
            return false;
        }
    }

    public static class UseIOSNativeScrollingImplUseNativeScrolling extends UseIOSNativeScrollingImpl {
        @Override
        public final boolean useIOSNativeScrolling() {
            return true;
        }
    }

    public static class UsingAutoTestImpl {
        public boolean usingAutoTest() {
            return false;
        }
    }

    public static class UsingAutoTestImplUsingAutoTest extends UsingAutoTestImpl {
        @Override
        public final boolean usingAutoTest() {
            return true;
        }
    }

    private final IsHistoryEnabledImpl isHistoryEnabledImpl = GWT.create(IsHistoryEnabledImpl.class);
    private final GetAndroidWindowSoftInputModeImpl getAndroidWindowSoftInputModeImpl = GWT.create(GetAndroidWindowSoftInputModeImpl.class);
    private final GetHideTabBarDuringKeyboardFocusImpl getHideTabBarDuringKeyboardFocusImpl = GWT.create(GetHideTabBarDuringKeyboardFocusImpl.class);
    private final GetFixNavigationBarPositionDuringKeyboardFocusImpl getFixNavigationBarPositionDuringKeyboardFocusImpl = GWT.create(GetFixNavigationBarPositionDuringKeyboardFocusImpl.class);
    private final IsUIWebViewImpl isUIWebViewImpl = GWT.create(IsUIWebViewImpl.class);
    private final IsIOS5Impl isIOS5Impl = GWT.create(IsIOS5Impl.class);
    private final IsIOSMin6_0Impl isIOSMin6_0Impl = GWT.create(IsIOSMin6_0Impl.class);
    private final IsIOSMin7_0Impl isIOSMin7_0Impl = GWT.create(IsIOSMin7_0Impl.class);
    private final IsIOSMin7_1Impl isIOSMin7_1Impl = GWT.create(IsIOSMin7_1Impl.class);
    private final IsStandaloneModeImpl isStandaloneModeImpl = GWT.create(IsStandaloneModeImpl.class);
    private final UseIOSNativeScrollingImpl useIOSNativeScrollingImpl = GWT.create(UseIOSNativeScrollingImpl.class);
    private final UsingAutoTestImpl usingAutoTestImpl = GWT.create(UsingAutoTestImpl.class);

    public final boolean isHistoryEnabled() {
        return isHistoryEnabledImpl.isHistoryEnabled();
    }

    public boolean isAndroid() {
        return false;
    }

    public boolean isIPad() {
        return false;
    }

    public boolean isIPhone() {
        return false;
    }

    public boolean isSafari() {
        return false;
    }

    public final AndroidWindowSoftInputMode getAndroidWindowSoftInputMode() {
        return getAndroidWindowSoftInputModeImpl.getAndroidWindowSoftInputMode();
    }

    public final boolean getHideTabBarDuringKeyboardFocus() {
        return getHideTabBarDuringKeyboardFocusImpl.getHideTabBarDuringKeyboardFocus();
    }

    public final boolean getFixNavigationBarPositionDuringKeyboardFocus() {
        return getFixNavigationBarPositionDuringKeyboardFocusImpl.getFixNavigationBarPositionDuringKeyboardFocus();
    }

    public final boolean isUIWebView() {
        return isUIWebViewImpl.isUIWebView();
    }

    public final boolean isIOS5() {
        return isIOS5Impl.isIOS5();
    }

    public final boolean isIOSMin6_0() {
        return isIOSMin6_0Impl.isIOSMin6_0();
    }

    public final boolean isIOSMin7_0() {
        return isIOSMin7_0Impl.isIOSMin7_0();
    }

    public final boolean isIOSMin7_1() {
        return isIOSMin7_1Impl.isIOSMin7_1();
    }

    public final boolean isStandaloneMode() {
        return isStandaloneModeImpl.isStandaloneMode();
    }

    public final boolean useIOSNativeScrolling() {
        return useIOSNativeScrollingImpl.useIOSNativeScrolling();
    }

    public final boolean usingAutoTest() {
        return usingAutoTestImpl.usingAutoTest();
    }
}
