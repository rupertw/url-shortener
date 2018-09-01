#基础镜像信息
FROM openjdk:8u171-jdk-alpine3.8
#维护者信息
MAINTAINER "www"<rupertwong@163.com>
#定义构建镜像时需要的参数
ARG JAR_FILE
#格式为 ADD <src> <dest>
#该命令将复制指定的<src>到容器中的<dest>。其中<src>可以是Dockerfile所在目录的一个相对路径；也可以是一个 URL；还可以是一个tar文件（自动解压为目录）。
ADD ${JAR_FILE} /usr/local/url-shortener.jar
#定义docker容器对外暴露的端口号（spring boot项目启动端口）
EXPOSE 8080
#配置容器启动后执行的命令。
#每个Dockerfile中只能有一个ENTRYPOINT，当指定多个时，只有最后一个起效。
ENTRYPOINT ["java","-jar","/usr/local/url-shortener.jar"]