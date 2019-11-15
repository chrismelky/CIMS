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

import org.church.domain.MemberContribution;
import org.church.domain.*; // for static metamodels
import org.church.repository.MemberContributionRepository;
import org.church.service.dto.MemberContributionCriteria;

/**
 * Service for executing complex queries for {@link MemberContribution} entities in the database.
 * The main input is a {@link MemberContributionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberContribution} or a {@link Page} of {@link MemberContribution} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberContributionQueryService extends QueryService<MemberContribution> {

    private final Logger log = LoggerFactory.getLogger(MemberContributionQueryService.class);

    private final MemberContributionRepository memberContributionRepository;

    public MemberContributionQueryService(MemberContributionRepository memberContributionRepository) {
        this.memberContributionRepository = memberContributionRepository;
    }

    /**
     * Return a {@link List} of {@link MemberContribution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberContribution> findByCriteria(MemberContributionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MemberContribution> specification = createSpecification(criteria);
        return memberContributionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MemberContribution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberContribution> findByCriteria(MemberContributionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MemberContribution> specification = createSpecification(criteria);
        return memberContributionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberContributionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MemberContribution> specification = createSpecification(criteria);
        return memberContributionRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberContributionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MemberContribution> createSpecification(MemberContributionCriteria criteria) {
        Specification<MemberContribution> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MemberContribution_.id));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), MemberContribution_.paymentDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), MemberContribution_.amount));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberId(),
                    root -> root.join(MemberContribution_.member, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(MemberContribution_.church, JoinType.LEFT).get(Church_.id)));
            }
            if (criteria.getPaymentMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethodId(),
                    root -> root.join(MemberContribution_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)));
            }
            if (criteria.getPromiseId() != null) {
                specification = specification.and(buildSpecification(criteria.getPromiseId(),
                    root -> root.join(MemberContribution_.promise, JoinType.LEFT).get(MemberPromise_.id)));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(MemberContribution_.type, JoinType.LEFT).get(ContributionType_.id)));
            }
        }
        return specification;
    }
}
