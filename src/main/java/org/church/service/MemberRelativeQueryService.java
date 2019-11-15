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

import org.church.domain.MemberRelative;
import org.church.domain.*; // for static metamodels
import org.church.repository.MemberRelativeRepository;
import org.church.service.dto.MemberRelativeCriteria;

/**
 * Service for executing complex queries for {@link MemberRelative} entities in the database.
 * The main input is a {@link MemberRelativeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberRelative} or a {@link Page} of {@link MemberRelative} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberRelativeQueryService extends QueryService<MemberRelative> {

    private final Logger log = LoggerFactory.getLogger(MemberRelativeQueryService.class);

    private final MemberRelativeRepository memberRelativeRepository;

    public MemberRelativeQueryService(MemberRelativeRepository memberRelativeRepository) {
        this.memberRelativeRepository = memberRelativeRepository;
    }

    /**
     * Return a {@link List} of {@link MemberRelative} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberRelative> findByCriteria(MemberRelativeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MemberRelative> specification = createSpecification(criteria);
        return memberRelativeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MemberRelative} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberRelative> findByCriteria(MemberRelativeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MemberRelative> specification = createSpecification(criteria);
        return memberRelativeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberRelativeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MemberRelative> specification = createSpecification(criteria);
        return memberRelativeRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberRelativeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MemberRelative> createSpecification(MemberRelativeCriteria criteria) {
        Specification<MemberRelative> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MemberRelative_.id));
            }
            if (criteria.getRelativeType() != null) {
                specification = specification.and(buildSpecification(criteria.getRelativeType(), MemberRelative_.relativeType));
            }
            if (criteria.getRelativeFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelativeFullName(), MemberRelative_.relativeFullName));
            }
            if (criteria.getRelativePhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelativePhoneNumber(), MemberRelative_.relativePhoneNumber));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberId(),
                    root -> root.join(MemberRelative_.member, JoinType.LEFT).get(Member_.id)));
            }
        }
        return specification;
    }
}
