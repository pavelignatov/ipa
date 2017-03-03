package com.ipa.auth

import com.ipa.App
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity


@ContextConfiguration(
        loader = SpringBootContextLoader,
        classes = [App]
)
@WebAppConfiguration
class AuthenticationSpec extends Specification {
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

    def "unauthenticated users cannot get resource"() {
        when:
        def res = mvc.perform(MockMvcRequestBuilders.get("/api/simple"))

        then:
        res.andReturn().response.status == HttpStatus.FORBIDDEN.value()
    }

    @WithMockUser
    def "authenticated users can get resource"() {
        when:
        def res = mvc.perform(MockMvcRequestBuilders.get("/api/simple"))

        then:
        res.andReturn().response.status == HttpStatus.OK.value()
    }
}
