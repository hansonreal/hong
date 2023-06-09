package com.github.hong.core.base.id;


import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.Clock;

/**
 * snowflake的结构如下(每部分用-分开):
 * <br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * <br>
 * （1）第一位为未使用
 * （2）接下来的41位为毫秒级时间(41位的长度可以使用69年)
 * （3）然后是5位datacenterId
 * （4）5位workerId
 * （5）最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <br>
 * <p>
 * 正数的 Long 类型转换为 10 进制数范围：0~9,223,372,036,854,775,807，可见长度为最多 19 位，因此 SnowFlake 算法生成的 id 位数统一设定为 19 位为宜。
 * <p>
 * 一般刚开始使用时为 18 位，但时间距离起始时间超过一定值后，会变为 19 位。
 * <p>
 * 消耗完 18 位所需的时间：1*10^18 / (3600 * 24 * 365 * 1000 * 2^22) ≈ 7.56 年，即时间差超过 7.56 年，就会达到 19 位。
 * <p>
 * 此处暂定为18位,业务数模至少要19位，以免几年后字段超长
 *
 * @author TuLiMing 2020/7/24
 */
public class SnowflakeSequence implements Serializable {

    //private static final Logger logger = LoggerFactory.getLogger(SnowflakeSequence.class);

    private static final long serialVersionUID = 1L;
    private static volatile SnowflakeSequence instance;
    /**
     * 开始时间截 (2023-01-01)
     */
    private final long twepoch = 1672502400000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    private boolean useSystemClock;

    /**
     * 无参构造器，自动生成 workerId/datacenterId
     */
    public SnowflakeSequence() {
        this(false);
    }

    /**
     * @param isUseSystemClock 是否使用{@link Clock.SystemClock} 获取当前时间戳
     */
    public SnowflakeSequence(boolean isUseSystemClock) {
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
        this.useSystemClock = isUseSystemClock;
        String initialInfo = String.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, datacenterid  %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, datacenterId, workerId);
        //logger.info(initialInfo);
        //System.out.println(initialInfo);
    }

    /**
     * 构造
     *
     * @param workerId     终端ID
     * @param datacenterId 数据中心ID
     */
    public SnowflakeSequence(long workerId, long datacenterId) {
        this(workerId, datacenterId, false);
    }


    /**
     * 构造
     *
     * @param workerId         终端ID
     * @param datacenterId     数据中心ID
     * @param isUseSystemClock 是否使用{@link Clock.SystemClock} 获取当前时间戳
     */
    public SnowflakeSequence(long workerId, long datacenterId, boolean isUseSystemClock) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.useSystemClock = isUseSystemClock;
    }

    public static SnowflakeSequence getInstance() {
        if (instance == null) {
            synchronized (SnowflakeSequence.class) {
                if (instance == null) {
                    instance = new SnowflakeSequence();
                }
                return instance;
            }
        }
        return instance;
    }


    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> workerIdShift & ~(-1L << workerIdBits);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> datacenterIdShift & ~(-1L << datacenterIdBits);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------ Private method start

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = genTime();
        if (timestamp < lastTimestamp) {
            // 如果服务器时间有问题(时钟后退) 报错。
            throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %dms", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }
    // ------------------------------------------------------------------------------------------------------------------------------------ Private method end

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = genTime();
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long genTime() {
        //return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
        return System.currentTimeMillis();
    }

    private long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("GetDatacenterId Exception", e);
        }
        return id;
    }

    private long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder macIpPid = new StringBuilder();
        macIpPid.append(datacenterId);
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null && !name.isEmpty()) {
                //GET jvmPid
                macIpPid.append(name.split("@")[0]);
            }
            //GET hostIpAddress
            String hostIp = InetAddress.getLocalHost().getHostAddress();
            String ipStr = hostIp.replaceAll("\\.", "");
            macIpPid.append(ipStr);
        } catch (Exception e) {
            throw new RuntimeException("GetMaxWorkerId Exception", e);
        }
        //MAC + PID + IP 的 hashcode 取低 16 位
        return (macIpPid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }
}
