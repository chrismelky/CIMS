package org.church.web.rest;

import org.church.domain.ChurchType;
import org.church.service.ChurchTypeService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ChurchTypeCriteria;
import org.church.service.ChurchTypeQueryService;

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
 * REST controller for managing {@link org.church.domain.ChurchType}.
 */
@RestController
@RequestMapping("/api")
public class ChurchTypeResource {

    private final Logger log = LoggerFactory.getLogger(ChurchTypeResource.class);

    private static final String ENTITY_NAME = "churchType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchTypeService churchTypeService;

    private final ChurchTypeQueryService churchTypeQueryService;

    public ChurchTypeResource(ChurchTypeService churchTypeService, ChurchTypeQueryService churchTypeQueryService) {
        this.churchTypeService = churchTypeService;
        this.churchTypeQueryService = churchTypeQueryService;
    }

    /**
     * {@code POST  /church-types} : Create a new churchType.
     *
     * @param churchType the churchType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churchType, or with status {@code 400 (Bad Request)} if the churchType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/church-types")
    public ResponseEntity<ChurchType> createChurchType(@Valid @RequestBody ChurchType churchType) throws URISyntaxException {
        log.debug("REST request to save ChurchType : {}", churchType);
        if (churchType.getId() != null) {
            throw new BadRequestAlertException("A new churchType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChurchType result = churchTypeService.save(churchType);
        return ResponseEntity.created(new URI("/api/church-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /church-types} : Updates an existing churchType.
     *
     * @param churchType the churchType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchType,
     * or with status {@code 400 (Bad Request)} if the churchType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churchType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/church-types")
    public ResponseEntity<ChurchType> updateChurchType(@Valid @RequestBody ChurchType churchType) throws URISyntaxException {
        log.debug("REST request to update ChurchType : {}", churchType);
        if (churchType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChurchType result = churchTypeService.save(churchType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, churchType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /church-types} : get all the churchTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churchTypes in body.
     */
    @GetMapping("/church-types")
    public ResponseEntity<List<ChurchType>> getAllChurchTypes(ChurchTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChurchTypes by criteria: {}", criteria);
        Page<ChurchType> page = churchTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /church-types/count} : count all the churchTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/church-types/count")
    public ResponseEntity<Long> countChurchTypes(ChurchTypeCriteria criteria) {
        log.debug("REST request to count ChurchTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(churchTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /church-types/:id} : get the "id" churchType.
     *
     * @param id the id of the churchType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churchType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/church-types/{id}")
    public ResponseEntity<ChurchType> getChurchType(@PathVariable Long id) {
        log.debug("REST request to get ChurchType : {}", id);
        Optional<ChurchType> churchType = churchTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(churchType);
    }

    /**
     * {@code DELETE  /church-types/:id} : delete the "id" churchType.
     *
     * @param id the id of the churchType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/church-types/{id}")
    public ResponseEntity<Void> deleteChurchType(@PathVariable Long id) {
        log.debug("REST request to delete ChurchType : {}", id);
        churchTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
