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
UrlShortener ia a service for shortening URL, similar to [百度短网址][dwz] and [新浪短网址][sina].

# Features
- Development based on Spring Boot and Redis.
- Providing HTTP-based APIs for shortening a URL and get the original URL.
- Providing extended HTTP-based API for data statistics, such as the most top N visited short URLs.
- Support for building docker image by a maven plugin named dockerfile-maven-plugin.

# Quick Start
  >  The minimum requirements to run the quick start are: 
  >  * JDK 1.8 or above
  >  * A java-based project management software like [Maven][maven]
  >  * Redis service
  >  * Docker Engine (optional)

# Documents
* [Wiki](https://github.com/rupertw/url-shortener/wiki)

# License
UrlShortener is released under the [MIT License](https://mit-license.org/).

[maven]:https://maven.apache.org
[dwz]:http://dwz.cn/
[sina]:http://sina.lt/