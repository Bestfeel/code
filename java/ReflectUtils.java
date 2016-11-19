package com.gizwits.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by feel on 16/9/1.
 * 反射工具类
 */
public final class ReflectUtils {

    public static Method methodOf(Class clazz, Class cls) {
        for (Method method : clazz.getMethods()) {


            Annotation annotation = method.getAnnotation(cls);

            if (annotation != null) {
                return method;
            }
        }
        return null;
    }

    /**
     * 查找方法上是否有注解
     *
     * @param clazz
     * @param annotationType
     * @return
     */
    public static Boolean findMethodAnnotation(Class clazz, Class annotationType) {
        for (Method method : clazz.getMethods()) {


            Annotation annotation = method.getAnnotation(annotationType);

            if (annotation != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一个类是否包含一个特定的注解类型
     *
     * @param annotationType
     * @param clazz
     * @return
     */
    public static boolean isAnnotationDeclaredLocally(Class clazz, Class annotationType) {
        boolean declaredLocally = false;
        Iterator i$ = Arrays.asList(clazz.getDeclaredAnnotations()).iterator();
        do {
            if (!i$.hasNext())
                break;
            Annotation annotation = (Annotation) i$.next();
            if (!annotation.annotationType().equals(annotationType))
                continue;
            declaredLocally = true;
            break;
        } while (true);
        return declaredLocally;
    }


}
