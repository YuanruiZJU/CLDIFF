package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
/**
 * 记录find时候找到的节点，以及对应的fafafather 节点，以及该节点下，所有的action
 * @author huangkaifeng
 *
 */
public class HighLevelOperationBean {
	
	public ITree curNode;
	public Action curAction;
	public String curNodeType;
	public List<Action> actions;
	//insert delete change
	public String operationType;
	// if 
	public String operationEntity;

	public ITree fatherNode;
	public String fatherNodeType;
	public HighLevelOperationBean(Action curAction,String curNodeType,List<Action> actions,String operationType,String operationEntity,ITree fatherNode,String fatherNodeType){
		this.curAction = curAction;
		this.curNode = curAction.getNode();
		this.curNodeType = curNodeType;
		this.actions = actions;
		this.operationType = operationType;
		this.operationEntity = operationEntity;
		this.fatherNode = fatherNode;
		this.fatherNodeType = fatherNodeType;
	}
	
	public ITree getCurNode() {
		return curNode;
	}
	public List<Action> getActions() {
		return actions;
	}
	public String getOperationType() {
		return operationType;
	}
	public String getOperationEntity() {
		return operationEntity;
	}

	public String getCurNodeType() {
		return curNodeType;
	}

	public ITree getFatherNode() {
		return fatherNode;
	}

	public String getFatherNodeType() {
		return fatherNodeType;
	}

	public Action getCurAction() {
		return curAction;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}