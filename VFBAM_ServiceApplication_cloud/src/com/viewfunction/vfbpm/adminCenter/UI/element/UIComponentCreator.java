package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Component;

import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

public class UIComponentCreator {   

    public static DialogWindow createDialogWindow_AddData(String windowTitle, String windowDescription, int windowWidth, Component windowContentComponent) {
        DialogWindow addOperationDialogWindow = new DialogWindow();
        addOperationDialogWindow.setWindowTitleIcon(UICommonElementDefination.ICON_window_AddNewTtleIcon);
        addOperationDialogWindow.setWindowTitleMessage(windowTitle);
        addOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_addItem);
        addOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        addOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        addOperationDialogWindow.setWindowWidth(windowWidth);
        addOperationDialogWindow.assembleDialogWindow();
        addOperationDialogWindow.setResizable(true);
        addOperationDialogWindow.center();
        addOperationDialogWindow.setModal(true);
        return addOperationDialogWindow;
    }

    public static DialogWindow createDialogWindow_AddData(String windowTitle, String windowDescription, Component windowContentComponent) {
        DialogWindow addOperationDialogWindow = new DialogWindow();
        addOperationDialogWindow.setWindowTitleIcon(UICommonElementDefination.ICON_window_AddNewTtleIcon);
        addOperationDialogWindow.setWindowTitleMessage(windowTitle);
        addOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_addItem);
        addOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        addOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        addOperationDialogWindow.assembleDialogWindow();
        addOperationDialogWindow.setResizable(false);
        addOperationDialogWindow.center();
        addOperationDialogWindow.setModal(true);
        return addOperationDialogWindow;
    }

    public static LightDialogWindow createLightDialogWindow_AddData(String windowDescription, Component windowContentComponent) {
        LightDialogWindow addOperationDialogWindow = new LightDialogWindow();
        addOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_addItem);
        addOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        addOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        addOperationDialogWindow.assembleDialogWindow();
        addOperationDialogWindow.setResizable(false);
        addOperationDialogWindow.center();
        addOperationDialogWindow.setModal(true);
        return addOperationDialogWindow;
    }

    public static DialogWindow createDialogWindow_AddData_Confirm(String windowTitle, String windowDescription, Component windowContentComponent) {
        DialogWindow addOperationDialogWindow = new DialogWindow();
        addOperationDialogWindow.setWindowTitleIcon(UICommonElementDefination.ICON_window_AddNewTtleIcon);
        addOperationDialogWindow.setWindowTitleMessage(windowTitle);
        addOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_addItem);
        addOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        addOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        addOperationDialogWindow.assembleDialogWindow();
        addOperationDialogWindow.setResizable(false);
        addOperationDialogWindow.center();
        addOperationDialogWindow.setModal(true);
        return addOperationDialogWindow;
    }

    public static DialogWindow createDialogWindow_DeleteData_Confirm(String windowTitle, String windowDescription, int windowWidth, Component windowContentComponent) {
        DialogWindow deleteConfirmOperationDialogWindow = new DialogWindow();
        deleteConfirmOperationDialogWindow.setWindowTitleIcon(UICommonElementDefination.ICON_window_DeleteSelectTtleIcon);
        deleteConfirmOperationDialogWindow.setWindowTitleMessage(windowTitle);
        deleteConfirmOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_deleteItem);
        deleteConfirmOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        deleteConfirmOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        deleteConfirmOperationDialogWindow.setWindowWidth(windowWidth);
        deleteConfirmOperationDialogWindow.assembleDialogWindow();
        deleteConfirmOperationDialogWindow.setResizable(false);
        deleteConfirmOperationDialogWindow.center();
        deleteConfirmOperationDialogWindow.setModal(true);
        return deleteConfirmOperationDialogWindow;
    }

    public static DialogWindow createDialogWindow_DeleteData_Confirm(String windowTitle, String windowDescription, Component windowContentComponent) {
        DialogWindow deleteConfirmOperationDialogWindow = new DialogWindow();
        deleteConfirmOperationDialogWindow.setWindowTitleIcon(UICommonElementDefination.ICON_window_DeleteSelectTtleIcon);
        deleteConfirmOperationDialogWindow.setWindowTitleMessage(windowTitle);
        deleteConfirmOperationDialogWindow.setWindowDescriptionIcon(UICommonElementDefination.ICON_Button_deleteItem);
        deleteConfirmOperationDialogWindow.setWindowDescriptionMessage(windowDescription);
        deleteConfirmOperationDialogWindow.setWindowContentComponent(windowContentComponent);
        deleteConfirmOperationDialogWindow.assembleDialogWindow();
        deleteConfirmOperationDialogWindow.setResizable(false);
        deleteConfirmOperationDialogWindow.center();
        deleteConfirmOperationDialogWindow.setModal(true);
        return deleteConfirmOperationDialogWindow;
    }
}
