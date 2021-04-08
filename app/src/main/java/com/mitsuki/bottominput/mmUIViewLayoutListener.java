//package com.mitsuki.bottominput;
//
///*
//-----------------------------------------------------------------------------
//MIT License
//
//Copyright (c) 2019-2020 mm_longcheng@icloud.com
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//-----------------------------------------------------------------------------
//*/
//
//
//import android.annotation.SuppressLint;
//import android.content.pm.ActivityInfo;
//import android.graphics.drawable.ColorDrawable;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.animation.ObjectAnimator;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.graphics.Rect;
//import android.os.Build;
//import android.util.Log;
//import android.view.WindowManager;
//import android.widget.PopupWindow;
//
//
//import java.util.Locale;
//
//@SuppressWarnings("unused")
//public class mmUIViewLayoutListener extends PopupWindow implements ViewTreeObserver.OnGlobalLayoutListener
//{
//    private static final String TAG = mmUIViewLayoutListener.class.getSimpleName();
//
//    private final mmSurfaceContentKeypadStatus hContentKeypadStatus = new mmSurfaceContentKeypadStatus();
//
//    private mmUIViewSurfaceMaster pUIViewSurfaceMaster = null;
//
//    private final Rect hOriginalRect = new Rect();
//    private final Rect hCompressRectOld = new Rect();
//    private final Rect hCompressRectNew = new Rect();
//    private final double[] hTextEditRect = new double[4];
//
//    private final int[] hSafeArea = new int[4];
//    private final int[] hViewArea = new int[4];
//
//    private ObjectAnimator pAnimator = null;
//
//    private int hDeviationHeight = 0;
//    private int hNavigationBarHeight = 0;
//
//    private final ColorDrawable pColorDrawable = new ColorDrawable(android.graphics.Color.TRANSPARENT);
//    private View pUIViewListener = null;
//
//    public mmUIViewLayoutListener()
//    {
//        super();
//    }
//
//    public void Init()
//    {
//        java.util.Arrays.fill(this.hTextEditRect, 0, 4, (double)0);
//
//        Log.i(TAG, TAG + " Init succeed.");
//    }
//
//    public void Destroy()
//    {
//        java.util.Arrays.fill(this.hTextEditRect, 0, 4, (double)0);
//
//        Log.i(TAG, TAG + " Destroy succeed.");
//    }
//
//    public void SetUIViewSurfaceMaster(mmUIViewSurfaceMaster pUIViewSurfaceMaster)
//    {
//        this.pUIViewSurfaceMaster = pUIViewSurfaceMaster;
//    }
//
//    public void OnFinishLaunching()
//    {
//        Activity pActivity = this.pUIViewSurfaceMaster.GetActivity();
//
//        this.pUIViewListener = new View(pActivity);
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setWidth(0);
//        this.setBackgroundDrawable(this.pColorDrawable);
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        this.setInputMethodMode(INPUT_METHOD_NEEDED);
//        this.setContentView(this.pUIViewListener);
//
//        this.PopupWindowShowAtLocation();
//
//        ViewTreeObserver pViewTreeObserver = this.pUIViewListener.getViewTreeObserver();
//        AddOnGlobalLayoutListener(pViewTreeObserver, this);
//
//        // Navigation Bar will interference the getWindowVisibleDisplayFrame result.
//        // We need use size deviation for verify whether it is a soft keyboard.
//        mmUIDisplayMetrics.GetDisplayMetricsAreaSize(pActivity, this.hViewArea);
//
//        // (x, y, w, h)
//        Log.i(TAG, TAG + " ViewAreaPixel: (" +
//                this.hViewArea[0] + ", " +
//                this.hViewArea[1] + ", " +
//                this.hViewArea[2] + ", " +
//                this.hViewArea[3] + ")");
//
//        this.hOriginalRect.left   = this.hViewArea[0];
//        this.hOriginalRect.top    = this.hViewArea[1];
//        this.hOriginalRect.right  = this.hViewArea[0] + this.hViewArea[2];
//        this.hOriginalRect.bottom = this.hViewArea[1] + this.hViewArea[3];
//
//        // We can not use the parent pUIView.
//        // At Activity setContentView before setSoftInputMode SOFT_INPUT_ADJUST_NOTHING.
//        // The pUIView WindowVisibleDisplayFrame is unreliable.
//        // Use pUIViewListener for always correct value.
//        this.pUIViewListener.getWindowVisibleDisplayFrame(this.hCompressRectOld);
//
//        // Some devices will rotate when entering the background, trigger onGlobalLayout.
//        // Use max(StatusBarHeight, NavigationBarHeight) for the DeviationHeight.
//        //
//        // this.hDeviationHeight = this.hViewArea[3] - this.hCompressRectOld.height();
//        int hStatusBarHeight = mmUIDisplayMetrics.GetStatusBarHeight(pActivity);
//        int hNavigationBarHeight = mmUIDisplayMetrics.GetNavigationBarHeight(pActivity);
//        this.hDeviationHeight = Math.max(hStatusBarHeight, hNavigationBarHeight);
//        this.hDeviationHeight = Math.max(this.hDeviationHeight, 0);
//        this.hNavigationBarHeight = hNavigationBarHeight;
//
//        this.pAnimator = ObjectAnimator.ofFloat(this.pUIViewSurfaceMaster, "translationY", 0.0f);
//    }
//    public void OnBeforeTerminate()
//    {
//        this.pAnimator = null;
//
//        ViewTreeObserver pViewTreeObserver = this.pUIViewListener.getViewTreeObserver();
//        RemoveOnGlobalLayoutListener(pViewTreeObserver, this);
//
//        this.PopupWindowHideAtLocation();
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    @SuppressWarnings({"deprecation", "RedundantSuppression"})
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static void RemoveOnGlobalLayoutListener(ViewTreeObserver pViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener pListener)
//    {
//        if (Build.VERSION.SDK_INT < 16)
//        {
//            // Deprecated in API level 16
//            pViewTreeObserver.removeGlobalOnLayoutListener(pListener);
//        }
//        else
//        {
//            pViewTreeObserver.removeOnGlobalLayoutListener(pListener);
//        }
//    }
//
//    public static void AddOnGlobalLayoutListener(ViewTreeObserver pViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener pListener)
//    {
//        pViewTreeObserver.addOnGlobalLayoutListener(pListener);
//    }
//
//    void PopupWindowShowAtLocation()
//    {
//        Runnable pRunnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                View pView = mmUIViewLayoutListener.this.pUIViewSurfaceMaster;
//                View pRootView  = pView.getRootView();
//                mmUIViewLayoutListener.this.showAtLocation(pRootView, Gravity.NO_GRAVITY, 0, 0);
//            }
//        };
//        this.pUIViewSurfaceMaster.post(pRunnable);
//    }
//    void PopupWindowHideAtLocation()
//    {
//        this.dismiss();
//    }
//
//    @SuppressWarnings({"UnusedAssignment", "ConstantConditions"})
//    @Override
//    public void onGlobalLayout()
//    {
//        do
//        {
//            Activity pActivity = this.pUIViewSurfaceMaster.GetActivity();
//
//            // Some devices will rotate in the background before switching.
//            //屏幕旋转的问题
//            int w = this.pUIViewSurfaceMaster.getWidth();
//            int h = this.pUIViewSurfaceMaster.getHeight();
//            double min = (double)Math.min(w, h);
//            double max = (double)Math.max(w, h);
//            // max / min < 1.20 On such aspect-ratio. Could be a square resolution.
//            if (max / min >= 1.20)
//            {
//                int ro = pActivity.getRequestedOrientation();
//                if ((ro == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
//                        ro == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) && w > h)
//                {
//                    // Not need handle event.
//                    break;
//                }
//                if ((ro == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
//                        ro == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) && w < h)
//                {
//                    // Not need handle event.
//                    break;
//                }
//            }
//
//            // We can not use the parent pUIView.
//            // At Activity setContentView before setSoftInputMode SOFT_INPUT_ADJUST_NOTHING.
//            // The pUIView WindowVisibleDisplayFrame is unreliable.
//            // Use pUIViewListener for always correct value.
//            this.pUIViewListener.getWindowVisibleDisplayFrame(this.hCompressRectNew);
//
//            //过滤重复事件
//            if(this.hCompressRectNew.bottom == this.hCompressRectOld.bottom)
//            {
//                // Some devices also trigger this function when the area has not changed.
//                break;
//            }
//
//            //StatusBar显示隐藏事件处理
//            //需要减掉的导航栏高度
//            int hBottomIH = mmUIDisplayMetrics.GetBottomInvalidHeight(pActivity, this.pUIViewListener, this.hNavigationBarHeight);
//            //屏幕高度减去导航栏高度后的高度
//            int hBottomVH = this.hOriginalRect.bottom - hBottomIH;
//            //当前显示区域高度减去了上面的高度
//            int hRnHeight = this.hCompressRectNew.bottom - hBottomVH;
//            //当前显示区域高度和上次显示区域的高度差
//            int hCompress = this.hCompressRectNew.bottom - this.hCompressRectOld.bottom;
//
//            //屏幕高度减去导航栏高度后的高度 同 hBottomVH
//            int hHideLine = this.hOriginalRect.bottom - hBottomIH;
//            //hDeviationHeight导航栏高度和状态栏高度挑个大的
//            int hShowLine = this.hOriginalRect.bottom - this.hDeviationHeight;
//            //当前显示区域的高度
//            int hCurrLine = this.hCompressRectNew.bottom;
//
//            if((0 <= hRnHeight && hCompress <= this.hDeviationHeight) ||
//                    (0 <= hRnHeight && hCurrLine <  hHideLine) ||
//                    (0 >  hRnHeight && hCurrLine >= hShowLine && 0 != hBottomIH))
//            {
//                //StatusBar发生变化
//
//                // Ignore StatusBar NavigationBar show/hide event.
//                //     1. We need to be careful when the virtual navigation bar does not exist,
//                //        the invalid area at the bottom is 0.
//                //     2. There is a physical navigation bar but not showing.
//                //
//                // Keyboard hide. (0 <= hRnHeight && hCompress <= this.hDeviationHeight)
//                // Keyboard hide. (0 <= hRnHeight && hCurrLine <  hHideLine)
//                // Keyboard show. (0 >  hRnHeight && hCurrLine >= hShowLine && 0 != hBottomIH)
//                break;
//            }
//
//            int hOrHeight = this.hOriginalRect.bottom;
//            int hSrHeight = this.hCompressRectNew.bottom - this.hOriginalRect.bottom;
//            int hHrHeight = this.hOriginalRect.bottom - this.hCompressRectOld.bottom;
//            int hDtHeight = (0 <= hRnHeight) ? hHrHeight : hSrHeight;
//
//            // Note: The abs(hDtHeight) always keyboard height.
//            //       Height adjustment occurs when the input mode is switched.
//            double hKeypadRectY = (double)((0 <= hRnHeight) ? hOrHeight : hCurrLine);
//            double hKeypadRectH = (double)Math.abs(hDtHeight);
//
//            double hVelocity = (max / 2.0) / 0.2;
//            double hAnimationDuration = Math.abs(hCompress) / hVelocity;
//            int hState = 0;
//            int hAnimationCurve = mmSurfaceInterface.MM_SURFACE_KEYPAD_STATUS_AnimationCurveLinear;
//            double hTransformWindow = this.pUIViewSurfaceMaster.hTransformWindow;
//
//            // At this time, View size value is valid.
//            int hViewPixelW = this.pUIViewSurfaceMaster.getWidth();
//            int hViewPixelH = this.pUIViewSurfaceMaster.getHeight();
//
//            if(0 <= hRnHeight)
//            {
//                // keypad is fully retracted.
//                hState = mmSurfaceInterface.MM_SURFACE_KEYPAD_STATUS_HIDE;
//            }
//            else
//            {
//                // keypad is Height adjustment or first pop.
//                hState = mmSurfaceInterface.MM_SURFACE_KEYPAD_STATUS_SHOW;
//            }
//
//            mmUISafeAreaLayout.GetSafeAreaLayoutGuide(pActivity, this.pUIViewSurfaceMaster, this.hSafeArea);
//
//            this.hContentKeypadStatus.surface = this.pUIViewSurfaceMaster;
//
//            this.hContentKeypadStatus.screen_rect[0] = 0.0;
//            this.hContentKeypadStatus.screen_rect[1] = 0.0;
//            this.hContentKeypadStatus.screen_rect[2] = (hViewPixelW * hTransformWindow);
//            this.hContentKeypadStatus.screen_rect[3] = (hViewPixelH * hTransformWindow);
//
//            this.hContentKeypadStatus.safety_area[0] = (this.hSafeArea[0] * hTransformWindow);
//            this.hContentKeypadStatus.safety_area[1] = (this.hSafeArea[1] * hTransformWindow);
//            this.hContentKeypadStatus.safety_area[2] = (this.hSafeArea[2] * hTransformWindow);
//            this.hContentKeypadStatus.safety_area[3] = (this.hSafeArea[3] * hTransformWindow);
//
//            this.hContentKeypadStatus.keypad_rect[0] = (this.hCompressRectOld.left     * hTransformWindow);
//            this.hContentKeypadStatus.keypad_rect[1] = (hKeypadRectY                   * hTransformWindow);
//            this.hContentKeypadStatus.keypad_rect[2] = (this.hCompressRectOld.width()  * hTransformWindow);
//            this.hContentKeypadStatus.keypad_rect[3] = (hKeypadRectH                   * hTransformWindow);
//
//            this.hContentKeypadStatus.window_rect[0] = (this.hCompressRectNew.left     * hTransformWindow);
//            this.hContentKeypadStatus.window_rect[1] = (this.hCompressRectNew.top      * hTransformWindow);
//            this.hContentKeypadStatus.window_rect[2] = (this.hCompressRectNew.width()  * hTransformWindow);
//            this.hContentKeypadStatus.window_rect[3] = (this.hCompressRectNew.height() * hTransformWindow);
//
//            // we can not get the android soft input keypad animation status.
//            this.hContentKeypadStatus.animation_duration = hAnimationDuration;
//            this.hContentKeypadStatus.animation_curve = hAnimationCurve;
//
//            this.hContentKeypadStatus.state = hState;
//            this.hContentKeypadStatus.handle = 0;
//
//            this.pUIViewSurfaceMaster.NativeOnKeypadStatus(this.hContentKeypadStatus);
//
//            this.hCompressRectOld.set(this.hCompressRectNew);
//
//            String hStateString = (0 == hState) ? "show" : "hide";
//            String hDtHeightString = String.format(Locale.US, "%+d", hDtHeight);
//            mmLogger.LogI(TAG + " hDtHeight:" + hDtHeightString + " state:" + hStateString + " duration:" + hAnimationDuration);
//
//            if(0 != this.hContentKeypadStatus.handle)
//            {
//                // This event has been processed internally.
//                break;
//            }
//
//            // Edit text world pixel rectangle.
//            this.pUIViewSurfaceMaster.NativeOnGetTextEditRect(this.hTextEditRect);
//
//            double y1 = (double)((this.hContentKeypadStatus.keypad_rect[1]     ) / hTransformWindow);
//            double y2 = (double)((this.hTextEditRect[1] + this.hTextEditRect[3]) / hTransformWindow);
//            double y3 = y1 - y2;
//            double y4 = 0 > y3 ? y3 : 0.0;
//
//            // Sometimes it will trigger multiple times in succession, and only the last one is valid.
//            // We always cancel the previous Animator and take effect the last Animator.
//            this.pAnimator.cancel();
//            this.pAnimator.setPropertyName("translationY");
//            this.pAnimator.setTarget(this.pUIViewSurfaceMaster);
//            this.pAnimator.setDuration((long)(hAnimationDuration * 1000));
//            this.pAnimator.setFloatValues((float)y4);
//            this.pAnimator.setCurrentPlayTime(0);
//            this.pAnimator.start();
//
//            // For quick assignment.
//            // this.pUIViewSurfaceMaster.setTranslationY(y4);
//        }while(false);
//    }
//}