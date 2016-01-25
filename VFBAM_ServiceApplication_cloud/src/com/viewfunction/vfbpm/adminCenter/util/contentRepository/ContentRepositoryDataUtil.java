/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.util.contentRepository;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author wangychu
 */
public class ContentRepositoryDataUtil {

    public static final String BINARY_CONTENT_TYPE_BINARY = "BINARY_CONTENT_TYPE_BINARY";
    public static final String BINARY_CONTENT_TYPE_TEXT = "BINARY_CONTENT_TYPE_TEXT";

    public static enum AddRootContentObjectResult {

        AddRootContentObjectSuccessful,
        RootContentObjectAlreadyExists,
        GetRepositoryErrorDuringOperation
    }

    public static enum AddSubContentObjectResult {

        AddSubContentObjectSuccessful,
        ContentObjectAlreadyExists,
        GetRepositoryErrorDuringOperation
    }
    
    public static enum AddContentPropertiesResult {

        AddContentPropertiesSuccessful,
        AddContentPropertiesFail,
        GetRepositoryErrorDuringOperation
    }

    public static enum DeleteContentObjectResult {

        DeleteContentObjectSuccessful,
        ContentObjectAlreadyDeleted,
        GetRepositoryErrorDuringOperation
    }
    
    public static enum DeleteContentObjectPropertyResult {

        DeletePropertySuccessful,
        PropertyAlreadyDeleted,
        GetRepositoryErrorDuringOperation
    }
    
    public static enum AddBinaryContentObjectResult {

        AddBinaryContentObjectSuccessful,
        ContentObjectAlreadyExists,
        GetRepositoryErrorDuringOperation
    }

    public static AddRootContentObjectResult addRootContentObject(String contentSpaceName, String rootContentObjectName) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            if (cs.getRootContentObject(rootContentObjectName) != null) {
                return AddRootContentObjectResult.RootContentObjectAlreadyExists;
            } else {
                RootContentObject rco = ContentComponentFactory.createRootContentObject(rootContentObjectName);
                cs.addRootContentObject(rco);
                return AddRootContentObjectResult.AddRootContentObjectSuccessful;
            }
        } catch (ContentReposityException e) {
            return AddRootContentObjectResult.GetRepositoryErrorDuringOperation;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }

    public static AddSubContentObjectResult addSubContentObject(String contentSpaceName, String parentContentObjectAbsPath, String subContentObjectName, List<ContentObjectProperty> contentObjectList) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);           
            BaseContentObject bco = cs.getContentObjectByAbsPath(parentContentObjectAbsPath);
            if (bco.getSubContentObject(subContentObjectName) != null) {
                return AddSubContentObjectResult.ContentObjectAlreadyExists;
            }
            BaseContentObject sbco = bco.addSubContentObject(subContentObjectName, contentObjectList, true);
            return AddSubContentObjectResult.AddSubContentObjectSuccessful;
        } catch (ContentReposityException e) {
            return AddSubContentObjectResult.GetRepositoryErrorDuringOperation;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }
    
    public static AddContentPropertiesResult addContentObjectProperties(String contentSpaceName, String parentContentObjectAbsPath,List<ContentObjectProperty> contentObjectList){
    	 ContentSpace cs = null;
    	 try {
    		 cs = ContentComponentFactory.connectContentSpace(contentSpaceName);           
             BaseContentObject bco = cs.getContentObjectByAbsPath(parentContentObjectAbsPath);
             boolean result=bco.addProperty(contentObjectList, true);
    		 if(result){
    			 return AddContentPropertiesResult.AddContentPropertiesSuccessful;
    		 }else{
    			 return AddContentPropertiesResult.AddContentPropertiesFail;
    		 }    		 
    	 } catch (ContentReposityException e) {
    		 e.printStackTrace();
    		 return AddContentPropertiesResult.GetRepositoryErrorDuringOperation;
         } finally {
             if (cs != null) {
                 cs.closeContentSpace();
             }
         }    	
    }    

    public static long getRootComponentChildrenNumber(String contentSpaceName, String rootContentObjectName) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            RootContentObject rco = cs.getRootContentObject(rootContentObjectName);
            return rco.getSubContentObjectsCount();
        } catch (ContentReposityException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }

    public static long getSubComponentChildrenNumber(String contentSpaceName, String subContentObjectABSPath) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            BaseContentObject bco = cs.getContentObjectByAbsPath(subContentObjectABSPath);
            return bco.getSubContentObjectsCount();
        } catch (ContentReposityException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }

    public static DeleteContentObjectResult deleteRootContentObject(String contentSpaceName, String rootContentObjectName) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            boolean deleteResult = cs.removeRootContentObject(rootContentObjectName);
            if (deleteResult) {
                return DeleteContentObjectResult.DeleteContentObjectSuccessful;
            } else {
                return DeleteContentObjectResult.ContentObjectAlreadyDeleted;
            }
        } catch (ContentReposityException e) {
            e.printStackTrace();
            return DeleteContentObjectResult.GetRepositoryErrorDuringOperation;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }

    public static DeleteContentObjectResult deleteContentObject(String contentSpaceName, String parentContentObjectAbsPath, String ContentObjectName) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            BaseContentObject bco = cs.getContentObjectByAbsPath(parentContentObjectAbsPath);
            boolean deleteResult = bco.removeSubContentObject(ContentObjectName, true);
            if (deleteResult) {
                return DeleteContentObjectResult.DeleteContentObjectSuccessful;
            } else {
                return DeleteContentObjectResult.ContentObjectAlreadyDeleted;
            }
        } catch (ContentReposityException e) {
            e.printStackTrace();
            return DeleteContentObjectResult.GetRepositoryErrorDuringOperation;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }   
    
    public static DeleteContentObjectPropertyResult deleteContentObjectProperty(String contentSpaceName, String contentObjectAbsPath,String propertyName){
    	 ContentSpace cs = null;
         try {
             cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
             BaseContentObject bco = cs.getContentObjectByAbsPath(contentObjectAbsPath);
             boolean res=bco.removeProperty(propertyName, true);
             if(res){
            	 return DeleteContentObjectPropertyResult.DeletePropertySuccessful;
             }else{
            	 return DeleteContentObjectPropertyResult.PropertyAlreadyDeleted;
             }             
         } catch (ContentReposityException e) {
             e.printStackTrace();
             return DeleteContentObjectPropertyResult.GetRepositoryErrorDuringOperation;
         } finally {
             if (cs != null) {
                 cs.closeContentSpace();
             }
         }
    }

    public static AddBinaryContentObjectResult addBinaryContentFile(String contentSpaceName, String parentContentObjectAbsPath, File binaryFile, String contentType) {
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(contentSpaceName);
            BaseContentObject bco = cs.getContentObjectByAbsPath(parentContentObjectAbsPath);
            ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
            if (contentType.equals(BINARY_CONTENT_TYPE_BINARY)) {
                boolean res= coh.addBinaryContent(bco, binaryFile, "", true);
                if(res){
                	return AddBinaryContentObjectResult.AddBinaryContentObjectSuccessful;
                }else{
                	return AddBinaryContentObjectResult.ContentObjectAlreadyExists;
                }         
            } else if (contentType.equals(BINARY_CONTENT_TYPE_TEXT)) {               
                boolean res= coh.addTextContent(bco, binaryFile, "", true);
                if(res){
                	return AddBinaryContentObjectResult.AddBinaryContentObjectSuccessful;
                }else{
                	return AddBinaryContentObjectResult.ContentObjectAlreadyExists;
                }                 
            } else {
                return AddBinaryContentObjectResult.GetRepositoryErrorDuringOperation;
            }
        } catch (ContentReposityException e) {
            e.printStackTrace();
            return AddBinaryContentObjectResult.GetRepositoryErrorDuringOperation;
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }
    
    public static String getContentObjectABSPathByItemId(HierarchicalContainer contentSpace_DataContainer, Object itemID) {
        Item currentItem = contentSpace_DataContainer.getItem(itemID);
        if (itemID.toString().startsWith(ContentSpaceDataProvider.contentSpaceIdPerfix)) {
            return null;
        } else {
            //clicked content object
            String contentSpace = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).toString();
            String currentContentObjectName = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).toString();
            List<String> contentObjectPath = new ArrayList<String>();
            contentObjectPath.add(currentContentObjectName);
            Object parentItemID = contentSpace_DataContainer.getParent(itemID);
            while (parentItemID != null) {
                Object contentRepositoryElementName = contentSpace_DataContainer.getItem(parentItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME);
                if (contentRepositoryElementName != null && !contentRepositoryElementName.toString().equals(contentSpace)) {
                    contentObjectPath.add(contentRepositoryElementName.toString());
                }
                parentItemID = contentSpace_DataContainer.getParent(parentItemID);
            }
            Collections.reverse(contentObjectPath);
            String contentObjectABsPath = "/";
            for (String str : contentObjectPath) {
                contentObjectABsPath = contentObjectABsPath + str + "/";
            }
            return contentObjectABsPath.substring(0, contentObjectABsPath.length() - 1);
        }
    }
    
    public static boolean checkNewItemAlreadyExistInContentSpaceDataContainer(HierarchicalContainer contentSpace_DataContainer,Object parentNodeID, String newItemString) {
        if (!contentSpace_DataContainer.hasChildren(parentNodeID)) {
            return false;
        }
        Collection childrenItemCollection = contentSpace_DataContainer.getChildren(parentNodeID);
        Iterator childrenItemTor = childrenItemCollection.iterator();
        Item currentItem;
        String currentItemName;
        while (childrenItemTor.hasNext()) {
            currentItem = contentSpace_DataContainer.getItem(childrenItemTor.next());
            currentItemName = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue().toString();
            if (currentItemName.equals(newItemString.trim())) {
                return true;
            }
        }
        return false;
    }    
}