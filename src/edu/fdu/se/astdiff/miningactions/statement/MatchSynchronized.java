package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.statement.SynchronizedChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchSynchronized {
	
	public static void matchSynchronized(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseIf(a, subActions, changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		SynchronizedChangeEntity code = new SynchronizedChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SYNCHRONIZED_STMT);
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(null);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.setActionTraversedMap(subActions);
		fp.addOneChangeEntity(code);
	}

	public static void matchSynchronizedChangeNewEntity(MiningActionData fp,Action a,Tree queryFather,int treeType,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> sameEdits = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,sameEdits,changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,queryFather,treeType);
		SynchronizedChangeEntity code = new SynchronizedChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		code.stageIIBean.setOpt(ChangeEntityDesc.StageIIIOpt.OPT_CHANGE);
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SYNCHRONIZED_STMT);
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(null);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.setActionTraversedMap(sameEdits);
		fp.addOneChangeEntity(code);

	}
	public static void matchSynchronizedChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,newActions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
//		changeEntity.linkBean.addAppendedActions(newActions);
		fp.setActionTraversedMap(newActions);

	}


}
