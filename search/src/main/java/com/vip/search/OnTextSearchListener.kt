package com.vip.search

/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:监听是文字改变还是搜索点击
 */
interface OnTextSearchListener {
    fun textChanged(content: String)//文字改变
    fun clickSearch(content: String)//点击了搜索
}