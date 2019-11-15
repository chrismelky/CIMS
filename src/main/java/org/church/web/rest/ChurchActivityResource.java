package org.church.web.rest;

import org.church.domain.ChurchActivity;
import org.church.service.ChurchActivityService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ChurchActivityCriteria;
import org.church.service.ChurchActivityQueryService;

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
 * REST controller for managing {@link org.church.domain.ChurchActivity}.
 */
@RestController
@RequestMapping("/api")
public class ChurchActivityResource {

    private final Logger log = LoggerFactory.getLogger(ChurchActivityResource.class);

    private static final String ENTITY_NAME = "churchActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchActivityService churchActivityService;

    private final ChurchActivityQueryService churchActivityQueryService;

    public ChurchActivityResource(ChurchActivityService churchActivityService, ChurchActivityQueryService churchActivityQueryService) {
        this.churchActivityService = churchActivityService;
        this.churchActivityQueryService = churchActivityQueryService;
    }

    /**
     * {@code POST  /church-activities} : Create a new churchActivity.
     *
     * @param churchActivity the churchActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churchActivity, or with status {@code 400 (Bad Request)} if the churchActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/church-activities")
    public ResponseEntity<ChurchActivity> createChurchActivity(@Valid @RequestBody ChurchActivity churchActivity) throws URISyntaxException {
        log.debug("REST request to save ChurchActivity : {}", churchActivity);
        if (churchActivity.getId() != null) {
            throw new BadRequestAlertException("A new churchActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChurchActivity result = churchActivityService.save(churchActivity);
        return ResponseEntity.created(new URI("/api/church-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /church-activities} : Updates an existing churchActivity.
     *
     * @param churchActivity the churchActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchActivity,
     * or with status {@code 400 (Bad Request)} if the churchActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churchActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/church-activities")
    public ResponseEntity<ChurchActivity> updateChurchActivity(@Valid @RequestBody ChurchActivity churchActivity) throws URISyntaxException {
        log.debug("REST request to update ChurchActivity : {}", churchActivity);
        if (churchActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChurchActivity result = churchActivityService.save(churchActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, churchActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /church-activities} : get all the churchActivities.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churchActivities in body.
     */
    @GetMapping("/church-activities")
    public ResponseEntity<List<ChurchActivity>> getAllChurchActivities(ChurchActivityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChurchActivities by criteria: {}", criteria);
        Page<ChurchActivity> page = churchActivityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /church-activities/count} : count all the churchActivities.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/church-activities/count")
    public ResponseEntity<Long> countChurchActivities(ChurchActivityCriteria criteria) {
        log.debug("REST request to count ChurchActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(churchActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /church-activities/:id} : get the "id" churchActivity.
     *
     * @param id the id of the churchActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churchActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/church-activities/{id}")
    public ResponseEntity<ChurchActivity> getChurchActivity(@PathVariable Long id) {
        log.debug("REST request to get ChurchActivity : {}", id);
        Optional<ChurchActivity> churchActivity = churchActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(churchActivity);
    }

    /**
     * {@code DELETE  /church-activities/:id} : delete the "id" churchActivity.
     *
     * @param id the id of the churchActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/church-activities/{id}")
    public ResponseEntity<Void> deleteChurchActivity(@PathVariable Long id) {
        log.debug("REST request to delete ChurchActivity : {}", id);
        churchActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
