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

import org.church.domain.ChurchType;
import org.church.domain.*; // for static metamodels
import org.church.repository.ChurchTypeRepository;
import org.church.service.dto.ChurchTypeCriteria;

/**
 * Service for executing complex queries for {@link ChurchType} entities in the database.
 * The main input is a {@link ChurchTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChurchType} or a {@link Page} of {@link ChurchType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChurchTypeQueryService extends QueryService<ChurchType> {

    private final Logger log = LoggerFactory.getLogger(ChurchTypeQueryService.class);

    private final ChurchTypeRepository churchTypeRepository;

    public ChurchTypeQueryService(ChurchTypeRepository churchTypeRepository) {
        this.churchTypeRepository = churchTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ChurchType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChurchType> findByCriteria(ChurchTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChurchType> specification = createSpecification(criteria);
        return churchTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChurchType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchType> findByCriteria(ChurchTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChurchType> specification = createSpecification(criteria);
        return churchTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChurchTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChurchType> specification = createSpecification(criteria);
        return churchTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChurchTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChurchType> createSpecification(ChurchTypeCriteria criteria) {
        Specification<ChurchType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChurchType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ChurchType_.name));
            }
        }
        return specification;
    }
}
