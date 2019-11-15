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

import org.church.domain.ContributionType;
import org.church.domain.*; // for static metamodels
import org.church.repository.ContributionTypeRepository;
import org.church.service.dto.ContributionTypeCriteria;

/**
 * Service for executing complex queries for {@link ContributionType} entities in the database.
 * The main input is a {@link ContributionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContributionType} or a {@link Page} of {@link ContributionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContributionTypeQueryService extends QueryService<ContributionType> {

    private final Logger log = LoggerFactory.getLogger(ContributionTypeQueryService.class);

    private final ContributionTypeRepository contributionTypeRepository;

    public ContributionTypeQueryService(ContributionTypeRepository contributionTypeRepository) {
        this.contributionTypeRepository = contributionTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ContributionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContributionType> findByCriteria(ContributionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContributionType> specification = createSpecification(criteria);
        return contributionTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContributionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionType> findByCriteria(ContributionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContributionType> specification = createSpecification(criteria);
        return contributionTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContributionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContributionType> specification = createSpecification(criteria);
        return contributionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ContributionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContributionType> createSpecification(ContributionTypeCriteria criteria) {
        Specification<ContributionType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContributionType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ContributionType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContributionType_.name));
            }
        }
        return specification;
    }
}
