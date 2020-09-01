package com.hpz.butterknife_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;


/**
 * Created by hpz on 2020/8/31.
 */
class BindViewModel {
    private String        topClassName;//顶层类名
    private String        packageName;//顶层包名
    private List<Element> bindViewList = new ArrayList<>();

    BindViewModel(Element element, Elements elementUtils) {
        addBindView(element);
        Element enclosingElement = element.getEnclosingElement();//element.getEnclosingElement() 获得顶层元素
        topClassName = enclosingElement.getSimpleName().toString();//顶层类名
        packageName = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();//顶层包名

    }

    public void addBindView(Element element) {
        bindViewList.add(element);

    }

    public String getTopClassName() {
        return topClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<Element> getBindViewList() {
        return bindViewList;
    }
}
