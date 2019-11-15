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

import org.church.domain.Rite;
import org.church.domain.*; // for static metamodels
import org.church.repository.RiteRepository;
import org.church.service.dto.RiteCriteria;

/**
 * Service for executing complex queries for {@link Rite} entities in the database.
 * The main input is a {@link RiteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rite} or a {@link Page} of {@link Rite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RiteQueryService extends QueryService<Rite> {

    private final Logger log = LoggerFactory.getLogger(RiteQueryService.class);

    private final RiteRepository riteRepository;

    public RiteQueryService(RiteRepository riteRepository) {
        this.riteRepository = riteRepository;
    }

    /**
     * Return a {@link List} of {@link Rite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rite> findByCriteria(RiteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rite> specification = createSpecification(criteria);
        return riteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rite> findByCriteria(RiteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rite> specification = createSpecification(criteria);
        return riteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RiteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rite> specification = createSpecification(criteria);
        return riteRepository.count(specification);
    }

    /**
     * Function to convert {@link RiteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rite> createSpecification(RiteCriteria criteria) {
        Specification<Rite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Rite_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Rite_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Rite_.name));
            }
        }
        return specification;
    }
}
