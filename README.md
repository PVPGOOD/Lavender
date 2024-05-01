# Lavender

## 如何加快Maven 下载速度 
1. 参考文献：https://zhuanlan.zhihu.com/p/71998219

## 开发指南
1. 使用要监听事件时 例如:

          @EventTarget
           void onMove(EventMove event) {
            //code
          }
你需要申明该方法修饰符为 public 否则将导致 事件系统 抛出 IllegalAccessError 异常

正确的写法为

          @EventTarget
          public void onMove(EventMove event) {
            //code
          }
