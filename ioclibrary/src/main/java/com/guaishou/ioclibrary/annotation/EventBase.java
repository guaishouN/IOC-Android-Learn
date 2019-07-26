package com.guaishou.ioclibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//放在注解的上面
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    //事件的三个成员
    // 1 set 方法名
    String listenerSetter();
    // 2 监听的对象
    Class<?> listenerType();
    // 3 回调方法
    String callBackListener();
}
