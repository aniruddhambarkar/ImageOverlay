package com.aniruddhambarkar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by R1 on 7/18/2017.
 */
public class ImageOverlay extends ViewGroup implements View.OnTouchListener, View.OnClickListener {

    String TAG = "ImageOverlay";

    int pointSize = 45;
    private int bg_resource;
    private Drawable bgDrawable;

    private PointListener pointListener;

    public void setPointListener(PointListener pointListener) {
        this.pointListener = pointListener;
    }

    /**
     * @return arraylist of pointviews added/present on view
     */

    public ArrayList<PointView> getPointViews() {
        return pointViews;
    }

    /**
     * Sets points on image view
     *
     * @param pointViews
     */

    public void setPointViews(ArrayList<PointView> pointViews) {
        this.pointViews = pointViews;
        setPointView();
    }

    ArrayList<PointView> pointViews = new ArrayList<>();

    public ImageOverlay(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public ImageOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context, attrs, 0);
        setOnTouchListener(this);
    }

    public ImageOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        readAttrs(context, attrs, 0);
    }

    public ImageOverlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnTouchListener(this);

        readAttrs(context, attrs, defStyleRes);
    }


    private void readAttrs(final Context context, final AttributeSet attrs, final int defStyle) {

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ImageOverlay, defStyle, 0);

        pointSize = attributes.getDimensionPixelSize(R.styleable.ImageOverlay_point_size, 45);
        if (attributes.hasValue(R.styleable.ImageOverlay_point_drawable)) {
            bgDrawable = getResources().getDrawable(attributes.getResourceId(R.styleable.ImageOverlay_point_drawable, R.drawable.bg_dark_blue_round));
        } else {
            bgDrawable = getBgDrawable();
        }

        attributes.recycle();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    private void setPointView() {

        int scaledHeight = getHeight();
        int scaledWidth = getWidth();


        for (int count = 0; count < pointViews.size(); count++) {


            View view = new View(getContext());
            // view.setBackgroundResource(bg_resource);
            view.setBackground(bgDrawable);
            view.setMinimumHeight(pointSize);
            view.setMinimumWidth(pointSize);

            double relativeX = pointViews.get(count).getX() * scaledWidth;
            double relativeY = pointViews.get(count).getY() * scaledHeight;

            // Log.v("ImageOverlay", " Index " + count + " " + relativeX + "  " + relativeY + " X " + pointViews.get(count).getX() + " Height " + scaledWidth + " width " + scaledHeight);

            //view.layout( (int) pointViews.get(count).getX(),  (int) pointViews.get(count).getY(),  (int)pointViews.get(count).getX() + pointSize, (int)pointViews.get(count).getY() + pointSize);
            view.layout((int) relativeX, (int) relativeY, (int) relativeX + pointSize, (int) relativeY + pointSize);
            addView(view);
            view.setOnClickListener(this);

        }


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            String location = String.valueOf(motionEvent.getX()) + "x" + String.valueOf(motionEvent.getY());
            Log.v("Click", location);

            View childView = new View(getContext());
            childView.setBackground(bgDrawable);

            childView.setMinimumHeight(pointSize);
            childView.setMinimumWidth(pointSize);
            childView.layout((int) motionEvent.getX(), (int) motionEvent.getY(), (int) motionEvent.getX() + pointSize, (int) motionEvent.getY() + pointSize);
            addView(childView);
            childView.setOnClickListener(this);

            PointView pointView = new PointView();

            pointView.setX(motionEvent.getX());
            pointView.setY(motionEvent.getY());

            int scaledHeight = getHeight();
            int scaledWidth = getWidth();

            double relativeX = motionEvent.getX() / scaledWidth;
            double relativeY = motionEvent.getY() / scaledHeight;

            pointView.setX(relativeX);
            pointView.setY(relativeY);

            pointViews.add(pointView);

            if (pointListener != null) {
                pointListener.OnPointAdded(pointView);
            } else
                showPopup(childView, pointView);

            //  int[] location = new int[2];


        }

        return false;
    }


    private void showPopup(View childView, final PointView pointView) {


        final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_point_info, null, false);
        final EditText edtInfo = mView.findViewById(R.id.edtInfo);

        AppCompatButton btnSave = mView.findViewById(R.id.btnSave);
        AppCompatButton btnRemove = mView.findViewById(R.id.btnRemove);


        edtInfo.setText(pointView.getComment());

        final PopupWindow popUp = new PopupWindow(getContext());
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setContentView(mView);

        popUp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popUp.setOutsideTouchable(true);
        popUp.setFocusable(true);

        int[] viewLocation = new int[2];
        childView.getLocationOnScreen(viewLocation);
        // Removes default background.
        popUp.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popUp.setOutsideTouchable(true);
        //popUp.showAsDropDown(view1);

        mView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int width = mView.getMeasuredWidth();
        int height = mView.getMeasuredHeight();
        if (viewLocation[1] - height / 2 > 0) {

            popUp.showAtLocation(childView, Gravity.NO_GRAVITY, viewLocation[0], viewLocation[1] - height / 2);
        } else {
            popUp.showAtLocation(childView, Gravity.NO_GRAVITY, viewLocation[0], viewLocation[1]);
        }

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointView.setComment(edtInfo.getText().toString());
                popUp.dismiss();
            }
        });

        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
                removeOverlay(pointView);

            }
        });

    }


    private void showPopup(final View childView) {

        PointView pointView = null;
        int index = indexOfChild(childView);
        if (pointViews.size() >= index + 1) {
            pointView = pointViews.get(index);
        } else {
            return;
        }

        showPopup(childView, pointView);

    }

    @Override
    public void onClick(View view) {

        if (pointListener != null) {
            PointView pointView = null;
            int index = indexOfChild(view);
            if (pointViews.size() >= index + 1) {
                pointView = pointViews.get(index);
            }

            pointListener.OnPointClicked(pointView);
        } else
            showPopup(view);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setPointView();
    }

    private Drawable getBgDrawable() {


        GradientDrawable pointDrawable = new GradientDrawable();
        pointDrawable.setCornerRadius(pointSize / 2);
        pointDrawable.setColor(Color.BLUE);
        Log.v(TAG, " Point size " + pointSize);
        return pointDrawable;
    }

    /**
     * Removes specific overlay point from view
     *
     * @param pointView
     */
    public void removeOverlay(PointView pointView) {
        int index = pointViews.indexOf(pointView);
        removeViewAt(index);
        pointViews.remove(pointView);
    }

    /**
     * Removes all overlays added into view
     */
    public void clearOverlays() {
        pointViews.clear();
        removeAllViews();

    }

}
