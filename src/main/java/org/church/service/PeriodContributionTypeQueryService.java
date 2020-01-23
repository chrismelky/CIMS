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

import org.church.domain.PeriodContributionType;
import org.church.domain.*; // for static metamodels
import org.church.repository.PeriodContributionTypeRepository;
import org.church.service.dto.PeriodContributionTypeCriteria;

/**
 * Service for executing complex queries for {@link PeriodContributionType} entities in the database.
 * The main input is a {@link PeriodContributionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeriodContributionType} or a {@link Page} of {@link PeriodContributionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodContributionTypeQueryService extends QueryService<PeriodContributionType> {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionTypeQueryService.class);

    private final PeriodContributionTypeRepository periodContributionTypeRepository;

    public PeriodContributionTypeQueryService(PeriodContributionTypeRepository periodContributionTypeRepository) {
        this.periodContributionTypeRepository = periodContributionTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PeriodContributionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodContributionType> findByCriteria(PeriodContributionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PeriodContributionType> specification = createSpecification(criteria);
        return periodContributionTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PeriodContributionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContributionType> findByCriteria(PeriodContributionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PeriodContributionType> specification = createSpecification(criteria);
        return periodContributionTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodContributionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PeriodContributionType> specification = createSpecification(criteria);
        return periodContributionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodContributionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PeriodContributionType> createSpecification(PeriodContributionTypeCriteria criteria) {
        Specification<PeriodContributionType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PeriodContributionType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PeriodContributionType_.name));
            }
            if (criteria.getPeriodTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodTypeId(),
                    root -> root.join(PeriodContributionType_.periodType, JoinType.LEFT).get(PeriodType_.id)));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(PeriodContributionType_.church, JoinType.LEFT).get(Church_.id)));
            }
        }
        return specification;
    }
}
