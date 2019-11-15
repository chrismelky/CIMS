package org.church.service;

import org.church.domain.ChurchActivity;
import org.church.repository.ChurchActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChurchActivity}.
 */
@Service
@Transactional
public class ChurchActivityService {

    private final Logger log = LoggerFactory.getLogger(ChurchActivityService.class);

    private final ChurchActivityRepository churchActivityRepository;

    public ChurchActivityService(ChurchActivityRepository churchActivityRepository) {
        this.churchActivityRepository = churchActivityRepository;
    }

    /**
     * Save a churchActivity.
     *
     * @param churchActivity the entity to save.
     * @return the persisted entity.
     */
    public ChurchActivity save(ChurchActivity churchActivity) {
        log.debug("Request to save ChurchActivity : {}", churchActivity);
        return churchActivityRepository.save(churchActivity);
    }

    /**
     * Get all the churchActivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchActivity> findAll(Pageable pageable) {
        log.debug("Request to get all ChurchActivities");
        return churchActivityRepository.findAll(pageable);
    }


    /**
     * Get one churchActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChurchActivity> findOne(Long id) {
        log.debug("Request to get ChurchActivity : {}", id);
        return churchActivityRepository.findById(id);
    }

    /**
     * Delete the churchActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChurchActivity : {}", id);
        churchActivityRepository.deleteById(id);
    }
}
