package cn.dc.zero.rpc.core.node;

/**
 * @Author: DC
 * @Description:
 * @Date: 2022/3/8 11:39
 * @Version: 1.0
 */
public class NodeInfo {

    private String nodeId;

    private String nodeIdentify;

    private String nodeGroup;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeIdentify() {
        return nodeIdentify;
    }

    public void setNodeIdentify(String nodeIdentify) {
        this.nodeIdentify = nodeIdentify;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }
}
