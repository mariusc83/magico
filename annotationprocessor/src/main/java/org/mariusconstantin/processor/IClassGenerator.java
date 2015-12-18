package org.mariusconstantin.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by MConstantin on 12/18/2015.
 */
public interface IClassGenerator {

    int UNKNOWN = 0;
    int ON_CLICK_EVENT = 1;

    void addMethod(TypeElement annotation, Element element);

    Iterable<MethodSpec> generateMethods();

    TypeSpec generateClass();

    boolean writeClass(Filer filer) throws IOException;

    int getAnnotationType(final TypeElement annotation);
}
