package com.TrungTinhBackend.inventory_backend.specification;

import com.TrungTinhBackend.inventory_backend.models.Transaction;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class TransactionFilter {

    public static Specification<Transaction> byFilter(String searchValue) {

        return(root, query, criteriaBuilder) -> {
            if(searchValue == null || searchValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%"+searchValue.toLowerCase()+"%";
            // create a list to hold my predicates
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("note")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("transaction").as(String.class)), searchPattern));

            if(root.getJoins().stream().noneMatch(j ->j.getAttribute().getName().equals("user"))) {
                root.join("user", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("email")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("phoneNumber")),searchPattern));

            if(root.getJoins().stream().noneMatch(j ->j.getAttribute().getName().equals("supplier"))) {
                root.join("supplier", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("supplier",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("supplier",JoinType.LEFT).get("contactInfo")),searchPattern));

            if(root.getJoins().stream().noneMatch(j ->j.getAttribute().getName().equals("product"))) {
                root.join("product", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("sku")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("description")),searchPattern));

            if(root.getJoins().stream().noneMatch(j ->j.getAttribute().getName().equals("product")) &&
            root.join("product").getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("category"))) {
                root.join("product", JoinType.LEFT).join("category",JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).join("category",JoinType.LEFT).get("name")),searchPattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

    }
    public static Specification<Transaction> byMonthAndYear(int month,int year) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> monthExpression = criteriaBuilder.function("month",Integer.class,root.get("createdAt"));
            Expression<Integer> yearExpression = criteriaBuilder.function("year",Integer.class,root.get("createdAt"));

            Predicate monthPredicate = criteriaBuilder.equal(monthExpression,month);
            Predicate yearPredicate = criteriaBuilder.equal(yearExpression,year);

            return criteriaBuilder.and(monthPredicate,yearPredicate);
        };
    }
}
