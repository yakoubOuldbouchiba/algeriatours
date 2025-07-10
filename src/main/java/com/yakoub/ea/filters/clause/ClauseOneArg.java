package com.yakoub.ea.filters.clause;





import com.yakoub.ea.filters.creator.AttribueCreator;
import com.yakoub.ea.filters.creator.JoinCreator;
import com.yakoub.ea.filters.enums.Operation;
import com.yakoub.ea.filters.factory.ValueFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.Objects;


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
                        return criteriaBuilder.like(criteriaBuilder.upper(root.get(attribute))  , "%"+ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()).toString().toUpperCase()+"%");
                    }else {
                        return criteriaBuilder.like(criteriaBuilder.upper(joinMap.get(attribute))  , "%"+ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()).toString().toUpperCase()+"%");
                    }
                case Notcontains:
                    if(joinMap == null){
                        return criteriaBuilder.notLike(criteriaBuilder.upper(root.get(attribute)) , "%"+ValueFactory.toValue(root, attribute ,clauseOneArg.getArg()).toString().toUpperCase()+"%");
                    }else {
                        return criteriaBuilder.notLike(criteriaBuilder.upper(joinMap.get(attribute))   , "%"+ValueFactory.toValue(joinMap, attribute ,clauseOneArg.getArg()).toString().toUpperCase()+"%");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClauseOneArg that = (ClauseOneArg) o;
        return Objects.equals(arg, that.arg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), arg);
    }
}
