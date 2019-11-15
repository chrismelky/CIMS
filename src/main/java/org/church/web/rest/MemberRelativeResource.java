package org.church.web.rest;

import org.church.domain.MemberRelative;
import org.church.service.MemberRelativeService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.MemberRelativeCriteria;
import org.church.service.MemberRelativeQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.church.domain.MemberRelative}.
 */
@RestController
@RequestMapping("/api")
public class MemberRelativeResource {

    private final Logger log = LoggerFactory.getLogger(MemberRelativeResource.class);

    private static final String ENTITY_NAME = "memberRelative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberRelativeService memberRelativeService;

    private final MemberRelativeQueryService memberRelativeQueryService;

    public MemberRelativeResource(MemberRelativeService memberRelativeService, MemberRelativeQueryService memberRelativeQueryService) {
        this.memberRelativeService = memberRelativeService;
        this.memberRelativeQueryService = memberRelativeQueryService;
    }

    /**
     * {@code POST  /member-relatives} : Create a new memberRelative.
     *
     * @param memberRelative the memberRelative to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberRelative, or with status {@code 400 (Bad Request)} if the memberRelative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-relatives")
    public ResponseEntity<MemberRelative> createMemberRelative(@RequestBody MemberRelative memberRelative) throws URISyntaxException {
        log.debug("REST request to save MemberRelative : {}", memberRelative);
        if (memberRelative.getId() != null) {
            throw new BadRequestAlertException("A new memberRelative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberRelative result = memberRelativeService.save(memberRelative);
        return ResponseEntity.created(new URI("/api/member-relatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-relatives} : Updates an existing memberRelative.
     *
     * @param memberRelative the memberRelative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberRelative,
     * or with status {@code 400 (Bad Request)} if the memberRelative is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberRelative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-relatives")
    public ResponseEntity<MemberRelative> updateMemberRelative(@RequestBody MemberRelative memberRelative) throws URISyntaxException {
        log.debug("REST request to update MemberRelative : {}", memberRelative);
        if (memberRelative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemberRelative result = memberRelativeService.save(memberRelative);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberRelative.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-relatives} : get all the memberRelatives.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberRelatives in body.
     */
    @GetMapping("/member-relatives")
    public ResponseEntity<List<MemberRelative>> getAllMemberRelatives(MemberRelativeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MemberRelatives by criteria: {}", criteria);
        Page<MemberRelative> page = memberRelativeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /member-relatives/count} : count all the memberRelatives.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/member-relatives/count")
    public ResponseEntity<Long> countMemberRelatives(MemberRelativeCriteria criteria) {
        log.debug("REST request to count MemberRelatives by criteria: {}", criteria);
        return ResponseEntity.ok().body(memberRelativeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /member-relatives/:id} : get the "id" memberRelative.
     *
     * @param id the id of the memberRelative to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberRelative, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-relatives/{id}")
    public ResponseEntity<MemberRelative> getMemberRelative(@PathVariable Long id) {
        log.debug("REST request to get MemberRelative : {}", id);
        Optional<MemberRelative> memberRelative = memberRelativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberRelative);
    }

    /**
     * {@code DELETE  /member-relatives/:id} : delete the "id" memberRelative.
     *
     * @param id the id of the memberRelative to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-relatives/{id}")
    public ResponseEntity<Void> deleteMemberRelative(@PathVariable Long id) {
        log.debug("REST request to delete MemberRelative : {}", id);
        memberRelativeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
