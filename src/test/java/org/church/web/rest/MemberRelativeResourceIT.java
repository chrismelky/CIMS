package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.MemberRelative;
import org.church.domain.Member;
import org.church.repository.MemberRelativeRepository;
import org.church.service.MemberRelativeService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.MemberRelativeCriteria;
import org.church.service.MemberRelativeQueryService;

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
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.church.domain.enumeration.RelativeType;
/**
 * Integration tests for the {@link MemberRelativeResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class MemberRelativeResourceIT {

    private static final RelativeType DEFAULT_RELATIVE_TYPE = RelativeType.FATHER;
    private static final RelativeType UPDATED_RELATIVE_TYPE = RelativeType.MOTHER;

    private static final String DEFAULT_RELATIVE_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RELATIVE_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIVE_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RELATIVE_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private MemberRelativeRepository memberRelativeRepository;

    @Autowired
    private MemberRelativeService memberRelativeService;

    @Autowired
    private MemberRelativeQueryService memberRelativeQueryService;

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

    private MockMvc restMemberRelativeMockMvc;

    private MemberRelative memberRelative;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberRelativeResource memberRelativeResource = new MemberRelativeResource(memberRelativeService, memberRelativeQueryService);
        this.restMemberRelativeMockMvc = MockMvcBuilders.standaloneSetup(memberRelativeResource)
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
    public static MemberRelative createEntity(EntityManager em) {
        MemberRelative memberRelative = new MemberRelative()
            .relativeType(DEFAULT_RELATIVE_TYPE)
            .relativeFullName(DEFAULT_RELATIVE_FULL_NAME)
            .relativePhoneNumber(DEFAULT_RELATIVE_PHONE_NUMBER);
        return memberRelative;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberRelative createUpdatedEntity(EntityManager em) {
        MemberRelative memberRelative = new MemberRelative()
            .relativeType(UPDATED_RELATIVE_TYPE)
            .relativeFullName(UPDATED_RELATIVE_FULL_NAME)
            .relativePhoneNumber(UPDATED_RELATIVE_PHONE_NUMBER);
        return memberRelative;
    }

    @BeforeEach
    public void initTest() {
        memberRelative = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberRelative() throws Exception {
        int databaseSizeBeforeCreate = memberRelativeRepository.findAll().size();

        // Create the MemberRelative
        restMemberRelativeMockMvc.perform(post("/api/member-relatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRelative)))
            .andExpect(status().isCreated());

        // Validate the MemberRelative in the database
        List<MemberRelative> memberRelativeList = memberRelativeRepository.findAll();
        assertThat(memberRelativeList).hasSize(databaseSizeBeforeCreate + 1);
        MemberRelative testMemberRelative = memberRelativeList.get(memberRelativeList.size() - 1);
        assertThat(testMemberRelative.getRelativeType()).isEqualTo(DEFAULT_RELATIVE_TYPE);
        assertThat(testMemberRelative.getRelativeFullName()).isEqualTo(DEFAULT_RELATIVE_FULL_NAME);
        assertThat(testMemberRelative.getRelativePhoneNumber()).isEqualTo(DEFAULT_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createMemberRelativeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRelativeRepository.findAll().size();

        // Create the MemberRelative with an existing ID
        memberRelative.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberRelativeMockMvc.perform(post("/api/member-relatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRelative)))
            .andExpect(status().isBadRequest());

        // Validate the MemberRelative in the database
        List<MemberRelative> memberRelativeList = memberRelativeRepository.findAll();
        assertThat(memberRelativeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMemberRelatives() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList
        restMemberRelativeMockMvc.perform(get("/api/member-relatives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberRelative.getId().intValue())))
            .andExpect(jsonPath("$.[*].relativeType").value(hasItem(DEFAULT_RELATIVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].relativeFullName").value(hasItem(DEFAULT_RELATIVE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].relativePhoneNumber").value(hasItem(DEFAULT_RELATIVE_PHONE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getMemberRelative() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get the memberRelative
        restMemberRelativeMockMvc.perform(get("/api/member-relatives/{id}", memberRelative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(memberRelative.getId().intValue()))
            .andExpect(jsonPath("$.relativeType").value(DEFAULT_RELATIVE_TYPE.toString()))
            .andExpect(jsonPath("$.relativeFullName").value(DEFAULT_RELATIVE_FULL_NAME))
            .andExpect(jsonPath("$.relativePhoneNumber").value(DEFAULT_RELATIVE_PHONE_NUMBER));
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeType equals to DEFAULT_RELATIVE_TYPE
        defaultMemberRelativeShouldBeFound("relativeType.equals=" + DEFAULT_RELATIVE_TYPE);

        // Get all the memberRelativeList where relativeType equals to UPDATED_RELATIVE_TYPE
        defaultMemberRelativeShouldNotBeFound("relativeType.equals=" + UPDATED_RELATIVE_TYPE);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeType not equals to DEFAULT_RELATIVE_TYPE
        defaultMemberRelativeShouldNotBeFound("relativeType.notEquals=" + DEFAULT_RELATIVE_TYPE);

        // Get all the memberRelativeList where relativeType not equals to UPDATED_RELATIVE_TYPE
        defaultMemberRelativeShouldBeFound("relativeType.notEquals=" + UPDATED_RELATIVE_TYPE);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeType in DEFAULT_RELATIVE_TYPE or UPDATED_RELATIVE_TYPE
        defaultMemberRelativeShouldBeFound("relativeType.in=" + DEFAULT_RELATIVE_TYPE + "," + UPDATED_RELATIVE_TYPE);

        // Get all the memberRelativeList where relativeType equals to UPDATED_RELATIVE_TYPE
        defaultMemberRelativeShouldNotBeFound("relativeType.in=" + UPDATED_RELATIVE_TYPE);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeType is not null
        defaultMemberRelativeShouldBeFound("relativeType.specified=true");

        // Get all the memberRelativeList where relativeType is null
        defaultMemberRelativeShouldNotBeFound("relativeType.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName equals to DEFAULT_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldBeFound("relativeFullName.equals=" + DEFAULT_RELATIVE_FULL_NAME);

        // Get all the memberRelativeList where relativeFullName equals to UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldNotBeFound("relativeFullName.equals=" + UPDATED_RELATIVE_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName not equals to DEFAULT_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldNotBeFound("relativeFullName.notEquals=" + DEFAULT_RELATIVE_FULL_NAME);

        // Get all the memberRelativeList where relativeFullName not equals to UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldBeFound("relativeFullName.notEquals=" + UPDATED_RELATIVE_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName in DEFAULT_RELATIVE_FULL_NAME or UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldBeFound("relativeFullName.in=" + DEFAULT_RELATIVE_FULL_NAME + "," + UPDATED_RELATIVE_FULL_NAME);

        // Get all the memberRelativeList where relativeFullName equals to UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldNotBeFound("relativeFullName.in=" + UPDATED_RELATIVE_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName is not null
        defaultMemberRelativeShouldBeFound("relativeFullName.specified=true");

        // Get all the memberRelativeList where relativeFullName is null
        defaultMemberRelativeShouldNotBeFound("relativeFullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameContainsSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName contains DEFAULT_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldBeFound("relativeFullName.contains=" + DEFAULT_RELATIVE_FULL_NAME);

        // Get all the memberRelativeList where relativeFullName contains UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldNotBeFound("relativeFullName.contains=" + UPDATED_RELATIVE_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativeFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativeFullName does not contain DEFAULT_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldNotBeFound("relativeFullName.doesNotContain=" + DEFAULT_RELATIVE_FULL_NAME);

        // Get all the memberRelativeList where relativeFullName does not contain UPDATED_RELATIVE_FULL_NAME
        defaultMemberRelativeShouldBeFound("relativeFullName.doesNotContain=" + UPDATED_RELATIVE_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber equals to DEFAULT_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.equals=" + DEFAULT_RELATIVE_PHONE_NUMBER);

        // Get all the memberRelativeList where relativePhoneNumber equals to UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.equals=" + UPDATED_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber not equals to DEFAULT_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.notEquals=" + DEFAULT_RELATIVE_PHONE_NUMBER);

        // Get all the memberRelativeList where relativePhoneNumber not equals to UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.notEquals=" + UPDATED_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber in DEFAULT_RELATIVE_PHONE_NUMBER or UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.in=" + DEFAULT_RELATIVE_PHONE_NUMBER + "," + UPDATED_RELATIVE_PHONE_NUMBER);

        // Get all the memberRelativeList where relativePhoneNumber equals to UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.in=" + UPDATED_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber is not null
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.specified=true");

        // Get all the memberRelativeList where relativePhoneNumber is null
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber contains DEFAULT_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.contains=" + DEFAULT_RELATIVE_PHONE_NUMBER);

        // Get all the memberRelativeList where relativePhoneNumber contains UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.contains=" + UPDATED_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMemberRelativesByRelativePhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);

        // Get all the memberRelativeList where relativePhoneNumber does not contain DEFAULT_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldNotBeFound("relativePhoneNumber.doesNotContain=" + DEFAULT_RELATIVE_PHONE_NUMBER);

        // Get all the memberRelativeList where relativePhoneNumber does not contain UPDATED_RELATIVE_PHONE_NUMBER
        defaultMemberRelativeShouldBeFound("relativePhoneNumber.doesNotContain=" + UPDATED_RELATIVE_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMemberRelativesByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRelativeRepository.saveAndFlush(memberRelative);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        memberRelative.setMember(member);
        memberRelativeRepository.saveAndFlush(memberRelative);
        Long memberId = member.getId();

        // Get all the memberRelativeList where member equals to memberId
        defaultMemberRelativeShouldBeFound("memberId.equals=" + memberId);

        // Get all the memberRelativeList where member equals to memberId + 1
        defaultMemberRelativeShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberRelativeShouldBeFound(String filter) throws Exception {
        restMemberRelativeMockMvc.perform(get("/api/member-relatives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberRelative.getId().intValue())))
            .andExpect(jsonPath("$.[*].relativeType").value(hasItem(DEFAULT_RELATIVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].relativeFullName").value(hasItem(DEFAULT_RELATIVE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].relativePhoneNumber").value(hasItem(DEFAULT_RELATIVE_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restMemberRelativeMockMvc.perform(get("/api/member-relatives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberRelativeShouldNotBeFound(String filter) throws Exception {
        restMemberRelativeMockMvc.perform(get("/api/member-relatives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberRelativeMockMvc.perform(get("/api/member-relatives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMemberRelative() throws Exception {
        // Get the memberRelative
        restMemberRelativeMockMvc.perform(get("/api/member-relatives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberRelative() throws Exception {
        // Initialize the database
        memberRelativeService.save(memberRelative);

        int databaseSizeBeforeUpdate = memberRelativeRepository.findAll().size();

        // Update the memberRelative
        MemberRelative updatedMemberRelative = memberRelativeRepository.findById(memberRelative.getId()).get();
        // Disconnect from session so that the updates on updatedMemberRelative are not directly saved in db
        em.detach(updatedMemberRelative);
        updatedMemberRelative
            .relativeType(UPDATED_RELATIVE_TYPE)
            .relativeFullName(UPDATED_RELATIVE_FULL_NAME)
            .relativePhoneNumber(UPDATED_RELATIVE_PHONE_NUMBER);

        restMemberRelativeMockMvc.perform(put("/api/member-relatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMemberRelative)))
            .andExpect(status().isOk());

        // Validate the MemberRelative in the database
        List<MemberRelative> memberRelativeList = memberRelativeRepository.findAll();
        assertThat(memberRelativeList).hasSize(databaseSizeBeforeUpdate);
        MemberRelative testMemberRelative = memberRelativeList.get(memberRelativeList.size() - 1);
        assertThat(testMemberRelative.getRelativeType()).isEqualTo(UPDATED_RELATIVE_TYPE);
        assertThat(testMemberRelative.getRelativeFullName()).isEqualTo(UPDATED_RELATIVE_FULL_NAME);
        assertThat(testMemberRelative.getRelativePhoneNumber()).isEqualTo(UPDATED_RELATIVE_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberRelative() throws Exception {
        int databaseSizeBeforeUpdate = memberRelativeRepository.findAll().size();

        // Create the MemberRelative

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberRelativeMockMvc.perform(put("/api/member-relatives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberRelative)))
            .andExpect(status().isBadRequest());

        // Validate the MemberRelative in the database
        List<MemberRelative> memberRelativeList = memberRelativeRepository.findAll();
        assertThat(memberRelativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemberRelative() throws Exception {
        // Initialize the database
        memberRelativeService.save(memberRelative);

        int databaseSizeBeforeDelete = memberRelativeRepository.findAll().size();

        // Delete the memberRelative
        restMemberRelativeMockMvc.perform(delete("/api/member-relatives/{id}", memberRelative.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberRelative> memberRelativeList = memberRelativeRepository.findAll();
        assertThat(memberRelativeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberRelative.class);
        MemberRelative memberRelative1 = new MemberRelative();
        memberRelative1.setId(1L);
        MemberRelative memberRelative2 = new MemberRelative();
        memberRelative2.setId(memberRelative1.getId());
        assertThat(memberRelative1).isEqualTo(memberRelative2);
        memberRelative2.setId(2L);
        assertThat(memberRelative1).isNotEqualTo(memberRelative2);
        memberRelative1.setId(null);
        assertThat(memberRelative1).isNotEqualTo(memberRelative2);
    }
}
