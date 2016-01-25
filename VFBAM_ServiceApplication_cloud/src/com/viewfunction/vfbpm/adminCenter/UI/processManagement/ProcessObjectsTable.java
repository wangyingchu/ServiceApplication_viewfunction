package com.viewfunction.vfbpm.adminCenter.UI.processManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.processRepository.processBureau.HistoricProcessStep;
import com.viewfunction.processRepository.processBureau.ProcessObject;
import com.viewfunction.processRepository.processBureau.ProcessSpace;
import com.viewfunction.processRepository.processBureau.ProcessStep;

public class ProcessObjectsTable extends Table{
	private static final long serialVersionUID = 8210448270666033106L;
	
	private UserClientInfo userClientInfo;
	private List<ProcessObject> processObjectList;
	
	private String columnName_CurrentProcessSteps="columnName_CurrentProcessSteps";
	private String columnName_FinishedProcessSteps="columnName_FinishedProcessSteps";
	private String columnName_NextProcessSteps="columnName_NextProcessSteps";
	private String columnName_ProcessDurationInMillis="columnName_ProcessDurationInMillis";
	private String columnName_ProcessEndTime="columnName_ProcessEndTime";
	private String columnName_ProcessObjectId="columnName_ProcessObjectId";
	private String columnName_ProcessStartTime="columnName_ProcessStartTime";
	private String columnName_ProcessStartUserId="columnName_ProcessStartUserId";
	private String columnName_ProcessOperations="columnName_ProcessOperations";
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	public ProcessObjectsTable(UserClientInfo userClientInfo,List<ProcessObject> processObjectList,int processStatus){
		this.userClientInfo=userClientInfo;
		this.processObjectList=processObjectList;		
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getProcessObjectsData(processStatus));		
		if(processStatus==ProcessSpace.PROCESS_STATUS_UNFINISHED){
			setColumnHeaders(new String[] {
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processID"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_currentSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_finishedSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_nextSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startUser"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startTime"),
					"",
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processOperations")
			});
		}
		else if(processStatus==ProcessSpace.PROCESS_STATUS_FINISHED){
			setColumnHeaders(new String[] {
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processID"),					
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_finishedSteps"),				
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startUser"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startTime"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_endTime"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processDuration"),
					"",
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processOperations")
			});
			
		}else{
			setColumnHeaders(new String[] {
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processID"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_currentSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_finishedSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_nextSteps"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startUser"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_startTime"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_endTime"),
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processDuration"),
					"",
					this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_column_processOperations")
			});
		}		
		setColumnWidth(columnName_ProcessObjectId, 70);	
		setColumnWidth(columnName_ProcessStartTime, 100);
		setColumnWidth(columnName_ProcessEndTime, 100);
		setColumnAlignment(columnName_ProcessOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ProcessOperations, 150);		
		addGeneratedColumns(processStatus);
	}
	
	private IndexedContainer getProcessObjectsData(int processStatus){
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_ProcessObjectId, String.class,null);		
		if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
			container.addContainerProperty(columnName_CurrentProcessSteps, List.class,null);
		}		
		container.addContainerProperty(columnName_FinishedProcessSteps, List.class,null);
		if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
			container.addContainerProperty(columnName_NextProcessSteps, List.class,null);	
		}			
		container.addContainerProperty(columnName_ProcessStartUserId, String.class,null);		
		container.addContainerProperty(columnName_ProcessStartTime, Date.class,null);
		
		if(processStatus!=ProcessSpace.PROCESS_STATUS_UNFINISHED){
			container.addContainerProperty(columnName_ProcessEndTime, Date.class,null);
			container.addContainerProperty(columnName_ProcessDurationInMillis, String.class,null);
		}				
        container.addContainerProperty("DIV", String.class,null);
        container.addContainerProperty(columnName_ProcessOperations, String.class,null);	        
        if(processObjectList==null){
			return container;
		}
        if(processObjectList!=null){
			if(processObjectList.size()<=20){
				setPageLength(processObjectList.size());
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}  
        
        if(processObjectList!=null){
        	for(int i=0;i<processObjectList.size();i++){
        		ProcessObject processObject=processObjectList.get(i);			
				String id = ""+i;
				Item item = container.addItem(id);	
				item.getItemProperty(columnName_ProcessObjectId).setValue(processObject.getProcessObjectId());
				if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
					item.getItemProperty(columnName_CurrentProcessSteps).setValue(processObject.getCurrentProcessSteps());
				}				
				item.getItemProperty(columnName_FinishedProcessSteps).setValue(processObject.getFinishedProcessSteps());
				if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
					item.getItemProperty(columnName_NextProcessSteps).setValue(processObject.getNextProcessSteps());
				}				
				item.getItemProperty(columnName_ProcessStartUserId).setValue(processObject.getProcessStartUserId());				
				item.getItemProperty(columnName_ProcessStartTime).setValue(processObject.getProcessStartTime());			
				if(processStatus!=ProcessSpace.PROCESS_STATUS_UNFINISHED){
					item.getItemProperty(columnName_ProcessEndTime).setValue(processObject.getProcessEndTime());					
					item.getItemProperty(columnName_ProcessDurationInMillis).setValue(processObject.getProcessDurationInMillis());
				}				
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_ProcessOperations).setValue("");	
        	}
        }     
		return container;
	}
	
	private void addGeneratedColumns(int processStatus){
		addGeneratedColumn(columnName_ProcessObjectId, new Table.ColumnGenerator() {
			private static final long serialVersionUID = -4702242309131987421L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String objectNameString=(String)item.getItemProperty(columnName_ProcessObjectId).getValue();		            	
            	Label objectDisplayNameLabel= new Label("<span  style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectNameString+"</span>",Label.CONTENT_XHTML);
            	return objectDisplayNameLabel;
            }			
		 });
		if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
			addGeneratedColumn(columnName_CurrentProcessSteps, new Table.ColumnGenerator() { 
				private static final long serialVersionUID = 6251226039888789909L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		            Item item = getItem(itemId);
		            StringBuffer objectDisplayNameStringBuffer=new StringBuffer();
		            if(item.getItemProperty(columnName_CurrentProcessSteps).getValue()!=null){  
		            	List<ProcessStep> processSteps=(List<ProcessStep>)item.getItemProperty(columnName_CurrentProcessSteps).getValue();	            		
		            	for(ProcessStep processStep:processSteps){	            		
		            		objectDisplayNameStringBuffer.append(processStep.getStepName()+";");	            			
		            	}          		
		            }	            	
		            Label objectDisplayNameLabel= new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameStringBuffer.toString()+"</span>",Label.CONTENT_XHTML);
		            return objectDisplayNameLabel;
		            }
		    });	
		}		
		
		addGeneratedColumn(columnName_FinishedProcessSteps, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = 4018236623937602981L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	StringBuffer objectDisplayNameStringBuffer=new StringBuffer();
	            	if(item.getItemProperty(columnName_FinishedProcessSteps).getValue()!=null){
	            		List<HistoricProcessStep> processSteps=(List<HistoricProcessStep>)item.getItemProperty(columnName_FinishedProcessSteps).getValue();
	            		for(HistoricProcessStep historicProcessStep:processSteps){
	            			objectDisplayNameStringBuffer.append(historicProcessStep.getStepName()+";");	            			
	            		}	            		
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameStringBuffer.toString()+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	    });	
		
		if(processStatus!=ProcessSpace.PROCESS_STATUS_FINISHED){
			addGeneratedColumn(columnName_NextProcessSteps, new Table.ColumnGenerator() { 
				private static final long serialVersionUID = -7847467584321303334L;

					public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		            	Item item = getItem(itemId);
		            	StringBuffer objectDisplayNameStringBuffer=new StringBuffer();
		            	if(item.getItemProperty(columnName_NextProcessSteps).getValue()!=null){
		            		List<String> processSteps=(List<String>)item.getItemProperty(columnName_NextProcessSteps).getValue();
		            		for(String nextStep:processSteps){
		            			objectDisplayNameStringBuffer.append(nextStep+";");	            			
		            		}	            		
		            	}	            	
		            	Label objectDisplayNameLabel= new Label("<span style='color:#333333;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameStringBuffer.toString()+"</span>",Label.CONTENT_XHTML);
		            	return objectDisplayNameLabel;
		            }
		      });	
		}	
		
		addGeneratedColumn(columnName_ProcessStartUserId, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -7847467584321303334L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString=item.getItemProperty(columnName_ProcessStartUserId).getValue().toString();	            	         	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameString.toString()+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
		
		addGeneratedColumn(columnName_ProcessStartTime, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -984259220973751961L;

			public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            Item item = getItem(itemId);
	            Date objectDisplayNameString=(Date)item.getItemProperty(columnName_ProcessStartTime).getValue();	            	         	
	            Label objectDisplayNameLabel= new Label("<span style='color:#898989;'>"+formatter.format(objectDisplayNameString)+"</span>",Label.CONTENT_XHTML);
	            return objectDisplayNameLabel;
	           }
	    });	
		
		if(processStatus!=ProcessSpace.PROCESS_STATUS_UNFINISHED){
			addGeneratedColumn(columnName_ProcessEndTime, new Table.ColumnGenerator() { 		
				private static final long serialVersionUID = -7409590766876280770L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		            Item item = getItem(itemId);
		            if(item.getItemProperty(columnName_ProcessEndTime).getValue()!=null){
		            	 Date objectDisplayNameString=(Date)item.getItemProperty(columnName_ProcessEndTime).getValue();	            	         	
		 	            Label objectDisplayNameLabel= new Label("<span style='color:#898989;'>"+formatter.format(objectDisplayNameString)+"</span>",Label.CONTENT_XHTML);
		 	            return objectDisplayNameLabel;
		            }else{
		            	return new Label("");
		            }	           
		        }
		    });	
			
			addGeneratedColumn(columnName_ProcessDurationInMillis, new Table.ColumnGenerator() { 
				private static final long serialVersionUID = 3572062594338654808L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		            Item item = getItem(itemId);
		            if(item.getItemProperty(columnName_ProcessDurationInMillis).getValue()!=null){ 
		            	long duration=Long.parseLong(item.getItemProperty(columnName_ProcessDurationInMillis).getValue().toString());	            	            	         	
		 	            Label objectDisplayNameLabel= new Label("<span style='color:#898989;'>"+formatDuring(duration)+"</span>",Label.CONTENT_XHTML);
		 	            return objectDisplayNameLabel;
		            }else{
		            	return new Label("");
		            }	           
		        }
		    });				
		}
		
		
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -46210144282736859L;

			public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            return objectTypeIcon;
	        }
	     });
	}
	
	private String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);  
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
	    long seconds = (mss % (1000 * 60)) / 1000;  
	    return days + ""+this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_dayDisplay")+ 
	    	   hours + ""+this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_hourDisplay") + 
	    	   minutes + ""+this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_minuteDisplay")+ 
	    	   seconds + ""+this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectsTable_secondDisplay");  
	}  
}