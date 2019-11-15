package org.church.service;

import org.church.domain.MemberContribution;
import org.church.repository.MemberContributionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MemberContribution}.
 */
@Service
@Transactional
public class MemberContributionService {

    private final Logger log = LoggerFactory.getLogger(MemberContributionService.class);

    private final MemberContributionRepository memberContributionRepository;

    public MemberContributionService(MemberContributionRepository memberContributionRepository) {
        this.memberContributionRepository = memberContributionRepository;
    }

    /**
     * Save a memberContribution.
     *
     * @param memberContribution the entity to save.
     * @return the persisted entity.
     */
    public MemberContribution save(MemberContribution memberContribution) {
        log.debug("Request to save MemberContribution : {}", memberContribution);
        return memberContributionRepository.save(memberContribution);
    }

    /**
     * Get all the memberContributions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberContribution> findAll(Pageable pageable) {
        log.debug("Request to get all MemberContributions");
        return memberContributionRepository.findAll(pageable);
    }


    /**
     * Get one memberContribution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberContribution> findOne(Long id) {
        log.debug("Request to get MemberContribution : {}", id);
        return memberContributionRepository.findById(id);
    }

    /**
     * Delete the memberContribution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberContribution : {}", id);
        memberContributionRepository.deleteById(id);
    }
}
