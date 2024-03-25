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

        /*Инциализация контекста приложения*/
        private lateinit var applicationContext: ApplicationContext

        fun getApplicationContext(): ApplicationContext {
            return applicationContext
        }

        /*регулярка для удаления лишних скобок в выводе endpointa*/
        val deleteBrackets = """[{}\]\[]""".toRegex()

        /*
        * регулярка для удаления лишних эндпоинтов*/
        val deleteExtraEndpoints = Regex("(/swagger-ui.html)|(/error)|(/v3/)")
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationContextProviders.applicationContext = applicationContext
    }

}



