package com.example.javafilmoratekotlin.view

import org.springframework.stereotype.Component

@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
    override val type = DocumentViewType.HTML
    override fun generate(request: DocumentationSources): String {
        TODO("Not yet implemented")
    }
}