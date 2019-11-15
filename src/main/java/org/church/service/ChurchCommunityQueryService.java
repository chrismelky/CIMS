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

import org.church.domain.ChurchCommunity;
import org.church.domain.*; // for static metamodels
import org.church.repository.ChurchCommunityRepository;
import org.church.service.dto.ChurchCommunityCriteria;

/**
 * Service for executing complex queries for {@link ChurchCommunity} entities in the database.
 * The main input is a {@link ChurchCommunityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChurchCommunity} or a {@link Page} of {@link ChurchCommunity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChurchCommunityQueryService extends QueryService<ChurchCommunity> {

    private final Logger log = LoggerFactory.getLogger(ChurchCommunityQueryService.class);

    private final ChurchCommunityRepository churchCommunityRepository;

    public ChurchCommunityQueryService(ChurchCommunityRepository churchCommunityRepository) {
        this.churchCommunityRepository = churchCommunityRepository;
    }

    /**
     * Return a {@link List} of {@link ChurchCommunity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChurchCommunity> findByCriteria(ChurchCommunityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChurchCommunity> specification = createSpecification(criteria);
        return churchCommunityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChurchCommunity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchCommunity> findByCriteria(ChurchCommunityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChurchCommunity> specification = createSpecification(criteria);
        return churchCommunityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChurchCommunityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChurchCommunity> specification = createSpecification(criteria);
        return churchCommunityRepository.count(specification);
    }

    /**
     * Function to convert {@link ChurchCommunityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChurchCommunity> createSpecification(ChurchCommunityCriteria criteria) {
        Specification<ChurchCommunity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChurchCommunity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ChurchCommunity_.name));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(ChurchCommunity_.church, JoinType.LEFT).get(Church_.id)));
            }
            if (criteria.getChairPersonId() != null) {
                specification = specification.and(buildSpecification(criteria.getChairPersonId(),
                    root -> root.join(ChurchCommunity_.chairPerson, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getSecretaryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSecretaryId(),
                    root -> root.join(ChurchCommunity_.secretary, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getTreasurerId() != null) {
                specification = specification.and(buildSpecification(criteria.getTreasurerId(),
                    root -> root.join(ChurchCommunity_.treasurer, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getMembersId() != null) {
                specification = specification.and(buildSpecification(criteria.getMembersId(),
                    root -> root.join(ChurchCommunity_.members, JoinType.LEFT).get(Member_.id)));
            }
        }
        return specification;
    }
}
