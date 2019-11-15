package org.church.web.rest;

import org.church.domain.Rite;
import org.church.service.RiteService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.RiteCriteria;
import org.church.service.RiteQueryService;

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
 * REST controller for managing {@link org.church.domain.Rite}.
 */
@RestController
@RequestMapping("/api")
public class RiteResource {

    private final Logger log = LoggerFactory.getLogger(RiteResource.class);

    private static final String ENTITY_NAME = "rite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RiteService riteService;

    private final RiteQueryService riteQueryService;

    public RiteResource(RiteService riteService, RiteQueryService riteQueryService) {
        this.riteService = riteService;
        this.riteQueryService = riteQueryService;
    }

    /**
     * {@code POST  /rites} : Create a new rite.
     *
     * @param rite the rite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rite, or with status {@code 400 (Bad Request)} if the rite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rites")
    public ResponseEntity<Rite> createRite(@Valid @RequestBody Rite rite) throws URISyntaxException {
        log.debug("REST request to save Rite : {}", rite);
        if (rite.getId() != null) {
            throw new BadRequestAlertException("A new rite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rite result = riteService.save(rite);
        return ResponseEntity.created(new URI("/api/rites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rites} : Updates an existing rite.
     *
     * @param rite the rite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rite,
     * or with status {@code 400 (Bad Request)} if the rite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rites")
    public ResponseEntity<Rite> updateRite(@Valid @RequestBody Rite rite) throws URISyntaxException {
        log.debug("REST request to update Rite : {}", rite);
        if (rite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rite result = riteService.save(rite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rite.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rites} : get all the rites.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rites in body.
     */
    @GetMapping("/rites")
    public ResponseEntity<List<Rite>> getAllRites(RiteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rites by criteria: {}", criteria);
        Page<Rite> page = riteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /rites/count} : count all the rites.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/rites/count")
    public ResponseEntity<Long> countRites(RiteCriteria criteria) {
        log.debug("REST request to count Rites by criteria: {}", criteria);
        return ResponseEntity.ok().body(riteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rites/:id} : get the "id" rite.
     *
     * @param id the id of the rite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rites/{id}")
    public ResponseEntity<Rite> getRite(@PathVariable Long id) {
        log.debug("REST request to get Rite : {}", id);
        Optional<Rite> rite = riteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rite);
    }

    /**
     * {@code DELETE  /rites/:id} : delete the "id" rite.
     *
     * @param id the id of the rite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rites/{id}")
    public ResponseEntity<Void> deleteRite(@PathVariable Long id) {
        log.debug("REST request to delete Rite : {}", id);
        riteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
