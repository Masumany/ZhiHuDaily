package com.example.zhuhudaily.ui.theme

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BannerData(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // success
    @SerializedName("request_id")
    val requestId: String, // 776420338473750528
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("time")
    val time: Int, // 1746073033
    @SerializedName("usage")
    val usage: Int // 0
) : Serializable {
    data class Data(
        @SerializedName("date")
        val date: String, // 20250501
        @SerializedName("stories")
        val stories: List<Story>,
        @SerializedName("top_stories")
        val topStories: List<TopStory>
    ) :Serializable{
        data class Story(
            @SerializedName("ga_prefix")
            val gaPrefix: String, // 043007
            @SerializedName("hint")
            val hint: String, // 江南 · 1 分钟阅读
            @SerializedName("id")
            val id: Int, // 9780825
            @SerializedName("image_hue")
            val imageHue: String, // 0xb3937d
            @SerializedName("images")
            val images: List<String>,
            @SerializedName("title")
            val title: String, // 历史上有哪些经典的食物组合？
            @SerializedName("type")
            val type: Int, // 0
            @SerializedName("url")
            val url: String // https://daily.zhihu.com/story/9780825
        ):Serializable

        data class TopStory(
            @SerializedName("ga_prefix")
            val gaPrefix: String, // 043007
            @SerializedName("hint")
            val hint: String, // 作者 / 江南
            @SerializedName("id")
            val id: Int, // 9780825
            @SerializedName("image")
            val image: String, // https://pic1.zhimg.com/v2-eeb10b9de18f5eb40fd78807f06122ac.jpg?source=8673f162
            @SerializedName("image_hue")
            val imageHue: String, // 0xb3937d
            @SerializedName("title")
            val title: String, // 历史上有哪些经典的食物组合？
            @SerializedName("type")
            val type: Int, // 0
            @SerializedName("url")
            val url: String // https://daily.zhihu.com/story/9780825
        ):java.io.Serializable
    }
}