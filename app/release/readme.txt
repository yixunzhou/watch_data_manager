使用前，
需在watch_data_manager/目录下新建settings.txt文件，写入参数。
每次用完app最好手动清除后台。






settings.txt示例：
Server_ip:166.111.134.39
Server_port:6668
Device_num:2
Remote_dir:/home/ipsc/data_pool_2/yixun/watch_data/
Src_dir:btdata2/
Tar_dir:watch_data/
Min_rate:40
Max_rate:120
Watch_num:2
Min_band:-10
Max_band:10


说明：
格式 参数名:参数
第一行存放服务器公网ip地址
第二行存放服务器ssh端口
第三行存放设备编号
第四行存放远程目录
第五行存放原始目录
第六行存放本地目标目录
第七行存放最小心率
第八行存放最大心率
第九行存放腕表编号
第十行存放加速度计移动幅值下限
第十一行存放加速度计移动幅值上限