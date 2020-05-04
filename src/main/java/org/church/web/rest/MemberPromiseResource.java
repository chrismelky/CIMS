package org.church.web.rest;

import org.church.domain.MemberPromise;
import org.church.service.MemberPromiseService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.MemberPromiseCriteria;
import org.church.service.MemberPromiseQueryService;

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
 * REST controller for managing {@link org.church.domain.MemberPromise}.
 */
@RestController
@RequestMapping("/api")
public class MemberPromiseResource {

    private final Logger log = LoggerFactory.getLogger(MemberPromiseResource.class);

    private static final String ENTITY_NAME = "memberPromise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberPromiseService memberPromiseService;

    private final MemberPromiseQueryService memberPromiseQueryService;

    public MemberPromiseResource(MemberPromiseService memberPromiseService, MemberPromiseQueryService memberPromiseQueryService) {
        this.memberPromiseService = memberPromiseService;
        this.memberPromiseQueryService = memberPromiseQueryService;
    }

    /**
     * {@code POST  /member-promises} : Create a new memberPromise.
     *
     * @param memberPromise the memberPromise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberPromise, or with status {@code 400 (Bad Request)} if the memberPromise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-promises")
    public ResponseEntity<MemberPromise> createMemberPromise(@Valid @RequestBody MemberPromise memberPromise) throws URISyntaxException {
        log.debug("REST request to save MemberPromise : {}", memberPromise);
        if (memberPromise.getId() != null) {
            throw new BadRequestAlertException("A new memberPromise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberPromise result = memberPromiseService.save(memberPromise);
        return ResponseEntity.created(new URI("/api/member-promises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-promises} : Updates an existing memberPromise.
     *
     * @param memberPromise the memberPromise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberPromise,
     * or with status {@code 400 (Bad Request)} if the memberPromise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberPromise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-promises")
    public ResponseEntity<MemberPromise> updateMemberPromise(@Valid @RequestBody MemberPromise memberPromise) throws URISyntaxException {
        log.debug("REST request to update MemberPromise : {}", memberPromise);
        if (memberPromise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemberPromise result = memberPromiseService.save(memberPromise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberPromise.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-promises} : get all the memberPromises.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberPromises in body.
     */
    @GetMapping("/member-promises")
    public ResponseEntity<List<MemberPromise>> getAllMemberPromises(MemberPromiseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MemberPromises by criteria: {}", criteria);
        Page<MemberPromise> page = memberPromiseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/member-promises/{churchId}/{memberId}/{typeId}/{fyId}")
    public ResponseEntity<MemberPromise> getOne(
        @PathVariable Long churchId,
        @PathVariable Long memberId,
        @PathVariable Long typeId,
        @PathVariable Long fyId
    ) {
        log.debug("REST request to count MemberPromises by criteria: {}", fyId);
        return ResponseUtil.wrapOrNotFound(memberPromiseService.findOne(churchId, memberId, typeId, fyId));
    }

    /**
     * {@code GET  /member-promises/:id} : get the "id" memberPromise.
     *
     * @param id the id of the memberPromise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberPromise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-promises/{id}")
    public ResponseEntity<MemberPromise> getMemberPromise(@PathVariable Long id) {
        log.debug("REST request to get MemberPromise : {}", id);
        Optional<MemberPromise> memberPromise = memberPromiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberPromise);
    }

    /**
     * {@code DELETE  /member-promises/:id} : delete the "id" memberPromise.
     *
     * @param id the id of the memberPromise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-promises/{id}")
    public ResponseEntity<Void> deleteMemberPromise(@PathVariable Long id) {
        log.debug("REST request to delete MemberPromise : {}", id);
        memberPromiseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
