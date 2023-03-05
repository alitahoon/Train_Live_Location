
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.trainlivelocation.ui.CriticalPosts
import com.example.trainlivelocation.ui.NoneCriticalPost


class PostsMainSectionsAdapter(context: Context, fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {
    private val mContext: Context

    init {
        mContext = context.getApplicationContext()
    }

    override fun getItem(position: Int): Fragment {
        return when (TABS[position]) {
            critacl -> return CriticalPosts()
            none_critical -> return NoneCriticalPost()
            else -> {
                return CriticalPosts()
            }
        }
    }

    override fun getCount(): Int {
        return TABS.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (TABS[position]) {
            critacl -> return ""
            none_critical -> return ""
        }
        return null
    }

    companion object {
        private const val critacl = 0
        private const val none_critical = 1
        private val TABS = intArrayOf(critacl, none_critical)
    }
}