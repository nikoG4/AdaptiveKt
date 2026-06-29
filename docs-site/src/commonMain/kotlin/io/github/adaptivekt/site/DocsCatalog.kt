package io.github.adaptivekt.site

internal object DocsCatalog {
    fun components(): List<ComponentDoc> = componentDocs()
    fun topics(): List<DocsTopic> = docsTopics()
}
