package hu._ig.crm.crm4ig.specification;

import hu._ig.crm.crm4ig.domain.Partner;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PartnerSpecification {

    public static final String ADDRESSES = "addresses";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String STREET = "street";
    public static final String HOUSE_NUMBER = "houseNumber";

    public static Specification<Partner> getPartnersByAddress(String country, String city, String street, String houseNumber) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (country != null) {
                predicates.add(criteriaBuilder.equal(root.join(ADDRESSES, JoinType.LEFT).get(COUNTRY), country));
            }
            if (city != null) {
                predicates.add(criteriaBuilder.equal(root.join(ADDRESSES, JoinType.LEFT).get(CITY), city));
            }
            if (street != null) {
                predicates.add(criteriaBuilder.equal(root.join(ADDRESSES, JoinType.LEFT).get(STREET), street));
            }
            if (houseNumber != null) {
                predicates.add(criteriaBuilder.equal(root.join(ADDRESSES, JoinType.LEFT).get(HOUSE_NUMBER), houseNumber));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
