package org.church.web.rest;

import org.church.domain.Church;
import org.church.service.ChurchService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ChurchCriteria;
import org.church.service.ChurchQueryService;

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
 * REST controller for managing {@link org.church.domain.Church}.
 */
@RestController
@RequestMapping("/api")
public class ChurchResource {

    private final Logger log = LoggerFactory.getLogger(ChurchResource.class);

    private static final String ENTITY_NAME = "church";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchService churchService;

    private final ChurchQueryService churchQueryService;

    public ChurchResource(ChurchService churchService, ChurchQueryService churchQueryService) {
        this.churchService = churchService;
        this.churchQueryService = churchQueryService;
    }

    /**
     * {@code POST  /churches} : Create a new church.
     *
     * @param church the church to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new church, or with status {@code 400 (Bad Request)} if the church has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/churches")
    public ResponseEntity<Church> createChurch(@Valid @RequestBody Church church) throws URISyntaxException {
        log.debug("REST request to save Church : {}", church);
        if (church.getId() != null) {
            throw new BadRequestAlertException("A new church cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Church result = churchService.save(church);
        return ResponseEntity.created(new URI("/api/churches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /churches} : Updates an existing church.
     *
     * @param church the church to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated church,
     * or with status {@code 400 (Bad Request)} if the church is not valid,
     * or with status {@code 500 (Internal Server Error)} if the church couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/churches")
    public ResponseEntity<Church> updateChurch(@Valid @RequestBody Church church) throws URISyntaxException {
        log.debug("REST request to update Church : {}", church);
        if (church.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Church result = churchService.save(church);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, church.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /churches} : get all the churches.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churches in body.
     */
    @GetMapping("/churches")
    public ResponseEntity<List<Church>> getAllChurches(ChurchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Churches by criteria: {}", criteria);
        Page<Church> page = churchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /churches/count} : count all the churches.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/churches/count")
    public ResponseEntity<Long> countChurches(ChurchCriteria criteria) {
        log.debug("REST request to count Churches by criteria: {}", criteria);
        return ResponseEntity.ok().body(churchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /churches/:id} : get the "id" church.
     *
     * @param id the id of the church to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the church, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/churches/{id}")
    public ResponseEntity<Church> getChurch(@PathVariable Long id) {
        log.debug("REST request to get Church : {}", id);
        Optional<Church> church = churchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(church);
    }

    /**
     * {@code DELETE  /churches/:id} : delete the "id" church.
     *
     * @param id the id of the church to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/churches/{id}")
    public ResponseEntity<Void> deleteChurch(@PathVariable Long id) {
        log.debug("REST request to delete Church : {}", id);
        churchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
