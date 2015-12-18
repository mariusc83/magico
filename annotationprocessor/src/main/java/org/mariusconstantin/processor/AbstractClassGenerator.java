package org.mariusconstantin.processor;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.mariusconstantin.annotations.OnClickHandler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by MConstantin on 12/18/2015.
 */
public abstract class AbstractClassGenerator implements IClassGenerator {

    public static final String MAGICO_SUFFIX = "$$MAGICO";

    protected final Map<Integer, List<MethodDescription>> mMethods = new HashMap<>();
    protected final String mEnclosedClassCanonicalName;

    protected AbstractClassGenerator(String enclosedClassCanonicalName) {
        this.mEnclosedClassCanonicalName = enclosedClassCanonicalName;
    }

    @Override
    public void addMethod(TypeElement annotation, Element element) {
        final int annotationType = getAnnotationType(annotation);
        final MethodDescription methodDescription = new MethodDescription(element, annotationType);
        if (mMethods.containsKey(annotationType)) {
            mMethods.get(annotationType).add(methodDescription);
        } else {
            final List<MethodDescription> methodDescriptions = new ArrayList<>();
            methodDescriptions.add(methodDescription);
            mMethods.put(annotationType, methodDescriptions);
        }
    }

    @Override
    public abstract Iterable<MethodSpec> generateMethods();

    @Override
    public abstract TypeSpec generateClass();

    @Override
    public boolean writeClass(Filer filer) throws IOException {
        final TypeSpec generatedClass = generateClass();
        if (generatedClass != null) {
            JavaFile javaFile = JavaFile.builder(getEnclosedClassPackage(), generatedClass).build();
            javaFile.writeTo(filer);
            return true;
        }
        return false;
    }

    @Override
    public int getAnnotationType(final TypeElement annotation) {
        if (annotation.getSimpleName().toString().equals(OnClickHandler.class.getSimpleName())) {
            return ON_CLICK_EVENT;
        }
        return UNKNOWN;
    }

    protected String getToGenerateClassName() {
        return getEnclosedClassSimpleName() + MAGICO_SUFFIX;
    }

    protected String getEnclosedClassPackage() {
        return mEnclosedClassCanonicalName.substring(0, mEnclosedClassCanonicalName.lastIndexOf("."));
    }

    protected String getEnclosedClassSimpleName() {
        return mEnclosedClassCanonicalName.substring(getEnclosedClassPackage().length() + 1, mEnclosedClassCanonicalName.length());
    }

    // static

    public static IClassGenerator getGenerator(TypeElement annotation, Element enclosingElement, Elements utils) {
        if (annotation.getQualifiedName().toString().equals(OnClickHandler.class.getCanonicalName())) {
            return new ListenersGenerator(utils.getPackageOf(enclosingElement).getQualifiedName().toString() + "." + enclosingElement.getSimpleName().toString());
        }
        return null;
    }

    public static final class MethodDescription {
        private final Element mElement;
        private final int mType;

        @SuppressWarnings("unchecked")
        public <T extends Annotation> T getAnnotation(Class<T> type) {
            try {
                return mElement.getAnnotation(type);
            } catch (RuntimeException e) {
                return null;
            }
        }

        public String getDelegatedMethod() {
            return ((ExecutableElement) mElement).getSimpleName().toString();
        }

        public MethodDescription(Element mElement, int mType) {
            this.mElement = mElement;
            this.mType = mType;
        }
    }
}
