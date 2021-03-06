package cn.dc.zero.rpc.core.common;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author ：d3137
 * @date ：Created in 2021/10/25 17:32
 * @description：排序比较器
 * @version:
 */
public class OrderedComparator<T extends Sortable> implements Comparator<T>, Serializable {
    /**
     * 顺序（true:从小到大）还是倒序（false:从大到小）
     */
    private final boolean order;

    /**
     * 默认顺序，从小到大
     */
    public OrderedComparator() {
        this.order = true;
    }

    /**
     * Instantiates a new Ordered comparator.
     *
     * @param smallToLarge the small to large
     */
    public OrderedComparator(boolean smallToLarge) {
        this.order = smallToLarge;
    }

    @Override
    public int compare(T o1, T o2) {
        // order一样的情况下，顺序不变
        return order ? o1.getOrder() - o2.getOrder() :
                o2.getOrder() - o1.getOrder();
    }
}
