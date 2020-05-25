package org.church.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.church.domain.Member;
import org.church.domain.*; // for static metamodels
import org.church.repository.MemberRepository;
import org.church.service.dto.MemberCriteria;

/**
 * Service for executing complex queries for {@link Member} entities in the database.
 * The main input is a {@link MemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Member} or a {@link Page} of {@link Member} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberQueryService extends QueryService<Member> {

    private final Logger log = LoggerFactory.getLogger(MemberQueryService.class);

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Return a {@link List} of {@link Member} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Member> findByCriteria(MemberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Member} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> findByCriteria(MemberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Member> createSpecification(MemberCriteria criteria) {
        Specification<Member> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Member_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Member_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Member_.lastName));
            }if (criteria.getChurchRn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChurchRn(), Member_.churchRn));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Member_.middleName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Member_.gender));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Member_.phoneNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Member_.email));
            }
            if (criteria.getDateOfBith() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBith(), Member_.dateOfBirth));
            }
            if (criteria.getPlaceOfBith() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaceOfBith(), Member_.placeOfBirth));
            }
            if (criteria.getMaritalStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getMaritalStatus(), Member_.maritalStatus));
            }
            if (criteria.getWork() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWork(), Member_.work));
            }
            if (criteria.getPlaceOfWork() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaceOfWork(), Member_.placeOfWork));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Member_.isActive));
            }
            if (criteria.getIsDeceased() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeceased(), Member_.isDeceased));
            }
            if (criteria.getDeceasedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeceasedDate(), Member_.deceasedDate));
            }
            if (criteria.getRelativesId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelativesId(),
                    root -> root.join(Member_.relatives, JoinType.LEFT).get(MemberRelative_.id)));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(Member_.church, JoinType.LEFT).get(Church_.id)));
            }
            if (criteria.getChurchCommunitiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchCommunitiesId(),
                    root -> root.join(Member_.churchCommunities, JoinType.LEFT).get(ChurchCommunity_.id)));
            }
            if (criteria.getMemberRitesId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberRitesId(),
                    root -> root.join(Member_.memberRites, JoinType.LEFT).get(MemberRite_.id)));
            }if (criteria.getHomeChurchCommunityId() != null) {
                specification = specification.and(buildSpecification(criteria.getHomeChurchCommunityId(),
                    root -> root.join(Member_.homeChurchCommunity, JoinType.LEFT).get(HomeChurchCommunity_.id)));
            }
        }
        return specification;
    }
}
