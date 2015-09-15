# Http接口测试工

### 介绍
本工具用于测试http接口的返回结果，可自定义请求的参数、上传文件路径、header和cookie的值，并支持html，json，jsonp等响应类型。

### 编译部署

&ensp;&ensp;**1.下载工程**

&ensp;&ensp;`git clone https://github.com/yangziwen/httptest.git`

&ensp;&ensp;**2.初始化数据库**

&ensp;&ensp;`mvn compile exec:exec -Pinit-db`

&ensp;&ensp;生成名为db的目录，其中包含名为httptest.db的数据库文件

&ensp;&ensp;**3.生成war包**

&ensp;&ensp;`mvn clean package -Pcompress -Pstandalone`

&ensp;&ensp;生成httptest-standalone.war

&ensp;&ensp;**3.运行工程**

&ensp;&ensp;将前面生成的db目录与httptest-standalone.war文件放置在同一路径下，并执行以下命令

&ensp;&ensp;`java -Dport=8989 -Dcontextpath=/ -jar httptest-standalone.war`

&ensp;&ensp;**4.访问工程** [http://localhost:8989](http://localhost:8989)