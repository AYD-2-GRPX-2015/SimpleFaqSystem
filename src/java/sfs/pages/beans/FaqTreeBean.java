/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sfs.Utilities;
import sfs.catalog.CatalogServiceOption;
import sfs.pages.beans.objects.TreeTopicObject;
import sfs.persistence.objects.FaqTopic;
import sfs.service.common.Service;
import sfs.service.exceptions.PermissionDeniedException;

/**
 *
 * @author WL72166
 */
@ManagedBean(name = "faqTreeBean")
@RequestScoped
public class FaqTreeBean extends CommonBean{
    
    private TreeNode root; 
    private TreeNode selectedNode;  
    /**
     * Creates a new instance of FaqTreeBean
     */
    public FaqTreeBean() {
        super();
        try {
                buildTreeNode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private TreeNode buildTreeNode() throws PermissionDeniedException, Exception{      
        List<FaqTopic> topics = (List<FaqTopic>)Service.exec(null, CatalogServiceOption.GET_ROOT_FAQ_TOPICS);
        root = new DefaultTreeNode();
        
        if(topics==null){
            return root;
        }
        
        for(FaqTopic ft: topics){
            buildTreeNodeRecursive(ft, root);
        }
        
        String topicid = (String)getRequestValue("topicid");
        
        if(topicid == null){
            if(root.getChildCount() > 0){
                setSelectedNode(root.getChildren().get(0));
                getSelectedNode().setExpanded(true);
                getSelectedNode().setSelected(true);
            }
        }else{
            TreeNode node = findNodeByTopicId(Integer.parseInt(topicid), root);
            if(node!=null){
                setSelectedNode(node);
                getSelectedNode().setSelected(true);

                //expand nodes
                TreeNode current = node;
                while(current!=null){
                    current.setExpanded(true);
                    current = current.getParent();
                }
            }
            
        }
        
        return root;
    }
    
    private TreeNode findNodeByTopicId(Integer id, TreeNode node){
        
        if(node==null){
            return null;
        }
        
        if(node.getData()!=null && (node.getData() instanceof TreeTopicObject)){
            TreeTopicObject obj = (TreeTopicObject)node.getData();
            if(obj.getTopic().getId()==id){
                return node;
            }
        }
        
        if (node.getChildCount() > 0) {
            for (TreeNode child : node.getChildren()) {
                TreeNode found = findNodeByTopicId(id, child);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    
    private void buildTreeNodeRecursive(FaqTopic topic, TreeNode parent){
        
        TreeTopicObject treeTopicObject = new TreeTopicObject(topic);
        treeTopicObject.setToString(getNodeName(topic));
        
        DefaultTreeNode child = new DefaultTreeNode(treeTopicObject, parent);
        if(topic.getFaqTopicList()!=null){
            for(FaqTopic ftchild: topic.getFaqTopicList()){
                buildTreeNodeRecursive(ftchild, child);
            }
        }
    }
    
    private String getNodeName(FaqTopic topic){
        if (topic == null) {
            return "null";
        }
        String str = topic.getName();
        
        try{
            Long l = (Long)Service.exec(null, CatalogServiceOption.GET_FAQ_COUNT_RECURSIVE, topic);
            if(l!=null && l > 0){
                str += " ("+l+")";
            }
        }catch(Exception e){
            Utilities.printToLog("No fue posible obtener la cantidad de faqs", e, Utilities.LOG_LEVEL_ERROR);
        }
        return str;
        
    }
    
    
    public TreeNode getRoot(){
        return root;
    }
    
    
    public void onNodeSelect(NodeSelectEvent event) {
        
        System.out.println("onNodeSelect:inicio");
        
        TreeTopicObject tobj=(TreeTopicObject)event.getTreeNode().getData();
        System.out.println("onNodeSelect:"+tobj);
        System.out.println("onNodeSelect:"+tobj.getTopic().getName());
        addCallbackParam("topic", tobj.getTopic().getId());
        addCallbackParam("topicname", tobj.getTopic().getName());
    }  
    


    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    public String getSelectedTopicId(){
        if(getSelectedNode()==null){
            return null;
        }
        TreeTopicObject tto = (TreeTopicObject)getSelectedNode().getData();
        return tto.getTopic().getId().toString();
    }
    
    
    
    
}
