package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:启动类
 * @author: YangWanYi
 * @create: 2022-06-08 10:20
 **/
public class MissyApplicationContext {

    // 配置类
    private Class configClass;

    // BeanDefinition的map
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();

    // BeanPostProcessor集合
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MissyApplicationContext(Class<?> appConfigClass) {
        this.configClass = appConfigClass;
        scanBean(appConfigClass); // 扫描bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) { // 单例bean 直接创建bean对象并存到集合中
                Object singletonBeean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBeean); // 把bean放进单例池中
            } else {

            }
        }

    }

    /**
     * 创建bean
     *
     * @param beanName
     * @param beanDefinition
     * @return Object
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object object = null;
        try {
            object = clazz.getConstructor().newInstance(); // 创建对象

            // 属性注入
            for (Field field : clazz.getDeclaredFields()) { // 遍历对象定义的属性
                if (field.isAnnotationPresent(Autowired.class)) { // 属性有Autowired注解
                    field.setAccessible(true); // 反射
                    // 这里其实应该先根据类型找bean，如果找到多个再根据名称找bean。为了方便，直接根据名称获取bean了。
                    field.set(object, getBean(field.getName()));
                }
            }

            // 回调处理
            if (object instanceof BeanNameAware) { // 获取beanName的回调
                ((BeanNameAware) object).setBeanName(beanName);
            }

            // 初始化前的操作
            if (!BeanPostProcessor.class.isAssignableFrom(clazz)) { // 避免实现BeanPostProcessor的类也执行初始化前的操作
                for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                    beanPostProcessor.postProcessBeforeInitialization(object, beanName);
                }
            }

            // bean初始化
            if (object instanceof InitializingBean) { // 对象是否实现了接口InitializingBean
                ((InitializingBean) object).afterPropertiesSet(); // 调用初始化的方法
            }

            // 初始化后的操作
            if (!BeanPostProcessor.class.isAssignableFrom(clazz)) { // 避免实现BeanPostProcessor的类也执行初始化后的操作
                for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                    object = beanPostProcessor.postProcessAfterInitialization(object, beanName); // 接收代理对象
                }
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 扫描bean
     *
     * @param appConfigClass
     */
    private void scanBean(Class<?> appConfigClass) {
        if (appConfigClass.isAnnotationPresent(ComponentScan.class)) { // 配置类如果有ComponentScan注解
            ComponentScan componentScanAnnotation = appConfigClass.getAnnotation(ComponentScan.class);
            String componentScanValue = componentScanAnnotation.value(); // 获取配置的扫描包路径
//            System.out.println(componentScanValue);
            ClassLoader classLoader = MissyApplicationContext.class.getClassLoader(); // 获取类加载器
            componentScanValue = componentScanValue.replace(".", "/");
//            System.out.println(componentScanValue);
            URL resource = classLoader.getResource(componentScanValue);
            File file = new File(resource.getFile());
            if (file.isDirectory()) { // 路径是一个目录
                for (File f : file.listFiles()) {
//                    System.out.println(f.getAbsolutePath());
                    String absolutePath = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf("com"), f.getAbsolutePath().indexOf(".class")).replace("\\", ".");
//                    System.out.println(absolutePath);
                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath); // 加载类
                        if (clazz.isAnnotationPresent(Component.class)) { // 判断是否注解为bean
                            Component componentAnnotation = clazz.getAnnotation(Component.class); // 获取bean的name
                            String beanName = componentAnnotation.value();
                            if ("".equals(beanName)) { // 如果没有自定义名称 就根据类名默认生成名称
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            // 判断类是否实现了接口BeanPostProcessor
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor); // 添加beanPostProcessor到集合中

                            }

                            BeanDefinition beanDefinition = new BeanDefinition(); // 定义一个bean
                            beanDefinition.setType(clazz); // 设置bean类型beanName
                            if (clazz.isAnnotationPresent(Scope.class)) { // 判断是否有类型注解
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class); // 获取Scope注解 有可能是单例 也有可能是原型
                                String scopeValue = scopeAnnotation.value(); // 获取Scope注解的值
                                beanDefinition.setScope(scopeValue);
                            } else { // 没有scope注解 默认为单例
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition); // 添加beanDefinition到map中
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 根据beanName获取bean
     *
     * @param beanName
     * @return Object
     */
    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) { // beanDefinitionMap中没有这个beanName，表示程序中没有定义这个bean
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName); // 获取这个beanName对应的bean定义
        if ("singleton".equals(beanDefinition.getScope())) { // 单例bean
            Object singletonBean = singletonObjects.get(beanName);
            if (null == singletonBean) { // 可能这个单例bean还没来得及创建
                singletonBean = createBean(beanName, beanDefinition); // 创建单例bean
                singletonObjects.put(beanName, singletonBean); // 把单例bean放进单例池中
            }
            return singletonBean;
        } else { // 原型bean 每次获取都创建一个新的bean
            return createBean(beanName, beanDefinition);
        }
    }
}
