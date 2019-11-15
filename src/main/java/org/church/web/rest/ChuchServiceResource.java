package org.church.web.rest;

import org.church.domain.ChuchService;
import org.church.service.ChuchServiceService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ChuchServiceCriteria;
import org.church.service.ChuchServiceQueryService;

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
 * REST controller for managing {@link org.church.domain.ChuchService}.
 */
@RestController
@RequestMapping("/api")
public class ChuchServiceResource {

    private final Logger log = LoggerFactory.getLogger(ChuchServiceResource.class);

    private static final String ENTITY_NAME = "chuchService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChuchServiceService chuchServiceService;

    private final ChuchServiceQueryService chuchServiceQueryService;

    public ChuchServiceResource(ChuchServiceService chuchServiceService, ChuchServiceQueryService chuchServiceQueryService) {
        this.chuchServiceService = chuchServiceService;
        this.chuchServiceQueryService = chuchServiceQueryService;
    }

    /**
     * {@code POST  /chuch-services} : Create a new chuchService.
     *
     * @param chuchService the chuchService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chuchService, or with status {@code 400 (Bad Request)} if the chuchService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chuch-services")
    public ResponseEntity<ChuchService> createChuchService(@Valid @RequestBody ChuchService chuchService) throws URISyntaxException {
        log.debug("REST request to save ChuchService : {}", chuchService);
        if (chuchService.getId() != null) {
            throw new BadRequestAlertException("A new chuchService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChuchService result = chuchServiceService.save(chuchService);
        return ResponseEntity.created(new URI("/api/chuch-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chuch-services} : Updates an existing chuchService.
     *
     * @param chuchService the chuchService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chuchService,
     * or with status {@code 400 (Bad Request)} if the chuchService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chuchService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chuch-services")
    public ResponseEntity<ChuchService> updateChuchService(@Valid @RequestBody ChuchService chuchService) throws URISyntaxException {
        log.debug("REST request to update ChuchService : {}", chuchService);
        if (chuchService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChuchService result = chuchServiceService.save(chuchService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chuchService.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chuch-services} : get all the chuchServices.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chuchServices in body.
     */
    @GetMapping("/chuch-services")
    public ResponseEntity<List<ChuchService>> getAllChuchServices(ChuchServiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChuchServices by criteria: {}", criteria);
        Page<ChuchService> page = chuchServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /chuch-services/count} : count all the chuchServices.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/chuch-services/count")
    public ResponseEntity<Long> countChuchServices(ChuchServiceCriteria criteria) {
        log.debug("REST request to count ChuchServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(chuchServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chuch-services/:id} : get the "id" chuchService.
     *
     * @param id the id of the chuchService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chuchService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chuch-services/{id}")
    public ResponseEntity<ChuchService> getChuchService(@PathVariable Long id) {
        log.debug("REST request to get ChuchService : {}", id);
        Optional<ChuchService> chuchService = chuchServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chuchService);
    }

    /**
     * {@code DELETE  /chuch-services/:id} : delete the "id" chuchService.
     *
     * @param id the id of the chuchService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chuch-services/{id}")
    public ResponseEntity<Void> deleteChuchService(@PathVariable Long id) {
        log.debug("REST request to delete ChuchService : {}", id);
        chuchServiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
