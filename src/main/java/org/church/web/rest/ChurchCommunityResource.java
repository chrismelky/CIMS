package org.church.web.rest;

import org.church.domain.ChurchCommunity;
import org.church.service.ChurchCommunityService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ChurchCommunityCriteria;
import org.church.service.ChurchCommunityQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.church.domain.ChurchCommunity}.
 */
@RestController
@RequestMapping("/api")
public class ChurchCommunityResource {

    private final Logger log = LoggerFactory.getLogger(ChurchCommunityResource.class);

    private static final String ENTITY_NAME = "churchCommunity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchCommunityService churchCommunityService;

    private final ChurchCommunityQueryService churchCommunityQueryService;

    public ChurchCommunityResource(ChurchCommunityService churchCommunityService, ChurchCommunityQueryService churchCommunityQueryService) {
        this.churchCommunityService = churchCommunityService;
        this.churchCommunityQueryService = churchCommunityQueryService;
    }

    /**
     * {@code POST  /church-communities} : Create a new churchCommunity.
     *
     * @param churchCommunity the churchCommunity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churchCommunity, or with status {@code 400 (Bad Request)} if the churchCommunity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/church-communities")
    public ResponseEntity<ChurchCommunity> createChurchCommunity(@Valid @RequestBody ChurchCommunity churchCommunity) throws URISyntaxException {
        log.debug("REST request to save ChurchCommunity : {}", churchCommunity);
        if (churchCommunity.getId() != null) {
            throw new BadRequestAlertException("A new churchCommunity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChurchCommunity result = churchCommunityService.save(churchCommunity);
        return ResponseEntity.created(new URI("/api/church-communities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /church-communities} : Updates an existing churchCommunity.
     *
     * @param churchCommunity the churchCommunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchCommunity,
     * or with status {@code 400 (Bad Request)} if the churchCommunity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churchCommunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/church-communities")
    public ResponseEntity<ChurchCommunity> updateChurchCommunity(@Valid @RequestBody ChurchCommunity churchCommunity) throws URISyntaxException {
        log.debug("REST request to update ChurchCommunity : {}", churchCommunity);
        if (churchCommunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChurchCommunity result = churchCommunityService.save(churchCommunity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, churchCommunity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /church-communities} : get all the churchCommunities.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churchCommunities in body.
     */
    @GetMapping("/church-communities")
    public ResponseEntity<List<ChurchCommunity>> getAllChurchCommunities(ChurchCommunityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChurchCommunities by criteria: {}", criteria);
        Page<ChurchCommunity> page = churchCommunityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /church-communities/count} : count all the churchCommunities.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/church-communities/count")
    public ResponseEntity<Long> countChurchCommunities(ChurchCommunityCriteria criteria) {
        log.debug("REST request to count ChurchCommunities by criteria: {}", criteria);
        return ResponseEntity.ok().body(churchCommunityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /church-communities/:id} : get the "id" churchCommunity.
     *
     * @param id the id of the churchCommunity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churchCommunity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/church-communities/{id}")
    public ResponseEntity<ChurchCommunity> getChurchCommunity(@PathVariable Long id) {
        log.debug("REST request to get ChurchCommunity : {}", id);
        Optional<ChurchCommunity> churchCommunity = churchCommunityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(churchCommunity);
    }

    /**
     * {@code DELETE  /church-communities/:id} : delete the "id" churchCommunity.
     *
     * @param id the id of the churchCommunity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/church-communities/{id}")
    public ResponseEntity<Void> deleteChurchCommunity(@PathVariable Long id) {
        log.debug("REST request to delete ChurchCommunity : {}", id);
        churchCommunityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
