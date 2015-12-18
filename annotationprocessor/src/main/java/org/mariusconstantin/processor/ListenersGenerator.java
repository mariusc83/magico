package org.mariusconstantin.processor;

import android.view.View;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import org.mariusconstantin.annotations.OnClickHandler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by MConstantin on 12/20/2015.
 */
public class ListenersGenerator extends AbstractClassGenerator {
    public static final ClassName mSuperClassName = ClassName.get("org.mariusconstantin.magicoapi", "AbstractMagicoUIDelegate");

    public ListenersGenerator(String enclosedClassCanonicalName) {
        super(enclosedClassCanonicalName);
    }

    @Override
    public Iterable<MethodSpec> generateMethods() {
        // first we generate the init method based on the provided ids
        List<MethodSpec> methods = null;
        if (!mMethods.isEmpty()) {
            methods = new ArrayList<>();
            final List<MethodDescription> onClickAnnotationMethods = mMethods.get(ON_CLICK_EVENT);
            MethodSpec.Builder onClickBuilder = MethodSpec.methodBuilder("onClick")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(View.class, "v", Modifier.FINAL);
            MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("init")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(void.class);


            if (onClickAnnotationMethods != null && !onClickAnnotationMethods.isEmpty()) {
                onClickBuilder.beginControlFlow("switch(v.getId())");
                for (MethodDescription methodDescription : onClickAnnotationMethods) {
                    final OnClickHandler annotation = methodDescription.getAnnotation(OnClickHandler.class);
                    if (annotation != null) {
                        initBuilder.addStatement("mViewsIds.add($L)", annotation.elementId());
                        onClickBuilder.addCode("case $L:", annotation.elementId());
                        onClickBuilder.addStatement("mTarget.$L(v)", methodDescription.getDelegatedMethod());
                        onClickBuilder.addStatement("break");
                    }
                }
                onClickBuilder.endControlFlow();
            }

            methods.add(initBuilder.build());
            methods.add(onClickBuilder.build());

        }
        return methods;
    }

    @Override
    public TypeSpec generateClass() {
        final String toGenerateClassName = getToGenerateClassName();
        TypeSpec.Builder builder = TypeSpec.classBuilder(toGenerateClassName);
        builder.addModifiers(Modifier.PUBLIC);
        builder.addTypeVariable(TypeVariableName.get("T", ClassName.get(getEnclosedClassPackage(), getEnclosedClassSimpleName())));
        final Iterable<MethodSpec> methods = generateMethods();
        if (methods != null)
            builder.addMethods(methods);
        builder.superclass(ParameterizedTypeName.get(mSuperClassName, TypeVariableName.get("T")));
        return builder.build();
    }
}
