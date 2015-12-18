package org.mariusconstantin.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import org.mariusconstantin.annotations.OnClickHandler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MagicoAnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment mProcessingEnvironment;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mProcessingEnvironment = processingEnv;
        mFiler=mProcessingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() > 0) {
            final Map<String, IClassGenerator> generatedClasses = new HashMap<>();
            for (TypeElement element : annotations) {
                Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(element);
                if (annotatedElements.size() > 0) {
                    for (Element annotatedElement : annotatedElements) {
                        final Element enclosingElement = annotatedElement.getEnclosingElement();
                        if (annotatedElement.getKind() != ElementKind.METHOD) {
                            logError("You cannot annotated this element with Magico annotations");
                            return false;
                        }
                        final String key = getUniqueAnnotationIdentifier((TypeElement) enclosingElement, element);
                        if (generatedClasses.containsKey(key)) {
                            generatedClasses.get(key).addMethod(element, annotatedElement);
                        } else {
                            final IClassGenerator classGenerator = AbstractClassGenerator.getGenerator(element, enclosingElement, mProcessingEnvironment.getElementUtils());
                            if (classGenerator != null) {
                                classGenerator.addMethod(element, annotatedElement);
                                generatedClasses.put(key, classGenerator);
                            }
                        }

                    }
                }
            }

            // write all classes to disk
            final Filer filer = mProcessingEnvironment.getFiler();
            for (IClassGenerator classGenerator : generatedClasses.values()) {
                try {
                    classGenerator.writeClass(mFiler);
                } catch (IOException e) {
                    logError(e.getMessage());
                }
            }

        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> annotations = new HashSet<>();
        annotations.add(OnClickHandler.class.getCanonicalName());
        return Collections.unmodifiableSet(annotations);
    }


/*
    private String getMagicoClassName(Element enclosingElement){
        final String simpleName=enclosingElement.getClass().getCanonicalName();
        final String packageName=getPackageName(enclosingElement.getClass());
        return packageName+
    }
    private String getPackageName(Class<?> className) {
        final String canonicalName = className.getCanonicalName();
        final String simpleName = className.getSimpleName();
        return canonicalName.substring(0, canonicalName.length() - simpleName.length());
    }
*/

    private String getUniqueAnnotationIdentifier(TypeElement enclosedElement, TypeElement annotation) {
        final String enclosingClassName = enclosedElement.getQualifiedName().toString();
        final String annotationClassName = annotation.getSimpleName().toString();
        return enclosingClassName + annotationClassName + AbstractClassGenerator.MAGICO_SUFFIX;
    }

    public void logError(String message) {
        mProcessingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }
}
