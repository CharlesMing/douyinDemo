package com.example.admin.myapplication.widget.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.myapplication.R;

public class MyBottomNavigationItemView extends FrameLayout implements MenuView.ItemView {
    public static final int INVALID_ITEM_POSITION = -1;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};


    private ImageView mIcon;
    private ImageView mIconLarge;
    private final TextView mLargeLabel;
    private int mItemPosition = INVALID_ITEM_POSITION;
    private MenuItemImpl mItemData;

    private ColorStateList mIconTint;
    private boolean mIsSetLargeIcon = false;

    public MyBottomNavigationItemView(@NonNull Context context) {
        this(context, null);
    }

    public MyBottomNavigationItemView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBottomNavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.design_bottom_navigation_item, this, true);
        setBackgroundResource(android.support.design.R.drawable.design_bottom_navigation_item_background);
        mIcon = findViewById(android.support.design.R.id.icon);
        mIconLarge = findViewById(R.id.iconLarge);
        mLargeLabel = findViewById(android.support.design.R.id.largeLabel);
    }

    @Override
    public void initialize(MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitle());
        setId(itemData.getItemId());
        setContentDescription(itemData.getContentDescription());
        TooltipCompat.setTooltipText(this, itemData.getTooltipText());
    }

    public void setItemPosition(int position) {
        mItemPosition = position;
    }

    public int getItemPosition() {
        return mItemPosition;
    }

    @Override
    public MenuItemImpl getItemData() {
        return mItemData;
    }

    @Override
    public void setTitle(CharSequence title) {
        mLargeLabel.setText(title);
    }

    @Override
    public void setCheckable(boolean checkable) {
        refreshDrawableState();
    }

    @Override
    public void setChecked(boolean checked) {

    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        mLargeLabel.setEnabled(enabled);
        mIcon.setEnabled(enabled);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mItemData != null && mItemData.isCheckable() && mItemData.isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    @Override
    public void setIcon(Drawable icon) {
        if (icon != null) {
            Drawable.ConstantState state = icon.getConstantState();
            icon = DrawableCompat.wrap(state == null ? icon : state.newDrawable()).mutate();
            DrawableCompat.setTintList(icon, mIconTint);
        }
        if (mIsSetLargeIcon) {
            mIconLarge.setImageDrawable(icon);
        } else {
            mIcon.setImageDrawable(icon);
        }
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @Override
    public boolean showsIcon() {
        return true;
    }

    public void setIconTintList(ColorStateList tint) {
        mIconTint = tint;
        if (mItemData != null) {
            // Update the icon so that the tint takes effect
            setIcon(mItemData.getIcon());
        }
    }

    public void setTextColor(ColorStateList color) {
        mLargeLabel.setTextColor(color);
    }

    public void setLargeIcon(boolean largeIcon) {
        this.mIsSetLargeIcon = largeIcon;
        if (mIsSetLargeIcon) {
            mIcon.setVisibility(INVISIBLE);
            mIconLarge.setVisibility(VISIBLE);
        }
    }


    public void setItemBackground(int background) {
        Drawable backgroundDrawable = background == 0
                ? null : ContextCompat.getDrawable(getContext(), background);
        ViewCompat.setBackground(this, backgroundDrawable);
    }
}
