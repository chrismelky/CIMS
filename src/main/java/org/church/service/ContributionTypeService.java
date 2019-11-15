package org.church.service;

import org.church.domain.ContributionType;
import org.church.repository.ContributionTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ContributionType}.
 */
@Service
@Transactional
public class ContributionTypeService {

    private final Logger log = LoggerFactory.getLogger(ContributionTypeService.class);

    private final ContributionTypeRepository contributionTypeRepository;

    public ContributionTypeService(ContributionTypeRepository contributionTypeRepository) {
        this.contributionTypeRepository = contributionTypeRepository;
    }

    /**
     * Save a contributionType.
     *
     * @param contributionType the entity to save.
     * @return the persisted entity.
     */
    public ContributionType save(ContributionType contributionType) {
        log.debug("Request to save ContributionType : {}", contributionType);
        return contributionTypeRepository.save(contributionType);
    }

    /**
     * Get all the contributionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionType> findAll(Pageable pageable) {
        log.debug("Request to get all ContributionTypes");
        return contributionTypeRepository.findAll(pageable);
    }


    /**
     * Get one contributionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContributionType> findOne(Long id) {
        log.debug("Request to get ContributionType : {}", id);
        return contributionTypeRepository.findById(id);
    }

    /**
     * Delete the contributionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContributionType : {}", id);
        contributionTypeRepository.deleteById(id);
    }
}
