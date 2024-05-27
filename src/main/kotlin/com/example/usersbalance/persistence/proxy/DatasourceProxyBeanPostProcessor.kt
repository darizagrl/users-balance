package com.example.usersbalance.persistence.proxy

import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DatasourceProxyBeanPostProcessor : BeanPostProcessor {

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        return bean
    }

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is DataSource) {
            return ProxyDataSourceBuilder.create(bean).logQueryBySlf4j(SLF4JLogLevel.INFO).build()
        }
        return bean
    }
}