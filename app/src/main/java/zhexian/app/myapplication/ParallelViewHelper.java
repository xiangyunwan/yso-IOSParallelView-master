package zhexian.app.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


/**
 * 实现View对象，响应陀螺仪改变事件
 * 达到类似IOS那种背景图片移动的效果
 * 效果见提分宝选择科目模块
 * <p/>
 * **使用方式，初始化：{@link #ParallelViewHelper(Context, View, int)}
 * 调用：
 * 在onResume里使用{@link #start()}
 * 在onPause里使用{@link #stop()}
 * Created by 陈俊杰 on 2015/12/9.
 */
public class ParallelViewHelper implements GyroScopeSensorListener.ISensorListener {

    /**
     * 默认0.02f在宽度填满屏幕的图片上，移动起来看着很舒服
     */
    public static final float TRANSFORM_FACTOR = 0.02f;
    private float mTransformFactor = TRANSFORM_FACTOR;
    private View mParallelView;
    private float mCurrentShiftX;
    private float mCurrentShiftY;
    private GyroScopeSensorListener mSensorListener;
    private ViewGroup.LayoutParams mLayoutParams;
    private int mViewWidth;
    private int mViewHeight;
    private int mShiftDistancePX;

    public ParallelViewHelper(Context context, final View targetView) {
        this(context, targetView, context.getResources().getDimensionPixelSize(R.dimen.image_shift));
    }

    /**
     * 初始化一个
     *
     * @param context
     * @param targetView
     * @param shiftDistancePX
     */
    public ParallelViewHelper(Context context, final View targetView, int shiftDistancePX) {
        mShiftDistancePX = shiftDistancePX;
        mSensorListener = new GyroScopeSensorListener(context);
        mSensorListener.setSensorListener(this);
        mParallelView = targetView;
        mParallelView.setX(-mShiftDistancePX);
        mParallelView.setY(-mShiftDistancePX);
        mLayoutParams = mParallelView.getLayoutParams();
        mViewWidth = mParallelView.getWidth();
        mViewHeight = mParallelView.getHeight();

        if (mViewWidth > 0 && mViewHeight > 0) {
            bindView();
            return;
        }

        ViewTreeObserver vto = targetView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mViewWidth = targetView.getWidth();
                mViewHeight = targetView.getHeight();
                bindView();
            }
        });
    }

    void bindView() {
        mLayoutParams.width = mViewWidth + mShiftDistancePX * 2;
        mLayoutParams.height = mViewHeight + mShiftDistancePX * 2;
        mParallelView.setLayoutParams(mLayoutParams);
    }


    /**
     * 注册监听陀螺仪事件
     */
    public void start() {
        mSensorListener.start();
    }

    /**
     * 监听陀螺仪事件耗电，因此在onPause里需要注销监听事件
     */
    public void stop() {
        mSensorListener.stop();
    }

    /**
     * 设置移动的补偿变量，越高移动越快，标准参考{@link #TRANSFORM_FACTOR}
     *
     * @param transformFactor
     */
    public void setTransformFactor(float transformFactor) {
        mTransformFactor = transformFactor;
    }

    @Override
    public void onGyroScopeChange(float horizontalShift, float verticalShift) {
        mCurrentShiftX += mShiftDistancePX * horizontalShift * mTransformFactor;
        mCurrentShiftY += mShiftDistancePX * verticalShift * mTransformFactor;

        if (Math.abs(mCurrentShiftX) > mShiftDistancePX)
            mCurrentShiftX = mCurrentShiftX < 0 ? -mShiftDistancePX : mShiftDistancePX;

        if (Math.abs(mCurrentShiftY) > mShiftDistancePX)
            mCurrentShiftY = mCurrentShiftY < 0 ? -mShiftDistancePX : mShiftDistancePX;

        //默认就margin 负的边距尺寸，因此 margin的最大值是 负的边距尺寸*2 ~ 0
        mParallelView.setX((int) mCurrentShiftX - mShiftDistancePX);
        mParallelView.setY((int) mCurrentShiftY - mShiftDistancePX);
    }
}
