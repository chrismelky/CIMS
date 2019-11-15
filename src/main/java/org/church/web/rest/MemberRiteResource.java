package org.church.web.rest;

import org.church.domain.MemberRite;
import org.church.service.MemberRiteService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.MemberRiteCriteria;
import org.church.service.MemberRiteQueryService;

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
 * REST controller for managing {@link org.church.domain.MemberRite}.
 */
@RestController
@RequestMapping("/api")
public class MemberRiteResource {

    private final Logger log = LoggerFactory.getLogger(MemberRiteResource.class);

    private static final String ENTITY_NAME = "memberRite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberRiteService memberRiteService;

    private final MemberRiteQueryService memberRiteQueryService;

    public MemberRiteResource(MemberRiteService memberRiteService, MemberRiteQueryService memberRiteQueryService) {
        this.memberRiteService = memberRiteService;
        this.memberRiteQueryService = memberRiteQueryService;
    }

    /**
     * {@code POST  /member-rites} : Create a new memberRite.
     *
     * @param memberRite the memberRite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberRite, or with status {@code 400 (Bad Request)} if the memberRite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-rites")
    public ResponseEntity<MemberRite> createMemberRite(@Valid @RequestBody MemberRite memberRite) throws URISyntaxException {
        log.debug("REST request to save MemberRite : {}", memberRite);
        if (memberRite.getId() != null) {
            throw new BadRequestAlertException("A new memberRite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberRite result = memberRiteService.save(memberRite);
        return ResponseEntity.created(new URI("/api/member-rites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-rites} : Updates an existing memberRite.
     *
     * @param memberRite the memberRite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberRite,
     * or with status {@code 400 (Bad Request)} if the memberRite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberRite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-rites")
    public ResponseEntity<MemberRite> updateMemberRite(@Valid @RequestBody MemberRite memberRite) throws URISyntaxException {
        log.debug("REST request to update MemberRite : {}", memberRite);
        if (memberRite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemberRite result = memberRiteService.save(memberRite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberRite.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-rites} : get all the memberRites.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberRites in body.
     */
    @GetMapping("/member-rites")
    public ResponseEntity<List<MemberRite>> getAllMemberRites(MemberRiteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MemberRites by criteria: {}", criteria);
        Page<MemberRite> page = memberRiteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /member-rites/count} : count all the memberRites.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/member-rites/count")
    public ResponseEntity<Long> countMemberRites(MemberRiteCriteria criteria) {
        log.debug("REST request to count MemberRites by criteria: {}", criteria);
        return ResponseEntity.ok().body(memberRiteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /member-rites/:id} : get the "id" memberRite.
     *
     * @param id the id of the memberRite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberRite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-rites/{id}")
    public ResponseEntity<MemberRite> getMemberRite(@PathVariable Long id) {
        log.debug("REST request to get MemberRite : {}", id);
        Optional<MemberRite> memberRite = memberRiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberRite);
    }

    /**
     * {@code DELETE  /member-rites/:id} : delete the "id" memberRite.
     *
     * @param id the id of the memberRite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-rites/{id}")
    public ResponseEntity<Void> deleteMemberRite(@PathVariable Long id) {
        log.debug("REST request to delete MemberRite : {}", id);
        memberRiteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
