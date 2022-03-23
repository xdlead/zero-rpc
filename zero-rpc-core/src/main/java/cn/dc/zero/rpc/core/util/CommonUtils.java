package cn.dc.zero.rpc.core.util;

import java.util.Collection;
import java.util.Map;

public class CommonUtils {
    /**
     * 判断一个Array是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断一个集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    /**
     * 判断一个Array是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    public static int[] parseInts(String nums, String sperator) {
        String[] ss = StringUtils.split(nums, sperator);
        int[] ints = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            ints[i] = Integer.parseInt(ss[i]);
        }
        return ints;
    }

    public static int parseInt(String num, int defaultInt) {
        if (num == null) {
            return defaultInt;
        } else {
            try {
                return Integer.parseInt(num);
            } catch (Exception e) {
                return defaultInt;
            }
        }
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isTrue(String b) {
        return b != null && StringUtils.TRUE.equalsIgnoreCase(b);
    }
}
