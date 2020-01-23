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

import org.church.domain.Period;
import org.church.domain.*; // for static metamodels
import org.church.repository.PeriodRepository;
import org.church.service.dto.PeriodCriteria;

/**
 * Service for executing complex queries for {@link Period} entities in the database.
 * The main input is a {@link PeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Period} or a {@link Page} of {@link Period} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodQueryService extends QueryService<Period> {

    private final Logger log = LoggerFactory.getLogger(PeriodQueryService.class);

    private final PeriodRepository periodRepository;

    public PeriodQueryService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    /**
     * Return a {@link List} of {@link Period} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Period> findByCriteria(PeriodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Period> specification = createSpecification(criteria);
        return periodRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Period} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Period> findByCriteria(PeriodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Period> specification = createSpecification(criteria);
        return periodRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Period> specification = createSpecification(criteria);
        return periodRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Period> createSpecification(PeriodCriteria criteria) {
        Specification<Period> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Period_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Period_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Period_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Period_.endDate));
            }
            if (criteria.getIsCurrent() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCurrent(), Period_.isCurrent));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Period_.type, JoinType.LEFT).get(PeriodType_.id)));
            }
        }
        return specification;
    }
}
