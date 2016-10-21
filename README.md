# Luban-Circle-Demo
仿朋友圈按顺序上传图片至七牛，基于luban算法压缩

![](https://github.com/huijimuhe/Luban-Circle-Demo/blob/master/art/device-2016-08-05-122301.png) 

#Intro

这是一个完整的类似朋友圈的图片选择、压缩、上传的demo，改改就能拿来用。修改了luban项目，扣掉rxjava部分，采用线程池，在销毁时用shutDownNow()即可。图片上传的顺序与用户选择的顺序一致，这是本demo比较有趣的地方。

****很多朋友加我qq询问oom的问题，所以补充说明一下****

经过测试，该问题是demo使用的luban算法压缩造成的oom，不用压缩直接上传没有出现该问题，因此遇到此问题的朋友可以试试将luban压缩算法更新为最新的。

#TODO

要完整使用请修改自己的七牛token获取逻辑，请项目内搜索TODO修改。

# 技能/轮子树

https://github.com/Curzibn/Luban 图片压缩

https://github.com/lovetuzitong/MultiImageSelector 多图选择

https://github.com/huijimuhe/monolog-android 网络模块与自定义Recyclerview

https://github.com/litesuits/android-common 线程池与其他utils
	
#License
 
    Copyright 2016 Huijimuhe
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
