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

import org.church.domain.MemberRite;
import org.church.domain.*; // for static metamodels
import org.church.repository.MemberRiteRepository;
import org.church.service.dto.MemberRiteCriteria;

/**
 * Service for executing complex queries for {@link MemberRite} entities in the database.
 * The main input is a {@link MemberRiteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberRite} or a {@link Page} of {@link MemberRite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberRiteQueryService extends QueryService<MemberRite> {

    private final Logger log = LoggerFactory.getLogger(MemberRiteQueryService.class);

    private final MemberRiteRepository memberRiteRepository;

    public MemberRiteQueryService(MemberRiteRepository memberRiteRepository) {
        this.memberRiteRepository = memberRiteRepository;
    }

    /**
     * Return a {@link List} of {@link MemberRite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberRite> findByCriteria(MemberRiteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MemberRite> specification = createSpecification(criteria);
        return memberRiteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MemberRite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberRite> findByCriteria(MemberRiteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MemberRite> specification = createSpecification(criteria);
        return memberRiteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberRiteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MemberRite> specification = createSpecification(criteria);
        return memberRiteRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberRiteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MemberRite> createSpecification(MemberRiteCriteria criteria) {
        Specification<MemberRite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MemberRite_.id));
            }
            if (criteria.getDateReceived() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateReceived(), MemberRite_.dateReceived));
            }
            if (criteria.getRiteId() != null) {
                specification = specification.and(buildSpecification(criteria.getRiteId(),
                    root -> root.join(MemberRite_.rite, JoinType.LEFT).get(Rite_.id)));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberId(),
                    root -> root.join(MemberRite_.member, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getChurchId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchId(),
                    root -> root.join(MemberRite_.church, JoinType.LEFT).get(Church_.id)));
            }
        }
        return specification;
    }
}
