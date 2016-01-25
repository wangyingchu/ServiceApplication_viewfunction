package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import com.vaadin.Application;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

import org.apache.jackrabbit.value.ValueFactoryImpl;

public class DataFieldValueForm extends VerticalLayout{
	private static final long serialVersionUID = 2313287451570127682L;
	
	private String inputFieldIdPerfix="inputFieldIdPerfix";
	private int inputFieldNumber=1;
	private BaseForm newDataFieldValueForm;	
	private Map<String,Field> additionalInputFieldMap;
	private Map<String,ActivityData> activityDataMap;
	private Map<String,Label> activityDataValueLabelMap;
	private List<String> binrayTypeFieldValueList;
	private UserClientInfo userClientInfo;
	private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private  BinaryDataFieldUploader binaryDataFieldUploader;
	
	public DataFieldValueForm(final DataFieldDefinition dataFieldDefinition,UserClientInfo userClientInfo,Map<String,ActivityData> activityDataMap,Map<String,Label> activityDataValueLabelMap,Application application){	
		this.additionalInputFieldMap=new HashMap<String,Field>();
		this.activityDataMap=activityDataMap;
		this.activityDataValueLabelMap=activityDataValueLabelMap;
		this.userClientInfo=userClientInfo;
		String fieldType="";		
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.STRING:fieldType="String";break;
			case PropertyType.BOOLEAN:fieldType="Boolean";break;
			case PropertyType.DATE:fieldType="Date";break;
			case PropertyType.DECIMAL:fieldType="Decimal";break;
			case PropertyType.DOUBLE:fieldType="Double";break;
			case PropertyType.LONG:fieldType="Long";break;
			case PropertyType.BINARY:fieldType="Binary";break;
		}
		
		String typeDisplayStr="";
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.STRING:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_String");break;
			case PropertyType.BOOLEAN:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Boolean");break;
			case PropertyType.DATE:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Date");break;
			case PropertyType.DECIMAL:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Decimal");break;
			case PropertyType.DOUBLE:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Double");break;
			case PropertyType.LONG:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Long");break;
			case PropertyType.BINARY:typeDisplayStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueType_Binary");break;
		}
		
		Label fieldTypeLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_inputPrompt1")+
				" <b style='color:#ce0000;'>"+typeDisplayStr+"</b> "+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_inputPrompt2"),Label.CONTENT_XHTML);		
		this.addComponent(fieldTypeLabel);		
		newDataFieldValueForm=new BaseForm();	
		if(dataFieldDefinition.getFieldType()!=PropertyType.BINARY){
			newDataFieldValueForm.addField(inputFieldIdPerfix+1,getInputField(dataFieldDefinition));	
		}else{
			binrayTypeFieldValueList = new ArrayList<String>();
			VerticalLayout newBinaryPropertyValueLayout = new VerticalLayout();
            newBinaryPropertyValueLayout.setWidth("500px");
            newBinaryPropertyValueLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
            this.binaryDataFieldUploader= new BinaryDataFieldUploader(newBinaryPropertyValueLayout, binrayTypeFieldValueList, newDataFieldValueForm,dataFieldDefinition,this.activityDataMap,application,this.userClientInfo);
            newDataFieldValueForm.getLayout().addComponent(this.binaryDataFieldUploader);
            newDataFieldValueForm.getLayout().addComponent(new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_addPropertyLabel")));
            newDataFieldValueForm.getLayout().addComponent(newBinaryPropertyValueLayout);			
		}	
		boolean isArrayField=dataFieldDefinition.isArrayField();		
		if(isArrayField&&dataFieldDefinition.getFieldType()!=PropertyType.BINARY){
			Button addMoreValueButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_addMoreValueButton"));
			addMoreValueButton.setStyleName(BaseTheme.BUTTON_LINK);
			
			addMoreValueButton.addListener(new ClickListener(){            		
				private static final long serialVersionUID = -3567064194516804327L;

				public void buttonClick(ClickEvent event) {							
					renderNewDataField(dataFieldDefinition,-1);
				}});	
			
			List<Button> buttonList = new ArrayList<Button>();		   
		    buttonList.add(addMoreValueButton);
		    BaseButtonBar startNewActivityButtonBar = new BaseButtonBar(300, 45, Alignment.MIDDLE_RIGHT, buttonList);
		    newDataFieldValueForm.getLayout().addComponent(startNewActivityButtonBar);		    
		    
		    String fieldName=dataFieldDefinition.getFieldName();			
			Object existingDataValue=null;
			if(this.activityDataMap!=null&&this.activityDataMap.get(fieldName)!=null){
				existingDataValue=this.activityDataMap.get(fieldName).getDatFieldValue();
			}					
		    if(existingDataValue!=null){
		    	if(fieldType.equals("Boolean")){
		    		boolean[] valueObjectArray=(boolean[])existingDataValue;
			    	if(valueObjectArray.length>1){
			    		for(int i=0;i<valueObjectArray.length-1;i++){
			    			renderNewDataField(dataFieldDefinition,i+1);
			    		}		    		
			    	}		
		    	}else if(fieldType.equals("Double")){
		    		double[] valueObjectArray=(double[])existingDataValue;
			    	if(valueObjectArray.length>1){
			    		for(int i=0;i<valueObjectArray.length-1;i++){
			    			renderNewDataField(dataFieldDefinition,i+1);
			    		}		    		
			    	}	
		    	}else if(fieldType.equals("Long")){
		    		long[] valueObjectArray=(long[])existingDataValue;
			    	if(valueObjectArray.length>1){
			    		for(int i=0;i<valueObjectArray.length-1;i++){
			    			renderNewDataField(dataFieldDefinition,i+1);
			    		}		    		
			    	}	
		    	}else{
		    		Object[] valueObjectArray=(Object[])existingDataValue;
			    	if(valueObjectArray.length>1){
			    		for(int i=0;i<valueObjectArray.length-1;i++){
			    			renderNewDataField(dataFieldDefinition,i+1);
			    		}		    		
			    	}		    		
		    	}		    	 	
		    }		    
		}			
		
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_addDataFieldValue_confirmButton"));		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_addDataFieldValue_cancelButton"));
		okButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -2893061745634227087L;

			public void buttonClick(ClickEvent event) {	 
				saveDataFieldValue(dataFieldDefinition);
            }
        });	
		cancelAddbutton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 2872743438310799391L;

			public void buttonClick(ClickEvent event) {	 
            	closeWindow();
            }
        });		
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);		 
		List<Button> buttonList = new ArrayList<Button>();
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addDataFieldValueButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    newDataFieldValueForm.getFooter().addComponent(addDataFieldValueButtonBar);		
		this.addComponent(newDataFieldValueForm);
	}
	
	public void saveDataFieldValue(DataFieldDefinition dataFieldDefinition){
		newDataFieldValueForm.setValidationVisible(true);
        boolean validateResult = newDataFieldValueForm.isValid();
        if(!validateResult){
        	return;
        }
        String valueStyleFormatPerfix="<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>";
        String valueStyleFormatPostfix="</span>";
        ActivityData activityData=null;
        switch(dataFieldDefinition.getFieldType()){
        	case PropertyType.STRING:
        		validateResult=validateStringField();
        		if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
        				String[] dataValueArray=new String[additionalInputFieldMap.size()+1];
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];
        				fieldCollection.toArray(fieldArray);
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){        					
        					dataValueArray[i]=fieldArray[i].getValue().toString();        					
        					sb.append(valueStyleFormatPerfix+dataValueArray[i]+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");        					
        				}
        				dataValueArray[additionalInputFieldMap.size()]=newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue().toString(); 
        				sb.append(valueStyleFormatPerfix+dataValueArray[additionalInputFieldMap.size()]+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);        				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,singleField.getValue().toString());
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+singleField.getValue().toString()+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow();
        		}
        	break; 
        	case PropertyType.BOOLEAN:  
        		validateResult=validateBooleanField();        		
        		if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
        				boolean[] dataValueArray=new boolean[additionalInputFieldMap.size()+1];        				
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];        				
        				fieldCollection.toArray(fieldArray);        				
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){        					
        					dataValueArray[i]=Boolean.parseBoolean(fieldArray[i].getValue().toString());
        					sb.append(valueStyleFormatPerfix+dataValueArray[i]+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");        					
        				}
        				dataValueArray[additionalInputFieldMap.size()]=Boolean.parseBoolean(newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue().toString()); 
        				sb.append(valueStyleFormatPerfix+dataValueArray[additionalInputFieldMap.size()]+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);        				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,new Boolean(singleField.getValue().toString()));
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+singleField.getValue().toString()+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow();  
        		}        		
        	break;
        	case PropertyType.DATE:
        		validateResult=validateDateField(); 
        		if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
                        Calendar[] dataValueArray=new Calendar[additionalInputFieldMap.size()+1];        				
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];        				
        				fieldCollection.toArray(fieldArray);        				
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){ 
        					Calendar calDate = Calendar.getInstance();
        					calDate.setTime((Date) fieldArray[i].getValue());
        					sb.append(valueStyleFormatPerfix+formatter.format((Date)fieldArray[i].getValue())+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");
        					dataValueArray[i]=calDate; 					        					
        				}
        				Calendar calDate = Calendar.getInstance();
        				calDate.setTime((Date)(newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue()));        				
        				dataValueArray[additionalInputFieldMap.size()]=calDate; 
        				sb.append(valueStyleFormatPerfix+formatter.format(calDate.getTime())+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);  
        				Calendar calDate = Calendar.getInstance();
        				calDate.setTime((Date)singleField.getValue());         				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,calDate);
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+formatter.format((Date)singleField.getValue())+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow();  
        		} 
        	break;
        	case PropertyType.DECIMAL:        	
        		validateResult=validateDecimalField();
        		if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
        				BigDecimal[] dataValueArray=new BigDecimal[additionalInputFieldMap.size()+1];        				
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];        				
        				fieldCollection.toArray(fieldArray);        				
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){ 
        					sb.append(valueStyleFormatPerfix+fieldArray[i].getValue().toString()+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");
        					dataValueArray[i]=new BigDecimal(fieldArray[i].getValue().toString()); 	
        				}
        				dataValueArray[additionalInputFieldMap.size()]=new BigDecimal(newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue().toString()); 
        				sb.append(valueStyleFormatPerfix+dataValueArray[additionalInputFieldMap.size()]+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);        				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,new BigDecimal(singleField.getValue().toString()));
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+singleField.getValue().toString()+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow();  
        		}        		
        	break;			
			case PropertyType.DOUBLE:
				validateResult=validateDoubleField();
				if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
        				double[] dataValueArray=new double[additionalInputFieldMap.size()+1];        				
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];        				
        				fieldCollection.toArray(fieldArray);        				
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){        					
        					dataValueArray[i]=Double.parseDouble(fieldArray[i].getValue().toString());
        					sb.append(valueStyleFormatPerfix+dataValueArray[i]+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");        					
        				}
        				dataValueArray[additionalInputFieldMap.size()]=Double.parseDouble(newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue().toString()); 
        				sb.append(valueStyleFormatPerfix+dataValueArray[additionalInputFieldMap.size()]+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);        				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,new Double(singleField.getValue().toString()));
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+singleField.getValue().toString()+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow();  
        		}        		
			break;
			case PropertyType.LONG:
				validateResult=validateLongField();
				if(validateResult){
        			if(dataFieldDefinition.isArrayField()){
        				long[] dataValueArray=new long[additionalInputFieldMap.size()+1];        				
        				Collection<Field> fieldCollection=additionalInputFieldMap.values();
        				Field[] fieldArray=new Field[additionalInputFieldMap.size()];        				
        				fieldCollection.toArray(fieldArray);        				
        				StringBuffer sb=new StringBuffer(); 
        				for(int i=0;i<fieldArray.length;i++){        					
        					dataValueArray[i]=Long.parseLong(fieldArray[i].getValue().toString());
        					sb.append(valueStyleFormatPerfix+dataValueArray[i]+valueStyleFormatPostfix);
        					sb.append("<span style='color:#222222'>;</span>");        					
        				}
        				dataValueArray[additionalInputFieldMap.size()]=Long.parseLong(newDataFieldValueForm.getField(inputFieldIdPerfix+1).getValue().toString()); 
        				sb.append(valueStyleFormatPerfix+dataValueArray[additionalInputFieldMap.size()]+valueStyleFormatPostfix);
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,dataValueArray); 
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
        			}else{      
        				Field singleField=newDataFieldValueForm.getField(inputFieldIdPerfix+1);        				
        				activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,new Long(singleField.getValue().toString()));
        				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+singleField.getValue().toString()+valueStyleFormatPostfix);
        			}
        			this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
        			closeWindow(); 			
				}
			break;
			case PropertyType.BINARY:
				validateResult=validateBinaryField();
				if(validateResult){
					if(dataFieldDefinition.isArrayField()){	
						List<Binary> existingBinaryList=this.binaryDataFieldUploader.getExistingBinaryDataList();						
						List<Binary> binaryList=new ArrayList<Binary>();						
						Binary[] valueArray = new Binary[binrayTypeFieldValueList.size()+existingBinaryList.size()];	
						File fieldFile = null;
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < binrayTypeFieldValueList.size(); i++) {
	                        try {
	                        	fieldFile = new File(tempFileDir + binrayTypeFieldValueList.get(i));	                         
	                        	Binary currentBinary=ValueFactoryImpl.getInstance().createBinary(new FileInputStream(fieldFile));
	                        	binaryList.add(currentBinary);	                            
	                            sb.append(valueStyleFormatPerfix+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_sizeLabel")+
	                            		"["+currentBinary.getSize()+"]"+valueStyleFormatPostfix);
	                            fieldFile.delete();
	                        } catch (FileNotFoundException ex) {
	                            ex.printStackTrace();
	                        } catch (RepositoryException ex) {
	                           ex.printStackTrace();
	                        }    
	     					sb.append("<span style='color:#222222'>;</span>");                        
	                     }
						for(Binary binary:existingBinaryList){
							binaryList.add(binary);                            
                            try {
								sb.append(valueStyleFormatPerfix+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_sizeLabel")+
										"["+binary.getSize()+"]"+valueStyleFormatPostfix);
							} catch (RepositoryException e) {								
								e.printStackTrace();
							}							
                            sb.append("<span style='color:#222222'>;</span>");							
						}
						binaryList.toArray(valueArray);	
						activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,valueArray); 						
	     				activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(sb.toString());
					}else{						
						try {
							Binary binary=null;
							if(binrayTypeFieldValueList.size()>0){
								File fieldFile = new File(tempFileDir + binrayTypeFieldValueList.get(0));
								binary=ValueFactoryImpl.getInstance().createBinary(new FileInputStream(fieldFile));
								fieldFile.delete();
							}else{
								binary=this.binaryDataFieldUploader.getExistingBinaryDataList().get(0);
							}							
							activityData=ActivityComponentFactory.createActivityData(dataFieldDefinition,binary); 
							activityDataValueLabelMap.get(dataFieldDefinition.getFieldName()).setValue(valueStyleFormatPerfix+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_sizeLabel")
									+"["+binary.getSize()+"]"+valueStyleFormatPostfix);		     				
						} catch (FileNotFoundException e) {						
							e.printStackTrace();
						} catch (RepositoryException e) {						
							e.printStackTrace();
						}
					}
					this.activityDataMap.put(dataFieldDefinition.getFieldName(), activityData);  
	    			closeWindow(); 	
			}
			break;
		} 
	}
	
	public Field getInputField(DataFieldDefinition dataFieldDefinition){
		String fieldName=dataFieldDefinition.getFieldName();
		boolean isArrayField=dataFieldDefinition.isArrayField();
		Object existingDataValue=null;
		if(this.activityDataMap!=null&&this.activityDataMap.get(fieldName)!=null){
			existingDataValue=this.activityDataMap.get(fieldName).getDatFieldValue();
		}		
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.STRING:
				TextField dataValue=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));		
				dataValue.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValueInputPrompt"));
				dataValue.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
				dataValue.setWidth("250px");
				dataValue.setRequired(true);				
				if(existingDataValue!=null){
					if(!isArrayField){
						dataValue.setValue(existingDataValue.toString());						
					}else{						
						String stringInitData=((String[])existingDataValue)[0];
						dataValue.setValue(stringInitData);						
					}					
				}				
				return dataValue;
			case PropertyType.BOOLEAN:
				NativeSelect booleanValueSelect = new NativeSelect(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));
                booleanValueSelect.addItem(true);
                booleanValueSelect.addItem(false);
                booleanValueSelect.setRequired(true);
                booleanValueSelect.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
                if(existingDataValue!=null){
                	boolean booleanInitValue;
					if(!isArrayField){						
						booleanInitValue=((Boolean)existingDataValue).booleanValue();					
					}else{	
						booleanInitValue=((boolean[])existingDataValue)[0];											
					}	
					booleanValueSelect.setValue(booleanInitValue);
				}
                return booleanValueSelect;
			case PropertyType.LONG:
				TextField longValueInput = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));
	            longValueInput.addValidator(new IntegerValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_longTypeErrMsg")));
	            longValueInput.setWidth("250px");
	            longValueInput.setRequired(true);
	            longValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
	            if(existingDataValue!=null){
                	long longInitValue;
					if(!isArrayField){						
						longInitValue=((Long)existingDataValue).longValue();				
					}else{	
						longInitValue=((long[])existingDataValue)[0];											
					}	
					longValueInput.setValue(longInitValue);
				} 
	            return longValueInput;
			case PropertyType.DATE:	            
	            PopupDateField dateSelect = new PopupDateField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));
                dateSelect.setWidth("140px");
                dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
                dateSelect.setRequired(true);
                dateSelect.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
                if(existingDataValue!=null){
                	Calendar dateInitValue;
					if(!isArrayField){						
						dateInitValue=(Calendar)existingDataValue;				
					}else{	
						dateInitValue=((Calendar[])existingDataValue)[0];											
					}	
					dateSelect.setValue(dateInitValue.getTime());
				} 
                return dateSelect;
			case PropertyType.DECIMAL:  
                TextField decimalValueInput = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));
                decimalValueInput.addValidator(new DoubleValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_decimalTypeErrMsg")));
                decimalValueInput.setWidth("250px");
                decimalValueInput.setRequired(true);
                decimalValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
                if(existingDataValue!=null){
                	double decimalInitValue;
					if(!isArrayField){						
						decimalInitValue=((BigDecimal)existingDataValue).doubleValue();			
					}else{	
						decimalInitValue=((BigDecimal[])existingDataValue)[0].doubleValue();											
					}	
					decimalValueInput.setValue(decimalInitValue);
				} 
                return decimalValueInput;
			case PropertyType.DOUBLE:     
                TextField doubleValueInput = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValuePrompt"));
                doubleValueInput.addValidator(new DoubleValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_doubleTypeErrMsg")));
                doubleValueInput.setWidth("250px");
                doubleValueInput.setRequired(true);
                doubleValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
                if(existingDataValue!=null){
                	double doubleInitValue;
					if(!isArrayField){						
						doubleInitValue=((Double)existingDataValue).doubleValue();			
					}else{	
						doubleInitValue=((double[])existingDataValue)[0];											
					}	
					doubleValueInput.setValue(doubleInitValue);
				} 
                return doubleValueInput;                 
		}		
		return null;
	}
	
	public void renderNewDataField(DataFieldDefinition dataFieldDefinition,int arrayFieldDataIndex){
		int fieldIdx=inputFieldNumber++;
		HorizontalLayout newFieldLayout=renderDataInputLayout(dataFieldDefinition,fieldIdx,arrayFieldDataIndex);
		newDataFieldValueForm.getLayout().addComponent(newFieldLayout);
	}
	
	public HorizontalLayout renderDataInputLayout(DataFieldDefinition dataFieldDefinition,int fieldIndex,int arrayFieldDataIndex){	
		final String fieldMapKey=inputFieldIdPerfix+fieldIndex;
		HorizontalLayout inputFieldContainer=new HorizontalLayout();		
		final int fieldType=dataFieldDefinition.getFieldType();
		switch(fieldType){				
			case PropertyType.BOOLEAN:
				NativeSelect booleanValueSelect = new NativeSelect();
                booleanValueSelect.addItem(true);
                booleanValueSelect.addItem(false);
                booleanValueSelect.setImmediate(true);
                booleanValueSelect.setRequired(true);
                booleanValueSelect.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
                booleanValueSelect.addListener(new ValueChangeListener() {
					private static final long serialVersionUID = -5685556583252822015L;

					public void valueChange(ValueChangeEvent event) {						
						validateBooleanField();						
					}
				});
                if(arrayFieldDataIndex!=-1){
					String fieldName=dataFieldDefinition.getFieldName();			
					boolean[] dataValueArray=(boolean[])this.activityDataMap.get(fieldName).getDatFieldValue();				
					boolean currentValue= dataValueArray[arrayFieldDataIndex]; 
					booleanValueSelect.setValue(currentValue);
				}		
				inputFieldContainer.addComponent(booleanValueSelect);				
				additionalInputFieldMap.put(fieldMapKey, booleanValueSelect);
				inputFieldNumber++;				
				break;					
			case PropertyType.STRING:
				TextField stringFieldValue=new TextField();	
				stringFieldValue.setImmediate(true);
				stringFieldValue.setRequired(true);			
				stringFieldValue.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_dataFieldValueInputPrompt"));
				stringFieldValue.setWidth("250px");
				stringFieldValue.addListener(new ValueChangeListener() {
					private static final long serialVersionUID = 6714087021045885007L;

					public void valueChange(ValueChangeEvent event) {
						validateStringField();						
					}
				});					
				if(arrayFieldDataIndex!=-1){
					String fieldName=dataFieldDefinition.getFieldName();			
					String[] dataValueArray=(String[])this.activityDataMap.get(fieldName).getDatFieldValue();				
					String currentValue= dataValueArray[arrayFieldDataIndex]; 
					stringFieldValue.setValue(currentValue);
				}				
				inputFieldContainer.addComponent(stringFieldValue);				
				additionalInputFieldMap.put(fieldMapKey, stringFieldValue);
				inputFieldNumber++;
				break;
			case PropertyType.DATE:
				 PopupDateField dateSelect = new PopupDateField();
				 dateSelect.setImmediate(true);
	             dateSelect.setWidth("140px");
	             dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
	             dateSelect.setRequired(true);
	             dateSelect.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
	             dateSelect.addListener(new ValueChangeListener() {					
					private static final long serialVersionUID = -846041183239033543L;

						public void valueChange(ValueChangeEvent event) {
							validateDateField();						
						}
					});
	             if(arrayFieldDataIndex!=-1){
						String fieldName=dataFieldDefinition.getFieldName();			
						Calendar[] dataValueArray=(Calendar[])this.activityDataMap.get(fieldName).getDatFieldValue();				
						Calendar currentValue= dataValueArray[arrayFieldDataIndex]; 
						dateSelect.setValue(currentValue.getTime());
					}	
	             inputFieldContainer.addComponent(dateSelect);				
	             additionalInputFieldMap.put(fieldMapKey, dateSelect);
	             inputFieldNumber++;
	             break;
			case PropertyType.DECIMAL:
				 TextField decimalValueInput = new TextField();
				 decimalValueInput.setImmediate(true);
	             decimalValueInput.addValidator(new DoubleValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_decimalTypeErrMsg")));
	             decimalValueInput.setWidth("250px");
	             decimalValueInput.setRequired(true);
	             decimalValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
	             decimalValueInput.addListener(new ValueChangeListener() {
					private static final long serialVersionUID = -6372944241883917219L;

							public void valueChange(ValueChangeEvent event) {
								validateDecimalField();						
							}
						});
	             if(arrayFieldDataIndex!=-1){
						String fieldName=dataFieldDefinition.getFieldName();			
						BigDecimal[] dataValueArray=(BigDecimal[])this.activityDataMap.get(fieldName).getDatFieldValue();				
						BigDecimal currentValue= dataValueArray[arrayFieldDataIndex]; 
						decimalValueInput.setValue(currentValue.doubleValue());
					}	
	             inputFieldContainer.addComponent(decimalValueInput);				
	             additionalInputFieldMap.put(fieldMapKey, decimalValueInput);
	             inputFieldNumber++;
	             break;
			case PropertyType.DOUBLE:
				 TextField doubleValueInput = new TextField();
				 doubleValueInput.setImmediate(true);
	             doubleValueInput.addValidator(new DoubleValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_doubleTypeErrMsg")));
	             doubleValueInput.setWidth("250px");
	             doubleValueInput.setRequired(true);
	             doubleValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
	             doubleValueInput.addListener(new ValueChangeListener() {					
					private static final long serialVersionUID = -4961371735225276467L;

								public void valueChange(ValueChangeEvent event) {
									validateDoubleField();						
								}
							});
	             if(arrayFieldDataIndex!=-1){
						String fieldName=dataFieldDefinition.getFieldName();			
						double[] dataValueArray=(double[])this.activityDataMap.get(fieldName).getDatFieldValue();				
						double currentValue= dataValueArray[arrayFieldDataIndex]; 
						doubleValueInput.setValue(currentValue);
					}		
	             inputFieldContainer.addComponent(doubleValueInput);				
	             additionalInputFieldMap.put(fieldMapKey, doubleValueInput);
	             inputFieldNumber++;
	             break;
			case PropertyType.LONG:
				TextField longValueInput = new TextField();
				longValueInput.setImmediate(true);
	            longValueInput.addValidator(new IntegerValidator(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_longTypeErrMsg")));
	            longValueInput.setWidth("250px");
	            longValueInput.setRequired(true);
	            longValueInput.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg"));
	            longValueInput.addListener(new ValueChangeListener() {
					private static final long serialVersionUID = 3707012773881225056L;

								public void valueChange(ValueChangeEvent event) {
									validateLongField();						
								}
							});
	            if(arrayFieldDataIndex!=-1){
					String fieldName=dataFieldDefinition.getFieldName();			
					long[] dataValueArray=(long[])this.activityDataMap.get(fieldName).getDatFieldValue();				
					long currentValue= dataValueArray[arrayFieldDataIndex]; 
					longValueInput.setValue(currentValue);
				}	
	            inputFieldContainer.addComponent(longValueInput);				
	            additionalInputFieldMap.put(fieldMapKey, longValueInput);
	            inputFieldNumber++;	            
	            break;	
			case PropertyType.BINARY:break;
		}		
		Button deleteCurrentValueButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_deleteValueButton"));			
		deleteCurrentValueButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -2893061745634227087L;

			public void buttonClick(ClickEvent event) {
				additionalInputFieldMap.remove(fieldMapKey);
				switch(fieldType){
					case PropertyType.STRING:validateStringField();break;
					case PropertyType.BOOLEAN:validateBooleanField();break;
					case PropertyType.DATE:validateDateField();break;
					case PropertyType.DECIMAL:validateDecimalField();break;
					case PropertyType.DOUBLE:validateDoubleField();break;
					case PropertyType.LONG:validateLongField();break;
				}			
				newDataFieldValueForm.getLayout().removeComponent(event.getComponent().getParent());
			}});		
		deleteCurrentValueButton.setStyleName(BaseTheme.BUTTON_LINK);
		inputFieldContainer.addComponent(deleteCurrentValueButton);
		return inputFieldContainer;
	}
	
	private void closeWindow(){	
		LightContentWindow ContainerWindow=(LightContentWindow)(this.getParent().getParent());
		this.getApplication().getMainWindow().removeWindow(ContainerWindow);
		ContainerWindow=null;		
	}
	
	private boolean validateStringField(){
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();		    
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);
				newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));		    	
			}	
		}
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;
	}
	
	private boolean validateBooleanField(){	
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){			
			NativeSelect nativeSelect=(NativeSelect)currentField;			
			boolean fieldHasDataCheck = nativeSelect.getValue() == null ? false : true; 			
			validateResult =validateResult &fieldHasDataCheck;
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);
				newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));		    	
			}	
		}
		if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;
	}
	
	private boolean validateDateField(){
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();		    
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);
				newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));		    	
			}	
		}
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;
	}
	
	private boolean validateDecimalField(){
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();	
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);				
				if(currentField.getValue()!=null&&currentField.getValue().equals("")){
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));	
				}else{
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_decimalTypeErrMsg")));
				}					    	
			}	
		}
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;	
	}
	
	private boolean validateDoubleField(){
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();	
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);				
				if(currentField.getValue()!=null&&currentField.getValue().equals("")){
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));	
				}else{
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_doubleTypeErrMsg")));
				}					    	
			}	
		}
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;	
	}
	
	private boolean validateLongField(){
		boolean validateResult=true;
		Collection<Field> fieldCollection=additionalInputFieldMap.values();
		Field[] fieldArray=new Field[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		for(Field currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();	
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);				
				if(currentField.getValue()!=null&&currentField.getValue().equals("")){
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));	
				}else{
					newDataFieldValueForm.setComponentError(null);
					newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_longTypeErrMsg")));
				}					    	
			}	
		}
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;	
	}
	
	private boolean validateBinaryField(){
		boolean validateResult=true;
		if(binrayTypeFieldValueList.size()==0&&this.binaryDataFieldUploader.getExistingBinaryDataList().size()==0){
			newDataFieldValueForm.setValidationVisible(true);
			newDataFieldValueForm.setComponentError(null);
			newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DataFieldValueForm_valueRequiredErrMsg")));
			validateResult=false;
		}else{
			newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	      	validateResult=true;
		}
		return validateResult;		
	}
}