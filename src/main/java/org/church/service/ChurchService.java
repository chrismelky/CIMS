package org.church.service;

import org.church.domain.Church;
import org.church.repository.ChurchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Church}.
 */
@Service
@Transactional
public class ChurchService {

    private final Logger log = LoggerFactory.getLogger(ChurchService.class);

    private final ChurchRepository churchRepository;

    public ChurchService(ChurchRepository churchRepository) {
        this.churchRepository = churchRepository;
    }

    /**
     * Save a church.
     *
     * @param church the entity to save.
     * @return the persisted entity.
     */
    public Church save(Church church) {
        log.debug("Request to save Church : {}", church);
        return churchRepository.save(church);
    }

    /**
     * Get all the churches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Church> findAll(Pageable pageable) {
        log.debug("Request to get all Churches");
        return churchRepository.findAll(pageable);
    }


    /**
     * Get one church by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Church> findOne(Long id) {
        log.debug("Request to get Church : {}", id);
        return churchRepository.findById(id);
    }

    /**
     * Delete the church by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Church : {}", id);
        churchRepository.deleteById(id);
    }
}
