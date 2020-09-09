package cn.learn.review

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.learn.review.rx.RxBus
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.setOnClickListener {
           startPageActivity()
        }
    }
    private fun startPageActivity(){
        var classname = "cn.learn.paging.PagingActivity"
        startActivity(Intent(this, Class.forName(classname)))
    }
    private fun startSecondActivity(){
        startActivity(Intent(this,TestActivity::class.java))
    }
    /**
     * 调用拨号
     */
    private fun call() {
        var uri: Uri = Uri.parse("tel:10010")
        var intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)

        // 直接拨打电话，需要加上权限 <uses-permission id="android.permission.CALL_PHONE" />
//        val callUri = Uri.parse("tel:10010")
//        val intent = Intent(Intent.ACTION_CALL, callUri)
    }

    /**
     * 发送短信
     */
    private fun sendMessage(){
        // 给10010发送内容为“Hello”的短信
        val uri = Uri.parse("smsto:10010")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", "Hello")
        startActivity(intent)
    }
    /**
     * 发送彩信
     */
    private fun sendImageMessage(){
        // 发送彩信（相当于发送带附件的短信）
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra("sms_body", "Hello")
        val uri = Uri.parse("content://media/external/images/media/23")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/png"
        startActivity(intent)
    }
    /**
     * 打开网页
     */
    private fun openURL(){
        // 打开百度主页
        val uri = Uri.parse("https://www.baidu.com")
        val uri1 = Uri.parse("https://zrh.1001if.com/product/")
        val intent = Intent(Intent.ACTION_VIEW, uri1)
        startActivity(intent)
    }
    /**
     * 发送电子邮件
     */
    private fun sendEmail(){
        // 给someone@domain.com发邮件
        val uri = Uri.parse("mailto:cs376246940@163.com")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)

        // 给someone@domain.com发邮件发送内容为“Hello”的邮件
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.putExtra(Intent.EXTRA_EMAIL, "someone@domain.com")
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
//        intent.putExtra(Intent.EXTRA_TEXT, "Hello")
//        intent.type = "text/plain"
//        startActivity(intent)
    }
    private fun openMap(){
        // 打开Google地图中国北京位置（北纬39.9，东经116.3）
        val uri = Uri.parse("geo:39.9,116.3")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    private fun openMapPlan(){
        // 路径规划：从北京某地（北纬39.9，东经116.3）到上海某地（北纬31.2，东经121.4）
        val uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=39.9 116.3&daddr=31.2 121.4")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun rxCall(){

        RxBus.instance.toObservable().doOnDispose(Action {
            /**
             * 解除绑定
             */
        }).compose(this.bindUntilEvent(ActivityEvent.DESTROY)).subscribe(Consumer {

        })
    }
}