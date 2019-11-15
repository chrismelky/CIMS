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

import org.church.domain.ChuchService;
import org.church.domain.*; // for static metamodels
import org.church.repository.ChuchServiceRepository;
import org.church.service.dto.ChuchServiceCriteria;

/**
 * Service for executing complex queries for {@link ChuchService} entities in the database.
 * The main input is a {@link ChuchServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChuchService} or a {@link Page} of {@link ChuchService} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChuchServiceQueryService extends QueryService<ChuchService> {

    private final Logger log = LoggerFactory.getLogger(ChuchServiceQueryService.class);

    private final ChuchServiceRepository chuchServiceRepository;

    public ChuchServiceQueryService(ChuchServiceRepository chuchServiceRepository) {
        this.chuchServiceRepository = chuchServiceRepository;
    }

    /**
     * Return a {@link List} of {@link ChuchService} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChuchService> findByCriteria(ChuchServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChuchService> specification = createSpecification(criteria);
        return chuchServiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChuchService} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChuchService> findByCriteria(ChuchServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChuchService> specification = createSpecification(criteria);
        return chuchServiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChuchServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChuchService> specification = createSpecification(criteria);
        return chuchServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link ChuchServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChuchService> createSpecification(ChuchServiceCriteria criteria) {
        Specification<ChuchService> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChuchService_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ChuchService_.name));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildSpecification(criteria.getDay(), ChuchService_.day));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartTime(), ChuchService_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndTime(), ChuchService_.endTime));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(ChuchService_.church, JoinType.LEFT).get(Church_.id)));
            }
        }
        return specification;
    }
}
