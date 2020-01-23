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

import org.church.domain.HomeChurchCommunity;
import org.church.domain.*; // for static metamodels
import org.church.repository.HomeChurchCommunityRepository;
import org.church.service.dto.HomeChurchCommunityCriteria;

/**
 * Service for executing complex queries for {@link HomeChurchCommunity} entities in the database.
 * The main input is a {@link HomeChurchCommunityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomeChurchCommunity} or a {@link Page} of {@link HomeChurchCommunity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomeChurchCommunityQueryService extends QueryService<HomeChurchCommunity> {

    private final Logger log = LoggerFactory.getLogger(HomeChurchCommunityQueryService.class);

    private final HomeChurchCommunityRepository homeChurchCommunityRepository;

    public HomeChurchCommunityQueryService(HomeChurchCommunityRepository homeChurchCommunityRepository) {
        this.homeChurchCommunityRepository = homeChurchCommunityRepository;
    }

    /**
     * Return a {@link List} of {@link HomeChurchCommunity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomeChurchCommunity> findByCriteria(HomeChurchCommunityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HomeChurchCommunity> specification = createSpecification(criteria);
        return homeChurchCommunityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HomeChurchCommunity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeChurchCommunity> findByCriteria(HomeChurchCommunityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomeChurchCommunity> specification = createSpecification(criteria);
        return homeChurchCommunityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomeChurchCommunityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HomeChurchCommunity> specification = createSpecification(criteria);
        return homeChurchCommunityRepository.count(specification);
    }

    /**
     * Function to convert {@link HomeChurchCommunityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HomeChurchCommunity> createSpecification(HomeChurchCommunityCriteria criteria) {
        Specification<HomeChurchCommunity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HomeChurchCommunity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HomeChurchCommunity_.name));
            }
            if (criteria.getNumberOfHouseHold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfHouseHold(), HomeChurchCommunity_.numberOfHouseHold));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), HomeChurchCommunity_.phoneNumber));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), HomeChurchCommunity_.address));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(HomeChurchCommunity_.church, JoinType.LEFT).get(Church_.id)));
            }
            if (criteria.getChairmanId() != null) {
                specification = specification.and(buildSpecification(criteria.getChairmanId(),
                    root -> root.join(HomeChurchCommunity_.chairman, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getSecretaryId() != null) {
                specification = specification.and(buildSpecification(criteria.getSecretaryId(),
                    root -> root.join(HomeChurchCommunity_.secretary, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getTreasurerId() != null) {
                specification = specification.and(buildSpecification(criteria.getTreasurerId(),
                    root -> root.join(HomeChurchCommunity_.treasurer, JoinType.LEFT).get(Member_.id)));
            }
        }
        return specification;
    }
}
