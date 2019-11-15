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

import org.church.domain.MemberPromise;
import org.church.domain.*; // for static metamodels
import org.church.repository.MemberPromiseRepository;
import org.church.service.dto.MemberPromiseCriteria;

/**
 * Service for executing complex queries for {@link MemberPromise} entities in the database.
 * The main input is a {@link MemberPromiseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberPromise} or a {@link Page} of {@link MemberPromise} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberPromiseQueryService extends QueryService<MemberPromise> {

    private final Logger log = LoggerFactory.getLogger(MemberPromiseQueryService.class);

    private final MemberPromiseRepository memberPromiseRepository;

    public MemberPromiseQueryService(MemberPromiseRepository memberPromiseRepository) {
        this.memberPromiseRepository = memberPromiseRepository;
    }

    /**
     * Return a {@link List} of {@link MemberPromise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberPromise> findByCriteria(MemberPromiseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MemberPromise> specification = createSpecification(criteria);
        return memberPromiseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MemberPromise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberPromise> findByCriteria(MemberPromiseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MemberPromise> specification = createSpecification(criteria);
        return memberPromiseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberPromiseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MemberPromise> specification = createSpecification(criteria);
        return memberPromiseRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberPromiseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MemberPromise> createSpecification(MemberPromiseCriteria criteria) {
        Specification<MemberPromise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MemberPromise_.id));
            }
            if (criteria.getPromiseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPromiseDate(), MemberPromise_.promiseDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), MemberPromise_.amount));
            }
            if (criteria.getOtherPromise() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOtherPromise(), MemberPromise_.otherPromise));
            }
            if (criteria.getFulfillmentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFulfillmentDate(), MemberPromise_.fulfillmentDate));
            }
            if (criteria.getIsFulfilled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFulfilled(), MemberPromise_.isFulfilled));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberId(),
                    root -> root.join(MemberPromise_.member, JoinType.LEFT).get(Member_.id)));
            }
            if (criteria.getChurchActivityId() != null) {
                specification = specification.and(buildSpecification(criteria.getChurchActivityId(),
                    root -> root.join(MemberPromise_.churchActivity, JoinType.LEFT).get(ChurchActivity_.id)));
            }
        }
        return specification;
    }
}
