/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.util.contentRepository;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import java.util.List;

/**
 *
 * @author wangychu
 */
public class ContentSpaceDataProvider {

    public static final Object CONTENT_REPOSITORY_ELEMENT_NAME = "ElementDisplayName";
    public static final Object CONTENT_SPACE_NAME="ContentSpaceName";
    public static final String contentSpaceIdPerfix="CONTENTSPACE_";
    public static final String rootContentObjectIdPerfix="ROOTCONTENTOBJECT_";
    public static final String subContentObjectIdPerfix="_SUBCO_";
    
    public static HierarchicalContainer getRegisteredContentSpace_DataContainer() {
        Item contentSpaceItem = null;
        Item rootContentObjectItem=null;
        // Create new container
        HierarchicalContainer contentObjectTreeContainer = new HierarchicalContainer();
        // Create containerproperty for name
        contentObjectTreeContainer.addContainerProperty(CONTENT_REPOSITORY_ELEMENT_NAME, String.class, null);
        contentObjectTreeContainer.addContainerProperty(CONTENT_SPACE_NAME, String.class, null);
        try {
            //get all content space in repository
            List<String> registeredContentSpace = ContentComponentFactory.getRegisteredContentSpace();            
            for(int i=0;i<registeredContentSpace.size();i++){
                String currentContentSpaceName=registeredContentSpace.get(i);
                String currentContentSpaceItemId=contentSpaceIdPerfix+i;
                // Add new content space item
                contentSpaceItem = contentObjectTreeContainer.addItem(currentContentSpaceItemId);
                //Add name property for content space item
                contentSpaceItem.getItemProperty(CONTENT_REPOSITORY_ELEMENT_NAME).setValue(currentContentSpaceName);
                // Allow append children root content objects
                contentObjectTreeContainer.setChildrenAllowed(currentContentSpaceItemId, true);
                // get all root content objects in current content space
                ContentSpace currentContentSpace = ContentComponentFactory.connectContentSpace(currentContentSpaceName);                
                List<RootContentObject> rootContentObjects = currentContentSpace.getRootContentObjects();
                // add all children root content objects in tree
                for (int j = 0; j < rootContentObjects.size(); j++) {
                    RootContentObject crco = rootContentObjects.get(j);
                    String currentRootContentObjectItemId=rootContentObjectIdPerfix+i+j;
                    // Add new root content object item
                    rootContentObjectItem=contentObjectTreeContainer.addItem(currentRootContentObjectItemId);                   
                    rootContentObjectItem.getItemProperty(CONTENT_REPOSITORY_ELEMENT_NAME).setValue(crco.getRootContentObjectID());
                    rootContentObjectItem.getItemProperty(CONTENT_SPACE_NAME).setValue(currentContentSpaceName);
                    // setParetn node to current content space item
                    contentObjectTreeContainer.setParent(currentRootContentObjectItemId,currentContentSpaceItemId);                    
                    long subContentNum= crco.getSubContentObjectsCount();
                    if(subContentNum>0){
                        contentObjectTreeContainer.setChildrenAllowed(currentRootContentObjectItemId, true);
                    }else{
                        contentObjectTreeContainer.setChildrenAllowed(currentRootContentObjectItemId, false);
                    }                    
                }
                currentContentSpace.closeContentSpace();
            }
        } catch (ContentReposityException ex) {
        	ex.printStackTrace();
        }
        return contentObjectTreeContainer;
    }
}