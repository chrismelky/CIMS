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

import org.church.domain.PeriodType;
import org.church.domain.*; // for static metamodels
import org.church.repository.PeriodTypeRepository;
import org.church.service.dto.PeriodTypeCriteria;

/**
 * Service for executing complex queries for {@link PeriodType} entities in the database.
 * The main input is a {@link PeriodTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeriodType} or a {@link Page} of {@link PeriodType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodTypeQueryService extends QueryService<PeriodType> {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeQueryService.class);

    private final PeriodTypeRepository periodTypeRepository;

    public PeriodTypeQueryService(PeriodTypeRepository periodTypeRepository) {
        this.periodTypeRepository = periodTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PeriodType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodType> findByCriteria(PeriodTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PeriodType> specification = createSpecification(criteria);
        return periodTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PeriodType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodType> findByCriteria(PeriodTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PeriodType> specification = createSpecification(criteria);
        return periodTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PeriodType> specification = createSpecification(criteria);
        return periodTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PeriodType> createSpecification(PeriodTypeCriteria criteria) {
        Specification<PeriodType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PeriodType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PeriodType_.name));
            }
        }
        return specification;
    }
}
