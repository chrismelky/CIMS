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

import org.church.domain.PeriodContributionItem;
import org.church.domain.*; // for static metamodels
import org.church.repository.PeriodContributionItemRepository;
import org.church.service.dto.PeriodContributionItemCriteria;

/**
 * Service for executing complex queries for {@link PeriodContributionItem} entities in the database.
 * The main input is a {@link PeriodContributionItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeriodContributionItem} or a {@link Page} of {@link PeriodContributionItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodContributionItemQueryService extends QueryService<PeriodContributionItem> {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionItemQueryService.class);

    private final PeriodContributionItemRepository periodContributionItemRepository;

    public PeriodContributionItemQueryService(PeriodContributionItemRepository periodContributionItemRepository) {
        this.periodContributionItemRepository = periodContributionItemRepository;
    }

    /**
     * Return a {@link List} of {@link PeriodContributionItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodContributionItem> findByCriteria(PeriodContributionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PeriodContributionItem> specification = createSpecification(criteria);
        return periodContributionItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PeriodContributionItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContributionItem> findByCriteria(PeriodContributionItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PeriodContributionItem> specification = createSpecification(criteria);
        return periodContributionItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodContributionItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PeriodContributionItem> specification = createSpecification(criteria);
        return periodContributionItemRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodContributionItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PeriodContributionItem> createSpecification(PeriodContributionItemCriteria criteria) {
        Specification<PeriodContributionItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PeriodContributionItem_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PeriodContributionItem_.amount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PeriodContributionItem_.description));
            }
            if (criteria.getDateReceived() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateReceived(), PeriodContributionItem_.dateReceived));
            }
            if (criteria.getReceivedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReceivedBy(), PeriodContributionItem_.receivedBy));
            }
            if (criteria.getPeriodContributionId() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodContributionId(),
                    root -> root.join(PeriodContributionItem_.periodContribution, JoinType.LEFT).get(PeriodContribution_.id)));
            }
        }
        return specification;
    }
}
