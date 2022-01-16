package cn.dc.zero.rpc.core.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：d3137
 * @date ：Created in 2021/11/1 15:45
 * @description：
 * @version:
 */
public class ProviderGroup {

    protected  final  String name;

    protected List<ProviderInfo> providerInfoList;

    public ProviderGroup(String name) {
        this.name = name;
    }

    public ProviderGroup(String name, List<ProviderInfo> providerInfos) {
        this.name = name;
        this.providerInfoList = providerInfos == null ? new ArrayList<ProviderInfo>() : providerInfos;
    }
}
