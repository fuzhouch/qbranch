package net.dummydigit.qbranch.annotations

/**
 * Annotation to tag a Bond field is required to assign value.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ContainerElementCreatorField(val name : String)