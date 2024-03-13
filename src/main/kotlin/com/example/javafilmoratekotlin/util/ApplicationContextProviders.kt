package com.example.javafilmoratekotlin.util

import com.example.javafilmoratekotlin.FilmorateKotlinApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component


@Component
class ApplicationContextProviders() : ApplicationContextAware {


    companion object {

        private lateinit var applicationContext: ApplicationContext

        fun getApplicationContext(): ApplicationContext {
            return applicationContext
        }

        /*регул€рка дл€ удалени€ лишних скобок*/
        val deleteBrackets = """[{}\]\[]""".toRegex()

        /*
        * регул€рка дл€ удалени€ лишних эндпоинтов*/
        val deleteExtraEndpoints = Regex("(/swagger-ui.html)|(/error)|(/v3/)")
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationContextProviders.applicationContext = applicationContext
    }

}



