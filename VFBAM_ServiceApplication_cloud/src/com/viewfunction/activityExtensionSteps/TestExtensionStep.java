package com.viewfunction.activityExtensionSteps;

import java.util.ArrayList;
import java.util.List;

import com.viewfunction.activityEngine.extension.ActivityStepCustomExtension;
import com.viewfunction.activityEngine.extension.ActivityStepContext;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;

public class TestExtensionStep  extends ActivityStepCustomExtension{

	@Override
	protected void executeActivityStepExtensionLogic(ActivityStepContext activityStepContext) {
		/*
		System.out.println("Execute TestExtensionStep executeActivityStepExtensionLogic");	
		System.out.println("-----------------------------------------");
		System.out.println(activityStepContext.getProcessSpaceName());
		System.out.println(activityStepContext.getProcessDefinitionId());
		System.out.println(activityStepContext.getProcessObjectId());
		System.out.println(activityStepContext.getProcessStepId());
		System.out.println(activityStepContext.getProcessStepName());
		System.out.println(activityStepContext.getProcessType());
		System.out.println("-----------------------------------------");
		String activityInstanceFolderPath=activityStepContext.getApplicationSpaceDocumentFolderPath();
		System.out.println(activityInstanceFolderPath);
		*/
		String currentBusinessActivityDocumentFolderPath=activityStepContext.getCurrentBusinessActivityDocumentFolder();		
		String activitySpaceName =activityStepContext.getActivitySpaceName();		
		ContentSpace activityContentSpace = null;		
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(activitySpaceName);			
			BaseContentObject activityInstanceFolderRootContentObject=activityContentSpace.getContentObjectByAbsPath(currentBusinessActivityDocumentFolderPath);				
			
			SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();			
			List<PermissionObject> permissionObjectList =new ArrayList<PermissionObject>();			
			PermissionObject creatorPermission=ContentComponentFactory.createPermissionObject();
			creatorPermission.setAddContentPermission(true);
			creatorPermission.setAddSubFolderPermission(true);
			creatorPermission.setConfigPermissionPermission(true);
			creatorPermission.setDeleteContentPermission(true);
			creatorPermission.setDeleteSubFolderPermission(true);
			creatorPermission.setDisplayContentPermission(true);
			creatorPermission.setEditContentPermission(true);
			creatorPermission.setPermissionScope(PermissionObject.PermissionScope_Owner);
			permissionObjectList.add(creatorPermission);			
			PermissionObject otherPermission=ContentComponentFactory.createPermissionObject();
			otherPermission.setAddContentPermission(false);
			otherPermission.setAddSubFolderPermission(false);
			otherPermission.setConfigPermissionPermission(false);
			otherPermission.setDeleteContentPermission(false);
			otherPermission.setDeleteSubFolderPermission(false);
			otherPermission.setDisplayContentPermission(true);
			otherPermission.setEditContentPermission(true);
			otherPermission.setPermissionScope(PermissionObject.PermissionScope_Other);
			permissionObjectList.add(otherPermission);			
			soh.setContentPermissions(activityInstanceFolderRootContentObject, permissionObjectList);			
			
			BaseContentObject projectManagementObject=activityInstanceFolderRootContentObject.addSubContentObject("项目管理", null, false);			
			soh.setContentPermissions(projectManagementObject, buildCustomPermissionList());			
				BaseContentObject planDesignObject=projectManagementObject.addSubContentObject("设计策划", null, false);
				soh.setContentPermissions(planDesignObject, buildCustomPermissionList());			
				BaseContentObject planSchedulerObject=projectManagementObject.addSubContentObject("计划表", null, false);
				soh.setContentPermissions(planSchedulerObject, buildCustomPermissionList());
				BaseContentObject contactObject=projectManagementObject.addSubContentObject("通讯录", null, false);
				soh.setContentPermissions(contactObject, buildCustomPermissionList());
			
			BaseContentObject designInputObject=activityInstanceFolderRootContentObject.addSubContentObject("设计输入", null, false);	
			soh.setContentPermissions(designInputObject, buildCustomPermissionList());			
				BaseContentObject planConditionObject=designInputObject.addSubContentObject("规划条件", null, false);
				soh.setContentPermissions(planConditionObject, buildCustomPermissionList());			
				BaseContentObject fieldDetectObject=designInputObject.addSubContentObject("现场勘探", null, false);
				soh.setContentPermissions(fieldDetectObject, buildCustomPermissionList());			
				BaseContentObject ownerConditionObject=designInputObject.addSubContentObject("业主条件", null, false);
				soh.setContentPermissions(ownerConditionObject, buildCustomPermissionList());			
				BaseContentObject memoObject=designInputObject.addSubContentObject("会议纪要，设计备忘", null, false);
				soh.setContentPermissions(memoObject, buildCustomPermissionList());			
				BaseContentObject techStandObject=designInputObject.addSubContentObject("技术标准，法规政策", null, false);
				soh.setContentPermissions(techStandObject, buildCustomPermissionList());			
				BaseContentObject referanceObject=designInputObject.addSubContentObject("参考资料", null, false);
				soh.setContentPermissions(referanceObject, buildCustomPermissionList());			
			
			BaseContentObject exchangeDocumentsObject=activityInstanceFolderRootContentObject.addSubContentObject("往来文件", null, false);	
			soh.setContentPermissions(exchangeDocumentsObject, buildCustomPermissionList());				
				BaseContentObject customerObject=exchangeDocumentsObject.addSubContentObject("甲方", null, false);	
				soh.setContentPermissions(customerObject, buildCustomPermissionList());				
					BaseContentObject receiveObject=customerObject.addSubContentObject("收", null, false);	
					soh.setContentPermissions(receiveObject, buildCustomPermissionList());				
					BaseContentObject sendObject=customerObject.addSubContentObject("发", null, false);	
					soh.setContentPermissions(sendObject, buildCustomPermissionList());				
			
			BaseContentObject designDocumentsObject=activityInstanceFolderRootContentObject.addSubContentObject("设计文件", null, false);	
			soh.setContentPermissions(designDocumentsObject, buildCustomPermissionList());			
				BaseContentObject archObject=designDocumentsObject.addSubContentObject("建筑", null, false);	
				soh.setContentPermissions(archObject, buildCustomPermissionList());				
					BaseContentObject perDesignObject=archObject.addSubContentObject("前期方案", null, false);	
					soh.setContentPermissions(perDesignObject, buildCustomPermissionList());
						BaseContentObject resultAndMemoObject=perDesignObject.addSubContentObject("成果汇报文件，会议纪要，设计备忘", null, false);	
						soh.setContentPermissions(resultAndMemoObject, buildFreePermissionList());
						BaseContentObject picAndModelObject=perDesignObject.addSubContentObject("图片（草图，意向图，模型生成图）", null, false);	
						soh.setContentPermissions(picAndModelObject, buildFreePermissionList());
						BaseContentObject cadFileObject=perDesignObject.addSubContentObject("CAD文件（含打印格式，生成图）", null, false);	
						soh.setContentPermissions(cadFileObject, buildFreePermissionList());
						BaseContentObject skpFileObject=perDesignObject.addSubContentObject("skp模型（含生成图）", null, false);	
						soh.setContentPermissions(skpFileObject, buildFreePermissionList());
						BaseContentObject renderEffectObject=perDesignObject.addSubContentObject("效果图（含3Dmax模型）", null, false);	
						soh.setContentPermissions(renderEffectObject, buildFreePermissionList());
						BaseContentObject textAndSheetObject=perDesignObject.addSubContentObject("文本及表格（说明，计算书等）", null, false);	
						soh.setContentPermissions(textAndSheetObject, buildFreePermissionList());
						BaseContentObject otherObject=perDesignObject.addSubContentObject("其他", null, false);	
						soh.setContentPermissions(otherObject, buildFreePermissionList());
					BaseContentObject projectDesignObject=archObject.addSubContentObject("工程设计", null, false);	
					soh.setContentPermissions(projectDesignObject, buildCustomPermissionList());
					BaseContentObject factorCalcuObject=archObject.addSubContentObject("指标计算", null, false);	
					soh.setContentPermissions(factorCalcuObject, buildCustomPermissionList());
					BaseContentObject postCopertionObject=archObject.addSubContentObject("后期配合", null, false);	
					soh.setContentPermissions(postCopertionObject, buildCustomPermissionList());
					BaseContentObject stageBackupObject=archObject.addSubContentObject("阶段备份", null, false);	
					soh.setContentPermissions(stageBackupObject, buildCustomPermissionList());
				BaseContentObject structureObject=designDocumentsObject.addSubContentObject("结构", null, false);	
				soh.setContentPermissions(structureObject, buildCustomPermissionList());
					BaseContentObject draftDesignObject=structureObject.addSubContentObject("初步设计", null, false);	
					soh.setContentPermissions(draftDesignObject, buildCustomPermissionList());
					BaseContentObject blutprintDesignObject=structureObject.addSubContentObject("施工图设计", null, false);	
					soh.setContentPermissions(blutprintDesignObject, buildCustomPermissionList());
					BaseContentObject stageBackup_2Object=structureObject.addSubContentObject("阶段备份", null, false);	
					soh.setContentPermissions(stageBackup_2Object, buildCustomPermissionList());
				BaseContentObject equWaterObject=designDocumentsObject.addSubContentObject("设备-给排水", null, false);	
				soh.setContentPermissions(equWaterObject, buildCustomPermissionList());
					BaseContentObject methodDesignObject=equWaterObject.addSubContentObject("方案设计", null, false);	
					soh.setContentPermissions(methodDesignObject, buildCustomPermissionList());
					BaseContentObject draftDesign_2Object=equWaterObject.addSubContentObject("初步设计", null, false);	
					soh.setContentPermissions(draftDesign_2Object, buildCustomPermissionList());
					BaseContentObject blutprintDesign_2Object=equWaterObject.addSubContentObject("施工图设计", null, false);	
					soh.setContentPermissions(blutprintDesign_2Object, buildCustomPermissionList());
					BaseContentObject deployCorpObject=equWaterObject.addSubContentObject("施工配合", null, false);	
					soh.setContentPermissions(deployCorpObject, buildCustomPermissionList());
					BaseContentObject factoryCorpObject=equWaterObject.addSubContentObject("厂家配合", null, false);	
					soh.setContentPermissions(factoryCorpObject, buildCustomPermissionList());
					BaseContentObject stageBackup_3Object=equWaterObject.addSubContentObject("阶段备份", null, false);	
					soh.setContentPermissions(stageBackup_3Object, buildCustomPermissionList());
				BaseContentObject equWarmObject=designDocumentsObject.addSubContentObject("设备-暖通", null, false);	
				soh.setContentPermissions(equWarmObject, buildCustomPermissionList());
					BaseContentObject methodDesign_1Object=equWarmObject.addSubContentObject("方案设计", null, false);	
					soh.setContentPermissions(methodDesign_1Object, buildCustomPermissionList());
					BaseContentObject draftDesign_3Object=equWarmObject.addSubContentObject("初步设计", null, false);	
					soh.setContentPermissions(draftDesign_3Object, buildCustomPermissionList());
					BaseContentObject blutprintDesign_3Object=equWarmObject.addSubContentObject("施工图设计", null, false);	
					soh.setContentPermissions(blutprintDesign_3Object, buildCustomPermissionList());
					BaseContentObject deployCorp_1Object=equWarmObject.addSubContentObject("施工配合", null, false);	
					soh.setContentPermissions(deployCorp_1Object, buildCustomPermissionList());
					BaseContentObject factoryCorp_1Object=equWarmObject.addSubContentObject("厂家配合", null, false);	
					soh.setContentPermissions(factoryCorp_1Object, buildCustomPermissionList());
					BaseContentObject stageBackup_4Object=equWarmObject.addSubContentObject("阶段备份", null, false);	
					soh.setContentPermissions(stageBackup_4Object, buildCustomPermissionList());					
				BaseContentObject electObject=designDocumentsObject.addSubContentObject("电气", null, false);	
				soh.setContentPermissions(electObject, buildCustomPermissionList());
					BaseContentObject methodDesign_2Object=electObject.addSubContentObject("方案设计", null, false);	
					soh.setContentPermissions(methodDesign_2Object, buildCustomPermissionList());
					BaseContentObject draftDesign_4Object=electObject.addSubContentObject("初步设计", null, false);	
					soh.setContentPermissions(draftDesign_4Object, buildCustomPermissionList());
					BaseContentObject blutprintDesign_4Object=electObject.addSubContentObject("施工图设计", null, false);	
					soh.setContentPermissions(blutprintDesign_4Object, buildCustomPermissionList());			
					BaseContentObject stageBackup_5Object=electObject.addSubContentObject("阶段备份", null, false);	
					soh.setContentPermissions(stageBackup_5Object, buildCustomPermissionList());	
				BaseContentObject ecoyObject=designDocumentsObject.addSubContentObject("经济", null, false);	
				soh.setContentPermissions(ecoyObject, buildCustomPermissionList());		
				
			BaseContentObject parOutlineObject=activityInstanceFolderRootContentObject.addSubContentObject("竖向外线", null, false);	
			soh.setContentPermissions(parOutlineObject, buildCustomPermissionList());	
			
			BaseContentObject archDocumentObject=activityInstanceFolderRootContentObject.addSubContentObject("归档文件", null, false);	
			soh.setContentPermissions(archDocumentObject, buildCustomPermissionList());
				BaseContentObject bitPlanArcObject=archDocumentObject.addSubContentObject("投标方案归档", null, false);	
				soh.setContentPermissions(bitPlanArcObject, buildCustomPermissionList());
				BaseContentObject reportPlanArcObject=archDocumentObject.addSubContentObject("报规方案归档", null, false);	
				soh.setContentPermissions(reportPlanArcObject, buildCustomPermissionList());				
				BaseContentObject draftDesignArcObject=archDocumentObject.addSubContentObject("初步设计归档", null, false);	
				soh.setContentPermissions(draftDesignArcObject, buildCustomPermissionList());				
				BaseContentObject blueprintArcObject=archDocumentObject.addSubContentObject("施工图归档", null, false);	
				soh.setContentPermissions(blueprintArcObject, buildCustomPermissionList());				
				BaseContentObject deployCorpArcObject=archDocumentObject.addSubContentObject("施工配合归档", null, false);	
				soh.setContentPermissions(deployCorpArcObject, buildCustomPermissionList());				
				BaseContentObject finishPicArcObject=archDocumentObject.addSubContentObject("竣工图归档", null, false);	
				soh.setContentPermissions(finishPicArcObject, buildCustomPermissionList());				
				BaseContentObject conceptionReviewArcObject=archDocumentObject.addSubContentObject("规划及概念方案评审记录", null, false);	
				soh.setContentPermissions(conceptionReviewArcObject, buildCustomPermissionList());				
				BaseContentObject planReviewArcObject=archDocumentObject.addSubContentObject("方案评审记录", null, false);	
				soh.setContentPermissions(planReviewArcObject, buildCustomPermissionList());				
				BaseContentObject draftDesignReviewArcObject=archDocumentObject.addSubContentObject("初设评审和验证记录", null, false);	
				soh.setContentPermissions(draftDesignReviewArcObject, buildCustomPermissionList());				
				BaseContentObject deployPicReviewArcObject=archDocumentObject.addSubContentObject("施工图验证记录", null, false);	
				soh.setContentPermissions(deployPicReviewArcObject, buildCustomPermissionList());				
				BaseContentObject deployPicOutReviewArcObject=archDocumentObject.addSubContentObject("施工图外审记录", null, false);	
				soh.setContentPermissions(deployPicOutReviewArcObject, buildCustomPermissionList());				
				BaseContentObject sunshineCaltArcObject=archDocumentObject.addSubContentObject("日照计算成果", null, false);	
				soh.setContentPermissions(sunshineCaltArcObject, buildCustomPermissionList());				
				BaseContentObject savingEngyArcObject=archDocumentObject.addSubContentObject("节能计算成果", null, false);	
				soh.setContentPermissions(savingEngyArcObject, buildCustomPermissionList());				
				BaseContentObject warProofApplyArcObject=archDocumentObject.addSubContentObject("人防申报", null, false);	
				soh.setContentPermissions(warProofApplyArcObject, buildCustomPermissionList());				
				BaseContentObject traficApplyArcObject=archDocumentObject.addSubContentObject("交通申报", null, false);	
				soh.setContentPermissions(traficApplyArcObject, buildCustomPermissionList());				
				BaseContentObject greenlandApplyArcObject=archDocumentObject.addSubContentObject("绿化园林申报", null, false);	
				soh.setContentPermissions(greenlandApplyArcObject, buildCustomPermissionList());				
				BaseContentObject construcApplyArcObject=archDocumentObject.addSubContentObject("报建设工程规划许可证", null, false);	
				soh.setContentPermissions(construcApplyArcObject, buildCustomPermissionList());				
				BaseContentObject fireProofApplyArcObject=archDocumentObject.addSubContentObject("消防申报", null, false);	
				soh.setContentPermissions(fireProofApplyArcObject, buildCustomPermissionList());				
				BaseContentObject waterConsumeApplyArcObject=archDocumentObject.addSubContentObject("报用水量", null, false);	
				soh.setContentPermissions(waterConsumeApplyArcObject, buildCustomPermissionList());			
			BaseContentObject projectSummaryObject=activityInstanceFolderRootContentObject.addSubContentObject("项目总结", null, false);	
			soh.setContentPermissions(projectSummaryObject, buildCustomPermissionList());			
				BaseContentObject projectSummary_1Object=projectSummaryObject.addSubContentObject("项目总结", null, false);	
				soh.setContentPermissions(projectSummary_1Object, buildCustomPermissionList());
				BaseContentObject projectReviewObject=projectSummaryObject.addSubContentObject("项目现场及回访", null, false);	
				soh.setContentPermissions(projectReviewObject, buildCustomPermissionList());
				BaseContentObject projectReportObject=projectSummaryObject.addSubContentObject("报优资料", null, false);	
				soh.setContentPermissions(projectReportObject, buildCustomPermissionList());			
			BaseContentObject bimDocumentObject=activityInstanceFolderRootContentObject.addSubContentObject("BIM", null, false);	
			soh.setContentPermissions(bimDocumentObject, buildCustomPermissionList());
				BaseContentObject bimAdminObject=bimDocumentObject.addSubContentObject("BIM Admin", null, false);	
				soh.setContentPermissions(bimAdminObject, buildCustomPermissionList());
				BaseContentObject bimModelObject=bimDocumentObject.addSubContentObject("BIM模型", null, false);	
				soh.setContentPermissions(bimModelObject, buildCustomPermissionList());
				BaseContentObject outReferanceObject=bimDocumentObject.addSubContentObject("外部引用", null, false);	
				soh.setContentPermissions(outReferanceObject, buildCustomPermissionList());
				BaseContentObject projectClusterObject=bimDocumentObject.addSubContentObject("项目族库", null, false);	
				soh.setContentPermissions(projectClusterObject, buildCustomPermissionList());
				BaseContentObject stageBackup_6Object=bimDocumentObject.addSubContentObject("阶段备份", null, false);	
				soh.setContentPermissions(stageBackup_6Object, buildCustomPermissionList());
				BaseContentObject getPriObject=bimDocumentObject.addSubContentObject("提资", null, false);	
				soh.setContentPermissions(getPriObject, buildCustomPermissionList());
				BaseContentObject outputObject=bimDocumentObject.addSubContentObject("输出", null, false);	
				soh.setContentPermissions(outputObject, buildCustomPermissionList());			
		} catch (ContentReposityException e) {				
			e.printStackTrace();
		}finally{
			if(activityContentSpace!=null){
				activityContentSpace.closeContentSpace();
			}				
		}		
	}
	
	private List<PermissionObject> buildCustomPermissionList(){
		List<PermissionObject> permissionObjectList =new ArrayList<PermissionObject>();		
		PermissionObject creatorPermission=ContentComponentFactory.createPermissionObject();
		creatorPermission.setAddContentPermission(true);
		creatorPermission.setAddSubFolderPermission(true);
		creatorPermission.setConfigPermissionPermission(true);
		creatorPermission.setDeleteContentPermission(true);
		creatorPermission.setDeleteSubFolderPermission(true);
		creatorPermission.setDisplayContentPermission(true);
		creatorPermission.setEditContentPermission(true);
		creatorPermission.setPermissionScope(PermissionObject.PermissionScope_Owner);
		permissionObjectList.add(creatorPermission);		
		PermissionObject otherPermission=ContentComponentFactory.createPermissionObject();
		otherPermission.setAddContentPermission(true);
		otherPermission.setDeleteContentPermission(true);
		otherPermission.setAddSubFolderPermission(false);
		otherPermission.setDeleteSubFolderPermission(false);		
		otherPermission.setConfigPermissionPermission(false);		
		otherPermission.setDisplayContentPermission(true);
		otherPermission.setEditContentPermission(false);
		otherPermission.setPermissionScope(PermissionObject.PermissionScope_Other);
		permissionObjectList.add(otherPermission);		
		return permissionObjectList;		
	}
	
	private List<PermissionObject> buildFreePermissionList(){
		List<PermissionObject> permissionObjectList =new ArrayList<PermissionObject>();		
		PermissionObject creatorPermission=ContentComponentFactory.createPermissionObject();
		creatorPermission.setAddContentPermission(true);
		creatorPermission.setAddSubFolderPermission(true);
		creatorPermission.setConfigPermissionPermission(true);
		creatorPermission.setDeleteContentPermission(true);
		creatorPermission.setDeleteSubFolderPermission(true);
		creatorPermission.setDisplayContentPermission(true);
		creatorPermission.setEditContentPermission(true);
		creatorPermission.setPermissionScope(PermissionObject.PermissionScope_Owner);
		permissionObjectList.add(creatorPermission);		
		PermissionObject otherPermission=ContentComponentFactory.createPermissionObject();
		otherPermission.setAddContentPermission(true);
		otherPermission.setDeleteContentPermission(true);
		otherPermission.setAddSubFolderPermission(true);
		otherPermission.setDeleteSubFolderPermission(true);		
		otherPermission.setConfigPermissionPermission(true);		
		otherPermission.setDisplayContentPermission(true);
		otherPermission.setEditContentPermission(false);
		otherPermission.setPermissionScope(PermissionObject.PermissionScope_Other);
		permissionObjectList.add(otherPermission);		
		return permissionObjectList;		
	}	
}