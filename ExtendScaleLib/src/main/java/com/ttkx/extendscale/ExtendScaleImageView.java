package com.ttkx.extendscale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 扩展ImageView
 * <p>
 * 扩展功能一：扩展ScaleType属性
 * ScaleType 增加 leftCrop 、 topCrop、rightCrop、bottomCrop
 * leftCrop ： 垂直方向充满，均匀缩放，显示在控件左侧，超出控件部分进行裁剪，优先显示图片左侧内容
 * topCrop ： 水平方向充满，均匀缩放，显示在控件顶部，超出控件部分进行裁剪，优先显示图片顶部内容
 * rightCrop ： 垂直方向充满，均匀缩放，显示在控件右侧，超出控件部分进行裁剪，优先显示图片右侧内容
 * bottomCrop ： 水平方向充满，均匀缩放，显示在控件底部，超出控件部分进行裁剪，优先显示图片底部内容
 *
 * 使用：xml app:scaleType="topCrop"  code imageView.setScaleType(ExtendScaleImageView.ExtendScalType.TOP_CROP);
 *
 *  扩展功能二：增加 setGravity()方法，设置src 的显示位置（可以设置上下左右、水平和垂直居中，前提是scaleType设置为 matrix）
 *  使用：xml app:srcGravity="center_horizontal|right"   code imageView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
 */
public class ExtendScaleImageView extends AppCompatImageView {

    private ExtendScalType mScalType;
    private int mGravity = -1;

    public ExtendScaleImageView(@NonNull Context context) {
        this(context, null);
    }

    public ExtendScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendScaleType);
        final int index = a.getInt(R.styleable.ExtendScaleType_scaleType, -1);
        if (index >= 0) {
            ExtendScalType scalType = null;
            for (Map.Entry<ExtendScalType, ScaleType> entry : ExtendScalType.map.entrySet()) {
                if (entry.getKey().nativeInt == index) {
                    scalType = entry.getKey();
                    break;
                }
            }
            if (scalType != null) {
                setScaleType(scalType);
            }
        }

        final int srcGravity = a.getInt(R.styleable.ExtendScaleType_srcGravity, -1);
        setGravity(srcGravity);
        a.recycle();
    }

    @IntDef(flag = true, value = {Gravity.LEFT, Gravity.RIGHT, Gravity.TOP, Gravity.BOTTOM, Gravity.CENTER, Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityMode {
    }

    /**
     * 设置src 的显示位置（可以设置上下左右、水平和垂直居中，前提是scaleType设置为 matrix）
     *
     * @param gravity
     */
    public void setGravity(@GravityMode int gravity) {
        if (gravity != mGravity) {
            invalidate();
        }
        mGravity = gravity;
    }

    public void setScaleType(ExtendScalType scaleType) {
        mScalType = scaleType;
        if (mScalType != null) {
            ScaleType type = scaleType.getScaleType();
            if (type != null) {
                setScaleType(type);
            }
        }
    }

    public final void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean change = super.setFrame(l, t, r, b);
        Matrix matrix = getImageMatrix();
        Drawable drawable = getDrawable();

        final int dwidth = drawable.getIntrinsicWidth();
        final int dheight = drawable.getIntrinsicHeight();

        final int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (hasExtendScaleType()) {
            if (mScalType == ExtendScalType.LEFT_CROP) {
                float scale = (float) vheight / (float) dheight;
                matrix.setScale(scale, scale);
            } else if (mScalType == ExtendScalType.TOP_CROP) {
                float scale = (float) vwidth / (float) dwidth;
                matrix.setScale(scale, scale);
            } else if (mScalType == ExtendScalType.RIGHT_ROP) {
                float scale = (float) vheight / (float) dheight;
                matrix.setScale(scale, scale);
                float dx = vwidth - dwidth * scale;
                matrix.postTranslate(dx, 0);
            } else if (mScalType == ExtendScalType.BOTTOM_CROP) {
                float scale = (float) vwidth / (float) dwidth;
                matrix.setScale(scale, scale);
                float dy = vheight - dheight * scale;
                matrix.postTranslate(0, dy);
            }
            setImageMatrix(matrix);
        } else if (mGravity != -1 && ((mScalType == null && getScaleType() == ScaleType.MATRIX) || mScalType == ExtendScalType.MATRIX)) {//gravity有值，且scaleType=matrix
            int dx = 0;
            int dy = 0;

            final int gravityV = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
            if (gravityV != 0) {
                if (gravityV == Gravity.CENTER_VERTICAL) {
                    dy = (vheight - dheight) / 2;
                } else if (gravityV == Gravity.BOTTOM) {
                    dy = vheight - dheight;
                } else { // (gravity == Gravity.TOP)

                }
            }

            final int gravityH = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            if (gravityH != 0) {
                if (gravityH == Gravity.RIGHT) {
                    dx = vwidth - dwidth;
                } else if (gravityH == Gravity.CENTER_HORIZONTAL) { // (gravity == Gravity.CENTER_VERTICAL)
                    dx = (vwidth - dwidth) / 2;
                } else {// (gravity == Gravity.LEFT)

                }
            }
            matrix.postTranslate(dx, dy);
            setImageMatrix(matrix);
        }
        return change;
    }

    /**
     * 是否有扩展的scale属性
     * @return
     */
    private boolean hasExtendScaleType() {
        return getDrawable() != null && mScalType != null && mScalType.isExtendScaleType();
    }

    public static enum ExtendScalType {

        MATRIX(0),
        FIT_XY(1),
        FIT_START(2),
        FIT_CENTER(3),
        FIT_END(4),
        CENTER(5),
        CENTER_CROP(6),
        CENTER_INSIDE(7),

        LEFT_CROP(8),
        TOP_CROP(9),
        RIGHT_ROP(10),
        BOTTOM_CROP(11);

        static Map<ExtendScalType, ScaleType> map = new HashMap<>();
        final int nativeInt;

        static {
            map.put(MATRIX, ScaleType.MATRIX);
            map.put(FIT_XY, ScaleType.FIT_XY);
            map.put(FIT_START, ScaleType.FIT_START);
            map.put(FIT_CENTER, ScaleType.FIT_CENTER);
            map.put(FIT_END, ScaleType.FIT_END);
            map.put(CENTER, ScaleType.CENTER);
            map.put(CENTER_CROP, ScaleType.CENTER_CROP);
            map.put(CENTER_INSIDE, ScaleType.CENTER_INSIDE);

            map.put(LEFT_CROP, ScaleType.MATRIX);
            map.put(TOP_CROP, ScaleType.MATRIX);
            map.put(RIGHT_ROP, ScaleType.MATRIX);
            map.put(BOTTOM_CROP, ScaleType.MATRIX);
        }

        ExtendScalType(int ni) {
            nativeInt = ni;
        }

        public ScaleType getScaleType() {
            return map.get(this);
        }

        public boolean isExtendScaleType() {
            return this.nativeInt >= 8;
        }
    }
}
