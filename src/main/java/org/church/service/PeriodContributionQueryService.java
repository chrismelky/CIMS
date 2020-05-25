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

import org.church.domain.PeriodContribution;
import org.church.domain.*; // for static metamodels
import org.church.repository.PeriodContributionRepository;
import org.church.service.dto.PeriodContributionCriteria;

/**
 * Service for executing complex queries for {@link PeriodContribution} entities in the database.
 * The main input is a {@link PeriodContributionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeriodContribution} or a {@link Page} of {@link PeriodContribution} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodContributionQueryService extends QueryService<PeriodContribution> {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionQueryService.class);

    private final PeriodContributionRepository periodContributionRepository;

    public PeriodContributionQueryService(PeriodContributionRepository periodContributionRepository) {
        this.periodContributionRepository = periodContributionRepository;
    }

    /**
     * Return a {@link List} of {@link PeriodContribution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodContribution> findByCriteria(PeriodContributionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PeriodContribution> specification = createSpecification(criteria);
        return periodContributionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PeriodContribution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContribution> findByCriteria(PeriodContributionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PeriodContribution> specification = createSpecification(criteria);
        return periodContributionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodContributionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PeriodContribution> specification = createSpecification(criteria);
        return periodContributionRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodContributionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PeriodContribution> createSpecification(PeriodContributionCriteria criteria) {
        Specification<PeriodContribution> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PeriodContribution_.id));
            }
            if (criteria.getAmountPromised() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountPromised(), PeriodContribution_.amountPromised));
            }
            if (criteria.getAmountContributed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountContributed(), PeriodContribution_.amountContributed));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PeriodContribution_.description));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), PeriodContribution_.dueDate));
            }
            if (criteria.getPeriodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodId(),
                    root -> root.join(PeriodContribution_.period, JoinType.LEFT).get(Period_.id)));
            }
            if (criteria.getMemberPromiseId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberPromiseId(),
                    root -> root.join(PeriodContribution_.memberPromise, JoinType.LEFT).get(MemberPromise_.id)));
            }
        }
        return specification;
    }
}
