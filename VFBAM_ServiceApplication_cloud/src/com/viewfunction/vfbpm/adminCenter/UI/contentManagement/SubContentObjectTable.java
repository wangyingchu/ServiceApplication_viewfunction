package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class SubContentObjectTable extends Table{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3535903781056903217L;
	
	private String columnName_ObjectName="columnName_ObjectName";
	private String columnName_ObjectType="columnName_ObjectType";
	private String columnName_ObjectVersion="columnName_ObjectVersion";
	private String columnName_ObjectIsLinked="columnName_ObjectIsLinked";
	private String columnName_ObjectIsLocked="columnName_ObjectIsLocked";	
	UserClientInfo userClientInfo;

	public SubContentObjectTable(final UserClientInfo userClientInfo,final ContentObjectDetail contentObjectDetail){
		this.userClientInfo=userClientInfo;
	
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getSubContentObjects());		
		setPageLength(1);
		//setSelectable(true);
		setColumnReorderingAllowed(false);
		setColumnHeaders(new String[] { 
				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_objectName"), 
				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_objectType"),
				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_objectVersion"),
				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_objectIsLinked"),
				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_objectIsLocked")
				});		
		
		setColumnAlignment(columnName_ObjectIsLocked,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ObjectIsLocked, 80);
		setColumnAlignment(columnName_ObjectIsLinked,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ObjectIsLinked, 80);
		setColumnAlignment(columnName_ObjectVersion,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ObjectVersion, 80);
		setColumnAlignment(columnName_ObjectType,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ObjectType, 60);			
		
		addGeneratedColumn(columnName_ObjectName, new Table.ColumnGenerator() {
	            /**
			 * 
			 */
			private static final long serialVersionUID = -3285047772166704878L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	final String objectNameString=(String)item.getItemProperty(columnName_ObjectName).getValue();		            	
	            	Button objNameButton = new Button(objectNameString);
	            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
	            	objNameButton.addListener(new ClickListener(){

	            		/**
						 * 
						 */
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
	            			contentObjectDetail.renderSubContentObjectData(objectNameString);
						}}); 
	            	return objNameButton;
	            }

	        });

		 addGeneratedColumn(columnName_ObjectType, new Table.ColumnGenerator() {
	            /**
			 * 
			 */
			private static final long serialVersionUID = -275813645317432371L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectTypeString=(String)item.getItemProperty(columnName_ObjectType).getValue();	
	            	Embedded objectTypeIcon;
	            	if(objectTypeString.equals("StandaloneObject")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_standaloneObjectColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_standaloneTypeTooltip"));
	            	}else if(objectTypeString.equals("BinaryObject")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryObjectColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_binaryTypeTooltip"));
	            	}else if(objectTypeString.equals("TextObject")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_textObjectColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_textTypeTooltip"));
	            	}else{
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_folderObjectColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_folderTypeTooltip"));
	            	}			            		            	
	            	return objectTypeIcon;
	            }

	        });
		 
		 
		 addGeneratedColumn(columnName_ObjectVersion, new Table.ColumnGenerator() {
	            /**
			 * 
			 */
			private static final long serialVersionUID = -358343936705469647L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectVersionString=(String)item.getItemProperty(columnName_ObjectVersion).getValue();
	            	Label objectNameLabel= new Label("<span style='color:#2c3033;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectVersionString+"</span>",Label.CONTENT_XHTML);
	            	return objectNameLabel;
	            }

	        });		 
		
		 addGeneratedColumn(columnName_ObjectIsLinked, new Table.ColumnGenerator() {
	            /**
			 * 
			 */
			private static final long serialVersionUID = 2642066340895262347L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectTypeString=(String)item.getItemProperty(columnName_ObjectIsLinked).getValue();	
	            	Embedded objectTypeIcon;
	            	if(objectTypeString.equals("linked")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_linkTableColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_linkedTooltip"));
	            	}else{
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_unlinkTableColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_unlinkedTooltip"));
	            	}	            		            	
	            	return objectTypeIcon;
	            }

	        });		
		
		 addGeneratedColumn(columnName_ObjectIsLocked, new Table.ColumnGenerator() {
	            /**
			 * 
			 */
			private static final long serialVersionUID = 2576048663634554543L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectTypeString=(String)item.getItemProperty(columnName_ObjectIsLocked).getValue();	
	            	Embedded objectTypeIcon;
	            	if(objectTypeString.equals("locked")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_lockTableColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_lockedTooltip"));
	            	}else{
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.AppPanel_unlockTableColumnIcon);
	            		objectTypeIcon.setDescription(
	            				userClientInfo.getI18NProperties().getProperty("contentManage_subObjectTable_unlockedTooltip"));
	            	}	            		            	
	            	return objectTypeIcon;
	            }

	        });		        
	}
	
	public void refreshTableDataSource(List<BaseContentObject> objectList,ContentOperationHelper coh){	
		
		IndexedContainer container = new IndexedContainer();			
		container.addContainerProperty(columnName_ObjectName, String.class,null);
        container.addContainerProperty(columnName_ObjectType, String.class,null);
        container.addContainerProperty(columnName_ObjectVersion, String.class,null);
        container.addContainerProperty(columnName_ObjectIsLinked, String.class,null);
        container.addContainerProperty(columnName_ObjectIsLocked, String.class,null); 		
		
		for(int i=0;i<objectList.size();i++){
			BaseContentObject cobj=objectList.get(i);			
			Item item = container.addItem(i+20);
			try {
				item.getItemProperty(columnName_ObjectName).setValue(cobj.getContentObjectName());	
				 
				String cType = coh.getContentObjectType(cobj);
				String contentType = "";
                if (cType.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)) {
                    contentType = "BinaryObject";
                } else if (cType.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)) {
                    contentType = "TextObject";
                } else if (cType.equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)) {
                    contentType = "FolderObject";
                } else if (cType.equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)) {
                    contentType = "StandaloneObject";
                } else {
                    contentType = "StandaloneObject";
                }
                item.getItemProperty(columnName_ObjectType).setValue(contentType); 
                item.getItemProperty(columnName_ObjectVersion).setValue(cobj.getCurrentVersion().getCurrentVersionNumber()); 
				if(cobj.isLocked()){
					item.getItemProperty(columnName_ObjectIsLocked).setValue("locked");
				}else{
					item.getItemProperty(columnName_ObjectIsLocked).setValue("unlocked");
				}
				//if(cobj.isLinkContentObject()){
				if(false){
					item.getItemProperty(columnName_ObjectIsLinked).setValue("linked");					
				}else{
					item.getItemProperty(columnName_ObjectIsLinked).setValue("unlinked");
				}				
			} catch (ReadOnlyException e) {				
				e.printStackTrace();
			} catch (ConversionException e) {				
				e.printStackTrace();
			} catch (ContentReposityException e) {				
				e.printStackTrace();
			}			
		}	

        container.sort(new Object[] { "Object Name" },new boolean[] { true });
        
        int newpageLeangth=objectList.size()+1;
        if(newpageLeangth<16){
        	 setPageLength(newpageLeangth);
        }		
		this.setContainerDataSource(container);
	}

	private IndexedContainer getSubContentObjects(){
		IndexedContainer container = new IndexedContainer();			
		container.addContainerProperty(columnName_ObjectName, String.class,null);
        container.addContainerProperty(columnName_ObjectType, String.class,null);
        container.addContainerProperty(columnName_ObjectVersion, String.class,null);
        container.addContainerProperty(columnName_ObjectIsLinked, String.class,null);
        container.addContainerProperty(columnName_ObjectIsLocked, String.class,null);  
        container.sort(new Object[] { "Object Name" },new boolean[] { true });		
		return container;
	}	
}
