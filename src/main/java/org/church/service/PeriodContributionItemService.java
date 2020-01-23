package org.church.service;

import org.church.domain.PeriodContribution;
import org.church.domain.PeriodContributionItem;
import org.church.repository.PeriodContributionItemRepository;
import org.church.repository.PeriodContributionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PeriodContributionItem}.
 */
@Service
@Transactional
public class PeriodContributionItemService {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionItemService.class);

    private final PeriodContributionItemRepository periodContributionItemRepository;

    private final PeriodContributionRepository periodContributionRepository;

    public PeriodContributionItemService(
        PeriodContributionItemRepository periodContributionItemRepository,
        PeriodContributionRepository periodContributionRepository) {

        this.periodContributionItemRepository = periodContributionItemRepository;
        this.periodContributionRepository = periodContributionRepository;
    }

    /**
     * Save a periodContributionItem.
     *
     * @param periodContributionItem the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public PeriodContributionItem save(PeriodContributionItem periodContributionItem) {
        log.debug("Request to save PeriodContributionItem : {}", periodContributionItem);
        PeriodContributionItem item = periodContributionItemRepository.save(periodContributionItem);
        PeriodContribution contribution = periodContributionRepository.findById(item.getPeriodContribution().getId()).get();
       updateTotal(contribution);
        return item;
    }

    /**
     * Get all the periodContributionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContributionItem> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodContributionItems");
        return periodContributionItemRepository.findAll(pageable);
    }


    /**
     * Get one periodContributionItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodContributionItem> findOne(Long id) {
        log.debug("Request to get PeriodContributionItem : {}", id);
        return periodContributionItemRepository.findById(id);
    }

    /**
     * Delete the periodContributionItem by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete PeriodContributionItem : {}", id);
        PeriodContributionItem item = periodContributionItemRepository.findById(id).get();
        PeriodContribution contribution = item.getPeriodContribution();
        periodContributionItemRepository.deleteById(id);
        updateTotal(contribution);
    }

    private void updateTotal(PeriodContribution contribution) {
        BigDecimal amount = periodContributionItemRepository.getSumByPeriodContribution_Id(contribution.getId());
        contribution.setAmountContributed(amount);
        periodContributionRepository.save(contribution);
    }
}
