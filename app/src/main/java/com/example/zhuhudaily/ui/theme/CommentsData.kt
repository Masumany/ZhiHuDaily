package com.example.zhuhudaily.ui.theme


import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
{
  "request_id": "776851469736284160",
  "success": true,
  "message": "success",
  "code": 200,
  "data": {
    "comments": [
      {
        "author": "朱宇飞",
        "content": "那时候酒度数低",
        "avatar": "https://picx.zhimg.com/v2-7b91c81d88f72d331a597b0ae9259b56_l.jpg?source=8673f162",
        "time": 1746078254,
        "id": 34295691,
        "likes": 0
      },
      {
        "author": "Winston张",
        "content": "今天是第三天，分享个热知识，香辣蟹和冰雪碧不能共用，但是小龙虾和冰雪碧却可以。",
        "avatar": "https://pica.zhimg.com/v2-72a4090a5010a86cf428939fd7b9c8eb_l.jpg?source=8673f162",
        "time": 1746036907,
        "id": 34295664,
        "likes": 0
      },
      {
        "author": "星空星空199",
        "content": "哈哈 这可不是一般的酒肉朋友",
        "avatar": "https://picx.zhimg.com/v2-fdf6764ccd53d791c9a53b0c2137656f_l.jpg?source=8673f162",
        "time": 1745984399,
        "id": 34295544,
        "likes": 1
      },
      {
        "author": "日报用户",
        "content": "酒肉穿肠过，\n便秘又癌瘤。",
        "avatar": "https://pica.zhimg.com/v2-5fc67a2efe2e8f52b40fac8a80da1442_l.jpg?source=8673f162",
        "time": 1745982944,
        "id": 34295526,
        "likes": 4
      },
      {
        "author": "不辣的皮特",
        "content": "本篇作者还应包括司马迁、施耐庵",
        "avatar": "https://picx.zhimg.com/da8e974dc_l.jpg?source=8673f162",
        "time": 1745978155,
        "id": 34295515,
        "likes": 5
      },
      {
        "author": "Kim-Wexler",
        "content": "大段大段复制粘贴凑字数呢",
        "avatar": "https://picx.zhimg.com/v2-e436fc7d7060bb6436bd47d683dba68d_l.jpg?source=8673f162",
        "time": 1745971756,
        "id": 34295468,
        "likes": 3
      },
      {
        "author": "Tom Cat",
        "content": "还有狗肉",
        "avatar": "https://picx.zhimg.com/03221b4d6e79382a51bb7c4a605f6b4d_l.jpg?source=8673f162",
        "time": 1745969678,
        "id": 34295429,
        "likes": 2
      }
    ]
  },
  "time": 1746175823,
  "usage": 0
}
*/
data class CommentsData(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // success
    @SerializedName("request_id")
    val requestId: String, // 776851469736284160
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("time")
    val time: Int, // 1746175823
    @SerializedName("usage")
    val usage: Int // 0
) : Serializable {
    data class Data(
        @SerializedName("comments")
        val comments: List<Comment>
    ) :Serializable{
        data class Comment(
            @SerializedName("author")
            val author: String, // 朱宇飞
            @SerializedName("avatar")
            val avatar: String, // https://picx.zhimg.com/v2-7b91c81d88f72d331a597b0ae9259b56_l.jpg?source=8673f162
            @SerializedName("content")
            val content: String, // 那时候酒度数低
            @SerializedName("id")
            val id: Int, // 34295691
            @SerializedName("likes")
            val likes: Int, // 0
            @SerializedName("time")
            val time: Int // 1746078254
        ):Serializable
    }
}