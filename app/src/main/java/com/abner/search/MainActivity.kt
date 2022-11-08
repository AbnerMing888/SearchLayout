package com.abner.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vip.search.SearchBean
import com.vip.search.SearchLayout
import com.vip.search.SearchList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchLayout = findViewById<SearchLayout>(R.id.search_layout)
        val searchList = findViewById<SearchList>(R.id.search_list)

        searchLayout.setOnTextSearchListener({
            //搜索内容改变

        }, {
            //软键盘点击了搜索
            searchList.doSearchContent(it)
        })

        //设置用于测试的热门搜索列表
        searchList.setHotList(getHotList())
        //热门搜索条目点击事件
        searchList.setOnHotItemClickListener { s, i ->
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        }
        //历史搜索条目点击事件
        searchList.setOnHistoryItemClickListener { s, i ->
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:模拟热门搜索列表
     */
    private val mTestHotList = arrayListOf(
        "二流小码农", "三流小可爱", "Android",
        "Kotlin", "iOS", "Java", "Python", "Php是世界上最好的语言"
    )

    private fun getHotList(): ArrayList<SearchBean> {
        return ArrayList<SearchBean>().apply {
            mTestHotList.forEachIndexed { index, s ->
                val bean = SearchBean()
                bean.content = s
                bean.isShowLeftIcon = true

                val drawable: Drawable? = if (index < 2) {
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.shape_circle_select)
                } else if (index == 2) {
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.shape_circle_ordinary)
                } else {
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.shape_circle_normal)
                }
                drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                bean.leftIcon = drawable

                add(bean)
            }
        }
    }
}