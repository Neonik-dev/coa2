package lisval.service1.config

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurerAdapter
import org.springframework.ws.transport.http.MessageDispatcherServlet
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition
import org.springframework.xml.xsd.SimpleXsdSchema
import org.springframework.xml.xsd.XsdSchema

@EnableWs
@Configuration
class WebServiceConfig : WsConfigurerAdapter() {

    @Bean
    fun messageDispatcherServlet(applicationContext: ApplicationContext): ServletRegistrationBean<MessageDispatcherServlet> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(applicationContext)
        servlet.setTransformWsdlLocations(true)
        return ServletRegistrationBean(servlet, "/ws/*")
    }

    @Bean(name = ["persons"])
    fun defaultWsdl11Definition(schema: XsdSchema): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("PersonsPort")
        wsdl11Definition.setLocationUri("/ws")
        wsdl11Definition.setTargetNamespace("http://localhost/api/persons")
        wsdl11Definition.setSchema(schema)
        return wsdl11Definition
    }

    @Bean
    fun personsSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("xsd/persons.xsd"))
    }
}