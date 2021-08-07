环境：centos7  jdk1.8   IDEA  Mysql  hadoop  springboot

本云盘系统是基于hadoop hdfs的集群分布式系统

功能点
1、安全认证
使用SpringSecurity进行安全认证，验证码验证提高系统安全性
2、用户管理
登录，注册，安全退出
3、文件管理
文件剪切，复制，删除，上传，重命名，下载（包括目录下载），分类，新建目录。
4、hadoop信息查看

问题
1、vue如何让界面感知到数组的变化
解决方法：不能采用改变数组下标的方式，必须采用函数操作的方法
splice.(index,1) //数组切片，从数组下标为1的这个位置进行切片。
//三个参数为替换

2、加入目录下载后文件下载出现Refused to display '<URL>' in a frame because it set 'X-Frame-Options' to 'deny'错误
解决方法： 重启hdfs，start-fds.sh 


