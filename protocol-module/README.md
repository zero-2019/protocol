bacnet 控制水系统  
接口：http://localhost:8996/protocol/bacnet/writeproperty

参数:
    
     ip=172.31.56.54   控制机组的ip
     
     deviceId=250002   设备deviceid
     
     changeValue=37.0   设备要改变的值  浮点数
     
     propertyName=m995_12    设备propertyname 
     
     broadcastIp=172.31.56.254   网关ip
     
     
     
 
 返回:  return  code=1 成功 
  
  
modbus 控制风系统 

接口：http://localhost:8996/protocol/modbus/writeproperty 

参数： 

        host=172.31.56.54   主站ip 
        port=502   主站连接端口号  
        slaveId=1   从站id  
        offset=0    偏移量 最多247  
        riteValue=15   修改值)
        
  返回:  return  code=1 成功