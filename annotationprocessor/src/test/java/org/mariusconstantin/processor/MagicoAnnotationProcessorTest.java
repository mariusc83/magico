package org.mariusconstantin.processor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;


import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by MConstantin on 12/20/2015.
 */
public class MagicoAnnotationProcessorTest {

    @Test
    public void testClickListeners() {
        final String classToWorkOn = "package org.mariusconstantin.magico;\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "import android.os.Bundle;\n" +
                "import android.view.View;\n" +
                "\n" +
                "import org.mariusconstantin.annotations.OnClickHandler;\n" +
                "\n" +
                "public class MainActivity extends Activity {\n" +
                "\n" +
                "    @Override\n" +
                "    protected void onCreate(Bundle savedInstanceState) {\n" +
                "        super.onCreate(savedInstanceState);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @OnClickHandler(elementId = android.R.id.button1)\n" +
                "    public void onSubmit(View view) {\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    @OnClickHandler(elementId = android.R.id.button2)\n" +
                "    public void onCancel(View view) {\n" +
                "\n" +
                "    }\n" +
                "}\n";

        final String expectedGeneratedClass = "package org.mariusconstantin.magico;\n" +
                "\n" +
                "import android.view.View;\n" +
                "import java.lang.Override;\n" +
                "import org.mariusconstantin.magicoapi.AbstractMagicoUIDelegate;\n" +
                "\n" +
                "public class MainActivity$$MAGICO<T extends MainActivity> extends AbstractMagicoUIDelegate<T> {\n" +
                "  @Override\n" +
                "  protected void init() {\n" +
                "    mViewsIds.add(16908313);\n" +
                "    mViewsIds.add(16908314);\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public void onClick(final View v) {\n" +
                "    switch(v.getId()) {\n" +
                "      case 16908313:mTarget.onSubmit(v);\n" +
                "      break;\n" +
                "      case 16908314:mTarget.onCancel(v);\n" +
                "      break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        final JavaFileObject sampleActivity = JavaFileObjects
                .forSourceString("org.mariusconstantin.magico.MainActivity", classToWorkOn);

        final JavaFileObject expectedActivity = JavaFileObjects
                .forSourceString("org.mariusconstantin.magico.MainActivity$$MAGICO", expectedGeneratedClass);

        assert_().about(javaSource())
                .that(sampleActivity)
                .processedWith(new MagicoAnnotationProcessor())
                .compilesWithoutError().and().generatesFiles(expectedActivity);
    }
}
