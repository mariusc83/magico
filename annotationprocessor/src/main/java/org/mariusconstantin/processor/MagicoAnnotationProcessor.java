package org.mariusconstantin.processor;

import com.google.auto.service.AutoService;

import org.mariusconstantin.annotations.GET;
import org.mariusconstantin.annotations.POST;
import org.mariusconstantin.annotations.PUT;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class MagicoAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> annotations = new HashSet<>();
        annotations.add(GET.class.getCanonicalName());
        annotations.add(POST.class.getCanonicalName());
        annotations.add(PUT.class.getCanonicalName());

        return Collections.unmodifiableSet(annotations);
    }
}
