package com.vip.search

import android.graphics.drawable.Drawable

/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:
 */
class SearchBean {
    var content: String? = null//文字内容
    var textColor = 0//文字颜色
    var isShowLeftIcon = false//是否展示文字右边的小图标
    var leftIcon: Drawable? = null//右侧的小图标
}