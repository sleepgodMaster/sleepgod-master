package com.org.sleepgod.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by cool on 2016/10/26 0026.
 * 使用说明：http://blog.csdn.net/cool_fuwei/article/details/52997644
 */

public class SearchFlowLayout extends ViewGroup {
    List<Line> lines = new ArrayList<Line>();//所有的行
    private Line currentLine;

    public SearchFlowLayout(Context context) {
        super(context);
    }

    public SearchFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void click(int position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lines.clear();
        currentLine = null;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float maxWidth = width - getPaddingLeft() - getPaddingRight();
        float horizontalDistance = dp2px(getContext(), 10);
        float vercitalDistance = dp2px(getContext(), 10);
        int childCount = getChildCount();
        currentLine = new Line(maxWidth, horizontalDistance, vercitalDistance);
        lines.add(currentLine);
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            final int position = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.click(position);
                    }
                }
            });
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            if (currentLine.canAddView(childView)) {
                currentLine.addView(childView);
            } else {
                currentLine = new Line(maxWidth, horizontalDistance, vercitalDistance);
                if (currentLine.canAddView(childView)) {
                    currentLine.addView(childView);
                }
                lines.add(currentLine);
            }
        }

        int hight = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                hight += getPaddingTop();
            } else if (i == lines.size() - 1) {
                hight += getPaddingBottom();
            }
            hight += lines.get(i).mLineHight + vercitalDistance;
        }

        setMeasuredDimension(width, hight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineTop = getPaddingTop();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(lineTop);
            lineTop += line.mLineHight + line.mVercitalDistance;
        }
    }

    /**
     * 每一行
     */
    class Line {
        public List<View> mViews = new ArrayList<View>();
        public float mHorizontalDistance;//行中两个子View之间水平间距
        public float mVercitalDistance;//两个行竖直间距
        public float mMaxWidth;//行中的最大的宽度
        public float mUseWidth;//行中已经使用的宽度
        public float mLineHight;//行的高度

        public Line(float maxWidth, float horizontalDistance, float vercitalDistance) {
            this.mMaxWidth = maxWidth;
            this.mHorizontalDistance = horizontalDistance;
            this.mVercitalDistance = vercitalDistance;
        }

        /**
         * 是否能添加View
         *
         * @param view
         * @return
         */
        private boolean canAddView(View view) {

            if (mViews.size() == 0) {
                mUseWidth += getPaddingLeft() + getPaddingRight() + view.getMeasuredWidth();
                return true;
            } else {
                mUseWidth += view.getMeasuredWidth() + mHorizontalDistance;
            }
            if (mUseWidth <= mMaxWidth) {
                return true;
            }
            return false;
        }

        /**
         * 添加View
         *
         * @param view
         */
        private void addView(View view) {
            mViews.add(view);
            mLineHight = mLineHight < view.getMeasuredHeight() ? view.getMeasuredHeight() : mLineHight;
        }

        /**
         * 摆放子view
         */
        private void layout(int lineTop) {
            int left = getPaddingLeft();
            int top = lineTop;
            int right = 0;
            int bottom = 0;

            for (int i = 0; i < mViews.size(); i++) {
                View view = mViews.get(i);
                right = left + view.getMeasuredWidth();
                bottom = top + view.getMeasuredHeight();

                view.layout(left, top, right, bottom);
                left += view.getMeasuredWidth() + mHorizontalDistance;

            }
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);
        return px;
    }
}
