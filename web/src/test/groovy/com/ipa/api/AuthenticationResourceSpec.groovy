package com.ipa.api

import com.ipa.App
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.session.MapSessionRepository
import org.springframework.session.web.http.HeaderHttpSessionStrategy
import org.springframework.session.web.http.SessionRepositoryFilter
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@ContextConfiguration(
        loader = SpringBootContextLoader,
        classes = [App]
)
@WebAppConfiguration
@Stepwise
class AuthenticationResourceSpec extends Specification {

    @Shared
    String token

    @Shared
    private def sessionRepository = new MapSessionRepository()

    @Autowired
    private WebApplicationContext wac
    private MockMvc mvc
    private static final JsonSlurper slurper = new JsonSlurper()

    def setup() {
        def sessionFilter = new SessionRepositoryFilter(sessionRepository)
        sessionFilter.httpSessionStrategy = new HeaderHttpSessionStrategy()

        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilter(sessionFilter)
        .build()
    }

    def "bad authentication"() {
        given:
        def credentials = [username: 'user', password: 'badpassword']

        when:
        def res = mvc.perform(MockMvcRequestBuilders.post('/api/session')
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonOutput.toJson(credentials)))
        res.andReturn().response.status
        then:
        res.andReturn().response.status == HttpStatus.UNAUTHORIZED.value()
    }

    def "good authentication"() {
        given:
        def credentials = [username: 'admin', password: 'batut4crazy']

        when:
        def res = mvc.perform(
                MockMvcRequestBuilders.post('/api/session')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonOutput.toJson(credentials))
        )

        def json = slurper.parseText(res.andReturn().response.getContentAsString())

        token = json.token

        then:
        res.andReturn().response.status == HttpStatus.OK.value()
        json.userName == 'admin'
        token != null
    }

    def "get session"() {
        when:
        def res = mvc.perform(
                MockMvcRequestBuilders.get("/api/session")
                .header('X-Auth-Token', token)
        )

        def json = slurper.parseText(res.andReturn().response.getContentAsString())

        then:
        res.andReturn().response.status == HttpStatus.OK.value()
        json.userName == 'admin'
    }


}

