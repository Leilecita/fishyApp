package com.leila.android.fishy.Interfaces;

public interface ItemTouchHelperAdapter {
    boolean isDragEnabled();

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
