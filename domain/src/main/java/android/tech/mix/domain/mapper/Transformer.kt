package android.tech.mix.domain.mapper

/**
* This abstraction interface serves for executing common functionalities in converting the data layer model into
* the domain layer model. This domain layer model will then be employed within the presentation layer.
* @param FromObject Data layer model (e.g., network or local database model)
* @param ToObject Domain layer model (e.g., UI model or business logic model)
 */

interface Transformer<in FromObject, ToObject> {

    fun mapObject(fromObject: FromObject): ToObject
}