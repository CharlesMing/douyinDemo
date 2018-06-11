package com.example.admin.myapplication.widget.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.TextScale;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@RestrictTo(LIBRARY_GROUP)
public class MyBottomNavigationMenuView extends ViewGroup implements MenuView {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;

    private final TransitionSet mSet;
    private final int mActiveItemMaxWidth;
    private final int mItemHeight;
    private final OnClickListener mOnClickListener;
    private final Pools.Pool<MyBottomNavigationItemView> mItemPool = new Pools.SynchronizedPool<>(5);

    private MyBottomNavigationItemView[] mButtons;
    private int mSelectedItemId = 0;
    private int mSelectedItemPosition = 0;
    private ColorStateList mItemIconTint;
    private ColorStateList mItemTextColor;
    private int mItemBackgroundRes;
    private int[] mTempChildWidths;

    private MyBottomNavigationPresenter mPresenter;
    private MenuBuilder mMenu;

    public MyBottomNavigationMenuView(Context context) {
        this(context, null);
    }

    @SuppressLint("RestrictedApi")
    public MyBottomNavigationMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Resources res = getResources();
        mActiveItemMaxWidth = res.getDimensionPixelSize(
                android.support.design.R.dimen.design_bottom_navigation_active_item_max_width);
        mItemHeight = res.getDimensionPixelSize(android.support.design.R.dimen.design_bottom_navigation_height);

        mSet = new AutoTransition();
        mSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
        mSet.setDuration(ACTIVE_ANIMATION_DURATION_MS);
        mSet.setInterpolator(new FastOutSlowInInterpolator());
        mSet.addTransition(new TextScale());

        mOnClickListener = new OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                final MyBottomNavigationItemView itemView = (MyBottomNavigationItemView) v;
                MenuItem item = itemView.getItemData();
                if (!mMenu.performItemAction(item, mPresenter, 0)) {
                    item.setChecked(true);
                }
            }
        };
        mTempChildWidths = new int[BottomNavigationMenu.MAX_ITEM_COUNT];
    }

    @Override
    public void initialize(MenuBuilder menu) {
        mMenu = menu;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int count = getChildCount();

        final int heightSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);

        final int maxAvailable = width / (count == 0 ? 1 : count);
        final int childWidth = Math.min(maxAvailable, mActiveItemMaxWidth);
        int extra = width - childWidth * count;
        for (int i = 0; i < count; i++) {
            mTempChildWidths[i] = childWidth;
            if (extra > 0) {
                mTempChildWidths[i]++;
                extra--;
            }
        }

        int totalWidth = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(MeasureSpec.makeMeasureSpec(mTempChildWidths[i], MeasureSpec.EXACTLY),
                    heightSpec);
            LayoutParams params = child.getLayoutParams();
            params.width = child.getMeasuredWidth();
            totalWidth += child.getMeasuredWidth();
        }
        setMeasuredDimension(
                View.resolveSizeAndState(totalWidth,
                        MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), 0),
                View.resolveSizeAndState(mItemHeight, heightSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int width = right - left;
        final int height = bottom - top;
        int used = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                child.layout(width - used - child.getMeasuredWidth(), 0, width - used, height);
            } else {
                child.layout(used, 0, child.getMeasuredWidth() + used, height);
            }
            used += child.getMeasuredWidth();
        }
    }

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    /**
     * Sets the tint which is applied to the menu items' icons.
     *
     * @param tint the tint to apply
     */
    public void setIconTintList(ColorStateList tint) {
        mItemIconTint = tint;
        if (mButtons == null) return;
        for (MyBottomNavigationItemView item : mButtons) {
            item.setIconTintList(tint);
        }
    }

    /**
     * Returns the tint which is applied to menu items' icons.
     *
     * @return the ColorStateList that is used to tint menu items' icons
     */
    @Nullable
    public ColorStateList getIconTintList() {
        return mItemIconTint;
    }

    /**
     * Sets the text color to be used on menu items.
     *
     * @param color the ColorStateList used for menu items' text.
     */
    public void setItemTextColor(ColorStateList color) {
        mItemTextColor = color;
        if (mButtons == null) return;
        for (MyBottomNavigationItemView item : mButtons) {
            item.setTextColor(color);
        }
    }

    /**
     * Returns the text color used on menu items.
     *
     * @return the ColorStateList used for menu items' text
     */
    public ColorStateList getItemTextColor() {
        return mItemTextColor;
    }

    /**
     * Sets the resource ID to be used for item background.
     *
     * @param background the resource ID of the background
     */
    public void setItemBackgroundRes(int background) {
        mItemBackgroundRes = background;
        if (mButtons == null) return;
        for (MyBottomNavigationItemView item : mButtons) {
            item.setItemBackground(background);
        }
    }

    /**
     * Returns the resource ID for the background of the menu items.
     *
     * @return the resource ID for the background
     */
    public int getItemBackgroundRes() {
        return mItemBackgroundRes;
    }

    public void setPresenter(MyBottomNavigationPresenter presenter) {
        mPresenter = presenter;
    }

    private static final String TAG = "MyBottomNavigationMenuV";

    @SuppressLint("RestrictedApi")
    public void buildMenuView() {
        removeAllViews();
        if (mButtons != null) {
            for (MyBottomNavigationItemView item : mButtons) {
                mItemPool.release(item);
            }
        }
        if (mMenu.size() == 0) {
            mSelectedItemId = 0;
            mSelectedItemPosition = 0;
            mButtons = null;
            return;
        }

        mButtons = new MyBottomNavigationItemView[mMenu.size()];
        int centerIndex = mMenu.size() / 2;
        for (int i = 0; i < mMenu.size(); i++) {
            mPresenter.setUpdateSuspended(true);
            mMenu.getItem(i).setCheckable(true);
            mPresenter.setUpdateSuspended(false);
            MyBottomNavigationItemView child = getNewItem();
            mButtons[i] = child;
            if (centerIndex == i) {
                child.setLargeIcon(true);
            } else {
                child.setLargeIcon(false);
            }
            child.setIconTintList(mItemIconTint);
            child.setTextColor(mItemTextColor);
            child.setItemBackground(mItemBackgroundRes);
            child.initialize((MenuItemImpl) mMenu.getItem(i), 0);
            child.setItemPosition(i);
            child.setOnClickListener(mOnClickListener);
            addView(child);
        }
        mSelectedItemPosition = Math.min(mMenu.size() - 1, mSelectedItemPosition);
        mMenu.getItem(mSelectedItemPosition).setChecked(true);
    }

    @SuppressLint("RestrictedApi")
    public void updateMenuView() {
        @SuppressLint("RestrictedApi") final int menuSize = mMenu.size();
        if (menuSize != mButtons.length) {
            // The size has changed. Rebuild menu view from scratch.
            buildMenuView();
            return;
        }
        int previousSelectedId = mSelectedItemId;

        for (int i = 0; i < menuSize; i++) {
            @SuppressLint("RestrictedApi") MenuItem item = mMenu.getItem(i);
            if (item.isChecked()) {
                mSelectedItemId = item.getItemId();
                mSelectedItemPosition = i;
            }
        }
        if (previousSelectedId != mSelectedItemId) {
            // Note: this has to be called before BottomNavigationItemView#initialize().
            TransitionManager.beginDelayedTransition(this, mSet);
        }

        for (int i = 0; i < menuSize; i++) {
            mPresenter.setUpdateSuspended(true);
            mButtons[i].initialize((MenuItemImpl) mMenu.getItem(i), 0);
            mPresenter.setUpdateSuspended(false);
        }

    }

    private MyBottomNavigationItemView getNewItem() {
        MyBottomNavigationItemView item = mItemPool.acquire();
        if (item == null) {
            item = new MyBottomNavigationItemView(getContext());
        }
        return item;
    }

    public int getSelectedItemId() {
        return mSelectedItemId;
    }

    void tryRestoreSelectedItemId(int itemId) {
        @SuppressLint("RestrictedApi") final int size = mMenu.size();
        for (int i = 0; i < size; i++) {
            @SuppressLint("RestrictedApi") MenuItem item = mMenu.getItem(i);
            if (itemId == item.getItemId()) {
                mSelectedItemId = itemId;
                mSelectedItemPosition = i;
                item.setChecked(true);
                break;
            }
        }
    }
}
