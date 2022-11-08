package com.vip.search

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import org.json.JSONArray
import org.json.JSONObject

/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:搜索列表
 */
class SearchList : LinearLayout {
    private var mHotMarginTop = 0f
    private var mHistoryMarginTop = 0f
    private var mSearchHistoryTag: TextView? = null
    private var mSearchHotTag: TextView? = null
    private var mHotItemLine = 2
    private var mHotPaddingLeft = 0f
    private var mHotPaddingTop = 0f
    private var mHotPaddingRight = 0f
    private var mHotPaddingBottom = 0f
    private var mHistoryPaddingLeft = 0f
    private var mHistoryPaddingRight = 0f
    private var mHistoryPaddingTop = 0f
    private var mHistoryPaddingBottom = 0f
    private var mSearchHistoryClear: TextView? = null
    private var mHistoryClearColor = 0
    private var mHistoryClearSize = 0f
    private var mHistoryClearText: String? = null
    private var mHistoryClearIcon: Drawable? = null
    private var mVisibilityHistoryClear = false
    private var mHotItemBg: Drawable? = null
    private var mRlSearchHistoryTag: RelativeLayout? = null
    private var mHistoryCenter = false
    private var mHotCenter = false
    private var mHistoryGridSpanCount = 2
    private var mHistoryFlexBoxOrGrid = false
    private var mHotGridSpanCount = 2//热门搜索列表 网格样式 默认条目数
    private var mHotFlexBoxOrGrid = false
    private var mHistorySearchAdapter: SearchAdapter? = null
    private var mHotSearchAdapter: SearchAdapter? = null
    private var mRvHot: RecyclerView? = null
    private var mRvHistory: RecyclerView? = null
    private var mContext: Context? = null
    private var mHistoryListSize = 10//默认的历史搜索列表数量

    constructor(
        context: Context
    ) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val os = context.obtainStyledAttributes(attrs, R.styleable.SearchList)
        //初始化历史搜索属性
        initHistoryAttrs(os)
        //初始化热门搜索属性
        initHotAttrs(os)
        init(context)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:初始化热门搜索属性
     */
    private fun initHotAttrs(ta: TypedArray) {
        //是网格还是流式布局
        mHotFlexBoxOrGrid = ta.getBoolean(R.styleable.SearchList_is_hot_flex_box_or_grid, false)
        //文字是否居中
        mHotCenter = ta.getBoolean(R.styleable.SearchList_is_hot_center, false)
        //如果是网格形式，条目数
        mHotGridSpanCount = ta.getInt(R.styleable.SearchList_hot_grid_span_count, 2)
        //item背景
        mHotItemBg = ta.getDrawable(R.styleable.SearchList_hot_item_bg)

        //item 文字 展示几行
        mHotItemLine = ta.getInt(R.styleable.SearchList_hot_item_line, 2)

        //内边距 左
        mHotPaddingLeft =
            ta.getDimension(R.styleable.SearchList_hot_padding_left, 0f)
        //内边距 上
        mHotPaddingTop =
            ta.getDimension(R.styleable.SearchList_hot_padding_top, 0f)
        //内边距 右
        mHotPaddingRight =
            ta.getDimension(R.styleable.SearchList_hot_padding_right, 0f)
        //内边距 下
        mHotPaddingBottom =
            ta.getDimension(R.styleable.SearchList_hot_padding_bottom, 0f)

        //外边距 上
        mHotMarginTop =
            ta.getDimension(R.styleable.SearchList_hot_item_margin_top, 20f)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:初始化历史搜索属性
     */
    private fun initHistoryAttrs(ta: TypedArray) {
        //是网格还是流式布局
        mHistoryFlexBoxOrGrid =
            ta.getBoolean(R.styleable.SearchList_is_history_flex_box_or_grid, false)
        //历史搜索流式布局中，最多展示几个item
        mHistoryListSize = ta.getInt(R.styleable.SearchList_history_flex_box_count, 10)
        //文字是否居中
        mHistoryCenter = ta.getBoolean(R.styleable.SearchList_is_history_center, false)
        //如果是网格形式，条目数
        mHistoryGridSpanCount = ta.getInt(R.styleable.SearchList_history_grid_span_count, 2)
        //是否显示右边的清除小图标
        mVisibilityHistoryClear =
            ta.getBoolean(R.styleable.SearchList_is_visibility_history_clear, false)
        //右边的清除小图标
        mHistoryClearIcon = ta.getDrawable(R.styleable.SearchList_history_clear_icon)
        //右边的清除文字
        mHistoryClearText = ta.getString(R.styleable.SearchList_history_clear_text)
        //右边的清除文字 大小
        mHistoryClearSize =
            ta.getDimension(R.styleable.SearchList_history_clear_size, 0f)
        //右边的清除文字 颜色
        mHistoryClearColor =
            ta.getColor(R.styleable.SearchList_history_clear_color, 0)
        //内边距 左
        mHistoryPaddingLeft =
            ta.getDimension(R.styleable.SearchList_history_padding_left, 15f)
        //内边距 上
        mHistoryPaddingTop =
            ta.getDimension(R.styleable.SearchList_history_padding_top, 5f)
        //内边距 右
        mHistoryPaddingRight =
            ta.getDimension(R.styleable.SearchList_history_padding_right, 15f)
        //内边距 下
        mHistoryPaddingBottom =
            ta.getDimension(R.styleable.SearchList_history_padding_bottom, 5f)

        //外边距 上
        mHistoryMarginTop =
            ta.getDimension(R.styleable.SearchList_history_item_margin_top, 20f)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:初始化视图
     */
    private fun init(context: Context) {
        mContext = context
        val view = View.inflate(context, R.layout.search_list, null)
        mRvHot = view.findViewById(R.id.rv_hot)
        mRvHistory = view.findViewById(R.id.rv_history)
        mRlSearchHistoryTag = view.findViewById(R.id.rl_search_history_tag)
        mSearchHistoryClear = view.findViewById(R.id.tv_search_history_clear)
        mSearchHistoryTag = view.findViewById(R.id.tv_search_history_tag)
        mSearchHotTag = view.findViewById(R.id.tv_search_hot_tag)
        initSearchHistoryClear()
        initData()
        addView(view)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:初始化历史搜索右侧的属性
     */
    private fun initSearchHistoryClear() {
        mSearchHistoryClear?.apply {
            setOnClickListener {
                //清除全部
                SearchSharedPreUtils.put(mContext!!, "search_history", "")
                showOrHideHistoryLayout(View.GONE)
            }
            //展示小图标
            if (mVisibilityHistoryClear) {
                if (mHistoryClearIcon === null) {
                    //设置小图标
                    mHistoryClearIcon =
                        ContextCompat.getDrawable(mContext!!, R.drawable.view_ic_clear)
                }

                mHistoryClearIcon?.setBounds(
                    0,
                    0,
                    mHistoryClearIcon!!.minimumWidth,
                    mHistoryClearIcon!!.minimumHeight
                )
                setCompoundDrawables(mHistoryClearIcon, null, null, null)
                compoundDrawablePadding = 10
            }


            //文字大小
            if (mHistoryClearSize != 0f) {
                textSize = mHistoryClearSize
            }
            //文字颜色
            if (mHistoryClearColor != 0) {
                setTextColor(mHistoryClearColor)
            }
            //设置文字
            mHistoryClearText?.let {
                text = it
            }

        }
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:隐藏历史列表布局
     */
    private fun showOrHideHistoryLayout(type: Int) {
        mRlSearchHistoryTag?.visibility = type
        mRvHistory?.visibility = type
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:初始化数据
     */
    private fun initData() {
        //历史记录
        if (mHistoryFlexBoxOrGrid) {
            mRvHistory?.layoutManager = GridLayoutManager(mContext, mHistoryGridSpanCount)
        } else {
            mRvHistory?.layoutManager = FlexboxLayoutManager(mContext)
        }
        mHistorySearchAdapter = SearchAdapter(mContext!!)
        mRvHistory?.adapter = mHistorySearchAdapter
        mHistorySearchAdapter?.apply {
            //设置是否居中
            setViewCenter(mHistoryCenter)
            //设置文字距离上边的距离
            setItemTopMargin(mHistoryMarginTop)

            //设置内边距
            setItemPadding(
                mHistoryPaddingLeft, mHistoryPaddingTop, mHistoryPaddingRight, mHistoryPaddingBottom
            )
        }
        //设置item 背景
        setHistoryItemBg(R.drawable.shape_solid_10)

        //热门搜索
        if (mHotFlexBoxOrGrid) {
            mRvHot?.layoutManager = GridLayoutManager(mContext, mHotGridSpanCount)
        } else {
            mRvHot?.layoutManager = FlexboxLayoutManager(mContext)
        }
        mHotSearchAdapter = SearchAdapter(mContext!!)
        mRvHot?.adapter = mHotSearchAdapter

        mHotSearchAdapter?.apply {
            //设置是否居中
            setViewCenter(mHotCenter)
            //设置内边距
            setItemPadding(
                mHotPaddingLeft, mHotPaddingTop, mHotPaddingRight, mHotPaddingBottom
            )
            //设置展示几行
            setItemLine(mHotItemLine)
            //设置文字距离上边的距离
            setItemTopMargin(mHotMarginTop)
        }

        //首次进来获取历史搜索
        doSearchContent("")

    }


    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置热门列表
     */
    fun setHotList(list: ArrayList<SearchBean>) {
        mHotSearchAdapter?.setList(list)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:点击了搜索，需要做的是添加至历史搜索中
     */
    fun doSearchContent(it: String) {
        val searchHistory = getSearchHistory()

        if (!TextUtils.isEmpty(it)) {
            val jsonArray: JSONArray = if (TextUtils.isEmpty(searchHistory)) {
                JSONArray()
            } else {
                JSONArray(searchHistory)
            }

            val json = JSONObject()
            json.put("content", it)

            //如果出现了一样的，删除后，加到第一个
            var isEqual = false
            var equalPosition = 0
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val content = item.getString("content")
                if (it == content) {
                    isEqual = true
                    equalPosition = i
                    break
                }
            }
            //有一样的
            if (isEqual) {
                jsonArray.remove(equalPosition)
            } else {
                //超过了指定的阀门之后，就不在扩充
                if (jsonArray.length() >= mHistoryListSize) {
                    jsonArray.remove(0)
                }
            }

            jsonArray.put(json)

            SearchSharedPreUtils.put(mContext!!, "search_history", jsonArray.toString())
        }

        getSearchHistory()?.let {
            eachSearchHistory(it)
        }
        //两个有一个不为空，展示
        if (!TextUtils.isEmpty(it) || !TextUtils.isEmpty(searchHistory)) {
            showOrHideHistoryLayout(View.VISIBLE)
        }

    }

    private fun getSearchHistory(): String? {
        return SearchSharedPreUtils.getString(mContext!!, "search_history")
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:遍历历史搜索的历史记录
     */
    private fun eachSearchHistory(searchContent: String) {
        val list = ArrayList<SearchBean>()
        if (TextUtils.isEmpty(searchContent)) {
            list.clear()
        } else {
            val jsonArray = JSONArray(searchContent)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val bean = SearchBean()
                bean.content = item.getString("content")
                list.add(0, bean)
            }
        }
        mHistorySearchAdapter?.setList(list)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:热门搜索条目点击
     */
    fun setOnHotItemClickListener(click: (String, Int) -> Unit) {
        mHotSearchAdapter?.setOnItemClickListener { s, i ->
            click(s, i)
        }
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:历史搜索条目点击
     */
    fun setOnHistoryItemClickListener(click: (String, Int) -> Unit) {
        mHistorySearchAdapter?.setOnItemClickListener { s, i ->
            click(s, i)
        }
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置历史搜索背景
     */
    fun setHistoryItemBg(historyItemBg: Int) {
        mHistorySearchAdapter?.setItemBg(historyItemBg)
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:获取历史搜索tag右边的清除按钮
     */
    fun getHistoryClearTextView(): TextView {
        return mSearchHistoryClear!!
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:获取历史搜索tag
     */
    fun getHistoryTag(): TextView? {
        return mSearchHistoryTag
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:获取热门搜索tag
     */
    fun getHotTag(): TextView? {
        return mSearchHotTag
    }
}