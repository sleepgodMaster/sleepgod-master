package com.cool.tageventbus;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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
import javax.lang.model.util.Elements;


@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.cool.tageventbus.Subscribe"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SubscriberProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private Elements mElementUtils;
    private Log log;
    private Map<String, List<SubscriberInfoModel>> mSubscriberInfoMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        log = new Log(mMessager);
        mSubscriberInfoMap = new HashMap<>();
        log.e("=========init============");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        log.e("=========process============");
        if (set != null && set.size() > 0) {
            getSubscribeMethodInfo(roundEnvironment);

            generateJavaClass();
            return true;
        }
        return false;
    }

    /**
     * 生成java文件
     */
    private void generateJavaClass() {
        Set<Map.Entry<String, List<SubscriberInfoModel>>> entries = mSubscriberInfoMap.entrySet();
        for (Map.Entry<String, List<SubscriberInfoModel>> entry : entries) {
            String className = entry.getKey();
            List<SubscriberInfoModel> subscriberInfoModels = entry.getValue();
            generateJavaFile(className,subscriberInfoModels);
        }
    }

    private void generateJavaFile(String className, List<SubscriberInfoModel> subscriberInfoModels) {
        TypeElement classTypeElement = mElementUtils.getTypeElement(className);

        String packageName = mElementUtils.getPackageOf(classTypeElement).getQualifiedName().toString();
        String classSimpleName = classTypeElement.getSimpleName().toString();
        String newClassSimpleName = classSimpleName+Constant.BUSINDEX;

        TypeSpec busIndex = TypeSpec.classBuilder(newClassSimpleName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.bestGuess(Constant.PACKAGE_SUBSCRIBERINFOINDEX))
                .addMethod(generateGetSubscriberInfoMethod())
                .addMethod(generatePutIndex())
                .addField(generateSubscriberIndexField())
                .addStaticBlock(generateStaticBlock(classTypeElement,subscriberInfoModels))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, busIndex)
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CodeBlock generateStaticBlock(TypeElement classTypeElement, List<SubscriberInfoModel> subscriberInfoModels) {
        CodeBlock.Builder builder = CodeBlock.builder();
        ClassName simpleSubscriberInfoType = ClassName.bestGuess(Constant.PACKAGE_SIMPLESUBSCRIBERINFO);
        ClassName subscriberMethodInfoType = ClassName.bestGuess(Constant.PACKAGE_SUBSCRIBERMETHODINFO);

        ClassName list = ClassName.get("java.util","List");
        ClassName arrayList = ClassName.get("java.util","ArrayList");
        ParameterizedTypeName listTypeName = ParameterizedTypeName.get(list, subscriberMethodInfoType);
        ParameterizedTypeName arrayListTypeName = ParameterizedTypeName.get(arrayList, subscriberMethodInfoType);
        builder.addStatement("$T subscriberMethodInfos = new $T()",listTypeName,arrayListTypeName);

        for (SubscriberInfoModel subscriberInfoModel : subscriberInfoModels) {
            String methodName = subscriberInfoModel.getMethodName();
            List<? extends VariableElement> parameters = subscriberInfoModel.getParameters();
            String tag = subscriberInfoModel.getTag();
            ThreadMode threadMode = subscriberInfoModel.getThreadMode();
            builder.add("subscriberMethodInfos.add(new $T($S, $S,",subscriberMethodInfoType,methodName,tag);
            builder.add("new Class<?>[]{");
            if(parameters != null){
                for (int i = 0; i < parameters.size(); i++) {
                    VariableElement variableElement = parameters.get(i);
                    if(i != 0){
                        builder.add(",");
                    }
                    builder.add("$T.class",variableElement.asType());
                }
            }
            builder.add("},");
            builder.add("$T.$N));", ClassName.get(threadMode.getClass()),threadMode.name());
            builder.add("\n");
        }

        builder.addStatement("putIndex(new $T($T.class,subscriberMethodInfos))"
                ,simpleSubscriberInfoType,classTypeElement);
        return builder.build();
    }

    /**
     *    @Override
     *     public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
     *         SubscriberInfo subscriberInfo = SUBSCRIBER_INDEX.get(subscriberClass);
     *         if(subscriberInfo == null){
     *             return null;
     *         }
     *         return subscriberInfo;
     *     }
     * @return
     */
    private MethodSpec generateGetSubscriberInfoMethod(){
        TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);

        TypeName classOfAny = ParameterizedTypeName.get(
                ClassName.get(Class.class), wildcard);

        TypeName subscriberInfo = ClassName.bestGuess(Constant.PACKAGE_SUBSCRIBERINFO);

        MethodSpec methodSpec = MethodSpec.methodBuilder(Constant.METHMOD_GETSUBSCRIBERINFO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(subscriberInfo)
                .addParameter(classOfAny,"subscriberClass")
                .addCode("SubscriberInfo subscriberInfo = SUBSCRIBER_INDEX.get(subscriberClass);\n"
                +"if (subscriberInfo == null){ \n    return null; \n }\n"
                +"return subscriberInfo;")
                .build();
        return methodSpec;
    }

    /**
     *    private static void putIndex(SubscriberInfo info) {
     *         SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
     *     }
     * @return
     */
    private MethodSpec generatePutIndex(){
        TypeName subscriberInfo = ClassName.bestGuess(Constant.PACKAGE_SUBSCRIBERINFO);
        MethodSpec methodSpec = MethodSpec.methodBuilder(Constant.METHMOD_PUTINDEX)
                .addModifiers(Modifier.STATIC, Modifier.PRIVATE)
                .addParameter(subscriberInfo, "info")
                .addStatement("SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info)")
                .build();

        return methodSpec;
    }

    /**
     * private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX = new HashMap<Class<?>, SubscriberInfo>();
     * @return
     */
    private FieldSpec generateSubscriberIndexField(){

        TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);

        TypeName classOfAny = ParameterizedTypeName.get(
                ClassName.get(Class.class), wildcard);

        TypeName subscriberInfo = ClassName.bestGuess(Constant.PACKAGE_SUBSCRIBERINFO);

        TypeName mapOfStringAndClassOfAny = ParameterizedTypeName.get(ClassName.get(Map.class), classOfAny,subscriberInfo);

        TypeName hashMapOfStringAndClassOfAny = ParameterizedTypeName.get(ClassName.get(HashMap.class), classOfAny,subscriberInfo);

        FieldSpec fieldSpec = FieldSpec.builder(mapOfStringAndClassOfAny, Constant.FIELD_SUBSCRIBER_INDEX)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC,Modifier.FINAL)
                .initializer("new $T()", hashMapOfStringAndClassOfAny)
                .build();

        return fieldSpec;
    }

    private void getSubscribeMethodInfo(RoundEnvironment roundEnvironment) {
        Set<? extends Element> subscribeAnnotatedElements = roundEnvironment.getElementsAnnotatedWith(Subscribe.class);
        for (Element subscribeAnnotatedElement : subscribeAnnotatedElements) {
            Element classElement = subscribeAnnotatedElement.getEnclosingElement();//类节点

            TypeElement classTypeElement = (TypeElement) classElement;

            String className = classTypeElement.toString();
            Subscribe subscribe = subscribeAnnotatedElement.getAnnotation(Subscribe.class);

            ExecutableElement executableElement = (ExecutableElement) subscribeAnnotatedElement;
            String methodName = executableElement.getSimpleName().toString();
            List<? extends VariableElement> parameters = executableElement.getParameters();

            List<SubscriberInfoModel> subscriberInfoModels = mSubscriberInfoMap.get(className);

            if(subscriberInfoModels == null){
                subscriberInfoModels = new ArrayList<>();
                mSubscriberInfoMap.put(className,subscriberInfoModels);
            }
            SubscriberInfoModel subscriberInfoModel = new SubscriberInfoModel(methodName,subscribe,parameters);
            subscriberInfoModels.add(subscriberInfoModel);
        }
    }
}
