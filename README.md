Maven Plugin for generating subclasses from template without new functionality 

## Core Configuration

#### pom.xml
```xml
<project>
    <modelVersion>4.0.0</modelVersion>

    <groupId>vko</groupId>
    <artifactId>vko-maven-plugin-test</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>Generate Placeholder Class Maven Plugin Test</name>

    <build>
        <plugins>
            <plugin>
                <groupId>vko.plugin</groupId>
                <artifactId>vko-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <count>12</count>
                    <baseClass>vko.Main</baseClass>
                    <sourcePath>vko</sourcePath>>
                </configuration>
                <executions>
                    <execution>
                        <phase>process-sources</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```


baseClass - class for cloning

count - number of instances (default: 10)
 

```java
package vko;

public class Main {

    private String name;

    public Main() {
        name = getClass().getSimpleName();
    }

    public static void main(String[] argc) {
        try {
            Class cls = Class.forName("vko.Main0");
            Main m = (Main) cls.newInstance();
            System.out.println("Dynamic loaded class  " + m.name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }   
}
```