package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class DataFieldValueEditor extends VerticalLayout{
	private static final long serialVersionUID = -1879674934473451811L;
	
	private UserClientInfo userClientInfo;
	private Map<String,ActivityData> activityDataMap;
	private Map<String,Label> activityDataValueLabelMap;	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	
	public DataFieldValueEditor(DataFieldDefinition[] dataFieldDefinitionArry,UserClientInfo userClientInfo,Map<String,ActivityData> activityDataMap){
		this.activityDataValueLabelMap=new HashMap<String,Label>();		
		this.userClientInfo=userClientInfo;
		this.activityDataMap=activityDataMap;		
		for(int i=0;i<dataFieldDefinitionArry.length;i++){
			DataFieldDefinition currentDataFieldDefinition=dataFieldDefinitionArry[i];
			this.addComponent(renderDataFieldDefinition(currentDataFieldDefinition,null));				
		}	
	}
	
	public DataFieldValueEditor(ActivityData[] activityDataArry,UserClientInfo userClientInfo,Map<String,ActivityData> activityDataMap){
		this.activityDataValueLabelMap=new HashMap<String,Label>();		
		this.userClientInfo=userClientInfo;
		this.activityDataMap=activityDataMap;		
		for(int i=0;i<activityDataArry.length;i++){
			DataFieldDefinition currentDataFieldDefinition=activityDataArry[i].getDataFieldDefinition();
			this.addComponent(renderDataFieldDefinition(currentDataFieldDefinition,activityDataArry[i]));				
		}	
	}
	
	public PropertyItem renderDataFieldDefinition(final DataFieldDefinition dataFieldDefinition,final ActivityData activityData){
		HorizontalLayout definationActionContainer=new HorizontalLayout();		
		definationActionContainer.setWidth("30px");		
		Button editDefinationButton=new Button();		
		editDefinationButton.setCaption(null);
		editDefinationButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editDefinationButton.setStyleName(BaseTheme.BUTTON_LINK);
		editDefinationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));		
		definationActionContainer.addComponent(editDefinationButton);
		
		editDefinationButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = 8399865348110168523L;

			public void buttonClick(ClickEvent event) {
				PropertyItem parentPropertyItem =(PropertyItem)( event.getComponent().getParent().getParent());
				renderUpdateDataFieldValueUI(dataFieldDefinition,parentPropertyItem);				
			}});		
		
		HorizontalLayout definationDetailLayout=new HorizontalLayout();		
		Label displayNameLabel=new Label("("+dataFieldDefinition.getDisplayName()+")");		
		definationDetailLayout.addComponent(displayNameLabel);
		
		HorizontalLayout definationProperLayout=new HorizontalLayout();
		definationProperLayout.setWidth("50px");		
	
		Embedded isMandatoryFieldIcon=null;
		if(dataFieldDefinition.isMandatoryField()){
			isMandatoryFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_MandatoryField);	
			isMandatoryFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_mandatoryFieldLabel"));
		}else{
			isMandatoryFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_NormalField);
			isMandatoryFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_normalFieldLabel"));
		}		
		Embedded isSystemFieldIcon=null;
		if(dataFieldDefinition.isSystemField()){
			isSystemFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_SystemField);
			isSystemFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_systemFieldLabel"));
		}else{
			isSystemFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_BusinessField);
			isSystemFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_businessFieldLabel"));
		}		
		
		definationProperLayout.addComponent(isMandatoryFieldIcon);
		definationProperLayout.addComponent(isSystemFieldIcon);		
		definationProperLayout.setComponentAlignment(isMandatoryFieldIcon, Alignment.MIDDLE_CENTER);
		definationProperLayout.setComponentAlignment(isSystemFieldIcon, Alignment.MIDDLE_CENTER);	
		
		definationDetailLayout.addComponent(definationProperLayout);		
		Label divLabel=new Label(":");
		definationDetailLayout.addComponent(divLabel);
		
		HorizontalLayout spaceDivLayout=new HorizontalLayout();
		spaceDivLayout.setWidth("10px");
		definationDetailLayout.addComponent(spaceDivLayout);
		
		Label fieldValueDisplay=new Label("",Label.CONTENT_XHTML);
		definationDetailLayout.addComponent(fieldValueDisplay);		
		this.activityDataValueLabelMap.put(dataFieldDefinition.getFieldName(), fieldValueDisplay);
		
		Embedded typeIcon=null;		
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.BINARY:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binary"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binaryArray"));
				}
				break;
			case PropertyType.BOOLEAN:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_boolean"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_booleanArray"));
				}
				break;
			case PropertyType.DATE:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_date"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_dateArray"));
				}
				break;
			case PropertyType.DECIMAL:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimale"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimalArray"));
				}
				break;
			case PropertyType.DOUBLE:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_double"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_doubleArray"));
				}
				break;
			case PropertyType.LONG:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_long"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_longArray"));	
				}
				break;
			case PropertyType.STRING:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_string"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_stringArray"));
				}
				break;
		}		
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,typeIcon,dataFieldDefinition.getFieldName(),
				dataFieldDefinition.getDescription(),definationDetailLayout,definationActionContainer);		
		
		if(activityData!=null&&activityData.getDatFieldValue()!=null){
			fieldValueDisplay.setValue(dataValueDisplayStr(dataFieldDefinition,activityData));			
		}		
		return namePropertyItem;
	}
	
	private String dataValueDisplayStr(DataFieldDefinition dataFieldDefinition,ActivityData activityData){
		String valueStyleFormatPerfix="<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>";
	    String valueStyleFormatPostfix="</span>";
		String dataValueDisplayStr="";
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.BINARY:
				if(!dataFieldDefinition.isArrayField()){					
					Binary dataValue=(Binary)activityData.getDatFieldValue();
					try {
						dataValueDisplayStr=valueStyleFormatPerfix+userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_sizeLabel")+"["+dataValue.getSize()+"]"+valueStyleFormatPostfix;						
					} catch (RepositoryException e) {						
						e.printStackTrace();
					}					
				}else{					
					Binary[] dataValue=(Binary[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(Binary currentValue:dataValue){
						sb.append(valueStyleFormatPerfix);
						try {
							sb.append(userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_sizeLabel")+"["+currentValue.getSize()+"]");
						} catch (RepositoryException e) {							
							e.printStackTrace();
						}
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();					
				}
				break;			
			case PropertyType.BOOLEAN:
				if(!dataFieldDefinition.isArrayField()){
					dataValueDisplayStr=valueStyleFormatPerfix+((Boolean)activityData.getDatFieldValue()).booleanValue()+valueStyleFormatPostfix;
				}else{
					boolean[] dataValueArray=(boolean[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(boolean currentValue:dataValueArray){
						sb.append(valueStyleFormatPerfix);
						sb.append(currentValue);
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();					
				}
				break;
			case PropertyType.DATE:
				if(!dataFieldDefinition.isArrayField()){
					Calendar dataValue=(Calendar)activityData.getDatFieldValue();				
					dataValueDisplayStr=valueStyleFormatPerfix+formatter.format(dataValue.getTime())+valueStyleFormatPostfix;
				}else{
					Calendar[] dataValue=(Calendar[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(Calendar currentValue:dataValue){
						sb.append(valueStyleFormatPerfix);
						sb.append(formatter.format(currentValue.getTime()));
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();
				}
				break;				
			case PropertyType.DECIMAL:
				if(!dataFieldDefinition.isArrayField()){
					BigDecimal dataValue=(BigDecimal)activityData.getDatFieldValue();					
					dataValueDisplayStr=valueStyleFormatPerfix+dataValue.doubleValue()+valueStyleFormatPostfix;
				}else{
					BigDecimal[] dataValue=(BigDecimal[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(BigDecimal currentValue:dataValue){
						sb.append(valueStyleFormatPerfix);
						sb.append(currentValue.doubleValue());
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();
				}
				break;
			case PropertyType.DOUBLE:
				if(!dataFieldDefinition.isArrayField()){
					dataValueDisplayStr=valueStyleFormatPerfix+((Double)activityData.getDatFieldValue()).doubleValue()+valueStyleFormatPostfix;
				}else{
					double[] dataValueArray=(double[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(double currentValue:dataValueArray){
						sb.append(valueStyleFormatPerfix);
						sb.append(currentValue);
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();
				}
				break;
			case PropertyType.LONG:
				if(!dataFieldDefinition.isArrayField()){
					dataValueDisplayStr=valueStyleFormatPerfix+((Long)activityData.getDatFieldValue()).longValue()+valueStyleFormatPostfix;
				}else{
					long[] dataValueArray=(long[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(long currentValue:dataValueArray){
						sb.append(valueStyleFormatPerfix);
						sb.append(currentValue);
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();
				}
				break;
			case PropertyType.STRING:
				if(!dataFieldDefinition.isArrayField()){
					dataValueDisplayStr=valueStyleFormatPerfix+activityData.getDatFieldValue().toString()+valueStyleFormatPostfix;
				}else{
					String[] dataValueArray=(String[])activityData.getDatFieldValue();
					StringBuffer sb=new StringBuffer();
					for(String currentValue:dataValueArray){
						sb.append(valueStyleFormatPerfix);
						sb.append(currentValue);
						sb.append(valueStyleFormatPostfix);
						sb.append(";");						
					}
					dataValueDisplayStr=sb.toString();
				}
				break;
		}		
		return dataValueDisplayStr;
	}
	
	public void renderUpdateDataFieldValueUI(DataFieldDefinition dataFieldDefinition,PropertyItem parentPropertyItem){	
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueEditor_dataFieldValueWindowTitle")+" : "+"</b> <b style='color:#ce0000;'>" +dataFieldDefinition.getFieldName()+ "</b>", Label.CONTENT_XHTML);	
		DataFieldValueForm dataFieldValueForm=new DataFieldValueForm(dataFieldDefinition,this.userClientInfo,this.activityDataMap,this.activityDataValueLabelMap,this.getApplication());
		LightContentWindow lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,dataFieldValueForm,"550px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);	
	}
}