package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class SynchronizedChangeEntity extends StatementPlusChangeEntity {
    public SynchronizedChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }

    public void generateDesc(){

    }

    @Override
    public String toString(){
        return this.outputDesc;
    }
}
