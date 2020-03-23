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

import org.church.domain.FinancialYear;
import org.church.domain.*; // for static metamodels
import org.church.repository.FinancialYearRepository;
import org.church.service.dto.FinancialYearCriteria;

/**
 * Service for executing complex queries for {@link FinancialYear} entities in the database.
 * The main input is a {@link FinancialYearCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FinancialYear} or a {@link Page} of {@link FinancialYear} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinancialYearQueryService extends QueryService<FinancialYear> {

    private final Logger log = LoggerFactory.getLogger(FinancialYearQueryService.class);

    private final FinancialYearRepository financialYearRepository;

    public FinancialYearQueryService(FinancialYearRepository financialYearRepository) {
        this.financialYearRepository = financialYearRepository;
    }

    /**
     * Return a {@link List} of {@link FinancialYear} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FinancialYear> findByCriteria(FinancialYearCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FinancialYear> specification = createSpecification(criteria);
        return financialYearRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FinancialYear} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialYear> findByCriteria(FinancialYearCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinancialYear> specification = createSpecification(criteria);
        return financialYearRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinancialYearCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinancialYear> specification = createSpecification(criteria);
        return financialYearRepository.count(specification);
    }

    /**
     * Function to convert {@link FinancialYearCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinancialYear> createSpecification(FinancialYearCriteria criteria) {
        Specification<FinancialYear> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FinancialYear_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FinancialYear_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), FinancialYear_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), FinancialYear_.endDate));
            }
        }
        return specification;
    }
}
