import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideOutItemAnimator : DefaultItemAnimator() {
    private val pendingRemovals: MutableList<RecyclerView.ViewHolder> = mutableListOf()


    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        val animator = ObjectAnimator.ofFloat(holder?.itemView, "translationX", holder?.itemView?.width?.toFloat() ?: 0f)
        animator.duration = 300
        animator.interpolator = AccelerateInterpolator()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                dispatchRemoveFinished(holder)
            }
        })
        animator.start()
        return true
    }

    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
        postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo?
    ): Boolean {
        return false
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo?,
        postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
        postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
        postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun runPendingAnimations() {
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
    }

    override fun endAnimations() {
    }

    override fun isRunning(): Boolean {
        return false
    }

// Override other necessary methods as per your requirements

    // Override other necessary methods as per your requirements

}
