// MessageCenter.aidl
package com.qlf.aidl_client_demo;

//作用是定义方法

//导入所需要使用的非默认支持数据类型的包
import com.qlf.aidl_client_demo.Info;

interface MessageCenter {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      List<Info> getInfo();

      void addInfo(inout Info info);
}
