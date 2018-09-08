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
UrlShortener ia a service for shortening URL, similar to [Google URL Shortener][goo] and [百度短网址][dwz].

# Features
- Development based on Spring Boot and Redis.
- Providing HTTP-based APIs for shortening a URL and get the original URL.
- Providing extended HTTP-based API for data statistics, such as the most top N visited short URLs.
- Support for building docker image by a maven plugin named dockerfile-maven-plugin.

# Quick Start
  >  The minimum requirements to run the quick start are: 
  >  * JDK 1.8 or above
  >  * Maven 3.5.3 or above
  >  * Git
  >  * Redis service
  >  * An Java IDE like IntelliJ IDEA (optional)
  >  * Docker Engine (optional)
  
  1. Clone
     ```
     > git clone https://github.com/rupertw/url-shortener.git
     ```
  2. Modifiy application.properties on Indicator Item
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
  3. Build
     ```
     > cd url-shortener
     > mvn -DskipTests clean install -U
     ```
  4. Start application 
     ```
     > java -jar url-shortener-1.0.0.RELEASE.jar
     ```
  5. Test the HTTP-based APIs
     ```
     www.me/shorten?url=https://github.com/rupertw/url-shortener
     www.me/original?surl=http://www.me/cu9Wsskgwd
     www.me/statistics/topnVisit?topn=5
     ```

# Documents
* [Wiki](https://github.com/rupertw/url-shortener/wiki)
* [Wiki(中文)](https://github.com/rupertw/url-shortener/wiki/zh_overview)

# Reference
* [如何将一个长URL转换为一个短URL？](https://blog.csdn.net/xlgen157387/article/details/80026452)
* [SpringMVC4.1之Controller层最佳实践](https://github.com/kuitos/kuitos.github.io/issues/9)

# License
UrlShortener is released under the [MIT License](https://github.com/rupertw/url-shortener/blob/master/LICENSE).

[goo]:https://goo.gl/
[dwz]:http://dwz.cn/