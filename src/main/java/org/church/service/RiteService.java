package org.church.service;

import org.church.domain.Rite;
import org.church.repository.RiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Rite}.
 */
@Service
@Transactional
public class RiteService {

    private final Logger log = LoggerFactory.getLogger(RiteService.class);

    private final RiteRepository riteRepository;

    public RiteService(RiteRepository riteRepository) {
        this.riteRepository = riteRepository;
    }

    /**
     * Save a rite.
     *
     * @param rite the entity to save.
     * @return the persisted entity.
     */
    public Rite save(Rite rite) {
        log.debug("Request to save Rite : {}", rite);
        return riteRepository.save(rite);
    }

    /**
     * Get all the rites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rite> findAll(Pageable pageable) {
        log.debug("Request to get all Rites");
        return riteRepository.findAll(pageable);
    }


    /**
     * Get one rite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rite> findOne(Long id) {
        log.debug("Request to get Rite : {}", id);
        return riteRepository.findById(id);
    }

    /**
     * Delete the rite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rite : {}", id);
        riteRepository.deleteById(id);
    }
}
