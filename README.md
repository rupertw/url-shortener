# UrlShortener
![GitHub top language](https://img.shields.io/github/languages/top/rupertw/url-shortener.svg)
![GitHub language count](https://img.shields.io/github/languages/count/rupertw/url-shortener.svg)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/rupertw/url-shortener/pulls)
![GitHub release](https://img.shields.io/github/release/rupertw/url-shortener.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/rupertw/url-shortener.svg)
[![GitHub license](https://img.shields.io/github/license/rupertw/url-shortener.svg)](https://github.com/rupertw/url-shortener/blob/master/LICENSE)
<!--
[![GitHub issues](https://img.shields.io/github/issues/rupertw/url-shortener.svg)](https://github.com/rupertw/url-shortener/issues)
[![GitHub forks](https://img.shields.io/github/forks/rupertw/url-shortener.svg)](https://github.com/rupertw/url-shortener/network)
[![GitHub stars](https://img.shields.io/github/stars/rupertw/url-shortener.svg)](https://github.com/rupertw/url-shortener/stargazers)
![GitHub forks](https://img.shields.io/github/forks/rupertw/url-shortener.svg?style=social&label=Fork)
![GitHub stars](https://img.shields.io/github/stars/rupertw/url-shortener.svg?style=social&label=Stars)
![GitHub watchers](https://img.shields.io/github/watchers/rupertw/url-shortener.svg?style=social&label=Watch)
![GitHub followers](https://img.shields.io/github/followers/rupertw.svg?style=social&label=Follow)
![GitHub last commit](https://img.shields.io/github/last-commit/rupertw/url-shortener.svg)
![GitHub contributors](https://img.shields.io/github/contributors/rupertw/url-shortener.svg)
-->

# Overview
UrlShortener is a quick, open-source project for shortening URL, you can easily host your own URL shortener service with it, similar to [TinyURL.com][tinyurl] and [百度短网址][dwz], plus basic statistics over the data.

# Features
- Development based on Spring Boot and Redis.
- Support for building docker image by a maven plugin named dockerfile-maven-plugin.
- Support for configuring blacklist for long URL via domains.
- Providing REST APIs for shortening a URL and get the original URL.
- Providing extended REST APIs for data statistics, such as the most top N visited short URLs.

# Quick Start
  >  The minimum requirements to run the quick start are: 
  >  * JDK 1.8 or above
  >  * Git
  >  * Maven 3.5.3 or above
  >  * Redis server
  >  * Docker Engine
  >  * A Java IDE like IntelliJ IDEA (optional)
  
  1. Clone
     ```bash
     $ git clone https://github.com/rupertw/url-shortener.git
     $ cd url-shortener
     ```
  2. Modifiy application.properties, blacklist
     ``` 
     #默认8080端口
     #server.port=80
     
     #域名
     url.shortener.service.host=http://www.me
   
     #SnowFlake
     snowflake.datacenterId=1
     snowflake.machineId=1
   
     #Redis
     spring.redis.host=localhost
     spring.redis.port=6379
     spring.redis.password=todo
     spring.redis.database=1
     ...
     ```
     ``` 
     #xxx.com
     yyy.com
     ```
  3. Build an image from the Dockerfile
     ```bash
     $ mvn -DskipTests clean package -U
     $ mvn dockerfile:build
     ```
  4. Create a container layer over the new image, and then start it
     ```bash
     $ docker run -p 80:8080 rupertw/url-shortener:1.0.0.RELEASE
     ```
  5. Test the REST APIs
     ```
     http://127.0.0.1/swagger-ui.html
     ```

# Documents
* [Wiki](https://github.com/rupertw/url-shortener/wiki)
* [Wiki(中文)](https://github.com/rupertw/url-shortener/wiki/zh_overview)

# Releases
* [All releases](https://github.com/rupertw/url-shortener/releases)
* [v1.0.1.RELEASE](https://github.com/rupertw/url-shortener/releases/tag/v1.0.1.RELEASE)
* [v1.0.0.RELEASE](https://github.com/rupertw/url-shortener/releases/tag/v1.0.0.RELEASE)

# References
* [如何将一个长URL转换为一个短URL？](https://blog.csdn.net/xlgen157387/article/details/80026452)
* [SpringMVC4.1之Controller层最佳实践](https://github.com/kuitos/kuitos.github.io/issues/9)

# License
UrlShortener is released under the [MIT License](https://github.com/rupertw/url-shortener/blob/master/LICENSE).

[tinyurl]:https://tinyurl.com/
[dwz]:http://dwz.cn/