package com.ijson.blog.model;

import lombok.Data;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.util.Arrays;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/19 7:15 PM
 * <p>
 * System.out.println("系统版本:" + os);
 * System.out.format("CPU温度: %.1f°C%n", hal.getSensors().getCpuTemperature());
 * System.out.println("风扇速度: " + Arrays.toString(hal.getSensors().getFanSpeeds()));
 * System.out.format("CPU电压: %.1fV%n", hal.getSensors().getCpuVoltage());
 * <p>
 * System.out.println("以使用内存: " + FormatUtil.formatBytes(hal.getMemory().getAvailable()));
 * System.out.println("内存总共:" + FormatUtil.formatBytes(hal.getMemory().getTotal()));
 * System.out.println("Swap分区占用: " + FormatUtil.formatBytes(hal.getMemory().getSwapUsed()));
 * System.out.println("Swap分区共计: " + FormatUtil.formatBytes(hal.getMemory().getSwapTotal()));
 * <p>
 * <p>
 * System.out.println("CPU: " + hal.getProcessor().getName());
 * System.out.println("物理CPU计数: " + hal.getProcessor().getPhysicalProcessorCount() + " physical CPU(s)");
 * System.out.println("逻辑CPU计数: " + hal.getProcessor().getLogicalProcessorCount() + " logical CPU(s)");
 */
@Data
public class SystemInfo {
    private String operatingSystem;
    private double cpuVoltage;
    private String memoryUsed;
    private String memoryTotal;
    private String swapUsed;
    private String swapTotal;
    private String cpuName;
    private int physicalCount;
    private int logicalProcessorCount;
    private String jdkVersion;

    public static SystemInfo getSystemInfo() {
        oshi.SystemInfo si = new oshi.SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setOperatingSystem(os.toString());
        systemInfo.setCpuVoltage(hal.getSensors().getCpuVoltage());
        systemInfo.setMemoryUsed(FormatUtil.formatBytes(hal.getMemory().getAvailable()));
        systemInfo.setMemoryTotal(FormatUtil.formatBytes(hal.getMemory().getTotal()));
        systemInfo.setSwapUsed(FormatUtil.formatBytes(hal.getMemory().getSwapUsed()));
        systemInfo.setSwapTotal(FormatUtil.formatBytes(hal.getMemory().getSwapTotal()));
        systemInfo.setCpuName(hal.getProcessor().getName());
        systemInfo.setPhysicalCount(hal.getProcessor().getPhysicalProcessorCount());
        systemInfo.setLogicalProcessorCount(hal.getProcessor().getLogicalProcessorCount());
        systemInfo.setJdkVersion(System.getProperty("java.version"));
        return systemInfo;
    }

    public static void main(String[] args) {
        SystemInfo systemInfo = SystemInfo.getSystemInfo();
        System.out.println(systemInfo);
    }
}
