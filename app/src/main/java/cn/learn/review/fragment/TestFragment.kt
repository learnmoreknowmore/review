package cn.learn.review.fragment

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * Fragment 返回键拦截
有时候，您需要阻止用户返回上一级。 在这种情况下，您需要在 Activity 中重写 onBackPressed() 方法。
但是，当您使用 Fragment 时，没有直接的方法来拦截返回。
在 Fragment 类中没有可用的 onBackPressed() 方法，这是为了防止同时存在多个 Fragment 时发生意外行为。
但是，从 AndroidX Activity 1.0.0 开始，您可以使用 OnBackPressedDispatcher 在您可以访问该 Activity 的代码的任何位置（例如，在 Fragment 中）注册 OnBackPressedCallback。
 */
class TestFragment: Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        var callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }
    companion object{
        private var fragment:TestFragment? = null
        private const val arg = "ARG"
        fun getInstance(args:String) = TestFragment().apply {
            arguments = Bundle().apply {
                putString(arg,args)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    /**
     * FragmentFactory 的使用
    过去，我们只能使用其默认的空构造函数实例化Fragment实例。
    这是因为在某些情况下，例如配置更改和应用程序的流程重新创建，系统需要重新初始化。
    如果不是默认的构造方法，系统将不知道如何重新初始化Fragment实例。
    创建FragmentFactory来解决此限制。 通过向其提供实例化Fragment所需的必要参数/依赖关系，它可以帮助系统创建Fragment实例。
     */
    class MyFragmentFactory(private var args: String) : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            if (className == TestFragment::class.simpleName){
                return TestFragment.getInstance(args)
            }
            return super.instantiate(classLoader, className)
        }
    }
}

