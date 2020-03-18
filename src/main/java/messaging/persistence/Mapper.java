package messaging.persistence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Mapper {
    private static Mapper mapper;
    private static volatile boolean alive;
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static Set<Class> uselessClasses;

    private Mapper() {
        alive = true;
        uselessClasses = new HashSet<>();
        uselessClasses.add(Object.class);
        uselessClasses.add(Byte.class);
        uselessClasses.add(Short.class);
        uselessClasses.add(Character.class);
        uselessClasses.add(Integer.class);
        uselessClasses.add(Float.class);
        uselessClasses.add(Long.class);
        uselessClasses.add(Double.class);
        uselessClasses.add(String.class);
    }

    public static Mapper getMapper() {
        Mapper map;
        if (!alive) {
            createMapper();
        }
        map = mapper;
        return map;
    }

    private static void createMapper() {
        mapper = new Mapper();
    }

    public  <O, C> C map(O obj, Class<C> aClass) {
        try {
            C aClassInstance = createInstanceOfClass(aClass);
            return mapMethodsAndReturnObject(obj, aClassInstance);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <C> C createInstanceOfClass(Class<C> aClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<C> classConstructor = aClass.getDeclaredConstructor();
        classConstructor.setAccessible(true);
        return classConstructor.newInstance();
    }

    private <C, O> C mapMethodsAndReturnObject(O obj, C aClassInstance) throws IllegalAccessException, InvocationTargetException {
        Method[] objMethods = getAllDeclaredMethodsInHierarchy(obj.getClass());
        Method[] aClassMethods = getAllMethodsInHierarchy(aClassInstance.getClass());
        SortedSet<String> getMethodsInvoked = new TreeSet<>();
        SortedSet<String> setMethodsInvoked = new TreeSet<>();
        for (int i = 0; i < objMethods.length; i++) {
            Method getMethod = objMethods[i];
            String objMethodName = getMethod.getName();
            if (objMethodName.startsWith(GET_PREFIX)) {
                if (getMethodsInvoked.contains(objMethodName)) {
                    continue;
                }
                String toFind = objMethodName.replace(GET_PREFIX, SET_PREFIX);
                Method setMethod = findSetMethod(toFind, aClassMethods);
                if (setMethod == null) {
                    continue;
                }
                Object objectToPut = null;
                if (checkIfMethodsMatch(getMethod, setMethod)) {
                    Object objFromGetter = invokeGetter(getMethod, obj);
                    objectToPut = constructAndMapAgain(setMethod.getParameterTypes()[0], objFromGetter);
                } else {
                    objectToPut = invokeGetter(getMethod, obj);
                }
                invokeSetter(setMethod, aClassInstance, objectToPut);
                try {
                    setMethod.invoke(aClassInstance, objectToPut);
                }catch (Exception e){
                    continue;
                }
                getMethodsInvoked.add(objMethodName);
                setMethodsInvoked.add(setMethod.getName());
            }
        }
        return aClassInstance;
    }

    private boolean checkIfMethodsMatch(Method getMethod, Method setMethod) {
        Class<?> setMethodInputParameter = checkSetMethod(setMethod);
        Class<?> getMethodReturnType = checkGetMethod(getMethod);
        return checkReturnTypes(getMethodReturnType, setMethodInputParameter);
    }

    private Class<?> checkGetMethod(Method getMethod) {
        if (getMethod.getParameterCount() > 0) {
            throw new IllegalArgumentException("Illegal getter!");
        }
        return getMethod.getReturnType();
    }

    private Class<?> checkSetMethod(Method setMethod) {
        if (setMethod.getParameterCount() > 1) {
            throw new IllegalArgumentException("Illegal setter!");
        }
        return setMethod.getParameterTypes()[0];
    }

    private <C> void invokeSetter(Method setMethod, C aClassInstance, Object objectToPut) throws InvocationTargetException, IllegalAccessException {
        setMethod.setAccessible(true);
        setMethod.invoke(aClassInstance, objectToPut);
    }

    private Object invokeGetter(Method getMethod, Object object) throws InvocationTargetException, IllegalAccessException {
        getMethod.setAccessible(true);
        return getMethod.invoke(object);
    }

    private boolean checkReturnTypes(Class<?> getMethodReturnType, Class<?> setMethodParameterType) {
        boolean b = false;
        if (!getMethodReturnType.equals(setMethodParameterType)) {
            b = true;
        }
        return b;
    }

    private Object constructAndMapAgain(Class<?> setMethodParameterType, Object objectToPut) {
        return map(objectToPut, setMethodParameterType);
    }

    private Method findSetMethod(String toFind, Method[] aClassMethods) {
        for (Method aClassMethod : aClassMethods) {
            String aClassMethodName = aClassMethod.getName();
            if (aClassMethodName.equals(toFind)) {
                return aClassMethod;
            }
        }
        return null;
    }

    private Method[] getAllDeclaredMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<>();
        if (!uselessClasses.contains(objectClass)) {
            Method[] declaredMethods = objectClass.getDeclaredMethods();
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllDeclaredMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
            allMethods.addAll(Arrays.asList(declaredMethods));

        }
        return allMethods.toArray(new Method[allMethods.size()]);
    }
    private Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<>();
        if (!uselessClasses.contains(objectClass)) {
            Method[] methods = objectClass.getMethods();
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllDeclaredMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
            allMethods.addAll(Arrays.asList(methods));

        }
        return allMethods.toArray(new Method[allMethods.size()]);
    }

}



