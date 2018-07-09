package com.cool.butterknife.compiler;

import com.cool.butterknife.annoation.BindView;
import com.cool.butterknife.annoation.OnClick;
import com.cool.butterknife.compiler.bean.BindViewBean;
import com.cool.butterknife.compiler.bean.ElementBean;
import com.cool.butterknife.compiler.bean.OnClickBean;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by cool on 2018/7/5.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.cool.butterknife.annoation.OnClick", "com.cool.butterknife.annoation.BindView"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ViewProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private Elements mElementUtils;
    private Log log;

    private Map<String, ElementBean> mMaps = new HashMap<>();

//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> set = new HashSet<>();
//        set.add("com.cool.butterknife.annoation.OnClick");
//        set.add("com.cool.butterknife.annoation.BindView");
//        return set;
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        log = new Log(mMessager);
        log.e("=========init============");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        log.e("=========process============");

        if (set != null && set.size() > 0) {

            getOnClickBean(roundEnvironment);

            getBindViewBean(roundEnvironment);

            Set<Map.Entry<String, ElementBean>> entries = mMaps.entrySet();
            for (Map.Entry<String, ElementBean> entry : entries) {
                String key = entry.getKey();
                ElementBean elementBean = entry.getValue();
                generateViewBinding(key, elementBean);
            }

            return true;
        }

        return false;
    }

    /**
     * 生成代码
     *
     * @param className   全类名 com.cool.butterknife.MainActivity
     * @param elementBean 保存被BindView OnClick注解的元素相关信息
     */
    private void generateViewBinding(String className, ElementBean elementBean) {
        //获取类的元素节点
        TypeElement classTypeElement = mElementUtils.getTypeElement(className);

        List<BindViewBean> bindViewBeans = elementBean.mBindViewList;
        List<OnClickBean> onClickBeans = elementBean.mOnClickList;

        //生成参数 final MainActivity target
        ParameterSpec targetParameterSpec = ParameterSpec
                .builder(ClassName.get(classTypeElement), Constant.TARGET, Modifier.FINAL)
                .build();
        //生成参数  View source
        ParameterSpec viewParameterSpec = ParameterSpec.builder(ClassName.bestGuess(Constant.VIEWPACKAGE), Constant.SOURCE)
                .build();
        MethodSpec constructor = null;

        //生成构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                .addParameter(targetParameterSpec)
                .addParameter(viewParameterSpec)
                .addAnnotation(ClassName.bestGuess(Constant.UITHREAD))
                .addModifiers(Modifier.PUBLIC);

        //如果有父类的话，构造函数中还要添加这句  super(target, source);
        if (elementBean.hasSuperClass() && isHasViewBinding(elementBean.superClassTypeMirror)) {
            constructorMethodBuilder.addStatement("super(target, source)");
        }
        //  构造函数中添加代码块
        constructorMethodBuilder.addStatement("this.target = target");

        //往构造函数中添加 target.textView = source.findViewById(2131165322);
        for (int i = 0; bindViewBeans != null && i < bindViewBeans.size(); i++) {
            BindViewBean bindViewBean = bindViewBeans.get(i);
            constructorMethodBuilder.addStatement("target.$L = source.findViewById($L)", bindViewBean.getFiledName(), bindViewBean.getId());
        }

        //往构造函数中添加View view2131165218 = source.findViewById(2131165218);
        //view2131165218.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View view) {
        //        target.onClick(view);
        //      }
        //    });
        for (int i = 0; onClickBeans != null && i < onClickBeans.size(); i++) {
            OnClickBean onClickBean = onClickBeans.get(i);
            constructorMethodBuilder.addStatement("$T view$L = source.findViewById($L)", ClassName.bestGuess(Constant.VIEWPACKAGE), onClickBean.getId(), onClickBean.getId());
            constructorMethodBuilder.addStatement("this.view$L = view$L", onClickBean.getId(), onClickBean.getId());
            TypeSpec typeSpec = generateAnonymousInnerClasses(onClickBean);
            constructorMethodBuilder.addStatement("view$L.setOnClickListener($L)", onClickBean.getId(), typeSpec);
        }

        //创建构造函数
        constructor = constructorMethodBuilder.build();

        //生成unbind方法
        MethodSpec.Builder unbindMethodSpec = MethodSpec.methodBuilder(Constant.UNBIND)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        //往unbind中添加代码块
        unbindMethodSpec.addStatement("$T target = this.target", ClassName.get(classTypeElement));
        unbindMethodSpec.addStatement("this.target = null");

        //往unbind中添加 target.textView = null;
        for (int i = 0; bindViewBeans != null && i < bindViewBeans.size(); i++) {
            BindViewBean bindViewBean = bindViewBeans.get(i);
            unbindMethodSpec.addStatement("target.$L = null", bindViewBean.getFiledName());
        }

        //往unbind中添加 view2131165218.setOnClickListener(null); view2131165218 = null;
        for (int i = 0; onClickBeans != null && i < onClickBeans.size(); i++) {
            OnClickBean onClickBean = onClickBeans.get(i);
            unbindMethodSpec.addStatement("view$L.setOnClickListener(null)", onClickBean.getId());
            unbindMethodSpec.addStatement("view$L = null", onClickBean.getId());
        }

        //如果有父类的话还要在unbind中添加这句 super.unbind()
        if (elementBean.hasSuperClass() && isHasViewBinding(elementBean.superClassTypeMirror)) {
            unbindMethodSpec.addStatement("super.unbind()");
        }

        //生成MainActivity_ViewBinding类
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(classTypeElement.getSimpleName() + Constant.VIEWBINDING)
                .addField(ClassName.get(classTypeElement), Constant.TARGET, Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(unbindMethodSpec.build())
                .addModifiers(Modifier.PUBLIC);

        //如果有父类则继承父类
        if (elementBean.hasSuperClass() && isHasViewBinding(elementBean.superClassTypeMirror)) {
            String baseClassNameType = elementBean.superClassTypeMirror.toString();
            if (baseClassNameType.contains("<")) {
                int index = baseClassNameType.indexOf("<");
                baseClassNameType = baseClassNameType.substring(0, index);

            }
            String baseClassNameString = baseClassNameType + Constant.VIEWBINDING;
            ClassName baseClassName = ClassName.bestGuess(baseClassNameString);
            typeSpec.superclass(baseClassName);
        } else {
            typeSpec.addSuperinterface(ClassName.bestGuess(Constant.UNBINDER));
        }

        //生成成员变量 private View view2131165218;
        for (int i = 0; onClickBeans != null && i < onClickBeans.size(); i++) {
            OnClickBean onClickBean = onClickBeans.get(i);
            typeSpec.addField(ClassName.bestGuess(Constant.VIEWPACKAGE), "view" + onClickBean.getId(), Modifier.PRIVATE);
        }

        //获取包名
        String packageName = mElementUtils.getPackageOf(classTypeElement).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec.build())
                .build();

        try {
            //写入java文件
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isHasViewBinding(TypeMirror superClassTypeMirror) {
        String baseClassNameType = superClassTypeMirror.toString();
        if (baseClassNameType.contains("<")) {
            int index = baseClassNameType.indexOf("<");
            baseClassNameType = baseClassNameType.substring(0, index);

        }
        ElementBean elementBean = mMaps.get(baseClassNameType);
        return elementBean != null;
    }

    /**
     * 生成OnClickListener匿名内部类
     * <p>
     * new View.OnClickListener() {
     *
     * @param onClickBean
     * @return
     * @Override public void onClick(View view) {
     * target.onClick(view);
     * }
     * }
     */
    private TypeSpec generateAnonymousInnerClasses(OnClickBean onClickBean) {
        TypeElement typeElement = mElementUtils.getTypeElement(Constant.ONCLICKLISTENER);
        ParameterizedTypeName.get(typeElement.asType());
        TypeSpec onclickListener = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(typeElement.asType()))
                .addMethod(MethodSpec.methodBuilder(Constant.ONCLICK)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.bestGuess(Constant.VIEWPACKAGE), Constant.VIEW)
                        .addStatement("target.$L(view)", onClickBean.getMethodName())
                        .build())
                .build();
        return onclickListener;
    }

    /**
     * 获取被BindView注解的节点并添加到集合中
     *
     * @param roundEnvironment
     */
    private void getBindViewBean(RoundEnvironment roundEnvironment) {
        Set<? extends Element> bindViewElementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        for (Element element : bindViewElementsAnnotatedWith) {
            Element classElement = element.getEnclosingElement();
            TypeElement superElement = (TypeElement) classElement;//父节点MainActivity，是类节点，强转
            TypeMirror superMirror = superElement.getSuperclass();//父类 BaseActivity
            String superClassName = superMirror.toString();//父类全类名

            String className = classElement.toString();
            String simpleClassName = classElement.getSimpleName().toString();
            ElementBean elementBean = mMaps.get(className);

            if (elementBean == null) {
                elementBean = new ElementBean();
                mMaps.put(className, elementBean);
            }
            if (elementBean.mBindViewList == null) {
                elementBean.mBindViewList = new ArrayList<>();
            }
            elementBean.mBindViewList.add(new BindViewBean(element, simpleClassName));

            //添加父类节点
            if (!superClassName.startsWith("android.") && !superClassName.startsWith("java.")) {
                elementBean.superClassTypeMirror = superMirror;
            }
        }
    }

    /**
     * 获取被OnClick注解的节点并添加到集合中
     *
     * @param roundEnvironment
     */
    private void getOnClickBean(RoundEnvironment roundEnvironment) {
        Set<? extends Element> clickElementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element : clickElementsAnnotatedWith) {
            Element classElement = element.getEnclosingElement();//父节点MainActivity

            TypeElement superElement = (TypeElement) classElement;//父节点MainActivity,是类节点，强转
            TypeMirror superMirror = superElement.getSuperclass();//父类 BaseActivity
            String superClassName = superMirror.toString();//父类全类名

            String className = classElement.toString();
            String simpleClassName = classElement.getSimpleName().toString();
            OnClick onClick = element.getAnnotation(OnClick.class);

            ExecutableElement executableElement = (ExecutableElement) element;
            List<? extends VariableElement> parameters = executableElement.getParameters();

            ElementBean elementBean = mMaps.get(className);
            if (elementBean == null) {
                elementBean = new ElementBean();
                mMaps.put(className, elementBean);
            }
            if (elementBean.mOnClickList == null) {
                elementBean.mOnClickList = new ArrayList<>();
            }

            int[] value = onClick.value();
            for (int id : value) {
                OnClickBean onClickBean = new OnClickBean(element, simpleClassName, id, parameters);
                elementBean.mOnClickList.add(onClickBean);
            }
            //添加父类节点
            if (!superClassName.startsWith("android.") && !superClassName.startsWith("java.")) {
                elementBean.superClassTypeMirror = superMirror;
            }
        }
    }
}
