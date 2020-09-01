package com.hpz.butterknife_compiler;

import com.google.auto.service.AutoService;
import com.hpz.butterknife_annotation.BindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by hpz on 2020/8/31.
 */

/*
 * AutoService自动生成注册注解处理器
 * 生成在:butterknife_compiler/build/classes/java/main/META-INF/services/javax.annotation.processing.Processor
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ButterKnifeProcessor extends AbstractProcessor {
    private Messager                   messager;
    private ProcessingEnvironment      environment;
    private Map<String, BindViewModel> map = new HashMap<>();//注解分类 一个class对应多个注解

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        environment = processingEnvironment;
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsSet = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        if (set.isEmpty()) return true;
        classifyElements(elementsSet);
        generatingJavaClass();
        return true;
    }

    /*
     * 区分注解该属于哪个类
     * */
    private void classifyElements(Set<? extends Element> elementSet) {
        for (Element element : elementSet) {
            String className = getFullClassName(element);
            BindViewModel bindViewModel = map.get(className);
            if (bindViewModel == null) {
                bindViewModel = new BindViewModel(element, environment.getElementUtils());
                map.put(className, bindViewModel);
            } else {
                bindViewModel.addBindView(element);
            }
        }
    }

    /**
     * 获取注解属性的完整类名
     */
    private String getFullClassName(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String packageName = environment.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        return packageName + "." + typeElement.getSimpleName().toString();
    }

    //使用java poet生成绑定视图代码
    private void generatingJavaClass() {
        map.forEach(new BiConsumer<String, BindViewModel>() {
            @Override
            public void accept(String s, BindViewModel bindViewModel) {
                //JavaPoet获得class的重要的方法
                ClassName activityClass = ClassName.get(bindViewModel.getPackageName(), bindViewModel.getTopClassName());
                final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(activityClass, "activity")
                        .addStatement("this.activity = activity");

                bindViewModel.getBindViewList().forEach(new Consumer<Element>() {
                    @Override
                    public void accept(Element element) {
                        BindView bindView = element.getAnnotation(BindView.class);//获得注解对象
                        constructorBuilder.addStatement("activity.$L = activity.findViewById($L)", element.getSimpleName(), bindView.value());

                    }
                });

                TypeSpec classType = TypeSpec.classBuilder(bindViewModel.getTopClassName() + "$ViewBinding")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(constructorBuilder.build())
                        .addField(activityClass, "activity", Modifier.PRIVATE)
                        .build();

                try {
                    JavaFile.builder(bindViewModel.getPackageName(), classType)
                            .build().writeTo(environment.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class.getCanonicalName());
        return annotations;
    }
}
