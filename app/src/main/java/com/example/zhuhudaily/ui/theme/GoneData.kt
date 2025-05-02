package com.example.zhuhudaily.ui.theme


import com.google.gson.annotations.SerializedName

/**
{
  "request_id": "776474670707621888",
  "success": true,
  "message": "success",
  "code": 200,
  "data": {
    "date": "20250429",
    "stories": [
      {
        "id": 9780783,
        "url": "https://daily.zhihu.com/story/9780783",
        "hint": "岭云 · 1 分钟阅读",
        "type": 0,
        "title": "石头缝里都能长树，为什么草原不长树？",
        "images": [
          "https://picx.zhimg.com/v2-89d71f8ed9c41b04312dbd72352afb3c.jpg?source=8673f162"
        ],
        "ga_prefix": "042807",
        "image_hue": "0x95a740"
      },
      {
        "id": 9780772,
        "url": "https://daily.zhihu.com/story/9780772",
        "hint": "漩涡鸣人yy · 1 分钟阅读",
        "type": 0,
        "title": "为什么四大家鱼是青草鲢鳙，而没有鲫鱼和鲤鱼？",
        "images": [
          "https://pic1.zhimg.com/v2-485014d8164f9390a27dce06306c2c70.jpg?source=8673f162"
        ],
        "ga_prefix": "042807",
        "image_hue": "0x2d3a40"
      },
      {
        "id": 9780775,
        "url": "https://daily.zhihu.com/story/9780775",
        "hint": "斜绿天蛾 · 1 分钟阅读",
        "type": 0,
        "title": "为什么会有卖活蟑螂的？或者说，买蟑螂能用来干什么？",
        "images": [
          "https://picx.zhimg.com/v2-d51d65c3d46bac3601f5fb295b9252f1.jpg?source=8673f162"
        ],
        "ga_prefix": "042807",
        "image_hue": "0x211817"
      },
      {
        "id": 9780781,
        "url": "https://daily.zhihu.com/story/9780781",
        "hint": "VOL.3647",
        "type": 0,
        "title": "瞎扯 · 如何正确地吐槽",
        "images": [
          "https://picx.zhimg.com/v2-6f7fc115fc9b281e845a89a66ee960f6.jpg?source=8673f162"
        ],
        "ga_prefix": "042806",
        "image_hue": "0x665739"
      }
    ],
    "top_stories": [
      {
        "id": 9780783,
        "url": "https://daily.zhihu.com/story/9780783",
        "hint": "作者 / 岭云",
        "type": 0,
        "image": "https://picx.zhimg.com/v2-d02d27f8c0adeca6600904c40dd6d3ff.jpg?source=8673f162",
        "title": "石头缝里都能长树，为什么草原不长树？",
        "ga_prefix": "042807",
        "image_hue": "0x95a740"
      },
      {
        "id": 9780765,
        "url": "https://daily.zhihu.com/story/9780765",
        "hint": "作者 / 大宝",
        "type": 0,
        "image": "https://pic1.zhimg.com/v2-17e74615fabda2120191594970c4830b.jpg?source=8673f162",
        "title": "为什么感觉高压锅炖出来的排骨没有肉味，而且味道有点淡不太好吃？",
        "ga_prefix": "042707",
        "image_hue": "0xaeaeae"
      },
      {
        "id": 9780735,
        "url": "https://daily.zhihu.com/story/9780735",
        "hint": "作者 / Archimon",
        "type": 0,
        "image": "https://pic1.zhimg.com/v2-e6cb721d36e590cd49f4698a9e18b3ba.jpg?source=8673f162",
        "title": "眼镜没发明出来之前，眼镜蛇叫什么？",
        "ga_prefix": "042607",
        "image_hue": "0x9c986d"
      },
      {
        "id": 9780684,
        "url": "https://daily.zhihu.com/story/9780684",
        "hint": "作者 / 赵泠",
        "type": 0,
        "image": "https://pic1.zhimg.com/v2-7d0fa1a999d411cb8a9900880890122b.jpg?source=8673f162",
        "title": "训练一个由骨架输出动物外貌的大模型，是不是就能完美还原恐龙了?",
        "ga_prefix": "042507",
        "image_hue": "0x2b271e"
      },
      {
        "id": 9780677,
        "url": "https://daily.zhihu.com/story/9780677",
        "hint": "作者 / 冯.若衣卖",
        "type": 0,
        "image": "https://pic1.zhimg.com/v2-1f6eacca593164fbf17b3fdd2baf4304.jpg?source=8673f162",
        "title": "为什么中国南宋就有了活字印刷，明清还要抄书呢？",
        "ga_prefix": "042407",
        "image_hue": "0xb3b3b3"
      }
    ]
  },
  "time": 1746085987,
  "usage": 0
}
*/
data class GoneData(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // success
    @SerializedName("request_id")
    val requestId: String, // 776474670707621888
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("time")
    val time: Int, // 1746085987
    @SerializedName("usage")
    val usage: Int // 0
) {
    data class Data(
        @SerializedName("date")
        val date: String, // 20250429
        @SerializedName("stories")
        val stories: List<Story>,
        @SerializedName("top_stories")
        val topStories: List<TopStory>
    ) {
        data class Story(
            @SerializedName("ga_prefix")
            val gaPrefix: String, // 042807
            @SerializedName("hint")
            val hint: String, // 岭云 · 1 分钟阅读
            @SerializedName("id")
            val id: Int, // 9780783
            @SerializedName("image_hue")
            val imageHue: String, // 0x95a740
            @SerializedName("images")
            val images: List<String>,
            @SerializedName("title")
            val title: String, // 石头缝里都能长树，为什么草原不长树？
            @SerializedName("type")
            val type: Int, // 0
            @SerializedName("url")
            val url: String // https://daily.zhihu.com/story/9780783
        )

        data class TopStory(
            @SerializedName("ga_prefix")
            val gaPrefix: String, // 042807
            @SerializedName("hint")
            val hint: String, // 作者 / 岭云
            @SerializedName("id")
            val id: Int, // 9780783
            @SerializedName("image")
            val image: String, // https://picx.zhimg.com/v2-d02d27f8c0adeca6600904c40dd6d3ff.jpg?source=8673f162
            @SerializedName("image_hue")
            val imageHue: String, // 0x95a740
            @SerializedName("title")
            val title: String, // 石头缝里都能长树，为什么草原不长树？
            @SerializedName("type")
            val type: Int, // 0
            @SerializedName("url")
            val url: String // https://daily.zhihu.com/story/9780783
        )
    }
}