package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class SwitchChangeEntity extends StatementPlusChangeEntity {
    final static public String switchStatement = "Switch";
    final static public String switchCase = "Switch_Case";
    final static public String defaultCase = "Default SwitchCase";

    public SwitchChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }



}
