package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util;

import java.io.Serializable;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.BinaryContentFileProgressMonitoringUploader;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.BinaryPropertyFileProgressMonitoringUploader;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.ContentManagementPanel;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightDialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.AddContentPropertiesResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.AddSubContentObjectResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.DeleteContentObjectResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.AddBinaryContentObjectResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.value.ValueFactoryImpl;


public class ContentObjectUIElementCreator implements Serializable{	
	private static final long serialVersionUID = 59524184896165659L;
	
	private HierarchicalContainer contentSpaceDataContainer;	
	private ContentManagementPanel contentManagementPanel;
	private final UserClientInfo userClientInfo;
	private final Application application;	
	private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
	
	private static DialogWindow addContentObjectWindow;
	private static DialogWindow deleteContentObjectConfirmWindow;
	private static DialogWindow addBinaryContentFormWindow;
	private static DialogWindow addTextContentFormWindow;
	
	private static DialogWindow addPropertyWindow;

	private static LightDialogWindow addContentObjectBooleanTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectDoubleTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectLongTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectStringTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectDateTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectDecimalTypePropertyFormWindow;
	private static LightDialogWindow addContentObjectBinaryTypePropertyFormWindow;	
	
	public ContentObjectUIElementCreator( HierarchicalContainer contentSpace_DataContainer,UserClientInfo userClientInfo,ContentManagementPanel contentManagementPanel){
		this.contentSpaceDataContainer=contentSpace_DataContainer;
		this.userClientInfo=userClientInfo;
		this.contentManagementPanel=contentManagementPanel;
		this.application=contentManagementPanel.getApplication();
	}
	
	 public DialogWindow createAddContentObjectFormWindow(Object targetItem) {
	        Object contentObjectName = this.contentSpaceDataContainer.getItem(targetItem).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();	        
	        String windowTitle = "Add Content Object";
	        String windowDesc = "Add new content object in parent content object " + CommonStyleUtil.formatCurrentItemStyle(contentObjectName.toString());
	        addContentObjectWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, createAddSubContentObjectForm(targetItem));
	        return addContentObjectWindow;
	 }	

	 private Layout createAddSubContentObjectForm(Object actionItemID) {
	        final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();
	        final String parentNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, actionItemID);
	        final VerticalLayout formContainer = new VerticalLayout();
	        final BaseForm newContentObjectForm = new BaseForm();
	        final Map<String, ContentObjectProperty> contentObjectPropertyMap = new HashMap<String, ContentObjectProperty>();
	        VerticalLayout formAdvLayout = new VerticalLayout();
	        formAdvLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	        newContentObjectForm.setLayout(formAdvLayout);
	        final Object parentItemId = actionItemID;
	        final int currentChildNum;
	        if (this.contentSpaceDataContainer.getChildren(parentItemId) == null) {
	            currentChildNum = 0;
	        } else {
	            currentChildNum = this.contentSpaceDataContainer.getChildren(parentItemId).size();
	        }
	        newContentObjectForm.setImmediate(true);
	        final TextField newContentObjectName = new TextField("Content Object Name:");
	        newContentObjectName.setInputPrompt("Please input content object name");
	        newContentObjectName.setWidth("430px");
	        newContentObjectName.setRequired(true);
	        newContentObjectName.setRequiredError("Content object name is required.");
	        final AbstractValidator validator = new AbstractValidator("Content object < {0} > already exists.") {

	            public boolean isValid(Object value) {
	                boolean checkResult = !checkNewItemAlreadyExistInContentSpaceDataContainer(parentItemId, value.toString());
	                return checkResult;
	            }
	        };
	        newContentObjectName.addValidator(validator);
	        newContentObjectForm.addField("_SubContentObjectName", newContentObjectName);

	        List<Button> controlButtonList = new ArrayList<Button>();

	        Button addBooleanPropertyButton = new Button("Boolean");
	        addBooleanPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addBooleanPropertyButton);
	        Button addDoublePropertyButton = new Button("Double");
	        addDoublePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDoublePropertyButton);
	        Button addLongPropertyButton = new Button("Long");
	        addLongPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addLongPropertyButton);
	        Button addDecimalPropertyButton = new Button("Decimal");
	        addDecimalPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDecimalPropertyButton);
	        Button addBinaryPropertyButton = new Button("Binary");
	        addBinaryPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addBinaryPropertyButton);
	        Button addDatePropertyButton = new Button("Date");
	        addDatePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDatePropertyButton);
	        Button addStringPropertyButton = new Button("String");
	        addStringPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addStringPropertyButton);

	        BaseButtonBar propertyControlButtonBar = new BaseButtonBar(280, 30, Alignment.MIDDLE_LEFT, controlButtonList);
	        HorizontalLayout addPropertyControlLayout = new HorizontalLayout();
	        Label addPropertyLabel = new Label("Add Property : ");
	        addPropertyControlLayout.addComponent(addPropertyLabel);
	        addPropertyControlLayout.addComponent(propertyControlButtonBar);
	        addPropertyControlLayout.setComponentAlignment(addPropertyLabel, Alignment.MIDDLE_LEFT);
	        addPropertyControlLayout.setComponentAlignment(propertyControlButtonBar, Alignment.MIDDLE_LEFT);
	        newContentObjectForm.getLayout().addComponent(addPropertyControlLayout);
	        final VerticalLayout prpoertyDataContainer = new VerticalLayout();
	        prpoertyDataContainer.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	        prpoertyDataContainer.setSizeFull();
	        newContentObjectForm.getLayout().addComponent(prpoertyDataContainer);
	        addBooleanPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectBooleanTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
	                    addContentObjectBooleanTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectBooleanTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>BOOLEAN</span> type property</b>", windowContent);
	                final List<NativeSelect> muitlPropertyValueList = new ArrayList<NativeSelect>();
	                final BaseForm newBooleanPropertyForm = new BaseForm();
	                newBooleanPropertyForm.setImmediate(true);
	                final TextField newBooleanPropertyName = new TextField("Property Name:");
	                newBooleanPropertyName.setInputPrompt("Please input property name");
	                newBooleanPropertyName.setWidth("320px");
	                newBooleanPropertyName.setRequired(true);
	                newBooleanPropertyName.setRequiredError("Property name is required.");
	                newBooleanPropertyForm.addField("_ADDNEW_BOOLEAN_PROPERTY", newBooleanPropertyName);

	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newBooleanPropertyName.addValidator(validator);

	                final NativeSelect booleanValueSelect = new NativeSelect("Property Value:");
	                booleanValueSelect.addItem(true);
	                booleanValueSelect.addItem(false);
	                booleanValueSelect.setRequired(true);
	                booleanValueSelect.setRequiredError("Property value is required.");
	                newBooleanPropertyForm.addField("_ADDNEW_BOOLEAN_PROPERTY_Value", booleanValueSelect);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("200px");
	                        final NativeSelect booleanValueSelect = new NativeSelect();
	                        booleanValueSelect.addItem(true);
	                        booleanValueSelect.addItem(false);
	                        booleanValueSelect.setImmediate(true);
	                        booleanValueSelect.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newBooleanPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (NativeSelect select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newBooleanPropertyForm.setValidationVisible(false);
	                                        newBooleanPropertyForm.setComponentError(null);
	                                    } else {
	                                        newBooleanPropertyForm.setValidationVisible(true);
	                                        newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        booleanValueSelect.setRequired(true);
	                        booleanValueSelect.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(booleanValueSelect);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(booleanValueSelect);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newBooleanPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(booleanValueSelect);
	                                boolean validateResult = newBooleanPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (NativeSelect select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newBooleanPropertyForm.setValidationVisible(false);
	                                        newBooleanPropertyForm.setComponentError(null);
	                                    } else {
	                                        newBooleanPropertyForm.setValidationVisible(true);
	                                        newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newBooleanPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newBooleanPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newBooleanPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newBooleanPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (NativeSelect select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newBooleanPropertyForm.setValidationVisible(false);
	                                newBooleanPropertyForm.setComponentError(null);
	                            } else {
	                                newBooleanPropertyForm.setValidationVisible(true);
	                                newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newBooleanPropertyForm.setValidationVisible(false);
	                            ContentObjectProperty booleanTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            booleanTypeContentObjectProperty.setPropertyName(newBooleanPropertyName.getValue().toString());
	                            booleanTypeContentObjectProperty.setPropertyType(PropertyType.BOOLEAN);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                booleanTypeContentObjectProperty.setMultiple(true);
	                                boolean[] valueArray = new boolean[propertyValueNum];
	                                valueArray[0] = Boolean.getBoolean(booleanValueSelect.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(booleanValueSelect.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Boolean.parseBoolean(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                booleanTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                booleanTypeContentObjectProperty.setMultiple(false);
	                                booleanTypeContentObjectProperty.setPropertyValue(new Boolean(booleanValueSelect.getValue().toString()));
	                                propertyValueString = booleanValueSelect.getValue().toString();
	                            }
	                            contentObjectPropertyMap.put(newBooleanPropertyName.getValue().toString(), booleanTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Boolean]</span> " + newBooleanPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newBooleanPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newBooleanPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);   
	                            application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
	    	                    addContentObjectBooleanTypePropertyFormWindow=null;
	                        } else {
	                            newBooleanPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
		                    addContentObjectBooleanTypePropertyFormWindow=null;
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newBooleanPropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectBooleanTypePropertyFormWindow.addComponent(newBooleanPropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectBooleanTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectBooleanTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectBooleanTypePropertyFormWindow);
	            }
	        });

	        addDoublePropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDoubleTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
	                    addContentObjectDoubleTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDoubleTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DOUBLE</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newDoublePropertyForm = new BaseForm();
	                newDoublePropertyForm.setImmediate(true);
	                final TextField newDoublePropertyName = new TextField("Property Name:");
	                newDoublePropertyName.setInputPrompt("Please input property name");
	                newDoublePropertyName.setWidth("320px");
	                newDoublePropertyName.setRequired(true);
	                newDoublePropertyName.setRequiredError("Property name is required.");
	                newDoublePropertyForm.addField("_ADDNEW_DOUBLE_PROPERTY", newDoublePropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDoublePropertyName.addValidator(validator);

	                final TextField doubleValueInput = new TextField("Property Value:");
	                doubleValueInput.addValidator(new DoubleValidator("property value should be a double type number."));
	                newDoublePropertyName.setWidth("200px");
	                doubleValueInput.setRequired(true);
	                doubleValueInput.setRequiredError("Property value is required.");
	                newDoublePropertyForm.addField("_ADDNEW_DOUBLE_PROPERTY_Value", doubleValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField doubleValueInput = new TextField();
	                        newDoublePropertyName.setWidth("200px");
	                        doubleValueInput.setImmediate(true);
	                        doubleValueInput.setRequired(true);
	                        doubleValueInput.setRequiredError("Property value is required.");
	                        doubleValueInput.addValidator(new DoubleValidator("property value should be a double type number."));
	                        doubleValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newDoublePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a double type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDoublePropertyForm.setValidationVisible(false);
	                                        newDoublePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDoublePropertyForm.setValidationVisible(true);
	                                        newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        doubleValueInput.setRequired(true);
	                        doubleValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(doubleValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(doubleValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDoublePropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(doubleValueInput);
	                                boolean validateResult = newDoublePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a double type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDoublePropertyForm.setValidationVisible(false);
	                                        newDoublePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDoublePropertyForm.setValidationVisible(true);
	                                        newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDoublePropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDoublePropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDoublePropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDoublePropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDoublePropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a double type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDoublePropertyForm.setValidationVisible(false);
	                                newDoublePropertyForm.setComponentError(null);
	                            } else {
	                                newDoublePropertyForm.setValidationVisible(true);
	                                newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDoublePropertyForm.setValidationVisible(false);
	                            newDoublePropertyForm.setComponentError(null);
	                            ContentObjectProperty doubleTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            doubleTypeContentObjectProperty.setPropertyName(newDoublePropertyName.getValue().toString());
	                            doubleTypeContentObjectProperty.setPropertyType(PropertyType.DOUBLE);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                doubleTypeContentObjectProperty.setMultiple(true);
	                                double[] valueArray = new double[propertyValueNum];
	                                valueArray[0] = Double.parseDouble(doubleValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(doubleValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Double.parseDouble(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                doubleTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                doubleTypeContentObjectProperty.setMultiple(false);
	                                doubleTypeContentObjectProperty.setPropertyValue(new Double(doubleValueInput.getValue().toString()));
	                                propertyValueString = doubleValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newDoublePropertyName.getValue().toString(), doubleTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Double]</span> " + newDoublePropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDoublePropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDoublePropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                     
	                            application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
	    	                    addContentObjectDoubleTypePropertyFormWindow=null;
	                        } else {
	                            newDoublePropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
		                    addContentObjectDoubleTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectDoubleTypePropertyFormWindow.addComponent(newDoublePropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectDoubleTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDoubleTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDoubleTypePropertyFormWindow);
	            }
	        });

	        addLongPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectLongTypePropertyFormWindow != null) {	                    
	                    application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
	                    addContentObjectLongTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectLongTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>LONG</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newLongPropertyForm = new BaseForm();
	                newLongPropertyForm.setImmediate(true);
	                final TextField newLongPropertyName = new TextField("Property Name:");
	                newLongPropertyName.setInputPrompt("Please input property name");
	                newLongPropertyName.setWidth("320px");
	                newLongPropertyName.setRequired(true);
	                newLongPropertyName.setRequiredError("Property name is required.");
	                newLongPropertyForm.addField("_ADDNEW_LONG_PROPERTY", newLongPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newLongPropertyName.addValidator(validator);

	                final TextField longValueInput = new TextField("Property Value:");
	                longValueInput.addValidator(new IntegerValidator("property value should be a long type number."));
	                newLongPropertyName.setWidth("200px");
	                longValueInput.setRequired(true);
	                longValueInput.setRequiredError("Property value is required.");
	                newLongPropertyForm.addField("_ADDNEW_LONG_PROPERTY_Value", longValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField longValueInput = new TextField();
	                        newLongPropertyName.setWidth("200px");
	                        longValueInput.setImmediate(true);
	                        longValueInput.setRequired(true);
	                        longValueInput.setRequiredError("Property value is required.");
	                        longValueInput.addValidator(new IntegerValidator("property value should be a long type number."));
	                        longValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newLongPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a long type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newLongPropertyForm.setValidationVisible(false);
	                                        newLongPropertyForm.setComponentError(null);
	                                    } else {
	                                        newLongPropertyForm.setValidationVisible(true);
	                                        newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        longValueInput.setRequired(true);
	                        longValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(longValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(longValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newLongPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(longValueInput);
	                                boolean validateResult = newLongPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a long type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newLongPropertyForm.setValidationVisible(false);
	                                        newLongPropertyForm.setComponentError(null);
	                                    } else {
	                                        newLongPropertyForm.setValidationVisible(true);
	                                        newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newLongPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newLongPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newLongPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newLongPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newLongPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a long type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newLongPropertyForm.setValidationVisible(false);
	                                newLongPropertyForm.setComponentError(null);
	                            } else {
	                                newLongPropertyForm.setValidationVisible(true);
	                                newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newLongPropertyForm.setValidationVisible(false);
	                            newLongPropertyForm.setComponentError(null);
	                            ContentObjectProperty longTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            longTypeContentObjectProperty.setPropertyName(newLongPropertyName.getValue().toString());
	                            longTypeContentObjectProperty.setPropertyType(PropertyType.LONG);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                longTypeContentObjectProperty.setMultiple(true);
	                                long[] valueArray = new long[propertyValueNum];
	                                valueArray[0] = Long.parseLong(longValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(longValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Long.parseLong(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                longTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                longTypeContentObjectProperty.setMultiple(false);
	                                longTypeContentObjectProperty.setPropertyValue(new Long(longValueInput.getValue().toString()));
	                                propertyValueString = longValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newLongPropertyName.getValue().toString(), longTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Long]</span> " + newLongPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newLongPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newLongPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
	    	                    addContentObjectLongTypePropertyFormWindow=null;
	                        } else {
	                            newLongPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
		                    addContentObjectLongTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectLongTypePropertyFormWindow.addComponent(newLongPropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectLongTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectLongTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectLongTypePropertyFormWindow);
	            }
	        });

	        addStringPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectStringTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
	                    addContentObjectStringTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectStringTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>STRING</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newStringPropertyForm = new BaseForm();
	                newStringPropertyForm.setImmediate(true);
	                final TextField newStringPropertyName = new TextField("Property Name:");
	                newStringPropertyName.setInputPrompt("Please input property name");
	                newStringPropertyName.setWidth("320px");
	                newStringPropertyName.setRequired(true);
	                newStringPropertyName.setRequiredError("Property name is required.");
	                newStringPropertyForm.addField("_ADDNEW_STRING_PROPERTY", newStringPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newStringPropertyName.addValidator(validator);

	                final TextField stringValueInput = new TextField("Property Value:");
	                newStringPropertyName.setWidth("200px");
	                stringValueInput.setRequired(true);
	                stringValueInput.setRequiredError("Property value is required.");
	                newStringPropertyForm.addField("_ADDNEW_STRING_PROPERTY_Value", stringValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField stringValueInput = new TextField();
	                        newStringPropertyName.setWidth("200px");
	                        stringValueInput.setImmediate(true);
	                        stringValueInput.setRequired(true);
	                        stringValueInput.setRequiredError("Property value is required.");
	                        stringValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newStringPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newStringPropertyForm.setValidationVisible(false);
	                                        newStringPropertyForm.setComponentError(null);
	                                    } else {
	                                        newStringPropertyForm.setValidationVisible(true);
	                                        newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        stringValueInput.setRequired(true);
	                        stringValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(stringValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(stringValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newStringPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(stringValueInput);
	                                boolean validateResult = newStringPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newStringPropertyForm.setValidationVisible(false);
	                                        newStringPropertyForm.setComponentError(null);
	                                    } else {
	                                        newStringPropertyForm.setValidationVisible(true);
	                                        newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newStringPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newStringPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newStringPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newStringPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newStringPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newStringPropertyForm.setValidationVisible(false);
	                                newStringPropertyForm.setComponentError(null);
	                            } else {
	                                newStringPropertyForm.setValidationVisible(true);
	                                newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newStringPropertyForm.setValidationVisible(false);
	                            newStringPropertyForm.setComponentError(null);
	                            ContentObjectProperty stringTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            stringTypeContentObjectProperty.setPropertyName(newStringPropertyName.getValue().toString());
	                            stringTypeContentObjectProperty.setPropertyType(PropertyType.STRING);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                stringTypeContentObjectProperty.setMultiple(true);
	                                String[] valueArray = new String[propertyValueNum];
	                                valueArray[0] = stringValueInput.getValue().toString();
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(stringValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = muitlPropertyValueList.get(i).getValue().toString();
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                stringTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                stringTypeContentObjectProperty.setMultiple(false);
	                                stringTypeContentObjectProperty.setPropertyValue(stringValueInput.getValue().toString());
	                                propertyValueString = stringValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newStringPropertyName.getValue().toString(), stringTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[String]</span> " + newStringPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newStringPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newStringPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
	    	                    addContentObjectStringTypePropertyFormWindow=null;	                            
	                        } else {
	                            newStringPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
		                    addContentObjectStringTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectStringTypePropertyFormWindow.addComponent(newStringPropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectStringTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectStringTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectStringTypePropertyFormWindow);
	            }
	        });

	        addDatePropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDateTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
	                    addContentObjectDateTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDateTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DATE</span> type property</b>", windowContent);
	                final List<PopupDateField> muitlPropertyValueList = new ArrayList<PopupDateField>();
	                final BaseForm newDatePropertyForm = new BaseForm();
	                newDatePropertyForm.setImmediate(true);
	                final TextField newDatePropertyName = new TextField("Property Name:");
	                newDatePropertyName.setInputPrompt("Please input property name");
	                newDatePropertyName.setWidth("320px");
	                newDatePropertyName.setRequired(true);
	                newDatePropertyName.setRequiredError("Property name is required.");
	                newDatePropertyForm.addField("_ADDNEW_DATE_PROPERTY", newDatePropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDatePropertyName.addValidator(validator);

	                final PopupDateField dateSelect = new PopupDateField("Property Value:");
	                dateSelect.setWidth("140px");
	                dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
	                dateSelect.setRequired(true);
	                dateSelect.setRequiredError("Property value is required.");
	                newDatePropertyForm.addField("_ADDNEW_DATE_PROPERTY_Value", dateSelect);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final PopupDateField dateSelect = new PopupDateField();
	                        dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
	                        dateSelect.setRequired(true);
	                        dateSelect.setImmediate(true);
	                        dateSelect.setRequiredError("Property value is required.");
	                        dateSelect.setWidth("140px");

	                        dateSelect.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {
	                                boolean validateResult = newDatePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (PopupDateField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDatePropertyForm.setValidationVisible(false);
	                                        newDatePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDatePropertyForm.setValidationVisible(true);
	                                        newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        newPropertyValueLayout.addComponent(dateSelect);

	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(dateSelect);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDatePropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(dateSelect);
	                                boolean validateResult = newDatePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (PopupDateField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDatePropertyForm.setValidationVisible(false);
	                                        newDatePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDatePropertyForm.setValidationVisible(true);
	                                        newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDatePropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDatePropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDatePropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDatePropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (PopupDateField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDatePropertyForm.setValidationVisible(false);
	                                newDatePropertyForm.setComponentError(null);
	                            } else {
	                                newDatePropertyForm.setValidationVisible(true);
	                                newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDatePropertyForm.setValidationVisible(false);
	                            ContentObjectProperty dateTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            dateTypeContentObjectProperty.setPropertyName(newDatePropertyName.getValue().toString());
	                            dateTypeContentObjectProperty.setPropertyType(PropertyType.DATE);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                dateTypeContentObjectProperty.setMultiple(true);
	                                Calendar[] valueArray = new Calendar[propertyValueNum];
	                                Calendar calDate = Calendar.getInstance();
	                                calDate.setTime((Date) dateSelect.getValue());
	                                valueArray[0] = calDate;
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(dateSelect.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    Calendar calDateAdd = Calendar.getInstance();
	                                    calDateAdd.setTime((Date) muitlPropertyValueList.get(i).getValue());
	                                    valueArray[i + 1] = calDateAdd;
	                                    sb.append("," + ((Date) muitlPropertyValueList.get(i).getValue()).toString());
	                                }
	                                dateTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                dateTypeContentObjectProperty.setMultiple(false);
	                                Calendar calDate = Calendar.getInstance();
	                                calDate.setTime((Date) dateSelect.getValue());
	                                dateTypeContentObjectProperty.setPropertyValue(calDate);
	                                propertyValueString = ((Date) dateSelect.getValue()).toString();
	                            }
	                            contentObjectPropertyMap.put(newDatePropertyName.getValue().toString(), dateTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Date]</span> " + newDatePropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDatePropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDatePropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);
	                            application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
	    	                    addContentObjectDateTypePropertyFormWindow=null;	                            
	                        } else {
	                            newDatePropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                    	application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
		                    addContentObjectDateTypePropertyFormWindow=null;
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDatePropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectDateTypePropertyFormWindow.addComponent(newDatePropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectDateTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDateTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDateTypePropertyFormWindow);
	            }
	        });

	        addDecimalPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDecimalTypePropertyFormWindow != null) {
	                	application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
	                	addContentObjectDecimalTypePropertyFormWindow=null;	                   
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDecimalTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DECIMAL</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newDecimalPropertyForm = new BaseForm();
	                newDecimalPropertyForm.setImmediate(true);
	                final TextField newDecimalPropertyName = new TextField("Property Name:");
	                newDecimalPropertyName.setInputPrompt("Please input property name");
	                newDecimalPropertyName.setWidth("320px");
	                newDecimalPropertyName.setRequired(true);
	                newDecimalPropertyName.setRequiredError("Property name is required.");
	                newDecimalPropertyForm.addField("_ADDNEW_DECIMAL_PROPERTY", newDecimalPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDecimalPropertyName.addValidator(validator);

	                final TextField decimalValueInput = new TextField("Property Value:");
	                decimalValueInput.addValidator(new DoubleValidator("property value should be a decimal type number."));
	                newDecimalPropertyName.setWidth("200px");
	                decimalValueInput.setRequired(true);
	                decimalValueInput.setRequiredError("Property value is required.");
	                newDecimalPropertyForm.addField("_ADDNEW_DECIMAL_PROPERTY_Value", decimalValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField decimalValueInput = new TextField();
	                        newDecimalPropertyName.setWidth("200px");
	                        decimalValueInput.setImmediate(true);
	                        decimalValueInput.setRequired(true);
	                        decimalValueInput.setRequiredError("Property value is required.");
	                        decimalValueInput.addValidator(new DoubleValidator("property value should be a decimal type number."));
	                        decimalValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newDecimalPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a decimal type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDecimalPropertyForm.setValidationVisible(false);
	                                        newDecimalPropertyForm.setComponentError(null);
	                                    } else {
	                                        newDecimalPropertyForm.setValidationVisible(true);
	                                        newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        decimalValueInput.setRequired(true);
	                        decimalValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(decimalValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(decimalValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDecimalPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(decimalValueInput);
	                                boolean validateResult = newDecimalPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a decimal type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDecimalPropertyForm.setValidationVisible(false);
	                                        newDecimalPropertyForm.setComponentError(null);
	                                    } else {
	                                        newDecimalPropertyForm.setValidationVisible(true);
	                                        newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDecimalPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDecimalPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDecimalPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDecimalPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDecimalPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a decimal type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDecimalPropertyForm.setValidationVisible(false);
	                                newDecimalPropertyForm.setComponentError(null);
	                            } else {
	                                newDecimalPropertyForm.setValidationVisible(true);
	                                newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDecimalPropertyForm.setValidationVisible(false);
	                            newDecimalPropertyForm.setComponentError(null);
	                            ContentObjectProperty decimalTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            decimalTypeContentObjectProperty.setPropertyName(newDecimalPropertyName.getValue().toString());
	                            decimalTypeContentObjectProperty.setPropertyType(PropertyType.DECIMAL);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                decimalTypeContentObjectProperty.setMultiple(true);
	                                BigDecimal[] valueArray = new BigDecimal[propertyValueNum];
	                                valueArray[0] = new BigDecimal(decimalValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(decimalValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = new BigDecimal(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                decimalTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                decimalTypeContentObjectProperty.setMultiple(false);
	                                decimalTypeContentObjectProperty.setPropertyValue(new BigDecimal(decimalValueInput.getValue().toString()));
	                                propertyValueString = decimalValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newDecimalPropertyName.getValue().toString(), decimalTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Decimal]</span> " + newDecimalPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDecimalPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDecimalPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
	                            addContentObjectDecimalTypePropertyFormWindow=null;
	                        } else {
	                            newDecimalPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                        
	                        application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
		                	addContentObjectDecimalTypePropertyFormWindow=null;	  
	                    }
	                });
	                addContentObjectDecimalTypePropertyFormWindow.addComponent(newDecimalPropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectDecimalTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDecimalTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDecimalTypePropertyFormWindow);
	            }
	        });

	        addBinaryPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectBinaryTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
	                    addContentObjectBinaryTypePropertyFormWindow=null;	  
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectBinaryTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>BINARY</span> type property</b>", windowContent);
	                final List<String> muitlPropertyValueList = new ArrayList<String>();
	                final BaseForm newBinaryPropertyForm = new BaseForm();

	                VerticalLayout uploaderContainerLayout = new VerticalLayout();
	                uploaderContainerLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	                newBinaryPropertyForm.setLayout(uploaderContainerLayout);
	                newBinaryPropertyForm.setImmediate(true);
	                final TextField newBinaryPropertyName = new TextField("Property Name:");
	                newBinaryPropertyName.setInputPrompt("Please input property name");
	                newBinaryPropertyName.setWidth("320px");
	                newBinaryPropertyName.setRequired(true);
	                newBinaryPropertyName.setRequiredError("Property name is required.");
	                newBinaryPropertyForm.addField("_ADDNEW_BINARY_PROPERTY", newBinaryPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newBinaryPropertyName.addValidator(validator);

	                VerticalLayout newBinaryPropertyValueLayout = new VerticalLayout();
	                newBinaryPropertyValueLayout.setWidth("500px");
	                newBinaryPropertyValueLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	                newBinaryPropertyForm.getLayout().addComponent(new BinaryPropertyFileProgressMonitoringUploader(newBinaryPropertyValueLayout, muitlPropertyValueList, newBinaryPropertyForm));
	                newBinaryPropertyForm.getLayout().addComponent(new Label("Added files:"));
	                newBinaryPropertyForm.getLayout().addComponent(newBinaryPropertyValueLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newBinaryPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newBinaryPropertyForm.isValid();
	                        String errorMessage = "";
	                        if (validateResult) {
	                            if (muitlPropertyValueList.size() == 0) {
	                                errorMessage = "Please upload at least one binary file.";
	                                newBinaryPropertyForm.setValidationVisible(true);
	                                newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
	                                validateResult = false;
	                            } else {
	                                newBinaryPropertyForm.setValidationVisible(false);
	                                newBinaryPropertyForm.setComponentError(null);
	                            }
	                        }

	                        if (validateResult) {
	                            ContentObjectProperty binaryTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            binaryTypeContentObjectProperty.setPropertyName(newBinaryPropertyName.getValue().toString());
	                            binaryTypeContentObjectProperty.setPropertyType(PropertyType.BINARY);
	                            int propertyValueNum = muitlPropertyValueList.size();
	                            String propertyValueString = muitlPropertyValueList.get(0);
	                            File propertyFile = null;
	                            if (propertyValueNum > 1) {
	                                binaryTypeContentObjectProperty.setMultiple(true);
	                                Binary[] valueArray = new Binary[propertyValueNum];
	                                StringBuffer sb = new StringBuffer();
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    try {
	                                        propertyFile = new File(tempFileDir + muitlPropertyValueList.get(i));
	                                        valueArray[i] = ValueFactoryImpl.getInstance().createBinary(new FileInputStream(propertyFile));
	                                        propertyFile.delete();//need comfirm if this works
	                                    } catch (FileNotFoundException ex) {
	                                        Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                    } catch (RepositoryException ex) {
	                                        Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                    }
	                                    if (i != 0) {
	                                        sb.append("," + muitlPropertyValueList.get(i));
	                                    }
	                                }
	                                binaryTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = propertyValueString + sb.toString();
	                            } else {
	                                binaryTypeContentObjectProperty.setMultiple(false);
	                                try {
	                                    propertyFile = new File(tempFileDir + muitlPropertyValueList.get(0));
	                                    binaryTypeContentObjectProperty.setPropertyValue(ValueFactoryImpl.getInstance().createBinary(new FileInputStream(propertyFile)));
	                                    propertyFile.delete();//need comfirm if this works
	                                } catch (FileNotFoundException ex) {
	                                    Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                } catch (RepositoryException ex) {
	                                    Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                }
	                            }
	                            contentObjectPropertyMap.put(newBinaryPropertyName.getValue().toString(), binaryTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Date]</span> " + newBinaryPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newBinaryPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newBinaryPropertyName.getValue().toString());
	                                    for (String fileName : muitlPropertyValueList) {
	                                        new File(tempFileDir + fileName).delete();
	                                    }
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
	    	                    addContentObjectBinaryTypePropertyFormWindow=null;	 
	                        } else {
	                            newBinaryPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                        
	                        application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
		                    addContentObjectBinaryTypePropertyFormWindow=null;	 
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newBinaryPropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectBinaryTypePropertyFormWindow.addComponent(newBinaryPropertyForm);
	                int postion_x = addContentObjectWindow.getPositionX() + 10;
	                int postion_y = addContentObjectWindow.getPositionX() + 130;
	                addContentObjectBinaryTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectBinaryTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectBinaryTypePropertyFormWindow);
	            }
	        });

	        final Button okButton = new Button("Confirm add action");
	        final Button cancelAddbutton = new Button("Cancel add action", new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {               
	                
	                application.getMainWindow().removeWindow(addContentObjectWindow);
	                addContentObjectWindow=null;	 
	            }
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	        okButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                newContentObjectForm.setValidationVisible(true);
	                boolean validateResult = newContentObjectForm.isValid();
	                Label messageLable = new Label("<b style='color:#333333;'>Adding Content Object...</b>", Label.CONTENT_XHTML);
	                messageLable.setHeight("24px");
	                if (validateResult) {
	                    okButton.setEnabled(false);
	                    newContentObjectForm.setValidationVisible(false);
	                    newContentObjectName.setEnabled(false);
	                    newContentObjectName.removeValidator(validator);
	                    newContentObjectForm.getLayout().removeComponent(newContentObjectName);
	                    newContentObjectForm.getLayout().addComponent(messageLable);
	                    cancelAddbutton.setCaption("Close window");
	                    String subContentObjectName = newContentObjectName.getValue().toString();
	                    AddSubContentObjectResult addrelu = null;
	                    if (!contentObjectPropertyMap.isEmpty()) {
	                        List<ContentObjectProperty> contentObjectList = new ArrayList<ContentObjectProperty>(contentObjectPropertyMap.values());
	                        addrelu = ContentRepositoryDataUtil.addSubContentObject(contentSpaceName.toString(), parentNodeABSPath, subContentObjectName, contentObjectList);
	                    } else {
	                        addrelu = ContentRepositoryDataUtil.addSubContentObject(contentSpaceName.toString(), parentNodeABSPath, subContentObjectName, null);
	                    }
	                    String subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + currentChildNum;
	                    Item subContentObjectItem;
	                    switch (addrelu) {
	                        case AddSubContentObjectSuccessful:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>Add content object success.</b>", Label.CONTENT_XHTML));
	                            subContentObjectItem = contentSpaceDataContainer.addItem(subContentObjectID);
	                            subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(subContentObjectName);
	                            subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                            contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                            contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                            //REFRESH CONTENT DETAIL
	                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
	                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                            }  
	                            break;
	                        case ContentObjectAlreadyExists:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>Content object already exists in content repository.</b>", Label.CONTENT_XHTML));
	                            subContentObjectItem = contentSpaceDataContainer.addItem(subContentObjectID);
	                            subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(subContentObjectName);
	                            subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                            contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                            String newNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, subContentObjectID);
	                            long childrenNumber = ContentRepositoryDataUtil.getSubComponentChildrenNumber(contentSpaceName.toString(), newNodeABSPath);
	                            if (childrenNumber > 0) {
	                                contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, true);
	                            } else {
	                                contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                            }
	                            //REFRESH CONTENT DETAIL	                          
	                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
	                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                            } 
	                            break;
	                        case GetRepositoryErrorDuringOperation:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>Get content repository error during add operation,please contact system administrator.</b>", Label.CONTENT_XHTML));
	                            break;
	                    }
	                }
	            }
	        });
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(260, 45, Alignment.MIDDLE_CENTER, buttonList);
	        newContentObjectForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newContentObjectForm);
	        return formContainer;
	    }	
	
	 public  DialogWindow createAddPropertyFormWindow(Object targetItem) {
		 Object contentObjectName = this.contentSpaceDataContainer.getItem(targetItem).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();	        
		 String windowTitle = "Add Content Object Properties";
	     String windowDesc = "Add new properties for content object " + CommonStyleUtil.formatCurrentItemStyle(contentObjectName.toString());
	     addPropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, createAddPropertyForm(targetItem));
	     return addPropertyWindow;		
	 }
	 
	 private Layout createAddPropertyForm(Object actionItemID) {
	        final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();
	        final String parentNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, actionItemID);
	        final VerticalLayout formContainer = new VerticalLayout();
	        final BaseForm newContentObjectForm = new BaseForm();
	        final Map<String, ContentObjectProperty> contentObjectPropertyMap = new HashMap<String, ContentObjectProperty>();
	        VerticalLayout formAdvLayout = new VerticalLayout();
	        formAdvLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	        newContentObjectForm.setLayout(formAdvLayout);
	        
	        final Object parentItemId = actionItemID;  	        
	        newContentObjectForm.setImmediate(true);
	        
	        List<Button> controlButtonList = new ArrayList<Button>();

	        Button addBooleanPropertyButton = new Button("Boolean");
	        addBooleanPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addBooleanPropertyButton);
	        Button addDoublePropertyButton = new Button("Double");
	        addDoublePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDoublePropertyButton);
	        Button addLongPropertyButton = new Button("Long");
	        addLongPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addLongPropertyButton);
	        Button addDecimalPropertyButton = new Button("Decimal");
	        addDecimalPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDecimalPropertyButton);
	        Button addBinaryPropertyButton = new Button("Binary");
	        addBinaryPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addBinaryPropertyButton);
	        Button addDatePropertyButton = new Button("Date");
	        addDatePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addDatePropertyButton);
	        Button addStringPropertyButton = new Button("String");
	        addStringPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	        controlButtonList.add(addStringPropertyButton);

	        BaseButtonBar propertyControlButtonBar = new BaseButtonBar(280, 30, Alignment.MIDDLE_LEFT, controlButtonList);
	        HorizontalLayout addPropertyControlLayout = new HorizontalLayout();
	        Label addPropertyLabel = new Label("Add Property : ");
	        addPropertyControlLayout.addComponent(addPropertyLabel);
	        addPropertyControlLayout.addComponent(propertyControlButtonBar);
	        addPropertyControlLayout.setComponentAlignment(addPropertyLabel, Alignment.MIDDLE_LEFT);
	        addPropertyControlLayout.setComponentAlignment(propertyControlButtonBar, Alignment.MIDDLE_LEFT);
	        newContentObjectForm.getLayout().addComponent(addPropertyControlLayout);
	        final VerticalLayout prpoertyDataContainer = new VerticalLayout();
	        prpoertyDataContainer.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	        prpoertyDataContainer.setSizeFull();
	        newContentObjectForm.getLayout().addComponent(prpoertyDataContainer);
	        addBooleanPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectBooleanTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
	                    addContentObjectBooleanTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectBooleanTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>BOOLEAN</span> type property</b>", windowContent);
	                final List<NativeSelect> muitlPropertyValueList = new ArrayList<NativeSelect>();
	                final BaseForm newBooleanPropertyForm = new BaseForm();
	                newBooleanPropertyForm.setImmediate(true);
	                final TextField newBooleanPropertyName = new TextField("Property Name:");
	                newBooleanPropertyName.setInputPrompt("Please input property name");
	                newBooleanPropertyName.setWidth("320px");
	                newBooleanPropertyName.setRequired(true);
	                newBooleanPropertyName.setRequiredError("Property name is required.");
	                newBooleanPropertyForm.addField("_ADDNEW_BOOLEAN_PROPERTY", newBooleanPropertyName);

	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newBooleanPropertyName.addValidator(validator);

	                final NativeSelect booleanValueSelect = new NativeSelect("Property Value:");
	                booleanValueSelect.addItem(true);
	                booleanValueSelect.addItem(false);
	                booleanValueSelect.setRequired(true);
	                booleanValueSelect.setRequiredError("Property value is required.");
	                newBooleanPropertyForm.addField("_ADDNEW_BOOLEAN_PROPERTY_Value", booleanValueSelect);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("200px");
	                        final NativeSelect booleanValueSelect = new NativeSelect();
	                        booleanValueSelect.addItem(true);
	                        booleanValueSelect.addItem(false);
	                        booleanValueSelect.setImmediate(true);
	                        booleanValueSelect.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newBooleanPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (NativeSelect select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newBooleanPropertyForm.setValidationVisible(false);
	                                        newBooleanPropertyForm.setComponentError(null);
	                                    } else {
	                                        newBooleanPropertyForm.setValidationVisible(true);
	                                        newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        booleanValueSelect.setRequired(true);
	                        booleanValueSelect.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(booleanValueSelect);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(booleanValueSelect);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newBooleanPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(booleanValueSelect);
	                                boolean validateResult = newBooleanPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (NativeSelect select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newBooleanPropertyForm.setValidationVisible(false);
	                                        newBooleanPropertyForm.setComponentError(null);
	                                    } else {
	                                        newBooleanPropertyForm.setValidationVisible(true);
	                                        newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newBooleanPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newBooleanPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newBooleanPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newBooleanPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (NativeSelect select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newBooleanPropertyForm.setValidationVisible(false);
	                                newBooleanPropertyForm.setComponentError(null);
	                            } else {
	                                newBooleanPropertyForm.setValidationVisible(true);
	                                newBooleanPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newBooleanPropertyForm.setValidationVisible(false);
	                            ContentObjectProperty booleanTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            booleanTypeContentObjectProperty.setPropertyName(newBooleanPropertyName.getValue().toString());
	                            booleanTypeContentObjectProperty.setPropertyType(PropertyType.BOOLEAN);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                booleanTypeContentObjectProperty.setMultiple(true);
	                                boolean[] valueArray = new boolean[propertyValueNum];
	                                valueArray[0] = Boolean.getBoolean(booleanValueSelect.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(booleanValueSelect.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Boolean.parseBoolean(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                booleanTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                booleanTypeContentObjectProperty.setMultiple(false);
	                                booleanTypeContentObjectProperty.setPropertyValue(new Boolean(booleanValueSelect.getValue().toString()));
	                                propertyValueString = booleanValueSelect.getValue().toString();
	                            }
	                            contentObjectPropertyMap.put(newBooleanPropertyName.getValue().toString(), booleanTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Boolean]</span> " + newBooleanPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newBooleanPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newBooleanPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);   
	                            application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
	    	                    addContentObjectBooleanTypePropertyFormWindow=null;
	                        } else {
	                            newBooleanPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectBooleanTypePropertyFormWindow);
		                    addContentObjectBooleanTypePropertyFormWindow=null;
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newBooleanPropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectBooleanTypePropertyFormWindow.addComponent(newBooleanPropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectBooleanTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectBooleanTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectBooleanTypePropertyFormWindow);
	            }
	        });

	        addDoublePropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDoubleTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
	                    addContentObjectDoubleTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDoubleTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DOUBLE</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newDoublePropertyForm = new BaseForm();
	                newDoublePropertyForm.setImmediate(true);
	                final TextField newDoublePropertyName = new TextField("Property Name:");
	                newDoublePropertyName.setInputPrompt("Please input property name");
	                newDoublePropertyName.setWidth("320px");
	                newDoublePropertyName.setRequired(true);
	                newDoublePropertyName.setRequiredError("Property name is required.");
	                newDoublePropertyForm.addField("_ADDNEW_DOUBLE_PROPERTY", newDoublePropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDoublePropertyName.addValidator(validator);

	                final TextField doubleValueInput = new TextField("Property Value:");
	                doubleValueInput.addValidator(new DoubleValidator("property value should be a double type number."));
	                newDoublePropertyName.setWidth("200px");
	                doubleValueInput.setRequired(true);
	                doubleValueInput.setRequiredError("Property value is required.");
	                newDoublePropertyForm.addField("_ADDNEW_DOUBLE_PROPERTY_Value", doubleValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField doubleValueInput = new TextField();
	                        newDoublePropertyName.setWidth("200px");
	                        doubleValueInput.setImmediate(true);
	                        doubleValueInput.setRequired(true);
	                        doubleValueInput.setRequiredError("Property value is required.");
	                        doubleValueInput.addValidator(new DoubleValidator("property value should be a double type number."));
	                        doubleValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newDoublePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a double type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDoublePropertyForm.setValidationVisible(false);
	                                        newDoublePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDoublePropertyForm.setValidationVisible(true);
	                                        newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        doubleValueInput.setRequired(true);
	                        doubleValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(doubleValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(doubleValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDoublePropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(doubleValueInput);
	                                boolean validateResult = newDoublePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a double type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDoublePropertyForm.setValidationVisible(false);
	                                        newDoublePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDoublePropertyForm.setValidationVisible(true);
	                                        newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDoublePropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDoublePropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDoublePropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDoublePropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDoublePropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a double type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDoublePropertyForm.setValidationVisible(false);
	                                newDoublePropertyForm.setComponentError(null);
	                            } else {
	                                newDoublePropertyForm.setValidationVisible(true);
	                                newDoublePropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDoublePropertyForm.setValidationVisible(false);
	                            newDoublePropertyForm.setComponentError(null);
	                            ContentObjectProperty doubleTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            doubleTypeContentObjectProperty.setPropertyName(newDoublePropertyName.getValue().toString());
	                            doubleTypeContentObjectProperty.setPropertyType(PropertyType.DOUBLE);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                doubleTypeContentObjectProperty.setMultiple(true);
	                                double[] valueArray = new double[propertyValueNum];
	                                valueArray[0] = Double.parseDouble(doubleValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(doubleValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Double.parseDouble(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                doubleTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                doubleTypeContentObjectProperty.setMultiple(false);
	                                doubleTypeContentObjectProperty.setPropertyValue(new Double(doubleValueInput.getValue().toString()));
	                                propertyValueString = doubleValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newDoublePropertyName.getValue().toString(), doubleTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Double]</span> " + newDoublePropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDoublePropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDoublePropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                     
	                            application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
	    	                    addContentObjectDoubleTypePropertyFormWindow=null;
	                        } else {
	                            newDoublePropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectDoubleTypePropertyFormWindow);
		                    addContentObjectDoubleTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectDoubleTypePropertyFormWindow.addComponent(newDoublePropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectDoubleTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDoubleTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDoubleTypePropertyFormWindow);
	            }
	        });

	        addLongPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectLongTypePropertyFormWindow != null) {	                    
	                    application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
	                    addContentObjectLongTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectLongTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>LONG</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newLongPropertyForm = new BaseForm();
	                newLongPropertyForm.setImmediate(true);
	                final TextField newLongPropertyName = new TextField("Property Name:");
	                newLongPropertyName.setInputPrompt("Please input property name");
	                newLongPropertyName.setWidth("320px");
	                newLongPropertyName.setRequired(true);
	                newLongPropertyName.setRequiredError("Property name is required.");
	                newLongPropertyForm.addField("_ADDNEW_LONG_PROPERTY", newLongPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newLongPropertyName.addValidator(validator);

	                final TextField longValueInput = new TextField("Property Value:");
	                longValueInput.addValidator(new IntegerValidator("property value should be a long type number."));
	                newLongPropertyName.setWidth("200px");
	                longValueInput.setRequired(true);
	                longValueInput.setRequiredError("Property value is required.");
	                newLongPropertyForm.addField("_ADDNEW_LONG_PROPERTY_Value", longValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField longValueInput = new TextField();
	                        newLongPropertyName.setWidth("200px");
	                        longValueInput.setImmediate(true);
	                        longValueInput.setRequired(true);
	                        longValueInput.setRequiredError("Property value is required.");
	                        longValueInput.addValidator(new IntegerValidator("property value should be a long type number."));
	                        longValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newLongPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a long type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newLongPropertyForm.setValidationVisible(false);
	                                        newLongPropertyForm.setComponentError(null);
	                                    } else {
	                                        newLongPropertyForm.setValidationVisible(true);
	                                        newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        longValueInput.setRequired(true);
	                        longValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(longValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(longValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newLongPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(longValueInput);
	                                boolean validateResult = newLongPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a long type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newLongPropertyForm.setValidationVisible(false);
	                                        newLongPropertyForm.setComponentError(null);
	                                    } else {
	                                        newLongPropertyForm.setValidationVisible(true);
	                                        newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newLongPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newLongPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newLongPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newLongPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newLongPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a long type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newLongPropertyForm.setValidationVisible(false);
	                                newLongPropertyForm.setComponentError(null);
	                            } else {
	                                newLongPropertyForm.setValidationVisible(true);
	                                newLongPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newLongPropertyForm.setValidationVisible(false);
	                            newLongPropertyForm.setComponentError(null);
	                            ContentObjectProperty longTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            longTypeContentObjectProperty.setPropertyName(newLongPropertyName.getValue().toString());
	                            longTypeContentObjectProperty.setPropertyType(PropertyType.LONG);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                longTypeContentObjectProperty.setMultiple(true);
	                                long[] valueArray = new long[propertyValueNum];
	                                valueArray[0] = Long.parseLong(longValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(longValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = Long.parseLong(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                longTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                longTypeContentObjectProperty.setMultiple(false);
	                                longTypeContentObjectProperty.setPropertyValue(new Long(longValueInput.getValue().toString()));
	                                propertyValueString = longValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newLongPropertyName.getValue().toString(), longTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Long]</span> " + newLongPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newLongPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newLongPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
	    	                    addContentObjectLongTypePropertyFormWindow=null;
	                        } else {
	                            newLongPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                       
	                        application.getMainWindow().removeWindow(addContentObjectLongTypePropertyFormWindow);
		                    addContentObjectLongTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectLongTypePropertyFormWindow.addComponent(newLongPropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectLongTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectLongTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectLongTypePropertyFormWindow);
	            }
	        });

	        addStringPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectStringTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
	                    addContentObjectStringTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectStringTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>STRING</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newStringPropertyForm = new BaseForm();
	                newStringPropertyForm.setImmediate(true);
	                final TextField newStringPropertyName = new TextField("Property Name:");
	                newStringPropertyName.setInputPrompt("Please input property name");
	                newStringPropertyName.setWidth("320px");
	                newStringPropertyName.setRequired(true);
	                newStringPropertyName.setRequiredError("Property name is required.");
	                newStringPropertyForm.addField("_ADDNEW_STRING_PROPERTY", newStringPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newStringPropertyName.addValidator(validator);

	                final TextField stringValueInput = new TextField("Property Value:");
	                newStringPropertyName.setWidth("200px");
	                stringValueInput.setRequired(true);
	                stringValueInput.setRequiredError("Property value is required.");
	                newStringPropertyForm.addField("_ADDNEW_STRING_PROPERTY_Value", stringValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField stringValueInput = new TextField();
	                        newStringPropertyName.setWidth("200px");
	                        stringValueInput.setImmediate(true);
	                        stringValueInput.setRequired(true);
	                        stringValueInput.setRequiredError("Property value is required.");
	                        stringValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newStringPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newStringPropertyForm.setValidationVisible(false);
	                                        newStringPropertyForm.setComponentError(null);
	                                    } else {
	                                        newStringPropertyForm.setValidationVisible(true);
	                                        newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        stringValueInput.setRequired(true);
	                        stringValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(stringValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(stringValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newStringPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(stringValueInput);
	                                boolean validateResult = newStringPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newStringPropertyForm.setValidationVisible(false);
	                                        newStringPropertyForm.setComponentError(null);
	                                    } else {
	                                        newStringPropertyForm.setValidationVisible(true);
	                                        newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newStringPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newStringPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newStringPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newStringPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newStringPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newStringPropertyForm.setValidationVisible(false);
	                                newStringPropertyForm.setComponentError(null);
	                            } else {
	                                newStringPropertyForm.setValidationVisible(true);
	                                newStringPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newStringPropertyForm.setValidationVisible(false);
	                            newStringPropertyForm.setComponentError(null);
	                            ContentObjectProperty stringTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            stringTypeContentObjectProperty.setPropertyName(newStringPropertyName.getValue().toString());
	                            stringTypeContentObjectProperty.setPropertyType(PropertyType.STRING);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                stringTypeContentObjectProperty.setMultiple(true);
	                                String[] valueArray = new String[propertyValueNum];
	                                valueArray[0] = stringValueInput.getValue().toString();
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(stringValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = muitlPropertyValueList.get(i).getValue().toString();
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                stringTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                stringTypeContentObjectProperty.setMultiple(false);
	                                stringTypeContentObjectProperty.setPropertyValue(stringValueInput.getValue().toString());
	                                propertyValueString = stringValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newStringPropertyName.getValue().toString(), stringTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[String]</span> " + newStringPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newStringPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newStringPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
	    	                    addContentObjectStringTypePropertyFormWindow=null;	                            
	                        } else {
	                            newStringPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        application.getMainWindow().removeWindow(addContentObjectStringTypePropertyFormWindow);
		                    addContentObjectStringTypePropertyFormWindow=null;
	                    }
	                });
	                addContentObjectStringTypePropertyFormWindow.addComponent(newStringPropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectStringTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectStringTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectStringTypePropertyFormWindow);
	            }
	        });

	        addDatePropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDateTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
	                    addContentObjectDateTypePropertyFormWindow=null;
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDateTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DATE</span> type property</b>", windowContent);
	                final List<PopupDateField> muitlPropertyValueList = new ArrayList<PopupDateField>();
	                final BaseForm newDatePropertyForm = new BaseForm();
	                newDatePropertyForm.setImmediate(true);
	                final TextField newDatePropertyName = new TextField("Property Name:");
	                newDatePropertyName.setInputPrompt("Please input property name");
	                newDatePropertyName.setWidth("320px");
	                newDatePropertyName.setRequired(true);
	                newDatePropertyName.setRequiredError("Property name is required.");
	                newDatePropertyForm.addField("_ADDNEW_DATE_PROPERTY", newDatePropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDatePropertyName.addValidator(validator);

	                final PopupDateField dateSelect = new PopupDateField("Property Value:");
	                dateSelect.setWidth("140px");
	                dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
	                dateSelect.setRequired(true);
	                dateSelect.setRequiredError("Property value is required.");
	                newDatePropertyForm.addField("_ADDNEW_DATE_PROPERTY_Value", dateSelect);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final PopupDateField dateSelect = new PopupDateField();
	                        dateSelect.setResolution(PopupDateField.RESOLUTION_MIN);
	                        dateSelect.setRequired(true);
	                        dateSelect.setImmediate(true);
	                        dateSelect.setRequiredError("Property value is required.");
	                        dateSelect.setWidth("140px");

	                        dateSelect.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {
	                                boolean validateResult = newDatePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (PopupDateField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDatePropertyForm.setValidationVisible(false);
	                                        newDatePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDatePropertyForm.setValidationVisible(true);
	                                        newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        newPropertyValueLayout.addComponent(dateSelect);

	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(dateSelect);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDatePropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(dateSelect);
	                                boolean validateResult = newDatePropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (PopupDateField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDatePropertyForm.setValidationVisible(false);
	                                        newDatePropertyForm.setComponentError(null);
	                                    } else {
	                                        newDatePropertyForm.setValidationVisible(true);
	                                        newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDatePropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDatePropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDatePropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDatePropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (PopupDateField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue() == null ? false : true;
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDatePropertyForm.setValidationVisible(false);
	                                newDatePropertyForm.setComponentError(null);
	                            } else {
	                                newDatePropertyForm.setValidationVisible(true);
	                                newDatePropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDatePropertyForm.setValidationVisible(false);
	                            ContentObjectProperty dateTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            dateTypeContentObjectProperty.setPropertyName(newDatePropertyName.getValue().toString());
	                            dateTypeContentObjectProperty.setPropertyType(PropertyType.DATE);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                dateTypeContentObjectProperty.setMultiple(true);
	                                Calendar[] valueArray = new Calendar[propertyValueNum];
	                                Calendar calDate = Calendar.getInstance();
	                                calDate.setTime((Date) dateSelect.getValue());
	                                valueArray[0] = calDate;
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(dateSelect.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    Calendar calDateAdd = Calendar.getInstance();
	                                    calDateAdd.setTime((Date) muitlPropertyValueList.get(i).getValue());
	                                    valueArray[i + 1] = calDateAdd;
	                                    sb.append("," + ((Date) muitlPropertyValueList.get(i).getValue()).toString());
	                                }
	                                dateTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                dateTypeContentObjectProperty.setMultiple(false);
	                                Calendar calDate = Calendar.getInstance();
	                                calDate.setTime((Date) dateSelect.getValue());
	                                dateTypeContentObjectProperty.setPropertyValue(calDate);
	                                propertyValueString = ((Date) dateSelect.getValue()).toString();
	                            }
	                            contentObjectPropertyMap.put(newDatePropertyName.getValue().toString(), dateTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Date]</span> " + newDatePropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDatePropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDatePropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);
	                            application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
	    	                    addContentObjectDateTypePropertyFormWindow=null;	                            
	                        } else {
	                            newDatePropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                    	application.getMainWindow().removeWindow(addContentObjectDateTypePropertyFormWindow);
		                    addContentObjectDateTypePropertyFormWindow=null;
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDatePropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectDateTypePropertyFormWindow.addComponent(newDatePropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectDateTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDateTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDateTypePropertyFormWindow);
	            }
	        });

	        addDecimalPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectDecimalTypePropertyFormWindow != null) {
	                	application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
	                	addContentObjectDecimalTypePropertyFormWindow=null;	                   
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectDecimalTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>DECIMAL</span> type property</b>", windowContent);
	                final List<TextField> muitlPropertyValueList = new ArrayList<TextField>();
	                final BaseForm newDecimalPropertyForm = new BaseForm();
	                newDecimalPropertyForm.setImmediate(true);
	                final TextField newDecimalPropertyName = new TextField("Property Name:");
	                newDecimalPropertyName.setInputPrompt("Please input property name");
	                newDecimalPropertyName.setWidth("320px");
	                newDecimalPropertyName.setRequired(true);
	                newDecimalPropertyName.setRequiredError("Property name is required.");
	                newDecimalPropertyForm.addField("_ADDNEW_DECIMAL_PROPERTY", newDecimalPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newDecimalPropertyName.addValidator(validator);

	                final TextField decimalValueInput = new TextField("Property Value:");
	                decimalValueInput.addValidator(new DoubleValidator("property value should be a decimal type number."));
	                newDecimalPropertyName.setWidth("200px");
	                decimalValueInput.setRequired(true);
	                decimalValueInput.setRequiredError("Property value is required.");
	                newDecimalPropertyForm.addField("_ADDNEW_DECIMAL_PROPERTY_Value", decimalValueInput);

	                Button addPropertyButton = new Button("+ Add more value");
	                addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                addPropertyButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        HorizontalLayout newPropertyValueLayout = new HorizontalLayout();
	                        newPropertyValueLayout.setWidth("280px");
	                        final TextField decimalValueInput = new TextField();
	                        newDecimalPropertyName.setWidth("200px");
	                        decimalValueInput.setImmediate(true);
	                        decimalValueInput.setRequired(true);
	                        decimalValueInput.setRequiredError("Property value is required.");
	                        decimalValueInput.addValidator(new DoubleValidator("property value should be a decimal type number."));
	                        decimalValueInput.addListener(new ValueChangeListener() {

	                            public void valueChange(ValueChangeEvent event) {

	                                boolean validateResult = newDecimalPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a decimal type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDecimalPropertyForm.setValidationVisible(false);
	                                        newDecimalPropertyForm.setComponentError(null);
	                                    } else {
	                                        newDecimalPropertyForm.setValidationVisible(true);
	                                        newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });

	                        decimalValueInput.setRequired(true);
	                        decimalValueInput.setRequiredError("property value is required.");
	                        newPropertyValueLayout.addComponent(decimalValueInput);
	                        Button removePropertyButton = new Button("- Remove this value");
	                        muitlPropertyValueList.add(decimalValueInput);
	                        removePropertyButton.addListener(new Button.ClickListener() {

	                            public void buttonClick(ClickEvent event) {
	                                Component parentLayout = event.getComponent().getParent();
	                                newDecimalPropertyForm.getLayout().removeComponent(parentLayout);
	                                muitlPropertyValueList.remove(decimalValueInput);
	                                boolean validateResult = newDecimalPropertyForm.isValid();
	                                if (validateResult) {
	                                    String errorMessage = "";
	                                    boolean addonValuevaild = true;
	                                    for (TextField select : muitlPropertyValueList) {
	                                        boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                        boolean fieldValid = select.isValid();
	                                        if (!fieldHasDataCheck) {
	                                            errorMessage = "property value is required.";
	                                        } else {
	                                            if (!fieldValid) {
	                                                errorMessage = "property value should be a decimal type number.";
	                                            }
	                                        }
	                                        addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                                    }
	                                    if (addonValuevaild) {
	                                        newDecimalPropertyForm.setValidationVisible(false);
	                                        newDecimalPropertyForm.setComponentError(null);
	                                    } else {
	                                        newDecimalPropertyForm.setValidationVisible(true);
	                                        newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                                    }
	                                    validateResult = validateResult & addonValuevaild;
	                                }
	                            }
	                        });
	                        removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                        newPropertyValueLayout.addComponent(removePropertyButton);
	                        newDecimalPropertyForm.getLayout().addComponent(newPropertyValueLayout);
	                    }
	                });

	                HorizontalLayout addPropertyValueButtonLayout = new HorizontalLayout();
	                addPropertyValueButtonLayout.setWidth("320px");
	                addPropertyValueButtonLayout.addComponent(addPropertyButton);
	                addPropertyValueButtonLayout.setComponentAlignment(addPropertyButton, Alignment.MIDDLE_RIGHT);
	                newDecimalPropertyForm.getLayout().addComponent(addPropertyValueButtonLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newDecimalPropertyForm.getFooter().addComponent(addPropertyButtonBar);

	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newDecimalPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newDecimalPropertyForm.isValid();
	                        if (validateResult) {
	                            String errorMessage = "";
	                            boolean addonValuevaild = true;
	                            for (TextField select : muitlPropertyValueList) {
	                                boolean fieldHasDataCheck = select.getValue().equals("") ? false : true;
	                                boolean fieldValid = select.isValid();
	                                if (!fieldHasDataCheck) {
	                                    errorMessage = "property value is required.";
	                                } else {
	                                    if (!fieldValid) {
	                                        errorMessage = "property value should be a decimal type number.";
	                                    }
	                                }
	                                addonValuevaild = addonValuevaild & validateResult & fieldValid & fieldHasDataCheck;
	                            }
	                            if (addonValuevaild) {
	                                newDecimalPropertyForm.setValidationVisible(false);
	                                newDecimalPropertyForm.setComponentError(null);
	                            } else {
	                                newDecimalPropertyForm.setValidationVisible(true);
	                                newDecimalPropertyForm.setComponentError(new UserError(errorMessage));
	                            }
	                            validateResult = validateResult & addonValuevaild;
	                        }

	                        if (validateResult) {
	                            newDecimalPropertyForm.setValidationVisible(false);
	                            newDecimalPropertyForm.setComponentError(null);
	                            ContentObjectProperty decimalTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            decimalTypeContentObjectProperty.setPropertyName(newDecimalPropertyName.getValue().toString());
	                            decimalTypeContentObjectProperty.setPropertyType(PropertyType.DECIMAL);
	                            int propertyValueNum = muitlPropertyValueList.size() + 1;
	                            String propertyValueString = "";
	                            if (propertyValueNum > 1) {
	                                decimalTypeContentObjectProperty.setMultiple(true);
	                                BigDecimal[] valueArray = new BigDecimal[propertyValueNum];
	                                valueArray[0] = new BigDecimal(decimalValueInput.getValue().toString());
	                                StringBuffer sb = new StringBuffer();
	                                sb.append(decimalValueInput.getValue().toString());
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    valueArray[i + 1] = new BigDecimal(muitlPropertyValueList.get(i).getValue().toString());
	                                    sb.append("," + valueArray[i + 1]);
	                                }
	                                decimalTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = sb.toString();
	                            } else {
	                                decimalTypeContentObjectProperty.setMultiple(false);
	                                decimalTypeContentObjectProperty.setPropertyValue(new BigDecimal(decimalValueInput.getValue().toString()));
	                                propertyValueString = decimalValueInput.getValue().toString();
	                            }

	                            contentObjectPropertyMap.put(newDecimalPropertyName.getValue().toString(), decimalTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Decimal]</span> " + newDecimalPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newDecimalPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newDecimalPropertyName.getValue().toString());
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
	                            addContentObjectDecimalTypePropertyFormWindow=null;
	                        } else {
	                            newDecimalPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                        
	                        application.getMainWindow().removeWindow(addContentObjectDecimalTypePropertyFormWindow);
		                	addContentObjectDecimalTypePropertyFormWindow=null;	  
	                    }
	                });
	                addContentObjectDecimalTypePropertyFormWindow.addComponent(newDecimalPropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectDecimalTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectDecimalTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectDecimalTypePropertyFormWindow);
	            }
	        });

	        addBinaryPropertyButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                if (addContentObjectBinaryTypePropertyFormWindow != null) {	                   
	                    application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
	                    addContentObjectBinaryTypePropertyFormWindow=null;	  
	                }
	                VerticalLayout windowContent = new VerticalLayout();
	                addContentObjectBinaryTypePropertyFormWindow = UIComponentCreator.createLightDialogWindow_AddData("<b>Add <span style='color:#ce0000;'>BINARY</span> type property</b>", windowContent);
	                final List<String> muitlPropertyValueList = new ArrayList<String>();
	                final BaseForm newBinaryPropertyForm = new BaseForm();

	                VerticalLayout uploaderContainerLayout = new VerticalLayout();
	                uploaderContainerLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	                newBinaryPropertyForm.setLayout(uploaderContainerLayout);
	                newBinaryPropertyForm.setImmediate(true);
	                final TextField newBinaryPropertyName = new TextField("Property Name:");
	                newBinaryPropertyName.setInputPrompt("Please input property name");
	                newBinaryPropertyName.setWidth("320px");
	                newBinaryPropertyName.setRequired(true);
	                newBinaryPropertyName.setRequiredError("Property name is required.");
	                newBinaryPropertyForm.addField("_ADDNEW_BINARY_PROPERTY", newBinaryPropertyName);
	                AbstractValidator validator = new AbstractValidator("") {

	                    public boolean isValid(Object value) {
	                        if (contentObjectPropertyMap.containsKey(value)) {
	                            this.setErrorMessage("Property < {0} > already exists.");
	                            return false;
	                        }
	                        return true;
	                    }
	                };
	                newBinaryPropertyName.addValidator(validator);

	                VerticalLayout newBinaryPropertyValueLayout = new VerticalLayout();
	                newBinaryPropertyValueLayout.setWidth("500px");
	                newBinaryPropertyValueLayout.setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
	                newBinaryPropertyForm.getLayout().addComponent(new BinaryPropertyFileProgressMonitoringUploader(newBinaryPropertyValueLayout, muitlPropertyValueList, newBinaryPropertyForm));
	                newBinaryPropertyForm.getLayout().addComponent(new Label("Added files:"));
	                newBinaryPropertyForm.getLayout().addComponent(newBinaryPropertyValueLayout);

	                List<Button> controlButtonList = new ArrayList<Button>();
	                Button okButton = new Button("Confirm add action");
	                Button cancelAddButton = new Button("Cancel add action");
	                cancelAddButton.setStyleName(BaseTheme.BUTTON_LINK);
	                controlButtonList.add(okButton);
	                controlButtonList.add(cancelAddButton);
	                okButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {
	                        newBinaryPropertyForm.setValidationVisible(true);
	                        boolean validateResult = newBinaryPropertyForm.isValid();
	                        String errorMessage = "";
	                        if (validateResult) {
	                            if (muitlPropertyValueList.size() == 0) {
	                                errorMessage = "Please upload at least one binary file.";
	                                newBinaryPropertyForm.setValidationVisible(true);
	                                newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
	                                validateResult = false;
	                            } else {
	                                newBinaryPropertyForm.setValidationVisible(false);
	                                newBinaryPropertyForm.setComponentError(null);
	                            }
	                        }

	                        if (validateResult) {
	                            ContentObjectProperty binaryTypeContentObjectProperty = ContentComponentFactory.createContentObjectProperty();
	                            binaryTypeContentObjectProperty.setPropertyName(newBinaryPropertyName.getValue().toString());
	                            binaryTypeContentObjectProperty.setPropertyType(PropertyType.BINARY);
	                            int propertyValueNum = muitlPropertyValueList.size();
	                            String propertyValueString = muitlPropertyValueList.get(0);
	                            File propertyFile = null;
	                            if (propertyValueNum > 1) {
	                                binaryTypeContentObjectProperty.setMultiple(true);
	                                Binary[] valueArray = new Binary[propertyValueNum];
	                                StringBuffer sb = new StringBuffer();
	                                for (int i = 0; i < muitlPropertyValueList.size(); i++) {
	                                    try {
	                                        propertyFile = new File(tempFileDir + muitlPropertyValueList.get(i));
	                                        valueArray[i] = ValueFactoryImpl.getInstance().createBinary(new FileInputStream(propertyFile));
	                                        propertyFile.delete();//need comfirm if this works
	                                    } catch (FileNotFoundException ex) {
	                                        Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                    } catch (RepositoryException ex) {
	                                        Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                    }
	                                    if (i != 0) {
	                                        sb.append("," + muitlPropertyValueList.get(i));
	                                    }
	                                }
	                                binaryTypeContentObjectProperty.setPropertyValue(valueArray);
	                                propertyValueString = propertyValueString + sb.toString();
	                            } else {
	                                binaryTypeContentObjectProperty.setMultiple(false);
	                                try {
	                                    propertyFile = new File(tempFileDir + muitlPropertyValueList.get(0));
	                                    binaryTypeContentObjectProperty.setPropertyValue(ValueFactoryImpl.getInstance().createBinary(new FileInputStream(propertyFile)));
	                                    propertyFile.delete();//need comfirm if this works
	                                } catch (FileNotFoundException ex) {
	                                    Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                } catch (RepositoryException ex) {
	                                    Logger.getLogger(ContentObjectUIElementCreator.class.getName()).log(Level.SEVERE, null, ex);
	                                }
	                            }
	                            contentObjectPropertyMap.put(newBinaryPropertyName.getValue().toString(), binaryTypeContentObjectProperty);
	                            HorizontalLayout addedPropertyInfoLayout = new HorizontalLayout();
	                            addedPropertyInfoLayout.setWidth("400px");
	                            Label propertyNameLable = new Label("<b style='color:#ce0000;'><span style='color:#333333;'>[Date]</span> " + newBinaryPropertyName.getValue().toString() + "</b> : " + propertyValueString, Label.CONTENT_XHTML);
	                            propertyNameLable.setWidth("330px");
	                            propertyNameLable.setDescription("<b style='color:#ce0000;'>" + newBinaryPropertyName.getValue().toString() + ":</b>" + propertyValueString);
	                            Button removePropertyButton = new Button("- Remove");
	                            removePropertyButton.addListener(new Button.ClickListener() {

	                                public void buttonClick(ClickEvent event) {
	                                    Component parentLayout = event.getComponent().getParent();
	                                    prpoertyDataContainer.removeComponent(parentLayout);
	                                    contentObjectPropertyMap.remove(newBinaryPropertyName.getValue().toString());
	                                    for (String fileName : muitlPropertyValueList) {
	                                        new File(tempFileDir + fileName).delete();
	                                    }
	                                }
	                            });
	                            removePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
	                            addedPropertyInfoLayout.addComponent(propertyNameLable);
	                            addedPropertyInfoLayout.addComponent(removePropertyButton);
	                            addedPropertyInfoLayout.setComponentAlignment(removePropertyButton, Alignment.BOTTOM_RIGHT);
	                            prpoertyDataContainer.addComponent(addedPropertyInfoLayout);	                           
	                            application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
	    	                    addContentObjectBinaryTypePropertyFormWindow=null;	 
	                        } else {
	                            newBinaryPropertyForm.setValidationVisible(true);
	                        }
	                    }
	                });

	                cancelAddButton.addListener(new Button.ClickListener() {

	                    public void buttonClick(ClickEvent event) {	                        
	                        application.getMainWindow().removeWindow(addContentObjectBinaryTypePropertyFormWindow);
		                    addContentObjectBinaryTypePropertyFormWindow=null;	 
	                    }
	                });
	                BaseButtonBar addPropertyButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, controlButtonList);
	                newBinaryPropertyForm.getFooter().addComponent(addPropertyButtonBar);
	                addContentObjectBinaryTypePropertyFormWindow.addComponent(newBinaryPropertyForm);
	                int postion_x = addPropertyWindow.getPositionX() + 10;
	                int postion_y = addPropertyWindow.getPositionX() + 130;
	                addContentObjectBinaryTypePropertyFormWindow.setPositionX(postion_x);
	                addContentObjectBinaryTypePropertyFormWindow.setPositionY(postion_y);
	                application.getMainWindow().addWindow(addContentObjectBinaryTypePropertyFormWindow);
	            }
	        });

	        final Button okButton = new Button("Confirm add action");
	        final Button cancelAddbutton = new Button("Cancel add action", new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {               
	                
	                application.getMainWindow().removeWindow(addPropertyWindow);
	                addPropertyWindow=null;	 
	            }
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	        
	        okButton.addListener(new Button.ClickListener() {

	            public void buttonClick(ClickEvent event) {
	                newContentObjectForm.setValidationVisible(true);
	                boolean validateResult = newContentObjectForm.isValid();
	                Label messageLable = new Label("<b style='color:#333333;'>Adding Content Object Properties...</b>", Label.CONTENT_XHTML);
	                messageLable.setHeight("24px");
	                if (validateResult) {
	                    okButton.setEnabled(false);
	                    newContentObjectForm.getLayout().addComponent(messageLable);
	                    cancelAddbutton.setCaption("Close window");
	                    
	                    AddContentPropertiesResult addrelu = null;
	                    if (!contentObjectPropertyMap.isEmpty()) {
	                        List<ContentObjectProperty> contentObjectList = new ArrayList<ContentObjectProperty>(contentObjectPropertyMap.values());
	                        addrelu = ContentRepositoryDataUtil.addContentObjectProperties(contentSpaceName.toString(), parentNodeABSPath, contentObjectList); 
	                    } else {
	                        //addrelu = ContentRepositoryDataUtil.addSubContentObject(contentSpaceName.toString(), parentNodeABSPath, subContentObjectName, null);
	                    }
	            
	                    switch (addrelu) {
	                        case AddContentPropertiesSuccessful:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>Add Property success.</b>", Label.CONTENT_XHTML));
	                            //REFRESH CONTENT DETAIL
	                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
	                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                            }  
	                            break;
	                        case AddContentPropertiesFail:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>AddProperty failed.</b>", Label.CONTENT_XHTML));	         
	                            //REFRESH CONTENT DETAIL	                          
	                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
	                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                            } 
	                            break;
	                        case GetRepositoryErrorDuringOperation:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>Get content repository error during add operation,please contact system administrator.</b>", Label.CONTENT_XHTML));
	                            break;
	                    }
	                }
	            }
	        });
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(260, 45, Alignment.MIDDLE_CENTER, buttonList);
	        newContentObjectForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newContentObjectForm);
	        return formContainer;
	    }
	 
	 public DialogWindow createDeleteContentObjectConfirmWindow(Object targetItem) {
		 Object parentItemKeyID = this.contentSpaceDataContainer.getParent(targetItem);
	     Object parentItemName = this.contentSpaceDataContainer.getItem(parentItemKeyID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	     Object contentObjectName = this.contentSpaceDataContainer.getItem(targetItem).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();

	     if (deleteContentObjectConfirmWindow != null) {	    	
	    	 application.getMainWindow().removeWindow(deleteContentObjectConfirmWindow);
	    	 deleteContentObjectConfirmWindow=null;
	     }
	     String windowTitle = "Delete Content Object";
	     String windowDesc = "Delete content object " + CommonStyleUtil.formatCurrentItemStyle(contentObjectName.toString()) + " of parent content object " + CommonStyleUtil.formatParentItemStyle(parentItemName.toString());
	     return deleteContentObjectConfirmWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, buildDeleteContentObjectWindowControlButtons(targetItem));	     
	 }
	 
	 private HorizontalLayout buildDeleteContentObjectWindowControlButtons(Object actionItemID) {
	        final Object currentItemID = actionItemID;
	        final Object parentItemID = this.contentSpaceDataContainer.getParent(actionItemID);
	        final Object parentItemName = this.contentSpaceDataContainer.getItem(parentItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        final Object contentObjectName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();

	        Button confirmDeleteButton = new Button("Confirm delete action");
	        confirmDeleteButton.addListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1547570654790544291L;

				public void buttonClick(ClickEvent event) {
	                HorizontalLayout deleteResultLayout = new HorizontalLayout();
	                deleteResultLayout.setStyleName(UICommonElementDefination.DIALOG_ITEM_DESCRIPTION_LABLE_STYLE);
	                deleteResultLayout.setWidth("450px");
	                deleteResultLayout.setHeight("40px");

	                Label messageLable = new Label("<b style='color:#333333;'>Deleting Content Object...</b>", Label.CONTENT_XHTML);
	                Button closeButton = new Button("Close dialog");
	                closeButton.setStyleName(BaseTheme.BUTTON_LINK);
	                closeButton.addListener(new Button.ClickListener() {	                
						private static final long serialVersionUID = 1108629575973076554L;

						public void buttonClick(ClickEvent event) {	                        
	                        application.getMainWindow().removeWindow(deleteContentObjectConfirmWindow);
	                        deleteContentObjectConfirmWindow=null;
	                    }
	                });
	                deleteResultLayout.addComponent(messageLable);
	                deleteResultLayout.setComponentAlignment(messageLable, Alignment.MIDDLE_CENTER);
	                deleteResultLayout.addComponent(closeButton);
	                deleteResultLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
	                deleteContentObjectConfirmWindow.refreshDialogWindowContent(deleteResultLayout, Alignment.MIDDLE_RIGHT);
	                DeleteContentObjectResult deleteResult = null;
	                boolean parentNodeIsContentSpace = false;
	                String parentNodeABSPath =null;
	                if (currentItemID.toString().contains(ContentSpaceDataProvider.subContentObjectIdPerfix)) {
	                    //delete content object
	                	parentNodeABSPath= ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, parentItemID);
	                    deleteResult = ContentRepositoryDataUtil.deleteContentObject(contentSpaceName.toString(), parentNodeABSPath, contentObjectName.toString());
	                } else {
	                    //delete root content object
	                    parentNodeIsContentSpace = true;
	                    deleteResult = ContentRepositoryDataUtil.deleteRootContentObject(parentItemName.toString(), contentObjectName.toString());
	                }
	                switch (deleteResult) {
	                    case DeleteContentObjectSuccessful:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>Delete content object success.</b>", Label.CONTENT_XHTML));
	                        contentSpaceDataContainer.removeItemRecursively(currentItemID);
	                        if (!parentNodeIsContentSpace && contentSpaceDataContainer.getChildren(parentItemID)==null) {
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemID, false);
	                        }	                        
	                        //REFRESH CONTENT DETAIL	                          
                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
                            	if(parentNodeABSPath!=null){                            		
                            		contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemID);
                            	}else{
                            		//current object in contentObjectDetail is root content object
                            	}                            	
                            } 
	                        break;
	                    case ContentObjectAlreadyDeleted:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>Content object already deleted from content repository.</b>", Label.CONTENT_XHTML));
	                        contentSpaceDataContainer.removeItemRecursively(currentItemID);
	                        if (!parentNodeIsContentSpace && contentSpaceDataContainer.getChildren(parentItemID)==null) {
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemID, false);
	                        }
	                        //REFRESH CONTENT DETAIL	                          
                            if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(parentNodeABSPath)){
                            	if(parentNodeABSPath!=null){                            		
                            		contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemID);
                            	}else{
                            		//current object in contentObjectDetail is root content object
                            	}                            	
                            } 
	                        break;
	                    case GetRepositoryErrorDuringOperation:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>Get content repository error during add operation,please contact system administrator.</b>", Label.CONTENT_XHTML));
	                        break;
	                }
	            }
	        });

	        Button cancelDeleteButton = new Button("Cancel delete action");
	        cancelDeleteButton.addListener(new Button.ClickListener() {	           
				private static final long serialVersionUID = -795256272360354300L;

				public void buttonClick(ClickEvent event) {	               
	                application.getMainWindow().removeWindow(deleteContentObjectConfirmWindow);
                    deleteContentObjectConfirmWindow=null;
	                
	            }
	        });
	        cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(confirmDeleteButton);
	        buttonList.add(cancelDeleteButton);
	        return new BaseButtonBar(300, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    }
	 
	 public DialogWindow createAddBinaryContentFormWindow(Object actionItemID) {
	        Object actionItemName = contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        if (addBinaryContentFormWindow != null) {
	        	 application.getMainWindow().removeWindow(addBinaryContentFormWindow);
	        	 addBinaryContentFormWindow=null;	            
	        }
	        String windowTitle = "Add Binary Content";
	        String windowDesc = "Add new binary file in content object " + CommonStyleUtil.formatCurrentItemStyle(actionItemName.toString());
	        return  addBinaryContentFormWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, createAddBinaryContentForm(actionItemID,ContentRepositoryDataUtil.BINARY_CONTENT_TYPE_BINARY));
	    }	 
	 
	 private Layout createAddBinaryContentForm(Object actionItemID,final String contentType) {	     
	     final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();	     
	     final Object parentItemId = actionItemID;
	     final int currentChildNum;
	        if (this.contentSpaceDataContainer.getChildren(parentItemId) == null) {
	            currentChildNum = 0;
	        } else {
	            currentChildNum = this.contentSpaceDataContainer.getChildren(parentItemId).size();
	        }
	     final VerticalLayout formContainer = new VerticalLayout();
	     final BaseForm newBinaryContentForm = new BaseForm();	      
	     final String nodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, parentItemId);
	     newBinaryContentForm.setImmediate(true);
	     final List<File> uploadedFileList = new ArrayList<File>();
	     newBinaryContentForm.getLayout().addComponent(new BinaryContentFileProgressMonitoringUploader(uploadedFileList));
	     final Button okButton = new Button("Confirm add action");
	     final Button cancelAddbutton = new Button("Cancel add action", new Button.ClickListener() {	           
	    	 private static final long serialVersionUID = -1051216463070087984L;

				public void buttonClick(ClickEvent event) {
	            	application.getMainWindow().removeWindow(addBinaryContentFormWindow);
		        	addBinaryContentFormWindow=null;
	                if (!uploadedFileList.isEmpty()) {
	                    uploadedFileList.get(0).delete();
	                }
	            }
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);

	        okButton.addListener(new Button.ClickListener() {	          
				private static final long serialVersionUID = 1787662632489409058L;

				AddBinaryContentObjectResult result=null;				
				public void buttonClick(ClickEvent event) {	               
					result = ContentRepositoryDataUtil.addBinaryContentFile(contentSpaceName.toString(), nodeABSPath, uploadedFileList.get(0),contentType);					
					String newBinaryObjectName=uploadedFileList.get(0).getName();
					uploadedFileList.get(0).delete();					
					Item subContentObjectItem;
					String subContentObjectID;
					switch (result) {
						case AddBinaryContentObjectSuccessful:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Add binary content object success.</b>", Label.CONTENT_XHTML));
							subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + currentChildNum;   	 
							subContentObjectItem= contentSpaceDataContainer.addItem(subContentObjectID);
							subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(newBinaryObjectName);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                        contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                        contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                        contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                        if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(nodeABSPath)){
	                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                        }  
							break;
						case ContentObjectAlreadyExists:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Binary content object already exists in content repository.</b>", Label.CONTENT_XHTML));
							subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + currentChildNum;
							subContentObjectItem = contentSpaceDataContainer.addItem(subContentObjectID);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(newBinaryObjectName);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                        contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                        contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                        String newNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, subContentObjectID);
	                        long childrenNumber = ContentRepositoryDataUtil.getSubComponentChildrenNumber(contentSpaceName.toString(), newNodeABSPath);
	                        if (childrenNumber > 0) {
	                            contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, true);
	                        } else {
	                            contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                        }
	                        if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(nodeABSPath)){
                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                        } 
	                        break;							
						case GetRepositoryErrorDuringOperation:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Get content repository error during add operation,please contact system administrator.</b>", Label.CONTENT_XHTML));
							break;					 
					 }	
	            }
	        });   

	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	        newBinaryContentForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newBinaryContentForm);
	        return formContainer;
	    }
	 
	 public DialogWindow createAddTextContentFormWindow(Object actionItemID) {
	        Object actionItemName = contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        if (addTextContentFormWindow != null) {
	        	 application.getMainWindow().removeWindow(addTextContentFormWindow);
	        	 addTextContentFormWindow=null;
	        }
	        String windowTitle = "Add Text Content";
	        String windowDesc = "Add new text file in content object " + CommonStyleUtil.formatCurrentItemStyle(actionItemName.toString());
	        return addTextContentFormWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, createAddTextContentForm(actionItemID,ContentRepositoryDataUtil.BINARY_CONTENT_TYPE_TEXT));
	    }	 
	 
	 private Layout createAddTextContentForm(Object actionItemID,final String contentType) {
		 final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();
	     final Object parentItemId = actionItemID;
	     final int currentChildNum;
	     if (this.contentSpaceDataContainer.getChildren(parentItemId) == null) {
	         currentChildNum = 0;
	     } else {
	         currentChildNum = this.contentSpaceDataContainer.getChildren(parentItemId).size();
	     }
		 
	     final VerticalLayout formContainer = new VerticalLayout();
	     final BaseForm newBinaryContentForm = new BaseForm();
	       
	     final String nodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, parentItemId);
	     newBinaryContentForm.setImmediate(true);
	     final List<File> uploadedFileList = new ArrayList<File>();
	     newBinaryContentForm.getLayout().addComponent(new BinaryContentFileProgressMonitoringUploader(uploadedFileList));
	     final Button okButton = new Button("Confirm add action");
	     final Button cancelAddbutton = new Button("Cancel add action", new Button.ClickListener() {	           
	    	private static final long serialVersionUID = -1051216463070087984L;

			public void buttonClick(ClickEvent event) {
	           	application.getMainWindow().removeWindow(addTextContentFormWindow);
	           	addTextContentFormWindow=null;
	               if (!uploadedFileList.isEmpty()) {
	                   uploadedFileList.get(0).delete();
	               }
	           }
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);

	        okButton.addListener(new Button.ClickListener() {	          
				private static final long serialVersionUID = 1787662632489409058L;
				AddBinaryContentObjectResult result=null;
				public void buttonClick(ClickEvent event) {	               
					result = ContentRepositoryDataUtil.addBinaryContentFile(contentSpaceName.toString(), nodeABSPath, uploadedFileList.get(0),contentType);							
					String newBinaryObjectName=uploadedFileList.get(0).getName();
					uploadedFileList.get(0).delete();					
					Item subContentObjectItem;
					String subContentObjectID;
					switch (result) {
						case AddBinaryContentObjectSuccessful:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Add text content object success.</b>", Label.CONTENT_XHTML));
							subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + currentChildNum;   	 
							subContentObjectItem= contentSpaceDataContainer.addItem(subContentObjectID);
							subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(newBinaryObjectName);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                        contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                        contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                        contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                        if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(nodeABSPath)){
                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                        } 
							break;
						case ContentObjectAlreadyExists:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Text content object already exists in content repository.</b>", Label.CONTENT_XHTML));
							subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + currentChildNum;
							subContentObjectItem = contentSpaceDataContainer.addItem(subContentObjectID);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(newBinaryObjectName);
	                        subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                        contentSpaceDataContainer.setChildrenAllowed(parentItemId, true);
	                        contentSpaceDataContainer.setParent(subContentObjectID, parentItemId);
	                        String newNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, subContentObjectID);
	                        long childrenNumber = ContentRepositoryDataUtil.getSubComponentChildrenNumber(contentSpaceName.toString(), newNodeABSPath);
	                        if (childrenNumber > 0) {
	                            contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, true);
	                        } else {
	                            contentSpaceDataContainer.setChildrenAllowed(subContentObjectID, false);
	                        }
	                        if(contentManagementPanel!=null&&contentManagementPanel.contentObjectDetail.currentContentObjectABSPath.equals(nodeABSPath)){
                            	contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(parentItemId);	                            	
	                        } 
	                        break;							
						case GetRepositoryErrorDuringOperation:
							//messageLable.setValue(new Label("<b style='color:#333333;'>Get content repository error during add operation,please contact system administrator.</b>", Label.CONTENT_XHTML));
							break;					 
					 }	
	            }	           
	        });   

	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	        newBinaryContentForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newBinaryContentForm);
	        return formContainer;
	    }	
	 
	 private boolean checkNewItemAlreadyExistInContentSpaceDataContainer(Object parentNodeID, String newItemString) {
		 return ContentRepositoryDataUtil.checkNewItemAlreadyExistInContentSpaceDataContainer(this.contentSpaceDataContainer, parentNodeID, newItemString);		
	 }	
}
