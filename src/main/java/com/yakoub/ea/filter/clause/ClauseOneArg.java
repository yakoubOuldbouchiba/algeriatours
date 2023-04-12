package com.yakoub.ea.filter.clause;

import com.yakoub.ea.filter.enums.Operation;
import com.yakoub.ea.filter.creator.AttribueCreator;
import com.yakoub.ea.filter.creator.JoinCreator;
import com.yakoub.ea.filter.factory.ValueFactory;

import javax.persistence.criteria.*;
import java.text.ParseException;


public class ClauseOneArg  extends Clause {

    private String arg;



    public ClauseOneArg(String filed, Operation operation, String arg) {
        super(filed, operation);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

     public static Predicate toPredicate(Root root , CriteriaBuilder criteriaBuilder , Clause clause) throws ParseException, ClassNotFoundException {
        ClauseOneArg clauseOneArg = (ClauseOneArg) clause;
        Join joinMap  = JoinCreator.createJoin(root , clause.getFiled());
        String attribute = AttribueCreator.createAttribute(clause.getFiled());
        try {
            switch (clauseOneArg.getOperation()){
                case NotEquals:
                    if(joinMap == null){
                        return criteriaBuilder.notEqual(root.get(attribute)  , ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.notEqual(joinMap.get(attribute) , ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
                case Endswith:
                    if(joinMap == null){
                        return criteriaBuilder.like(root.get(attribute)  , "%"+ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.like(joinMap.get(attribute)  , "%"+ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
                case Startswith:
                    if(joinMap == null){
                        return criteriaBuilder.like(root.get(attribute)  , ValueFactory.toValue(root, attribute ,clauseOneArg.getArg())+"%");
                    }else {
                        return criteriaBuilder.like(joinMap.get(attribute)  , clauseOneArg.getArg()+"%");
                    }
                case Contains:
                    if(joinMap == null){
                        return criteriaBuilder.like(root.get(attribute)  , "%"+ValueFactory.toValue(root, attribute ,clauseOneArg.getArg())+"%");
                    }else {
                        return criteriaBuilder.like(joinMap.get(attribute)  , "%"+ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg())+"%");
                    }
                case Notcontains:
                    if(joinMap == null){
                        return criteriaBuilder.notLike(root.get(attribute)  , "%"+ValueFactory.toValue(root, attribute ,clauseOneArg.getArg())+"%");
                    }else {
                        return criteriaBuilder.notLike(joinMap.get(attribute)  , "%"+ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg())+"%");
                    }
                case Less:
                    if(joinMap == null){
                        return criteriaBuilder.lessThan(root.get(attribute)  ,  (Comparable) ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.lessThan(joinMap.get(attribute)  , (Comparable) ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }

                case LessOrEquals:
                    if(joinMap == null){
                        return criteriaBuilder.lessThanOrEqualTo(root.get(attribute)  ,  (Comparable) ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.lessThanOrEqualTo(joinMap.get(attribute)  , (Comparable) ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
                case Greater:
                    if(joinMap == null){
                        return criteriaBuilder.greaterThan(root.get(attribute)  , (Comparable) ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.greaterThan(joinMap.get(attribute)  , (Comparable) ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
                case GreaterOrEquals:
                    if(joinMap == null){
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(attribute)  , (Comparable) ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.greaterThanOrEqualTo(joinMap.get(attribute)  , (Comparable) ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
                default:
                    if(joinMap == null){
                        return criteriaBuilder.equal(root.get(attribute)  , ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()));
                    }else {
                        return criteriaBuilder.equal(joinMap.get(attribute)  , ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()));
                    }
            }
        }catch (IllegalArgumentException | ClassNotFoundException illegalArgumentException){
            throw  illegalArgumentException;
        }
    }

}
