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

import org.church.domain.Church;
import org.church.domain.*; // for static metamodels
import org.church.repository.ChurchRepository;
import org.church.service.dto.ChurchCriteria;

/**
 * Service for executing complex queries for {@link Church} entities in the database.
 * The main input is a {@link ChurchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Church} or a {@link Page} of {@link Church} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChurchQueryService extends QueryService<Church> {

    private final Logger log = LoggerFactory.getLogger(ChurchQueryService.class);

    private final ChurchRepository churchRepository;

    public ChurchQueryService(ChurchRepository churchRepository) {
        this.churchRepository = churchRepository;
    }

    /**
     * Return a {@link List} of {@link Church} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Church> findByCriteria(ChurchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Church> specification = createSpecification(criteria);
        return churchRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Church} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Church> findByCriteria(ChurchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Church> specification = createSpecification(criteria);
        return churchRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChurchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Church> specification = createSpecification(criteria);
        return churchRepository.count(specification);
    }

    /**
     * Function to convert {@link ChurchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Church> createSpecification(ChurchCriteria criteria) {
        Specification<Church> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Church_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Church_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Church_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Church_.phoneNumber));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Church_.address));
            }
            if (criteria.getFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFax(), Church_.fax));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Church_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Church_.longitude));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(Church_.parent, JoinType.LEFT).get(Church_.id)));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Church_.type, JoinType.LEFT).get(ChurchType_.id)));
            }
        }
        return specification;
    }
}
