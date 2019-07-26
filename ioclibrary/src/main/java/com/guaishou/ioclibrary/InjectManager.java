package com.guaishou.ioclibrary;

import android.app.Activity;
import android.view.View;

import com.guaishou.ioclibrary.annotation.EventBase;
import com.guaishou.ioclibrary.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Activity activity){
        injectView(activity);
        injectEvent(activity);
    }
    /**
     * 控件注入
     * @param activity
     */
    private static void injectView(Activity activity){
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类的所有属性
        Field[] fields = clazz.getDeclaredFields();
        //循环拿到每个属性
        for (Field field : fields){
            //获取属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            //获取注解值
            if (injectView!=null){
                int viewId = injectView.value();
                //获取findViewById
                Method method = null;
                try {
                    method = clazz.getMethod("findViewById",int.class);
                    Object view = method.invoke(activity,viewId);
                    field.setAccessible(true);
                    field.set(activity,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 事件注入
     * @param activity
     */
    public static void injectEvent(Activity activity){
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类的所有方法
        Method[] methods= clazz.getDeclaredMethods();
        //遍历方法
        for (Method method : methods) {
            //获取每个方法的注解(多个控件id)
            Annotation[] annotations = method.getAnnotations();
            //遍历注解
            for (Annotation annotation : annotations){
                //获取注解上的注解
                //获取OnClick注解的注解类型

                //获取OnClick这个注解的class，获取注解的处理对象为class
                //通过getAnnotation(.class)获取具体的注解实例
                Class<? extends Annotation> annotationtype = annotation.annotationType();
                if (annotationtype !=null){
                    //通过EventBase指定获取
                    EventBase eventBase = annotationtype.getAnnotation(EventBase.class);
                    if (eventBase !=null){
                        //获取事件的3大成员
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();
                        // 获取注解的值，执行方法再去获取注解的值
                        try{
                            //装B通过反射获取value值
                            //通过annotationType获取onClick注解的viewId
                            Method valueMethod =annotationtype.getDeclaredMethod("value");
                            //执行value方法获取主借的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            //代理方式（3个成员组合）
                            //拦截方法
                            //得到监听的代理对象 （新建代理单例 类加载器
                            // 指定要代理的对象类型、class实例）
                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            //添加拦截方法到拦截表里
                            handler.addMethod(callBackListener,method);
                            //监听对象的代理对象
                            // 监听对象的代理对象
                            // ClassLoader loader:指定当前目标对象使用类加载器,获取加载器的方法是固定的
                            // Class<?>[] interfaces:目标对象实现的接口的类型,使用泛型方式确认类型
                            // InvocationHandler h:事件处理,执行目标对象的方法时,会触发事件处理器的方法
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader()
                            ,new Class[]{listenerType},handler);

                            //遍历注解的值
                            for (int viewId : viewIds){
                                //获取当前activity的view
                                View view = activity.findViewById(viewId);
                                //获取制定的方法
                                Method setter = view.getClass().getMethod(listenerSetter,listenerType);
                                //执行方法
                                setter.invoke(view,listener);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
