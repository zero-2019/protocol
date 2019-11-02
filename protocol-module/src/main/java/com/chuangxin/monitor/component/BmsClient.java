package com.chuangxin.monitor.component;

import com.chuangxin.monitor.constants.BacnetConstants;
import com.chuangxin.monitor.entity.BmsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;


@Component
public class BmsClient {

    private static final Logger logger = LoggerFactory.getLogger(BmsClient.class);

    @Autowired
    private FeatureCollector  collector;


    private int toInt( Byte byte1, Byte byte2,  Byte byte3,  Byte byte4){
        //((byte4.toInt() and 0xFF) shl 24).plus((byte3.toInt() and 0xFF) shl 16).plus((byte2.toInt() and 0xFF) shl 8).plus(byte1.toInt() and 0xFF)

    //    int length = String.format("%02x %02x %02x %02x ", byte1, byte2, byte3, byte4).length();

        int length =  ((byte4.intValue() & 0xFF) << 24)| ((byte3.intValue() & 0xFF) <<  16) | ((byte2.intValue() & 0xFF) <<  8) | (byte1.intValue() & 0xFF);
        return length;
    }

    private int toInt(Byte byte1,Byte byte2) {
        // (byte1.toInt() and 0xFF).plus((byte2.toInt() and 0xFF) shl 8)
        //int length = String.format("%02x %02x", byte1, byte2).length();
        int length = (byte1.intValue() & 0xFF) | ((byte2.intValue() & 0xFF) << 8);

        return length;
    }
    private void receiveAndPrintHex(DataInputStream inputStream, String name) {

      try {
          StringBuilder data = new StringBuilder();
          data.append(name);
          for (int i=0;i<4;i++){
              byte temp = inputStream.readByte();
              data.append(String.format("%02x", temp));
          }
          logger.debug(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Async
    public  void   startBmsSocket(BmsEntity bmsEntity) {

        Socket socket = null;
        try {

            Charset charset = Charset.forName(BacnetConstants.CHARSET_NAME);

            logger.info("try connect to %s:%d".format(bmsEntity.getHost(), bmsEntity.getPort()));

            socket = new Socket(bmsEntity.getHost(), Integer.parseInt(bmsEntity.getPort()));

            logger.info("socket connected");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            byte[] kvContent = new byte[1024 * 1024];

            while (true) {
                receiveAndPrintHex(inputStream, "包头标记");
                receiveAndPrintHex(inputStream, "协议版本");
                receiveAndPrintHex(inputStream, "命令类型");

                byte b1 = inputStream.readByte();
                byte b2 = inputStream.readByte();
                byte b3 = inputStream.readByte();
                byte b4 = inputStream.readByte();
                int packageLen = toInt(b1, b2, b3, b4);
                inputStream.skipBytes(16); // 保留位

                int byteRead = 0;
                while (byteRead < packageLen) {
                    int groupLength = toInt(inputStream.readByte(), inputStream.readByte());
                    byteRead += 2 + groupLength;
                    inputStream.readFully(kvContent, 0, groupLength);

                    byte[] kvBytes = Arrays.copyOfRange(kvContent, 0, groupLength);

                    String kv = new String(kvBytes, charset);

                    logger.debug("kv  =====>  "  +  kv);
                    collector.process(bmsEntity.getDatcentId(),kv);

                }
            }
        } catch (Exception e) {
            logger.error("Error receiving data"  + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
