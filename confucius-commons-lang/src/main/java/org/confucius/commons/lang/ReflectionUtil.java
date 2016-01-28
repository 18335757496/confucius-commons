/**
 * Project   : commons-lang
 * File      : ReflectionUtil.java
 * Date      : 2012-1-5
 * Time      : ����01:29:58
 * Copyright : taobao.com Ltd.
 */
package org.confucius.commons.lang;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * ���乤����
 *
 * @author <a href="mailto:taogu.mxx@taobao.com">Mercy</a>
 * @version 1.0.0
 * @see Method
 * @see Field
 * @see Constructor
 * @see Array
 * @since 1.0.0 2012-1-5 ����01:29:58
 */
@SuppressWarnings("unchecked")
public class ReflectionUtil extends BaseUtil {

    /**
     * �����Ƿ�Ϊ�Ϸ���������
     *
     * @param array �������
     * @param index ����
     * @throws IllegalArgumentException       �ο�{@link ReflectionUtil#assertArrayType(Object)}
     * @throws ArrayIndexOutOfBoundsException ��<code>index</code>С��0�����ߴ��ڻ�������鳤��
     */
    public static void assertArrayIndex(Object array, int index) throws IllegalArgumentException {
        if (index < 0) {
            String message = String.format("The index argument must be positive , actual is %s", index);
            throw new ArrayIndexOutOfBoundsException(message);
        }
        ReflectionUtil.assertArrayType(array);
        int length = Array.getLength(array);
        if (index > length - 1) {
            String message = String.format("The index must be less than %s , actual is %s", length, index);
            throw new ArrayIndexOutOfBoundsException(message);
        }
    }

    /**
     * ���Բ����Ƿ�����
     *
     * @param array �������
     * @throws IllegalArgumentException ��<code>array</code>������������ʱ
     */
    public static void assertArrayType(Object array) throws IllegalArgumentException {
        Class<?> type = array.getClass();
        if (!type.isArray()) {
            String message = String.format("The argument is not an array object, its type is %s", type.getName());
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * �����Ƿ��ֶ��Ƿ�ƥ���ڴ�������
     *
     * @param object       ����
     * @param fieldName    �ֶ�����
     * @param expectedType �ڴ�������
     * @throws IllegalArgumentException �����ͷ���ƥ��ʱ
     */
    public static void assertFieldMatchType(Object object, String fieldName, Class<?> expectedType) throws IllegalArgumentException {
        Class<?> type = object.getClass();
        Field field = getField(type, fieldName);
        Class<?> fieldType = field.getType();
        if (!expectedType.isAssignableFrom(fieldType)) {
            String message = String.format("The type[%s] of field[%s] in Class[%s] can't match expected type[%s]", fieldType.getName(), fieldName, type.getName(), expectedType.getName());
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * ��ȡ�����е��ֶ�ֵ
     *
     * @param object    ����
     * @param fieldName �ֶ�����
     * @return ����Ҳ����Ļ�������<code>null</code>
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getField(object.getClass(), fieldName);
        Object value = null;
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            value = field.get(object);
        } catch (Exception ignored) {
        } finally {
            field.setAccessible(accessible);
        }
        return value;
    }

    /**
     * ��ȡ���е��ֶζ���
     *
     * @param typeWithField ���ֶε������
     * @param fieldName     �ֶ�����
     * @return ����Ҳ����Ļ�������<code>null</code>
     * @throws IllegalArgumentException ���ƶ��������޷�ͨ���ֶ����ƻ�ȡʱ
     * @throws NullPointerException     ������Ϊ<code>null</code>ʱ
     */
    public static Field getField(Class<?> typeWithField, String fieldName) throws IllegalArgumentException, NullPointerException {
        Field field = null;
        try {
            field = typeWithField.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
            String message = String.format("Field[%s] can not be found in type[%s]", fieldName, typeWithField.getName());
            throw new IllegalArgumentException(message);
        }
        return field;
    }

    /**
     * ����ָ�������ֶε�ֵ
     *
     * @param object     Ŀ�����
     * @param fieldName  �ֶ�����
     * @param fieldValue �ֶ�ֵ
     */
    public static void setFiled(Object object, String fieldName, Object fieldValue) {
        Field field = getField(object.getClass(), fieldName);
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (IllegalAccessException e) {
            String message = String.format("Field[%s] can not be set with value[%s]", fieldName, fieldValue);
            throw new IllegalArgumentException(message, e);
        } finally {
            if (field != null) {
                field.setAccessible(accessible);
            }
        }
    }

    // /**
    // * ����ǩ��
    // */
    // private static class MethodSignature {
    // final private Class<?> targetClass;
    // final private String methodName;
    // final private Class<?>[] argClasses;
    //
    // final private int hashCode;
    //
    // public MethodSignature(Class<?> targetClass, String methodName,
    // Class<?>... argClasses) {
    // this.targetClass = targetClass;
    // this.methodName = methodName;
    // this.argClasses = argClasses;
    // this.hashCode = createHashCode();
    // }
    //
    // private int createHashCode() {
    // int result = 17;
    // result = 37 * result + targetClass.hashCode();
    // result = 37 * result + methodName.hashCode();
    // if (argClasses != null) {
    // for (int i = 0; i < argClasses.length; i++) {
    // result = 37 * result + ((argClasses[i] == null) ? 0 :
    // argClasses[i].hashCode());
    // }
    // }
    // return result;
    // }
    //
    // public boolean equals(Object o2) {
    // if (this == o2) {
    // return true;
    // }
    // MethodSignature that = (MethodSignature) o2;
    // if (!(targetClass == that.targetClass)) {
    // return false;
    // }
    // if (!(methodName.equals(that.methodName))) {
    // return false;
    // }
    // if (argClasses.length != that.argClasses.length) {
    // return false;
    // }
    // for (int i = 0; i < argClasses.length; i++) {
    // if (!(argClasses[i] == that.argClasses[i])) {
    // return false;
    // }
    // }
    // return true;
    // }
    //
    // public int hashCode() {
    // return hashCode;
    // }
    // }

    /**
     * ��ָ�������л�ȡ�������ƺͲ�����Ӧ�ķ�������û���޸�{@link Method#setAccessible(boolean) �ɷ�����}
     * �������û���ҵ��Ļ�������<code>null</code>
     *
     * @param classObject         �����
     * @param methodName          ��������
     * @param methodArgumentTypes ���������б�
     * @return ָ�������л�ȡ�������ƺͲ�����Ӧ�ķ����������û���ҵ��Ļ�������<code>null</code>
     * @version 1.0.0
     * @see Method
     * @since 1.0.0 2012-1-5 ����01:36:46
     */
    public static Method getMethod(Class<?> classObject, String methodName, Class<?>... methodArgumentTypes) {
        Method method = null;

        try {
            method = classObject.getDeclaredMethod(methodName, methodArgumentTypes);
        } catch (Exception ignored) {
            method = null;
        }

        return method;
    }

    private static Method findMethod(Class<?> type, String methodName, Object... arguments) {
        List<Class<?>> methodArgumentTypesList = Lists.newArrayListWithCapacity(arguments.length);
        for (Object argument : arguments) {
            methodArgumentTypesList.add(argument.getClass());
        }
        Method method = findMethod(type, methodName, methodArgumentTypesList.toArray(new Class<?>[0]));
        return method;
    }

    /**
     * ���÷Ǿ�̬����
     *
     * @param <T>
     * @param source     ����Դ
     * @param methodName ������
     * @param arguments  ��������
     * @return ����ִ�з�����Ľ��
     * @version 1.0.0
     * @since 1.0.0 2012-1-5 ����02:15:26
     */
    public static <T> T invokeMethod(Object source, String methodName, Object... arguments) {

        Class<?> type = source.getClass();

        Method method = findMethod(type, methodName, arguments);

        if (method == null)
            return null;

        return (T) invokeMethod(source, method, arguments);
    }

    /**
     * ���þ�̬�������������û���ҵ��Ļ�������<code>null</code>
     *
     * @param <T>        ��������
     * @param type       ָ������
     * @param methodName ָ���ķ�������
     * @param arguments  ������������
     * @return ���ط���ֵ���������û���ҵ��Ļ�������<code>null</code>
     * @version 1.0.0
     * @since 1.0.0 2012-3-15 ����02:19:52
     */
    public static <T> T invokeStaticMethod(Class<?> type, String methodName, Object... arguments) {
        Method method = findMethod(type, methodName, arguments);
        if (method == null)
            return null;
        return (T) invokeMethod(null, method, arguments);
    }

    /**
     * ��������
     *
     * @param source
     * @param method
     * @param arguments
     * @return ��������ֵ
     * @throws IllegalArgumentException ��װ{@link Method#invoke(Object, Object...)}�����쳣��Ϣ
     * @version 1.0.0
     * @see Method#invoke(Object, Object...)
     * @since 1.0.0 2012-3-15 ����02:03:12
     */
    private static Object invokeMethod(Object source, Method method, Object... arguments) throws IllegalArgumentException {
        final boolean accessible = method.isAccessible();
        Object value = null;
        try {
            method.setAccessible(true);
            value = method.invoke(source, arguments);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            method.setAccessible(accessible);
        }
        return value;
    }

    /**
     * ��ָ�������в��ҷ������ƺͲ�����Ӧ�ķ�������û���޸�{@link Method#setAccessible(boolean) �ɷ�����}
     * �������û���ҵ��Ļ����ݹ�س����丸�����ҡ��������û�еĻ�������<code>null</code>
     *
     * @param classObject         �����
     * @param methodName          ��������
     * @param methodArgumentTypes ���������б�
     * @return ��ָ�������в��ҷ������ƺͲ�����Ӧ�ķ�������û���޸�{@link Method#setAccessible(boolean)
     * �ɷ�����} �������û���ҵ��Ļ����ݹ�س����丸�����ҡ��������û�еĻ�������<code>null</code>
     * @version 1.0.0
     * @see Method
     * @since 1.0.0 2012-1-5 ����01:36:46
     */
    public static Method findMethod(Class<?> classObject, String methodName, Class<?>... methodArgumentTypes) {
        Method method = null;

        Class<?> classToFind = classObject;

        while (classToFind != null) {
            try {
                method = classToFind.getDeclaredMethod(methodName, methodArgumentTypes);
            } catch (Exception ignored) {
                method = null;
                classToFind = classToFind.getSuperclass();
            }

            if (method != null)
                break;

        }

        return method;
    }

}
