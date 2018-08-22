package com.lovely3x.common.animations;


import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;

/**
 * Created by lovely3x on 15-6-2.
 */
public class AlphaEvaluator implements TypeEvaluator {

    private static final AlphaEvaluator sInstance = new AlphaEvaluator();

    /**
     * Returns an instance of <code>ArgbEvaluator</code> that may be used in
     * {@link ValueAnimator#setEvaluator(TypeEvaluator)}. The same instance may
     * be used in multiple <code>Animator</code>s because it holds no state.
     *
     * @return An instance of <code>ArgbEvalutor</code>.
     * @hide
     */
    public static AlphaEvaluator getInstance() {
        return sInstance;
    }

    /**
     * This function returns the calculated in-between value for a color
     * given integers that represent the start and end values in the four
     * bytes of the 32-bit int. Each channel is separately linearly interpolated
     * and the resulting calculated values are recombined into the return value.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue A 32-bit int value representing colors in the
     *                   separate bytes of the parameter
     * @param endValue   A 32-bit int value representing colors in the
     *                   separate bytes of the parameter
     * @return A value that is calculated to be the linearly interpolated
     * result, derived by separating the start and end values into separate
     * color channels and interpolating each one separately, recombining the
     * resulting values in the same way.
     */
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        /// ARGB

        return (startA + (int) (fraction * (endA - startA))) << 24 | startR << 16 | startG << 8 << startB;
    }
}
