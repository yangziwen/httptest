# Http接口测试工具

### 介绍
本工具用于测试http接口的返回结果，可自定义请求的参数、上传文件路径、header和cookie的值，并支持html、json、jsonp、图片等响应类型。

### 编译部署

&ensp;&ensp;**1.下载工程**

&ensp;&ensp;`git clone https://github.com/yangziwen/httptest.git`

&ensp;&ensp;**2.生成war包**

&ensp;&ensp;`mvn clean package -Pcompress -Pstandalone -Pprod`

&ensp;&ensp;生成httptest-standalone.war

&ensp;&ensp;**3.运行工程**

&ensp;&ensp;`java -Dport=8989 -Dcontextpath=/ -jar httptest-standalone.war`

&ensp;&ensp;**4.访问工程** [http://localhost:8989](http://localhost:8989)