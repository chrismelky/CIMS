package org.church.service;

import org.church.domain.HomeChurchCommunity;
import org.church.repository.HomeChurchCommunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link HomeChurchCommunity}.
 */
@Service
@Transactional
public class HomeChurchCommunityService {

    private final Logger log = LoggerFactory.getLogger(HomeChurchCommunityService.class);

    private final HomeChurchCommunityRepository homeChurchCommunityRepository;

    public HomeChurchCommunityService(HomeChurchCommunityRepository homeChurchCommunityRepository) {
        this.homeChurchCommunityRepository = homeChurchCommunityRepository;
    }

    /**
     * Save a homeChurchCommunity.
     *
     * @param homeChurchCommunity the entity to save.
     * @return the persisted entity.
     */
    public HomeChurchCommunity save(HomeChurchCommunity homeChurchCommunity) {
        log.debug("Request to save HomeChurchCommunity : {}", homeChurchCommunity);
        return homeChurchCommunityRepository.save(homeChurchCommunity);
    }

    /**
     * Get all the homeChurchCommunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeChurchCommunity> findAll(Pageable pageable) {
        log.debug("Request to get all HomeChurchCommunities");
        return homeChurchCommunityRepository.findAll(pageable);
    }


    /**
     * Get one homeChurchCommunity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HomeChurchCommunity> findOne(Long id) {
        log.debug("Request to get HomeChurchCommunity : {}", id);
        return homeChurchCommunityRepository.findById(id);
    }

    /**
     * Delete the homeChurchCommunity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HomeChurchCommunity : {}", id);
        homeChurchCommunityRepository.deleteById(id);
    }
}
