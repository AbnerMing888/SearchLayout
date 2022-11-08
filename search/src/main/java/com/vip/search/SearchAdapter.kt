package com.vip.search

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:适配器
 */
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private var mItemTopMargin = 0f
    private var mSearchList = ArrayList<SearchBean>()
    private var mContext: Context? = null
    private var mViewCenter = false
    private var mPaddingLeft = 0
    private var mPaddingRight = 0
    private var mPaddingTop = 0
    private var mPaddingBottom = 0
    private var mItemLine = 1//文字展示几行
    private var mItemBg = 0

    constructor(
        context: Context
    ) : super() {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(View.inflate(mContext, R.layout.search_text, null))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchBean = mSearchList[position]
        holder.mTvContent?.apply {
            text = searchBean.content
            //设置背景
            if (mItemBg != 0) {
                background =
                    ContextCompat.getDrawable(mContext!!, mItemBg)
            }

            //添加右边的小图标
            if (searchBean.isShowLeftIcon && searchBean.leftIcon != null) {
                setCompoundDrawables(searchBean.leftIcon, null, null, null)
                compoundDrawablePadding = 10
            } else {
                setCompoundDrawables(null, null, null, null)
            }

            //选中 改变文字颜色
            if (searchBean.textColor == 0) {
                setTextColor(ContextCompat.getColor(mContext!!, R.color.color_666666))
            } else {
                setTextColor(ContextCompat.getColor(mContext!!, searchBean.textColor))
            }
            //点击事件
            setOnClickListener {
                //点击事件
                mOnItemClickListener?.let {
                    it.itemClick(searchBean.content!!, position)
                }
            }

            //设置居中还是默认模式
            val params = layoutParams as ConstraintLayout.LayoutParams
            if (mViewCenter) {
                params.rightToRight = 0
            } else {
                params.rightToRight = -1
            }
            //设置文字距离上边的距离
            if (mItemTopMargin != 0f) {
                params.topMargin = mItemTopMargin.toInt()
            }
            layoutParams = params

            //设置内边距
            setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            //设置文字居中
            maxLines = mItemLine
        }


    }

    override fun getItemCount(): Int {
        return mSearchList.size
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置数据
     */
    fun setList(searchList: ArrayList<SearchBean>) {
        mSearchList = searchList
        notifyDataSetChanged()
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置属性
     */
    fun setViewCenter(viewCenter: Boolean) {
        mViewCenter = viewCenter
    }


    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTvContent: TextView? = null

        init {
            mTvContent = itemView.findViewById(R.id.tv_content)
        }
    }

    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(click: (String, Int) -> Unit) {
        mOnItemClickListener = object : OnItemClickListener {
            override fun itemClick(text: String, position: Int) {
                click(text, position)
            }
        }
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置文字的内边距
     */
    fun setItemPadding(
        paddingLeft: Float,
        paddingTop: Float,
        paddingRight: Float,
        paddingBottom: Float
    ) {
        mPaddingLeft = paddingLeft.toInt()
        mPaddingTop = paddingTop.toInt()
        mPaddingRight = paddingRight.toInt()
        mPaddingBottom = paddingBottom.toInt()
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置文字显示几行
     */
    fun setItemLine(mHotItemLine: Int) {
        mItemLine = mHotItemLine
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置背景色
     */
    fun setItemBg(itemBg: Int) {
        mItemBg = itemBg
    }

    /**
     * AUTHOR:AbnerMing
     * INTRODUCE:设置距离上边的距离
     */
    fun setItemTopMargin(itemTopMargin: Float) {
        mItemTopMargin = itemTopMargin
    }
}