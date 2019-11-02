package com.chuangxin.monitor.service

import com.serotonin.bacnet4j.LocalDevice
import com.serotonin.bacnet4j.RemoteDevice
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder
import com.serotonin.bacnet4j.npdu.ip.IpNetworkUtils
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest
import com.serotonin.bacnet4j.transport.DefaultTransport
import com.serotonin.bacnet4j.type.constructed.SequenceOf
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier
import com.serotonin.bacnet4j.type.primitive.Real
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger
import com.serotonin.bacnet4j.util.PropertyReferences
import com.serotonin.bacnet4j.util.RequestUtils
import org.slf4j.LoggerFactory

/**
 * Created by morgan on 2019-03-20.
 * Project: bac
 */
open class BacnetWriter(broadcastIpPort: String) {
    private val defaultPort = 47808

    private val logger = LoggerFactory.getLogger(BacnetWriter::class.java)

    private val   deviceMap = HashMap<Int, HashMap<String, ObjectIdentifier>>()
    private val broadcastIp: String
    private val broadcastPort: Int
    init {
        val ipPorts = broadcastIpPort.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        broadcastIp = ipPorts[0]
        broadcastPort = if (ipPorts.size > 1) Integer.parseInt(ipPorts[1]) else defaultPort
    }

    private fun getObjectIdentifier(localDevice: LocalDevice, remoteDevice: RemoteDevice, propertyName: String): ObjectIdentifier? {
        val deviceId = remoteDevice.instanceNumber
        val objectIdentifier = deviceMap[deviceId]?.get(propertyName)
        if(null == objectIdentifier) {
            syncObjectIdentifier(localDevice, remoteDevice)
        }
        return deviceMap[deviceId]?.get(propertyName)
    }

    private fun syncObjectIdentifier(localDevice: LocalDevice, remoteDevice: RemoteDevice) {
        val remoteDeviceId = remoteDevice.instanceNumber
        logger.info("try to read property id of $remoteDeviceId")
        val oids = (RequestUtils.sendReadPropertyAllowNull(
                localDevice, remoteDevice, remoteDevice.objectIdentifier, PropertyIdentifier.objectList)
                as SequenceOf<ObjectIdentifier>).values


        val references = PropertyReferences()

        for (objectIdentifier in oids) {
            references.add(objectIdentifier, PropertyIdentifier.presentValue, PropertyIdentifier.objectName)
        }

        logger.info("try to read properties name and present value of $remoteDeviceId")
        val values = RequestUtils.readProperties(localDevice, remoteDevice, references, null)

        for (objectIdentifier in oids) {
            val objectName = values.getString(objectIdentifier, PropertyIdentifier.objectName)

            val pres = values.getString(objectIdentifier, PropertyIdentifier.presentValue);

            logger.info("objectId:$objectIdentifier <==> property: $objectName  <==> value: $pres")


            setObjectIdentifier(remoteDeviceId, objectName, objectIdentifier)
        }
    }

    private fun setObjectIdentifier(deviceId: Int, propertyName: String, objectIdentifier: ObjectIdentifier) {
        if (!deviceMap.containsKey(deviceId)) {
            deviceMap[deviceId] = HashMap()
        }
        val propertyMap = deviceMap[deviceId]!!
        propertyMap[propertyName] = objectIdentifier
    }

    open fun writeProperty(remoteIp: String, remoteDeviceId: Int,
                      propertyName: String, value: Float) : String{
        var res = "1"
        var localDevice: LocalDevice? = null
        try {
            localDevice = getLocalDevice(broadcastIp, broadcastPort)

            val remoteDevice = localDevice.findRemoteDevice(IpNetworkUtils.toAddress(remoteIp, defaultPort), remoteDeviceId)

            val objectIdentifier = getObjectIdentifier(localDevice, remoteDevice, propertyName)
                    ?: throw java.lang.Exception("Fail to get object identifier of $remoteDeviceId:$propertyName")

            try {
                /**
                 *    这个地方的值不固定  万国的是null  烽火是8
                 */
                val writePropertyRequest = WritePropertyRequest(objectIdentifier, PropertyIdentifier.presentValue,
                        null, Real(value), UnsignedInteger(8))
                localDevice.send(remoteDevice, writePropertyRequest)
                logger.info("Succeed to writeAsync property $remoteDeviceId:$propertyName to $value")

            } catch (e: Throwable) {
                logger.warn("Fail to writeAsync property $remoteDeviceId:$propertyName to $value")
                syncObjectIdentifier(localDevice, remoteDevice)
                res = e.toString()
            } finally {
                Thread.sleep(100)
                localDevice.terminate()
            }
        } catch (e: Exception) {
            logger.warn("Error when try to writeAsync property", e)
            res = e.toString()
        } finally {
            if (localDevice != null && localDevice.isInitialized) {
                localDevice.terminate()
            }
        }
        return res
    }


    private fun getLocalDevice(broadcastIp: String, port: Int = defaultPort, subnetMask: String = "255.255.255.0"): LocalDevice {
        val networkBuilder = IpNetworkBuilder().subnetMask(subnetMask)
                .broadcastIp(broadcastIp).port(port)
        val transport = DefaultTransport(networkBuilder.build())

        val localDeviceID = 10000 + (Math.random() * 10000).toInt()
        val localDevice = LocalDevice(localDeviceID, transport)
        localDevice.initialize()
        return localDevice
    }
}