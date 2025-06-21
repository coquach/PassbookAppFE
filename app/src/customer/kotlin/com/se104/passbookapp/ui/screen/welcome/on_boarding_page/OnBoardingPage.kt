package com.se104.passbookapp.ui.screen.welcome.on_boarding_page

import androidx.annotation.DrawableRes
import com.se104.passbookapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    data object First : OnBoardingPage(
        image = R.drawable.welcome_first,
        title = "Gửi tiết kiệm",
        description = "Tạo sổ tiết kiệm nhanh chóng,\nlãi suất minh bạch, an toàn!"
    )

    data object Second : OnBoardingPage(
        image = R.drawable.welcome_second,
        title = "Nạp & Rút tiền",
        description = "Giao dịch linh hoạt,\nchỉ với vài thao tác đơn giản!"
    )

    data object Third : OnBoardingPage(
        image = R.drawable.welcome_third,
        title = "Theo dõi giao dịch",
        description = "Xem lại lịch sử, kiểm tra mọi giao dịch rõ ràng & minh bạch!"
    )
}