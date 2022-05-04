#### maven命令使用

如果需要重启的项目中引入的模块中有代码发生改变，则需要重新`install`父工程，如果只是要重启的项目中有代码改变，则直接重启

#### 勘误

1. 在导入tomcat插件时，如果报错可以添加版本号，待项目不报错后**删除版本号**

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
                <!-- 指定端口 -->
                <port>81</port>
                <!-- 请求路径 -->
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

2. 在需要引入`HttpServletResponse`类时，在该项目的`pom.xml`的`dependencies`中添加如下依赖

```xml

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
    <scope>provided</scope>
</dependency>
```

3. 在引入`jasperreports`时，需要额外引入`itext-2.1.7.js6.jar`依赖，此jar包置于项目父工程中的lib文件夹中，进入到项目父工程的目录下，执行以下代码

```shell
mvn install:install-file -Dfile=".\itext-2.1.7.js6.jar" -DgroupId="com.lowagie" -DartifactId=itext -Dversion="2.1.7.js6" -Dpackaging=jar
```

完成手动导入`itext-2.1.7.js6.jar`依赖

4. 如果在使用`jasperreport`进行报表PDF导出时，发现导出的PDF为空白PDF，需更改`imageExpression`标签中的图片路径

#### 必要配置

`health-parent\health-common\src\main\java\com\reine\utils\QiniuUtils.java`中配置

```java
public static String accessKey="公钥";
public static String secretKey="私钥";
public static String bucket="空间";
```

`health-parent\health-mobile\src\main\webapp\pages\setmeal.html`中配置

```html
<!--TODO-->
<img class="img-object f-left" :src="'${自己的域名}'+setmeal.img" alt="">
```

`health-parent\health-mobile\src\main\webapp\pages\orderInfo.html`中配置

```html
<!--TODO-->
<img :src="'${自己的域名}'+setmeal.img" width="100%" height="100%"/>
```

`health-parent\health-mobile\src\main\webapp\pages\setmeal_detail.html`中配置

```javascript
// TODO
this.imgUrl = '${自己的域名}' + this.setmeal.img
```

`health-parent\health-backend\src\main\webapp\pages\setmeal.html`中配置

```javascript
// TODO
baseSite: '${自己的域名}'
```