package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.MemberRite;
import org.church.domain.Rite;
import org.church.domain.Member;
import org.church.domain.Church;
import org.church.repository.MemberRiteRepository;
import org.church.service.MemberRiteService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.MemberRiteCriteria;
import org.church.service.MemberRiteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MemberRiteResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class MemberRiteResourceIT {

    private static final LocalDate DEFAULT_DATE_RECEIVED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEIVED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_RECEIVED = LocalDate.ofEpochDay(-1L);

    @Autowired
    private MemberRiteRepository memberRiteRepository;

    @Autowired
    private MemberRiteService memberRiteService;

    @Autowired
    private MemberRiteQueryService memberRiteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMemberRiteMockMvc;

    private MemberRite memberRite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberRiteResource memberRiteResource = new MemberRiteResource(memberRiteService, memberRiteQueryService);
        this.restMemberRiteMockMvc = MockMvcBuilders.standaloneSetup(memberRiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberRite createEntity(EntityManager em) {
        MemberRite memberRite = new MemberRite()
            .dateReceived(DEFAULT_DATE_RECEIVED);
        // Add required entity
        Rite rite;
        if (TestUtil.findAll(em, Rite.class).isEmpty()) {
            rite = RiteResourceIT.createEntity(em);
            em.persist(rite);
            em.flush();
        } else {
            rite = TestUtil.findAll(em, Rite.class).get(0);
        }
        memberRite.setRite(rite);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        memberRite.setChurch(church);
        return memberRite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberRite createUpdatedEntity(EntityManager em) {
        MemberRite memberRite = new MemberRite()
            .dateReceived(UPDATED_DATE_RECEIVED);
        // Add required entity
        Rite rite;
        if (TestUtil.findAll(em, Rite.class).isEmpty()) {
            rite = RiteResourceIT.createUpdatedEntity(em);
            em.persist(rite);
            em.flush();
        } else {
            rite = TestUtil.findAll(em, Rite.class).get(0);
        }
        memberRite.setRite(rite);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        memberRite.setChurch(church);
        return memberRite;
    }

    @BeforeEach
    public void initTest() {
        memberRite = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberRite() throws Exception {
        int databaseSizeBeforeCreate = memberRiteRepository.findAll().size();

        // Create the MemberRite
        restMemberRiteMockMvc.perform(post("/api/member-rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRite)))
            .andExpect(status().isCreated());

        // Validate the MemberRite in the database
        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeCreate + 1);
        MemberRite testMemberRite = memberRiteList.get(memberRiteList.size() - 1);
        assertThat(testMemberRite.getDateReceived()).isEqualTo(DEFAULT_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void createMemberRiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRiteRepository.findAll().size();

        // Create the MemberRite with an existing ID
        memberRite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberRiteMockMvc.perform(post("/api/member-rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRite)))
            .andExpect(status().isBadRequest());

        // Validate the MemberRite in the database
        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRiteRepository.findAll().size();
        // set the field null
        memberRite.setDateReceived(null);

        // Create the MemberRite, which fails.

        restMemberRiteMockMvc.perform(post("/api/member-rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRite)))
            .andExpect(status().isBadRequest());

        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberRites() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList
        restMemberRiteMockMvc.perform(get("/api/member-rites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberRite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())));
    }
    
    @Test
    @Transactional
    public void getMemberRite() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get the memberRite
        restMemberRiteMockMvc.perform(get("/api/member-rites/{id}", memberRite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(memberRite.getId().intValue()))
            .andExpect(jsonPath("$.dateReceived").value(DEFAULT_DATE_RECEIVED.toString()));
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived equals to DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.equals=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.equals=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived not equals to DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.notEquals=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived not equals to UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.notEquals=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived in DEFAULT_DATE_RECEIVED or UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.in=" + DEFAULT_DATE_RECEIVED + "," + UPDATED_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.in=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived is not null
        defaultMemberRiteShouldBeFound("dateReceived.specified=true");

        // Get all the memberRiteList where dateReceived is null
        defaultMemberRiteShouldNotBeFound("dateReceived.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived is greater than or equal to DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.greaterThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived is greater than or equal to UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.greaterThanOrEqual=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived is less than or equal to DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.lessThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived is less than or equal to SMALLER_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.lessThanOrEqual=" + SMALLER_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsLessThanSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived is less than DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.lessThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived is less than UPDATED_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.lessThan=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMemberRitesByDateReceivedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);

        // Get all the memberRiteList where dateReceived is greater than DEFAULT_DATE_RECEIVED
        defaultMemberRiteShouldNotBeFound("dateReceived.greaterThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the memberRiteList where dateReceived is greater than SMALLER_DATE_RECEIVED
        defaultMemberRiteShouldBeFound("dateReceived.greaterThan=" + SMALLER_DATE_RECEIVED);
    }


    @Test
    @Transactional
    public void getAllMemberRitesByRiteIsEqualToSomething() throws Exception {
        // Get already existing entity
        Rite rite = memberRite.getRite();
        memberRiteRepository.saveAndFlush(memberRite);
        Long riteId = rite.getId();

        // Get all the memberRiteList where rite equals to riteId
        defaultMemberRiteShouldBeFound("riteId.equals=" + riteId);

        // Get all the memberRiteList where rite equals to riteId + 1
        defaultMemberRiteShouldNotBeFound("riteId.equals=" + (riteId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberRitesByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRiteRepository.saveAndFlush(memberRite);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        memberRite.setMember(member);
        memberRiteRepository.saveAndFlush(memberRite);
        Long memberId = member.getId();

        // Get all the memberRiteList where member equals to memberId
        defaultMemberRiteShouldBeFound("memberId.equals=" + memberId);

        // Get all the memberRiteList where member equals to memberId + 1
        defaultMemberRiteShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberRitesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = memberRite.getChurch();
        memberRiteRepository.saveAndFlush(memberRite);
        Long churchId = church.getId();

        // Get all the memberRiteList where church equals to churchId
        defaultMemberRiteShouldBeFound("churchId.equals=" + churchId);

        // Get all the memberRiteList where church equals to churchId + 1
        defaultMemberRiteShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberRiteShouldBeFound(String filter) throws Exception {
        restMemberRiteMockMvc.perform(get("/api/member-rites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberRite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())));

        // Check, that the count call also returns 1
        restMemberRiteMockMvc.perform(get("/api/member-rites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberRiteShouldNotBeFound(String filter) throws Exception {
        restMemberRiteMockMvc.perform(get("/api/member-rites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberRiteMockMvc.perform(get("/api/member-rites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMemberRite() throws Exception {
        // Get the memberRite
        restMemberRiteMockMvc.perform(get("/api/member-rites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberRite() throws Exception {
        // Initialize the database
        memberRiteService.save(memberRite);

        int databaseSizeBeforeUpdate = memberRiteRepository.findAll().size();

        // Update the memberRite
        MemberRite updatedMemberRite = memberRiteRepository.findById(memberRite.getId()).get();
        // Disconnect from session so that the updates on updatedMemberRite are not directly saved in db
        em.detach(updatedMemberRite);
        updatedMemberRite
            .dateReceived(UPDATED_DATE_RECEIVED);

        restMemberRiteMockMvc.perform(put("/api/member-rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMemberRite)))
            .andExpect(status().isOk());

        // Validate the MemberRite in the database
        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeUpdate);
        MemberRite testMemberRite = memberRiteList.get(memberRiteList.size() - 1);
        assertThat(testMemberRite.getDateReceived()).isEqualTo(UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberRite() throws Exception {
        int databaseSizeBeforeUpdate = memberRiteRepository.findAll().size();

        // Create the MemberRite

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberRiteMockMvc.perform(put("/api/member-rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRite)))
            .andExpect(status().isBadRequest());

        // Validate the MemberRite in the database
        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemberRite() throws Exception {
        // Initialize the database
        memberRiteService.save(memberRite);

        int databaseSizeBeforeDelete = memberRiteRepository.findAll().size();

        // Delete the memberRite
        restMemberRiteMockMvc.perform(delete("/api/member-rites/{id}", memberRite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberRite> memberRiteList = memberRiteRepository.findAll();
        assertThat(memberRiteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberRite.class);
        MemberRite memberRite1 = new MemberRite();
        memberRite1.setId(1L);
        MemberRite memberRite2 = new MemberRite();
        memberRite2.setId(memberRite1.getId());
        assertThat(memberRite1).isEqualTo(memberRite2);
        memberRite2.setId(2L);
        assertThat(memberRite1).isNotEqualTo(memberRite2);
        memberRite1.setId(null);
        assertThat(memberRite1).isNotEqualTo(memberRite2);
    }
}
