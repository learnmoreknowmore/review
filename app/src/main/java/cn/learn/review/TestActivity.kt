package cn.learn.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class TestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //FragmentStatePagerAdapter
        //FragmentPagerAdapter
    }
}
object Demo1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val a = A()
        a.show()
        val b:A = B()
        b.show2()
    }
}
internal open class A {
    open fun show() {
        show2()
    }

    open fun show2() {
        println("A")
    }
}
internal open class B : A() {
    override fun show() {
        super.show()
    }

    override fun show2() {
        println("B")
    }
}
internal class C : B() {
    override fun show() {
        super.show()
    }

    override fun show2() {
        println("C")
    }
}