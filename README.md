# **App简要介绍**：  
仿写知乎日报app，分为4个页面：主页，登陆设置页，内容页和日历界面，可以方便的查看每日的新闻及其对应的评论。  
*主页*：顶部组件，Banner，文章列表  
*内容页*：顶部组件，文章内容，评论  
*登陆设置页*：顶部组件，登录相关，切换主题及设置  
*日历界面*：顶部组件，日历  
# **App功能展示**  
![20250505-0425-00 0300051](https://github.com/user-attachments/assets/fcba56bf-be20-4e3a-a65c-218b196a2a24)
[链接](https://github.com/user-attachments/assets/fcba56bf-be20-4e3a-a65c-218b196a2a24)
# **技术亮点和自己认为写得不错的地方**   
1.采用Compose框架，使用mvvm，运用协程进行网络请求，在项目中加入深色模式的切换。  
2.在文章内容页面，点击切换按钮能够及时切换文章内容与对应的评论，较为便捷。  
3.顶部的日期及时更新，点击即可查看全年的日历  
4.顶部的banner切换较为美观，比较流畅。  
5.文章评论可以展开或折叠，便于更好的阅读文章  
# **心得体会**   
这段时间的学习学习，我接触到 Compose 框架、协程等新知识。它们为我开启安卓开发的深层大门，让我对界面构建、异步操作等有了更透彻的认知。  
这学期课堂上，不少知识晦涩难懂。课后我怀着忐忑心情向学姐请教，害怕自己的问题太过幼稚，但她总是耐心微笑，细致剖析那些令我绞尽脑汁的难题。不仅详细解答，还拓展延伸，分享诸多课堂外的实用技巧与经验。
独自摸索安卓开发时，每进一步，都更清楚自己知识匮乏。起初，我以为掌握基础界面布局和简单功能，就算入了安卓开发的门。可仿写知乎日报，处理多 API 数据整合、页面流畅度优化等复杂任务时，才惊觉自己目光短浅。  
数据处理上，从两个 API 取文章数据并在列表合并展示，看似简单，实则困难重重。我原以为课堂学的基础数据结构知识够用，可面对格式、结构复杂的数据，那些理论如同纸上谈兵。这让我深知安卓开发绝非表面简单，仅靠基础理论远远不够，需深入钻研数据处理技巧策略，才能在项目中得心应手。  
性能优化方面，页面卡顿、数据加载慢等问题频出，暴露出我在内存管理、资源加载优化等深层领域近乎空白。我明白安卓开发体系庞大复杂，细节会影响应用体验。只有不断深入探索，学习内存分析工具、异步加载原理等知识，才能提升开发水平。此次经历是转折点，激励我在安卓开发路上深耕细作，填补认知漏洞。
仿写知乎日报时，问题接踵而至：从两个 API 获取数据后列表展示的合并；列表启动内容时，Parcel 类通过 Intent 传递数据的运用；页面卡顿、数据获取低效，进而接触协程……过程中，我边做边发现问题，也不断学习新知识，深刻意识到自己知识储备不足，对诸多函数、功能了解有限。  
*Compose 看似简洁高效，上手才发现是 “甜蜜的陷阱”*，它看似比传统方式便捷，却也带来许多从未见过的问题，复杂些的还会影响其他功能。但在解决问题过程中，我对 Compose 有了更深入的理解，收获颇丰。  
# **待提升的地方**  
还有许多功能没有实现，我会逐步学习并完善这些功能的
