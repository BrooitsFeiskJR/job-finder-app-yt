package dev.tontech.job_finder_yt.ui.adapters.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingItemDecoration(
    private val spacing: Int,
    private val includeEdge: Boolean,
    private val headerNum: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) - headerNum
        if (position >= 0) {
            if (includeEdge) {
                outRect.left = if (position == 0) spacing else spacing / 2
                outRect.right = if (position == state.itemCount - 1) spacing else spacing / 2
            } else {
                outRect.left = if (position == 0) 0 else spacing / 2
                outRect.right = if (position == state.itemCount - 1) 0 else spacing / 2
            }
        } else {
            outRect.setEmpty()
        }
    }
}