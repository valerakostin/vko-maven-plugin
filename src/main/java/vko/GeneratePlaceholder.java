package vko;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * Created by vv on 10.04.17.
 */

@Mojo(name = "generate")
public class GeneratePlaceholder extends AbstractMojo {

    @Parameter(property = "generate.count", defaultValue = "10")
    private int count;

    @Parameter(property = "generate.baseClass")
    private String baseClass;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;


    public void execute() throws MojoExecutionException, MojoFailureException {
        Log logger = getLog();
        logger.info("Start generation of " + count + " placeholder classes " + baseClass + "...");

        try {
            File temp = createTempFolder();
            File base = createFolderHierarchy(temp, baseClass);

            int index = baseClass.indexOf(".");

            String packageName = null;
            String templateClass;
            if (index != -1) {
                packageName = baseClass.substring(0, index);
                templateClass = baseClass.substring(index + 1);
            } else {
                templateClass = baseClass;
            }

            for (int i = 0; i < count; i++) {
                String content = createClassContent(packageName, templateClass, i);
                String name = baseClass + i + ".java";

                File file = new File(base, name);

                Files.write(file.toPath(), Collections.singletonList(content), Charset.forName("UTF-8"));
            }
            project.addCompileSourceRoot(temp.getAbsolutePath());

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private String createClassContent(String packageName, String templateClass, int i) {
        StringBuilder sb = new StringBuilder();
        if (packageName != null) {
            sb.append("package ");
            sb.append(packageName);
            sb.append(";\n");
        }

        sb.append("class ");
        sb.append(templateClass);
        sb.append(i);
        sb.append(" extends ");
        sb.append(baseClass);
        sb.append("\n{}");

        return sb.toString();
    }

    private File createTempFolder() throws IOException {
        File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
        temp.delete();
        temp.mkdir();
        return temp;
    }

    private File createFolderHierarchy(File temp, String baseClass) {
        String[] items = baseClass.split(Pattern.quote("."));
        File base = temp;
        for (int i = 0; i < items.length - 1; i++) {
            File f = new File(base, items[i]);
            f.mkdir();
            base = f;
        }
        return base;
    }
}
