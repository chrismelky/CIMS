package org.church.service;

import org.church.domain.ChurchCommunity;
import org.church.repository.ChurchCommunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChurchCommunity}.
 */
@Service
@Transactional
public class ChurchCommunityService {

    private final Logger log = LoggerFactory.getLogger(ChurchCommunityService.class);

    private final ChurchCommunityRepository churchCommunityRepository;

    public ChurchCommunityService(ChurchCommunityRepository churchCommunityRepository) {
        this.churchCommunityRepository = churchCommunityRepository;
    }

    /**
     * Save a churchCommunity.
     *
     * @param churchCommunity the entity to save.
     * @return the persisted entity.
     */
    public ChurchCommunity save(ChurchCommunity churchCommunity) {
        log.debug("Request to save ChurchCommunity : {}", churchCommunity);
        return churchCommunityRepository.save(churchCommunity);
    }

    /**
     * Get all the churchCommunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchCommunity> findAll(Pageable pageable) {
        log.debug("Request to get all ChurchCommunities");
        return churchCommunityRepository.findAll(pageable);
    }


    /**
     * Get one churchCommunity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChurchCommunity> findOne(Long id) {
        log.debug("Request to get ChurchCommunity : {}", id);
        return churchCommunityRepository.findById(id);
    }

    /**
     * Delete the churchCommunity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChurchCommunity : {}", id);
        churchCommunityRepository.deleteById(id);
    }
}
